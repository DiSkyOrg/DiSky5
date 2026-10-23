package net.itsthesky.disky5.actions;

import ch.njol.skript.util.Date;
import ch.njol.skript.util.Timespan;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public final class ModerationActions {

    private ModerationActions() {}

    @Effect(value = "[discord] ban [the] [member] %member% "
        + "[and (delete|remove) %-timespan% [worth ]of message[s]]", reason = true)
    @Documentation(
        name = "Ban Member",
        description = {"Bans a member from their guild, optionally deleting their recent messages.",
            "The bot needs the Ban Members permission and a role above the target's."},
        examples = {"ban event-member because of \"spam\" and delete 7 days worth of messages"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> ban(Member member, @Nullable Timespan deleteMessages) {
        return member.ban(purgeMillis(deleteMessages), TimeUnit.MILLISECONDS);
    }

    @Effect(value = "[discord] ban [the] [user] [with] [id] %string% (from|of) [the] [guild] %guild% "
        + "[and (delete|remove) %-timespan% [worth ]of message[s]]", reason = true)
    @Documentation(
        name = "Ban User ID",
        description = {"Bans a user ID from a guild without retrieving the member first.",
            "Works on users who already left the guild."},
        examples = {"ban user id \"000000000000000000\" from event-guild due to \"ban evasion\""},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> banById(String userId, Guild guild,
                                                    @Nullable Timespan deleteMessages) {
        return guild.ban(UserSnowflake.fromId(userId.replaceAll("[<@!>]", "")), purgeMillis(deleteMessages), TimeUnit.MILLISECONDS);
    }

    @Effect(value = "[discord] un[-| ]ban [the] [user] %user% (from|in) [the] [guild] %guild%", reason = true)
    @Documentation(
        name = "Unban User",
        description = "Lifts a user's ban on a guild.",
        examples = {"unban event-user from event-guild"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> unban(User user, Guild guild) {
        return guild.unban(user);
    }

    @Effect(value = "[discord] kick [the] [member] %member%", reason = true)
    @Documentation(
        name = "Kick Member",
        description = "Kicks a member from their guild.",
        examples = {"kick event-member because of \"being lame\""},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> kick(Member member) {
        return member.kick();
    }

    @Effect(value = "time[( |-)]out [the] [member] %member% for %timespan%", reason = true)
    @Documentation(
        name = "Time Out Member For",
        description = {"Times a member out for a duration (Discord caps it at 28 days).",
            "A timed-out member cannot talk, react, or join voice channels."},
        examples = {"time out event-member for 10 minutes due to \"flooding\""},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> timeOutFor(Member member, Timespan duration) {
        return member.timeoutFor(duration.getDuration());
    }

    @Effect(value = "time[( |-)]out [the] [member] %member% until %date%", reason = true)
    @Documentation(
        name = "Time Out Member Until",
        description = "Times a member out until a given date (Discord caps it at 28 days out).",
        examples = {"time out event-member until now + 1 hour"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> timeOutUntil(Member member, Date until) {
        return member.timeoutUntil(until.toInstant());
    }

    @Effect(value = "(stop|remove) [the] time[( |-)]out (of|from) [the] [member] %member%", reason = true)
    @Documentation(
        name = "Remove Time Out",
        description = "Lifts a member's time out.",
        examples = {"remove the time out of event-member"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> removeTimeOut(Member member) {
        return member.removeTimeout();
    }

    @Effect(value = "[voice] mute [the] [member] %member%", reason = true)
    @Documentation(
        name = "Mute Member",
        description = "Server-mutes a member. The member must be connected to a voice channel.",
        examples = {"mute event-member"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> mute(Member member) {
        return member.mute(true);
    }

    @Effect(value = "[voice] un[ |-]mute [the] [member] %member%", reason = true)
    @Documentation(
        name = "Unmute Member",
        description = "Lifts a member's server mute.",
        examples = {"unmute event-member"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> unmute(Member member) {
        return member.mute(false);
    }

    @Effect(value = "[voice] deafen [the] [member] %member%", reason = true)
    @Documentation(
        name = "Deafen Member",
        description = "Server-deafens a member. The member must be connected to a voice channel.",
        examples = {"deafen event-member"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> deafen(Member member) {
        return member.deafen(true);
    }

    @Effect(value = "[voice] un[ |-]deafen [the] [member] %member%", reason = true)
    @Documentation(
        name = "Undeafen Member",
        description = "Lifts a member's server deafen.",
        examples = {"undeafen event-member"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> undeafen(Member member) {
        return member.deafen(false);
    }

    @Effect("[voice] move [the] [member] %member% to [the] [voice] [channel] %audiochannel%")
    @Documentation(
        name = "Move Member",
        description = "Moves a member to another voice channel. The member must already be connected.",
        examples = {"move event-member to voice channel with id \"000000000000000000\""},
        since = "5.0.0-a1")
    public static RestAction<Void> moveToVoiceChannel(Member member, AudioChannel channel) {
        return member.getGuild().moveVoiceMember(member, channel);
    }

    @Effect("[voice] disconnect [the] [member] %member%")
    @Documentation(
        name = "Disconnect Member",
        description = "Disconnects a member from the voice channel they are in.",
        examples = {"disconnect event-member"},
        since = "5.0.0-a1")
    public static RestAction<Void> disconnectFromVoice(Member member) {
        return member.getGuild().kickVoiceMember(member);
    }

    private static int purgeMillis(@Nullable Timespan deleteMessages) {
        return deleteMessages == null ? 0 : (int) deleteMessages.getAs(Timespan.TimePeriod.MILLISECOND);
    }
}
