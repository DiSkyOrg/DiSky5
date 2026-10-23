package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Member Self Mute Event",
        patterns = {"member [self] [un]mute[d]"},
        jda = GuildVoiceSelfMuteEvent.class
)
@Documentation(
        name = "Member Self Mute Event",
        description = "Fired when a member mutes or unmutes themselves in a voice channel.",
        examples = {
            "on member mute:",
            "\tif member mute state is true:",
            "\t\tbroadcast \"%event-member% muted themselves\"",
            "\telse:",
            "\t\tbroadcast \"%event-member% unmuted themselves\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberSelfMute {

    @EventValue
    public static Guild guild(GuildVoiceSelfMuteEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildVoiceSelfMuteEvent event) {
        return event.getMember();
    }

    @EventExpression("[member] mute[d] state")
    public static Boolean muteState(GuildVoiceSelfMuteEvent event) {
        return event.isSelfMuted();
    }

    @EventExpression("(old|past|previous) [member] mute[d] state")
    public static Boolean oldMuteState(GuildVoiceSelfMuteEvent event) {
        return !event.isSelfMuted();
    }
}
