package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Delete Event",
        patterns = {"[discord] channel delet(e|ion)"},
        jda = ChannelDeleteEvent.class
)
@Documentation(
        name = "Channel Delete Event",
        description = {"Fired when a channel is deleted."},
        examples = {
            "on channel deletion:",
            "\tbroadcast \"%event-channel%, %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelDelete {

    @EventValue
    public static Guild guild(ChannelDeleteEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelDeleteEvent event) {
        return event.getChannel();
    }
}
