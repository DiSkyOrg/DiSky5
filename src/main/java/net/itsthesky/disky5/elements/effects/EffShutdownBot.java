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

@Name("Shutdown Bot")
@Description("Disconnects the given bot(s) from Discord and unloads them. The bot can be logged in again afterwards.")
@Examples({
    "shutdown the bot named \"my-bot\"",
    "stop event-bot"
})
@Since("5.0.0-a1")
public final class EffShutdownBot extends Effect {

    private Expression<Bot> bots;

    public static void register() {
        ManualSyntax.effect(EffShutdownBot.class,
            "(shut[ ]down|stop|disconnect) [the] [discord] bot[s] %bots%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        bots = (Expression<Bot>) expressions[0];
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        for (Bot bot : bots.getArray(event)) {
            try {
                bot.shutdown();
            } catch (Throwable t) {
                DiSkyRuntimeHandler.error(t);
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "shutdown " + bots.toString(event, debug);
    }
}
