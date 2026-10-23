package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

import java.time.OffsetDateTime;

@SkriptEvent(
        name = "Member Boost Time Update Event",
        patterns = {"[guild] member boost time (change|update)"},
        jda = GuildMemberUpdateBoostTimeEvent.class
)
@Documentation(
        name = "Member Boost Time Update Event",
        description = "Fired when a member's boost time is updated, which can happen when they start or "
                + "stop boosting a server.",
        examples = {
            "on member boost time change:",
            "\tbroadcast \"%event-member% boost time updated from %past member boost time% to %member boost time%\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberBoostTime {

    @EventValue
    public static Guild guild(GuildMemberUpdateBoostTimeEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildMemberUpdateBoostTimeEvent event) {
        return event.getMember();
    }

    @EventValue
    public static User user(GuildMemberUpdateBoostTimeEvent event) {
        return event.getUser();
    }

    @EventExpression("[member] boost time")
    public static OffsetDateTime boostTime(GuildMemberUpdateBoostTimeEvent event) {
        return event.getNewTimeBoosted();
    }

    @EventExpression("(old|past|previous) [member] boost time")
    public static OffsetDateTime oldBoostTime(GuildMemberUpdateBoostTimeEvent event) {
        return event.getOldTimeBoosted();
    }
}
