package net.itsthesky.disky5.core;

import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.attribute.ISlowmodeChannel;
import net.dv8tion.jda.api.entities.channel.attribute.IThreadContainer;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.MediaChannel;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.itsthesky.disky5.annotations.Converter;
import org.jetbrains.annotations.Nullable;

public final class ChannelConverters {

    private ChannelConverters() {}

    @Converter
    public static @Nullable MessageChannel toMessageChannel(Channel channel) {
        return channel instanceof MessageChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable GuildChannel toGuildChannel(Channel channel) {
        return channel instanceof GuildChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable StandardGuildChannel toStandardGuildChannel(Channel channel) {
        return channel instanceof StandardGuildChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable StandardGuildMessageChannel toStandardGuildMessageChannel(Channel channel) {
        return channel instanceof StandardGuildMessageChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable TextChannel toTextChannel(Channel channel) {
        return channel instanceof TextChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable NewsChannel toNewsChannel(Channel channel) {
        return channel instanceof NewsChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable VoiceChannel toVoiceChannel(Channel channel) {
        return channel instanceof VoiceChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable StageChannel toStageChannel(Channel channel) {
        return channel instanceof StageChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable AudioChannel toAudioChannel(Channel channel) {
        return channel instanceof AudioChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable ThreadChannel toThreadChannel(Channel channel) {
        return channel instanceof ThreadChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable IThreadContainer toThreadContainer(Channel channel) {
        return channel instanceof IThreadContainer narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable ISlowmodeChannel toSlowmodeChannel(Channel channel) {
        return channel instanceof ISlowmodeChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable Category toCategory(Channel channel) {
        return channel instanceof Category narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable ForumChannel toForumChannel(Channel channel) {
        return channel instanceof ForumChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable MediaChannel toMediaChannel(Channel channel) {
        return channel instanceof MediaChannel narrowed ? narrowed : null;
    }

    @Converter
    public static @Nullable PrivateChannel toPrivateChannel(Channel channel) {
        return channel instanceof PrivateChannel narrowed ? narrowed : null;
    }
}
