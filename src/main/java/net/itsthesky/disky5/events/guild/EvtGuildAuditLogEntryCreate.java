package net.itsthesky.disky5.events.guild;

import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildAuditLogEntryCreateEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Log Entry Create Event",
        patterns = {"[discord] guild log [entry] create"},
        jda = GuildAuditLogEntryCreateEvent.class
)
@Documentation(
        name = "Guild Log Entry Create Event",
        description = {
                "Fired when a new log entry is created in a guild. Can be used to monitor administrative actions within a guild."
        },
        examples = {
                "on guild log entry create:",
                "\tbroadcast \"New audit log entry created in %event-guild% for action type %event-entry's type%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildAuditLogEntryCreate {

    @EventValue
    public static AuditLogEntry entry(GuildAuditLogEntryCreateEvent event) {
        return event.getEntry();
    }

    @EventValue
    public static Guild guild(GuildAuditLogEntryCreateEvent event) {
        return event.getGuild();
    }

    // TODO: the acting member (retrieveMemberById on the entry's user id) is intentionally omitted;
    // async-event-value codegen for a REST-retrieved value isn't available yet.
}
