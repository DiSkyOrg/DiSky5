package net.itsthesky.disky5.processor.handlers;

import com.squareup.javapoet.ClassName;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.processor.handlers.EffectHandler.Param;
import net.itsthesky.disky5.processor.handlers.EffectHandler.Slot;
import net.itsthesky.disky5.processor.util.Log;
import net.itsthesky.disky5.processor.util.NameUtils;
import net.itsthesky.disky5.processor.util.TypeUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BuilderHandler {

    private BuilderHandler() {}

    public static Map<String, NewSpec> newFactorySink() {
        return new LinkedHashMap<>();
    }

    public static Map<String, BuildSpec> buildSink() {
        return new LinkedHashMap<>();
    }

    public static void collectFactory(ProcessingEnvironment env, ExecutableElement method, New factory,
                                      Map<String, String> jdaFqnByCodeName, Map<String, NewSpec> sink) {
        List<String> patterns = List.of(factory.value());
        if (patterns.isEmpty()) {
            Log.error(env, "@New needs at least one pattern", method);
            return;
        }
        if (TypeUtils.isVoid(method.getReturnType())) {
            Log.error(env, "a @New factory must return the builder it creates", method);
            return;
        }

        List<Param> params = EffectHandler.params(method);
        List<Slot> slots = EffectHandler.validateBinding(env, method, patterns, params, jdaFqnByCodeName, "@New");
        if (slots == null) return;

        String className = "NewExpr_" + NameUtils.toClassName(EffectHandler.humanName(method));
        if (sink.containsKey(className)) {
            Log.error(env, "two @New factories would generate " + className
                + "; rename one so each keeps a stable class name", method);
            return;
        }

        ClassName builderType = ClassName.bestGuess(TypeUtils.boxed(
            TypeUtils.stripAnnotations(method.getReturnType().toString())));
        sink.put(className, new NewSpec(className, ClassName.get((TypeElement) method.getEnclosingElement()),
            method.getSimpleName().toString(), EffectHandler.humanName(method), patterns, slots, params,
            builderType, method.getAnnotation(Documentation.class)));
    }

    public static void collectBuild(ProcessingEnvironment env, ExecutableElement method,
                                    Map<String, BuildSpec> sink) {
        if (method.getParameters().size() != 1) {
            Log.error(env, "a @Build method must take exactly the builder it executes", method);
            return;
        }
        TypeElement descriptor = (TypeElement) method.getEnclosingElement();
        SkriptType type = descriptor.getAnnotation(SkriptType.class);
        if (type == null) {
            Log.error(env, "@Build must be declared on a @SkriptType descriptor (the builder's own)", method);
            return;
        }
        if (TypeUtils.isVoid(method.getReturnType())) {
            Log.error(env, "a @Build method must return what it creates (or a RestAction of it); an "
                + "action with nothing to hand back is an @Effect, not a builder", method);
            return;
        }

        boolean rest = TypeUtils.isRestAction(env, method.getReturnType());
        String resultFqn = TypeUtils.boxed(rest
            ? TypeUtils.soleTypeArgument(method.getReturnType())
            : TypeUtils.stripAnnotations(method.getReturnType().toString()));

        String className = "BuildExpr_" + NameUtils.toClassName(type.name());
        if (sink.containsKey(className)) {
            Log.error(env, "the builder '" + type.name() + "' already declares a @Build method", method);
            return;
        }

        ClassName builderType = ClassName.bestGuess(TypeUtils.stripAnnotations(
            method.getParameters().get(0).asType().toString()));
        sink.put(className, new BuildSpec(className, ClassName.get(descriptor),
            method.getSimpleName().toString(), builderType, type.name(), resultFqn, rest,
            method.getAnnotation(Documentation.class)));
    }

    public record NewSpec(String className, ClassName owner, String method, String humanName,
                          List<String> patterns, List<Slot> slots, List<Param> params,
                          ClassName builderType, Documentation docs) {}

    public record BuildSpec(String className, ClassName owner, String method, ClassName builderType,
                            String builderCodeName, String resultFqn, boolean rest, Documentation docs) {

        public List<String> patterns(String resultCodeName) {
            List<String> patterns = new ArrayList<>();
            patterns.add("[the] (build|built) result of [the] %" + builderCodeName + "%");
            if (resultCodeName != null)
                patterns.add("[the] built " + resultCodeName + " [from] [the] %" + builderCodeName + "%");
            return patterns;
        }
    }
}
