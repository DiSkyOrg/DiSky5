package net.itsthesky.disky5.elements.sections;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.util.Kleenean;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import net.itsthesky.disky5.core.DiscordError;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("Try / Catch")
@Description({
    "Runs a block and catches any DiSky runtime errors. On error, jumps to the catch block with the",
    "caught discorderror(s) stored in the given variable. Add 'and stop' to halt the trigger afterwards."
})
@Examples({
    "try:",
    "    ban member event-member",
    "catch {_errors::*}:",
    "    reply with \"Could not ban: %error meaning of {_errors::1}%\""
})
@Since("5.0.0-a1")
public final class SecTry extends Section {

    public static void register() {
        ManualSyntax.section(SecTry.class,
            "try",
            "catch [the] [error[s]] %~objects% [(stop:[and] stop)]");
    }

    private enum Type { TRY, CATCH }

    private Type type;
    private @Nullable Expression<?> errorVariable;
    private boolean stop;

    private @Nullable SecTry catchSection;
    private Trigger trigger;

    // Linked once, not per walk: appending it on every walk grew the trigger by one item per
    // execution, so a second run of the same try chained a duplicate and walked twice.
    private @Nullable TriggerItem afterBody;
    private boolean catchLinked;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult,
                        @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
        type = matchedPattern == 0 ? Type.TRY : Type.CATCH;
        if (type == Type.CATCH) {
            errorVariable = expressions[0];
            TriggerItem last = triggerItems.isEmpty() ? null : triggerItems.get(triggerItems.size() - 1);
            if (last instanceof SecTry prev && prev.type == Type.TRY) {
                prev.catchSection = this;
                prev.stop = parseResult.hasTag("stop");
                trigger = loadSectionCode(sectionNode);
                return Changer.ChangerUtils.acceptsChange(errorVariable, Changer.ChangeMode.SET, DiscordError.class, DiscordError[].class);
            }
            Skript.error("A 'catch' section must directly follow a 'try' section.");
            return false;
        }
        trigger = loadSectionCode(sectionNode);
        return true;
    }

    private Trigger loadSectionCode(SectionNode sectionNode) {
        List<TriggerItem> items = ScriptLoader.loadItems(sectionNode);
        return new Trigger(getParser().getCurrentScript(), "try/catch", new SimpleEvent(), items);
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        if (type == Type.CATCH)
            return null;

        if (afterBody == null) {
            afterBody = new RunnableItem(this::bodyFinished);
            setLast(trigger, afterBody);
        }

        DiSkyRuntimeHandler.start(event);
        TriggerItem.walk(trigger, event);
        return null;
    }

    private void bodyFinished(Event event) {
        List<DiscordError> errors = DiSkyRuntimeHandler.end(event);
        if (!errors.isEmpty() && catchSection != null) {
            catchSection.errorVariable.change(event, errors.toArray(new DiscordError[0]), Changer.ChangeMode.SET);
            // Same reason as afterBody: link the continuation after the catch body only once.
            if (!catchLinked && !stop && getNext() != null) {
                setLast(catchSection.trigger, getNext().getNext());
                catchLinked = true;
            }
            TriggerItem.walk(catchSection.trigger, event);
        } else {
            TriggerItem next = getNext();
            if (next instanceof SecTry)
                next = next.getNext();
            if (next != null)
                TriggerItem.walk(next, event);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return type == Type.TRY ? "try" : "catch " + (errorVariable == null ? "?" : errorVariable.toString(event, debug));
    }

    private static void setLast(TriggerItem item, @Nullable TriggerItem tail) {
        TriggerItem last = item;
        TriggerItem next = item;
        while (next != null) {
            last = next;
            next = next.getNext();
        }
        last.setNext(tail);
    }

    private static final class RunnableItem extends TriggerItem {
        private final java.util.function.Consumer<Event> action;
        RunnableItem(java.util.function.Consumer<Event> action) { this.action = action; }

        @Override
        protected boolean run(@NotNull Event event) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected @Nullable TriggerItem walk(@NotNull Event event) {
            action.accept(event);
            return null;
        }

        @Override
        public @NotNull String toString(@Nullable Event event, boolean debug) {
            return "<try/catch internal>";
        }
    }
}
