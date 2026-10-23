package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.checkbox.Checkbox;
import org.jetbrains.annotations.Nullable;

public final class CheckboxBuilder implements ModalChildBuilder<Checkbox> {

    private String id;
    private boolean checked;
    private @Nullable String label;
    private @Nullable String description;
    private int uniqueId;

    public CheckboxBuilder(String id) {
        this.id = id;
    }

    @Override
    public Checkbox build() {
        return Checkbox.of(id, checked);
    }

    public String id() { return id; }
    public void id(String id) { this.id = id; }
    public boolean checked() { return checked; }
    public void checked(boolean checked) { this.checked = checked; }
    @Override public @Nullable String label() { return label; }
    @Override public void label(@Nullable String label) { this.label = label; }
    @Override public @Nullable String description() { return description; }
    @Override public void description(@Nullable String description) { this.description = description; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "checkbox " + id;
    }
}
