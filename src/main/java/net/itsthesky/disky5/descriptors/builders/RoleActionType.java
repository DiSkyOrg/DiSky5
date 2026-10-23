package net.itsthesky.disky5.descriptors.builders;

import ch.njol.skript.util.Color;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "roleaction", jda = RoleAction.class)
@Documentation(
    name = "Role Builder",
    description = {"A role that does not exist yet: configure it, then create it with 'the build result of'.",
        "Nothing is sent to Discord until then, so setting its properties costs no request."},
    since = "5.0.0-a1")
public final class RoleActionType {

    private RoleActionType() {}

    private static final int MAX_ROLES_PER_GUILD = 250;

    @New("[a] new role builder [(named|with [the] name)] %string% in [the] [guild] %guild%")
    @Documentation(
        name = "New Role Builder",
        description = {"Creates a role builder. The role is only created on Discord once you read "
            + "'the build result of' it, which requires 'await'."},
        examples = {"set {_b} to a new role builder named \"Moderator\" in event-guild",
            "set color of {_b} to orange",
            "set hoisted of {_b} to true",
            "await set {_role} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static RoleAction newRoleBuilder(String name, Guild guild) {
        return guild.createRole().setName(name);
    }

    @Build
    @Documentation(
        name = "Built Role",
        description = {"Creates the role on Discord and returns it. Requires 'await'.",
            "Building the same builder twice creates two roles."},
        examples = {"await set {_role} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static RestAction<Role> build(RoleAction action) {
        if (action.getGuild().getRoles().size() >= MAX_ROLES_PER_GUILD)
            throw new IllegalStateException("this guild already has the maximum of "
                + MAX_ROLES_PER_GUILD + " roles");
        return action;
    }

    @Property.Setter(value = "color", shared = true)
    public static void setColor(RoleAction action, @Nullable Color value) {
        action.setColor(Conversions.awt(value));
    }

    @Property.Setter("hoisted")
    @Documentation(
        name = "Role Builder Hoisted",
        description = "Whether the role being built is displayed separately in the member list.",
        examples = {"set hoisted of {_b} to true"},
        since = "5.0.0-a1")
    public static void setHoisted(RoleAction action, Boolean value) {
        action.setHoisted(value);
    }

    @Property.Setter("mentionable")
    @Documentation(
        name = "Role Builder Mentionable",
        description = "Whether the role being built can be mentioned by anyone.",
        examples = {"set mentionable of {_b} to false"},
        since = "5.0.0-a1")
    public static void setMentionable(RoleAction action, Boolean value) {
        action.setMentionable(value);
    }
}
