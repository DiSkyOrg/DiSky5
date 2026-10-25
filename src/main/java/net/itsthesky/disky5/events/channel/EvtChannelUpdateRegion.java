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
    public static @Nullable String newRegion(ChannelUpdateRegionEvent event) {
        return key(event.getNewValue());
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable String oldRegion(ChannelUpdateRegionEvent event) {
        return key(event.getOldValue());
    }

    @EventValue
    public static Guild guild(ChannelUpdateRegionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateRegionEvent event) {
        return event.getChannel();
    }

    // Same as the channel's own `region` property: automatic is <none>. JDA maps a null rtc_region to UNKNOWN.
    private static @Nullable String key(@Nullable Region region) {
        return region == null || region == Region.AUTOMATIC || region == Region.UNKNOWN ? null : region.getKey();
    }
}
