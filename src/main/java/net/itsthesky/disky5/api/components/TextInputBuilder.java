package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import org.jetbrains.annotations.Nullable;

public final class TextInputBuilder implements ModalChildBuilder<TextInput> {

    private String id;
    private TextInputStyle style;
    private @Nullable String label;
    private @Nullable String description;
    private @Nullable String placeholder;
    private @Nullable String value;
    private int minLength = -1;
    private int maxLength = -1;
    private boolean required = true;
    private int uniqueId;

    public TextInputBuilder(String id, TextInputStyle style) {
        this.id = id;
        this.style = style;
    }

    public static TextInputBuilder from(TextInput input) {
        TextInputBuilder b = new TextInputBuilder(input.getCustomId(), input.getStyle());
        b.placeholder = input.getPlaceHolder();
        b.value = input.getValue();
        b.minLength = input.getMinLength();
        b.maxLength = input.getMaxLength();
        b.required = input.isRequired();
        b.uniqueId = input.getUniqueId();
        return b;
    }

    @Override
    public TextInput build() {
        TextInput.Builder builder = TextInput.create(id, style).setRequired(required);
        if (placeholder != null) builder.setPlaceholder(placeholder);
        if (value != null) builder.setValue(value);
        if (minLength >= 0) builder.setMinLength(minLength);
        if (maxLength >= 0) builder.setMaxLength(maxLength);
        return builder.build();
    }

    public String id() { return id; }
    public void id(String id) { this.id = id; }
    public TextInputStyle style() { return style; }
    public void style(TextInputStyle style) { this.style = style; }
    public @Nullable String placeholder() { return placeholder; }
    public void placeholder(@Nullable String placeholder) { this.placeholder = placeholder; }
    public @Nullable String value() { return value; }
    public void value(@Nullable String value) { this.value = value; }
    public int minLength() { return minLength; }
    public void minLength(int minLength) { this.minLength = minLength; }
    public int maxLength() { return maxLength; }
    public void maxLength(int maxLength) { this.maxLength = maxLength; }
    public boolean required() { return required; }
    public void required(boolean required) { this.required = required; }
    @Override public @Nullable String label() { return label; }
    @Override public void label(@Nullable String label) { this.label = label; }
    @Override public @Nullable String description() { return description; }
    @Override public void description(@Nullable String description) { this.description = description; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "text input " + id;
    }
}
