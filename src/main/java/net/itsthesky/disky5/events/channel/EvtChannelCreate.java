package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Create Event",
        patterns = {"[discord] channel creat(e|ion)"},
        jda = ChannelCreateEvent.class
)
@Documentation(
        name = "Channel Create Event",
        description = {"Fired when a channel is created."},
        examples = {
            "on channel creation:",
            "\tbroadcast \"%event-channel%, %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelCreate {

    @EventValue
    public static Guild guild(ChannelCreateEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelCreateEvent event) {
        return event.getChannel();
    }
}
