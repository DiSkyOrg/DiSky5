package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateAutoArchiveDurationEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Channel Auto Archive Duration Update Event",
        patterns = {"[discord] channel auto[( |-)]archive duration (change|update)"},
        jda = ChannelUpdateAutoArchiveDurationEvent.class
)
@Documentation(
        name = "Channel Auto Archive Duration Update Event",
        description = {"Fired when a thread channel's auto archive duration is changed."},
        examples = {
            "on channel auto-archive duration change:",
            "\tbroadcast \"Channel %event-channel% auto archive duration changed\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateAutoArchiveDuration {

    @EventValue
    public static @Nullable Number newDuration(ChannelUpdateAutoArchiveDurationEvent event) {
        return event.getNewValue() == null ? null : event.getNewValue().getMinutes();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable Number oldDuration(ChannelUpdateAutoArchiveDurationEvent event) {
        return event.getOldValue() == null ? null : event.getOldValue().getMinutes();
    }

    @EventValue
    public static Guild guild(ChannelUpdateAutoArchiveDurationEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateAutoArchiveDurationEvent event) {
        return event.getChannel();
    }
}
