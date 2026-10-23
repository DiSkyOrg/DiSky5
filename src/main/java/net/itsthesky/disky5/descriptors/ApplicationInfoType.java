package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.User;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "applicationinfo", jda = ApplicationInfo.class)
public final class ApplicationInfoType {

    @Property("application description")
    @Documentation(
            name = "Application Description",
            description = "The description of the application.",
            examples = "reply with application description of {_app}",
            since = "5.0.0-a1"
    )
    public static @Nullable String applicationDescription(ApplicationInfo a) {
        return a.getDescription();
    }

    @Property("application owner")
    @Documentation(
            name = "Application Owner",
            description = "The user who owns the application.",
            examples = "reply with application owner of {_app}",
            since = "5.0.0-a1"
    )
    public static @Nullable User applicationOwner(ApplicationInfo a) {
        return a.getOwner();
    }

    @Property("privacy policy url")
    @Documentation(
            name = "Application Privacy Policy URL",
            description = "The privacy policy URL of the application, or null if it has none.",
            examples = "reply with privacy policy url of {_app}",
            since = "5.0.0-a1"
    )
    public static @Nullable String privacyPolicyUrl(ApplicationInfo a) {
        return a.getPrivacyPolicyUrl();
    }

    @Property("terms of service url")
    @Documentation(
            name = "Application Terms Of Service URL",
            description = "The terms of service URL of the application, or null if it has none.",
            examples = "reply with terms of service url of {_app}",
            since = "5.0.0-a1"
    )
    public static @Nullable String termsOfServiceUrl(ApplicationInfo a) {
        return a.getTermsOfServiceUrl();
    }

    @Property(value = "name", shared = true)
    public static String discordName(ApplicationInfo a) {
        return a.getName();
    }

    @Property(value = "avatar url", shared = true)
    public static String avatarUrl(ApplicationInfo a) {
        return a.getIconUrl();
    }
}
