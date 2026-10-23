package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.SkuSnowflake;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.Nullable;

public final class ButtonBuilder implements RowChildBuilder<Button>, SectionAccessoryBuilder<Button> {

    private ButtonStyle style;
    private @Nullable String id;
    private @Nullable String url;
    private @Nullable String sku;
    private @Nullable String label;
    private @Nullable Emoji emoji;
    private boolean disabled;
    private int uniqueId;

    public ButtonBuilder(ButtonStyle style) {
        this.style = style;
    }

    public static ButtonBuilder from(Button button) {
        ButtonBuilder b = new ButtonBuilder(button.getStyle());
        b.id = button.getCustomId();
        b.url = button.getUrl();
        b.sku = button.getSku() == null ? null : button.getSku().getId();
        b.label = button.getLabel().isEmpty() ? null : button.getLabel();
        b.emoji = button.getEmoji();
        b.disabled = button.isDisabled();
        b.uniqueId = button.getUniqueId();
        return b;
    }

    @Override
    public Button build() {
        Button button;
        if (style == ButtonStyle.PREMIUM) {
            if (sku == null)
                throw new IllegalStateException("a premium button needs a sku id");
            button = Button.premium(SkuSnowflake.fromId(sku));
        } else {
            String target = style == ButtonStyle.LINK ? url : id;
            if (target == null || target.isBlank())
                throw new IllegalStateException(style == ButtonStyle.LINK
                    ? "a link button needs a url" : "a " + style.name().toLowerCase() + " button needs an id");
            if ((label == null || label.isBlank()) && emoji == null)
                throw new IllegalStateException("a button needs a label or an emoji");
            button = Button.of(style, target, label == null ? "" : label, emoji);
        }
        return disabled ? button.asDisabled() : button;
    }

    public ButtonStyle style() { return style; }
    public void style(ButtonStyle style) { this.style = style; }
    public @Nullable String id() { return id; }
    public void id(@Nullable String id) { this.id = id; }
    public @Nullable String url() { return url; }
    public void url(@Nullable String url) { this.url = url; }
    public @Nullable String sku() { return sku; }
    public void sku(@Nullable String sku) { this.sku = sku; }
    public @Nullable String label() { return label; }
    public void label(@Nullable String label) { this.label = label; }
    public @Nullable Emoji emoji() { return emoji; }
    public void emoji(@Nullable Emoji emoji) { this.emoji = emoji; }
    public boolean disabled() { return disabled; }
    public void disabled(boolean disabled) { this.disabled = disabled; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "button " + (id != null ? id : url != null ? url : sku);
    }
}
