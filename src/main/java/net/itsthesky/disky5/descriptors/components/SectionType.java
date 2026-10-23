package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.Component;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.ComponentBuilder;
import net.itsthesky.disky5.api.components.SectionBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "containersection", jda = SectionBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Section",
    description = {"A components V2 block of one to three texts with an accessory (a button or a thumbnail) "
        + "on its right.",
        "Its texts are text displays; 'a new section with texts ...' wraps plain texts for you."},
    since = "5.0.0-a1")
public final class SectionType {

    private SectionType() {}

    @New("[a] new [container] section [with [the] accessory %-component%] [with [the] text[s] %-strings%]")
    @Documentation(
        name = "New Section",
        description = "Creates a section. It needs an accessory and at least one text before it is sent.",
        examples = {"set {_s} to a new section with accessory {_thumbnail} with texts \"**Title**\" and \"Some text\"",
            "add {_s} to components of {_container}"},
        since = "5.0.0-a1")
    public static SectionBuilder newSection(@Nullable ComponentBuilder<?> accessory, String @Nullable [] texts) {
        SectionBuilder section = new SectionBuilder();
        section.accessory(accessory);
        if (texts != null)
            for (String text : texts)
                section.add(text);
        return section;
    }

    @Property("components")
    public static List<Component> components(SectionBuilder section) {
        return List.copyOf(section.content());
    }

    @Property.Setter("components")
    public static void setComponents(SectionBuilder section, ComponentBuilder<?>[] components) {
        section.content().clear();
        for (ComponentBuilder<?> component : components)
            section.add(component);
    }

    @Property.Adder("components")
    public static void addComponent(SectionBuilder section, ComponentBuilder<?> component) {
        section.add(component);
    }

    @Property.Remover("components")
    public static void removeComponent(SectionBuilder section, Number uniqueId) {
        section.content().removeIf(c -> c.getUniqueId() == uniqueId.intValue());
    }

    @Property("accessory")
    @Documentation(
        name = "Section Accessory",
        description = "The button or thumbnail shown on the right of a section.",
        examples = {"set accessory of {_s} to a new thumbnail from url \"https://example.com/logo.png\"",
            "set accessory of {_s} to {_button}"},
        since = "5.0.0-a1")
    public static @Nullable Component accessory(SectionBuilder section) {
        return section.accessory();
    }

    @Property.Setter("accessory")
    public static void setAccessory(SectionBuilder section, @Nullable ComponentBuilder<?> accessory) {
        section.accessory(accessory);
    }
}
