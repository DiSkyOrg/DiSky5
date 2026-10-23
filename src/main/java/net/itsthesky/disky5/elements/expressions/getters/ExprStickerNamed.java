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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.itsthesky.disky5.api.async.DiSkyAsync;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("Sticker Named")
@Description({
    "The sticker of a guild with the given name, compared case-insensitively. Returns nothing when the guild has no such sticker.",
    "It reads the guild's sticker cache. With 'await', a name missing from the cache is looked up on Discord."
})
@Examples({
    "set {_sticker} to sticker named \"wave\" in event-guild",
    "await set {_sticker} to the sticker with name \"wave\" from event-guild"
})
@Since("5.0.0-b1")
public final class ExprStickerNamed extends SimpleExpression<GuildSticker> {

    private Expression<String> name;
    private Expression<Guild> guild;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprStickerNamed.class, GuildSticker.class, false, ExpressionType.COMBINED,
            "[the] sticker (named|with name) %string% (in|from|of) [the] [guild] %guild%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        name = (Expression<String>) expressions[0];
        guild = (Expression<Guild>) expressions[1];
        node = getParser().getNode();
        return true;
    }

    @Override
    protected GuildSticker @Nullable [] get(@NotNull Event event) {
        String target = name.getSingle(event);
        Guild scope = guild.getSingle(event);
        if (target == null || scope == null) return new GuildSticker[0];
        try {
            List<GuildSticker> cached = scope.getStickersByName(target, true);
            if (!cached.isEmpty()) return new GuildSticker[] {cached.get(0)};
            if (!DiSkyAsync.isAwaited(event)) return new GuildSticker[0];
            return scope.retrieveStickers().complete().stream()
                .filter(sticker -> sticker.getName().equalsIgnoreCase(target))
                .limit(1)
                .toArray(GuildSticker[]::new);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
            return new GuildSticker[0];
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends GuildSticker> getReturnType() {
        return GuildSticker.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the sticker named " + name.toString(event, debug) + " in " + guild.toString(event, debug);
    }
}
