package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.Nullable;

public final class Emojis {

    private Emojis() {}

    public static @Nullable Emoji from(@Nullable Object raw) {
        if (raw == null) return null;
        if (raw instanceof Emoji emoji) return emoji;
        String text = String.valueOf(raw).trim();
        return text.isEmpty() ? null : Emoji.fromFormatted(text);
    }
}
