package net.itsthesky.disky5.events.voice;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Voice Server Mute Event",
        patterns = {"[discord] guild [voice] server mute[d]"},
        jda = GuildVoiceGuildMuteEvent.class
)
@Documentation(
        name = "Guild Voice Server Mute Event",
        description = {
                "Fired when a member is server-muted or server-unmuted by a guild moderator.",
                "Unlike the Guild Voice Mute Event, this ONLY fires for guild-initiated (server) mutes, not self-mutes.",
                "Use `event-boolean` to check whether the member is now server-muted (true) or server-unmuted (false)."
        },
        examples = {
                "on guild voice server mute:",
                "\tif event-boolean is true:",
                "\t\tbroadcast \"%event-member% was server-muted in %event-guild%\"",
                "\telse:",
                "\t\tbroadcast \"%event-member% was server-unmuted in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildVoiceServerMute {

    @EventValue
    public static Boolean guildMuted(GuildVoiceGuildMuteEvent event) {
        return event.isGuildMuted();
    }

    @EventValue
    public static Member member(GuildVoiceGuildMuteEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(GuildVoiceGuildMuteEvent event) {
        return event.getGuild();
    }
}
