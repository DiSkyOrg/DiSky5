package net.itsthesky.disky5.api.syntax;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionInfo;
import ch.njol.skript.lang.SyntaxElementInfo;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SyntaxCollisionCheck {

    private SyntaxCollisionCheck() {}

    private static final String DISKY_PACKAGE = "net.itsthesky.disky5.";

    private static final int MAX_VARIANTS = 96;

    private static final char SLOT = '\0';

    public static void run() {
        List<Entry> registry = new ArrayList<>();
        for (SyntaxElementInfo<?> info : Skript.getEffects())    collect(registry, info, 1);
        for (SyntaxElementInfo<?> info : Skript.getConditions()) collect(registry, info, 1);
        for (SyntaxElementInfo<?> info : Skript.getSections())   collect(registry, info, 1);
        Iterator<ExpressionInfo<?, ?>> expressions = Skript.getExpressions();
        while (expressions.hasNext()) {
            ExpressionInfo<?, ?> info = expressions.next();
            collect(registry, info, info.getExpressionType().ordinal());
        }

        Map<String, Entry> owners = new LinkedHashMap<>();
        registry.sort((a, b) -> a.priority != b.priority ? Integer.compare(a.priority, b.priority)
            : Integer.compare(a.index, b.index));
        for (Entry entry : registry)
            for (String shape : entry.shapes)
                owners.putIfAbsent(shape, entry);

        List<String> shadowed = new ArrayList<>();
        for (Entry entry : registry) {
            if (!entry.mine) continue;
            for (String shape : entry.shapes) {
                Entry owner = owners.get(shape);
                if (owner == null || owner == entry || owner.mine) continue;
                shadowed.add(readable(shape) + "  ->  tried first by " + owner.owner);
                break;
            }
        }

        if (shadowed.isEmpty())
            return;

        Bukkit.getConsoleSender().sendMessage("[DiSky] " + shadowed.size()
            + " syntax(es) are ambiguous with an earlier-registered one. On an untyped variable the");
        Bukkit.getConsoleSender().sendMessage("[DiSky] other syntax is tried first, and if it accepts the operand DiSky's never runs:");
        for (String line : shadowed)
            Bukkit.getConsoleSender().sendMessage("[DiSky]   " + line);
    }

    private static void collect(List<Entry> registry, SyntaxElementInfo<?> info, int priority) {
        Class<?> element = info.getElementClass();
        boolean mine = element.getName().startsWith(DISKY_PACKAGE);
        Set<String> shapes = new LinkedHashSet<>();
        for (String pattern : info.getPatterns())
            if (pattern != null)
                shapes.addAll(shapes(pattern));
        if (!shapes.isEmpty())
            registry.add(new Entry(mine, element.getName(), shapes, priority, registry.size()));
    }

    static Set<String> shapes(String pattern) {
        String base = pattern
            .replaceAll("%[^%]*%", String.valueOf(SLOT))
            .replaceAll("<[^<>]*>", String.valueOf(SLOT));

        List<String> variants = new ArrayList<>();
        variants.add(base);
        for (int round = 0; round < 24; round++) {
            List<String> next = new ArrayList<>();
            boolean changed = false;
            for (String variant : variants) {
                int open = innermost(variant, '(', ')');
                if (open >= 0) {
                    int close = matching(variant, open, '(', ')');
                    if (close > open) {
                        changed = true;
                        for (String alternative : split(variant.substring(open + 1, close)))
                            next.add(variant.substring(0, open) + strip(alternative) + variant.substring(close + 1));
                        continue;
                    }
                }
                open = innermost(variant, '[', ']');
                if (open >= 0) {
                    int close = matching(variant, open, '[', ']');
                    if (close > open) {
                        changed = true;
                        next.add(variant.substring(0, open) + strip(variant.substring(open + 1, close))
                            + variant.substring(close + 1));
                        next.add(variant.substring(0, open) + variant.substring(close + 1));
                        continue;
                    }
                }
                next.add(variant);
            }
            variants = next.size() > MAX_VARIANTS ? next.subList(0, MAX_VARIANTS) : next;
            if (!changed) break;
        }

        Set<String> shapes = new LinkedHashSet<>();
        for (String variant : variants) {
            // strip(), not trim(): trim() drops every char <= U+0020, which would eat the NUL slot
            // markers at both ends and make "await <.+>" look like a bare "await".
            String normalised = variant.replaceAll("\\s+", " ").strip();
            if (!normalised.isEmpty() && normalised.indexOf('[') < 0 && normalised.indexOf('(') < 0)
                shapes.add(normalised);
        }
        return shapes;
    }

    private static int innermost(String s, char open, char close) {
        int candidate = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == open) candidate = i;
            else if (c == close && candidate >= 0) return candidate;
        }
        return -1;
    }

    private static int matching(String s, int open, char openChar, char closeChar) {
        return s.indexOf(closeChar, open + 1);
    }

    private static List<String> split(String group) {
        List<String> parts = new ArrayList<>();
        int depth = 0, start = 0;
        for (int i = 0; i < group.length(); i++) {
            char c = group.charAt(i);
            if (c == '(' || c == '[') depth++;
            else if (c == ')' || c == ']') depth--;
            else if (c == '|' && depth == 0) { parts.add(group.substring(start, i)); start = i + 1; }
        }
        parts.add(group.substring(start));
        return parts;
    }

    private static String strip(String alternative) {
        String stripped = alternative.replaceAll("^\\s*\\d+\\u00A6", "");
        int colon = stripped.indexOf(':');
        if (colon > 0 && stripped.lastIndexOf(' ', colon) < 0)
            stripped = stripped.substring(colon + 1);
        return stripped;
    }

    private static String readable(String shape) {
        return shape.replace(String.valueOf(SLOT), "%..%");
    }

    private record Entry(boolean mine, String owner, Set<String> shapes, int priority, int index) {}
}
