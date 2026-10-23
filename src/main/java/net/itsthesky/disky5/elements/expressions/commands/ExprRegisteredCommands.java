package net.itsthesky.disky5.elements.expressions.commands;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.itsthesky.disky5.api.async.DiSkyAsync;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandHolder;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandHolders;
import net.itsthesky.disky5.api.commands.application.CommandScope;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Registered Discord Commands")
@Description({
    "The application commands currently registered on Discord: globally for a bot, or for one guild. "
        + "They come back as regular command objects (with their 'command id'), so they can be edited and pushed again.",
    "This asks Discord, so it needs 'await'."
})
@Examples({
    "await set {_cmds::*} to registered commands of event-guild",
    "loop {_cmds::*}:",
    "\tsend \"%name of loop-value% (%command id of loop-value%)\" to console"
})
@Since("5.0.0-a2")
public final class ExprRegisteredCommands extends SimpleExpression<ApplicationCommandHolder> {

    private Expression<?> target;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprRegisteredCommands.class, ApplicationCommandHolder.class, true, ExpressionType.PROPERTY,
            "[all [of]] [the] registered [discord] [application] commands (of|from|in) [the] %bot/guild%");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!DiSkyAsync.isParsingInsideAwait()) {
            Skript.error("'registered commands of' asks Discord and requires 'await' to be read");
            return false;
        }
        target = expressions[0];
        node = getParser().getNode();
        return true;
    }

    @Override
    protected ApplicationCommandHolder @Nullable [] get(@NotNull Event event) {
        Object raw = target.getSingle(event);
        if (raw == null) return new ApplicationCommandHolder[0];
        try {
            List<ApplicationCommandHolder> holders = new ArrayList<>();
            for (Command command : CommandScope.of(raw).retrieve().complete()) {
                ApplicationCommandHolder holder = ApplicationCommandHolders.from(command);
                if (holder != null) holders.add(holder);
            }
            return holders.toArray(new ApplicationCommandHolder[0]);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
            return new ApplicationCommandHolder[0];
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends ApplicationCommandHolder> getReturnType() {
        return ApplicationCommandHolder.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the registered discord commands of " + target.toString(event, debug);
    }
}
