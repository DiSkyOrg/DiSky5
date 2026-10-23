package net.itsthesky.disky5.events.guildupdate;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild AFK Channel Event",
        patterns = {"[discord] guild afk channel (change|update)"},
        jda = GuildUpdateAfkChannelEvent.class
)
@Documentation(
        name = "Guild AFK Channel Event",
        description = "Fired when the AFK channel of a guild changes. Can be used to get the old/new channel, the author and the guild.",
        examples = {
            "on guild afk channel change:",
            "\tbroadcast \"Guild %event-guild% changed AFK channel from %past afk channel% to %current afk channel%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildAfkChannel {

    @EventValue
    public static Guild guild(GuildUpdateAfkChannelEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(GuildUpdateAfkChannelEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Channel pastChannel(GuildUpdateAfkChannelEvent event) {
        return event.getOldValue();
    }
}
