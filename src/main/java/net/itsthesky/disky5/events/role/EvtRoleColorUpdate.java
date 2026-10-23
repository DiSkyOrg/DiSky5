package net.itsthesky.disky5.events.role;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

@SkriptEvent(
        name = "Role Color Update Event",
        patterns = {"[discord] [guild] role color (update|change)"},
        jda = RoleUpdateColorEvent.class
)
@Documentation(
        name = "Role Color Update Event",
        description = {
                "Fired when the color of a role changes.",
                "This event provides access to both the old and new colors of the role.",
                "It can be used for tracking aesthetic changes to roles or for synchronization systems."
        },
        examples = {
                "on role color change:",
                "\tbroadcast \"Role %event-role% color changed from %past event-color% to %event-color%\""
        },
        since = "5.0.0-a1"
)
public final class EvtRoleColorUpdate {

    @EventValue
    public static Guild guild(RoleUpdateColorEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Role role(RoleUpdateColorEvent event) {
        return event.getRole();
    }

    @EventValue
    public static @Nullable Color color(RoleUpdateColorEvent event) {
        return event.getNewColor();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable Color pastColor(RoleUpdateColorEvent event) {
        return event.getOldColor();
    }
}
