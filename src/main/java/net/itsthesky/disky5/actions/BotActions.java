package net.itsthesky.disky5.actions;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Bot;
import org.jetbrains.annotations.Nullable;

public final class BotActions {

    private BotActions() {}

    @Effect("[discord] connect [the] [bot] %bot% to [the] [(audio|voice)] [channel] %audiochannel%")
    @Documentation(
        name = "Connect Bot To Voice",
        description = {"Connects a bot to an audio (voice or stage) channel.",
            "The bot needs the Connect permission on that channel."},
        examples = {"connect bot \"MyBot\" to voice channel with id \"000000000000000000\""},
        since = "5.0.0-a1")
    public static void connectBot(Bot bot, AudioChannel channel) {
        Guild guild = resolveGuild(bot, channel.getGuild());
        AudioChannel target = guild.getChannelById(AudioChannel.class, channel.getId());
        if (target == null)
            throw new IllegalArgumentException("the bot '" + bot + "' cannot see the audio channel '"
                + channel.getName() + "'");
        guild.getAudioManager().openAudioConnection(target);
    }

    @Effect("disconnect [[the] [bot] %-bot%] from [the] [guild] %guild%")
    @Documentation(
        name = "Disconnect Bot From Voice",
        description = {"Closes a bot's audio connection in a guild.",
            "Without a bot, the one owning the given guild is used."},
        examples = {"disconnect from event-guild",
            "disconnect bot \"MyBot\" from event-guild"},
        since = "5.0.0-a1")
    public static void disconnectBot(@Nullable Bot bot, Guild guild) {
        Guild target = bot == null ? guild : resolveGuild(bot, guild);
        target.getAudioManager().closeAudioConnection();
    }

    private static Guild resolveGuild(Bot bot, Guild guild) {
        Guild own = bot.getGuildById(guild.getId());
        if (own == null)
            throw new IllegalArgumentException("the bot '" + bot + "' is not in the guild '"
                + guild.getName() + "'");
        return own;
    }
}
