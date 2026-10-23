package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.Component;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.ComponentBuilder;
import net.itsthesky.disky5.api.components.ModalBuilder;

import java.util.List;

@SkriptType(name = "modal", jda = ModalBuilder.class)
@Documentation(
    name = "Modal",
    description = {"A pop-up form shown to the user in answer to a button click, a dropdown click or a slash "
        + "command, with 'show modal'. Its text inputs and dropdowns are read back in 'on modal received'.",
        "It holds up to 5 components; each text input or dropdown needs a label."},
    since = "5.0.0-a1")
public final class ModalType {

    private ModalType() {}

    @New("[a] new modal [with] [the] id %string% (named|titled|with [the] title) %string%")
    @Documentation(
        name = "New Modal",
        description = "Creates a modal with the given id (what 'received id' returns) and title.",
        examples = {"set {_m} to a new modal with id \"feedback\" titled \"Your feedback\"",
            "add a new paragraph text input with id \"body\" labeled \"Message\" to components of {_m}",
            "show {_m}"},
        since = "5.0.0-a1")
    public static ModalBuilder newModal(String id, String title) {
        return new ModalBuilder(id, title);
    }

    @Property("custom id")
    public static String customId(ModalBuilder modal) {
        return modal.id();
    }

    @Property.Setter("custom id")
    public static void setCustomId(ModalBuilder modal, String id) {
        modal.id(id);
    }

    @Property(value = "title", shared = true)
    public static String title(ModalBuilder modal) {
        return modal.title();
    }

    @Property.Setter(value = "title", shared = true)
    public static void setTitle(ModalBuilder modal, String title) {
        modal.title(title);
    }

    @Property("components")
    public static List<Component> components(ModalBuilder modal) {
        return List.copyOf(modal.components());
    }

    @Property.Setter("components")
    public static void setComponents(ModalBuilder modal, ComponentBuilder<?>[] components) {
        modal.components().clear();
        for (ComponentBuilder<?> component : components)
            modal.add(component);
    }

    @Property.Adder("components")
    public static void addComponent(ModalBuilder modal, ComponentBuilder<?> component) {
        modal.add(component);
    }

    @Property.Remover("components")
    public static void removeComponent(ModalBuilder modal, Number uniqueId) {
        modal.components().removeIf(c -> c.getUniqueId() == uniqueId.intValue());
    }
}
