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
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Recommended Shard Count")
@Description("The value (-1) that tells DiSky to use Discord's recommended shard count.")
@Examples("set shard count of {_bot} to recommended shard count")
@Since("5.0.0-a1")
public final class ExprRecommendedShardCount extends SimpleExpression<Long> {

    public static void register() {
        ManualSyntax.expression(ExprRecommendedShardCount.class, Long.class, false, ExpressionType.SIMPLE,
            "[the] recommended shard count");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    protected Long @NotNull [] get(@NotNull Event event) {
        return new Long[]{ -1L };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Long> getReturnType() {
        return Long.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "recommended shard count";
    }
}
