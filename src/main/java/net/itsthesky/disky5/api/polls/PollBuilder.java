package net.itsthesky.disky5.api.polls;

import net.dv8tion.jda.api.entities.messages.MessagePoll;
import net.dv8tion.jda.api.utils.messages.MessagePollData;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class PollBuilder {

    private String question;
    private Duration duration = Duration.ofHours(24);
    private boolean multiAnswer;
    private final List<PollAnswerData> answers = new ArrayList<>();

    public PollBuilder(String question) {
        this.question = question;
    }

    public MessagePollData build() {
        if (question == null || question.isBlank())
            throw new IllegalStateException("the poll question is empty");
        if (question.length() > MessagePoll.MAX_QUESTION_TEXT_LENGTH)
            throw new IllegalStateException("the poll question is " + question.length()
                + " characters long, the maximum is " + MessagePoll.MAX_QUESTION_TEXT_LENGTH);
        if (answers.isEmpty())
            throw new IllegalStateException("a poll needs at least one answer");
        if (answers.size() > MessagePoll.MAX_ANSWERS)
            throw new IllegalStateException("a poll holds at most " + MessagePoll.MAX_ANSWERS
                + " answers, this one has " + answers.size());
        if (duration.compareTo(Duration.ofHours(1)) < 0
            || duration.compareTo(Duration.ofHours(MessagePoll.MAX_DURATION_HOURS)) > 0)
            throw new IllegalStateException("a poll lasts between 1 hour and " + MessagePoll.MAX_DURATION_HOURS
                + " hours (7 days), this one lasts " + duration.toHours() + " hours");
        net.dv8tion.jda.api.utils.messages.MessagePollBuilder builder = MessagePollData.builder(question)
            .setDuration(duration)
            .setMultiAnswer(multiAnswer);
        for (PollAnswerData answer : answers) {
            if (answer.text() == null || answer.text().isBlank())
                throw new IllegalStateException("a poll answer has no text");
            if (answer.text().length() > MessagePoll.MAX_ANSWER_TEXT_LENGTH)
                throw new IllegalStateException("the poll answer \"" + answer.text() + "\" is "
                    + answer.text().length() + " characters long, the maximum is "
                    + MessagePoll.MAX_ANSWER_TEXT_LENGTH);
            builder.addAnswer(answer.text(), answer.emoji());
        }
        return builder.build();
    }

    public String question() { return question; }
    public void question(String question) { this.question = question; }
    public Duration duration() { return duration; }
    public void duration(Duration duration) { this.duration = duration; }
    public boolean multiAnswer() { return multiAnswer; }
    public void multiAnswer(boolean multiAnswer) { this.multiAnswer = multiAnswer; }
    public List<PollAnswerData> answers() { return answers; }

    @Override
    public String toString() {
        return "poll " + question;
    }
}
