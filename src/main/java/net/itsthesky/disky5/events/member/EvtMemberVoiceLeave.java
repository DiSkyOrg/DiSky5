package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventFilter;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Member Voice Leave Event",
        patterns = {"[member] voice [channel] leave"},
        jda = GuildVoiceUpdateEvent.class
)
@Documentation(
        name = "Member Voice Leave Event",
        description = "Fired when a member leaves a voice or stage channel. This includes both "
                + "disconnecting completely and moving to another channel.",
        examples = {
            "on voice channel leave:",
            "\tbroadcast \"%event-member% left voice channel %left voice channel%\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberVoiceLeave {

    /** Leaves and moves only: GuildVoiceUpdateEvent also covers pure joins, where left is null. */
    @EventFilter
    public static boolean accept(GuildVoiceUpdateEvent event) {
        return event.getChannelLeft() != null;
    }

    @EventValue
    public static Guild guild(GuildVoiceUpdateEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildVoiceUpdateEvent event) {
        return event.getMember();
    }

    @EventValue
    public static @Nullable Channel leftChannel(GuildVoiceUpdateEvent event) {
        return event.getChannelLeft();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable Channel joinedChannel(GuildVoiceUpdateEvent event) {
        return event.getChannelJoined();
    }
}
