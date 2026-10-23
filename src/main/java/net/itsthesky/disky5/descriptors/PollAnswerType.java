package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.entities.messages.MessagePoll;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "pollanswer", jda = MessagePoll.Answer.class)
public final class PollAnswerType {

    @Property("answer text")
    @Documentation(
            name = "Poll Answer Text",
            description = "The text of a poll answer.",
            examples = "reply with answer text of {_answer}",
            since = "5.0.0-a1"
    )
    public static String answerText(MessagePoll.Answer answer) {
        return answer.getText();
    }

    @Property("answer votes")
    @Documentation(
            name = "Poll Answer Votes",
            description = "The number of votes a poll answer has received.",
            examples = "reply with answer votes of {_answer}",
            since = "5.0.0-a1"
    )
    public static int answerVotes(MessagePoll.Answer answer) {
        return answer.getVotes();
    }

    @Property("answer emote")
    @Documentation(
            name = "Poll Answer Emote",
            description = "The emoji of a poll answer, or null if it has none.",
            examples = "reply with answer emote of {_answer}",
            since = "5.0.0-a1"
    )
    public static @Nullable EmojiUnion answerEmote(MessagePoll.Answer answer) {
        return answer.getEmoji();
    }

    @Property("answer id")
    @Documentation(
            name = "Poll Answer ID",
            description = "The id of a poll answer, as used by 'voters of poll answer'. Answers are numbered from 1 in the order they were declared.",
            examples = "reply with answer id of {_answer}",
            since = "5.0.0-a2"
    )
    public static String answerId(MessagePoll.Answer answer) {
        return String.valueOf(answer.getId());
    }

    @Property("answer self voted")
    @Documentation(
            name = "Poll Answer Voted by the Bot",
            description = "Whether the bot itself voted for this answer.",
            examples = "if answer self voted of {_answer} is true:",
            since = "5.0.0-a2"
    )
    public static Boolean selfVoted(MessagePoll.Answer answer) {
        return answer.isSelfVoted();
    }
}
