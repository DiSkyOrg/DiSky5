package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ContainerBuilder implements MessageTopLevelBuilder<Container> {

    private final List<ContainerChildComponent> components = new ArrayList<>();
    private @Nullable Integer accentColor;
    private boolean spoiler;
    private int uniqueId;

    public static ContainerBuilder from(Container container) {
        ContainerBuilder b = new ContainerBuilder();
        b.components.addAll(container.getComponents());
        b.accentColor = container.getAccentColorRaw();
        b.spoiler = container.isSpoiler();
        b.uniqueId = container.getUniqueId();
        return b;
    }

    public void add(ComponentBuilder<?> child) {
        if (child instanceof RowChildBuilder<?> rowChild) {
            RowBuilder row = new RowBuilder();
            row.add(rowChild.buildWithId());
            components.add(row.build());
            return;
        }
        if (!(child instanceof ContainerChildBuilder<?> containerChild))
            throw new IllegalArgumentException("a " + child + " cannot go in a container");
        components.add(containerChild.buildWithId());
    }

    @Override
    public Container build() {
        if (components.isEmpty())
            throw new IllegalStateException("this container is empty");
        Container container = Container.of(components).withSpoiler(spoiler);
        return accentColor != null ? container.withAccentColor(accentColor) : container;
    }

    public List<ContainerChildComponent> components() { return components; }
    public @Nullable Integer accentColor() { return accentColor; }
    public void accentColor(@Nullable Integer accentColor) { this.accentColor = accentColor; }
    public boolean spoiler() { return spoiler; }
    public void spoiler(boolean spoiler) { this.spoiler = spoiler; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "container of " + components.size() + " component(s)";
    }
}
