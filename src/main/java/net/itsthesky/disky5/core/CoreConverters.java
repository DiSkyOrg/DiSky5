package net.itsthesky.disky5.core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.itsthesky.disky5.annotations.Comparator;
import net.itsthesky.disky5.annotations.Converter;

public final class CoreConverters {
    private CoreConverters() {}

    @Converter
    public static User memberToUser(Member member) {
        return member.getUser();
    }

    @Converter
    public static User botToUser(Bot bot) {
        return bot.getSelfUser();
    }

    @Converter
    public static EmbedBuilder embedToBuilder(MessageEmbed embed) {
        return new EmbedBuilder(embed);
    }

    // Skript takes the first registered match for a runtime class, so separate Message /
    // IMentionable / ISnowflake converters would make the result depend on registration order.
    @Converter
    public static String snowflakeToString(ISnowflake snowflake) {
        if (snowflake instanceof Message message)
            return message.getContentRaw();
        if (snowflake instanceof IMentionable mentionable)
            return mentionable.getAsMention();
        return snowflake.getId();
    }

    @Converter
    public static Activity stringToActivity(String text) {
        return text.isBlank() ? null : Activity.playing(text);
    }

    @Comparator
    public static boolean memberIsUser(Member member, User user) {
        return member.getIdLong() == user.getIdLong();
    }

    @Comparator
    public static boolean channelIsType(Channel channel, ChannelType type) {
        return channel.getType() == type;
    }
}
