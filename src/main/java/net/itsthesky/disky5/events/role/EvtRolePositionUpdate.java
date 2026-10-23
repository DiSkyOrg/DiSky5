package net.itsthesky.disky5.events.role;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePositionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Role Position Update Event",
        patterns = {"[discord] [guild] role position (update|change)"},
        jda = RoleUpdatePositionEvent.class
)
@Documentation(
        name = "Role Position Update Event",
        description = {
                "Fired when the position of a role changes in the role hierarchy.",
                "This event provides access to both the old and new positions.",
                "It's useful for tracking changes to the role hierarchy that may affect permissions."
        },
        examples = {
                "on role position change:",
                "\tbroadcast \"Role %event-role% position changed from %past event-number% to %event-number%\""
        },
        since = "5.0.0-a1"
)
public final class EvtRolePositionUpdate {

    @EventValue
    public static Guild guild(RoleUpdatePositionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Role role(RoleUpdatePositionEvent event) {
        return event.getRole();
    }

    @EventValue
    public static Integer position(RoleUpdatePositionEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Integer pastPosition(RoleUpdatePositionEvent event) {
        return event.getOldValue();
    }
}
