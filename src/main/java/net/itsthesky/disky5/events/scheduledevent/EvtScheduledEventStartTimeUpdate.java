package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateStartTimeEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

import java.time.OffsetDateTime;

@SkriptEvent(
        name = "Scheduled Event Start Time Update",
        patterns = {"[discord] scheduled[( |-)]event start[( |-)]time (change|update)"},
        jda = ScheduledEventUpdateStartTimeEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event Start Time Update",
        description = {
                "Fired when a scheduled event's start time is changed.",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event start time change:",
                "\tbroadcast \"%event-scheduledevent's name% will now start at %new scheduled event start time%\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventStartTimeUpdate {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventUpdateStartTimeEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventUpdateStartTimeEvent event) {
        return event.getGuild();
    }

    @EventExpression("scheduled [event] start[ ]time")
    public static OffsetDateTime startTime(ScheduledEventUpdateStartTimeEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) scheduled [event] start[ ]time")
    public static OffsetDateTime oldStartTime(ScheduledEventUpdateStartTimeEvent event) {
        return event.getOldValue();
    }
}
