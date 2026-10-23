package net.itsthesky.disky5.elements.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.components.Component;
import net.dv8tion.jda.api.components.attribute.ICustomId;
import net.dv8tion.jda.api.components.replacer.ComponentReplacer;
import net.dv8tion.jda.api.components.tree.MessageComponentTree;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.callbacks.IMessageEditCallback;
import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
import net.itsthesky.disky5.api.async.AsyncEffect;
import net.itsthesky.disky5.api.components.ComponentBuilder;
import net.itsthesky.disky5.api.components.MessageComponents;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Replace Component")
@Description({
    "Swaps one component of a sent message, found by its text id (a button, a dropdown) or its unique id "
        + "(any component, including text displays and sections inside a container), for a new one. "
        + "Every other component is kept as it is.",
    "Inside a button or dropdown click on that same message, the edit is the interaction's answer."
})
@Examples({
    "replace the component with id \"confirm\" of event-message with {_disabledButton}",
    "replace the component with unique id 42 in {_msg} with a new text display \"Updated!\""
})
@Since("5.0.0-a1")
public final class EffReplaceComponent extends AsyncEffect {

    private Expression<?> id;
    private Expression<Message> message;
    private Expression<ComponentBuilder<?>> replacement;
    private boolean unique;
    private ch.njol.skript.config.Node node;

    public static void register() {
        ManualSyntax.effect(EffReplaceComponent.class,
            "replace [the] component with [the] [:unique] id %string/number% (of|in|from) [the] [message] %message% (with|by) %component%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        id = expressions[0];
        message = (Expression<Message>) expressions[1];
        replacement = (Expression<ComponentBuilder<?>>) expressions[2];
        unique = parseResult.hasTag("unique");
        node = getParser().getNode();
        getParser().setHasDelayBefore(Kleenean.TRUE);
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        try {
            Object rawId = id.getSingle(event);
            Message target = message.getSingle(event);
            ComponentBuilder<?> builder = replacement.getSingle(event);
            if (rawId == null || target == null || builder == null) {
                DiSkyRuntimeHandler.error(new IllegalArgumentException("the id, the message or the replacement is not set"), node, event);
                return;
            }
            Component replacementComponent = builder.buildWithId();
            ComponentReplacer replacer = unique || rawId instanceof Number
                ? ComponentReplacer.byUniqueId(((Number) (rawId instanceof Number n ? n : Integer.parseInt(rawId.toString()))).intValue(), replacementComponent)
                : ComponentReplacer.of(ICustomId.class, c -> rawId.toString().equals(c.getCustomId()), c -> replacementComponent);
            MessageComponentTree tree = target.getComponentTree().replace(replacer);

            Object jda = event instanceof DiSkyBukkitEvent bukkit ? bukkit.getJdaEvent() : null;
            if (jda instanceof ComponentInteraction interaction && interaction.getMessageIdLong() == target.getIdLong()
                    && !interaction.isAcknowledged()) {
                MessageComponents.apply(((IMessageEditCallback) interaction).editComponents(tree), tree.getComponents()).complete();
                return;
            }
            MessageComponents.apply(target.editMessageComponents(tree), tree.getComponents()).complete();
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "replace component with " + (unique ? "unique " : "") + "id " + id.toString(event, debug)
            + " of " + message.toString(event, debug) + " with " + replacement.toString(event, debug);
    }
}
