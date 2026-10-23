package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdatePositionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Position Update Event",
        patterns = {"[discord] channel position (change|update)"},
        jda = ChannelUpdatePositionEvent.class
)
@Documentation(
        name = "Channel Position Update Event",
        description = {"Fired when a channel's position is changed."},
        examples = {
            "on channel position change:",
            "\tbroadcast \"Channel %event-channel% position changed from %past event-number% to %event-number%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdatePosition {

    @EventValue
    public static Integer newPosition(ChannelUpdatePositionEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Integer oldPosition(ChannelUpdatePositionEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdatePositionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdatePositionEvent event) {
        return event.getChannel();
    }
}
