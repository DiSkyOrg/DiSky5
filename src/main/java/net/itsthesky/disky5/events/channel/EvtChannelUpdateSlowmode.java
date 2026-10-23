package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateSlowmodeEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Slowmode Update Event",
        patterns = {"[discord] channel slowmode (change|update)"},
        jda = ChannelUpdateSlowmodeEvent.class
)
@Documentation(
        name = "Channel Slowmode Update Event",
        description = {"Fired when a channel's slowmode setting is changed."},
        examples = {
            "on channel slowmode change:",
            "\tbroadcast \"Channel %event-channel% slowmode changed from %past event-number% to %event-number% seconds\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateSlowmode {

    @EventValue
    public static Integer newSlowmode(ChannelUpdateSlowmodeEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Integer oldSlowmode(ChannelUpdateSlowmodeEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateSlowmodeEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateSlowmodeEvent event) {
        return event.getChannel();
    }
}
