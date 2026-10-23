package net.itsthesky.disky5.events.role;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Role Name Update Event",
        patterns = {"[discord] [guild] role name (update|change)"},
        jda = RoleUpdateNameEvent.class
)
@Documentation(
        name = "Role Name Update Event",
        description = {
                "Fired when the name of a role changes.",
                "This event provides access to both the old and new names of the role.",
                "It's useful for tracking role identity changes or updating external systems that reference roles by name."
        },
        examples = {
                "on role name change:",
                "\tbroadcast \"Role name changed from '%past event-string%' to '%event-string%' in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtRoleNameUpdate {

    @EventValue
    public static Guild guild(RoleUpdateNameEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Role role(RoleUpdateNameEvent event) {
        return event.getRole();
    }

    @EventValue
    public static String name(RoleUpdateNameEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static String pastName(RoleUpdateNameEvent event) {
        return event.getOldValue();
    }
}
