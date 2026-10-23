package net.itsthesky.disky5.events.thread;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberLeaveEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Thread Leave Event",
        patterns = {"[discord] thread leave"},
        jda = ThreadMemberLeaveEvent.class
)
@Documentation(
        name = "Thread Leave Event",
        description = {
            "Fired when a member leaves a thread, either by leaving itself or by a moderator ",
            "can be used to get the thread, the guild and the member."
        },
        examples = {"on thread leave:"},
        since = "5.0.0-a1"
)
public final class EvtThreadLeave {

    @EventValue
    public static Channel channel(ThreadMemberLeaveEvent event) {
        return event.getThread();
    }

    @EventValue
    public static Member member(ThreadMemberLeaveEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(ThreadMemberLeaveEvent event) {
        return event.getGuild();
    }
}
