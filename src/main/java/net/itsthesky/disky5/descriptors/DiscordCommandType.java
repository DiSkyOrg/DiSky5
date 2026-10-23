package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Timespan;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.commands.CommandDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "discordcommand", jda = CommandDefinition.class)
@Documentation(
    name = "Discord Command",
    description = {"A command declared with the 'discord command' structure. Available as "
        + "'event-discordcommand' in its trigger and in the 'discord command used' / 'failed' events, "
        + "or through 'all discord commands'."},
    examples = {"reply with \"Usage: %usage of event-discordcommand%\""},
    since = "5.0.0-a1")
public final class DiscordCommandType {

    private DiscordCommandType() {}

    @Property(value = "name", shared = true)
    public static String name(CommandDefinition command) {
        return command.name();
    }

    @Property("description")
    @Documentation(
        name = "Command Description",
        description = "The 'description' entry of the command, if any.",
        examples = {"send description of event-discordcommand to console"},
        since = "5.0.0-a1")
    public static @Nullable String description(CommandDefinition command) {
        return command.description();
    }

    @Property("usage")
    @Documentation(
        name = "Command Usage",
        description = "The 'usage' entry of the command, or a generated one from its name and arguments.",
        examples = {"reply with \"Usage: %usage of event-discordcommand%\""},
        since = "5.0.0-a1")
    public static String usage(CommandDefinition command) {
        return command.usage() != null ? command.usage() : command.defaultUsage();
    }

    @Property("category")
    @Documentation(
        name = "Command Category",
        description = "The 'category' entry of the command, if any.",
        examples = {"if category of event-discordcommand is \"admin\":"},
        since = "5.0.0-a1")
    public static @Nullable String category(CommandDefinition command) {
        return command.category();
    }

    @Property("aliases")
    @Documentation(
        name = "Command Aliases",
        description = "The aliases of the command, without its main name.",
        examples = {"send aliases of event-discordcommand to console"},
        since = "5.0.0-a1")
    public static List<String> aliases(CommandDefinition command) {
        return command.aliases();
    }

    @Property("prefixes")
    @Documentation(
        name = "Command Prefixes",
        description = "The prefixes of the command. Empty when it answers to its bare name.",
        examples = {"send prefixes of event-discordcommand to console"},
        since = "5.0.0-a1")
    public static List<String> prefixes(CommandDefinition command) {
        return command.prefixes(null);
    }

    @Property("cooldown")
    @Documentation(
        name = "Command Cooldown",
        description = "The 'cooldown' entry of the command, if any.",
        examples = {"send cooldown of event-discordcommand to console"},
        since = "5.0.0-a1")
    public static @Nullable Timespan cooldown(CommandDefinition command) {
        return command.cooldown();
    }
}
