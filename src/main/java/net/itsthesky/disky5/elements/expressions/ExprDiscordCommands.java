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
import net.itsthesky.disky5.api.commands.CommandDefinition;
import net.itsthesky.disky5.api.commands.CommandRegistry;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("All Discord Commands")
@Description("Every 'discord command' currently loaded, from every script.")
@Examples({"loop all discord commands:", "\tbroadcast \"%name of loop-value%: %usage of loop-value%\""})
@Since("5.0.0-a1")
public final class ExprDiscordCommands extends SimpleExpression<CommandDefinition> {

    public static void register() {
        ManualSyntax.expression(ExprDiscordCommands.class, CommandDefinition.class, true, ExpressionType.SIMPLE,
            "[all [of]] [the] [registered] discord commands");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    protected CommandDefinition @Nullable [] get(@NotNull Event event) {
        return CommandRegistry.INSTANCE.all().toArray(new CommandDefinition[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends CommandDefinition> getReturnType() {
        return CommandDefinition.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "all discord commands";
    }
}
