package net.itsthesky.disky5.descriptors.components;

import ch.njol.skript.util.Color;
import net.dv8tion.jda.api.components.Component;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.ComponentBuilder;
import net.itsthesky.disky5.api.components.ContainerBuilder;
import net.itsthesky.disky5.core.Conversions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "container", jda = ContainerBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Container",
    description = {"A components V2 box with an optional accent color on its left edge, holding text displays, "
        + "sections, separators, media galleries, file displays and rows of buttons or dropdowns.",
        "A message carrying a container is a components V2 message: it can have no content and no embed."},
    since = "5.0.0-a1")
public final class ContainerType {

    private ContainerType() {}

    @New("[a] new [discord] container [with [the] [components] %-components%]")
    @Documentation(
        name = "New Container",
        description = "Creates a container, optionally filled with components.",
        examples = {"set {_c} to a new container",
            "set accent color of {_c} to red",
            "add a new text display \"**Hello**\" to components of {_c}",
            "add a new separator to components of {_c}",
            "add {_button} to components of {_c}",
            "post {_c} to event-channel"},
        since = "5.0.0-a1")
    public static ContainerBuilder newContainer(ComponentBuilder<?> @Nullable [] components) {
        ContainerBuilder container = new ContainerBuilder();
        if (components != null)
            for (ComponentBuilder<?> component : components)
                container.add(component);
        return container;
    }

    @Property("components")
    public static List<Component> components(ContainerBuilder container) {
        return List.copyOf(container.components());
    }

    @Property.Setter("components")
    public static void setComponents(ContainerBuilder container, ComponentBuilder<?>[] components) {
        container.components().clear();
        for (ComponentBuilder<?> component : components)
            container.add(component);
    }

    @Property.Adder("components")
    public static void addComponent(ContainerBuilder container, ComponentBuilder<?> component) {
        container.add(component);
    }

    @Property.Remover("components")
    public static void removeComponent(ContainerBuilder container, Number uniqueId) {
        container.components().removeIf(c -> c.getUniqueId() == uniqueId.intValue());
    }

    @Property("accent color")
    @Documentation(
        name = "Container Accent Color",
        description = "The color of the stripe on the left edge of the container, like an embed's color.",
        examples = {"set accent color of {_c} to red"},
        since = "5.0.0-a1")
    public static @Nullable Color accentColor(ContainerBuilder container) {
        Integer raw = container.accentColor();
        return raw == null ? null : Conversions.color(new java.awt.Color(raw));
    }

    @Property.Setter("accent color")
    public static void setAccentColor(ContainerBuilder container, @Nullable Color color) {
        java.awt.Color awt = Conversions.awt(color);
        container.accentColor(awt == null ? null : awt.getRGB() & 0xFFFFFF);
    }

    @Property("spoiler")
    @Documentation(
        name = "Component Spoiler",
        description = "Whether the container, file display, thumbnail or media gallery is blurred until clicked.",
        examples = {"set spoiler of {_c} to true"},
        since = "5.0.0-a1")
    public static Boolean spoiler(ContainerBuilder container) {
        return container.spoiler();
    }

    @Property.Setter("spoiler")
    public static void setSpoiler(ContainerBuilder container, Boolean spoiler) {
        container.spoiler(Boolean.TRUE.equals(spoiler));
    }
}
