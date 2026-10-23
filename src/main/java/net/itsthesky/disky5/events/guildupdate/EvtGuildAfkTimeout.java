package net.itsthesky.disky5.events.guildupdate;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkTimeoutEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild AFK Timeout Event",
        patterns = {"[discord] guild afk timeout (change|update)"},
        jda = GuildUpdateAfkTimeoutEvent.class
)
@Documentation(
        name = "Guild AFK Timeout Event",
        description = "Fired when the AFK timeout of a guild changes. Can be used to get the old/new timeout value, the author and the guild.",
        examples = {
            "on guild afk timeout change:",
            "\tbroadcast \"Guild %event-guild% changed AFK timeout from %past afk timeout% to %current afk timeout%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildAfkTimeout {

    @EventValue
    public static Guild guild(GuildUpdateAfkTimeoutEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Guild.Timeout afkTimeout(GuildUpdateAfkTimeoutEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Guild.Timeout pastAfkTimeout(GuildUpdateAfkTimeoutEvent event) {
        return event.getOldValue();
    }
}
