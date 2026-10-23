package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.section.SectionAccessoryComponent;
import net.dv8tion.jda.api.components.section.SectionContentComponent;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class SectionBuilder implements MessageTopLevelBuilder<Section>, ContainerChildBuilder<Section> {

    private final List<SectionContentComponent> content = new ArrayList<>();
    private @Nullable SectionAccessoryComponent accessory;
    private int uniqueId;

    public static SectionBuilder from(Section section) {
        SectionBuilder b = new SectionBuilder();
        b.content.addAll(section.getContentComponents());
        b.accessory = section.getAccessory();
        b.uniqueId = section.getUniqueId();
        return b;
    }

    public void add(Object child) {
        if (content.size() >= Section.MAX_COMPONENTS)
            throw new IllegalStateException("a section holds at most " + Section.MAX_COMPONENTS + " texts");
        if (child instanceof TextDisplayBuilder text)
            content.add(text.buildWithId());
        else if (child instanceof ComponentBuilder<?>)
            throw new IllegalArgumentException("a " + child + " cannot go in a section; only text displays can");
        else
            content.add(TextDisplay.of(String.valueOf(child)));
    }

    public void accessory(@Nullable ComponentBuilder<?> child) {
        if (child == null) {
            accessory = null;
            return;
        }
        if (!(child instanceof SectionAccessoryBuilder<?> sectionAccessory))
            throw new IllegalArgumentException("a " + child + " cannot be the accessory of a section; only a button or a thumbnail can");
        accessory = sectionAccessory.buildWithId();
    }

    @Override
    public Section build() {
        if (accessory == null)
            throw new IllegalStateException("a section needs an accessory (a button or a thumbnail)");
        if (content.isEmpty())
            throw new IllegalStateException("a section needs at least one text");
        return Section.of(accessory, content);
    }

    public List<SectionContentComponent> content() { return content; }
    public @Nullable SectionAccessoryComponent accessory() { return accessory; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "section";
    }
}
