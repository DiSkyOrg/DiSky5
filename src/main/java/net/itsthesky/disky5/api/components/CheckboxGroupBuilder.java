package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.checkboxgroup.CheckboxGroup;
import net.dv8tion.jda.api.components.checkboxgroup.CheckboxGroupOption;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class CheckboxGroupBuilder implements ModalChildBuilder<CheckboxGroup> {

    private String id;
    private final List<CheckboxGroupOption> options = new ArrayList<>();
    private int minValues = 1;
    private int maxValues = -1;
    private boolean required = true;
    private @Nullable String label;
    private @Nullable String description;
    private int uniqueId;

    public CheckboxGroupBuilder(String id) {
        this.id = id;
    }

    @Override
    public CheckboxGroup build() {
        if (options.isEmpty())
            throw new IllegalStateException("the checkbox group '" + id + "' has no option");
        // Discord refuses "required" with a minimum of 0: a group nothing has to be ticked in is optional.
        CheckboxGroup.Builder builder = CheckboxGroup.create(id).addOptions(options)
            .setRequired(required && minValues > 0);
        builder.setMinValues(minValues);
        builder.setMaxValues(maxValues < 0 ? options.size() : maxValues);
        return builder.build();
    }

    public String id() { return id; }
    public void id(String id) { this.id = id; }
    public List<CheckboxGroupOption> options() { return options; }
    public int minValues() { return minValues; }
    public void minValues(int minValues) { this.minValues = minValues; }
    public int maxValues() { return maxValues; }
    public void maxValues(int maxValues) { this.maxValues = maxValues; }
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
        return "checkbox group " + id;
    }
}
