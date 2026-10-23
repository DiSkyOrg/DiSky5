package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateNameEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Scheduled Event Name Update",
        patterns = {"[discord] scheduled[( |-)]event name (change|update)"},
        jda = ScheduledEventUpdateNameEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event Name Update",
        description = {
                "Fired when a scheduled event's name is changed.",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event name change:",
                "\tbroadcast \"Scheduled event renamed from '%past scheduled event name%' to '%new scheduled event name%'\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventNameUpdate {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventUpdateNameEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventUpdateNameEvent event) {
        return event.getGuild();
    }

    @EventExpression("scheduled [event] name")
    public static String name(ScheduledEventUpdateNameEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) scheduled [event] name")
    public static String oldName(ScheduledEventUpdateNameEvent event) {
        return event.getOldValue();
    }
}
