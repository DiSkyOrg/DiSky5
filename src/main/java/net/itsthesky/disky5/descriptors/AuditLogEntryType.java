package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@SkriptType(name = "logentry", jda = AuditLogEntry.class)
public final class AuditLogEntryType {

    @Property("logged user")
    @Documentation(
            name = "Logged User",
            description = "The user who triggered this audit log entry, or null if it is unknown.",
            examples = "reply with logged user of {_entry}",
            since = "5.0.0-a1"
    )
    public static @Nullable User loggedUser(AuditLogEntry e) {
        return e.getUser();
    }

    @Property("log action type")
    @Documentation(
            name = "Log Action Type",
            description = "The action type of the audit log entry (e.g. ban, kick, channel create...).",
            examples = "reply with log action type of {_entry}",
            since = "5.0.0-a1"
    )
    public static String logActionType(AuditLogEntry e) {
        return e.getType().name().toLowerCase(Locale.ROOT).replace('_', ' ');
    }

    @Property("log reason")
    @Documentation(
            name = "Log Reason",
            description = "The reason of the audit log entry, or null if none was provided.",
            examples = "reply with log reason of {_entry}",
            since = "5.0.0-a1"
    )
    public static @Nullable String logReason(AuditLogEntry e) {
        return e.getReason();
    }

    @Property(value = "guild", shared = true)
    public static Guild guild(AuditLogEntry e) {
        return e.getGuild();
    }
}
