package net.itsthesky.disky5.elements.structures;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.SimpleNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Utils;
import ch.njol.util.NonNullPair;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.itsthesky.disky5.api.commands.ArgumentSpecParser;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandRegistry;
import net.itsthesky.disky5.api.commands.application.NodeEntryData;
import net.itsthesky.disky5.api.commands.application.SlashArgument;
import net.itsthesky.disky5.api.commands.application.SlashArgumentType;
import net.itsthesky.disky5.api.commands.application.SlashArgumentTypes;
import net.itsthesky.disky5.api.commands.application.SlashCommandModel;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_SlashCommand;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_SlashCommandCompletionEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.SectionEntryData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.MatchResult;

@Name("Slash Command Structure")
@Description({
    "Declares a slash command: its path (up to three words: command, group, subcommand), its arguments "
        + "as '<name: type>' (wrapped in [ ] when optional), and a trigger that runs when it is used. "
        + "Every argument is available as '{_name}' and as 'arg-1' / 'user argument' in the trigger.",
    "Entries: description (a text, or a section of 'locale: text' lines with en-US), name (a section of "
        + "translated names), enabled for (permissions), disabled, nsfw, contexts, integration types, "
        + "cooldown + cooldown scope + on cooldown, arguments (per argument: a description, or a section "
        + "with description, choices and 'on completion request'), trigger.",
    "Declaring 'slash command mod ban' and 'slash command mod kick' gives one '/mod' command with two "
        + "subcommands; a 'slash command mod:' without trigger describes the root. Nothing is sent to Discord: "
        + "register the result from 'on ready' / 'on guild ready' with 'update the discord commands "
        + "(all structured application commands) in ...'."
})
@Examples({
    "slash command mod ban <target: member> [<reason: string>]:",
    "    description: Ban a member",
    "    enabled for: ban members",
    "    arguments:",
    "        target: Who to ban",
    "        reason: Why",
    "    trigger:",
    "        defer the interaction",
    "        ban {_target} with reason {_reason}",
    "        reply with \"Banned %{_target}%\"",
    "",
    "on guild ready:",
    "    update the discord commands (all structured application commands) in event-guild"
})
@Since("5.0.0-a2")
public final class StructSlashCommand extends ApplicationCommandStructure {

    private static final EntryValidator ARGUMENT_VALIDATOR = EntryValidator.builder()
        .addEntryData(stringEntry("description"))
        .addEntryData(new SectionEntryData("choices", null, true))
        .addEntryData(new SectionEntryData("on completion request", null, true))
        .unexpectedEntryMessage(key -> "Unexpected entry '" + key + "' in a slash command argument. Known entries: "
            + "description, choices, on completion request.")
        .build();

    public static void register() {
        ManualSyntax.structure(StructSlashCommand.class,
            commonEntries(EntryValidator.builder()
                .addEntryData(new NodeEntryData("description", true))
                .addEntryData(new SectionEntryData("arguments", null, true))
                .addEntryData(new SectionEntryData("trigger", null, true)))
                .unexpectedEntryMessage(key -> "Unexpected entry '" + key + "' in a slash command. Known entries: "
                    + "description, name, enabled for, disabled, nsfw, contexts, integration types, cooldown, "
                    + "cooldown scope, on cooldown, arguments, trigger.")
                .build(),
            "slash command <([\\p{L}\\p{N}_'-]+( [\\p{L}\\p{N}_'-]+){0,2})( .+)?$>"
        );
    }

