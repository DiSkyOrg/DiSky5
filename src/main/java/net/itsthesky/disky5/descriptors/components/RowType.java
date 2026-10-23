package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.Component;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.ComponentBuilder;
import net.itsthesky.disky5.api.components.RowBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "row", jda = RowBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Component Row",
    description = {"A horizontal line of components on a message: up to 5 buttons, or one dropdown.",
        "You rarely need one: adding a button or a dropdown straight to a message builder puts it in a "
            + "row for you. Build a row yourself to control exactly which buttons share a line."},
    since = "5.0.0-a1")
public final class RowType {

    private RowType() {}

    @New("[a] new [component[s]] row [with [the] [components] %-components%]")
    @Documentation(
        name = "New Component Row",
        description = "Creates a row, optionally filled with buttons or one dropdown.",
        examples = {"set {_row} to a new row with {_yes} and {_no}",
            "add {_row} to components of {_message}"},
        since = "5.0.0-a1")
    public static RowBuilder newRow(ComponentBuilder<?> @Nullable [] components) {
        RowBuilder row = new RowBuilder();
        if (components != null)
            for (ComponentBuilder<?> component : components)
                row.add(component);
        return row;
    }

    @Property("components")
    @Documentation(
        name = "Components",
        description = {"The components inside a row, a container, a section, a modal or a message builder.",
            "Add a component to it; the component is copied at that moment. Remove one by its unique id.",
            "On a message builder, a bare button joins the last row of buttons when it has room, a bare "
                + "dropdown gets a row of its own, and any container, section, text display, separator, "
                + "media gallery or file display switches the message to components V2 (no content, no embed)."},
        examples = {"add {_button} to components of {_message}",
            "add {_dropdown} to components of {_row}",
            "add {_input} to components of {_modal}",
            "add {_section} to components of {_container}",
            "set components of {_message} to {_row1} and {_row2}"},
        since = "5.0.0-a1")
    public static List<Component> components(RowBuilder row) {
        return List.copyOf(row.components());
    }

    @Property.Setter("components")
    public static void setComponents(RowBuilder row, ComponentBuilder<?>[] components) {
        row.components().clear();
        for (ComponentBuilder<?> component : components)
            row.add(component);
    }

    @Property.Adder("components")
    public static void addComponent(RowBuilder row, ComponentBuilder<?> component) {
        row.add(component);
    }

    @Property.Remover("components")
    public static void removeComponent(RowBuilder row, Number uniqueId) {
        row.components().removeIf(c -> c.getUniqueId() == uniqueId.intValue());
    }
}
