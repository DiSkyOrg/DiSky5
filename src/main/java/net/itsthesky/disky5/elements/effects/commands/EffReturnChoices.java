package net.itsthesky.disky5.elements.effects.commands;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.TriggerSection;
import ch.njol.skript.sections.SecLoop;
import ch.njol.skript.sections.SecWhile;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.itsthesky.disky5.api.commands.application.SlashChoiceHolder;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_SlashCommandCompletionEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Return Slash Choices")
@Description({
    "Answers an autocomplete request with up to 25 suggestions and stops the trigger. Give choices, "
        + "or plain texts / numbers (used as both the label and the value).",
    "Only in 'on slash completion' and in an 'on completion request:' section of a slash command structure."
})
@Examples({
    "on slash completion:",
    "\tif current argument is \"color\":",
    "\t\treturn choices \"red\", \"green\" and \"blue\""
})
@Since("5.0.0-a2")
public final class EffReturnChoices extends Effect {

    private Expression<?> choices;
    private Node node;

    public static void register() {
        ManualSyntax.effect(EffReturnChoices.class,
            "return [the] [slash] (choices|completions) %slashchoices/strings/numbers%");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(GeneratedBukkitEvt_SlashCommandCompletionEvent.class)) {
            Skript.error("'return choices' can only be used in a slash completion event");
            return false;
        }
        choices = expressions[0];
        node = getParser().getNode();
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        if (event instanceof DiSkyBukkitEvent bukkit && bukkit.getJdaEvent() instanceof CommandAutoCompleteInteractionEvent completion) {
            try {
                OptionType type = completion.getFocusedOption().getType();
                List<Command.Choice> built = new ArrayList<>();
                for (Object raw : choices.getArray(event)) {
                    if (built.size() >= OptionData.MAX_CHOICES) break;
                    built.add(toChoice(raw, type));
                }
                completion.replyChoices(built).complete();
            } catch (Throwable t) {
                DiSkyRuntimeHandler.error(t, node, event);
            }
        }
        TriggerSection parent = getParent();
        while (parent != null) {
            if (parent instanceof SecLoop loop) loop.exit(event);
            else if (parent instanceof SecWhile loop) loop.exit(event);
            parent = parent.getParent();
        }
        return null;
    }

    private static Command.Choice toChoice(Object raw, OptionType type) {
        if (raw instanceof SlashChoiceHolder holder) return holder.build(type);
        if (raw instanceof Number number) {
            return type == OptionType.NUMBER ? new Command.Choice(String.valueOf(number.doubleValue()), number.doubleValue())
                : type == OptionType.INTEGER ? new Command.Choice(String.valueOf(number.longValue()), number.longValue())
                : new Command.Choice(String.valueOf(number), String.valueOf(number));
        }
        String text = String.valueOf(raw);
        return switch (type) {
            case INTEGER -> new Command.Choice(text, Long.parseLong(text.trim()));
            case NUMBER -> new Command.Choice(text, Double.parseDouble(text.trim()));
            default -> new Command.Choice(text, text);
        };
    }

    @Override
    protected void execute(@NotNull Event event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "return choices " + choices.toString(event, debug);
    }
}
