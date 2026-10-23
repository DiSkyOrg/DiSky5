package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateBitrateEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Bitrate Update Event",
        patterns = {"[discord] channel bitrate (change|update)"},
        jda = ChannelUpdateBitrateEvent.class
)
@Documentation(
        name = "Channel Bitrate Update Event",
        description = {"Fired when a voice channel's bitrate is changed."},
        examples = {
            "on channel bitrate change:",
            "\tbroadcast \"Channel %event-channel% bitrate changed from %past event-number% to %event-number%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateBitrate {

    @EventValue
    public static Integer newBitrate(ChannelUpdateBitrateEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Integer oldBitrate(ChannelUpdateBitrateEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateBitrateEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateBitrateEvent event) {
        return event.getChannel();
    }
}
