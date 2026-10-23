package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.TextInputBuilder;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "textinput", jda = TextInputBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Text Input",
    description = {"A text field of a modal. Read what the user typed in 'on modal received' with "
        + "'value of text input with id'.",
        "It only lives inside a modal, never on a message, and needs a label there."},
    since = "5.0.0-a1")
public final class TextInputType {

    private TextInputType() {}

    @New("[a] new [%-textinputstyle%] text[( |-)]input [with] [the] id %string% (named|labeled|with [the] label) %string%")
    @Documentation(
        name = "New Text Input",
        description = {"Creates a text input for a modal. The style is 'short' (one line, the default) "
            + "or 'paragraph' (a text area)."},
        examples = {"set {_i} to a new short text input with id \"name\" labeled \"Your name\"",
            "set {_i} to a new paragraph text input with id \"body\" labeled \"Your message\"",
            "add {_i} to components of {_modal}"},
        since = "5.0.0-a1")
    public static TextInputBuilder newTextInput(@Nullable TextInputStyle style, String id, String label) {
        TextInputBuilder input = new TextInputBuilder(id, style == null ? TextInputStyle.SHORT : style);
        input.label(label);
        return input;
    }

    @Property("custom id")
    public static String customId(TextInputBuilder input) {
        return input.id();
    }

    @Property.Setter("custom id")
    public static void setCustomId(TextInputBuilder input, String id) {
        input.id(id);
    }

    @Property(value = "style", shared = true)
    public static TextInputStyle style(TextInputBuilder input) {
        return input.style();
    }

    @Property.Setter(value = "style", shared = true)
    public static void setStyle(TextInputBuilder input, TextInputStyle style) {
        input.style(style);
    }

    @Property("label")
    public static @Nullable String label(TextInputBuilder input) {
        return input.label();
    }

    @Property.Setter("label")
    public static void setLabel(TextInputBuilder input, @Nullable String label) {
        input.label(label);
    }

    @Property("description")
    public static @Nullable String description(TextInputBuilder input) {
        return input.description();
    }

    @Property.Setter("description")
    public static void setDescription(TextInputBuilder input, @Nullable String description) {
        input.description(description);
    }

    @Property("placeholder")
    public static @Nullable String placeholder(TextInputBuilder input) {
        return input.placeholder();
    }

    @Property.Setter("placeholder")
    public static void setPlaceholder(TextInputBuilder input, @Nullable String placeholder) {
        input.placeholder(placeholder);
    }

    @Property("default value")
    @Documentation(
        name = "Text Input Default Value",
        description = "The text pre-filled in the input when the modal opens.",
        examples = {"set default value of {_input} to \"John\""},
        since = "5.0.0-a1")
    public static @Nullable String defaultValue(TextInputBuilder input) {
        return input.value();
    }

    @Property.Setter("default value")
    public static void setDefaultValue(TextInputBuilder input, @Nullable String value) {
        input.value(value);
    }

    @Property("min range")
    public static Number minRange(TextInputBuilder input) {
        return input.minLength();
    }

    @Property.Setter("min range")
    public static void setMinRange(TextInputBuilder input, Number value) {
        input.minLength(value.intValue());
    }

    @Property("max range")
    public static Number maxRange(TextInputBuilder input) {
        return input.maxLength();
    }

    @Property.Setter("max range")
    public static void setMaxRange(TextInputBuilder input, Number value) {
        input.maxLength(value.intValue());
    }

    @Property("required")
    public static Boolean required(TextInputBuilder input) {
        return input.required();
    }

    @Property.Setter("required")
    public static void setRequired(TextInputBuilder input, Boolean required) {
        input.required(Boolean.TRUE.equals(required));
    }
}