    private String[] path;
    private List<SlashArgument> arguments;
    private @Nullable SlashCommandModel model;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern,
                        SkriptParser.@NotNull ParseResult parseResult, @Nullable EntryContainer entryContainer) {
        MatchResult header = parseResult.regexes.get(0);
        path = header.group(1).trim().split(" ");
        for (String part : path) {
            if (!validName(part)) {
                Skript.error("'" + part + "' is not a valid slash command name (lowercase, 1 to 32 letters, digits, - or _)");
                return false;
            }
        }
        String spec = header.group(3) == null ? "" : header.group(3).trim();
        if (!spec.isEmpty() && spec.charAt(0) != '<' && spec.charAt(0) != '[') {
            Skript.error("A slash command has at most three levels (command, group, subcommand); "
                + "arguments are written as <name: type>");
            return false;
        }
        arguments = parseArguments(spec);
        return arguments != null;
    }

    private static @Nullable List<SlashArgument> parseArguments(String spec) {
        List<ArgumentSpecParser.Segment> segments = ArgumentSpecParser.scan(spec, "slash command");
        if (segments == null) return null;
        List<SlashArgument> result = new ArrayList<>();
        for (ArgumentSpecParser.Segment segment : segments) {
            if (segment.name() == null) {
                Skript.error("A slash command argument needs a name: <" + segment.typeName() + "> should be <name: "
                    + segment.typeName() + ">");
                return null;
            }
            if (segment.defaultValue() != null) {
                Skript.error("A slash command argument cannot have a default value (Discord has none); make it "
                    + "optional and test {_" + segment.name() + "} in the trigger");
                return null;
            }
            if (!validName(segment.name())) {
                Skript.error("'" + segment.name() + "' is not a valid option name (lowercase, 1 to 32 letters, digits, - or _)");
                return null;
            }
            for (SlashArgument other : result)
                if (other.name().equals(segment.name())) {
                    Skript.error("Duplicated argument name '" + segment.name() + "'");
                    return null;
                }
            NonNullPair<String, Boolean> plural = Utils.getEnglishPlural(segment.typeName());
            if (plural.getSecond()) {
                Skript.error("A slash command argument is always a single value; use '" + plural.getFirst() + "'");
                return null;
            }
            ClassInfo<?> info = Classes.getClassInfoFromUserInput(segment.typeName());
            if (info == null) {
                Skript.error("Unknown type '" + segment.typeName() + "' for an argument of the slash command");
                return null;
            }
            SlashArgumentType<?> type = SlashArgumentTypes.of(info);
            if (type == null) {
                Skript.error("'" + info.getCodeName() + "' cannot be an argument of a slash command");
                return null;
            }
            result.add(new SlashArgument(segment.name(), type, info, !segment.optional(), segment.index()));
        }
        return result;
    }

    @Override
    public boolean load() {
        EntryContainer container = getEntryContainer();
        String what = "slash command '" + String.join(" ", path) + "'";
        SlashCommandModel model = new SlashCommandModel(getParser().getCurrentScript(), path);

        if (!readDescription(container, what, model::description, model.descriptionLocalizations()))
            return false;
        if (!loadArguments(container, what))
            return false;

        SectionNode triggerNode = container.getOptional("trigger", SectionNode.class, true);
        if (triggerNode == null && !arguments.isEmpty()) {
            Skript.error("The " + what + " has arguments but no trigger; a structure without trigger only describes a command or a group");
            return false;
        }
        if (triggerNode == null && path.length == 3) {
            Skript.error("The " + what + " has no trigger; only a command or a group (one or two words) can be described without one");
            return false;
        }
        model.arguments().addAll(arguments);

        if (!loadCommon(container, model, what, arguments))
            return false;
        if (triggerNode != null)
            model.trigger(loadTrigger(triggerNode, "slash command", GeneratedBukkitEvt_SlashCommand.class, what, arguments));
        this.model = model;
        return true;
    }

    private boolean loadArguments(EntryContainer container, String what) {
        SectionNode section = container.getOptional("arguments", SectionNode.class, true);
        if (section == null) return true;
        for (Node node : section) {
            String key = node.getKey();
            if (key == null) continue;
            String name;
            if (node instanceof SimpleNode) {
                int colon = key.indexOf(':');
                name = (colon < 0 ? key : key.substring(0, colon)).trim();
            } else {
                name = key.trim();
            }
            SlashArgument argument = null;
            for (SlashArgument candidate : arguments)
                if (candidate.name().equals(name)) argument = candidate;
            if (argument == null) {
                Skript.error("The " + what + " has no argument named '" + name + "'");
                return false;
            }
            if (node instanceof SimpleNode simple) {
                argument.description(NodeEntryData.valueOf(simple, name));
                continue;
            }
            EntryContainer entries = ARGUMENT_VALIDATOR.validate((SectionNode) node);
            if (entries == null) return false;
            String description = single(entries, "description");
            if (description != null) argument.description(description);
            SectionNode choices = entries.getOptional("choices", SectionNode.class, true);
            SectionNode completion = entries.getOptional("on completion request", SectionNode.class, true);
            if (choices != null && completion != null) {
                Skript.error("The argument '" + name + "' cannot have both choices and an 'on completion request' section");
                return false;
            }
            if (choices != null && !loadChoices(choices, argument)) return false;
            if (completion != null) {
                Trigger trigger = loadTrigger(completion, "slash completion", GeneratedBukkitEvt_SlashCommandCompletionEvent.class,
                    "completion of " + name + " in " + what, null);
                argument.completion(trigger);
            }
        }
        return true;
    }

    private static boolean loadChoices(SectionNode section, SlashArgument argument) {
        OptionType type = argument.type().optionType();
        if (!type.canSupportChoices()) {
            Skript.error("The argument '" + argument.name() + "' is a " + type.name().toLowerCase() + " option, which cannot have choices");
            return false;
        }
        for (Node node : section) {
            String key = node.getKey();
            if (!(node instanceof SimpleNode) || key == null || !key.contains(":")) {
                Skript.error("Expected 'name: value' choice lines, got '" + key + "'");
                return false;
            }
            String name = key.substring(0, key.indexOf(':')).trim();
            String value = NodeEntryData.valueOf((SimpleNode) node, name);
            try {
                argument.choices().add(switch (type) {
                    case INTEGER -> new Command.Choice(name, Long.parseLong(value));
                    case NUMBER -> new Command.Choice(name, Double.parseDouble(value));
                    default -> new Command.Choice(name, value);
                });
            } catch (NumberFormatException ex) {
                Skript.error("'" + value + "' is not a valid " + type.name().toLowerCase(Locale.ROOT) + " for the choice '" + name + "'");
                return false;
            }
        }
        if (argument.choices().size() > 25) {
            Skript.error("The argument '" + argument.name() + "' has more than 25 choices; use 'on completion request' instead");
            return false;
        }
        return true;
    }

    @Override
    public boolean postLoad() {
        if (model != null)
            ApplicationCommandRegistry.INSTANCE.register(model);
        return true;
    }

    @Override
    public void unload() {
        if (model != null)
            ApplicationCommandRegistry.INSTANCE.unregister(model);
        model = null;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "slash command " + String.join(" ", path);
    }
}
