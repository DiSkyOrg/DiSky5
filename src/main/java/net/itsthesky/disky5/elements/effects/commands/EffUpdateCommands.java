package net.itsthesky.disky5.elements.effects.commands;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandHolder;
import net.itsthesky.disky5.api.commands.application.CommandScope;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Update Discord Commands")
@Description({
    "Replaces every application command of a bot (globally) or of a guild with the given ones: what is not "
        + "in the list is removed from Discord. This is the one call to make from 'on ready' / 'on guild ready'.",
    "Global commands take up to an hour to show up for users; guild commands are immediate. Discord limits "
        + "how often a guild's command list can be replaced (200 times a day).",
    "Once done, 'command id of' each command is known."
})
@Examples({
    "on guild ready:",
    "\tupdate the discord commands (all structured application commands) in event-guild",
    "",
    "on ready:",
    "\tset {_ping} to a new slash command named \"ping\" with description \"Pong!\"",
    "\tupdate the discord commands {_ping} globally in event-bot"
})
@Since("5.0.0-a2")
public final class EffUpdateCommands extends CommandPushEffect {

    private Expression<ApplicationCommandHolder> commands;

    public static void register() {
        ManualSyntax.effect(EffUpdateCommands.class,
            "update [the] [discord] [application] command[s] [to] %applicationcommands% (globally (in|for|of|on) [the] [bot] %-bot%|in [the] [guild] %-guild%)");
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
        List<ApplicationCommandHolder> holders = new ArrayList<>(Arrays.asList(commands.getArray(event)));
        try {
            List<CommandData> data = new ArrayList<>();
            for (ApplicationCommandHolder holder : holders)
                data.add(holder.build());
            List<Command> registered = scope.updateCommands().addCommands(data).complete();
            rememberIds(holders, registered);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "update the discord commands " + commands.toString(event, debug) + targetToString(event, debug);
    }
}
