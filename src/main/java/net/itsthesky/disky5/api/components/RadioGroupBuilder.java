package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.radiogroup.RadioGroup;
import net.dv8tion.jda.api.components.radiogroup.RadioGroupOption;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class RadioGroupBuilder implements ModalChildBuilder<RadioGroup> {

    private String id;
    private final List<RadioGroupOption> options = new ArrayList<>();
    private boolean required = true;
    private @Nullable String label;
    private @Nullable String description;
    private int uniqueId;

    public RadioGroupBuilder(String id) {
        this.id = id;
    }

    @Override
    public RadioGroup build() {
        if (options.isEmpty())
            throw new IllegalStateException("the radio group '" + id + "' has no option");
        return RadioGroup.create(id).addOptions(options).setRequired(required).build();
    }

    public String id() { return id; }
    public void id(String id) { this.id = id; }
    public List<RadioGroupOption> options() { return options; }
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
        return "radio group " + id;
    }
}
