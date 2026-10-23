package net.itsthesky.disky5.processor.handlers;

import net.itsthesky.disky5.processor.util.Versions;
import com.squareup.javapoet.*;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.processor.DescriptorContext;
import net.itsthesky.disky5.processor.DiSkyProcessor;
import net.itsthesky.disky5.processor.util.DocsUtils;
import net.itsthesky.disky5.processor.util.NameUtils;
import net.itsthesky.disky5.processor.util.SkriptTypes;
import net.itsthesky.disky5.processor.util.TypeUtils;
import net.itsthesky.disky5.processor.util.Log;

import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class PropertyHandler implements DescriptorHandler {

    @Override
    public void handle(DescriptorContext ctx) throws Exception {
        Set<String> names = new LinkedHashSet<>();
        for (Element enclosed : ctx.descriptor.getEnclosedElements()) {
            if (enclosed.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) enclosed;
            Property p = m.getAnnotation(Property.class);
            if (p != null && !p.shared()) addName(names, p.value());
            Property.Retrieve r = m.getAnnotation(Property.Retrieve.class);
            if (r != null) addName(names, r.value());
            Property.Setter st = m.getAnnotation(Property.Setter.class);
            if (st != null && !st.shared()) addName(names, st.value());
        }

        for (String name : names) {
            ExecutableElement getter   = find(ctx.descriptor, m -> {
                Property p = m.getAnnotation(Property.class);
                return p != null && p.value().equals(name);
            });
            ExecutableElement setter   = find(ctx.descriptor, m -> {
                Property.Setter s = m.getAnnotation(Property.Setter.class);
                return s != null && !s.shared() && s.value().equals(name);
            });
            ExecutableElement adder    = find(ctx.descriptor, m -> matches(m.getAnnotation(Property.Adder.class), name));
            ExecutableElement remover  = find(ctx.descriptor, m -> matches(m.getAnnotation(Property.Remover.class), name));
            ExecutableElement retrieve = find(ctx.descriptor, m -> matches(m.getAnnotation(Property.Retrieve.class), name));

            PropertyModel model = new PropertyModel(name, getter, setter, adder, remover, retrieve);
            if (ctx.multiTypeNames.contains(name))
                collectMultiType(ctx, model);
            else
                generate(ctx, model);
        }
    }

    private static void collectMultiType(DescriptorContext ctx, PropertyModel model) {
        ExecutableElement readMethod = model.getter() != null ? model.getter() : model.retrieve();
        ExecutableElement mutator = model.setter() != null ? model.setter()
            : model.adder() != null ? model.adder() : model.remover();
        if (readMethod == null && mutator == null) return;

        String returnType = readMethod != null
            ? TypeUtils.stripAnnotations(readMethod.getReturnType().toString())
            : TypeUtils.paramType(mutator.getParameters().get(1).asType().toString());
        if (readMethod != null && model.getter() == null && TypeUtils.isRestAction(ctx.env, readMethod.getReturnType()))
            returnType = TypeUtils.soleTypeArgument(readMethod.getReturnType());

        String element = TypeUtils.collectionElement(returnType);
        boolean multi = element != null;
        String elementFqn = TypeUtils.boxed(multi ? element : returnType);
        boolean array = returnType.endsWith("[]");

        if (model.getter() == null && model.retrieve() != null) {
            ctx.env.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "[DiSkyProcessor] union property '" + model.name() + "' on " + ctx.codeName
                    + " needs a cache @Property getter (pure-REST union not supported)");
            return;
        }

        String setterName = null; ClassName setterVal = null; boolean setterArray = false; boolean setterRest = false;
        if (model.setter() != null) {
            setterName = model.setter().getSimpleName().toString();
            setterVal  = valueParamClass(model.setter());
            setterArray = isArrayValueParam(model.setter());
            setterRest = isRestMutator(ctx, model.setter());
        }
        String adderName = null; ClassName adderVal = null; boolean adderArray = false; boolean adderRest = false;
        if (model.adder() != null) {
            adderName = model.adder().getSimpleName().toString();
            adderVal  = valueParamClass(model.adder());
            adderArray = isArrayValueParam(model.adder());
            adderRest = isRestMutator(ctx, model.adder());
        }
        String removerName = null; ClassName removerVal = null; boolean removerArray = false; boolean removerRest = false;
        if (model.remover() != null) {
            removerName = model.remover().getSimpleName().toString();
            removerVal  = valueParamClass(model.remover());
            removerArray = isArrayValueParam(model.remover());
            removerRest = isRestMutator(ctx, model.remover());
        }
        String retrieveName = null; boolean retrieveRest = false;
        if (model.retrieve() != null) {
            retrieveName = model.retrieve().getSimpleName().toString();
            retrieveRest = TypeUtils.isRestAction(ctx.env, model.retrieve().getReturnType());
        }

        ctx.multiTypeContributions.add(new DiSkyProcessor.MultiTypeContribution(
            model.name(), ctx.codeName,
            ClassName.bestGuess(ctx.jdaType), ClassName.bestGuess(ctx.descriptorFqn),
            model.getter() == null ? null : model.getter().getSimpleName().toString(),
            elementFqn, multi, array,
            setterName, setterVal, setterArray, setterRest, adderName, adderVal, adderArray, adderRest,
            removerName, removerVal, removerArray, removerRest, retrieveName, retrieveRest));

        recordSyntax(ctx, model, elementFqn, multi);
    }

    private static void addName(Set<String> names, String name) {
        if (name != null) names.add(name);
    }

    private static boolean matches(Object ann, String name) {
        if (ann instanceof Property.Setter s)   return s.value().equals(name);
        if (ann instanceof Property.Adder a)     return a.value().equals(name);
        if (ann instanceof Property.Remover r)   return r.value().equals(name);
        if (ann instanceof Property.Retrieve rt) return rt.value().equals(name);
        return false;
    }

    private interface Pred { boolean test(ExecutableElement m); }

    private static ExecutableElement find(TypeElement descriptor, Pred pred) {
        for (Element e : descriptor.getEnclosedElements())
            if (e.getKind() == ElementKind.METHOD && pred.test((ExecutableElement) e))
                return (ExecutableElement) e;
        return null;
    }

    private record PropertyModel(String name, ExecutableElement getter, ExecutableElement setter,
                                 ExecutableElement adder, ExecutableElement remover,
                                 ExecutableElement retrieve) {}

    private static void generate(DescriptorContext ctx, PropertyModel model) throws IOException {
        ExecutableElement readMethod = model.getter() != null ? model.getter() : model.retrieve();
        if (readMethod == null) {
            generateWriteOnly(ctx, model);
            return;
        }

        String returnType = TypeUtils.stripAnnotations(readMethod.getReturnType().toString());
        if (model.getter() == null && TypeUtils.isRestAction(ctx.env, readMethod.getReturnType()))
            returnType = TypeUtils.soleTypeArgument(readMethod.getReturnType());

        String element = TypeUtils.collectionElement(returnType);
        if (element != null)
            generateMulti(ctx, model, element);
        else
            generateSingle(ctx, model, returnType);
    }

    private static void generateWriteOnly(DescriptorContext ctx, PropertyModel model) throws IOException {
        ExecutableElement mutator = model.setter() != null ? model.setter()
            : model.adder() != null ? model.adder() : model.remover();
        if (mutator == null) return;
        if (mutator.getParameters().size() < 2) {
            ctx.env.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "[DiSkyProcessor] the mutator of write-only property '" + model.name()
                    + "' must take (owner, value)", mutator);
            return;
        }
        String valueType = TypeUtils.paramType(mutator.getParameters().get(1).asType().toString());
        String element = TypeUtils.collectionElement(valueType);
        if (element != null)
            generateMulti(ctx, model, element);
        else
            generateSingle(ctx, model, valueType);
    }

    private static void generateSingle(DescriptorContext ctx, PropertyModel model, String returnType) throws IOException {
        String className  = "Prop" + NameUtils.entityName(ctx.descriptorFqn) + NameUtils.toClassName(model.name());

        ClassName jdaClass        = ClassName.bestGuess(ctx.jdaType);
        ClassName returnClass     = ClassName.bestGuess(TypeUtils.boxed(returnType));
        ClassName descriptorClass = ClassName.bestGuess(ctx.descriptorFqn);

        boolean pureRest  = model.getter() == null && model.retrieve() != null;
        boolean writeOnly = model.getter() == null && model.retrieve() == null;

        TypeSpec.Builder cls = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("Auto-generated by DiSkyProcessor - do not edit.\n")
            .superclass(ParameterizedTypeName.get(
                SkriptTypes.SIMPLE_PROPERTY_EXPRESSION, jdaClass, returnClass));

        boolean changeable = model.setter() != null || model.adder() != null || model.remover() != null;
        if (changeable)
            cls.addSuperinterface(SkriptTypes.DISKY_CHANGEABLE);

        DocsUtils.attachDocumentation(cls, docOf(model));

        cls.addStaticBlock(CodeBlock.of(
            "register($L.class, $T.class, $S, $S);\n",
            className, returnClass, model.name(), ctx.codeName + "s"));

        if (pureRest)
            cls.addMethod(buildPureRestInit(model.name()));

        cls.addMethod(buildSingleConvert(model, jdaClass, returnClass, descriptorClass, writeOnly));
        if (model.retrieve() != null)
            cls.addMethod(buildSingleGet(ctx, model, jdaClass, returnClass, descriptorClass));

        cls.addMethod(MethodSpec.methodBuilder("getPropertyName")
            .addAnnotation(Override.class).addModifiers(Modifier.PROTECTED)
            .returns(String.class)
            .addStatement("return $S", model.name())
            .build());

        cls.addMethod(MethodSpec.methodBuilder("getReturnType")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName.get(ClassName.get(Class.class), returnClass))
            .addStatement("return $T.class", returnClass)
            .build());

        if (changeable)
            addChangeMethods(ctx, cls, model, jdaClass, descriptorClass);

        recordSyntax(ctx, model, TypeUtils.boxed(returnType), false);
        write(ctx, cls, className, "");
    }

    private static MethodSpec buildSingleConvert(PropertyModel model, ClassName jdaClass,
                                                 ClassName returnClass, ClassName descriptorClass,
                                                 boolean writeOnly) {
        MethodSpec.Builder convert = MethodSpec.methodBuilder("convert")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(returnClass)
            .addParameter(jdaClass, "entity");
        if (model.getter() != null)
            convert.addStatement("return $T.$L(entity)", descriptorClass, model.getter().getSimpleName());
        else if (writeOnly)
            convert.addStatement("return null");
        else
            convert.addStatement("throw new $T($S)", ClassName.get(IllegalStateException.class),
                "the '" + model.name() + "' property is online-only; read it with 'await'");
        return convert.build();
    }

    private static MethodSpec buildSingleGet(DescriptorContext ctx, PropertyModel model, ClassName jdaClass,
                                             ClassName returnClass, ClassName descriptorClass) {
        boolean retrieveRest = TypeUtils.isRestAction(ctx.env, model.retrieve().getReturnType());
        ArrayTypeName returnArray = ArrayTypeName.of(returnClass);

        MethodSpec.Builder get = MethodSpec.methodBuilder("get")
            .addAnnotation(Override.class).addModifiers(Modifier.PROTECTED)
            .returns(returnArray)
            .addParameter(SkriptTypes.EVENT, "event")
            .addParameter(ArrayTypeName.of(jdaClass), "source")
            .beginControlFlow("if (!$T.isAwaited(event))", SkriptTypes.DISKY_ASYNC)
            .addStatement("return super.get(event, source)")
            .endControlFlow()
            .addStatement("$T result = new $T[source.length]", returnArray, returnClass)
            .beginControlFlow("for (int i = 0; i < source.length; i++)");
        if (retrieveRest)
            get.addStatement("result[i] = $T.$L(source[i]).complete()", descriptorClass, model.retrieve().getSimpleName());
        else
            get.addStatement("result[i] = $T.$L(source[i])", descriptorClass, model.retrieve().getSimpleName());
        get.endControlFlow()
            .addStatement("return result");
        return get.build();
    }

    private static void generateMulti(DescriptorContext ctx, PropertyModel model, String element) throws IOException {
        String className  = "Prop" + NameUtils.entityName(ctx.descriptorFqn) + NameUtils.toClassName(model.name());

        ClassName jdaClass        = ClassName.bestGuess(ctx.jdaType);
        ClassName elementClass    = ClassName.bestGuess(TypeUtils.boxed(element));
        ClassName descriptorClass = ClassName.bestGuess(ctx.descriptorFqn);
        ArrayTypeName elementArray = ArrayTypeName.of(elementClass);

        boolean pureRest  = model.getter() == null && model.retrieve() != null;
        boolean writeOnly = model.getter() == null && model.retrieve() == null;

        TypeSpec.Builder cls = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("Auto-generated by DiSkyProcessor - do not edit.\n")
            .superclass(ParameterizedTypeName.get(
                SkriptTypes.SIMPLE_PROPERTY_EXPRESSION, jdaClass, elementClass));

        boolean changeable = model.setter() != null || model.adder() != null || model.remover() != null;
        if (changeable)
            cls.addSuperinterface(SkriptTypes.DISKY_CHANGEABLE);

        DocsUtils.attachDocumentation(cls, docOf(model));

        cls.addStaticBlock(CodeBlock.of(
            "register($L.class, $T.class, $S, $S);\n",
            className, elementClass, model.name(), ctx.codeName + "s"));

        if (pureRest)
            cls.addMethod(buildPureRestInit(model.name()));

        cls.addMethod(buildMultiGet(ctx, model, jdaClass, elementClass, elementArray, descriptorClass, writeOnly));

        cls.addMethod(buildMultiConvert(ctx, model, jdaClass, elementClass, descriptorClass, writeOnly));

        cls.addMethod(MethodSpec.methodBuilder("getPropertyName")
            .addAnnotation(Override.class).addModifiers(Modifier.PROTECTED)
            .returns(String.class)
            .addStatement("return $S", model.name())
            .build());

        cls.addMethod(MethodSpec.methodBuilder("getReturnType")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName.get(ClassName.get(Class.class), elementClass))
            .addStatement("return $T.class", elementClass)
            .build());

        cls.addMethod(MethodSpec.methodBuilder("isSingle")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(boolean.class)
            .addStatement("return false")
            .build());

        if (changeable)
            addChangeMethods(ctx, cls, model, jdaClass, descriptorClass);

        recordSyntax(ctx, model, TypeUtils.boxed(element), true);
        write(ctx, cls, className, "(multi) ");
    }

    private static MethodSpec buildMultiGet(DescriptorContext ctx, PropertyModel model, ClassName jdaClass,
                                            ClassName elementClass, ArrayTypeName elementArray,
                                            ClassName descriptorClass, boolean writeOnly) {
        MethodSpec.Builder get = MethodSpec.methodBuilder("get")
            .addAnnotation(Override.class).addModifiers(Modifier.PROTECTED)
            .returns(elementArray)
            .addParameter(SkriptTypes.EVENT, "event")
            .addParameter(ArrayTypeName.of(jdaClass), "source")
            .addStatement("$T<$T> result = new $T<>()",
                ClassName.get("java.util", "List"), elementClass, ClassName.get("java.util", "ArrayList"));

        if (writeOnly)
            return get.addStatement("return result.toArray(new $T[0])", elementClass).build();

        get.beginControlFlow("for ($T entity : source)", jdaClass);
        if (model.retrieve() != null) {
            boolean retrieveRest = TypeUtils.isRestAction(ctx.env, model.retrieve().getReturnType());
            get.beginControlFlow("if ($T.isAwaited(event))", SkriptTypes.DISKY_ASYNC);
            if (retrieveRest)
                get.addStatement("result.addAll($T.$L(entity).complete())", descriptorClass, model.retrieve().getSimpleName());
            else
                get.addStatement("result.addAll($T.$L(entity))", descriptorClass, model.retrieve().getSimpleName());
            if (model.getter() != null) {
                get.nextControlFlow("else");
                get.addStatement("result.addAll($T.$L(entity))", descriptorClass, model.getter().getSimpleName());
            }
            get.endControlFlow();
        } else {
            get.addStatement("result.addAll($T.$L(entity))", descriptorClass, model.getter().getSimpleName());
        }
        get.endControlFlow();
        get.addStatement("return result.toArray(new $T[0])", elementClass);
        return get.build();
    }

    private static MethodSpec buildMultiConvert(DescriptorContext ctx, PropertyModel model, ClassName jdaClass,
                                                ClassName elementClass, ClassName descriptorClass,
                                                boolean writeOnly) {
        MethodSpec.Builder convert = MethodSpec.methodBuilder("convert")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(elementClass)
            .addParameter(jdaClass, "entity");

        if (writeOnly)
            return convert.addStatement("return null").build();

        if (model.getter() != null) {
            convert.addStatement("$T<$T> values = $T.$L(entity)",
                ClassName.get("java.util", "Collection"), elementClass, descriptorClass, model.getter().getSimpleName());
        } else if (TypeUtils.isRestAction(ctx.env, model.retrieve().getReturnType())) {
            convert.addStatement("$T<$T> values = $T.$L(entity).complete()",
                ClassName.get("java.util", "Collection"), elementClass, descriptorClass, model.retrieve().getSimpleName());
        } else {
            convert.addStatement("$T<$T> values = $T.$L(entity)",
                ClassName.get("java.util", "Collection"), elementClass, descriptorClass, model.retrieve().getSimpleName());
        }
        convert.addStatement("return values.isEmpty() ? null : values.iterator().next()");
        return convert.build();
    }

    private static void addChangeMethods(DescriptorContext ctx, TypeSpec.Builder cls, PropertyModel model,
                                         ClassName jdaClass, ClassName descriptorClass) {
        CodeBlock.Builder accept = CodeBlock.builder();
        if (model.setter() != null)
            accept.addStatement("if (mode == $T.SET) return new Class[]{ $T$L.class }",
                SkriptTypes.CHANGER_MODE, valueParamClass(model.setter()),
                isArrayValueParam(model.setter()) ? "[]" : "");
        if (model.setter() != null && isArrayValueParam(model.setter()))
            accept.addStatement("if (mode == $T.DELETE) return new Class[]{ $T[].class }",
                SkriptTypes.CHANGER_MODE, valueParamClass(model.setter()));
        if (model.adder() != null)
            accept.addStatement("if (mode == $T.ADD) return new Class[]{ $T[].class }",
                SkriptTypes.CHANGER_MODE, valueParamClass(model.adder()));
        if (model.remover() != null)
            accept.addStatement("if (mode == $T.REMOVE) return new Class[]{ $T[].class }",
                SkriptTypes.CHANGER_MODE, valueParamClass(model.remover()));
        accept.addStatement("return null");

        cls.addMethod(MethodSpec.methodBuilder("acceptChange")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(Class[].class)
            .addParameter(SkriptTypes.CHANGER_MODE, "mode")
            .addCode(accept.build())
            .build());

        MethodSpec.Builder change = MethodSpec.methodBuilder("change")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .addParameter(SkriptTypes.EVENT, "event")
            .addParameter(Object[].class, "delta")
            .addParameter(SkriptTypes.CHANGER_MODE, "mode")
            .addStatement("boolean awaited = $T.isAwaited(event)", SkriptTypes.DISKY_ASYNC)
            .beginControlFlow("for ($T entity : getExpr().getArray(event))", jdaClass);
        change.beginControlFlow("try");

        boolean open = false;
        if (model.setter() != null) {
            if (isArrayValueParam(model.setter()))
                change.beginControlFlow("if (mode == $T.SET || mode == $T.DELETE)",
                    SkriptTypes.CHANGER_MODE, SkriptTypes.CHANGER_MODE);
            else
                change.beginControlFlow("if (mode == $T.SET)", SkriptTypes.CHANGER_MODE);
            emitSet(ctx, change, model.setter(), descriptorClass);
            open = true;
        }
        if (model.adder() != null) {
            if (open) change.nextControlFlow("else if (mode == $T.ADD)", SkriptTypes.CHANGER_MODE);
            else      change.beginControlFlow("if (mode == $T.ADD)", SkriptTypes.CHANGER_MODE);
            if (isArrayValueParam(model.adder())) emitSet(ctx, change, model.adder(), descriptorClass);
            else emitElementLoop(ctx, change, model.adder(), descriptorClass);
            open = true;
        }
        if (model.remover() != null) {
            if (open) change.nextControlFlow("else if (mode == $T.REMOVE)", SkriptTypes.CHANGER_MODE);
            else      change.beginControlFlow("if (mode == $T.REMOVE)", SkriptTypes.CHANGER_MODE);
            if (isArrayValueParam(model.remover())) emitSet(ctx, change, model.remover(), descriptorClass);
            else emitElementLoop(ctx, change, model.remover(), descriptorClass);
            open = true;
        }
        if (open) change.endControlFlow();
        change.nextControlFlow("catch ($T __ex)", Throwable.class)
              .addStatement("$T.error(__ex, null, event)", SkriptTypes.DISKY_RUNTIME_HANDLER)
              .endControlFlow();
        change.endControlFlow();

        cls.addMethod(change.build());
    }

    private static void emitSet(DescriptorContext ctx, MethodSpec.Builder change,
                                ExecutableElement mutator, ClassName descriptorClass) {
        ClassName valueClass = valueParamClass(mutator);
        String value = isArrayValueParam(mutator)
            ? "delta == null ? new $T[0] : $T.copyOf(delta, delta.length, $T[].class)"
            : "delta == null ? null : ($T) delta[0]";
        Object[] valueArgs = isArrayValueParam(mutator)
            ? new Object[]{valueClass, ClassName.get(java.util.Arrays.class), valueClass}
            : new Object[]{valueClass};
        CodeBlock valueCode = CodeBlock.of(value, valueArgs);
        if (isRestMutator(ctx, mutator)) {
            change.addStatement("$T action = $T.$L(entity, $L)",
                SkriptTypes.REST_ACTION, descriptorClass, mutator.getSimpleName(), valueCode);
            emitQueueOrComplete(change);
        } else {
            change.addStatement("$T.$L(entity, $L)",
                descriptorClass, mutator.getSimpleName(), valueCode);
        }
    }

    private static void emitElementLoop(DescriptorContext ctx, MethodSpec.Builder change,
                                        ExecutableElement mutator, ClassName descriptorClass) {
        ClassName valueClass = valueParamClass(mutator);
        change.beginControlFlow("for ($T raw : delta)", Object.class)
            .addStatement("$T value = ($T) raw", valueClass, valueClass);
        if (isRestMutator(ctx, mutator)) {
            change.addStatement("$T action = $T.$L(entity, value)",
                SkriptTypes.REST_ACTION, descriptorClass, mutator.getSimpleName());
            emitQueueOrComplete(change);
        } else {
            change.addStatement("$T.$L(entity, value)", descriptorClass, mutator.getSimpleName());
        }
        change.endControlFlow();
    }

    private static void emitQueueOrComplete(MethodSpec.Builder change) {
        change.beginControlFlow("if (awaited)")
            .addStatement("action.complete()")
            .nextControlFlow("else")
            .addStatement("action.queue()")
            .endControlFlow();
    }

    private static boolean isRestMutator(DescriptorContext ctx, ExecutableElement mutator) {
        return !TypeUtils.isVoid(mutator.getReturnType())
            && TypeUtils.isRestAction(ctx.env, mutator.getReturnType());
    }

    private static ClassName valueParamClass(ExecutableElement mutator) {
        String raw = TypeUtils.paramType(mutator.getParameters().get(1).asType().toString());
        return ClassName.bestGuess(TypeUtils.boxed(TypeUtils.arrayComponent(raw)));
    }

    private static boolean isArrayValueParam(ExecutableElement mutator) {
        return TypeUtils.isArray(TypeUtils.paramType(mutator.getParameters().get(1).asType().toString()));
    }

    private static MethodSpec buildPureRestInit(String propName) {
        return MethodSpec.methodBuilder("init")
            .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
            .returns(boolean.class)
            .addParameter(ArrayTypeName.of(ParameterizedTypeName.get(
                ClassName.get("ch.njol.skript.lang", "Expression"),
                WildcardTypeName.subtypeOf(Object.class))), "expressions")
            .addParameter(int.class, "matchedPattern")
            .addParameter(ClassName.get("ch.njol.util", "Kleenean"), "isDelayed")
            .addParameter(ClassName.get("ch.njol.skript.lang", "SkriptParser", "ParseResult"), "parseResult")
            .beginControlFlow("if (!$T.isParsingInsideAwait())", SkriptTypes.DISKY_ASYNC)
            .addStatement("$T.error($S)", ClassName.get("ch.njol.skript", "Skript"),
                "the '" + propName + "' property is online-only and requires 'await' to be read")
            .addStatement("return false")
            .endControlFlow()
            .addStatement("return super.init(expressions, matchedPattern, isDelayed, parseResult)")
            .build();
    }

    private static Documentation docOf(PropertyModel model) {
        for (ExecutableElement source : new ExecutableElement[]{
                model.getter(), model.retrieve(), model.setter(), model.adder(), model.remover()}) {
            if (source == null) continue;
            Documentation docs = source.getAnnotation(Documentation.class);
            if (docs != null) return docs;
        }
        return null;
    }

    private static void recordSyntax(DescriptorContext ctx, PropertyModel model,
                                     String returnTypeFqn, boolean returnList) {
        List<String> changeModes = new ArrayList<>();
        if (model.setter() != null)  changeModes.add("SET");
        if (model.adder() != null)   changeModes.add("ADD");
        if (model.remover() != null) changeModes.add("REMOVE");
        if (model.setter() != null && isArrayValueParam(model.setter())) changeModes.add("DELETE");

        boolean retrieve   = model.retrieve() != null;
        boolean retrieveOnly = retrieve && model.getter() == null;
        boolean restBacked = (model.setter()  != null && isRestMutator(ctx, model.setter()))
                          || (model.adder()   != null && isRestMutator(ctx, model.adder()))
                          || (model.remover() != null && isRestMutator(ctx, model.remover()));
        boolean awaitable  = retrieve || restBacked;

        Documentation d = docOf(model);
        String docName = (d != null && !d.name().isBlank()) ? d.name() : null;
        List<String> description = d != null ? List.of(d.description()) : List.of();
        List<String> examples    = d != null ? List.of(d.examples())    : List.of();
        String since = Versions.first(d == null ? null : d.since());

        boolean writeOnly = model.getter() == null && model.retrieve() == null;
        ctx.syntaxRecords.add(new DiSkyProcessor.SyntaxRecord(
            ctx.codeName, model.name(), "PROPERTY", returnTypeFqn, returnList,
            changeModes, awaitable, restBacked, retrieveOnly, false, writeOnly,
            docName, description, examples, since, List.of()));
    }

    private static void write(DescriptorContext ctx, TypeSpec.Builder cls, String className, String tag) throws IOException {
        JavaFile.builder(ctx.generatedPkg, cls.build()).build().writeTo(ctx.env.getFiler());
        ctx.generatedClasses.add(ctx.generatedPkg + "." + className);
        Log.note(ctx.env, "Generated " + tag + ctx.generatedPkg + "." + className);
    }
}
