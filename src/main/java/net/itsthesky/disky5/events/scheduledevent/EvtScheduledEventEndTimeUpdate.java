package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateEndTimeEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

@SkriptEvent(
        name = "Scheduled Event End Time Update",
        patterns = {"[discord] scheduled[( |-)]event end[( |-)]time (change|update)"},
        jda = ScheduledEventUpdateEndTimeEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event End Time Update",
        description = {
                "Fired when a scheduled event's end time is changed. The old or new value may be absent if the event has no defined end time.",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event end time change:",
                "\tbroadcast \"%event-scheduledevent's name% will now end at %new scheduled event end time%\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventEndTimeUpdate {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventUpdateEndTimeEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventUpdateEndTimeEvent event) {
        return event.getGuild();
    }

    @EventExpression("scheduled [event] end[ ]time")
    public static @Nullable OffsetDateTime endTime(ScheduledEventUpdateEndTimeEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) scheduled [event] end[ ]time")
    public static @Nullable OffsetDateTime oldEndTime(ScheduledEventUpdateEndTimeEvent event) {
        return event.getOldValue();
    }
}
