package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.scheduledevent.ScheduledEventUserRemoveEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Scheduled Event User Remove",
        patterns = {"[discord] scheduled[( |-)]event user (remove|unsubscribe|leave)"},
        jda = ScheduledEventUserRemoveEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event User Remove",
        description = {
                "Fired when a user unsubscribes from a scheduled event.",
                "The user/member may not be present if not cached.",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event user remove:",
                "\tbroadcast \"%event-user% is no longer interested in %event-scheduledevent's name%\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventUserRemove {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventUserRemoveEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventUserRemoveEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static User user(ScheduledEventUserRemoveEvent event) {
        return event.getUser();
    }

    @EventValue
    public static @Nullable Member member(ScheduledEventUserRemoveEvent event) {
        return event.getMember();
    }

    // TODO: no @Property.Retrieve/restValue codegen for events yet; the cached user/member
    // event-values above cover the common case, but there's no async fallback when uncached.
}
