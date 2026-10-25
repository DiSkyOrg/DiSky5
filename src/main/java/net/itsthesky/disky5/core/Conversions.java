package net.itsthesky.disky5.core;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.messages.MessagePoll;
import net.dv8tion.jda.api.entities.sticker.Sticker;
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
        // Same rendering as V4, so "%event-guild%" in a script still prints a name, not JDA's toString.
        return switch (value) {
            case Emoji emoji -> emoji.getFormatted();
            case Message message -> message.getContentRaw();
            case Member member -> member.getUser().getEffectiveName();
            case User user -> user.getEffectiveName();
            case Channel channel -> channel.getName();
            case Guild guild -> guild.getName();
            case Role role -> role.getName();
            case ScheduledEvent event -> event.getName();
            case Webhook webhook -> webhook.getName();
            case ForumTag tag -> tag.getName();
            case Sticker sticker -> sticker.getName();
            case Activity activity -> activity.getName();
            case Message.Attachment attachment -> attachment.getUrl();
            case Invite invite -> invite.getUrl();
            case MessagePoll poll -> poll.getQuestion().getText();
            case null, default -> String.valueOf(value);
        };
    }

    @Contract("null -> null")
    public static java.awt.@Nullable Color awt(@Nullable Color color) {
        if (color == null) return null;
        org.bukkit.Color bukkit = color.asBukkitColor();
        return new java.awt.Color(bukkit.getRed(), bukkit.getGreen(), bukkit.getBlue());
    }
}
