package net.itsthesky.disky5.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("New Embed")
@Description("A new, empty Discord embed to configure via the embed properties.")
@Examples({
    """
    set {_embed} to a new discord embed
    set title of {_embed} to "Hello"
    set description of {_embed} to "World"
    set color of {_embed} to red
    loop 5 times:
        add (a new field named "Ping" with value "%random number between loop-value and loop-value + 100%ms" with inline true) to fields of {_embed}
    """
})
@Since("5.0.0-a1")
public final class ExprNewEmbed extends SimpleExpression<EmbedBuilder> {

    public static void register() {
        ManualSyntax.expression(ExprNewEmbed.class, EmbedBuilder.class, false, ExpressionType.SIMPLE,
            "[a] new [discord] [message] embed");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    protected EmbedBuilder @NotNull [] get(@NotNull Event event) {
        return new EmbedBuilder[]{ new EmbedBuilder() };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends EmbedBuilder> getReturnType() {
        return EmbedBuilder.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "a new discord embed";
    }
}
