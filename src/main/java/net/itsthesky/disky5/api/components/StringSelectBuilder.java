package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.selections.SelectOption;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class StringSelectBuilder implements RowChildBuilder<StringSelectMenu>, ModalChildBuilder<StringSelectMenu> {

    private String id;
    private @Nullable String placeholder;
    private int minValues = 1;
    private int maxValues = 1;
    private boolean disabled;
    private @Nullable Boolean required;
    private final List<SelectOption> options = new ArrayList<>();
    private @Nullable String label;
    private @Nullable String description;
    private int uniqueId;

    public StringSelectBuilder(String id) {
        this.id = id;
    }

    public static StringSelectBuilder from(StringSelectMenu menu) {
        StringSelectBuilder b = new StringSelectBuilder(menu.getCustomId());
        b.placeholder = menu.getPlaceholder();
        b.minValues = menu.getMinValues();
        b.maxValues = menu.getMaxValues();
        b.disabled = menu.isDisabled();
        b.required = menu.isRequired();
        b.options.addAll(menu.getOptions());
        b.uniqueId = menu.getUniqueId();
        return b;
    }

    @Override
    public StringSelectMenu build() {
        if (options.isEmpty())
            throw new IllegalStateException("the dropdown '" + id + "' has no option");
        StringSelectMenu.Builder builder = StringSelectMenu.create(id)
            .addOptions(options)
            .setRequiredRange(minValues, maxValues)
            .setDisabled(disabled);
        if (placeholder != null) builder.setPlaceholder(placeholder);
        if (required != null) builder.setRequired(required);
        return builder.build();
    }

    public String id() { return id; }
    public void id(String id) { this.id = id; }
    public @Nullable String placeholder() { return placeholder; }
    public void placeholder(@Nullable String placeholder) { this.placeholder = placeholder; }
    public int minValues() { return minValues; }
    public void minValues(int minValues) { this.minValues = minValues; }
    public int maxValues() { return maxValues; }
    public void maxValues(int maxValues) { this.maxValues = maxValues; }
    public boolean disabled() { return disabled; }
    public void disabled(boolean disabled) { this.disabled = disabled; }
    public @Nullable Boolean required() { return required; }
    public void required(@Nullable Boolean required) { this.required = required; }
    public List<SelectOption> options() { return options; }
    @Override public @Nullable String label() { return label; }
    @Override public void label(@Nullable String label) { this.label = label; }
    @Override public @Nullable String description() { return description; }
    @Override public void description(@Nullable String description) { this.description = description; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "dropdown " + id;
    }
}
