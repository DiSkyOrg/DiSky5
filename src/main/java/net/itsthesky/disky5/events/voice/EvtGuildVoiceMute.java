package net.itsthesky.disky5.events.voice;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMuteEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Voice Mute Event",
        patterns = {"[discord] guild [voice] mute[d]"},
        jda = GuildVoiceMuteEvent.class
)
@Documentation(
        name = "Guild Voice Mute Event",
        description = {
                "Fired when a member's mute status changes for any reason (self-mute or server-mute).",
                "Use `event-boolean` to check whether the member is now muted (true) or unmuted (false).",
                "To listen only to server-initiated mutes, use the Guild Voice Server Mute Event instead."
        },
        examples = {
                "on guild voice mute:",
                "\tif event-boolean is true:",
                "\t\tbroadcast \"%event-member% is now muted in %event-guild%\"",
                "\telse:",
                "\t\tbroadcast \"%event-member% is now unmuted in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildVoiceMute {

    @EventValue
    public static Boolean muted(GuildVoiceMuteEvent event) {
        return event.isMuted();
    }

    @EventValue
    public static Member member(GuildVoiceMuteEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(GuildVoiceMuteEvent event) {
        return event.getGuild();
    }
}
