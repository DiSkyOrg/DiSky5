package net.itsthesky.disky5.events.voice;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Voice Stream Event",
        patterns = {"[discord] guild [voice] stream[ing]"},
        jda = GuildVoiceStreamEvent.class
)
@Documentation(
        name = "Guild Voice Stream Event",
        description = {
                "Fired when a member starts or stops streaming in a voice channel. Can be used to track moderation actions in voice channels.",
                "You may use `event-boolean` to check if the state is true (user started streaming) or false (user stopped streaming).",
                "",
                "!!! warning \"This **DOES NOT** include camera! Use the `GuildVoice Video Event` for that.\""
        },
        examples = {
                "on guild voice stream:",
                "\tif event-boolean is true:",
                "\t\tbroadcast \"%event-member% started streaming in %event-guild%\"",
                "\telse:",
                "\t\tbroadcast \"%event-member% stopped streaming in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildVoiceStream {

    @EventValue
    public static Boolean streaming(GuildVoiceStreamEvent event) {
        return event.isStream();
    }

    @EventValue
    public static Member member(GuildVoiceStreamEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(GuildVoiceStreamEvent event) {
        return event.getGuild();
    }
}
