package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateLockedEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Locked Update Event",
        patterns = {"[discord] channel locked (change|update)"},
        jda = ChannelUpdateLockedEvent.class
)
@Documentation(
        name = "Channel Locked Update Event",
        description = {"Fired when a thread channel's locked status is changed."},
        examples = {
            "on channel locked change:",
            "\tbroadcast \"Channel %event-channel% locked status changed from %past event-boolean% to %event-boolean%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateLocked {

    @EventValue
    public static Boolean newLocked(ChannelUpdateLockedEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Boolean oldLocked(ChannelUpdateLockedEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateLockedEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateLockedEvent event) {
        return event.getChannel();
    }
}
