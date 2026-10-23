package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.attribute.IPostContainer;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateDefaultSortOrderEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Channel Default Sort Order Update Event",
        patterns = {"[discord] channel default sort order (change|update)"},
        jda = ChannelUpdateDefaultSortOrderEvent.class
)
@Documentation(
        name = "Channel Default Sort Order Update Event",
        description = {"Fired when a forum channel's default sort order is changed."},
        examples = {
            "on channel default sort order change:",
            "\tbroadcast \"Channel %event-channel% default sort order changed\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateDefaultSortOrder {

    @EventValue
    public static IPostContainer.SortOrder newSortOrder(ChannelUpdateDefaultSortOrderEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static IPostContainer.SortOrder oldSortOrder(ChannelUpdateDefaultSortOrderEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateDefaultSortOrderEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateDefaultSortOrderEvent event) {
        return event.getChannel();
    }
}
