package net.itsthesky.disky5.events.role;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.RoleIcon;
import net.dv8tion.jda.api.events.role.update.RoleUpdateIconEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Role Icon Update Event",
        patterns = {"[discord] [guild] role icon (update|change)"},
        jda = RoleUpdateIconEvent.class
)
@Documentation(
        name = "Role Icon Update Event",
        description = {
                "Fired when the icon of a role changes.",
                "This event provides access to both the old and new icon URLs.",
                "It can be used for tracking visual changes to roles or updating external systems."
        },
        examples = {
                "on role icon change:",
                "\tbroadcast \"Role %event-role% icon changed from %past event-string% to %event-string%\""
        },
        since = "5.0.0-a1"
)
public final class EvtRoleIconUpdate {

    @EventValue
    public static Guild guild(RoleUpdateIconEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Role role(RoleUpdateIconEvent event) {
        return event.getRole();
    }

    @EventValue
    public static @Nullable String iconUrl(RoleUpdateIconEvent event) {
        final RoleIcon icon = event.getNewValue();
        return icon == null ? null : icon.getIconUrl();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable String pastIconUrl(RoleUpdateIconEvent event) {
        final RoleIcon icon = event.getOldValue();
        return icon == null ? null : icon.getIconUrl();
    }
}
