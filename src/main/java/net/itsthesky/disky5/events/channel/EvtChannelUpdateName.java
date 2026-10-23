package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Name Update Event",
        patterns = {"[discord] channel name (change|update)"},
        jda = ChannelUpdateNameEvent.class
)
@Documentation(
        name = "Channel Name Update Event",
        description = {"Fired when a channel's name is changed."},
        examples = {
            "on channel name change:",
            "\tbroadcast \"Channel %event-channel% was renamed from %past event-string% to %event-string%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateName {

    @EventValue
    public static String newName(ChannelUpdateNameEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static String oldName(ChannelUpdateNameEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateNameEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateNameEvent event) {
        return event.getChannel();
    }
}
