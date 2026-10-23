package net.itsthesky.disky5.api.commands;

import ch.njol.skript.Skript;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ArgumentSpecParser {

    public record Segment(@Nullable String name, String typeName, @Nullable String defaultValue, boolean optional, int index) {}

    private ArgumentSpecParser() {}

    public static @Nullable List<Segment> scan(String spec, String what) {
        List<Segment> result = new ArrayList<>();
        int optionalDepth = 0;
        boolean sawOptional = false;
        int i = 0;
        while (i < spec.length()) {
            char c = spec.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }
            if (c == '[') { optionalDepth++; i++; continue; }
            if (c == ']') {
                if (--optionalDepth < 0) {
                    Skript.error("Unbalanced ']' in the arguments of the " + what);
                    return null;
                }
                i++;
                continue;
            }
            if (c != '<') {
                Skript.error("Unexpected '" + c + "' in the arguments of the " + what + "; expected <type>, "
                    + "<name: type>, <name: type = default> or [<optional>]");
                return null;
            }
            int close = spec.indexOf('>', i);
            if (close < 0) {
                Skript.error("Missing '>' in the arguments of the " + what);
                return null;
            }
            boolean optional = optionalDepth > 0;
            if (!optional && sawOptional) {
                Skript.error("A required argument cannot follow an optional one in a " + what);
                return null;
            }
            sawOptional |= optional;
            Segment segment = segment(spec.substring(i + 1, close), optional, result.size());
            if (segment == null)
                return null;
            result.add(segment);
            i = close + 1;
        }
        if (optionalDepth != 0) {
            Skript.error("Unbalanced '[' in the arguments of the " + what);
            return null;
        }
        return result;
    }

    private static @Nullable Segment segment(String inner, boolean optional, int index) {
        String name = null, typeName = inner.trim(), def = null;
        int eq = typeName.indexOf('=');
        if (eq >= 0) {
            def = typeName.substring(eq + 1).trim();
            typeName = typeName.substring(0, eq).trim();
        }
        int colon = typeName.indexOf(':');
        if (colon >= 0) {
            name = typeName.substring(0, colon).trim();
            typeName = typeName.substring(colon + 1).trim();
            if (name.isEmpty() || name.contains("::") || name.contains("{") || name.contains("}")) {
                Skript.error("'" + name + "' is not a valid argument name (it becomes the local variable {_" + name + "})");
                return null;
            }
        }
        return new Segment(name, typeName, def, optional, index);
    }
}
