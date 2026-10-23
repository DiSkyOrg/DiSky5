package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.separator.Separator;

public final class SeparatorBuilder implements MessageTopLevelBuilder<Separator>, ContainerChildBuilder<Separator> {

    private boolean divider = true;
    private Separator.Spacing spacing;
    private int uniqueId;

    public SeparatorBuilder(Separator.Spacing spacing) {
        this.spacing = spacing;
    }

    public static SeparatorBuilder from(Separator separator) {
        SeparatorBuilder b = new SeparatorBuilder(separator.getSpacing());
        b.divider = separator.isDivider();
        b.uniqueId = separator.getUniqueId();
        return b;
    }

    @Override
    public Separator build() {
        return Separator.create(divider, spacing);
    }

    public boolean divider() { return divider; }
    public void divider(boolean divider) { this.divider = divider; }
    public Separator.Spacing spacing() { return spacing; }
    public void spacing(Separator.Spacing spacing) { this.spacing = spacing; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "separator";
    }
}
