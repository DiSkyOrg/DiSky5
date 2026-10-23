package net.itsthesky.disky5.elements.expressions.commands;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_ApplicationCommandCooldown;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_SlashCommand;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_SlashCommandCompletionEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Slash Option Value")
@Description({
    "The value the user gave to an option of the current slash command, by option name. Typed after what "
        + "Discord sent (a text, an integer, a number, a boolean, a user, a channel, a role, a mentionable, "
        + "an attachment), or after the option type you ask for; 'as member' turns a user into the guild member.",
    "For a command declared by a 'slash command' structure, '{_name}' and 'arg-1' are simpler."
})
@Examples({
    "on slash command:",
    "\tif used slash command is \"ban\":",
    "\t\tban slash option \"target\" as member with reason slash option \"reason\""
})
@Since("5.0.0-a2")
public final class ExprSlashOptionValue extends SimpleExpression<Object> {

    private Expression<String> name;
    private @Nullable OptionType type;
    private boolean asMember;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprSlashOptionValue.class, Object.class, false, ExpressionType.COMBINED,
            "[the] [slash] (option|arg[ument]) [value] [(named|with [the] name)] %string% [as (%-*optiontype%|:member)]");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(GeneratedBukkitEvt_SlashCommand.class)
                && !getParser().isCurrentEvent(GeneratedBukkitEvt_SlashCommandCompletionEvent.class)
                && !getParser().isCurrentEvent(GeneratedBukkitEvt_ApplicationCommandCooldown.class)) {
            Skript.error("A slash option can only be read in a slash command, slash completion or slash command cooldown event");
            return false;
        }
        name = (Expression<String>) expressions[0];
        if (expressions[1] != null)
            type = ((Literal<OptionType>) expressions[1]).getSingle();
        asMember = parseResult.hasTag("member");
        node = getParser().getNode();
        return true;
    }

    @Override
    protected Object @Nullable [] get(@NotNull Event event) {
        String optionName = name.getSingle(event);
        if (optionName == null || !(event instanceof DiSkyBukkitEvent bukkit)
                || !(bukkit.getJdaEvent() instanceof CommandInteractionPayload payload))
            return new Object[0];
        OptionMapping mapping = payload.getOption(optionName);
        if (mapping == null) return new Object[0];
        try {
            Object value = read(mapping);
            return value == null ? new Object[0] : new Object[]{value};
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
            return new Object[0];
        }
    }

    private @Nullable Object read(OptionMapping mapping) {
        if (asMember) return mapping.getAsMember();
        OptionType wanted = type != null ? type : mapping.getType();
        return switch (wanted) {
            case STRING -> mapping.getAsString();
            case INTEGER -> mapping.getAsLong();
            case NUMBER -> mapping.getAsDouble();
            case BOOLEAN -> mapping.getAsBoolean();
            case USER -> mapping.getAsUser();
            case CHANNEL -> mapping.getAsChannel();
            case ROLE -> mapping.getAsRole();
            case MENTIONABLE -> mapping.getAsMentionable();
            case ATTACHMENT -> mapping.getAsAttachment();
            default -> mapping.getAsString();
        };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "slash option " + name.toString(event, debug) + (asMember ? " as member" : type == null ? "" : " as " + type.name().toLowerCase());
    }
}
