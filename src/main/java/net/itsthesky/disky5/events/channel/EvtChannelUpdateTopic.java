package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateTopicEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Channel Topic Update Event",
        patterns = {"[discord] channel topic (change|update)"},
        jda = ChannelUpdateTopicEvent.class
)
@Documentation(
        name = "Channel Topic Update Event",
        description = {"Fired when a channel's topic is changed."},
        examples = {
            "on channel topic change:",
            "\tbroadcast \"Channel %event-channel% had its topic changed from '%past event-string%' to '%event-string%'\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateTopic {

    @EventValue
    public static @Nullable String newTopic(ChannelUpdateTopicEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable String oldTopic(ChannelUpdateTopicEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateTopicEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateTopicEvent event) {
        return event.getChannel();
    }
}
