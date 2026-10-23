package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Role Add Event",
        patterns = {"[member] role add[ed]"},
        jda = GuildMemberRoleAddEvent.class
)
@Documentation(
        name = "Role Add Event",
        description = "Fired when a member receives new roles. This is a log action, so event-author "
                + "returns who made the action and event-roles returns a list of added roles.",
        examples = {
            "on role add:",
            "\tbroadcast \"%event-author% added roles %added roles% to %event-member%\""
        },
        since = "5.0.0-a1"
)
public final class EvtRoleAdd {

    @EventValue
    public static Guild guild(GuildMemberRoleAddEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildMemberRoleAddEvent event) {
        return event.getMember();
    }

    @EventExpression("added roles")
    public static Role[] addedRoles(GuildMemberRoleAddEvent event) {
        return event.getRoles().toArray(Role[]::new);
    }
}
