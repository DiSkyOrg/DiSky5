package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.itsthesky.disky5.annotations.EnumAliases;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.Map;

@SkriptEnum(name = "buttonstyle", jda = ButtonStyle.class)
@Documentation(
    name = "Button Style",
    description = "The look of a button: primary (blurple), secondary (grey), success (green), danger (red), "
        + "link (opens a url) or premium (opens a sku purchase).",
    examples = "set {_b} to a new danger button with id \"delete\" named \"Delete\"",
    since = "5.0.0-a1")
public final class ButtonStyleType {

    @EnumAliases
    public static Map<ButtonStyle, String[]> aliases() {
        return Map.of(
            ButtonStyle.PRIMARY, new String[]{"blurple"},
            ButtonStyle.SECONDARY, new String[]{"grey", "gray"},
            ButtonStyle.SUCCESS, new String[]{"green"},
            ButtonStyle.DANGER, new String[]{"red"});
    }
}
