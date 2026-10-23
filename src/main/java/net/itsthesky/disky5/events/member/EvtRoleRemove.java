package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Role Remove Event",
        patterns = {"[member] role remove[d]"},
        jda = GuildMemberRoleRemoveEvent.class
)
@Documentation(
        name = "Role Remove Event",
        description = "Fired when roles are removed from a member. This is a log action, so event-author "
                + "returns who made the action and event-roles returns a list of removed roles.",
        examples = {
            "on role remove:",
            "\tbroadcast \"%event-author% removed roles %removed roles% from %event-member%\""
        },
        since = "5.0.0-a1"
)
public final class EvtRoleRemove {

    @EventValue
    public static Guild guild(GuildMemberRoleRemoveEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildMemberRoleRemoveEvent event) {
        return event.getMember();
    }

    @EventExpression("removed roles")
    public static Role[] removedRoles(GuildMemberRoleRemoveEvent event) {
        return event.getRoles().toArray(Role[]::new);
    }
}
