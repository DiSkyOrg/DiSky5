package net.itsthesky.disky5.elements.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.callbacks.IDeferrableCallback;
import net.dv8tion.jda.api.interactions.callbacks.IMessageEditCallback;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Defer Interaction")
@Description({
    "Acknowledges the current interaction without answering it yet, which buys up to 15 minutes to reply "
        + "instead of the 3 seconds Discord gives by default. A later 'reply with' goes through the "
        + "deferred answer.",
    "'hidden' makes the eventual reply ephemeral. 'and edit' (on a button or dropdown click) acknowledges "
        + "without any 'thinking...' state, for when the script will only edit the message."
})
@Examples({
    "on slash command:",
    "\tdefer the interaction hidden",
    "\twait 5 seconds",
    "\treply with \"Done!\"",
    "",
    "on button clicked:",
    "\tdefer the interaction and edit",
    "\tedit interaction components with a new row"
})
@Since("5.0.0-a1")
public final class EffDeferInteraction extends InteractionEffect {

    private boolean hidden;
    private boolean edit;

    public static void register() {
        ManualSyntax.effect(EffDeferInteraction.class,
            "(defer|acknowledge) [the] interaction [:hidden] [edit:and (edit|update)]");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        hidden = parseResult.hasTag("hidden");
        edit = parseResult.hasTag("edit");
        return initInteraction("defer the interaction");
    }

    @Override
    protected void answer(Event event) {
        IDeferrableCallback callback = interaction(event, IDeferrableCallback.class, "be deferred");
        if (callback == null)
            return;
        if (callback.isAcknowledged())
            throw new IllegalStateException("this interaction was already answered (replied or deferred)");
        if (edit) {
            if (!(callback instanceof IMessageEditCallback editable))
                throw new IllegalStateException("only a button or dropdown click can be deferred 'and edit'");
            editable.deferEdit().complete();
        } else {
            if (!(callback instanceof IReplyCallback replyable))
                throw new IllegalStateException("this interaction cannot be deferred for a reply");
            replyable.deferReply(hidden).complete();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "defer the interaction" + (hidden ? " hidden" : "") + (edit ? " and edit" : "");
    }
}
