package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.ModalTopLevelComponent;
import net.dv8tion.jda.api.modals.Modal;

import java.util.ArrayList;
import java.util.List;

public final class ModalBuilder {

    private String id;
    private String title;
    private final List<ModalTopLevelComponent> components = new ArrayList<>();

    public ModalBuilder(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public void add(ComponentBuilder<?> child) {
        if (components.size() >= Modal.MAX_COMPONENTS)
            throw new IllegalStateException("a modal holds at most " + Modal.MAX_COMPONENTS + " components");
        if (child instanceof ModalChildBuilder<?> input)
            components.add(input.buildLabel());
        else if (child instanceof TextDisplayBuilder text)
            components.add(text.buildWithId());
        else
            throw new IllegalArgumentException("a " + child + " cannot go in a modal; only text inputs, dropdowns and text displays can");
    }

    public Modal build() {
        if (components.isEmpty())
            throw new IllegalStateException("the modal '" + id + "' has no component");
        return Modal.create(id, title).addComponents(components).build();
    }

    public String id() { return id; }
    public void id(String id) { this.id = id; }
    public String title() { return title; }
    public void title(String title) { this.title = title; }
    public List<ModalTopLevelComponent> components() { return components; }

    @Override
    public String toString() {
        return "modal " + id;
    }
}
