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

import java.util.List;

@Name("Unregister Discord Commands")
@Description({
    "Removes application commands from a bot (globally) or from a guild: by command object, by name, "
        + "by id, or all of them.",
    "A command object whose id is known is deleted directly; otherwise the registered commands are "
        + "looked up and every one with that name (and type, for a command object) is removed."
})
@Examples({
    "unregister the discord command \"ping\" from event-guild",
    "unregister the discord commands {_cmd} globally from event-bot",
    "unregister all the discord commands from event-guild"
})
@Since("5.0.0-a2")
public final class EffUnregisterCommands extends CommandPushEffect {

    private @Nullable Expression<?> commands;

    public static void register() {
        ManualSyntax.effect(EffUnregisterCommands.class,
            "unregister [the] [discord] [application] command[s] %strings/applicationcommands% (globally (from|of|in) [the] [bot] %-bot%|(from|of|in) [the] [guild] %-guild%)",
            "unregister all [the] [discord] [application] commands (globally (from|of|in) [the] [bot] %-bot%|(from|of|in) [the] [guild] %-guild%)");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            commands = expressions[0];
            return initTarget(expressions, 1, 2);
        }
        return initTarget(expressions, 0, 1);
    }

    @Override
    protected void execute(@NotNull Event event) {
        CommandScope scope = scope(event);
        if (scope == null) return;
        try {
            if (commands == null) {
                scope.updateCommands().complete();
                return;
            }
            List<Command> registered = null;
            for (Object target : commands.getArray(event)) {
                if (target instanceof ApplicationCommandHolder holder && holder.id() != null) {
                    scope.delete(holder.id()).complete();
                    holder.id(null);
                    continue;
                }
                if (target instanceof String text && text.matches("\\d{15,21}")) {
                    scope.delete(Long.parseLong(text)).complete();
                    continue;
                }
                if (registered == null) registered = scope.retrieve().complete();
                for (Command command : registered)
                    if (matches(target, command))
                        scope.delete(command.getIdLong()).complete();
            }
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
        }
    }

    private static boolean matches(Object target, Command command) {
        if (target instanceof ApplicationCommandHolder holder)
            return holder.type() == command.getType() && holder.name().equalsIgnoreCase(command.getName());
        return target instanceof String name && name.equalsIgnoreCase(command.getName());
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "unregister the discord commands " + (commands == null ? "(all)" : commands.toString(event, debug))
            + targetToString(event, debug);
    }
}
