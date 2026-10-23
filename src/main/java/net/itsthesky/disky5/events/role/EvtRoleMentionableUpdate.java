package net.itsthesky.disky5.events.role;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.update.RoleUpdateMentionableEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Role Mentionable Update Event",
        patterns = {"[discord] [guild] role mentionable (update|change)"},
        jda = RoleUpdateMentionableEvent.class
)
@Documentation(
        name = "Role Mentionable Update Event",
        description = {
                "Fired when the mentionable status of a role changes.",
                "This event tracks whether a role can be mentioned by regular users.",
                "It provides access to both the old and new mentionable states.",
                "It's useful for tracking changes that affect role notifications and visibility."
        },
        examples = {
                "on role mentionable change:",
                "\tif event-boolean is true:",
                "\t\tbroadcast \"Role %event-role% can now be mentioned by everyone\"",
                "\telse:",
                "\t\tbroadcast \"Role %event-role% can no longer be mentioned by everyone\""
        },
        since = "5.0.0-a1"
)
public final class EvtRoleMentionableUpdate {

    @EventValue
    public static Guild guild(RoleUpdateMentionableEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Role role(RoleUpdateMentionableEvent event) {
        return event.getRole();
    }

    @EventValue
    public static Boolean mentionable(RoleUpdateMentionableEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Boolean pastMentionable(RoleUpdateMentionableEvent event) {
        return event.getOldValue();
    }
}
