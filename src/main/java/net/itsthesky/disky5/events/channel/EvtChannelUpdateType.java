package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateTypeEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Type Update Event",
        patterns = {"[discord] channel type (change|update)"},
        jda = ChannelUpdateTypeEvent.class
)
@Documentation(
        name = "Channel Type Update Event",
        description = {"Fired when a channel's type is changed."},
        examples = {
            "on channel type change:",
            "\tbroadcast \"Channel %event-channel% type changed\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateType {

    @EventValue
    public static ChannelType newType(ChannelUpdateTypeEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static ChannelType oldType(ChannelUpdateTypeEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateTypeEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateTypeEvent event) {
        return event.getChannel();
    }
}
