package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateStatusEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Scheduled Event Status Update",
        patterns = {"[discord] scheduled[( |-)]event status (change|update)"},
        jda = ScheduledEventUpdateStatusEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event Status Update",
        description = {
                "Fired when a scheduled event's status changes between: scheduled, active, completed, cancelled.",
                "This is how you detect when an event actually starts (status becomes 'active') or ends (status becomes 'completed').",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event status change:",
                "\tif new scheduled event status is \"active\":",
                "\t\tbroadcast \"%event-scheduledevent's name% has started!\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventStatusUpdate {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventUpdateStatusEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventUpdateStatusEvent event) {
        return event.getGuild();
    }

    @EventExpression("scheduled [event] status")
    public static @Nullable String status(ScheduledEventUpdateStatusEvent event) {
        return event.getNewValue() == null ? null : event.getNewValue().name().toLowerCase().replace("_", " ");
    }

    @EventExpression("(old|past|previous) scheduled [event] status")
    public static @Nullable String oldStatus(ScheduledEventUpdateStatusEvent event) {
        return event.getOldValue() == null ? null : event.getOldValue().name().toLowerCase().replace("_", " ");
    }
}
