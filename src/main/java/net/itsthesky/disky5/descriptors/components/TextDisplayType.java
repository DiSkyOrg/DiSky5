package net.itsthesky.disky5.descriptors.components;

import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.TextDisplayBuilder;

@SkriptType(name = "textdisplay", jda = TextDisplayBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Text Display",
    description = {"A components V2 block of markdown text. It goes in a container, a section, a modal, "
        + "or straight on a message."},
    since = "5.0.0-a1")
public final class TextDisplayType {

    private TextDisplayType() {}

    @New("[a] new text display [with [the] text] %string%")
    @Documentation(
        name = "New Text Display",
        description = "Creates a text display with the given markdown text.",
        examples = {"add a new text display \"# Welcome\" to components of {_container}"},
        since = "5.0.0-a1")
    public static TextDisplayBuilder newTextDisplay(String text) {
        return new TextDisplayBuilder(text);
    }

    @Property("discord content")
    public static String content(TextDisplayBuilder display) {
        return display.content();
    }

    @Property.Setter("discord content")
    public static void setContent(TextDisplayBuilder display, String content) {
        display.content(content);
    }
}
