package net.itsthesky.disky5.api.polls;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.Nullable;

public final class PollAnswerData {

    private String text;
    private @Nullable Emoji emoji;

    public PollAnswerData(String text, @Nullable Emoji emoji) {
        this.text = text;
        this.emoji = emoji;
    }

    public String text() { return text; }
    public void text(String text) { this.text = text; }
    public @Nullable Emoji emoji() { return emoji; }
    public void emoji(@Nullable Emoji emoji) { this.emoji = emoji; }

    @Override
    public String toString() {
        return "poll answer " + text;
    }
}
