package net.itsthesky.disky5.elements.structures;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.StringMode;
import ch.njol.skript.util.Utils;
import ch.njol.util.NonNullPair;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.itsthesky.disky5.api.commands.ArgumentSpecParser;
import net.itsthesky.disky5.api.commands.ArgumentType;
import net.itsthesky.disky5.api.commands.ArgumentTypes;
import net.itsthesky.disky5.api.commands.CommandArgument;
import net.itsthesky.disky5.api.commands.CommandDefinition;
import net.itsthesky.disky5.api.commands.CooldownTracker;
import net.itsthesky.disky5.api.commands.CommandRegistry;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_DiscordCommandFailed;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_DiscordCommandUsed;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.KeyValueEntryData;
import org.skriptlang.skript.lang.entry.SectionEntryData;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.lang.structure.Structure;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.MatchResult;

@Name("Discord Command")
@Description({
    "Declares a command triggered by a message: '<prefix><name> <arguments>'. Arguments are written like",
    "Skript's own commands ('<user>', '<n: number = 1>', '[<text>]'); Discord entities are resolved from a",
    "mention, an id or a name before the trigger runs, so 'arg-1' / '{_n}' hold real values.",
    "Entries: prefixes, aliases, description, usage, category, roles, permissions, bots, executable in",
    "(guild, dm, threads, voice, text), cooldown, cooldown scope (user, guild, channel), permission message,",
    "cooldown message, usage message, trigger. Without 'prefixes' the bare name matches."
})
@Examples({
    "discord command hello <user> [<n: number = 1>]:",
    "    prefixes: \"!\", \"?\"",
    "    aliases: hi",
    "    executable in: guild",
    "    cooldown: 5 seconds",
    "    cooldown message: \"Slow down, %remaining cooldown% left.\"",
    "    usage message: \"Usage: %usage of event-discordcommand%\"",
    "    trigger:",
    "        loop {_n} times:",
    "            reply with \"Hello %arg-1%!\""
})
@Since("5.0.0-a1")
public final class StructDiscordCommand extends Structure {

    private static final String LIST_SEPARATOR = "\\s*,\\s*|\\s+(?:and|or)\\s+";

