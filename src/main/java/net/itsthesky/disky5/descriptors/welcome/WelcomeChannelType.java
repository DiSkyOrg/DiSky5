package net.itsthesky.disky5.descriptors.welcome;

import net.dv8tion.jda.api.entities.GuildWelcomeScreen;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildChannel;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.Emojis;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "welcomechannel", jda = GuildWelcomeScreen.Channel.class)
@Documentation(
    name = "Welcome Channel",
    description = {"One entry of a welcome screen: a channel, the short text shown next to it and an optional emoji.",
        "Immutable: to change one, create a new one and replace it in the builder."},
    since = "5.0.0-a2")
public final class WelcomeChannelType {

    private WelcomeChannelType() {}

    @New("[a] new welcome channel for [the] [channel] %guildchannel% (with|and) [the] description %string% [(with|and) [the] emoji %-emote/string%]")
    @Documentation(
        name = "New Welcome Channel",
        description = {"Creates a welcome screen entry. The description is at most 42 characters; "
            + "the channel must be a text, voice, stage, news or forum channel (not a category or thread).",
            "The emoji is an emote, a unicode emoji or the '<:name:id>' form of a custom one."},
        examples = {"set {_rules} to a new welcome channel for channel with id \"123\" with description \"Read our rules\" with emoji \"📜\"",
            "add {_rules} to welcome channels of {_builder}"},
        since = "5.0.0-a2")
    public static GuildWelcomeScreen.Channel newWelcomeChannel(GuildChannel channel, String description,
                                                              @Nullable Object emoji) {
        if (!(channel instanceof StandardGuildChannel standard) || channel instanceof Category)
            throw new IllegalArgumentException("the channel '" + channel.getName()
                + "' cannot be put on a welcome screen");
        return GuildWelcomeScreen.Channel.of(standard, description, Emojis.from(emoji));
    }

    @Property("welcome channel")
    @Documentation(
        name = "Welcome Channel Target",
        description = "The channel a welcome screen entry points to. Empty when the bot cannot see it.",
        examples = {"reply with welcome channel of {_entry}"},
        since = "5.0.0-a2")
    public static @Nullable GuildChannel channel(GuildWelcomeScreen.Channel entry) {
        return entry.getChannel();
    }

    @Property("description")
    public static String description(GuildWelcomeScreen.Channel entry) {
        return entry.getDescription();
    }

    @Property("emoji")
    public static @Nullable String emoji(GuildWelcomeScreen.Channel entry) {
        return entry.getEmoji() == null ? null : entry.getEmoji().getFormatted();
    }
}
