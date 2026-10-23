package net.itsthesky.disky5.elements.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.components.MessageTopLevelComponent;
import net.dv8tion.jda.api.interactions.callbacks.IMessageEditCallback;
import net.itsthesky.disky5.api.components.ComponentBuilder;
import net.itsthesky.disky5.api.components.MessageComponents;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("Edit Interaction Components")
@Description({
    "Replaces every component of the message whose button or dropdown was just clicked, as the answer "
        + "to that interaction (or through it, if it was deferred 'and edit').",
    "Bare buttons are grouped into rows like on a message builder. Pass nothing to strip the components."
})
@Examples({
    "on button clicked:",
    "\tset disabled of event-button to true",
    "\tedit interaction components with event-button",
    "",
    "on dropdown clicked:",
    "\tedit interaction components with {_row1} and {_row2}"
})
@Since("5.0.0-a1")
public final class EffEditInteractionComponents extends InteractionEffect {

    private @Nullable Expression<ComponentBuilder<?>> components;

    public static void register() {
        ManualSyntax.effect(EffEditInteractionComponents.class,
            "edit [the] interaction components (with|to [show]) [the] [components] %-components%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        components = (Expression<ComponentBuilder<?>>) expressions[0];
        return initInteraction("edit interaction components");
    }

    @Override
    protected void answer(Event event) {
        IMessageEditCallback callback = interaction(event, IMessageEditCallback.class, "edit its message's components");
        if (callback == null)
            return;
        ComponentBuilder<?>[] builders = components == null ? new ComponentBuilder<?>[0] : components.getArray(event);
        List<MessageTopLevelComponent> grouped = MessageComponents.group(builders);
        if (callback.isAcknowledged())
            MessageComponents.apply(callback.getHook().editOriginalComponents(grouped), grouped).complete();
        else
            MessageComponents.apply(callback.editComponents(grouped), grouped).complete();
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "edit interaction components with " + (components == null ? "nothing" : components.toString(event, debug));
    }
}
