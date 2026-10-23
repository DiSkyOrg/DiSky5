package net.itsthesky.disky5.elements.expressions;

import ch.njol.skript.Skript;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.core.Bot;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Bots")
@Description("All loaded bots, or the bot(s) with a given name (as defined by a `define bot` structure).")
@Examples({
    "set {_bot} to the bot named \"my bot\"",
    "send \"%number of bots% bot(s) loaded\""
})
@Since("5.0.0-a1")
public final class ExprBots extends SimpleExpression<Bot> {

    private boolean all;
    private @Nullable Expression<String> names;

    public static void register() {
        ManualSyntax.expression(ExprBots.class, Bot.class, true, ExpressionType.SIMPLE,
            "[all] [the] bots",
            "[the] bot[s] named %string%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        all = matchedPattern == 0;
        if (!all) names = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    protected Bot @NotNull [] get(@NotNull Event event) {
        if (all)
            return DiSky5.getBots().all().toArray(new Bot[0]);
        List<Bot> result = new ArrayList<>();
        for (String name : names.getArray(event)) {
            Bot bot = DiSky5.getBots().byName(name);
            if (bot != null)
                result.add(bot);
        }
        return result.toArray(new Bot[0]);
    }

    @Override
    public boolean isSingle() {
        return !all && names.isSingle();
    }

    @Override
    public @NotNull Class<? extends Bot> getReturnType() {
        return Bot.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return all ? "all bots" : "the bot(s) named " + names.toString(event, debug);
    }
}
