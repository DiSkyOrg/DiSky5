package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Color;
import net.dv8tion.jda.api.entities.RoleColors;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "rolecolor", jda = RoleColors.class)
public final class RoleColorsType {

    @Property("primary color")
    @Documentation(
            name = "Primary Role Color",
            description = "The primary color of a role's colors.",
            examples = "reply with primary color of (colors of event-role)",
            since = "5.0.0-a1"
    )
    public static @Nullable Color primaryColor(RoleColors colors) {
        return Conversions.color(colors.getPrimary());
    }

    @Property("secondary color")
    @Documentation(
            name = "Secondary Role Color",
            description = "The secondary color of a role's colors, or null if the role has no gradient.",
            examples = "reply with secondary color of (colors of event-role)",
            since = "5.0.0-a1"
    )
    public static @Nullable Color secondaryColor(RoleColors colors) {
        return Conversions.color(colors.getSecondary());
    }

    @Property("tertiary color")
    @Documentation(
            name = "Tertiary Role Color",
            description = "The tertiary color of a role's colors, or null if the role is not holographic.",
            examples = "reply with tertiary color of (colors of event-role)",
            since = "5.0.0-a1"
    )
    public static @Nullable Color tertiaryColor(RoleColors colors) {
        return Conversions.color(colors.getTertiary());
    }
}
