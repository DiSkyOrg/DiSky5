package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateRegionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

// TODO: returns the native JDA Region; a String/Region Skript type + conversion is a pending gap.
@SkriptEvent(
        name = "Channel Region Update Event",
        patterns = {"[discord] channel region (change|update)"},
        jda = ChannelUpdateRegionEvent.class
)
@Documentation(
        name = "Channel Region Update Event",
        description = {"Fired when a voice channel's region is changed."},
        examples = {
            "on channel region change:",
            "\tbroadcast \"Channel %event-channel% region changed\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateRegion {

    @EventValue
    public static @Nullable Region newRegion(ChannelUpdateRegionEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable Region oldRegion(ChannelUpdateRegionEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateRegionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateRegionEvent event) {
        return event.getChannel();
    }
}
