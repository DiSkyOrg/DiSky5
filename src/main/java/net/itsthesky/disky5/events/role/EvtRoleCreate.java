package net.itsthesky.disky5.events.role;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Role Create Event",
        patterns = {"[discord] [guild] role create[d]"},
        jda = RoleCreateEvent.class
)
@Documentation(
        name = "Role Create Event",
        description = {
                "Fired when a new role is created in a guild.",
                "This event provides access to the newly created role and the guild it belongs to.",
                "It's useful for tracking administrative changes or implementing role management systems."
        },
        examples = {
                "on role create:",
                "\tbroadcast \"New role created: %event-role% in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtRoleCreate {

    @EventValue
    public static Guild guild(RoleCreateEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Role role(RoleCreateEvent event) {
        return event.getRole();
    }
}
