package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.selections.SelectOption;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.SelectOptionBuilder;
import net.itsthesky.disky5.api.components.StringSelectBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SkriptType(name = "dropdown", jda = StringSelectBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Dropdown",
    description = {"A select menu whose choices are options you define. Selecting fires 'on dropdown click' "
        + "with 'selected values'; in a modal, its values are read with 'value of dropdown with id'.",
        "A dropdown takes a whole row on its own."},
    since = "5.0.0-a1")
public final class StringSelectType {

    private StringSelectType() {}

    @New("[a] new [string] drop[( |-)]down [with] [the] id %string%")
    @Documentation(
        name = "New Dropdown",
        description = "Creates a dropdown with the given id. Add options to it before sending it.",
        examples = {"set {_d} to a new dropdown with id \"color\"",
            "set placeholder of {_d} to \"Pick a color\"",
            "add a new option with value \"red\" named \"Red\" to options of {_d}",
            "add a new option with value \"blue\" named \"Blue\" to options of {_d}",
            "add {_d} to components of {_message}"},
        since = "5.0.0-a1")
    public static StringSelectBuilder newDropdown(String id) {
        return new StringSelectBuilder(id);
    }

    @Property("custom id")
    public static String customId(StringSelectBuilder menu) {
        return menu.id();
    }

    @Property.Setter("custom id")
    public static void setCustomId(StringSelectBuilder menu, String id) {
        menu.id(id);
    }

    @Property("options")
    @Documentation(
        name = "Dropdown Options",
        description = {"The choices of a string dropdown, read back as their values.",
            "Add dropdown options to it; remove one by its value."},
        examples = {"add a new option with value \"red\" named \"Red\" to options of {_d}",
            "remove \"red\" from options of {_d}"},
        since = "5.0.0-a1")
    public static List<String> options(StringSelectBuilder menu) {
        return menu.options().stream().map(SelectOption::getValue).toList();
    }

    @Property.Setter("options")
    public static void setOptions(StringSelectBuilder menu, SelectOptionBuilder[] options) {
        menu.options().clear();
        for (SelectOptionBuilder option : options)
            menu.options().add(option.build());
    }

    @Property.Adder("options")
    public static void addOption(StringSelectBuilder menu, SelectOptionBuilder option) {
        menu.options().add(option.build());
    }

    @Property.Remover("options")
    public static void removeOption(StringSelectBuilder menu, String value) {
        menu.options().removeIf(option -> option.getValue().equals(value));
    }

    @Property("default values")
    @Documentation(
        name = "Dropdown Default Values",
        description = {"What is selected when the dropdown is first shown: option values for a string "
            + "dropdown, users, roles or channels for an entity dropdown."},
        examples = {"set default values of {_d} to \"red\"",
            "add event-member to default values of {_entities}"},
        since = "5.0.0-a1")
    public static List<String> defaultValues(StringSelectBuilder menu) {
        return menu.options().stream().filter(SelectOption::isDefault).map(SelectOption::getValue).toList();
    }

    @Property.Setter("default values")
    public static void setDefaultValues(StringSelectBuilder menu, String[] values) {
        List<String> selected = Arrays.asList(values);
        menu.options().replaceAll(option -> option.withDefault(selected.contains(option.getValue())));
    }

    @Property.Adder("default values")
    public static void addDefaultValue(StringSelectBuilder menu, String value) {
        menu.options().replaceAll(option -> option.getValue().equals(value) ? option.withDefault(true) : option);
    }

    @Property.Remover("default values")
    public static void removeDefaultValue(StringSelectBuilder menu, String value) {
        menu.options().replaceAll(option -> option.getValue().equals(value) ? option.withDefault(false) : option);
    }

    @Property("placeholder")
    @Documentation(
        name = "Component Placeholder",
        description = "The greyed text shown in a dropdown or a text input while nothing is selected or typed.",
        examples = {"set placeholder of {_d} to \"Pick a color\""},
        since = "5.0.0-a1")
    public static @Nullable String placeholder(StringSelectBuilder menu) {
        return menu.placeholder();
    }

    @Property.Setter("placeholder")
    public static void setPlaceholder(StringSelectBuilder menu, @Nullable String placeholder) {
        menu.placeholder(placeholder);
    }

    @Property("min range")
    @Documentation(
        name = "Component Minimum Range",
        description = "The least the user must provide: options selected in a dropdown, characters typed "
            + "in a text input.",
        examples = {"set min range of {_d} to 1",
            "set min range of {_input} to 3"},
        since = "5.0.0-a1")
    public static Number minRange(StringSelectBuilder menu) {
        return menu.minValues();
    }

    @Property.Setter("min range")
    public static void setMinRange(StringSelectBuilder menu, Number value) {
        menu.minValues(value.intValue());
    }

    @Property("max range")
    @Documentation(
        name = "Component Maximum Range",
        description = "The most the user may provide: options selected in a dropdown, characters typed "
            + "in a text input.",
        examples = {"set max range of {_d} to 3",
            "set max range of {_input} to 100"},
        since = "5.0.0-a1")
    public static Number maxRange(StringSelectBuilder menu) {
        return menu.maxValues();
    }

    @Property.Setter("max range")
    public static void setMaxRange(StringSelectBuilder menu, Number value) {
        menu.maxValues(value.intValue());
    }

    @Property("disabled")
    public static Boolean disabled(StringSelectBuilder menu) {
        return menu.disabled();
    }

    @Property.Setter("disabled")
    public static void setDisabled(StringSelectBuilder menu, Boolean disabled) {
        menu.disabled(Boolean.TRUE.equals(disabled));
    }

    @Property("required")
    @Documentation(
        name = "Component Required",
        description = "Whether the user must fill this component before submitting the modal it sits in. "
            + "Ignored on a message.",
        examples = {"set required of {_input} to false"},
        since = "5.0.0-a1")
    public static @Nullable Boolean required(StringSelectBuilder menu) {
        return menu.required();
    }

    @Property.Setter("required")
    public static void setRequired(StringSelectBuilder menu, @Nullable Boolean required) {
        menu.required(required);
    }

    @Property("label")
    public static @Nullable String label(StringSelectBuilder menu) {
        return menu.label();
    }

    @Property.Setter("label")
    public static void setLabel(StringSelectBuilder menu, @Nullable String label) {
        menu.label(label);
    }

    @Property("description")
    public static @Nullable String description(StringSelectBuilder menu) {
        return menu.description();
    }

    @Property.Setter("description")
    public static void setDescription(StringSelectBuilder menu, @Nullable String description) {
        menu.description(description);
    }
}
