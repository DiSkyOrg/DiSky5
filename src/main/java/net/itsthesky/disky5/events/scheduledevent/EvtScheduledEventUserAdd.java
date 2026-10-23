package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.scheduledevent.ScheduledEventUserAddEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Scheduled Event User Add",
        patterns = {"[discord] scheduled[( |-)]event user (add|subscribe|join)"},
        jda = ScheduledEventUserAddEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event User Add",
        description = {
                "Fired when a user subscribes (RSVPs as 'interested') to a scheduled event.",
                "The user/member may not be present if not cached.",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event user add:",
                "\tbroadcast \"%event-user% is now interested in %event-scheduledevent's name%\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventUserAdd {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventUserAddEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventUserAddEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static User user(ScheduledEventUserAddEvent event) {
        return event.getUser();
    }

    @EventValue
    public static @Nullable Member member(ScheduledEventUserAddEvent event) {
        return event.getMember();
    }

    // TODO: no @Property.Retrieve/restValue codegen for events yet; the cached user/member
    // event-values above cover the common case, but there's no async fallback when uncached.
}
