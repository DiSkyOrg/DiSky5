package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateUserLimitEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel User Limit Update Event",
        patterns = {"[discord] channel user limit (change|update)"},
        jda = ChannelUpdateUserLimitEvent.class
)
@Documentation(
        name = "Channel User Limit Update Event",
        description = {"Fired when a voice channel's user limit is changed."},
        examples = {
            "on channel user limit change:",
            "\tbroadcast \"Channel %event-channel% user limit changed from %past event-number% to %event-number%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateUserLimit {

    @EventValue
    public static Integer newUserLimit(ChannelUpdateUserLimitEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Integer oldUserLimit(ChannelUpdateUserLimitEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateUserLimitEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateUserLimitEvent event) {
        return event.getChannel();
    }
}
