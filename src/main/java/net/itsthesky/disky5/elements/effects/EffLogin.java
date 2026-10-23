package net.itsthesky.disky5.elements.effects;

import ch.njol.skript.Skript;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.itsthesky.disky5.core.Bot;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Login Bot")
@Description("Connects a configured bot to Discord under the given name. Validation/phase errors are reported cleanly.")
@Examples({
    "set {_bot} to a new discord bot",
    "set token of {_bot} to \"...\"",
    "login {_bot} as \"my-bot\""
})
@Since("5.0.0-a1")
public final class EffLogin extends Effect {

    private Expression<Bot> bot;
    private Expression<String> name;

    public static void register() {
        ManualSyntax.effect(EffLogin.class, "login %bot% as %string%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        bot = (Expression<Bot>) expressions[0];
        name = (Expression<String>) expressions[1];
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        Bot target = bot.getSingle(event);
        String botName = name.getSingle(event);
        if (target == null || botName == null)
            return;
        try {
            target.login(botName);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "login " + bot.toString(event, debug) + " as " + name.toString(event, debug);
    }
}
