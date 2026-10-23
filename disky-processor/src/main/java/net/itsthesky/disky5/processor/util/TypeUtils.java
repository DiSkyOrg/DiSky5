package net.itsthesky.disky5.processor.util;

import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.SkriptType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public final class TypeUtils {
    private TypeUtils() {}

    public static final String REST_ACTION = "net.dv8tion.jda.api.requests.RestAction";

    public static boolean isRestAction(ProcessingEnvironment env, TypeMirror type) {
        if (type == null || type.getKind() != TypeKind.DECLARED)
            return false;
        TypeElement restElement = env.getElementUtils().getTypeElement(REST_ACTION);
        if (restElement == null)
            return false;
        TypeMirror restErasure = env.getTypeUtils().erasure(restElement.asType());
        TypeMirror typeErasure = env.getTypeUtils().erasure(type);
        return env.getTypeUtils().isAssignable(typeErasure, restErasure);
    }

    public static boolean isVoid(TypeMirror type) {
        return type == null || type.getKind() == TypeKind.VOID;
    }

    public static String soleTypeArgument(TypeMirror type) {
        if (type instanceof DeclaredType declared && declared.getTypeArguments().size() == 1)
            return stripAnnotations(declared.getTypeArguments().get(0).toString());
        return "java.lang.Object";
    }

    // Calling a Class<?> annotation member at processor time always throws MirroredTypeException -
    // the standard APT idiom for reading it; the throw below is genuinely unreachable.
    public static String resolveJdaClass(TypeElement descriptor) {
        try {
            descriptor.getAnnotation(SkriptType.class).jda();
            throw new IllegalStateException("unreachable: MirroredTypeException always thrown by javac");
        } catch (MirroredTypeException ex) {
            return ex.getTypeMirror().toString();
        }
    }

    public static String resolveEnumClass(TypeElement holder) {
        try {
            holder.getAnnotation(SkriptEnum.class).jda();
            throw new IllegalStateException("unreachable: MirroredTypeException always thrown by javac");
        } catch (MirroredTypeException ex) {
            return ex.getTypeMirror().toString();
        }
    }

    public static String resolveParentClass(TypeElement descriptor) {
        String fqn;
        try {
            descriptor.getAnnotation(SkriptType.class).parent();
            throw new IllegalStateException("unreachable: MirroredTypeException always thrown by javac");
        } catch (MirroredTypeException ex) {
            fqn = ex.getTypeMirror().toString();
        }
        return "java.lang.Void".equals(fqn) ? null : fqn;
    }

    public static String stripAnnotations(String type) {
        return type.replaceAll("@[^ ]+\\s*", "").trim();
    }

    public static String paramType(String type) {
        return stripAnnotations(type).replaceAll("<.*>", "").trim();
    }

    public static String collectionElement(String type) {
        if (type.endsWith("[]"))
            return type.substring(0, type.length() - 2).trim();
        int lt = type.indexOf('<');
        if (lt < 0 || !type.endsWith(">"))
            return null;
        String raw = type.substring(0, lt).trim();
        if (!raw.equals("java.util.List")
                && !raw.equals("java.util.Collection")
                && !raw.equals("java.util.Set"))
            return null;
        return type.substring(lt + 1, type.length() - 1).trim();
    }

    public static boolean isArray(String type) {
        return type.endsWith("[]");
    }

    public static String arrayComponent(String type) {
        return isArray(type) ? type.substring(0, type.length() - 2).trim() : type;
    }

    public static String boxed(String type) {
        return switch (type) {
            case "int"     -> "java.lang.Integer";
            case "long"    -> "java.lang.Long";
            case "boolean" -> "java.lang.Boolean";
            case "double"  -> "java.lang.Double";
            case "float"   -> "java.lang.Float";
            case "short"   -> "java.lang.Short";
            case "byte"    -> "java.lang.Byte";
            case "char"    -> "java.lang.Character";
            case "void"    -> "java.lang.Void";
            default        -> type;
        };
    }
}
