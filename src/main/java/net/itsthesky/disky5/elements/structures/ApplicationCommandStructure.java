package net.itsthesky.disky5.elements.structures;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.SimpleNode;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.itsthesky.disky5.api.commands.CooldownTracker;
import net.itsthesky.disky5.api.commands.InvocationArgument;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandModel;
import net.itsthesky.disky5.api.commands.application.Localizations;
import net.itsthesky.disky5.api.commands.application.NodeEntryData;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_ApplicationCommandCooldown;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.KeyValueEntryData;
import org.skriptlang.skript.lang.entry.SectionEntryData;
import org.skriptlang.skript.lang.structure.Structure;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

abstract class ApplicationCommandStructure extends Structure {

    static final String LIST_SEPARATOR = "\\s*,\\s*|\\s+(?:and|or)\\s+";
    static final Pattern DISCORD_NAME = Pattern.compile("^[-_'\\p{L}\\p{N}\\p{sc=Deva}\\p{sc=Thai}]{1,32}$");

    static EntryValidator.EntryValidatorBuilder commonEntries(EntryValidator.EntryValidatorBuilder builder) {
        return builder
            .addEntryData(new SectionEntryData("name", null, true))
            .addEntryData(stringEntry("enabled for"))
            .addEntryData(stringEntry("disabled"))
            .addEntryData(stringEntry("nsfw"))
            .addEntryData(stringEntry("contexts"))
            .addEntryData(stringEntry("integration types"))
            .addEntryData(stringEntry("cooldown"))
            .addEntryData(stringEntry("cooldown scope"))
            .addEntryData(new SectionEntryData("on cooldown", null, true));
    }

    static KeyValueEntryData<String> stringEntry(String key) {
        return new KeyValueEntryData<>(key, null, true) {
            @Override
            protected String getValue(@NotNull String value) {
                return value;
            }
        };
    }

    boolean loadCommon(EntryContainer container, ApplicationCommandModel model, String what,
                       @Nullable List<? extends InvocationArgument> arguments) {
        SectionNode nameSection = container.getOptional("name", SectionNode.class, true);
        if (nameSection != null) {
            Map<DiscordLocale, String> names = Localizations.parse(nameSection);
            if (names == null) return false;
            model.nameLocalizations().putAll(names);
        }

        for (String word : list(container, "enabled for")) {
            Permission permission = Classes.parse(word, Permission.class, ParseContext.DEFAULT);
            if (permission == null) {
                Skript.error("Unknown permission '" + word + "' in the " + what);
                return false;
            }
            model.enabledFor().add(permission);
        }
        Boolean disabled = bool(container, "disabled", what);
        if (disabled == null) return false;
        model.disabled(disabled);
        Boolean nsfw = bool(container, "nsfw", what);
        if (nsfw == null) return false;
        model.nsfw(nsfw);

        List<String> contextWords = list(container, "contexts");
        if (!contextWords.isEmpty()) {
            Set<InteractionContextType> contexts = EnumSet.noneOf(InteractionContextType.class);
            for (String word : contextWords) {
                InteractionContextType context = Classes.parse(word, InteractionContextType.class, ParseContext.DEFAULT);
                if (context == null) {
                    Skript.error("'" + word + "' is not an interaction context (expected guild, bot dm or group dm)");
                    return false;
                }
                contexts.add(context);
            }
            model.contexts(contexts);
        }
        List<String> integrationWords = list(container, "integration types");
        if (!integrationWords.isEmpty()) {
            Set<IntegrationType> types = EnumSet.noneOf(IntegrationType.class);
            for (String word : integrationWords) {
                IntegrationType type = Classes.parse(word, IntegrationType.class, ParseContext.DEFAULT);
                if (type == null) {
                    Skript.error("'" + word + "' is not an integration type (expected guild install or user install)");
                    return false;
                }
                types.add(type);
            }
            model.integrationTypes(types);
        }

        String cooldownText = single(container, "cooldown");
        SectionNode onCooldown = container.getOptional("on cooldown", SectionNode.class, true);
        if (cooldownText != null) {
            Timespan cooldown = Timespan.parse(cooldownText);
            if (cooldown == null) {
                Skript.error("'" + cooldownText + "' is not a valid cooldown (expected a timespan like '5 seconds')");
                return false;
            }
            CooldownTracker.Scope scope = CooldownTracker.parseScope(single(container, "cooldown scope"));
            if (scope == null) return false;
            model.cooldowns(new CooldownTracker(cooldown, scope));
        } else if (onCooldown != null) {
            Skript.error("The " + what + " has an 'on cooldown' section but no 'cooldown' entry");
            return false;
        }
        if (onCooldown != null) {
            Trigger trigger = loadTrigger(onCooldown, "application command cooldown",
                GeneratedBukkitEvt_ApplicationCommandCooldown.class, "on cooldown of " + what, arguments);
            model.onCooldown(trigger);
        }
        return true;
    }

    Trigger loadTrigger(SectionNode section, String eventName, Class<? extends Event> eventClass, String label,
                        @Nullable List<? extends InvocationArgument> arguments) {
        ParserInstance parser = getParser();
        CommandParseData data = parser.getData(CommandParseData.class);
        parser.setCurrentEvent(eventName, eventClass);
        data.arguments(arguments);
        List<TriggerItem> items;
        try {
            items = ScriptLoader.loadItems(section);
        } finally {
            data.arguments(null);
            parser.deleteCurrentEvent();
        }
        return new Trigger(parser.getCurrentScript(), label, new SimpleEvent(), items);
    }

    static boolean validName(String part) {
        return DISCORD_NAME.matcher(part).matches() && part.equals(part.toLowerCase(Locale.ROOT));
    }

    static boolean readDescription(EntryContainer container, String what,
                                   java.util.function.Consumer<String> description, Map<DiscordLocale, String> localizations) {
        Node node = container.getOptional("description", Node.class, true);
        if (node == null) return true;
        if (node instanceof SimpleNode simple) {
            String text = NodeEntryData.valueOf(simple, "description");
            if (text.isEmpty()) {
                Skript.error("The description of the " + what + " is empty");
                return false;
            }
            description.accept(text);
            return true;
        }
        Map<DiscordLocale, String> map = Localizations.parse((SectionNode) node);
        if (map == null) return false;
        String base = map.remove(DiscordLocale.ENGLISH_US);
        if (base == null) {
            Skript.error("A localized description needs a default 'en-US' (or 'en') line");
            return false;
        }
        description.accept(base);
        localizations.putAll(map);
        return true;
    }

    static @Nullable Boolean bool(EntryContainer container, String key, String what) {
        String value = single(container, key);
        if (value == null) return false;
        return switch (value.toLowerCase(Locale.ROOT)) {
            case "true", "yes", "on" -> true;
            case "false", "no", "off" -> false;
            default -> {
                Skript.error("'" + value + "' is not a boolean for '" + key + "' of the " + what + " (expected true or false)");
                yield null;
            }
        };
    }

    static @Nullable String single(EntryContainer container, String key) {
        String value = container.getOptional(key, String.class, true);
        return value == null || value.isBlank() ? null : value.trim();
    }

    static List<String> list(EntryContainer container, String key) {
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
}
