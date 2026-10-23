package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchivedEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Archived Update Event",
        patterns = {"[discord] channel archived (change|update)"},
        jda = ChannelUpdateArchivedEvent.class
)
@Documentation(
        name = "Channel Archived Update Event",
        description = {"Fired when a thread channel's archived status is changed."},
        examples = {
            "on channel archived change:",
            "\tbroadcast \"Channel %event-channel% archived status changed from %past event-boolean% to %event-boolean%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateArchived {

    @EventValue
    public static Boolean newArchived(ChannelUpdateArchivedEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Boolean oldArchived(ChannelUpdateArchivedEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateArchivedEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateArchivedEvent event) {
        return event.getChannel();
    }
}
