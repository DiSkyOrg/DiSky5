package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateVoiceStatusEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Voice Status Update Event",
        patterns = {"[discord] channel voice status (change|update)"},
        jda = ChannelUpdateVoiceStatusEvent.class
)
@Documentation(
        name = "Channel Voice Status Update Event",
        description = {"Fired when a voice channel's status (video or voice) is changed."},
        examples = {
            "on channel voice status change:",
            "\tbroadcast \"Channel %event-channel% voice status changed from %past event-string% to %event-string%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateVoiceStatus {

    @EventValue
    public static String newVoiceStatus(ChannelUpdateVoiceStatusEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static String oldVoiceStatus(ChannelUpdateVoiceStatusEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateVoiceStatusEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateVoiceStatusEvent event) {
        return event.getChannel();
    }
}
