package net.itsthesky.disky5.elements.effects.commands;

import ch.njol.skript.config.Node;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.itsthesky.disky5.api.async.AsyncEffect;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandHolder;
import net.itsthesky.disky5.api.commands.application.CommandScope;
import net.itsthesky.disky5.core.Bot;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

abstract class CommandPushEffect extends AsyncEffect {

    protected @Nullable Expression<Bot> bot;
    protected @Nullable Expression<Guild> guild;
    protected Node node;

    @SuppressWarnings("unchecked")
    protected boolean initTarget(Expression<?>[] expressions, int botSlot, int guildSlot) {
        bot = (Expression<Bot>) expressions[botSlot];
        guild = (Expression<Guild>) expressions[guildSlot];
        node = getParser().getNode();
        getParser().setHasDelayBefore(Kleenean.TRUE);
        return true;
    }

    protected @Nullable CommandScope scope(Event event) {
        try {
            return CommandScope.of(bot == null ? null : bot.getSingle(event), guild == null ? null : guild.getSingle(event));
        } catch (RuntimeException ex) {
            DiSkyRuntimeHandler.error(ex, node, event);
            return null;
        }
    }

    protected String targetToString(@Nullable Event event, boolean debug) {
        if (guild != null) return " in " + guild.toString(event, debug);
        return " globally in " + (bot == null ? "?" : bot.toString(event, debug));
    }

    protected static void rememberIds(List<? extends ApplicationCommandHolder> holders, List<Command> registered) {
        for (Command command : registered)
            for (ApplicationCommandHolder holder : holders)
                if (holder.type() == command.getType() && holder.name().equals(command.getName()))
                    holder.id(command.getIdLong());
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return initTarget(expressions, expressions.length - 2, expressions.length - 1);
    }
}
