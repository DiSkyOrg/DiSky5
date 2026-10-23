package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Member Nickname Event",
        patterns = {"[guild] member nickname (change|update)"},
        jda = GuildMemberUpdateNicknameEvent.class
)
@Documentation(
        name = "Member Nickname Event",
        description = "Fired when a member changes their nickname in a guild.",
        examples = {
            "on member nickname change:",
            "\tbroadcast \"%event-member% changed their nickname from '%past member nickname%' to '%member nickname%'\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberNickname {

    @EventValue
    public static Guild guild(GuildMemberUpdateNicknameEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildMemberUpdateNicknameEvent event) {
        return event.getMember();
    }

    @EventExpression("[member] nickname")
    public static @Nullable String nickname(GuildMemberUpdateNicknameEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) [member] nickname")
    public static @Nullable String oldNickname(GuildMemberUpdateNicknameEvent event) {
        return event.getOldValue();
    }
}
