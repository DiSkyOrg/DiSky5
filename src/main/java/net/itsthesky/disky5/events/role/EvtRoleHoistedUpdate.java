package net.itsthesky.disky5.events.role;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.update.RoleUpdateHoistedEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Role Hoisted Update Event",
        patterns = {"[discord] [guild] role hoist[ed] (update|change)"},
        jda = RoleUpdateHoistedEvent.class
)
@Documentation(
        name = "Role Hoisted Update Event",
        description = {
                "Fired when the hoisted status of a role changes.",
                "Hoisted roles are displayed separately in the member list.",
                "This event provides access to both the old and new hoisted states.",
                "It's useful for tracking changes to role visibility in the member sidebar."
        },
        examples = {
                "on role hoisted change:",
                "\tif event-boolean is true:",
                "\t\tbroadcast \"Role %event-role% is now shown separately in the member list\"",
                "\telse:",
                "\t\tbroadcast \"Role %event-role% is no longer shown separately in the member list\""
        },
        since = "5.0.0-a1"
)
public final class EvtRoleHoistedUpdate {

    @EventValue
    public static Guild guild(RoleUpdateHoistedEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Role role(RoleUpdateHoistedEvent event) {
        return event.getRole();
    }

    @EventValue
    public static Boolean hoisted(RoleUpdateHoistedEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Boolean pastHoisted(RoleUpdateHoistedEvent event) {
        return event.getOldValue();
    }
}
