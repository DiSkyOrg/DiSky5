package net.itsthesky.disky5.descriptors.components;

import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.ComponentBuilder;

@SkriptType(name = "component", jda = ComponentBuilder.class)
@Documentation(
    name = "Component",
    description = {"Any message or modal component under construction: a button, a dropdown, a text input, "
        + "a row, a container, a section, a text display, a separator, a media gallery, a file display "
        + "or a thumbnail.",
        "Configure it with its properties, then add it to a message builder, a row, a container or a modal. "
            + "It is copied at that moment, so later changes to the component do not affect what was added."},
    since = "5.0.0-a1")
public final class ComponentType {

    private ComponentType() {}

    @Property("unique id")
    @Documentation(
        name = "Component Unique Id",
        description = {"The numeric identifier of a component inside a message, distinct from its text id.",
            "Discord assigns one when the message is sent; set it yourself to address the component "
                + "later with 'replace component with unique id'."},
        examples = {"set unique id of {_button} to 42"},
        since = "5.0.0-a1")
    public static Number uniqueId(ComponentBuilder<?> component) {
        return component.uniqueId();
    }

    @Property.Setter("unique id")
    public static void setUniqueId(ComponentBuilder<?> component, Number value) {
        component.uniqueId(value.intValue());
    }
}
