package net.itsthesky.disky5.events.role;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Role Permissions Update Event",
        patterns = {"[discord] [guild] role permission[s] (update|change)"},
        jda = RoleUpdatePermissionsEvent.class
)
@Documentation(
        name = "Role Permissions Update Event",
        description = {
                "Fired when the permissions of a role change.",
                "This event provides access to both the old and new permission sets.",
                "It's crucial for security monitoring, permission auditing, and tracking administrative changes."
        },
        examples = {
                "on role permissions change:",
                "\tbroadcast \"Permissions for role %event-role% have been updated in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtRolePermissionsUpdate {

    @EventValue
    public static Guild guild(RoleUpdatePermissionsEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Role role(RoleUpdatePermissionsEvent event) {
        return event.getRole();
    }

    @EventExpression("[the] [new] role permission[s]")
    public static Permission[] permissions(RoleUpdatePermissionsEvent event) {
        return event.getNewPermissions().toArray(Permission[]::new);
    }

    @EventExpression("[the] (old|past) role permission[s]")
    public static Permission[] pastPermissions(RoleUpdatePermissionsEvent event) {
        return event.getOldPermissions().toArray(Permission[]::new);
    }
}
