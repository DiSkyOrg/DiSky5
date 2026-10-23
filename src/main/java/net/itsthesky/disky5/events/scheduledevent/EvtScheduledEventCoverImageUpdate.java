package net.itsthesky.disky5.events.scheduledevent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateCoverImageEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Scheduled Event Cover Image Update",
        patterns = {"[discord] scheduled event [cover] image (change|update[d])"},
        jda = ScheduledEventUpdateCoverImageEvent.class,
        intents = {"SCHEDULED_EVENTS"}
)
@Documentation(
        name = "Scheduled Event Cover Image Update",
        description = {
                "Fired when a scheduled event's cover image is changed. The old/new values are image URLs (or absent if no image is set).",
                "Requires the SCHEDULED_EVENTS intent and the SCHEDULED_EVENTS cache flag to be enabled."
        },
        examples = {
                "on scheduled event cover image change:",
                "\tbroadcast \"Cover image of %event-scheduledevent's name% changed to %new scheduled event cover image%\""
        },
        since = "5.0.0-a1"
)
public final class EvtScheduledEventCoverImageUpdate {

    @EventValue
    public static ScheduledEvent scheduledEvent(ScheduledEventUpdateCoverImageEvent event) {
        return event.getScheduledEvent();
    }

    @EventValue
    public static Guild guild(ScheduledEventUpdateCoverImageEvent event) {
        return event.getGuild();
    }

    @EventExpression("[the] [new] scheduled [event] [cover] image")
    public static @Nullable String coverImage(ScheduledEventUpdateCoverImageEvent event) {
        return event.getNewImageUrl();
    }

    @EventExpression("[the] (old|past|previous) scheduled [event] [cover] image")
    public static @Nullable String oldCoverImage(ScheduledEventUpdateCoverImageEvent event) {
        return event.getOldImageUrl();
    }
}
