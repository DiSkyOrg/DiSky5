package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Member Join Event",
        patterns = {"member join[ed] [guild]"},
        jda = GuildMemberJoinEvent.class
)
@Documentation(
        name = "Member Join Event",
        description = "Fired when a member joins a guild.",
        examples = {
            "on member join:",
            "\tbroadcast \"Welcome %event-member% to %event-guild%!\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberJoin {

    @EventValue
    public static Guild guild(GuildMemberJoinEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildMemberJoinEvent event) {
        return event.getMember();
    }
}
