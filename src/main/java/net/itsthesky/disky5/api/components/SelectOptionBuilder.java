package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.checkboxgroup.CheckboxGroupOption;
import net.dv8tion.jda.api.components.radiogroup.RadioGroupOption;
import net.dv8tion.jda.api.components.selections.SelectOption;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.Nullable;

public final class SelectOptionBuilder {

    private String value;
    private @Nullable String label;
    private @Nullable String description;
    private @Nullable Emoji emoji;
    private boolean defaultOption;

    public SelectOptionBuilder(String value) {
        this.value = value;
    }

    public static SelectOptionBuilder from(SelectOption option) {
        SelectOptionBuilder b = new SelectOptionBuilder(option.getValue());
        b.label = option.getLabel();
        b.description = option.getDescription();
        b.emoji = option.getEmoji();
        b.defaultOption = option.isDefault();
        return b;
    }

    public SelectOption build() {
        SelectOption option = SelectOption.of(displayLabel(), value)
            .withDefault(defaultOption);
        if (description != null) option = option.withDescription(description);
        if (emoji != null) option = option.withEmoji(emoji);
        return option;
    }

    public RadioGroupOption buildRadio() {
        return RadioGroupOption.of(displayLabel(), value, description, defaultOption);
    }

    public CheckboxGroupOption buildCheckbox() {
        return CheckboxGroupOption.of(displayLabel(), value, description, defaultOption);
    }

    private String displayLabel() {
        return label == null || label.isBlank() ? value : label;
    }

    public String value() { return value; }
    public void value(String value) { this.value = value; }
    public @Nullable String label() { return label; }
    public void label(@Nullable String label) { this.label = label; }
    public @Nullable String description() { return description; }
    public void description(@Nullable String description) { this.description = description; }
    public @Nullable Emoji emoji() { return emoji; }
    public void emoji(@Nullable Emoji emoji) { this.emoji = emoji; }
    public boolean defaultOption() { return defaultOption; }
    public void defaultOption(boolean defaultOption) { this.defaultOption = defaultOption; }

    @Override
    public String toString() {
        return "option " + value;
    }
}
