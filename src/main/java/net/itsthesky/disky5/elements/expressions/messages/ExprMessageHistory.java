package net.itsthesky.disky5.elements.expressions.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;
import net.dv8tion.jda.api.requests.restaction.pagination.PaginationAction.PaginationOrder;
import net.itsthesky.disky5.api.async.DiSkyAsync;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Message History")
@Description({
    "Fetches messages from the history of a channel. 'last X messages of a channel' returns the most recent ones, "
        + "newest first. 'X messages before a message' returns the ones sent before it, closest (newest) first; "
        + "'X messages after a message' the ones sent after it, closest (oldest) first. The reference message "
        + "itself is never included.",
    "Any amount works: above 100, the history is fetched page by page (one request per 100 messages). "
        + "Fewer messages come back when the channel does not have that many. The amount must be at least 1.",
    "The bot needs the View Channel and Read Message History permissions. This asks Discord, so it needs 'await'."
})
@Examples({
    "await set {_msgs::*} to the last 10 messages of event-channel",
    "send \"newest: %discord content of {_msgs::1}%\" to console",
    "await purge (the last 50 messages in event-channel)",
    "await set {_older::*} to the 20 messages before event-message",
    "await set {_newer::*} to the 5 discord messages after {_pinned}"
})
@Since("5.0.0-b1")
public final class ExprMessageHistory extends SimpleExpression<Message> {

    private static final int LAST = 0;
    private static final int BEFORE = 1;
    private static final int AFTER = 2;

    private Expression<Number> amount;
    private Expression<?> anchor;
    private int mode;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprMessageHistory.class, Message.class, true, ExpressionType.COMBINED,
            "[the] last %number% [discord] messages (of|in|from) [the] [channel] %messagechannel%",
            "[the] %number% [discord] messages (1:before|2:after) [the] [message] %message%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!DiSkyAsync.isParsingInsideAwait()) {
            Skript.error("reading messages from a channel's history asks Discord and requires 'await'");
            return false;
        }
        amount = (Expression<Number>) expressions[0];
        anchor = expressions[1];
        mode = matchedPattern == 0 ? LAST : parseResult.mark;
        node = getParser().getNode();
        return true;
    }

    @Override
    protected Message @Nullable [] get(@NotNull Event event) {
        Number rawAmount = amount.getSingle(event);
        Object target = anchor.getSingle(event);
        if (rawAmount == null || target == null) return new Message[0];
        try {
            int count = rawAmount.intValue();
            if (count < 1)
                throw new IllegalArgumentException("cannot fetch " + rawAmount + " messages, the amount must be at least 1");
            MessagePaginationAction history;
            if (mode == LAST) {
                history = ((MessageChannel) target).getIterableHistory();
            } else {
                Message reference = (Message) target;
                history = reference.getChannel().getIterableHistory();
                if (mode == AFTER) history.order(PaginationOrder.FORWARD);
                history.skipTo(reference.getIdLong());
            }
            return history.takeAsync(count).join().toArray(new Message[0]);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
            return new Message[0];
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends Message> getReturnType() {
        return Message.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String count = amount.toString(event, debug);
        return switch (mode) {
            case BEFORE -> "the " + count + " messages before " + anchor.toString(event, debug);
            case AFTER -> "the " + count + " messages after " + anchor.toString(event, debug);
            default -> "the last " + count + " messages of " + anchor.toString(event, debug);
        };
    }
}
