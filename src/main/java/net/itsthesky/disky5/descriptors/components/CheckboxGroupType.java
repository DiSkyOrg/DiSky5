package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.checkboxgroup.CheckboxGroupOption;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.CheckboxGroupBuilder;
import net.itsthesky.disky5.api.components.SelectOptionBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "checkboxgroup", jda = CheckboxGroupBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Checkbox Group",
    description = {"A modal field where the user ticks any number of options. Read the ticks in "
        + "'on modal received' with 'values of checkbox group with id'.",
        "Its options are dropdown options (the emoji is ignored). It only lives inside a modal."},
    since = "5.0.0-a1")
public final class CheckboxGroupType {

    private CheckboxGroupType() {}

    @New("[a] new checkbox group [with] [the] id %string% (named|labeled|with [the] label) %string%")
    @Documentation(
        name = "New Checkbox Group",
        description = "Creates a checkbox group for a modal. Add options to it before showing the modal.",
        examples = {"set {_c} to a new checkbox group with id \"toppings\" labeled \"Toppings\"",
            "add a new option with value \"cheese\" named \"Cheese\" to options of {_c}",
            "set max range of {_c} to 2",
            "add {_c} to components of {_modal}"},
        since = "5.0.0-a1")
    public static CheckboxGroupBuilder newCheckboxGroup(String id, String label) {
        CheckboxGroupBuilder group = new CheckboxGroupBuilder(id);
        group.label(label);
        return group;
    }

    @Property("custom id")
    public static String customId(CheckboxGroupBuilder group) {
        return group.id();
    }

    @Property.Setter("custom id")
    public static void setCustomId(CheckboxGroupBuilder group, String id) {
        group.id(id);
    }

    @Property("label")
    public static @Nullable String label(CheckboxGroupBuilder group) {
        return group.label();
    }

    @Property.Setter("label")
    public static void setLabel(CheckboxGroupBuilder group, @Nullable String label) {
        group.label(label);
    }

    @Property("description")
    public static @Nullable String description(CheckboxGroupBuilder group) {
        return group.description();
    }

    @Property.Setter("description")
    public static void setDescription(CheckboxGroupBuilder group, @Nullable String description) {
        group.description(description);
    }

    @Property("options")
    public static List<String> options(CheckboxGroupBuilder group) {
        return group.options().stream().map(CheckboxGroupOption::getValue).toList();
    }

    @Property.Setter("options")
    public static void setOptions(CheckboxGroupBuilder group, SelectOptionBuilder[] options) {
        group.options().clear();
        for (SelectOptionBuilder option : options)
            group.options().add(option.buildCheckbox());
    }

    @Property.Adder("options")
    public static void addOption(CheckboxGroupBuilder group, SelectOptionBuilder option) {
        group.options().add(option.buildCheckbox());
    }

    @Property.Remover("options")
    public static void removeOption(CheckboxGroupBuilder group, String value) {
        group.options().removeIf(option -> option.getValue().equals(value));
    }

    @Property("min range")
    public static Number minRange(CheckboxGroupBuilder group) {
        return group.minValues();
    }

    @Property.Setter("min range")
    public static void setMinRange(CheckboxGroupBuilder group, Number value) {
        group.minValues(value.intValue());
    }

    @Property("max range")
    public static Number maxRange(CheckboxGroupBuilder group) {
        return group.maxValues();
    }

    @Property.Setter("max range")
    public static void setMaxRange(CheckboxGroupBuilder group, Number value) {
        group.maxValues(value.intValue());
    }

    @Property("required")
    public static Boolean required(CheckboxGroupBuilder group) {
        return group.required();
    }

    @Property.Setter("required")
    public static void setRequired(CheckboxGroupBuilder group, Boolean required) {
        group.required(Boolean.TRUE.equals(required));
    }
}
