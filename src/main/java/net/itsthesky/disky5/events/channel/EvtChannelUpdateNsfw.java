package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNSFWEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel NSFW Update Event",
        patterns = {"[discord] channel nsfw (change|update)"},
        jda = ChannelUpdateNSFWEvent.class
)
@Documentation(
        name = "Channel NSFW Update Event",
        description = {"Fired when a channel's NSFW status is changed."},
        examples = {
            "on channel nsfw change:",
            "\tbroadcast \"Channel %event-channel% NSFW status changed from %past event-boolean% to %event-boolean%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateNsfw {

    @EventValue
    public static Boolean newNsfw(ChannelUpdateNSFWEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Boolean oldNsfw(ChannelUpdateNSFWEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateNSFWEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateNSFWEvent event) {
        return event.getChannel();
    }
}
