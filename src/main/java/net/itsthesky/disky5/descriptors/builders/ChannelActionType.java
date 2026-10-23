package net.itsthesky.disky5.descriptors.builders;

import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "channelaction", jda = ChannelAction.class)
@Documentation(
    name = "Channel Builder",
    description = {"A channel that does not exist yet: configure it, then create it with 'the build result of'.",
        "Nothing is sent to Discord until then, so setting its properties costs no request."},
    since = "5.0.0-a1")
@SuppressWarnings({"rawtypes", "unchecked"})
public final class ChannelActionType {

    private ChannelActionType() {}

    private static final int MAX_CHANNELS_PER_GUILD = 500;

    @New("[a] new text[( |-)]channel builder [(named|with [the] name)] %string% in [the] [guild] %guild%")
    @Documentation(
        name = "New Text Channel Builder",
        description = "Creates a text channel builder. The channel only exists once you read "
            + "'the build result of' it, which requires 'await'.",
        examples = {"set {_b} to a new text channel builder named \"general\" in event-guild",
            "set topic of {_b} to \"Talk about anything here\"",
            "await set {_channel} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ChannelAction newTextChannelBuilder(String name, Guild guild) {
        return guild.createTextChannel(name);
    }

    @New("[a] new news[( |-)]channel builder [(named|with [the] name)] %string% in [the] [guild] %guild%")
    @Documentation(
        name = "New News Channel Builder",
        description = "Creates an announcement (news) channel builder.",
        examples = {"set {_b} to a new news channel builder named \"updates\" in event-guild",
            "await set {_channel} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ChannelAction newNewsChannelBuilder(String name, Guild guild) {
        return guild.createNewsChannel(name);
    }

    @New("[a] new voice[( |-)]channel builder [(named|with [the] name)] %string% in [the] [guild] %guild%")
    @Documentation(
        name = "New Voice Channel Builder",
        description = "Creates a voice channel builder.",
        examples = {"set {_b} to a new voice channel builder named \"Lounge\" in event-guild",
            "set bitrate of {_b} to 64000",
            "await set {_channel} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ChannelAction newVoiceChannelBuilder(String name, Guild guild) {
        return guild.createVoiceChannel(name);
    }

    @New("[a] new stage[( |-)]channel builder [(named|with [the] name)] %string% in [the] [guild] %guild%")
    @Documentation(
        name = "New Stage Channel Builder",
        description = "Creates a stage channel builder.",
        examples = {"set {_b} to a new stage channel builder named \"Conference\" in event-guild",
            "await set {_channel} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ChannelAction newStageChannelBuilder(String name, Guild guild) {
        return guild.createStageChannel(name);
    }

    @New("[a] new forum[( |-)]channel builder [(named|with [the] name)] %string% in [the] [guild] %guild%")
    @Documentation(
        name = "New Forum Channel Builder",
        description = "Creates a forum channel builder.",
        examples = {"set {_b} to a new forum channel builder named \"help\" in event-guild",
            "await set {_channel} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ChannelAction newForumChannelBuilder(String name, Guild guild) {
        return guild.createForumChannel(name);
    }

    @New("[a] new media[( |-)]channel builder [(named|with [the] name)] %string% in [the] [guild] %guild%")
    @Documentation(
        name = "New Media Channel Builder",
        description = "Creates a media channel builder.",
        examples = {"set {_b} to a new media channel builder named \"gallery\" in event-guild",
            "await set {_channel} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ChannelAction newMediaChannelBuilder(String name, Guild guild) {
        return guild.createMediaChannel(name);
    }

    @New("[a] new category builder [(named|with [the] name)] %string% in [the] [guild] %guild%")
    @Documentation(
        name = "New Category Builder",
        description = "Creates a category builder. Other channel builders can then be parented to "
            + "the built category.",
        examples = {"set {_b} to a new category builder named \"Staff\" in event-guild",
            "await set {_category} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ChannelAction newCategoryBuilder(String name, Guild guild) {
        return guild.createCategory(name);
    }

    @Build
    @Documentation(
        name = "Built Channel",
        description = {"Creates the channel on Discord and returns it. Requires 'await'.",
            "Building the same builder twice creates two channels."},
        examples = {"await set {_channel} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static RestAction<GuildChannel> build(ChannelAction action) {
        if (action.getGuild().getChannels().size() >= MAX_CHANNELS_PER_GUILD)
            throw new IllegalStateException("this guild already has the maximum of "
                + MAX_CHANNELS_PER_GUILD + " channels");
        return action;
    }

    @Property.Setter("topic")
    @Documentation(
        name = "Channel Builder Topic",
        description = "The topic of the channel being built. Text, news, forum and media channels only.",
        examples = {"set topic of {_b} to \"Rules and announcements\""},
        since = "5.0.0-a1")
    public static void setTopic(ChannelAction action, @Nullable String value) {
        action.setTopic(value);
    }

    @Property.Setter("nsfw")
    @Documentation(
        name = "Channel Builder NSFW",
        description = "Whether the channel being built is marked as age-restricted.",
        examples = {"set nsfw of {_b} to true"},
        since = "5.0.0-a1")
    public static void setNsfw(ChannelAction action, Boolean value) {
        if (value != null)
            action.setNSFW(value);
    }

    @Property.Setter("slowmode")
    @Documentation(
        name = "Channel Builder Slowmode",
        description = "The slowmode of the channel being built, in seconds (0 disables it).",
        examples = {"set slowmode of {_b} to 10"},
        since = "5.0.0-a1")
    public static void setSlowmode(ChannelAction action, Integer value) {
        if (value != null)
            action.setSlowmode(value);
    }

    @Property.Setter("bitrate")
    @Documentation(
        name = "Channel Builder Bitrate",
        description = "The audio bitrate (in bits per second) of the voice/stage channel being built.",
        examples = {"set bitrate of {_b} to 96000"},
        since = "5.0.0-a1")
    public static void setBitrate(ChannelAction action, @Nullable Integer value) {
        action.setBitrate(value);
    }

    @Property.Setter("user limit")
    @Documentation(
        name = "Channel Builder User Limit",
        description = "How many members may join the voice channel being built at once (0 = no limit).",
        examples = {"set user limit of {_b} to 5"},
        since = "5.0.0-a1")
    public static void setUserLimit(ChannelAction action, @Nullable Integer value) {
        action.setUserlimit(value);
    }

    @Property.Setter("region")
    @Documentation(
        name = "Channel Builder Region",
        description = "The raw voice region key of the audio channel being built, or none for automatic.",
        examples = {"set region of {_b} to \"europe\""},
        since = "5.0.0-a1")
    public static void setRegion(ChannelAction action, @Nullable String value) {
        action.setRegion(value == null ? null : Region.fromKey(value));
    }

    @Property.Setter("channel position")
    @Documentation(
        name = "Channel Builder Position",
        description = "The position of the channel being built in its guild's channel list.",
        examples = {"set channel position of {_b} to 1"},
        since = "5.0.0-a1")
    public static void setChannelPosition(ChannelAction action, @Nullable Integer value) {
        action.setPosition(value);
    }

    @Property.Setter("category")
    @Documentation(
        name = "Channel Builder Category",
        description = "The category the channel being built will be placed under.",
        examples = {"set category of {_b} to category with id \"000\""},
        since = "5.0.0-a1")
    public static void setCategory(ChannelAction action, @Nullable Category value) {
        action.setParent(value);
    }
}
