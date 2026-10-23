package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Member Leave Event",
        patterns = {"member (leave|left) [guild]"},
        jda = GuildMemberRemoveEvent.class
)
@Documentation(
        name = "Member Leave Event",
        description = "Fired when a member is removed from a guild either by leaving or being punished. "
                + "Use the ban/kick event instead to check the exact reason.",
        examples = {
            "on member leave:",
            "\tbroadcast \"%event-member% has left %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberLeave {

    @EventValue
    public static Guild guild(GuildMemberRemoveEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable Member member(GuildMemberRemoveEvent event) {
        return event.getMember();
    }

    @EventValue
    public static User user(GuildMemberRemoveEvent event) {
        return event.getUser();
    }
}
