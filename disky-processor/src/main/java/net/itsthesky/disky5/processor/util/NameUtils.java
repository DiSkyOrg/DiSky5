package net.itsthesky.disky5.processor.util;

public final class NameUtils {
    private NameUtils() {}

    public static String toClassName(String name) {
        StringBuilder sb = new StringBuilder();
        for (String part : name.split("[ _]"))
            if (!part.isEmpty())
                sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        return sb.toString();
    }

    public static String entityName(String descriptorFqn) {
        return descriptorFqn.substring(descriptorFqn.lastIndexOf('.') + 1).replace("Type", "");
    }

    public static String wrapInQuotes(String s) {
        return "\"" + s.replace("\"", "\\\"") + "\"";
    }

    public static String capitalize(String s) {
        if (s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
