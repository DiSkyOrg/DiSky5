package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateAvatarEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Member Avatar Event",
        patterns = {"[guild] member avatar (change|update)"},
        jda = GuildMemberUpdateAvatarEvent.class
)
@Documentation(
        name = "Member Avatar Event",
        description = "Fired when a member changes their server-specific avatar.",
        examples = {
            "on member avatar change:",
            "\tbroadcast \"%event-member% changed their server avatar. New URL: %member avatar url%\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberAvatar {

    @EventValue
    public static Guild guild(GuildMemberUpdateAvatarEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildMemberUpdateAvatarEvent event) {
        return event.getMember();
    }

    @EventExpression("[member] avatar url")
    public static @Nullable String avatarUrl(GuildMemberUpdateAvatarEvent event) {
        return event.getNewAvatarUrl();
    }

    @EventExpression("(old|past|previous) [member] avatar url")
    public static @Nullable String oldAvatarUrl(GuildMemberUpdateAvatarEvent event) {
        return event.getOldAvatarUrl();
    }
}
