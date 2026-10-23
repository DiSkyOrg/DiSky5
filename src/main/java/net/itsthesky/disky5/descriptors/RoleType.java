package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Color;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.RoleColors;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.annotations.ConditionType;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SkriptType(name = "role", jda = Role.class)
public final class RoleType {

    @Property(value = "name", shared = true)
    public static String discordName(Role role) {
        return role.getName();
    }

    @Property(value = "color", shared = true)
    @Documentation(
            name = "Role Color",
            description = "The color of the role, or null if the role has no color set.",
            examples = {
                "set {_c} to color of event-role",
                "set color of event-role to orange"
            },
            since = "5.0.0-a1"
    )
    public static @Nullable Color color(Role role) {
        return Conversions.color(role.getColor());
    }

    @Property.Setter(value = "color", shared = true)
    public static AuditableRestAction<Void> setColor(Role role, @Nullable Color value) {
        return role.getManager().setColor(Conversions.awt(value));
    }

    @Property.Setter(value = "name", shared = true)
    public static AuditableRestAction<Void> setName(Role role, String value) {
        return role.getManager().setName(value);
    }

    @Property("role position")
    @Documentation(
            name = "Role Position",
            description = "The position (ordering index) of the role within the guild's role list.",
            examples = "send \"%role position of event-role%\"",
            since = "5.0.0-a1"
    )
    public static int position(Role role) {
        return role.getPosition();
    }

    @Property("role colors")
    @Documentation(
            name = "Role Colors",
            description = "The full color set (primary / secondary / tertiary) of the role (JDA 6.x role colors).",
            examples = "reply with primary color of (role colors of event-role)",
            since = "5.0.0-a1"
    )
    public static RoleColors roleColors(Role role) {
        return role.getColors();
    }

    @Property("discord permissions")
    @Documentation(
            name = "Role Permissions",
            description = {
                "The guild-wide permissions of the role, the ones set in the role settings of the guild "
                    + "(channel overrides are not included; see the allowed and denied discord permissions in a channel).",
                "Set, add and remove change the role on Discord. Setting it to nothing (or deleting it) leaves the role "
                    + "with no permission at all; reset is not supported, since a role has no default to go back to.",
                "The bot needs the 'manage roles' permission, a role below its own highest role, and every permission it "
                    + "gives: Discord refuses to grant a permission the bot does not have itself."
            },
            examples = {
                "set {_perms::*} to discord permissions of event-role",
                "await set discord permissions of {_role} to send messages and embed links",
                "await add manage messages and kick members to discord permissions of {_role}",
                "remove administrator from discord permissions of {_role}"
            },
            since = "5.0.0-b1"
    )
    public static List<Permission> discordPermissions(Role role) {
        return List.copyOf(role.getPermissions());
    }

    @Property.Setter("discord permissions")
    public static AuditableRestAction<Void> setDiscordPermissions(Role role, Permission[] permissions) {
        return role.getManager().setPermissions(Arrays.asList(permissions));
    }

    @Property.Adder("discord permissions")
    public static AuditableRestAction<Void> addDiscordPermissions(Role role, Permission[] permissions) {
        return role.getManager().givePermissions(Arrays.asList(permissions));
    }

    @Property.Remover("discord permissions")
    public static AuditableRestAction<Void> removeDiscordPermissions(Role role, Permission[] permissions) {
        return role.getManager().revokePermissions(Arrays.asList(permissions));
    }

    @Property(value = "guild", shared = true)
    public static Guild guild(Role role) {
        return role.getGuild();
    }

    @Condition(property = "hoisted", type = ConditionType.BE)
    @Documentation(
            name = "Role is Hoisted",
            description = "Whether the role is hoisted (shown separately in the member list).",
            examples = "if {_role} is hoisted:",
            since = "5.0.0-a1"
    )
    public static boolean hoisted(Role r) {
        return r.isHoisted();
    }

    @Condition(property = "mentionable", type = ConditionType.BE)
    @Documentation(
            name = "Role is Mentionable",
            description = "Whether the role can be mentioned by anyone.",
            examples = "if {_role} is mentionable:",
            since = "5.0.0-a1"
    )
    public static boolean mentionable(Role r) {
        return r.isMentionable();
    }
}
