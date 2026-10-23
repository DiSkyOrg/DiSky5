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
import net.itsthesky.disky5.core.Bot;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("New Discord Bot")
@Description("A new, unconnected Discord bot to configure and then log in.")
@Examples({
    "set {_bot} to a new discord bot",
    "set token of {_bot} to \"...\"",
    "set shard count of {_bot} to 4",
    "login {_bot} as \"my-bot\""
})
@Since("5.0.0-a1")
public final class ExprNewBot extends SimpleExpression<Bot> {

    public static void register() {
        ManualSyntax.expression(ExprNewBot.class, Bot.class, false, ExpressionType.SIMPLE, "[a] new discord bot");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    protected Bot @NotNull [] get(@NotNull Event event) {
        return new Bot[]{ new Bot() };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Bot> getReturnType() {
        return Bot.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "a new discord bot";
    }
}
