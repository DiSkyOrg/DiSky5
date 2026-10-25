package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelFlag;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateFlagsEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

@SkriptEvent(
        name = "Channel Flags Update Event",
        patterns = {"[discord] channel flags (change|update)"},
        jda = ChannelUpdateFlagsEvent.class
)
@Documentation(
        name = "Channel Flags Update Event",
        description = {"Fired when a channel's flags are changed."},
        examples = {
            "on channel flags change:",
            "\tbroadcast \"Channel %event-channel% flags changed\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateFlags {

    @EventExpression("new [channel] flags")
    public static List<String> newFlags(ChannelUpdateFlagsEvent event) {
        return names(event.getNewValue());
    }

    @EventExpression("old [channel] flags")
    public static List<String> oldFlags(ChannelUpdateFlagsEvent event) {
        return names(event.getOldValue());
    }

    @EventValue
    public static Guild guild(ChannelUpdateFlagsEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateFlagsEvent event) {
        return event.getChannel();
    }

    private static List<String> names(EnumSet<ChannelFlag> flags) {
        return flags.stream().map(f -> f.name().toLowerCase(Locale.ROOT).replace('_', ' ')).toList();
    }
}
