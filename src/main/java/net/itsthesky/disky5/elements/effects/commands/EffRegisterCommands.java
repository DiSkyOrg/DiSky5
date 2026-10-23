package net.itsthesky.disky5.elements.effects.commands;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandHolder;
import net.itsthesky.disky5.api.commands.application.CommandScope;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Register Discord Command")
@Description({
    "Adds or replaces (by name) one or more application commands of a bot (globally) or of a guild, "
        + "leaving the other registered commands alone. Use 'update the discord commands' to replace the whole list.",
    "Once done, 'command id of' each command is known."
})
@Examples({
    "register the discord command {_cmd} in event-guild",
    "register the discord commands {_a} and {_b} globally in event-bot"
})
@Since("5.0.0-a2")
public final class EffRegisterCommands extends CommandPushEffect {

    private Expression<ApplicationCommandHolder> commands;

    public static void register() {
        ManualSyntax.effect(EffRegisterCommands.class,
            "register [the] [discord] [application] command[s] %applicationcommands% (globally (in|for|of|on) [the] [bot] %-bot%|in [the] [guild] %-guild%)");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        commands = (Expression<ApplicationCommandHolder>) expressions[0];
        return initTarget(expressions, 1, 2);
    }

    @Override
    protected void execute(@NotNull Event event) {
        CommandScope scope = scope(event);
        if (scope == null) return;
        for (ApplicationCommandHolder holder : commands.getArray(event)) {
            try {
                Command registered = scope.upsert(holder.build()).complete();
                holder.id(registered.getIdLong());
            } catch (Throwable t) {
                DiSkyRuntimeHandler.error(t, node, event);
                return;
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "register the discord commands " + commands.toString(event, debug) + targetToString(event, debug);
    }
}
