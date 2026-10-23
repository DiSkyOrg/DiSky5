package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.components.separator.Separator;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptEnum(name = "separatorspacing", jda = Separator.Spacing.class)
@Documentation(
    name = "Separator Spacing",
    description = "How much room a separator takes: small or large.",
    examples = "set {_s} to a new large separator",
    since = "5.0.0-a1")
public final class SeparatorSpacingType {
}
