package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventFilter;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Member Voice Join Event",
        patterns = {"[member] voice [channel] join"},
        jda = GuildVoiceUpdateEvent.class
)
@Documentation(
        name = "Member Voice Join Event",
        description = "Fired when a member joins a voice or stage channel. This event also fires when a "
                + "member moves from one voice channel to another.",
        examples = {
            "on voice channel join:",
            "\tbroadcast \"%event-member% joined voice channel %joined voice channel%\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberVoiceJoin {

    /** Joins and moves only: GuildVoiceUpdateEvent also covers pure leaves, where joined is null. */
    @EventFilter
    public static boolean accept(GuildVoiceUpdateEvent event) {
        return event.getChannelJoined() != null;
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
    public static @Nullable Channel joinedChannel(GuildVoiceUpdateEvent event) {
        return event.getChannelJoined();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable Channel leftChannel(GuildVoiceUpdateEvent event) {
        return event.getChannelLeft();
    }
}
