package net.itsthesky.disky5.descriptors.components;

import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.CheckboxBuilder;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "checkbox", jda = CheckboxBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Checkbox",
    description = {"A single yes/no box of a modal. Read it in 'on modal received' with "
        + "'value of checkbox with id' (true or false).",
        "It only lives inside a modal; its label is the text next to the box."},
    since = "5.0.0-a1")
public final class CheckboxType {

    private CheckboxType() {}

    @New("[a] new checkbox [with] [the] id %string% (named|labeled|with [the] label) %string%")
    @Documentation(
        name = "New Checkbox",
        description = "Creates a checkbox for a modal, unticked by default.",
        examples = {"set {_c} to a new checkbox with id \"agree\" labeled \"I agree to the rules\"",
            "add {_c} to components of {_modal}"},
        since = "5.0.0-a1")
    public static CheckboxBuilder newCheckbox(String id, String label) {
        CheckboxBuilder checkbox = new CheckboxBuilder(id);
        checkbox.label(label);
        return checkbox;
    }

    @Property("custom id")
    public static String customId(CheckboxBuilder checkbox) {
        return checkbox.id();
    }

    @Property.Setter("custom id")
    public static void setCustomId(CheckboxBuilder checkbox, String id) {
        checkbox.id(id);
    }

    @Property("label")
    public static @Nullable String label(CheckboxBuilder checkbox) {
        return checkbox.label();
    }

    @Property.Setter("label")
    public static void setLabel(CheckboxBuilder checkbox, @Nullable String label) {
        checkbox.label(label);
    }

    @Property("description")
    public static @Nullable String description(CheckboxBuilder checkbox) {
        return checkbox.description();
    }

    @Property.Setter("description")
    public static void setDescription(CheckboxBuilder checkbox, @Nullable String description) {
        checkbox.description(description);
    }

    @Property("checked")
    @Documentation(
        name = "Checkbox Checked",
        description = "Whether the box is ticked when the modal opens.",
        examples = {"set checked of {_c} to true"},
        since = "5.0.0-a1")
    public static Boolean checked(CheckboxBuilder checkbox) {
        return checkbox.checked();
    }

    @Property.Setter("checked")
    public static void setChecked(CheckboxBuilder checkbox, Boolean checked) {
        checkbox.checked(Boolean.TRUE.equals(checked));
    }
}
