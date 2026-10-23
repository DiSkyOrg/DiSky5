package net.itsthesky.disky5.descriptors.components;

import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.Emojis;
import net.itsthesky.disky5.api.components.SelectOptionBuilder;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "selectoption", jda = SelectOptionBuilder.class)
@Documentation(
    name = "Dropdown Option",
    description = {"One choice of a string dropdown. Its value is what 'selected values' returns; its "
        + "label is what the user sees (defaults to the value)."},
    since = "5.0.0-a1")
public final class SelectOptionType {

    private SelectOptionType() {}

    @New("[a] new [drop[( |-)]down] option [with] [the] value %string% [(named|with [the] label) %-string%] [with [the] description %-string%] [with [the] emoji %-emote/string%]")
    @Documentation(
        name = "New Dropdown Option",
        description = "Creates an option to add to the options of a string dropdown.",
        examples = {"set {_o} to a new option with value \"red\" named \"Red\" with emoji \"🔴\"",
            "add {_o} to options of {_dropdown}"},
        since = "5.0.0-a1")
    public static SelectOptionBuilder newOption(String value, @Nullable String label, @Nullable String description,
                                               @Nullable Object emoji) {
        SelectOptionBuilder option = new SelectOptionBuilder(value);
        option.label(label);
        option.description(description);
        option.emoji(Emojis.from(emoji));
        return option;
    }

    @Property("label")
    public static @Nullable String label(SelectOptionBuilder option) {
        return option.label();
    }

    @Property.Setter("label")
    public static void setLabel(SelectOptionBuilder option, @Nullable String label) {
        option.label(label);
    }

    @Property("option value")
    @Documentation(
        name = "Dropdown Option Value",
        description = "The value sent back when this option is selected.",
        examples = {"set option value of {_o} to \"red\""},
        since = "5.0.0-a1")
    public static String value(SelectOptionBuilder option) {
        return option.value();
    }

    @Property.Setter("option value")
    public static void setValue(SelectOptionBuilder option, String value) {
        option.value(value);
    }

    @Property("description")
    public static @Nullable String description(SelectOptionBuilder option) {
        return option.description();
    }

    @Property.Setter("description")
    public static void setDescription(SelectOptionBuilder option, @Nullable String description) {
        option.description(description);
    }

    @Property("emoji")
    public static @Nullable String emoji(SelectOptionBuilder option) {
        return option.emoji() == null ? null : option.emoji().getFormatted();
    }

    @Property.Setter("emoji")
    public static void setEmoji(SelectOptionBuilder option, @Nullable Object emoji) {
        option.emoji(Emojis.from(emoji));
    }

    @Property("default option")
    @Documentation(
        name = "Dropdown Option Default",
        description = "Whether this option is selected when the dropdown is first shown.",
        examples = {"set default option of {_o} to true"},
        since = "5.0.0-a1")
    public static Boolean defaultOption(SelectOptionBuilder option) {
        return option.defaultOption();
    }

    @Property.Setter("default option")
    public static void setDefaultOption(SelectOptionBuilder option, Boolean value) {
        option.defaultOption(Boolean.TRUE.equals(value));
    }
}
