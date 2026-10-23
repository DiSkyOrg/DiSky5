package net.itsthesky.disky5.events.role;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Role Delete Event",
        patterns = {"[discord] [guild] role delete"},
        jda = RoleDeleteEvent.class
)
@Documentation(
        name = "Role Delete Event",
        description = {
                "Fired when a role is deleted from a guild.",
                "This event provides access to the deleted role and the guild it belonged to.",
                "It can be used for auditing purposes or to trigger cleanup actions in your bot."
        },
        examples = {
                "on role delete:",
                "\tbroadcast \"Role %event-role% was deleted from %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtRoleDelete {

    @EventValue
    public static Guild guild(RoleDeleteEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Role role(RoleDeleteEvent event) {
        return event.getRole();
    }
}
