package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

public final class TextDisplayBuilder implements MessageTopLevelBuilder<TextDisplay>, ContainerChildBuilder<TextDisplay> {

    private String content;
    private int uniqueId;

    public TextDisplayBuilder(String content) {
        this.content = content;
    }

    public static TextDisplayBuilder from(TextDisplay display) {
        TextDisplayBuilder b = new TextDisplayBuilder(display.getContent());
        b.uniqueId = display.getUniqueId();
        return b;
    }

    @Override
    public TextDisplay build() {
        if (content == null || content.isEmpty())
            throw new IllegalStateException("a text display needs a text");
        return TextDisplay.of(content);
    }

    public String content() { return content; }
    public void content(String content) { this.content = content; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "text display";
    }
}