    public static void register() {
        ParserInstance.registerData(CommandParseData.class, CommandParseData::new);
        ManualSyntax.structure(
            StructDiscordCommand.class,
            EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("prefixes", null, true, String.class))
                .addEntryData(stringEntry("aliases"))
                .addEntryData(stringEntry("description"))
                .addEntryData(stringEntry("usage"))
                .addEntryData(stringEntry("category"))
                .addEntryData(stringEntry("roles"))
                .addEntryData(stringEntry("permissions"))
                .addEntryData(stringEntry("bots"))
                .addEntryData(stringEntry("executable in"))
                .addEntryData(stringEntry("cooldown"))
                .addEntryData(stringEntry("cooldown scope"))
                .addEntryData(stringEntry("permission message"))
                .addEntryData(stringEntry("cooldown message"))
                .addEntryData(stringEntry("usage message"))
                .addEntryData(new SectionEntryData("trigger", null, false))
                .unexpectedEntryMessage(key -> "Unexpected entry '" + key + "' in a discord command. Known entries: "
                    + "prefixes, aliases, description, usage, category, roles, permissions, bots, executable in, "
                    + "cooldown, cooldown scope, permission message, cooldown message, usage message, trigger.")
                .build(),
            "discord command <([^\\s]+)( .+)?$>"
        );
    }

    private static KeyValueEntryData<String> stringEntry(String key) {
        return new KeyValueEntryData<>(key, null, true) {
            @Override
            protected String getValue(@NotNull String value) {
                return value;
            }
        };
    }

    private String name;
    private List<CommandArgument> arguments;
    private @Nullable CommandDefinition definition;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern,
                        SkriptParser.@NotNull ParseResult parseResult, @Nullable EntryContainer entryContainer) {
        MatchResult header = parseResult.regexes.get(0);
        name = header.group(1);
        String spec = header.group(2);
        arguments = parseArguments(spec == null ? "" : spec.trim());
        return arguments != null;
    }

    private static @Nullable List<CommandArgument> parseArguments(String spec) {
        List<ArgumentSpecParser.Segment> segments = ArgumentSpecParser.scan(spec, "discord command");
        if (segments == null)
            return null;
        List<CommandArgument> result = new ArrayList<>();
        for (ArgumentSpecParser.Segment segment : segments) {
            CommandArgument argument = parseArgument(segment);
            if (argument == null)
                return null;
            result.add(argument);
        }
        return result;
    }

    private static @Nullable CommandArgument parseArgument(ArgumentSpecParser.Segment segment) {
        String argName = segment.name(), typeName = segment.typeName(), def = segment.defaultValue();
        boolean optional = segment.optional();
        int index = segment.index();
        NonNullPair<String, Boolean> plural = Utils.getEnglishPlural(typeName);
        ClassInfo<?> info = Classes.getClassInfoFromUserInput(plural.getFirst());
        if (info == null)
            info = Classes.getClassInfoFromUserInput(typeName);
        if (info == null) {
            Skript.error("Unknown type '" + typeName + "' for an argument of the discord command");
            return null;
        }
        ArgumentType<?> type = ArgumentTypes.of(info);
        if (type == null) {
            Skript.error("'" + info.getCodeName() + "' cannot be used as an argument of a discord command");
            return null;
        }
        Expression<?> defaultValue = null;
        if (def != null) {
            defaultValue = parseDefault(def, info);
            if (defaultValue == null) {
                Skript.error("'" + def + "' is not a valid default value for a " + info.getCodeName() + " argument");
                return null;
            }
        }
        boolean single = !plural.getSecond();
        return new CommandArgument(argName, type, info, single, optional || def != null, defaultValue, index);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static @Nullable Expression<?> parseDefault(String def, ClassInfo<?> info) {
        Object literal = Classes.parse(def, info.getC(), ParseContext.COMMAND);
        if (literal != null)
            return new SimpleLiteral(literal, false);
        return new SkriptParser(def, SkriptParser.ALL_FLAGS, ParseContext.DEFAULT).parseExpression(info.getC());
    }

    @Override
    public boolean load() {
        EntryContainer container = getEntryContainer();
        SectionNode triggerNode = container.get("trigger", SectionNode.class, false);

        CommandDefinition.Builder builder = CommandDefinition.builder(getParser().getCurrentScript(), name)
            .arguments(arguments)
            .aliases(list(container, "aliases"))
            .description(single(container, "description"))
            .usage(single(container, "usage"))
            .category(single(container, "category"))
            .roles(list(container, "roles"))
            .bots(list(container, "bots"));

        Expression<? extends String> prefixes = container.getOptional("prefixes", Expression.class, true);
        if (prefixes != null)
            builder.prefixes(List.of(prefixes));

        Set<Permission> permissions = EnumSet.noneOf(Permission.class);
        for (String word : list(container, "permissions")) {
            Permission permission = Classes.parse(word, Permission.class, ParseContext.DEFAULT);
            if (permission == null) {
                Skript.error("Unknown permission '" + word + "' in the discord command '" + name + "'");
                return false;
            }
            permissions.add(permission);
        }
        builder.permissions(permissions);

        Set<ChannelType> executableIn = parsePlaces(single(container, "executable in"));
        if (executableIn == null)
            return false;
        builder.executableIn(executableIn);

        String cooldownText = single(container, "cooldown");
        if (cooldownText != null) {
            Timespan cooldown = Timespan.parse(cooldownText);
            if (cooldown == null) {
                Skript.error("'" + cooldownText + "' is not a valid cooldown (expected a timespan like '5 seconds')");
                return false;
            }
            CooldownTracker.Scope scope = CooldownTracker.parseScope(single(container, "cooldown scope"));
            if (scope == null)
                return false;
            builder.cooldown(cooldown, scope);
        }

        ParserInstance parser = getParser();

        parser.setCurrentEvent("discord command failed", GeneratedBukkitEvt_DiscordCommandFailed.class);
        try {
            builder.permissionMessage(message(container, "permission message"))
                .cooldownMessage(message(container, "cooldown message"))
                .usageMessage(message(container, "usage message"));
        } finally {
            parser.deleteCurrentEvent();
        }

        CommandParseData data = parser.getData(CommandParseData.class);
        parser.setCurrentEvent("discord command", GeneratedBukkitEvt_DiscordCommandUsed.class);
        data.arguments(arguments);
        List<TriggerItem> items;
        try {
            items = ScriptLoader.loadItems(triggerNode);
        } finally {
            data.arguments(null);
            parser.deleteCurrentEvent();
        }
        builder.trigger(new Trigger(parser.getCurrentScript(), "discord command " + name, new SimpleEvent(), items));
        definition = builder.build();
        return true;
    }

    @Override
    public boolean postLoad() {
        if (definition != null)
            CommandRegistry.INSTANCE.register(definition);
        return true;
    }

    @Override
    public void unload() {
        if (definition != null)
            CommandRegistry.INSTANCE.unregister(definition);
        definition = null;
    }

    private static @Nullable VariableString message(EntryContainer container, String key) {
        String text = single(container, key);
        if (text == null) return null;
        if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\""))
            text = text.substring(1, text.length() - 1);
        return VariableString.newInstance(text, StringMode.MESSAGE);
    }

    private static @Nullable String single(EntryContainer container, String key) {
        String value = container.getOptional(key, String.class, true);
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static List<String> list(EntryContainer container, String key) {
        String value = single(container, key);
        if (value == null) return List.of();
        List<String> result = new ArrayList<>();
        for (String part : value.split(LIST_SEPARATOR)) {
            String item = part.trim();
            if (item.length() >= 2 && item.startsWith("\"") && item.endsWith("\""))
                item = item.substring(1, item.length() - 1);
            if (!item.isEmpty())
                result.add(item);
        }
        return result;
    }

    private static @Nullable Set<ChannelType> parsePlaces(@Nullable String value) {
        Set<ChannelType> guild = EnumSet.of(ChannelType.TEXT, ChannelType.NEWS, ChannelType.VOICE, ChannelType.STAGE,
            ChannelType.GUILD_PUBLIC_THREAD, ChannelType.GUILD_PRIVATE_THREAD, ChannelType.GUILD_NEWS_THREAD);
        if (value == null) {
            Set<ChannelType> all = EnumSet.copyOf(guild);
            all.add(ChannelType.PRIVATE);
            return all;
        }
        Set<ChannelType> result = EnumSet.noneOf(ChannelType.class);
        for (String part : value.split(LIST_SEPARATOR)) {
            switch (part.trim().toLowerCase(Locale.ROOT)) {
                case "guild", "server" -> result.addAll(guild);
                case "dm", "pm", "private message", "direct message", "private" -> result.add(ChannelType.PRIVATE);
                case "thread", "threads" -> result.addAll(EnumSet.of(ChannelType.GUILD_PUBLIC_THREAD,
                    ChannelType.GUILD_PRIVATE_THREAD, ChannelType.GUILD_NEWS_THREAD));
                case "voice" -> result.addAll(EnumSet.of(ChannelType.VOICE, ChannelType.STAGE));
                case "text" -> result.addAll(EnumSet.of(ChannelType.TEXT, ChannelType.NEWS));
                default -> {
                    Skript.error("'" + part.trim() + "' is not a valid place for 'executable in' "
                        + "(expected guild, dm, threads, voice or text)");
                    return null;
                }
            }
        }
        return result;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "discord command " + name;
    }
}
