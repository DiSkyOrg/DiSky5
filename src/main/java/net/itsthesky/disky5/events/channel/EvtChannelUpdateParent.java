package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateParentEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Channel Parent Update Event",
        patterns = {"[discord] channel parent (change|update)"},
        jda = ChannelUpdateParentEvent.class
)
@Documentation(
        name = "Channel Parent Update Event",
        description = {"Fired when a channel's parent category is changed."},
        examples = {
            "on channel parent change:",
            "\tbroadcast \"Channel %event-channel% was moved from %past event-category% to %event-category%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateParent {

    @EventValue
    public static @Nullable Category newParent(ChannelUpdateParentEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable Category oldParent(ChannelUpdateParentEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateParentEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateParentEvent event) {
        return event.getChannel();
    }
}
