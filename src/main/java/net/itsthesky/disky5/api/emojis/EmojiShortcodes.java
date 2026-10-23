package net.itsthesky.disky5.api.emojis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class EmojiShortcodes {

    private EmojiShortcodes() {}

    private static final String RESOURCE = "/emojis/discord-emoji-definition.json";

    public static @Nullable String unicodeOf(String shortcode) {
        String key = shortcode.trim().toLowerCase(Locale.ROOT);
        if (key.length() > 2 && key.startsWith(":") && key.endsWith(":"))
            key = key.substring(1, key.length() - 1);
        return key.isEmpty() ? null : Table.BY_SHORTCODE.get(key);
    }

    public static @Nullable String knownUnicode(String text) {
        if (Table.UNICODES.contains(text)) return text;
        // Keyboards and copy-pastes often drop or add the U+FE0F presentation selector.
        String withSelector = text + "️";
        if (Table.UNICODES.contains(withSelector)) return withSelector;
        String withoutSelector = text.replace("️", "");
        return Table.UNICODES.contains(withoutSelector) ? withoutSelector : null;
    }

    private static final class Table {
        static final Map<String, String> BY_SHORTCODE = new HashMap<>();
        static final Set<String> UNICODES;

        static {
            Map<String, List<String>> raw = load();
            UNICODES = Set.copyOf(raw.keySet());
            raw.forEach((unicode, aliases) -> {
                for (String alias : aliases) {
                    if (alias.length() > 2 && alias.startsWith(":") && alias.endsWith(":"))
                        BY_SHORTCODE.putIfAbsent(alias.substring(1, alias.length() - 1).toLowerCase(Locale.ROOT), unicode);
                }
            });
        }

        private static Map<String, List<String>> load() {
            try (InputStream in = EmojiShortcodes.class.getResourceAsStream(RESOURCE)) {
                if (in == null)
                    throw new IllegalStateException("missing emoji table " + RESOURCE + " in the DiSky jar");
                try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                    return new Gson().fromJson(reader, new TypeToken<Map<String, List<String>>>() {}.getType());
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
