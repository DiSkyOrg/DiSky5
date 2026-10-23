package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.ButtonBuilder;
import net.itsthesky.disky5.api.components.Emojis;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "button", jda = ButtonBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Button",
    description = {"A clickable button. A styled button carries an id you get back in 'on button click'; "
        + "a link button opens a url instead and never fires an event; a premium button opens a sku purchase.",
        "A button needs a label or an emoji. Up to 5 buttons fit in one row."},
    since = "5.0.0-a1")
public final class ButtonType {

    private ButtonType() {}

    @New("[a] new %buttonstyle% button [with] [the] id %string% [(named|with [the] label) %-string%] [with [the] emoji %-emote/string%]")
    @Documentation(
        name = "New Button",
        description = {"Creates a styled button (primary, secondary, success or danger) with the id you "
            + "will read back as 'clicked id' when it is clicked.",
            "The label and the emoji are optional here and can be set later, but one of them is required "
                + "before the button is sent."},
        examples = {"set {_b} to a new primary button with id \"confirm\" named \"Confirm\"",
            "set {_b} to a new danger button with id \"delete\" named \"Delete\" with emoji \"🗑️\"",
            "add {_b} to components of {_message}"},
        since = "5.0.0-a1")
    public static ButtonBuilder newButton(ButtonStyle style, String id, @Nullable String label, @Nullable Object emoji) {
        if (style == ButtonStyle.LINK || style == ButtonStyle.PREMIUM)
            throw new IllegalArgumentException("a " + style.name().toLowerCase() + " button has no id; use 'a new "
                + style.name().toLowerCase() + " button' instead");
        ButtonBuilder button = new ButtonBuilder(style);
        button.id(id);
        button.label(label);
        button.emoji(Emojis.from(emoji));
        return button;
    }

    @New("[a] new link button [with] [the] url %string% [(named|with [the] label) %-string%] [with [the] emoji %-emote/string%]")
    @Documentation(
        name = "New Link Button",
        description = "Creates a button that opens a url when clicked. It never fires 'on button click'.",
        examples = {"set {_b} to a new link button with url \"https://disky.me\" named \"Docs\""},
        since = "5.0.0-a1")
    public static ButtonBuilder newLinkButton(String url, @Nullable String label, @Nullable Object emoji) {
        ButtonBuilder button = new ButtonBuilder(ButtonStyle.LINK);
        button.url(url);
        button.label(label);
        button.emoji(Emojis.from(emoji));
        return button;
    }

    @New("[a] new premium button [with] [the] sku [id] %string%")
    @Documentation(
        name = "New Premium Button",
        description = "Creates a button that opens the purchase flow of one of the application's skus. "
            + "Discord draws its label itself.",
        examples = {"set {_b} to a new premium button with sku id \"1234567890\""},
        since = "5.0.0-a1")
    public static ButtonBuilder newPremiumButton(String sku) {
        ButtonBuilder button = new ButtonBuilder(ButtonStyle.PREMIUM);
        button.sku(sku);
        return button;
    }

    @Property("label")
    @Documentation(
        name = "Component Label",
        description = {"The text shown on a button or a dropdown option, or the title above a text input "
            + "or a dropdown inside a modal.",
            "A text input or a dropdown added to a modal must have one."},
        examples = {"set label of {_button} to \"Confirm\"",
            "set label of {_input} to \"Your name\""},
        since = "5.0.0-a1")
    public static @Nullable String label(ButtonBuilder button) {
        return button.label();
    }

    @Property.Setter("label")
    public static void setLabel(ButtonBuilder button, @Nullable String label) {
        button.label(label);
    }

    @Property(value = "style", shared = true)
    @Documentation(
        name = "Component Style",
        description = "The style of a button (its color) or of a text input (short or paragraph).",
        examples = {"set style of {_button} to danger",
            "set style of {_input} to paragraph"},
        since = "5.0.0-a1")
    public static ButtonStyle style(ButtonBuilder button) {
        return button.style();
    }

    @Property.Setter(value = "style", shared = true)
    public static void setStyle(ButtonBuilder button, ButtonStyle style) {
        button.style(style);
    }

    @Property("custom id")
    @Documentation(
        name = "Component Custom Id",
        description = {"The text id of a component: what 'clicked id' returns for a button, the key of a "
            + "dropdown or a text input in the received values, the id of a modal.",
            "A link or premium button has none."},
        examples = {"set custom id of {_button} to \"confirm\"",
            "if custom id of event-button is \"confirm\":"},
        since = "5.0.0-a1")
    public static @Nullable String customId(ButtonBuilder button) {
        return button.id();
    }

    @Property.Setter("custom id")
    public static void setCustomId(ButtonBuilder button, @Nullable String id) {
        button.id(id);
    }

    @Property("url")
    @Documentation(
        name = "Button Url",
        description = "The url a link button opens. Setting it turns the button into a link button.",
        examples = {"set url of {_button} to \"https://disky.me\""},
        since = "5.0.0-a1")
    public static @Nullable String url(ButtonBuilder button) {
        return button.url();
    }

    @Property.Setter("url")
    public static void setUrl(ButtonBuilder button, @Nullable String url) {
        button.url(url);
        if (url != null) button.style(ButtonStyle.LINK);
    }

    @Property("emoji")
    @Documentation(
        name = "Component Emoji",
        description = "The emoji shown on a button or a dropdown option. Accepts an emote, a unicode "
            + "emoji or the '<:name:id>' form of a custom one.",
        examples = {"set emoji of {_button} to \"✅\"",
            "set emoji of {_option} to {_emote}"},
        since = "5.0.0-a1")
    public static @Nullable String emoji(ButtonBuilder button) {
        return button.emoji() == null ? null : button.emoji().getFormatted();
    }

    @Property.Setter("emoji")
    public static void setEmoji(ButtonBuilder button, @Nullable Object emoji) {
        button.emoji(Emojis.from(emoji));
    }

    @Property("disabled")
    @Documentation(
        name = "Component Disabled",
        description = "Whether the component is greyed out and cannot be used.",
        examples = {"set disabled of {_button} to true",
            "set disabled of event-button to true",
            "edit interaction components with event-button"},
        since = "5.0.0-a1")
    public static Boolean disabled(ButtonBuilder button) {
        return button.disabled();
    }

    @Property.Setter("disabled")
    public static void setDisabled(ButtonBuilder button, Boolean disabled) {
        button.disabled(Boolean.TRUE.equals(disabled));
    }
}
