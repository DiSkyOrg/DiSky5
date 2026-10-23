package net.itsthesky.disky5.processor.handlers;

import net.itsthesky.disky5.annotations.Getter;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.processor.util.TypeUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GetterHandler {

    private GetterHandler() {}

    public enum Scope { BOT, GUILD, CHANNEL }

    public static final class GetterSpec {
        public final String name;
        public Scope scope;
        public String holderFqn;
        public String returnType;
        public String cacheMethod;
        public String retrieveMethod;
        public boolean retrieveIsRestAction;
        public Documentation docs;

        GetterSpec(String name) { this.name = name; }
    }

    public static void collect(ExecutableElement method, Map<String, GetterSpec> sink) {
        Getter cache = method.getAnnotation(Getter.class);
        Getter.Retrieve retrieve = method.getAnnotation(Getter.Retrieve.class);
        if (cache == null && retrieve == null) return;

        String name = cache != null ? cache.value() : retrieve.value();
        GetterSpec spec = sink.computeIfAbsent(name, GetterSpec::new);

        spec.holderFqn = ((javax.lang.model.element.TypeElement) method.getEnclosingElement())
            .getQualifiedName().toString();
        spec.scope = scopeOf(method);

        Documentation docs = method.getAnnotation(Documentation.class);
        if (docs != null) spec.docs = docs;

        if (cache != null) {
            spec.cacheMethod = method.getSimpleName().toString();
            spec.returnType = TypeUtils.stripAnnotations(method.getReturnType().toString());
        }
        if (retrieve != null) {
            spec.retrieveMethod = method.getSimpleName().toString();
            TypeMirror ret = method.getReturnType();
            String retStr = TypeUtils.stripAnnotations(ret.toString());
            if (retStr.contains("RestAction")) {
                spec.retrieveIsRestAction = true;
                String element = restActionElement(ret);
                if (spec.returnType == null && element != null) spec.returnType = element;
            } else if (spec.returnType == null) {
                spec.returnType = retStr;
            }
        }
    }

    private static Scope scopeOf(ExecutableElement method) {
        List<? extends javax.lang.model.element.VariableElement> params = method.getParameters();
        String scopeType = TypeUtils.stripAnnotations(params.get(0).asType().toString());
        if (scopeType.endsWith("Guild")) return Scope.GUILD;
        if (scopeType.endsWith("MessageChannel")) return Scope.CHANNEL;
        return Scope.BOT;
    }

    private static String restActionElement(TypeMirror ret) {
        if (ret instanceof DeclaredType declared && !declared.getTypeArguments().isEmpty())
            return TypeUtils.stripAnnotations(declared.getTypeArguments().get(0).toString());
        return null;
    }

    public static Map<String, GetterSpec> newSink() {
        return new LinkedHashMap<>();
    }
}
