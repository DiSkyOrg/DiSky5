package net.itsthesky.disky5.events.voice;

import ch.njol.skript.util.Date;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceRequestToSpeakEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

@SkriptEvent(
        name = "Guild Voice Request To Speak Event",
        patterns = {"[discord] guild [voice] request to speak"},
        jda = GuildVoiceRequestToSpeakEvent.class
)
@Documentation(
        name = "Guild Voice Request To Speak Event",
        description = {
                "Fired when a member requests to speak in a stage channel. Can be used to track moderation actions in voice channels.",
                "You may use `event-boolean` to check if the state is true (user requested to speak) or false (user cancelled the request)."
        },
        examples = {
                "on guild voice request to speak:",
                "\tif event-boolean is true:",
                "\t\tbroadcast \"%event-member% requested to speak in %event-guild%\"",
                "\telse:",
                "\t\tbroadcast \"%event-member% cancelled their request to speak in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildVoiceRequestToSpeak {

    @EventValue
    public static Boolean requested(GuildVoiceRequestToSpeakEvent event) {
        return event.getNewTime() != null;
    }

    @EventValue
    public static Member member(GuildVoiceRequestToSpeakEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(GuildVoiceRequestToSpeakEvent event) {
        return event.getGuild();
    }

    @EventExpression("request to speak")
    public static @Nullable Date requestToSpeak(GuildVoiceRequestToSpeakEvent event) {
        return convertDateTime(event.getNewTime());
    }

    @EventExpression("(old|past|previous) request to speak")
    public static @Nullable Date pastRequestToSpeak(GuildVoiceRequestToSpeakEvent event) {
        return convertDateTime(event.getOldTime());
    }

    private static @Nullable Date convertDateTime(@Nullable OffsetDateTime dateTime) {
        if (dateTime == null)
            return null;
        return new Date(dateTime.toInstant().toEpochMilli());
    }
}
