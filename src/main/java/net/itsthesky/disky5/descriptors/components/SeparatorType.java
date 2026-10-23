package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.separator.Separator;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.SeparatorBuilder;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "separator", jda = SeparatorBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Separator",
    description = "A components V2 gap between two components, with or without a visible line.",
    since = "5.0.0-a1")
public final class SeparatorType {

    private SeparatorType() {}

    @New("[a] new [%-separatorspacing%] (separator|divider)")
    @Documentation(
        name = "New Separator",
        description = "Creates a separator with a visible line and the given spacing (small by default).",
        examples = {"add a new separator to components of {_container}",
            "add a new large separator to components of {_container}"},
        since = "5.0.0-a1")
    public static SeparatorBuilder newSeparator(@Nullable Separator.Spacing spacing) {
        return new SeparatorBuilder(spacing == null ? Separator.Spacing.SMALL : spacing);
    }

    @Property("divider")
    @Documentation(
        name = "Separator Divider",
        description = "Whether the separator draws a line, or only leaves an empty gap.",
        examples = {"set divider of {_s} to false"},
        since = "5.0.0-a1")
    public static Boolean divider(SeparatorBuilder separator) {
        return separator.divider();
    }

    @Property.Setter("divider")
    public static void setDivider(SeparatorBuilder separator, Boolean divider) {
        separator.divider(Boolean.TRUE.equals(divider));
    }

    @Property("spacing")
    @Documentation(
        name = "Separator Spacing",
        description = "How much room the separator takes: small or large.",
        examples = {"set spacing of {_s} to large"},
        since = "5.0.0-a1")
    public static Separator.Spacing spacing(SeparatorBuilder separator) {
        return separator.spacing();
    }

    @Property.Setter("spacing")
    public static void setSpacing(SeparatorBuilder separator, Separator.Spacing spacing) {
        separator.spacing(spacing);
    }
}
