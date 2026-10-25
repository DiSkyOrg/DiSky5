package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchiveTimestampEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

import java.time.OffsetDateTime;

@SkriptEvent(
        name = "Channel Archive Timestamp/Date Update Event",
        patterns = {"[discord] channel archive (timestamp|date) (change|update)"},
        jda = ChannelUpdateArchiveTimestampEvent.class
)
@Documentation(
        name = "Channel Archive Timestamp/Date Update Event",
        description = {"Fired when a thread channel's archive timestamp is changed."},
        examples = {
            "on channel archive timestamp change:",
            "\tbroadcast \"Channel %event-channel% archive timestamp changed\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateArchiveTimestamp {

    @EventValue
    public static OffsetDateTime newArchiveTimestamp(ChannelUpdateArchiveTimestampEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static OffsetDateTime oldArchiveTimestamp(ChannelUpdateArchiveTimestampEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateArchiveTimestampEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateArchiveTimestampEvent event) {
        return event.getChannel();
    }
}
