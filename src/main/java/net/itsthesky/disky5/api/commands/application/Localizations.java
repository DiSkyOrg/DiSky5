package net.itsthesky.disky5.api.commands.application;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.SimpleNode;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class Localizations {

    private Localizations() {}

    public static @Nullable DiscordLocale parse(String key) {
        String k = key.trim();
        if (k.equalsIgnoreCase("en")) k = "en-US";
        DiscordLocale locale = DiscordLocale.from(k);
        if (locale == DiscordLocale.UNKNOWN) {
            for (DiscordLocale candidate : DiscordLocale.values())
                if (candidate != DiscordLocale.UNKNOWN && (candidate.name().equalsIgnoreCase(k.replace(' ', '_'))
                        || candidate.getLanguageName().equalsIgnoreCase(k)))
                    return candidate;
            return null;
        }
        return locale;
    }

    public static String available() {
        List<String> codes = new ArrayList<>();
        for (DiscordLocale locale : DiscordLocale.values())
            if (locale != DiscordLocale.UNKNOWN) codes.add(locale.getLocale());
        return String.join(", ", codes);
    }

    public static @Nullable Map<DiscordLocale, String> parse(SectionNode section) {
        Map<DiscordLocale, String> map = new LinkedHashMap<>();
        for (Node node : section) {
            if (!(node instanceof SimpleNode) || node.getKey() == null) {
                Skript.error("Expected 'locale: text' lines, got '" + node.getKey() + "'");
                return null;
            }
            String[] parts = node.getKey().split(":", 2);
            if (parts.length < 2 || parts[1].isBlank()) {
                Skript.error("Expected 'locale: text', got '" + node.getKey() + "'");
                return null;
            }
            DiscordLocale locale = parse(parts[0]);
            if (locale == null) {
                Skript.error("'" + parts[0].trim() + "' is not a Discord locale (available: " + available() + ")");
                return null;
            }
            map.put(locale, unquote(parts[1].trim()));
        }
        return map;
    }

    public static List<String> describe(LocalizationMap map) {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<DiscordLocale, String> entry : map.toMap().entrySet())
            lines.add(entry.getKey().getLocale().toLowerCase(Locale.ROOT) + ": " + entry.getValue());
        return lines;
    }

    private static String unquote(String text) {
        return text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"") ? text.substring(1, text.length() - 1) : text;
    }
}
