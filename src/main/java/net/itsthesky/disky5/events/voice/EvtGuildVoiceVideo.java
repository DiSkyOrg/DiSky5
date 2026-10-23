package net.itsthesky.disky5.events.voice;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceVideoEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Voice Video Event",
        patterns = {"[discord] guild [voice] video[ing]"},
        jda = GuildVoiceVideoEvent.class
)
@Documentation(
        name = "Guild Voice Video Event",
        description = {
                "Fired when a member starts or stops its camera in a voice channel. Can be used to track moderation actions in voice channels.",
                "You may use `event-boolean` to check if the state is true (user started its camera) or false (user stopped its camera).",
                "",
                "!!! warning \"This **DOES NOT** include streams! Use the `Guild Voice Stream Event` instead.\""
        },
        examples = {
                "on guild voice video:",
                "\tif event-boolean is true:",
                "\t\tbroadcast \"%event-member% started video in %event-guild%\"",
                "\telse:",
                "\t\tbroadcast \"%event-member% stopped video in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildVoiceVideo {

    @EventValue
    public static Boolean sendingVideo(GuildVoiceVideoEvent event) {
        return event.isSendingVideo();
    }

    @EventValue
    public static Member member(GuildVoiceVideoEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(GuildVoiceVideoEvent event) {
        return event.getGuild();
    }
}
