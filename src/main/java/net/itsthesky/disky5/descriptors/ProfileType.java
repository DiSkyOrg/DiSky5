package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Color;
import net.dv8tion.jda.api.entities.User;
import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.annotations.ConditionType;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "userprofile", jda = User.Profile.class)
public final class ProfileType {

    @Property("profile banner")
    @Documentation(
            name = "Profile Banner",
            description = {
                "The profile banner URL. If the user doesn't have a custom banner, this returns none.",
                "Use the 'profile color' expression to get the accent color instead in that case."
            },
            examples = "await set {_banner} to profile banner of (profile of event-user)",
            since = "5.0.0-a1"
    )
    public static @Nullable String profileBanner(User.Profile profile) {
        return profile.getBannerUrl();
    }

    @Property("profile color")
    @Documentation(
            name = "Profile Color",
            description = {
                "The profile accent color. If the user has a custom banner, this may return none.",
                "Use the 'profile banner' expression to get the banner URL instead in that case."
            },
            examples = "await set {_color} to profile color of (profile of event-user)",
            since = "5.0.0-a1"
    )
    public static @Nullable Color profileColor(User.Profile profile) {
        return Conversions.color(new java.awt.Color(profile.getAccentColorRaw()));
    }

    // Explicit patterns, not a PropertyCondition: that would derive the subject from the class
    // name (%profiles%), not the registered type name (userprofile).
    @Condition(property = "[a] [custom] banner", type = ConditionType.HAVE)
    @Documentation(
            name = "Profile has a Banner",
            description = "Whether the profile has a custom banner set. If it does not, the "
                    + "'profile color' expression returns the accent color instead.",
            examples = "if (profile of event-user) has a custom banner:",
            since = "5.0.0-a1"
    )
    public static boolean hasBanner(User.Profile profile) {
        return profile.getBannerUrl() != null;
    }
}
