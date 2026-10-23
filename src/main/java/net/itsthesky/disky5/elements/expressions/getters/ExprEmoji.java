package net.itsthesky.disky5.elements.expressions.getters;

import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.api.async.DiSkyAsync;
import net.itsthesky.disky5.api.emojis.EmojiShortcodes;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.Bot;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("Emoji")
@Description({
    "An emoji from its name, its unicode character or the id of a custom one (an emote). Several texts give several emojis.",
    "Each text is resolved in this order: a custom emoji id or its '<:name:id>' / '<a:name:id>' form, a unicode emoji "
        + "('😂'), then a name. With a guild, a name first looks for a custom emoji of that guild (case-insensitive), "
        + "then for a Discord shortcode ('joy' or ':joy:', skin tones as 'thumbsup_tone2'). Without a guild, the shortcode "
        + "comes first, then the custom emojis every loaded bot can see.",
    "It reads the cache. With 'await', an id or a name missing from the cache is also looked up on Discord: in the guild "
        + "when one is given, else among the bot's application emojis.",
    "A text that matches nothing is a readable error and is skipped. Name the guild when two guilds may hold "
        + "a custom emoji with the same name."
})
@Examples({
    "react to event-message with emoji \"joy\"",
    "set {_reactions::*} to reactions (\":sparkles:\" and \"thumbsup_tone2\")",
    "react to event-message with {_reactions::*}",
    "set {_emote} to emote \"disky\" in event-guild",
    "await set {_app} to emoji \"123456789012345678\""
})
@Since("5.0.0-b1")
public final class ExprEmoji extends SimpleExpression<Emoji> {

    private static final Pattern FORMATTED = Pattern.compile("^<a?:([A-Za-z0-9_~]+):(\\d+)>$");
    private static final Pattern SNOWFLAKE = Pattern.compile("^\\d{15,}$");

    private Expression<String> inputs;
    private @Nullable Expression<Guild> guild;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprEmoji.class, Emoji.class, true, ExpressionType.COMBINED,
            "[the] (emoji|emote|reaction)[s] %strings% [(in|from) [the] [guild] %-guild%]");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        inputs = (Expression<String>) expressions[0];
        guild = (Expression<Guild>) expressions[1];
        node = getParser().getNode();
        return true;
    }

    @Override
    protected Emoji @Nullable [] get(@NotNull Event event) {
        String[] texts = inputs.getArray(event);
        Guild scope = guild == null ? null : guild.getSingle(event);
        if (guild != null && scope == null) return new Emoji[0];
        boolean awaited = DiSkyAsync.isAwaited(event);
        List<Emoji> out = new ArrayList<>(texts.length);
        for (String text : texts) {
            try {
                out.add(resolve(text.trim(), scope, awaited));
            } catch (Throwable t) {
                DiSkyRuntimeHandler.error(t, node, event);
            }
        }
        return out.toArray(new Emoji[0]);
    }

    private static Emoji resolve(String text, @Nullable Guild guild, boolean awaited) {
        if (text.isEmpty())
            throw new IllegalArgumentException("an empty text is not an emoji");

        Matcher formatted = FORMATTED.matcher(text);
        if (formatted.matches()) {
            Emoji found = byId(formatted.group(2), guild, awaited);
            return found != null ? found : Emoji.fromFormatted(text);
        }
        if (SNOWFLAKE.matcher(text).matches()) {
            Emoji found = byId(text, guild, awaited);
            if (found == null)
                throw new IllegalArgumentException("no custom emoji with id '" + text + "' "
                    + (guild == null ? "is known to any loaded bot" : "in the guild '" + guild.getName() + "'")
                    + (awaited ? "" : " (cache only; use 'await' to ask Discord)"));
            return found;
        }

        String known = EmojiShortcodes.knownUnicode(text);
        if (known != null)
            return Emoji.fromUnicode(known);

        String name = text.length() > 2 && text.startsWith(":") && text.endsWith(":")
            ? text.substring(1, text.length() - 1) : text;

        if (guild != null) {
            CustomEmoji custom = inGuild(guild, name, awaited);
            if (custom != null) return custom;
        }

        String unicode = EmojiShortcodes.unicodeOf(name);
        if (unicode != null)
            return Emoji.fromUnicode(unicode);

        if (guild == null) {
            CustomEmoji custom = anywhere(name, awaited);
            if (custom != null) return custom;
        }

        // Not in the shortcode table but not a name either: most likely an emoji newer than the table.
        if (text.codePoints().anyMatch(c -> c > 0x7F) && text.codePoints().noneMatch(Character::isWhitespace))
            return Emoji.fromUnicode(text);

        throw new IllegalArgumentException("no emoji matches '" + text + "': it is neither a unicode emoji, a Discord "
            + "shortcode nor a custom emoji " + (guild == null ? "known to any loaded bot" : "of the guild '" + guild.getName() + "'"));
    }

    private static @Nullable CustomEmoji byId(String id, @Nullable Guild guild, boolean awaited) {
        if (guild != null) {
            CustomEmoji cached = guild.getEmojiById(id);
            return cached != null || !awaited ? cached : guild.retrieveEmojiById(id).complete();
        }
        for (Bot bot : DiSky5.getBots().all()) {
            ShardManager manager = bot.getShardManager();
            CustomEmoji cached = manager == null ? null : manager.getEmojiById(id);
            if (cached != null) return cached;
        }
        if (!awaited) return null;
        for (Bot bot : DiSky5.getBots().all()) {
            JDA jda = anyShard(bot);
            if (jda == null) continue;
            try {
                return jda.retrieveApplicationEmojiById(id).complete();
            } catch (RuntimeException ignored) {
                // Each bot owns its own application emojis: an unknown id only means "not this bot".
            }
        }
        return null;
    }

    private static @Nullable CustomEmoji inGuild(Guild guild, String name, boolean awaited) {
        List<? extends CustomEmoji> cached = guild.getEmojisByName(name, true);
        if (!cached.isEmpty()) return cached.get(0);
        if (!awaited) return null;
        return guild.retrieveEmojis().complete().stream()
            .filter(e -> e.getName().equalsIgnoreCase(name))
            .findFirst().orElse(null);
    }

    private static @Nullable CustomEmoji anywhere(String name, boolean awaited) {
        for (Bot bot : DiSky5.getBots().all()) {
            ShardManager manager = bot.getShardManager();
            if (manager == null) continue;
            List<? extends CustomEmoji> cached = manager.getEmojisByName(name, true);
            if (!cached.isEmpty()) return cached.get(0);
        }
        if (!awaited) return null;
        for (Bot bot : DiSky5.getBots().all()) {
            JDA jda = anyShard(bot);
            if (jda == null) continue;
            CustomEmoji found = jda.retrieveApplicationEmojis().complete().stream()
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
            if (found != null) return found;
        }
        return null;
    }

    private static @Nullable JDA anyShard(Bot bot) {
        ShardManager manager = bot.getShardManager();
        if (manager == null) return null;
        return manager.getShardCache().stream().findFirst().orElse(null);
    }

    @Override
    public boolean isSingle() {
        return inputs.isSingle();
    }

    @Override
    public @NotNull Class<? extends Emoji> getReturnType() {
        return Emoji.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "emoji " + inputs.toString(event, debug) + (guild == null ? "" : " in " + guild.toString(event, debug));
    }
}
