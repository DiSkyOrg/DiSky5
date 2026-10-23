package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.actionrow.ActionRowChildComponent;
import net.dv8tion.jda.api.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public final class RowBuilder implements MessageTopLevelBuilder<ActionRow>, ContainerChildBuilder<ActionRow> {

    public static final int MAX_BUTTONS = 5;

    private final List<ActionRowChildComponent> components = new ArrayList<>();
    private int uniqueId;

    public static RowBuilder from(ActionRow row) {
        RowBuilder b = new RowBuilder();
        b.components.addAll(row.getComponents());
        b.uniqueId = row.getUniqueId();
        return b;
    }

    public void add(ComponentBuilder<?> child) {
        if (!(child instanceof RowChildBuilder<?> rowChild))
            throw new IllegalArgumentException("a " + child + " cannot go in a row; only buttons and dropdowns can");
        add(rowChild.buildWithId());
    }

    public void add(ActionRowChildComponent component) {
        if (component instanceof Button) {
            if (components.stream().anyMatch(c -> !(c instanceof Button)))
                throw new IllegalStateException("a row holding a dropdown cannot hold a button too");
            if (components.size() >= MAX_BUTTONS)
                throw new IllegalStateException("a row holds at most " + MAX_BUTTONS + " buttons");
        } else if (!components.isEmpty()) {
            throw new IllegalStateException("a dropdown takes a whole row on its own");
        }
        components.add(component);
    }

    public boolean accepts(ActionRowChildComponent component) {
        return component instanceof Button
            && components.size() < MAX_BUTTONS
            && components.stream().allMatch(c -> c instanceof Button);
    }

    public List<ActionRowChildComponent> components() { return components; }

    @Override
    public boolean requiresComponentsV2() {
        return false;
    }

    @Override
    public ActionRow build() {
        if (components.isEmpty())
            throw new IllegalStateException("this row is empty");
        return ActionRow.of(components);
    }

    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "row of " + components.size() + " component(s)";
    }
}
