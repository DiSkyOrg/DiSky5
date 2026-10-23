package net.itsthesky.disky5.events.guildupdate;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Boost Count Update",
        patterns = {"[discord] guild boost count (change|update)"},
        jda = GuildUpdateBoostCountEvent.class
)
@Documentation(
        name = "Guild Boost Count Update",
        description = "Fired when the boost count of a guild changes. Can be used to get the old/new count, and the guild.",
        examples = {
            "on guild boost count change:",
            "\tbroadcast \"Guild %event-guild% boost count changed from %past boost count% to %current boost count%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildBoostCount {

    @EventValue
    public static Guild guild(GuildUpdateBoostCountEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Integer boostCount(GuildUpdateBoostCountEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Integer pastBoostCount(GuildUpdateBoostCountEvent event) {
        return event.getOldValue();
    }
}
