package net.itsthesky.disky5.descriptors.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandHolder;
import net.itsthesky.disky5.api.commands.application.Localizations;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@SkriptType(name = "applicationcommand", jda = ApplicationCommandHolder.class)
@Documentation(
    name = "Application Command",
    description = {"A slash, user or message command as Discord sees it: what 'update the discord commands' and "
        + "'register the discord command' push, and what 'registered commands of' brings back.",
        "Made by 'a new slash/user/message command', by the 'slash command' / 'user command' / 'message command' "
            + "structures (read them back with 'structured slash command \"name\"'), or retrieved from Discord."},
    since = "5.0.0-a2")
public final class ApplicationCommandType {

    private ApplicationCommandType() {}

    @Property(value = "name", shared = true)
    public static String name(ApplicationCommandHolder command) {
        return command.name();
    }

    @Property.Setter(value = "name", shared = true)
    public static void setName(ApplicationCommandHolder command, String name) {
        command.data().setName(name);
    }

    @Property("command id")
    @Documentation(
        name = "Command Id",
        description = "The Discord id of the command, known once it was registered or retrieved; none before.",
        examples = {"send command id of {_cmd} to console"},
        since = "5.0.0-a2")
    public static @Nullable String commandId(ApplicationCommandHolder command) {
        Long id = command.id();
        return id == null ? null : Long.toUnsignedString(id);
    }

    @Property("command type")
    @Documentation(
        name = "Command Type",
        description = "\"slash\", \"user\" or \"message\".",
        examples = {"if command type of {_cmd} is \"slash\":"},
        since = "5.0.0-a2")
    public static String commandType(ApplicationCommandHolder command) {
        return command.type().name().toLowerCase();
    }

    @Property("enabled for")
    @Documentation(
        name = "Command Enabled For",
        description = {"The permissions a member needs to see and use the command by default (admins always can). "
            + "Empty means everyone.",
            "Setting it to nothing re-enables the command for everyone; see 'disabled' to hide it from everyone."},
        examples = {"set enabled for of {_cmd} to manage server and ban members",
            "add kick members to enabled for of {_cmd}"},
        since = "5.0.0-a2")
    public static List<Permission> enabledFor(ApplicationCommandHolder command) {
        Long raw = command.data().getDefaultPermissions().getPermissionsRaw();
        return raw == null || raw == 0 ? List.of() : new ArrayList<>(Permission.getPermissions(raw));
    }

    @Property.Setter("enabled for")
    public static void setEnabledFor(ApplicationCommandHolder command, Permission[] permissions) {
        command.data().setDefaultPermissions(permissions.length == 0
            ? DefaultMemberPermissions.ENABLED : DefaultMemberPermissions.enabledFor(permissions));
    }

    @Property.Adder("enabled for")
    public static void addEnabledFor(ApplicationCommandHolder command, Permission permission) {
        Set<Permission> current = EnumSet.noneOf(Permission.class);
        current.addAll(enabledFor(command));
        current.add(permission);
        command.data().setDefaultPermissions(DefaultMemberPermissions.enabledFor(current));
    }

    @Property("disabled")
    @Documentation(
        name = "Command Disabled",
        description = "Whether the command is hidden from everyone but administrators by default.",
        examples = {"set disabled of {_cmd} to true"},
        since = "5.0.0-a2")
    public static Boolean disabled(ApplicationCommandHolder command) {
        Long raw = command.data().getDefaultPermissions().getPermissionsRaw();
        return raw != null && raw == 0;
    }

    @Property.Setter("disabled")
    public static void setDisabled(ApplicationCommandHolder command, Boolean disabled) {
        command.data().setDefaultPermissions(disabled ? DefaultMemberPermissions.DISABLED : DefaultMemberPermissions.ENABLED);
    }

    @Property("nsfw")
    @Documentation(
        name = "Command NSFW",
        description = "Whether the command is age-restricted (only usable in NSFW channels and by adult users).",
        examples = {"set nsfw of {_cmd} to true"},
        since = "5.0.0-a2")
    public static Boolean nsfw(ApplicationCommandHolder command) {
        return command.data().isNSFW();
    }

    @Property.Setter("nsfw")
    public static void setNsfw(ApplicationCommandHolder command, Boolean nsfw) {
        command.data().setNSFW(nsfw);
    }

    @Property("contexts")
    @Documentation(
        name = "Command Contexts",
        description = "Where the command can be used: guild, bot dm and/or group dm. Discord's default is guild only.",
        examples = {"set contexts of {_cmd} to guild and bot dm"},
        since = "5.0.0-a2")
    public static List<InteractionContextType> contexts(ApplicationCommandHolder command) {
        return new ArrayList<>(command.data().getContexts());
    }

    @Property.Setter("contexts")
    public static void setContexts(ApplicationCommandHolder command, InteractionContextType[] contexts) {
        command.data().setContexts(Set.of(contexts));
    }

    @Property.Adder("contexts")
    public static void addContext(ApplicationCommandHolder command, InteractionContextType context) {
        Set<InteractionContextType> current = EnumSet.noneOf(InteractionContextType.class);
        current.addAll(command.data().getContexts());
        current.add(context);
        command.data().setContexts(current);
    }

    @Property.Remover("contexts")
    public static void removeContext(ApplicationCommandHolder command, InteractionContextType context) {
        Set<InteractionContextType> current = EnumSet.noneOf(InteractionContextType.class);
        current.addAll(command.data().getContexts());
        current.remove(context);
        command.data().setContexts(current);
    }

    @Property("integration types")
    @Documentation(
        name = "Command Integration Types",
        description = "How the command is installed: guild install (the default) and/or user install.",
        examples = {"set integration types of {_cmd} to guild install and user install"},
        since = "5.0.0-a2")
    public static List<IntegrationType> integrationTypes(ApplicationCommandHolder command) {
        return new ArrayList<>(command.data().getIntegrationTypes());
    }

    @Property.Setter("integration types")
    public static void setIntegrationTypes(ApplicationCommandHolder command, IntegrationType[] types) {
        command.data().setIntegrationTypes(Set.of(types));
    }

    @Property.Adder("integration types")
    public static void addIntegrationType(ApplicationCommandHolder command, IntegrationType type) {
        Set<IntegrationType> current = EnumSet.noneOf(IntegrationType.class);
        current.addAll(command.data().getIntegrationTypes());
        current.add(type);
        command.data().setIntegrationTypes(current);
    }

    @Property.Remover("integration types")
    public static void removeIntegrationType(ApplicationCommandHolder command, IntegrationType type) {
        Set<IntegrationType> current = EnumSet.noneOf(IntegrationType.class);
        current.addAll(command.data().getIntegrationTypes());
        current.remove(type);
        command.data().setIntegrationTypes(current);
    }

    @Property("name localizations")
    @Documentation(
        name = "Command Name Localizations",
        description = "The translated names, as \"locale: text\" lines. Set them with 'localize the name of ...'.",
        examples = {"loop name localizations of {_cmd}:"},
        since = "5.0.0-a2")
    public static List<String> nameLocalizations(ApplicationCommandHolder command) {
        return Localizations.describe(command.data().getNameLocalizations());
    }
}
