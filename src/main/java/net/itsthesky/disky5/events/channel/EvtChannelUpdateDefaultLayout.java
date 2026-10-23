package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateDefaultLayoutEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Default Layout Update Event",
        patterns = {"[discord] channel default layout (change|update)"},
        jda = ChannelUpdateDefaultLayoutEvent.class
)
@Documentation(
        name = "Channel Default Layout Update Event",
        description = {"Fired when a forum channel's default layout is changed."},
        examples = {
            "on channel default layout change:",
            "\tbroadcast \"Channel %event-channel% default layout changed\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateDefaultLayout {

    @EventValue
    public static ForumChannel.Layout newLayout(ChannelUpdateDefaultLayoutEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static ForumChannel.Layout oldLayout(ChannelUpdateDefaultLayoutEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateDefaultLayoutEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateDefaultLayoutEvent event) {
        return event.getChannel();
    }
}
