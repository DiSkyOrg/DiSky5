package net.itsthesky.disky5.actions;

import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.docs.Documentation;

public final class ThreadActions {

    private ThreadActions() {}

    @Effect(value = "archive [the] [thread] %threadchannel%", reason = true)
    @Documentation(
        name = "Archive Thread",
        description = "Archives a thread, closing it to new messages until someone posts again.",
        examples = {"archive event-threadchannel"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> archiveThread(ThreadChannel thread) {
        return thread.getManager().setArchived(true);
    }

    @Effect(value = "unarchive [the] [thread] %threadchannel%", reason = true)
    @Documentation(
        name = "Unarchive Thread",
        description = "Re-opens an archived thread.",
        examples = {"unarchive event-threadchannel"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> unarchiveThread(ThreadChannel thread) {
        return thread.getManager().setArchived(false);
    }

    @Effect(value = "lock [the] [thread] %threadchannel%", reason = true)
    @Documentation(
        name = "Lock Thread",
        description = "Locks a thread: only moderators can unarchive it afterwards.",
        examples = {"lock event-threadchannel"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> lockThread(ThreadChannel thread) {
        return thread.getManager().setLocked(true);
    }

    @Effect(value = "unlock [the] [thread] %threadchannel%", reason = true)
    @Documentation(
        name = "Unlock Thread",
        description = "Unlocks a locked thread.",
        examples = {"unlock event-threadchannel"},
        since = "5.0.0-a1")
    public static AuditableRestAction<Void> unlockThread(ThreadChannel thread) {
        return thread.getManager().setLocked(false);
    }
}
