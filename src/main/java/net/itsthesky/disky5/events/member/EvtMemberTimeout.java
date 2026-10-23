package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateTimeOutEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

import java.time.OffsetDateTime;

@SkriptEvent(
        name = "Member Timeout Event",
        patterns = {"member time[ ]out[ed]"},
        jda = GuildMemberUpdateTimeOutEvent.class
)
@Documentation(
        name = "Member Timeout Event",
        description = "Fired when a member is timed out (temporarily restricted from interacting with "
                + "the server).",
        examples = {
            "on member timeout:",
            "\tbroadcast \"%event-member% has been timed out until %member timeout end%.\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberTimeout {

    // TODO: no audit-log pairing to confirm the timeout's origin; @SkriptEvent has no such mechanism yet.
    @EventValue
    public static Guild guild(GuildMemberUpdateTimeOutEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildMemberUpdateTimeOutEvent event) {
        return event.getMember();
    }

    @EventValue
    public static User user(GuildMemberUpdateTimeOutEvent event) {
        return event.getUser();
    }

    @EventExpression("[member] timeout end")
    public static OffsetDateTime timeoutEnd(GuildMemberUpdateTimeOutEvent event) {
        return event.getNewTimeOutEnd();
    }

    @EventExpression("(old|past|previous) [member] timeout end")
    public static OffsetDateTime oldTimeoutEnd(GuildMemberUpdateTimeOutEvent event) {
        return event.getOldTimeOutEnd();
    }
}
