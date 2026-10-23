package net.itsthesky.disky5.processor.writers;

import java.util.Objects;
import net.itsthesky.disky5.processor.util.Versions;
import com.squareup.javapoet.*;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventFilter;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.processor.DiSkyProcessor.EventDocValue;
import net.itsthesky.disky5.processor.DiSkyProcessor.EventExprDoc;
import net.itsthesky.disky5.processor.DiSkyProcessor.EventRegistration;
import net.itsthesky.disky5.processor.DiSkyProcessor.EventValueReg;
import net.itsthesky.disky5.processor.util.NameUtils;
import net.itsthesky.disky5.processor.util.SkriptTypes;
import net.itsthesky.disky5.processor.util.Log;
import net.itsthesky.disky5.processor.util.TypeUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EventWriter {
    private EventWriter() {}

    public static final String PACKAGE = "net.itsthesky.disky5.generated.events";

    private static final String CHANNEL_BASE = "net.dv8tion.jda.api.entities.channel.Channel";

    private static final List<ClassName> CHANNEL_TYPES = List.of(
        ClassName.get("net.dv8tion.jda.api.entities.channel", "Channel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.middleman", "MessageChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.middleman", "GuildChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.middleman", "GuildMessageChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.middleman", "AudioChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.middleman", "StandardGuildChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.middleman", "StandardGuildMessageChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.concrete", "TextChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.concrete", "NewsChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.concrete", "VoiceChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.concrete", "StageChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.concrete", "Category"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.concrete", "ThreadChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.concrete", "ForumChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.concrete", "MediaChannel"),
        ClassName.get("net.dv8tion.jda.api.entities.channel.concrete", "PrivateChannel")
    );

    public static EventRegistration write(ProcessingEnvironment env, TypeElement descriptor) throws IOException {
        SkriptEvent annotation = descriptor.getAnnotation(SkriptEvent.class);
        String key = NameUtils.toClassName(annotation.name()).replaceAll("[^A-Za-z0-9]", "");
        ClassName jda = resolveJda(annotation);
        ClassName descriptorClass = ClassName.get(descriptor);
        ClassName skriptEvent = ClassName.get(PACKAGE, "GeneratedEvt_" + key);
        ClassName bukkitEvent = ClassName.get(PACKAGE, "GeneratedBukkitEvt_" + key);

        String filterMethod = null;
        for (Element e : descriptor.getEnclosedElements()) {
            if (e.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) e;
            if (m.getAnnotation(EventFilter.class) == null) continue;
            if (m.getParameters().size() != 1) {
                Log.error(env, "an @EventFilter must take exactly one parameter (the JDA event)", m);
                continue;
            }
            if (m.getReturnType().getKind() != TypeKind.BOOLEAN) {
                Log.error(env, "an @EventFilter must return boolean", m);
                continue;
            }
            if (filterMethod != null) {
                Log.error(env, "only one @EventFilter is allowed per event descriptor", m);
                continue;
            }
            filterMethod = m.getSimpleName().toString();
        }

        List<EventValueReg> values = new ArrayList<>();
        List<EventDocValue> docValues = new ArrayList<>();
        List<RestVal> restValues = new ArrayList<>();
        // Skript rejects two event-values of the same (type, time) on one event; channel auto-expansion
        // can collide when an event exposes more than one channel. Dedupe by (type, time), first wins.
        Set<String> seen = new HashSet<>();
        for (Element e : descriptor.getEnclosedElements()) {
            if (e.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) e;
            EventValue ev = m.getAnnotation(EventValue.class);
            if (ev == null) continue;
            if (m.getParameters().size() != 1) {
                env.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "[DiSkyProcessor] an @EventValue must take exactly one parameter (the JDA event)", m);
                continue;
            }
            ClassName paramType = ClassName.bestGuess(
                TypeUtils.stripAnnotations(m.getParameters().get(0).asType().toString()));
            String method = m.getSimpleName().toString();
            int time = ev.time().skriptTime();
            String returnFqn = TypeUtils.stripAnnotations(m.getReturnType().toString());
            boolean isList = m.getReturnType().getKind() == TypeKind.ARRAY || isCollection(env, m.getReturnType());

            if (TypeUtils.isRestAction(env, m.getReturnType())) {
                String xFqn = TypeUtils.soleTypeArgument(m.getReturnType());
                ClassName valueType = ClassName.bestGuess(TypeUtils.boxed(xFqn));
                if (seen.add(valueType.canonicalName() + "@" + time)) {
                    restValues.add(new RestVal(method, valueType));
                    docValues.add(new EventDocValue(method, xFqn, false, time));
                    values.add(new EventValueReg(valueType, paramType, method, time, false, method));
                }
                continue;
            }

            docValues.add(new EventDocValue(method, isList ? elementFqn(returnFqn) : returnFqn, isList, time));

            if (isChannel(env, m.getReturnType())) {
                for (ClassName channelType : CHANNEL_TYPES)
                    if (seen.add(channelType.canonicalName() + "@" + time))
                        values.add(new EventValueReg(channelType, paramType, method, time, true, null));
            } else if (isList) {
                env.getMessager().printMessage(Diagnostic.Kind.WARNING,
                    "[DiSkyProcessor] list-valued @EventValue '" + method + "' on " + descriptor.getSimpleName()
                        + "' is not registered (use @EventExpression for plural event data).", m);
            } else {
                ClassName valueType = ClassName.bestGuess(TypeUtils.boxed(returnFqn));
                if (seen.add(valueType.canonicalName() + "@" + time))
                    values.add(new EventValueReg(valueType, paramType, method, time, false, null));
            }
        }

        for (Element e : descriptor.getEnclosedElements()) {
            if (e.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) e;
            if (m.getAnnotation(EventExpression.class) == null || m.getParameters().size() != 1) continue;
            if (!TypeUtils.isRestAction(env, m.getReturnType())) continue;
            String name = m.getSimpleName().toString();
            ClassName valueType = ClassName.bestGuess(TypeUtils.boxed(TypeUtils.soleTypeArgument(m.getReturnType())));
            if (restValues.stream().noneMatch(r -> r.name().equals(name)))
                restValues.add(new RestVal(name, valueType));
        }

        TypeSpec.Builder bukkit = TypeSpec.classBuilder(bukkitEvent.simpleName())
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("Auto-generated by DiSkyProcessor - do not edit.\n")
            .superclass(SkriptTypes.DISKY_BUKKIT_EVENT);
        MethodSpec.Builder ctor = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(SkriptTypes.GENERIC_EVENT, "jdaEvent")
            .addParameter(SkriptTypes.BOT, "bot")
            .addStatement("super(jdaEvent, bot)");
        for (RestVal rv : restValues) {
            bukkit.addField(rv.valueClass(), rv.name(), Modifier.PRIVATE, Modifier.FINAL);
            ctor.addParameter(rv.valueClass(), rv.name()).addStatement("this.$L = $L", rv.name(), rv.name());
            bukkit.addMethod(MethodSpec.methodBuilder(rv.name())
                .addModifiers(Modifier.PUBLIC).returns(rv.valueClass())
                .addStatement("return $L", rv.name()).build());
        }
        bukkit.addMethod(ctor.build());
        if (annotation.cancellable()) {
            bukkit.addSuperinterface(SkriptTypes.CANCELLABLE)
                .addField(boolean.class, "cancelled", Modifier.PRIVATE)
                .addMethod(MethodSpec.methodBuilder("isCancelled").addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC).returns(boolean.class)
                    .addStatement("return cancelled || (getJdaEvent() instanceof $T c && c.isCancelled())",
                        SkriptTypes.CANCELLATION_AWARE).build())
                .addMethod(MethodSpec.methodBuilder("setCancelled").addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC).addParameter(boolean.class, "cancel")
                    .addStatement("this.cancelled = cancel")
                    .beginControlFlow("if (getJdaEvent() instanceof $T c)", SkriptTypes.CANCELLATION_AWARE)
                    .addStatement("c.setCancelled(cancel)")
                    .endControlFlow().build());
        }
        JavaFile.builder(PACKAGE, bukkit.build()).build().writeTo(env.getFiler());

        MethodSpec.Builder create = MethodSpec.methodBuilder("createBukkitEvent")
            .addAnnotation(Override.class).addModifiers(Modifier.PROTECTED)
            .returns(SkriptTypes.DISKY_BUKKIT_EVENT)
            .addParameter(jda, "jdaEvent")
            .addParameter(SkriptTypes.BOT, "bot");
        CodeBlock.Builder args = CodeBlock.builder().add("jdaEvent, bot");
        for (RestVal rv : restValues) {
            // A REST mapper may return null (e.g. no member on a DM-originated event); without this
            // guard the NPE escapes the async task and the whole event is lost, not just this value.
            create.addStatement("var __$L = $T.$L(jdaEvent)", rv.name(), descriptorClass, rv.name());
            create.addStatement("$T $L = __$L == null ? null : __$L.complete()",
                rv.valueClass(), rv.name(), rv.name(), rv.name());
            args.add(", $L", rv.name());
        }
        create.addStatement("return new $T($L)", bukkitEvent, args.build());

        TypeSpec.Builder skript = TypeSpec.classBuilder(skriptEvent.simpleName())
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("Auto-generated by DiSkyProcessor - do not edit.\n")
            .superclass(ParameterizedTypeName.get(SkriptTypes.DISKY_EVENT, jda))
            .addMethod(MethodSpec.methodBuilder("jdaType")
                .addAnnotation(Override.class).addModifiers(Modifier.PROTECTED)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), jda))
                .addStatement("return $T.class", jda).build())
            .addMethod(create.build());
        if (filterMethod != null)
            skript.addMethod(MethodSpec.methodBuilder("accept")
                .addAnnotation(Override.class).addModifiers(Modifier.PROTECTED).returns(boolean.class)
                .addParameter(jda, "jdaEvent")
                .addParameter(SkriptTypes.BOT, "bot")
                .addStatement("return $T.$L(jdaEvent)", descriptorClass, filterMethod).build());
        if (!restValues.isEmpty())
            skript.addMethod(MethodSpec.methodBuilder("hasRestValues")
                .addAnnotation(Override.class).addModifiers(Modifier.PROTECTED).returns(boolean.class)
                .addStatement("return true").build());
        JavaFile.builder(PACKAGE, skript.build()).build().writeTo(env.getFiler());

        List<ClassName> expressionClasses = new ArrayList<>();
        List<EventExprDoc> exprDocs = new ArrayList<>();
        for (Element e : descriptor.getEnclosedElements()) {
            if (e.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) e;
            EventExpression ee = m.getAnnotation(EventExpression.class);
            if (ee == null) continue;
            if (m.getParameters().size() != 1) {
                env.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "[DiSkyProcessor] an @EventExpression must take exactly one parameter (the JDA event)", m);
                continue;
            }
            boolean rest = TypeUtils.isRestAction(env, m.getReturnType());
            expressionClasses.add(writeEventExpression(env, key, annotation.name(), bukkitEvent, descriptorClass, m, ee.value(), rest));
            boolean list = !rest && (m.getReturnType().getKind() == TypeKind.ARRAY || isCollection(env, m.getReturnType()));
            String retFqn = rest ? TypeUtils.soleTypeArgument(m.getReturnType())
                                 : TypeUtils.stripAnnotations(m.getReturnType().toString());
            exprDocs.add(new EventExprDoc(ee.value(), list ? elementFqn(retFqn) : retFqn, list));
        }

        List<String> patterns = new ArrayList<>();
        for (String p : annotation.patterns())
            patterns.add(p + " [seen by %-string%]");

        Documentation doc = descriptor.getAnnotation(Documentation.class);
        String description = (doc != null && doc.description().length > 0) ? String.join(" ", doc.description()) : null;
        List<String> examples = doc != null ? List.of(doc.examples()) : List.of();
        String since = Objects.requireNonNullElse(Versions.first(doc == null ? null : doc.since()), Versions.defaultSince());

        return new EventRegistration(annotation.name(), patterns, skriptEvent, bukkitEvent,
            descriptorClass, jda, values, List.of(annotation.patterns()), description, examples,
            since, annotation.cancellable(), List.of(annotation.intents()), docValues,
            expressionClasses, exprDocs);
    }

    private static ClassName writeEventExpression(ProcessingEnvironment env, String eventKey, String eventName,
            ClassName bukkitEvent, ClassName descriptor, ExecutableElement m, String pattern, boolean rest) throws IOException {
        String method = m.getSimpleName().toString();
        ClassName self = ClassName.get(PACKAGE, "GeneratedEvtExpr_" + eventKey + "_" + NameUtils.capitalize(method));
        ClassName paramType = ClassName.bestGuess(
            TypeUtils.stripAnnotations(m.getParameters().get(0).asType().toString()));
        String retFqn = rest ? TypeUtils.soleTypeArgument(m.getReturnType())
                             : TypeUtils.stripAnnotations(m.getReturnType().toString());
        boolean array = !rest && m.getReturnType().getKind() == TypeKind.ARRAY;
        boolean coll = !rest && isCollection(env, m.getReturnType());
        boolean list = array || coll;
        ClassName returnClass = ClassName.bestGuess(TypeUtils.boxed(list ? elementFqn(retFqn) : retFqn));
        ArrayTypeName returnArray = ArrayTypeName.of(returnClass);

        MethodSpec.Builder get = MethodSpec.methodBuilder("get")
            .addAnnotation(Override.class).addModifiers(Modifier.PROTECTED).returns(returnArray)
            .addParameter(SkriptTypes.EVENT, "event")
            .beginControlFlow("if (event instanceof $T bukkit)", bukkitEvent);
        if (rest)
            get.addStatement("return new $T[]{ bukkit.$L() }", returnClass, method);
        else if (!list)
            get.addStatement("return new $T[]{ $T.$L(($T) bukkit.getJdaEvent()) }", returnClass, descriptor, method, paramType);
        else if (array)
            get.addStatement("return $T.$L(($T) bukkit.getJdaEvent())", descriptor, method, paramType);
        else
            get.addStatement("return $T.$L(($T) bukkit.getJdaEvent()).toArray(new $T[0])", descriptor, method, paramType, returnClass);
        get.endControlFlow().addStatement("return new $T[0]", returnClass);

        TypeSpec spec = TypeSpec.classBuilder(self.simpleName())
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("Auto-generated by DiSkyProcessor - do not edit.\n")
            .superclass(ParameterizedTypeName.get(SkriptTypes.SIMPLE_EXPRESSION, returnClass))
            .addMethod(MethodSpec.methodBuilder("register")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("$T.registerExpression($T.class, $T.class, $T.SIMPLE, $S)",
                    SkriptTypes.SKRIPT, self, returnClass, SkriptTypes.EXPRESSION_TYPE, pattern)
                .build())
            .addMethod(MethodSpec.methodBuilder("init")
                .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(boolean.class)
                .addParameter(ArrayTypeName.of(ParameterizedTypeName.get(
                    SkriptTypes.EXPRESSION, WildcardTypeName.subtypeOf(Object.class))), "expressions")
                .addParameter(int.class, "matchedPattern")
                .addParameter(SkriptTypes.KLEENEAN, "isDelayed")
                .addParameter(SkriptTypes.PARSE_RESULT, "parseResult")
                .beginControlFlow("if (!getParser().isCurrentEvent($T.class))", bukkitEvent)
                .addStatement("$T.error($S)", SkriptTypes.SKRIPT,
                    "The '" + pattern + "' expression can only be used in the '" + eventName + "' event")
                .addStatement("return false")
                .endControlFlow()
                .addStatement("return true")
                .build())
            .addMethod(get.build())
            .addMethod(MethodSpec.methodBuilder("isSingle")
                .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(boolean.class)
                .addStatement("return $L", !list).build())
            .addMethod(MethodSpec.methodBuilder("getReturnType")
                .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(returnClass)))
                .addStatement("return $T.class", returnClass).build())
            .addMethod(MethodSpec.methodBuilder("toString")
                .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(String.class)
                .addParameter(SkriptTypes.EVENT, "event").addParameter(boolean.class, "debug")
                .addStatement("return $S", pattern).build())
            .build();

        JavaFile.builder(PACKAGE, spec).build().writeTo(env.getFiler());
        return self;
    }

    private static String elementFqn(String fqn) {
        if (fqn.contains("<")) return fqn.substring(fqn.indexOf('<') + 1, fqn.lastIndexOf('>')).trim();
        if (fqn.endsWith("[]")) return fqn.substring(0, fqn.length() - 2).trim();
        return fqn;
    }

    private record RestVal(String name, ClassName valueClass) {}

    private static boolean isCollection(ProcessingEnvironment env, TypeMirror returnType) {
        TypeElement coll = env.getElementUtils().getTypeElement("java.util.Collection");
        if (coll == null) return false;
        return env.getTypeUtils().isAssignable(
            env.getTypeUtils().erasure(returnType),
            env.getTypeUtils().erasure(coll.asType()));
    }

    private static boolean isChannel(ProcessingEnvironment env, TypeMirror returnType) {
        TypeElement base = env.getElementUtils().getTypeElement(CHANNEL_BASE);
        if (base == null) return false;
        return env.getTypeUtils().isAssignable(
            env.getTypeUtils().erasure(returnType),
            env.getTypeUtils().erasure(base.asType()));
    }

    private static ClassName resolveJda(SkriptEvent annotation) {
        try {
            annotation.jda();
        } catch (MirroredTypeException ex) {
            return ClassName.bestGuess(TypeUtils.stripAnnotations(ex.getTypeMirror().toString()));
        }
        throw new IllegalStateException("unreachable");
    }
}
