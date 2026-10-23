package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateDefaultThreadSlowmodeEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Default Thread Slowmode Update Event",
        patterns = {"[discord] channel default thread slowmode (change|update)"},
        jda = ChannelUpdateDefaultThreadSlowmodeEvent.class
)
@Documentation(
        name = "Channel Default Thread Slowmode Update Event",
        description = {"Fired when a forum channel's default thread slowmode is changed."},
        examples = {
            "on channel default thread slowmode change:",
            "\tbroadcast \"Channel %event-channel% default thread slowmode changed from %past event-number% to %event-number%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateDefaultThreadSlowmode {

    @EventValue
    public static Integer newSlowmode(ChannelUpdateDefaultThreadSlowmodeEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Integer oldSlowmode(ChannelUpdateDefaultThreadSlowmodeEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateDefaultThreadSlowmodeEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateDefaultThreadSlowmodeEvent event) {
        return event.getChannel();
    }
}
