package net.itsthesky.disky5.elements.expressions.polls;

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
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.messages.MessagePoll;
import net.itsthesky.disky5.api.async.DiSkyAsync;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Poll Voters")
@Description({
    "The users who voted for one answer of a poll. The answer is a poll answer object or its number "
        + "(answers are numbered from 1 in the order they were declared); the message is the one carrying the poll, "
        + "not the poll itself. Every page of voters is fetched.",
    "This asks Discord, so it needs 'await'."
})
@Examples({
    "await set {_voters::*} to the voters of poll answer 1 of event-message",
    "loop poll answers of (poll of event-message):",
    "\tawait set {_v::*} to the voters of poll answer loop-value of event-message"
})
@Since("5.0.0-a2")
public final class ExprPollVoters extends SimpleExpression<User> {

    private Expression<?> answer;
    private Expression<Message> message;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprPollVoters.class, User.class, true, ExpressionType.COMBINED,
            "[the] [poll] voters of [poll] answer %number/pollanswer% (of|in|from) [the] [message] %message%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!DiSkyAsync.isParsingInsideAwait()) {
            Skript.error("'voters of poll answer' asks Discord and requires 'await' to be read");
            return false;
        }
        answer = expressions[0];
        message = (Expression<Message>) expressions[1];
        node = getParser().getNode();
        return true;
    }

    @Override
    protected User @Nullable [] get(@NotNull Event event) {
        Object rawAnswer = answer.getSingle(event);
        Message target = message.getSingle(event);
        if (rawAnswer == null || target == null) return new User[0];
        try {
            long id = rawAnswer instanceof MessagePoll.Answer a ? a.getId() : ((Number) rawAnswer).longValue();
            if (target.getPoll() == null)
                throw new IllegalStateException("this message carries no poll");
            if (target.getPoll().getAnswers().stream().noneMatch(a -> a.getId() == id))
                throw new IllegalArgumentException("this poll has no answer numbered " + id);
            return target.retrievePollVoters(id).stream().toArray(User[]::new);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
            return new User[0];
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends User> getReturnType() {
        return User.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the voters of poll answer " + answer.toString(event, debug) + " of " + message.toString(event, debug);
    }
}
