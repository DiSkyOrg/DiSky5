package net.itsthesky.disky5.processor.writers;

import java.util.Objects;
import net.itsthesky.disky5.processor.util.Versions;
import com.squareup.javapoet.*;
import net.itsthesky.disky5.processor.DiSkyProcessor.SharedContribution;
import net.itsthesky.disky5.processor.DiSkyProcessor.SharedDoc;
import net.itsthesky.disky5.processor.util.NameUtils;
import net.itsthesky.disky5.processor.util.SkriptTypes;
import net.itsthesky.disky5.processor.util.TypeUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TypePropertyWriter {
    private TypePropertyWriter() {}

    public static final String PACKAGE = "net.itsthesky.disky5.generated";

    private record Builtin(String constant, ClassName handler, String fallback) {
        Builtin(String constant, ClassName handler) { this(constant, handler, null); }
    }

    private static final Map<String, Builtin> BUILTINS = new LinkedHashMap<>();
    static {
        BUILTINS.put("name", new Builtin("NAME", SkriptTypes.EXPRESSION_PROPERTY_HANDLER));
        BUILTINS.put("display name", new Builtin("DISPLAY_NAME", SkriptTypes.EXPRESSION_PROPERTY_HANDLER));
        BUILTINS.put("nickname", new Builtin("DISPLAY_NAME", SkriptTypes.EXPRESSION_PROPERTY_HANDLER));
        BUILTINS.put("amount", new Builtin("AMOUNT", SkriptTypes.EXPRESSION_PROPERTY_HANDLER));
        BUILTINS.put("size", new Builtin("SIZE", SkriptTypes.EXPRESSION_PROPERTY_HANDLER));
        BUILTINS.put("number", new Builtin("NUMBER", SkriptTypes.EXPRESSION_PROPERTY_HANDLER));
        // Property.COLOR only exists in Skript builds carrying the type-properties color PR: resolved
        // reflectively, with a "discord color" property of our own when it is missing.
        BUILTINS.put("color", new Builtin("COLOR", SkriptTypes.EXPRESSION_PROPERTY_HANDLER, "discord color"));
        BUILTINS.put("title", new Builtin("TITLE", SkriptTypes.EXPRESSION_PROPERTY_HANDLER));
        BUILTINS.put("style", new Builtin("STYLE", SkriptTypes.EXPRESSION_PROPERTY_HANDLER));
        BUILTINS.put("value", new Builtin("TYPED_VALUE", SkriptTypes.TYPED_VALUE_HANDLER));
    }

    private static final ParameterizedTypeName HANDLER_WILDCARD = ParameterizedTypeName.get(
        SkriptTypes.EXPRESSION_PROPERTY_HANDLER,
        WildcardTypeName.subtypeOf(Object.class), WildcardTypeName.subtypeOf(Object.class));
    private static final ParameterizedTypeName PROPERTY_OF_HANDLER = ParameterizedTypeName.get(
        SkriptTypes.PROPERTY, HANDLER_WILDCARD);

    public static void write(ProcessingEnvironment env,
                             Map<String, List<SharedContribution>> byName,
                             Map<String, SharedDoc> docs) throws IOException {
        MethodSpec.Builder reg = MethodSpec.methodBuilder("register")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(SkriptTypes.SKRIPT_ADDON, "addon");

        List<TypeSpec> exprClasses = new ArrayList<>();
        boolean needsLookup = false;
        int handlerSeq = 0;
        for (Map.Entry<String, List<SharedContribution>> entry : byName.entrySet()) {
            String name = entry.getKey();
            List<SharedContribution> group = entry.getValue();
            SharedDoc doc = docs.get(name);
            String description = describe(name, doc);
            String since = Objects.requireNonNullElse(Versions.first(doc == null ? null : doc.since()), Versions.defaultSince());

            Builtin builtin = BUILTINS.get(name);
            if (builtin != null && builtin.fallback() != null) {
                needsLookup = true;
                String local = "builtin" + NameUtils.toClassName(name);
                String fallbackClass = "GeneratedPropExpr_" + NameUtils.toClassName(builtin.fallback());
                reg.addCode("\n");
                reg.addStatement("$T $L = builtinProperty($S)", PROPERTY_OF_HANDLER, local, builtin.constant());
                reg.beginControlFlow("if ($L != null)", local);
                for (SharedContribution c : group)
                    emitHandler(reg, c, CodeBlock.of("$L", local), description, handlerSeq++, builtin.handler());
                reg.nextControlFlow("else");
                reg.addStatement("$T.register(addon)", ClassName.get(PACKAGE, fallbackClass));
                reg.addStatement("$T.getInstance().getLogger().info($S)", SkriptTypes.DISKY5,
                    "This Skript version has no native '" + name + "' property: use '"
                        + builtin.fallback() + " of ...' instead.");
                reg.endControlFlow();
                exprClasses.add(buildPropExprClass(fallbackClass, builtin.fallback(), description, since, group));
            } else if (builtin != null) {
                CodeBlock propertyRef = CodeBlock.of("$T.$L", SkriptTypes.PROPERTY, BUILTINS.get(name).constant());
                reg.addCode("\n");
                ClassName handlerType = BUILTINS.get(name).handler();
                for (SharedContribution c : group)
                    emitHandler(reg, c, propertyRef, description, handlerSeq++, handlerType);
            } else {
                String className = "GeneratedPropExpr_" + NameUtils.toClassName(name);
                reg.addStatement("$T.register(addon)", ClassName.get(PACKAGE, className));
                exprClasses.add(buildPropExprClass(className, name, description, since, group));
            }
        }

        TypeSpec.Builder cls = TypeSpec.classBuilder("GeneratedTypeProperties")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("Auto-generated by DiSkyProcessor - do not edit.\n"
                + "Registers shared / cross-cutting properties on Skript's native type-property system.\n")
            .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build())
            .addMethod(reg.build());
        if (needsLookup)
            cls.addMethod(MethodSpec.methodBuilder("builtinProperty")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unchecked").build())
                .returns(PROPERTY_OF_HANDLER)
                .addParameter(String.class, "constant")
                .beginControlFlow("try")
                .addStatement("return ($T) $T.class.getField(constant).get(null)", PROPERTY_OF_HANDLER, SkriptTypes.PROPERTY)
                .nextControlFlow("catch ($T e)", ReflectiveOperationException.class)
                .addStatement("return null")
                .endControlFlow()
                .build());

        JavaFile.builder(PACKAGE, cls.build()).build().writeTo(env.getFiler());
        for (TypeSpec exprClass : exprClasses)
            JavaFile.builder(PACKAGE, exprClass).build().writeTo(env.getFiler());
    }

    private static TypeSpec buildPropExprClass(String className, String name, String description,
                                               String since, List<SharedContribution> group) {
        ClassName self = ClassName.get(PACKAGE, className);

        FieldSpec property = FieldSpec.builder(PROPERTY_OF_HANDLER, "PROPERTY", Modifier.PRIVATE, Modifier.STATIC)
            .addJavadoc("Set by {@link #register}; read by {@link #getProperty()} during parse.\n")
            .build();

        MethodSpec getProperty = MethodSpec.methodBuilder("getProperty")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(PROPERTY_OF_HANDLER)
            .addStatement("return PROPERTY")
            .build();

        MethodSpec.Builder reg = MethodSpec.methodBuilder("register")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(SkriptTypes.SKRIPT_ADDON, "addon")
            .addJavadoc("Creates the custom property, registers it + its syntax, and attaches the\n"
                + "per-type handlers. Call once, inside a Skript registration window.\n")
            .addStatement("PROPERTY = $T.of($S, $S, $S, addon, $T.class)",
                SkriptTypes.PROPERTY, name, description, since, SkriptTypes.EXPRESSION_PROPERTY_HANDLER)
            .addStatement("$T.getAddonInstance().registry($T.class).register(PROPERTY)",
                SkriptTypes.SKRIPT, SkriptTypes.PROPERTY_REGISTRY)
            .addStatement("$T.registerExpression($T.class, $T.class, $T.PROPERTY, $S, $S)",
                SkriptTypes.SKRIPT, self, ClassName.get(Object.class), SkriptTypes.EXPRESSION_TYPE,
                "[the] " + name + " of %objects%", "%objects%'[s] " + name);

        reg.addCode("\n");
        CodeBlock propertyRef = CodeBlock.of("PROPERTY");
        int seq = 0;
        for (SharedContribution c : group)
            emitHandler(reg, c, propertyRef, description, seq++, SkriptTypes.EXPRESSION_PROPERTY_HANDLER);

        return TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(ParameterizedTypeName.get(SkriptTypes.PROPERTY_BASE_EXPRESSION, HANDLER_WILDCARD))
            .addJavadoc("Auto-generated by DiSkyProcessor - do not edit.\n"
                + "The $S property: $L of %objects%, dispatched per runtime type by Skript's native\n"
                + "type-property system.\n", name, name)
            .addField(property)
            .addMethod(getProperty)
            .addMethod(reg.build())
            .build();
    }

    private static void emitHandler(MethodSpec.Builder code, SharedContribution c,
                                    CodeBlock propertyRef, String description, int seq,
                                    ClassName handlerType) {
        ClassName returnClass = ClassName.bestGuess(TypeUtils.boxed(c.returnTypeBoxed()));
        String h = "h" + seq;
        if (c.settable()) {
            code.addStatement("$T<$T, $T> $L = $L",
                handlerType, c.paramType(), returnClass, h,
                buildSettableHandler(c, returnClass, handlerType));
        } else if (handlerType.equals(SkriptTypes.EXPRESSION_PROPERTY_HANDLER)) {
            code.addStatement("$T<$T, $T> $L = $T.of($T::$L, $T.class)",
                handlerType, c.paramType(), returnClass, h,
                SkriptTypes.EXPRESSION_PROPERTY_HANDLER, c.owner(), c.method(), returnClass);
        } else {
            code.addStatement("$T<$T, $T> $L = $L",
                handlerType, c.paramType(), returnClass, h,
                buildReadOnlyHandler(c, returnClass, handlerType));
        }
        code.addStatement("$T.getExactClassInfo($T.class).property($L, $S, addon, $L)",
            SkriptTypes.CLASSES, c.paramType(), propertyRef, description, h);
    }

    private static TypeSpec buildSettableHandler(SharedContribution c, ClassName returnClass,
                                                 ClassName handlerType) {
        ClassName param = c.paramType();
        ClassName valueClass = c.setterValueType();

        MethodSpec.Builder convertBuilder = MethodSpec.methodBuilder("convert")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(returnClass)
            .addParameter(param, "propertyHolder");
        if (c.method() == null)
            convertBuilder.addStatement("return null");
        else
            convertBuilder.addStatement("return $T.$L(propertyHolder)", c.owner(), c.method());
        MethodSpec convert = convertBuilder.build();

        MethodSpec returnType = MethodSpec.methodBuilder("returnType")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName.get(ClassName.get(Class.class), returnClass))
            .addStatement("return $T.class", returnClass)
            .build();

        MethodSpec acceptChange = MethodSpec.methodBuilder("acceptChange")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(Class[].class)
            .addParameter(SkriptTypes.CHANGER_MODE, "mode")
            .addStatement("if (mode == $T.SET) return new Class[]{ $T.class }", SkriptTypes.CHANGER_MODE, valueClass)
            .addStatement("return null")
            .build();

        MethodSpec.Builder change = MethodSpec.methodBuilder("change")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .addParameter(param, "propertyHolder")
            .addParameter(ArrayTypeName.of(Object.class), "delta")
            .addParameter(SkriptTypes.CHANGER_MODE, "mode")
            .beginControlFlow("if (mode != $T.SET)", SkriptTypes.CHANGER_MODE)
            .addStatement("return")
            .endControlFlow()
            .beginControlFlow("try");
        if (c.setterRest()) {
            change.addStatement("$T action = $T.$L(propertyHolder, delta == null ? null : ($T) delta[0])",
                SkriptTypes.REST_ACTION, c.owner(), c.setterMethod(), valueClass);
            change.beginControlFlow("if ($T.isPrimaryThread())", SkriptTypes.BUKKIT)
                .addStatement("action.queue()")
                .nextControlFlow("else")
                .addStatement("action.complete()")
                .endControlFlow();
        } else {
            change.addStatement("$T.$L(propertyHolder, delta == null ? null : ($T) delta[0])",
                c.owner(), c.setterMethod(), valueClass);
        }
        change.nextControlFlow("catch ($T __ex)", Throwable.class)
            .addStatement("$T.error(__ex)", SkriptTypes.DISKY_RUNTIME_HANDLER)
            .endControlFlow();

        return TypeSpec.anonymousClassBuilder("")
            .addSuperinterface(ParameterizedTypeName.get(handlerType, param, returnClass))
            .addMethod(convert)
            .addMethod(returnType)
            .addMethod(acceptChange)
            .addMethod(change.build())
            .build();

    }

    private static TypeSpec buildReadOnlyHandler(SharedContribution c, ClassName returnClass,
                                                 ClassName handlerType) {
        return TypeSpec.anonymousClassBuilder("")
            .addSuperinterface(ParameterizedTypeName.get(handlerType, c.paramType(), returnClass))
            .addMethod(MethodSpec.methodBuilder("convert")
                .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                .returns(returnClass)
                .addParameter(c.paramType(), "propertyHolder")
                .addStatement("return $T.$L(propertyHolder)", c.owner(), c.method())
                .build())
            .addMethod(MethodSpec.methodBuilder("returnType")
                .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), returnClass))
                .addStatement("return $T.class", returnClass)
                .build())
            .build();
    }

    private static String describe(String name, SharedDoc doc) {
        if (doc != null && doc.description() != null && doc.description().length > 0)
            return String.join(" ", doc.description());
        return "The " + name + " of this Discord entity.";
    }
}
