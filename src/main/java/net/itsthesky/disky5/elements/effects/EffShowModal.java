package net.itsthesky.disky5.elements.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.callbacks.IModalCallback;
import net.itsthesky.disky5.api.components.ModalBuilder;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Show Modal")
@Description({
    "Opens a modal for the user who triggered the current interaction: a button click, a dropdown click "
        + "or a slash command. It is the interaction's answer, so nothing else may have replied or deferred "
        + "it before, and a modal cannot answer another modal.",
    "What the user submits comes back in 'on modal received'."
})
@Examples({
    "on button clicked:",
    "\tset {_m} to a new modal with id \"feedback\" titled \"Your feedback\"",
    "\tadd a new paragraph text input with id \"body\" labeled \"Message\" to components of {_m}",
    "\tshow {_m}"
})
@Since("5.0.0-a1")
public final class EffShowModal extends InteractionEffect {

    private Expression<ModalBuilder> modal;

    public static void register() {
        ManualSyntax.effect(EffShowModal.class, "show [the] [modal] %modal% [to [the] event-user]");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        modal = (Expression<ModalBuilder>) expressions[0];
        return initInteraction("show modal");
    }

    @Override
    protected void answer(Event event) {
        ModalBuilder builder = modal.getSingle(event);
        if (builder == null) {
            DiSkyRuntimeHandler.error(new IllegalArgumentException("the modal to show is not set"), node, event);
            return;
        }
        IModalCallback callback = interaction(event, IModalCallback.class, "show a modal");
        if (callback == null)
            return;
        if (callback.isAcknowledged())
            throw new IllegalStateException("this interaction was already answered (replied or deferred); a modal must be its first answer");
        callback.replyModal(builder.build()).complete();
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "show modal " + modal.toString(event, debug);
    }
}
