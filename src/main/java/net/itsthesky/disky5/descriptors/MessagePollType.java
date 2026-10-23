package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Date;
import net.dv8tion.jda.api.entities.messages.MessagePoll;
import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.annotations.ConditionType;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;

import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "messagepoll", jda = MessagePoll.class)
public final class MessagePollType {

    @Property("poll question")
    @Documentation(
            name = "Poll Question",
            description = "The question text of a poll.",
            examples = "reply with poll question of (poll of event-message)",
            since = "5.0.0-a1"
    )
    public static String pollQuestion(MessagePoll poll) {
        return poll.getQuestion().getText();
    }

    @Property("poll answers")
    @Documentation(
            name = "Poll Answers",
            description = "The list of answers of a poll.",
            examples = {
                    "set {_answers::*} to poll answers of (poll of event-message)",
                    "loop poll answers of (poll of event-message):"
            },
            since = "5.0.0-a1"
    )
    public static List<MessagePoll.Answer> pollAnswers(MessagePoll poll) {
        return poll.getAnswers();
    }

    @Condition(property = "expired", type = ConditionType.BE)
    @Documentation(
            name = "Poll is Expired",
            description = "Whether the poll has passed its deadline and no longer accepts votes.",
            examples = "if (poll of event-message) is expired:",
            since = "5.0.0-a1"
    )
    public static boolean expired(MessagePoll poll) {
        return poll.isExpired();
    }

    @Condition(property = "finalized", type = ConditionType.BE)
    @Documentation(
            name = "Poll is Finalized",
            description = "Whether the poll votes are finalized, meaning the reported vote counts are the "
                    + "definitive ones.",
            examples = "if (poll of event-message) is finalized:",
            since = "5.0.0-a1"
    )
    public static boolean finalized(MessagePoll poll) {
        return poll.isFinalizedVotes();
    }

    @Property("poll expiration date")
    @Documentation(
            name = "Poll Expiration Date",
            description = "The date at which the poll stops accepting votes.",
            examples = "reply with poll expiration date of (poll of event-message)",
            since = "5.0.0-a2"
    )
    public static @Nullable Date expirationDate(MessagePoll poll) {
        return Conversions.date(poll.getTimeExpiresAt());
    }

    @Property("multi answer")
    @Documentation(
            name = "Poll Multi Answer",
            description = "Whether voters may select several answers.",
            examples = "if multi answer of (poll of event-message) is true:",
            since = "5.0.0-a2"
    )
    public static Boolean multiAnswer(MessagePoll poll) {
        return poll.isMultiAnswer();
    }

    @Property("poll layout")
    @Documentation(
            name = "Poll Layout",
            description = "The layout of the poll, as a lowercase name (Discord only defines 'default' for now).",
            examples = "reply with poll layout of (poll of event-message)",
            since = "5.0.0-a2"
    )
    public static String layout(MessagePoll poll) {
        return poll.getLayout().name().toLowerCase();
    }
}
