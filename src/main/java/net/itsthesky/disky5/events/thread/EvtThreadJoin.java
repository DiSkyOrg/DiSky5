package net.itsthesky.disky5.events.thread;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberJoinEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Thread Join Event",
        patterns = {"[discord] thread join"},
        jda = ThreadMemberJoinEvent.class
)
@Documentation(
        name = "Thread Join Event",
        description = {
            "Fired when a member joins a tread, either by joining itself or by a moderator ",
            "can be used to get the thread, the guild and the member."
        },
        examples = {"on thread join:"},
        since = "5.0.0-a1"
)
public final class EvtThreadJoin {

    @EventValue
    public static Channel channel(ThreadMemberJoinEvent event) {
        return event.getThread();
    }

    @EventValue
    public static Member member(ThreadMemberJoinEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(ThreadMemberJoinEvent event) {
        return event.getGuild();
    }
}
