package net.itsthesky.disky5.elements.structures;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import net.dv8tion.jda.api.OnlineStatus;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.util.ContextlessEvent;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.core.Bot;
import net.itsthesky.disky5.core.BotManager;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.KeyValueEntryData;
import org.skriptlang.skript.lang.entry.util.VariableStringEntryData;
import org.skriptlang.skript.lang.structure.Structure;

@Name("Define Bot")
@Description({
    "Declares and logs in a Discord bot from a config block — a reload-safe convenience over",
    "'a new discord bot' + 'login'. The token is required; intents/cache/shard/activity/status are",
    "optional. Lifecycle is handled by the global on ready / on shutdown events."
})
@Examples({
    "define bot named \"my-bot\":",
    "    token: \"YOUR-TOKEN\"",
    "    intents: default intents"
})
@Since("5.0.0-a1")
public final class StructBot extends Structure {

    public static void register() {
        ManualSyntax.structure(
            StructBot.class,
            EntryValidator.builder()
                .addEntryData(new VariableStringEntryData("token", null, false))
                .addEntryData(stringEntry("intents", "default intents"))
                .addEntryData(stringEntry("member cache policy", "default"))
                .addEntryData(stringEntry("cache flags", "none"))
                .addEntryData(stringEntry("compression", "zlib"))
                .addEntryData(stringEntry("shard count", "-1"))
                .addEntryData(stringEntry("activity", null))
                .addEntryData(stringEntry("online status", null))
                .unexpectedEntryMessage(key ->
                    "Unexpected entry '" + key + "'. Lifecycle (on ready / on shutdown) is now a global event, "
                        + "not a section of 'define bot'.")
                .build(),
            "define [the] [new] bot (with name|named) %string%"
        );
    }

    private static KeyValueEntryData<String> stringEntry(String key, @Nullable String def) {
        return new KeyValueEntryData<>(key, def, true) {
            @Override
            protected String getValue(@NotNull String value) {
                return value;
            }
        };
    }

    private String name;
    private EntryContainer container;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern,
                        SkriptParser.@NotNull ParseResult parseResult, @Nullable EntryContainer entryContainer) {
        this.name = ((Literal<String>) args[0]).getSingle();
        this.container = entryContainer;
        return true;
    }

    @Override
    public boolean load() {
        VariableString token = container.getOptional("token", VariableString.class, true);
        if (token == null) {
            Skript.error("The bot '" + name + "' has no token; it is required.");
            return false;
        }

        try {
            BotManager manager = DiSky5.getBots();
            Bot existing = manager.byName(name);
            if (existing != null)
                existing.shutdown();

            Bot bot = new Bot();
            bot.setToken(token.getSingle(ContextlessEvent.get()));
            String intents = single("intents");
            if (intents != null) bot.setIntents(intents);
            String flags = single("cache flags");
            if (flags != null) bot.setCacheFlags(flags);
            String policy = single("member cache policy");
            if (policy != null) bot.setMemberCachePolicy(policy);
            String compression = single("compression");
            if (compression != null) bot.setCompression(compression);
            bot.setShardCount(parseInt(single("shard count"), -1));
            String activity = single("activity");
            if (activity != null) bot.setActivity(activity);
            String status = single("online status");
            OnlineStatus parsed = parseStatus(status);
            if (parsed != null) bot.setOnlineStatus(parsed);

            bot.login(name);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(new RuntimeException(
                "Failed to define the bot '" + name + "': " + t.getMessage(), t));
        }
        return true;
    }

    private @Nullable String single(String key) {
        return container.getOptional(key, String.class, true);
    }

    private static int parseInt(@Nullable String s, int def) {
        if (s == null) return def;
        try { return Integer.parseInt(s.trim()); } catch (NumberFormatException ex) { return def; }
    }

    private static @Nullable OnlineStatus parseStatus(@Nullable String s) {
        if (s == null) return null;
        try { return OnlineStatus.fromKey(s.trim().toLowerCase()); } catch (IllegalArgumentException ex) { return null; }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "define bot named " + name;
    }
}
