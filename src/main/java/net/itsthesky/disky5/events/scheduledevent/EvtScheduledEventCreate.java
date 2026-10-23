package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.events.guild.scheduledevent.ScheduledEventCreateEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Scheduled Event Create",
        patterns = {"[discord] scheduled[( |-)]event creat(e|ion)"},
        jda = ScheduledEventCreateEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event Create",
        description = {
                "Fired when a scheduled event is created in a guild. Can be used to get the scheduled event and the guild it was created in.",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event creation:",
                "\tbroadcast \"A new scheduled event '%event-scheduledevent's name%' was created in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventCreate {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventCreateEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventCreateEvent event) {
        return event.getGuild();
    }
}
