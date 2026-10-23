package net.itsthesky.disky5.core;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.ColorRGB;
import ch.njol.skript.util.Date;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.OffsetDateTime;

public final class Conversions {

    private Conversions() {}

    @Contract("null -> null")
    public static @Nullable Date date(@Nullable OffsetDateTime dateTime) {
        return dateTime == null ? null : new Date(dateTime.toInstant().toEpochMilli());
    }

    @Contract("null -> null")
    public static @Nullable Date date(@Nullable Instant instant) {
        return instant == null ? null : new Date(instant.toEpochMilli());
    }

    public static Date date(long epochMillis) {
        return new Date(epochMillis);
    }

    @Contract("null -> null")
    public static @Nullable Color color(java.awt.@Nullable Color color) {
        return color == null ? null : new ColorRGB(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String display(@Nullable Object value) {
        if (value instanceof Emoji emoji)
            return emoji.getFormatted();
        return String.valueOf(value);
    }

    @Contract("null -> null")
    public static java.awt.@Nullable Color awt(@Nullable Color color) {
        if (color == null) return null;
        org.bukkit.Color bukkit = color.asBukkitColor();
        return new java.awt.Color(bukkit.getRed(), bukkit.getGreen(), bukkit.getBlue());
    }
}
