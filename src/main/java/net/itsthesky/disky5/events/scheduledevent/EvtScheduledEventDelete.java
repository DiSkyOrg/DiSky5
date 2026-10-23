package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.events.guild.scheduledevent.ScheduledEventDeleteEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Scheduled Event Delete",
        patterns = {"[discord] scheduled[( |-)]event delet(e|ion)"},
        jda = ScheduledEventDeleteEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event Delete",
        description = {
                "Fired when a scheduled event is deleted from a guild. Can be used to get the scheduled event and the guild it was deleted from.",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event deletion:",
                "\tbroadcast \"Scheduled event '%event-scheduledevent's name%' was deleted from %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventDelete {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventDeleteEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventDeleteEvent event) {
        return event.getGuild();
    }
}
