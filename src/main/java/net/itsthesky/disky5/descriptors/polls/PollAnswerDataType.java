package net.itsthesky.disky5.descriptors.polls;

import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.Emojis;
import net.itsthesky.disky5.api.polls.PollAnswerData;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "pollanswerdata", jda = PollAnswerData.class)
@Documentation(
    name = "Poll Answer Draft",
    description = {"An answer of a poll that has not been sent yet: a text and an optional emoji.",
        "Once the poll is sent, its answers are read back as regular 'pollanswer' objects."},
    since = "5.0.0-a2")
public final class PollAnswerDataType {

    private PollAnswerDataType() {}

    @New("[a] new poll answer [with] [the] (text|content) %string% [with [the] emoji %-emote/string%]")
    @Documentation(
        name = "New Poll Answer",
        description = "Creates a poll answer with an optional emoji, to add to the answers of a poll builder.",
        examples = {"set {_a} to a new poll answer with text \"Pizza\" with emoji \"🍕\"",
            "add {_a} to poll answer drafts of {_poll}"},
        since = "5.0.0-a2")
    public static PollAnswerData newAnswer(String text, @Nullable Object emoji) {
        return new PollAnswerData(text, Emojis.from(emoji));
    }

    @Property("draft answer text")
    @Documentation(
        name = "Poll Answer Draft Text",
        description = "The text of a poll answer that has not been sent yet (55 characters at most).",
        examples = {"set draft answer text of {_a} to \"Burger\""},
        since = "5.0.0-a2")
    public static String text(PollAnswerData answer) {
        return answer.text();
    }

    @Property.Setter("draft answer text")
    public static void setText(PollAnswerData answer, String text) {
        answer.text(text);
    }

    @Property("draft answer emoji")
    @Documentation(
        name = "Poll Answer Draft Emoji",
        description = "The emoji of a poll answer that has not been sent yet, as written in a message.",
        examples = {"set draft answer emoji of {_a} to \"🍔\""},
        since = "5.0.0-a2")
    public static @Nullable String emoji(PollAnswerData answer) {
        return answer.emoji() == null ? null : answer.emoji().getFormatted();
    }

    @Property.Setter("draft answer emoji")
    public static void setEmoji(PollAnswerData answer, @Nullable Object emoji) {
        answer.emoji(Emojis.from(emoji));
    }
}
