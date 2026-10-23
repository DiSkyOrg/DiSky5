package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.radiogroup.RadioGroupOption;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.RadioGroupBuilder;
import net.itsthesky.disky5.api.components.SelectOptionBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "radiogroup", jda = RadioGroupBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Radio Group",
    description = {"A modal field where the user picks exactly one of several options. Read the pick in "
        + "'on modal received' with 'value of radio group with id'.",
        "Its options are dropdown options (the emoji is ignored). It only lives inside a modal."},
    since = "5.0.0-a1")
public final class RadioGroupType {

    private RadioGroupType() {}

    @New("[a] new radio group [with] [the] id %string% (named|labeled|with [the] label) %string%")
    @Documentation(
        name = "New Radio Group",
        description = "Creates a radio group for a modal. Add options to it before showing the modal.",
        examples = {"set {_r} to a new radio group with id \"size\" labeled \"Size\"",
            "add a new option with value \"s\" named \"Small\" to options of {_r}",
            "add a new option with value \"l\" named \"Large\" to options of {_r}",
            "add {_r} to components of {_modal}"},
        since = "5.0.0-a1")
    public static RadioGroupBuilder newRadioGroup(String id, String label) {
        RadioGroupBuilder group = new RadioGroupBuilder(id);
        group.label(label);
        return group;
    }

    @Property("custom id")
    public static String customId(RadioGroupBuilder group) {
        return group.id();
    }

    @Property.Setter("custom id")
    public static void setCustomId(RadioGroupBuilder group, String id) {
        group.id(id);
    }

    @Property("label")
    public static @Nullable String label(RadioGroupBuilder group) {
        return group.label();
    }

    @Property.Setter("label")
    public static void setLabel(RadioGroupBuilder group, @Nullable String label) {
        group.label(label);
    }

    @Property("description")
    public static @Nullable String description(RadioGroupBuilder group) {
        return group.description();
    }

    @Property.Setter("description")
    public static void setDescription(RadioGroupBuilder group, @Nullable String description) {
        group.description(description);
    }

    @Property("options")
    public static List<String> options(RadioGroupBuilder group) {
        return group.options().stream().map(RadioGroupOption::getValue).toList();
    }

    @Property.Setter("options")
    public static void setOptions(RadioGroupBuilder group, SelectOptionBuilder[] options) {
        group.options().clear();
        for (SelectOptionBuilder option : options)
            group.options().add(option.buildRadio());
    }

    @Property.Adder("options")
    public static void addOption(RadioGroupBuilder group, SelectOptionBuilder option) {
        group.options().add(option.buildRadio());
    }

    @Property.Remover("options")
    public static void removeOption(RadioGroupBuilder group, String value) {
        group.options().removeIf(option -> option.getValue().equals(value));
    }

    @Property("required")
    public static Boolean required(RadioGroupBuilder group) {
        return group.required();
    }

    @Property.Setter("required")
    public static void setRequired(RadioGroupBuilder group, Boolean required) {
        group.required(Boolean.TRUE.equals(required));
    }
}
