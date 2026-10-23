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
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import net.itsthesky.disky5.core.DiscordError;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Last Discord Error")
@Description("The most recent Discord error that occurred (anywhere), as a discorderror. None if no error has occurred yet.")
@Examples({
    "discord command ban:",
    "    trigger:",
    "        ban member event-member",
    "        if the last discord error is set:",
    "            reply with \"Failed: %error meaning of the last discord error%\""
})
@Since("5.0.0-a1")
public final class ExprLastError extends SimpleExpression<DiscordError> {

    public static void register() {
        ManualSyntax.expression(ExprLastError.class, DiscordError.class, false, ExpressionType.SIMPLE,
            // `discord` is mandatory: the bare "the last error" is Skript's own ExprParseError.
            "[the] last discord error");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    protected DiscordError @Nullable [] get(@NotNull Event event) {
        DiscordError last = DiSkyRuntimeHandler.getLastError();
        return last == null ? new DiscordError[0] : new DiscordError[]{ last };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends DiscordError> getReturnType() {
        return DiscordError.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the last discord error";
    }
}
