package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateInvitableEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Invitable Update Event",
        patterns = {"[discord] channel invitable (change|update)"},
        jda = ChannelUpdateInvitableEvent.class
)
@Documentation(
        name = "Channel Invitable Update Event",
        description = {"Fired when a thread channel's invitable status is changed."},
        examples = {
            "on channel invitable change:",
            "\tbroadcast \"Channel %event-channel% invitable status changed from %past event-boolean% to %event-boolean%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateInvitable {

    @EventValue
    public static Boolean newInvitable(ChannelUpdateInvitableEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Boolean oldInvitable(ChannelUpdateInvitableEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateInvitableEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateInvitableEvent event) {
        return event.getChannel();
    }
}
