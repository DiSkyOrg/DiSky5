package net.itsthesky.disky5.descriptors.commands;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.commands.application.Localizations;
import net.itsthesky.disky5.api.commands.application.SlashCommandHolder;
import net.itsthesky.disky5.api.commands.application.SlashOptionHolder;
import net.itsthesky.disky5.api.commands.application.SubcommandGroupHolder;
import net.itsthesky.disky5.api.commands.application.SubcommandHolder;

import java.util.ArrayList;
import java.util.List;

@SkriptType(name = "slashcommand", jda = SlashCommandHolder.class, parent = ApplicationCommandType.class)
@Documentation(
    name = "Slash Command",
    description = {"A '/name' command with a description and, either, options or subcommands (grouped or not).",
        "Build one, fill it with the properties, then push it with 'update the discord commands' or "
            + "'register the discord command'. Use 'on slash command' to answer it, or declare it with the "
            + "'slash command' structure so its trigger runs on its own."},
    since = "5.0.0-a2")
public final class SlashCommandType {

    private SlashCommandType() {}

    @New("[a] new slash command (named|with [the] name) %string% with [the] desc[ription] %string%")
    @Documentation(
        name = "New Slash Command",
        description = "Creates a slash command. The name must be lowercase, 1 to 32 characters, without spaces.",
        examples = {"set {_cmd} to a new slash command named \"ping\" with description \"Pong!\"",
            "add a new string option named \"text\" with description \"What to echo\" to options of {_cmd}",
            "update the discord commands {_cmd} in event-guild"},
        since = "5.0.0-a2")
    public static SlashCommandHolder newSlashCommand(String name, String description) {
        return new SlashCommandHolder(Commands.slash(name, description));
    }

    @Property("description")
    public static String description(SlashCommandHolder command) {
        return command.data().getDescription();
    }

    @Property.Setter("description")
    public static void setDescription(SlashCommandHolder command, String description) {
        command.data().setDescription(description);
    }

    @Property("description localizations")
    @Documentation(
        name = "Command Description Localizations",
        description = "The translated descriptions, as \"locale: text\" lines. Set them with 'localize the description of ...'.",
        examples = {"loop description localizations of {_cmd}:"},
        since = "5.0.0-a2")
    public static List<String> descriptionLocalizations(SlashCommandHolder command) {
        return Localizations.describe(command.data().getDescriptionLocalizations());
    }

    @Property("options")
    @Documentation(
        name = "Slash Command Options",
        description = {"The options (arguments) of a slash command or a subcommand, in order.",
            "A command with subcommands cannot have options of its own."},
        examples = {"add a new user option named \"target\" with description \"Who\" to options of {_cmd}",
            "remove {_opt} from options of {_cmd}"},
        since = "5.0.0-a2")
    public static List<SlashOptionHolder> options(SlashCommandHolder command) {
        List<SlashOptionHolder> options = new ArrayList<>();
        for (OptionData option : command.data().getOptions())
            options.add(SlashOptionHolder.from(option));
        return options;
    }

    @Property.Setter("options")
    public static void setOptions(SlashCommandHolder command, SlashOptionHolder[] options) {
        command.data().removeOptions(o -> true);
        for (SlashOptionHolder option : options)
            command.data().addOptions(option.build());
    }

    @Property.Adder("options")
    public static void addOption(SlashCommandHolder command, SlashOptionHolder option) {
        command.data().addOptions(option.build());
    }

    @Property.Remover("options")
    public static void removeOption(SlashCommandHolder command, SlashOptionHolder option) {
        command.data().removeOptionByName(option.data().getName());
    }

    @Property("subcommands")
    @Documentation(
        name = "Slash Command Subcommands",
        description = "The subcommands ('/root sub') directly under a slash command or under a subcommand group.",
        examples = {"add {_ban} to subcommands of {_mod}"},
        since = "5.0.0-a2")
    public static List<SubcommandHolder> subcommands(SlashCommandHolder command) {
        List<SubcommandHolder> subcommands = new ArrayList<>();
        for (SubcommandData subcommand : command.data().getSubcommands())
            subcommands.add(SubcommandHolder.from(subcommand));
        return subcommands;
    }

    @Property.Setter("subcommands")
    public static void setSubcommands(SlashCommandHolder command, SubcommandHolder[] subcommands) {
        command.data().removeSubcommands(s -> true);
        for (SubcommandHolder subcommand : subcommands)
            command.data().addSubcommands(subcommand.build());
    }

    @Property.Adder("subcommands")
    public static void addSubcommand(SlashCommandHolder command, SubcommandHolder subcommand) {
        command.data().addSubcommands(subcommand.build());
    }

    @Property.Remover("subcommands")
    public static void removeSubcommand(SlashCommandHolder command, SubcommandHolder subcommand) {
        command.data().removeSubcommandByName(subcommand.data().getName());
    }

    @Property("subcommand groups")
    @Documentation(
        name = "Slash Command Subcommand Groups",
        description = "The subcommand groups ('/root group sub') of a slash command.",
        examples = {"add {_group} to subcommand groups of {_settings}"},
        since = "5.0.0-a2")
    public static List<SubcommandGroupHolder> subcommandGroups(SlashCommandHolder command) {
        List<SubcommandGroupHolder> groups = new ArrayList<>();
        for (SubcommandGroupData group : command.data().getSubcommandGroups())
            groups.add(SubcommandGroupHolder.from(group));
        return groups;
    }

    @Property.Setter("subcommand groups")
    public static void setSubcommandGroups(SlashCommandHolder command, SubcommandGroupHolder[] groups) {
        command.data().removeSubcommandGroups(g -> true);
        for (SubcommandGroupHolder group : groups)
            command.data().addSubcommandGroups(group.build());
    }

    @Property.Adder("subcommand groups")
    public static void addSubcommandGroup(SlashCommandHolder command, SubcommandGroupHolder group) {
        command.data().addSubcommandGroups(group.build());
    }

    @Property.Remover("subcommand groups")
    public static void removeSubcommandGroup(SlashCommandHolder command, SubcommandGroupHolder group) {
        command.data().removeSubcommandGroupByName(group.data().getName());
    }
}
