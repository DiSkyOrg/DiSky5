package net.itsthesky.disky5.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.itsthesky.disky5.api.async.AsyncEffect;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import net.itsthesky.disky5.core.MessagePayloads;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Reply With")
@Description({
    "Replies to the current event: in a message event the reply is posted in the event's channel, "
        + "in an interaction event (slash command, button, ...) it answers the interaction.",
    "'hidden' makes an interaction reply ephemeral (only the user who triggered it sees it); "
        + "it is ignored outside interactions.",
    "'with reference' links the reply to a message, like Discord's own reply feature.",
    "A message builder can be replied with too: its content, embeds, attachments and poll are sent, "
        + "its own channel is ignored. A component (a container, a row, a button...) is sent on its own."
})
@Examples({
    "reply with \"Hello world!\"",
    "reply with {_embed} with reference event-message",
    "reply with hidden \"Only you can see this\""
})
@Since("5.0.0-a1")
public final class EffReplyWith extends AsyncEffect {

    private Expression<?> content;
    private @Nullable Expression<Message> reference;
    private boolean hidden;
    private Node node;

    public static void register() {
        ManualSyntax.effect(EffReplyWith.class,
            "reply with [:hidden] %string/embedbuilder/messageaction/component/pollbuilder% [with [the] reference[d] [message] %-message%]");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        Class<? extends Event>[] events = getParser().getCurrentEvents();
        if (events == null || events.length == 0) {
            Skript.error("'reply with' can only be used inside a DiSky event.");
            return false;
        }
        // Not EventValues.getEventValueConverter(Class, Class, int): the 2.16 nightly's deprecated
        // path calls Flags.NONE.with(...), which EnumSet.copyOf rejects on an empty non-EnumSet.
        EventValueRegistry registry = Skript.instance().registry(EventValueRegistry.class);
        for (Class<? extends Event> event : events) {
            if (!DiSkyBukkitEvent.class.isAssignableFrom(event)
                    || !registry.resolve(event, MessageChannel.class, EventValue.Time.NOW).successful()) {
                Skript.error("'reply with' can only be used in a DiSky event that has a channel "
                    + "(a message or an interaction event).");
                return false;
            }
        }
        content = expressions[0];
        reference = (Expression<Message>) expressions[1];
        hidden = parseResult.hasTag("hidden");
        node = getParser().getNode();
        getParser().setHasDelayBefore(Kleenean.TRUE);
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        Object raw = content.getSingle(event);
        if (raw == null) {
            DiSkyRuntimeHandler.error(new IllegalArgumentException("the content of 'reply with' is not set"), node, event);
            return;
        }
        Message referenced = reference == null ? null : reference.getSingle(event);

        try (MessageCreateData data = MessagePayloads.toCreateData(raw)) {
            if (((DiSkyBukkitEvent) event).getJdaEvent() instanceof IReplyCallback callback) {
                if (callback.isAcknowledged())
                    callback.getHook().sendMessage(data).setEphemeral(hidden).complete();
                else
                    callback.reply(data).setEphemeral(hidden).complete();
                return;
            }
            MessageChannel channel = ((DiSkyBukkitEvent) event).getJdaEvent() instanceof GenericMessageEvent message
                ? message.getChannel()
                : EventValues.getEventValue(event, MessageChannel.class, EventValues.TIME_NOW);
            if (channel == null) {
                DiSkyRuntimeHandler.error(new IllegalStateException("this event has no channel to reply in"), node, event);
                return;
            }
            var action = channel.sendMessage(data);
            if (referenced != null)
                action.setMessageReference(referenced);
            action.complete();
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "reply with " + (hidden ? "hidden " : "") + content.toString(event, debug)
            + (reference == null ? "" : " with reference " + reference.toString(event, debug));
    }
}
