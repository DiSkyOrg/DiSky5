package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdatePendingEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Member Accept Screen Event",
        patterns = {"[guild] member screen accept"},
        jda = GuildMemberUpdatePendingEvent.class
)
@Documentation(
        name = "Member Accept Screen Event",
        description = "Fired when a member has agreed to membership screen requirements. This can be useful "
                + "for adding roles since the member is not fully available until they've accepted the "
                + "screen requirements.",
        examples = {
            "on member screen accept:",
            "\tbroadcast \"%event-member% has completed the membership screening in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberScreenAccept {

    @EventValue
    public static Guild guild(GuildMemberUpdatePendingEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildMemberUpdatePendingEvent event) {
        return event.getMember();
    }

    @EventExpression("[member] pending state")
    public static Boolean pendingState(GuildMemberUpdatePendingEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) [member] pending state")
    public static Boolean oldPendingState(GuildMemberUpdatePendingEvent event) {
        return event.getOldValue();
    }
}
