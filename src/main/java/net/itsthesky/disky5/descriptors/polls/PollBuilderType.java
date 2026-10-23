package net.itsthesky.disky5.descriptors.polls;

import ch.njol.skript.util.Timespan;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.Emojis;
import net.itsthesky.disky5.api.polls.PollAnswerData;
import net.itsthesky.disky5.api.polls.PollBuilder;

import java.time.Duration;
import java.util.List;

@SkriptType(name = "pollbuilder", jda = PollBuilder.class)
@Documentation(
    name = "Poll Builder",
    description = {"A poll that has not been sent yet: a question, 1 to 10 answers, a duration and whether "
        + "voters may pick several answers.",
        "Send it with 'post' / 'reply with', or attach it to a message builder with 'attached poll of'. "
            + "It is validated when attached, and the attached poll is a snapshot: editing the builder "
            + "afterwards changes nothing."},
    since = "5.0.0-a2")
public final class PollBuilderType {

    private PollBuilderType() {}

    @New("[a] new poll (with|titled|asking) [the] [question] %string%")
    @Documentation(
        name = "New Poll Builder",
        description = {"Creates a poll builder from its question (300 characters at most). Without an explicit "
            + "duration it lasts 24 hours. Add at least one answer before sending it."},
        examples = {"set {_poll} to a new poll titled \"Favorite food?\"",
            "add \"Pizza\" and \"Burger\" to poll answer drafts of {_poll}",
            "set poll duration of {_poll} to 2 hours",
            "post {_poll} to event-channel"},
        since = "5.0.0-a2")
    public static PollBuilder newPoll(String question) {
        return new PollBuilder(question);
    }

    @Property("poll question")
    public static String question(PollBuilder poll) {
        return poll.question();
    }

    @Property.Setter("poll question")
    public static void setQuestion(PollBuilder poll, String question) {
        poll.question(question);
    }

    @Property("poll duration")
    @Documentation(
        name = "Poll Builder Duration",
        description = "How long the poll stays open once sent: from 1 hour to 7 days (24 hours by default).",
        examples = {"set poll duration of {_poll} to 3 days"},
        since = "5.0.0-a2")
    public static Timespan duration(PollBuilder poll) {
        return new Timespan(poll.duration().toMillis());
    }

    @Property.Setter("poll duration")
    public static void setDuration(PollBuilder poll, Timespan duration) {
        poll.duration(Duration.ofMillis(duration.getAs(Timespan.TimePeriod.MILLISECOND)));
    }

    @Property("multi answer")
    @Documentation(
        name = "Poll Builder Multi Answer",
        description = "Whether voters may select several answers.",
        examples = {"set multi answer of {_poll} to true"},
        since = "5.0.0-a2")
    public static Boolean multiAnswer(PollBuilder poll) {
        return poll.multiAnswer();
    }

    @Property.Setter("multi answer")
    public static void setMultiAnswer(PollBuilder poll, Boolean value) {
        poll.multiAnswer(Boolean.TRUE.equals(value));
    }

    @Property("poll answer drafts")
    @Documentation(
        name = "Poll Builder Answers",
        description = {"The answers of the poll being built, in order (10 at most).",
            "Add plain texts or 'a new poll answer' objects (to give an answer an emoji); remove one by its text."},
        examples = {"add \"Pizza\" to poll answer drafts of {_poll}",
            "add (a new poll answer with text \"Burger\" with emoji \"🍔\") to poll answer drafts of {_poll}",
            "remove \"Pizza\" from poll answer drafts of {_poll}"},
        since = "5.0.0-a2")
    public static List<PollAnswerData> answers(PollBuilder poll) {
        return List.copyOf(poll.answers());
    }

    @Property.Setter("poll answer drafts")
    public static void setAnswers(PollBuilder poll, Object[] answers) {
        poll.answers().clear();
        for (Object answer : answers)
            addAnswer(poll, answer);
    }

    @Property.Adder("poll answer drafts")
    public static void addAnswer(PollBuilder poll, Object answer) {
        if (answer instanceof PollAnswerData data)
            poll.answers().add(new PollAnswerData(data.text(), data.emoji()));
        else
            poll.answers().add(new PollAnswerData(String.valueOf(answer), null));
    }

    @Property.Remover("poll answer drafts")
    public static void removeAnswer(PollBuilder poll, Object answer) {
        String text = answer instanceof PollAnswerData data ? data.text() : String.valueOf(answer);
        poll.answers().removeIf(a -> a.text().equals(text));
    }
}
