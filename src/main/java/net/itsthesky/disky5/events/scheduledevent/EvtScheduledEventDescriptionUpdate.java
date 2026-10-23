package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateDescriptionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Scheduled Event Description Update",
        patterns = {"[discord] scheduled[( |-)]event description (change|update)"},
        jda = ScheduledEventUpdateDescriptionEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event Description Update",
        description = {
                "Fired when a scheduled event's description is changed.",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event description change:",
                "\tbroadcast \"Description of %event-scheduledevent's name% changed\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventDescriptionUpdate {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventUpdateDescriptionEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventUpdateDescriptionEvent event) {
        return event.getGuild();
    }

    @EventExpression("scheduled [event] description")
    public static String description(ScheduledEventUpdateDescriptionEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) scheduled [event] description")
    public static String oldDescription(ScheduledEventUpdateDescriptionEvent event) {
        return event.getOldValue();
    }
}
