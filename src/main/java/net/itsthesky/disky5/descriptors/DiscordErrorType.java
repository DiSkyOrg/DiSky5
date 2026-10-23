package net.itsthesky.disky5.descriptors;

import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.DiscordError;

@SkriptType(name = "discorderror", jda = DiscordError.class)
public final class DiscordErrorType {

    @Property("error code")
    @Documentation(
            name = "Discord Error Code",
            description = "The Discord error code (e.g. 10008 = Unknown Message), or -1 for a non-Discord error.",
            examples = "if error code of {_err} is 50013:",
            since = "5.0.0-a1"
    )
    public static long errorCode(DiscordError error) {
        return error.getCode();
    }

    @Property("error meaning")
    @Documentation(
            name = "Discord Error Meaning",
            description = "The human-readable meaning of the error (JDA's, e.g. \"Missing Permissions\").",
            examples = "send \"%error meaning of {_err}%\"",
            since = "5.0.0-a1"
    )
    public static String errorMeaning(DiscordError error) {
        return error.getMeaning();
    }

    @Property("error message")
    @Documentation(
            name = "Discord Error Message",
            description = "The full error message.",
            examples = "send \"%error message of {_err}%\"",
            since = "5.0.0-a1"
    )
    public static String errorMessage(DiscordError error) {
        return error.getMessage();
    }
}
