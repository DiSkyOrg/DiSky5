package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateLocationEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Scheduled Event Location Update",
        patterns = {"[discord] scheduled[( |-)]event location (change|update)"},
        jda = ScheduledEventUpdateLocationEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event Location Update",
        description = {
                "Fired when a scheduled event's location is changed.",
                "For external events this is a free-form string; for channel-bound events it is the channel ID as a string.",
                "Use 'event-channel' to access the new channel if the event is bound to a channel.",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event location change:",
                "\tbroadcast \"%event-scheduledevent's name% moved from '%past scheduled event location%' to '%new scheduled event location%'\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventLocationUpdate {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventUpdateLocationEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventUpdateLocationEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable Channel channel(ScheduledEventUpdateLocationEvent event) {
        return event.getScheduledEvent().getChannel();
    }

    @EventExpression("scheduled [event] location")
    public static String location(ScheduledEventUpdateLocationEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) scheduled [event] location")
    public static String oldLocation(ScheduledEventUpdateLocationEvent event) {
        return event.getOldValue();
    }
}
