package net.itsthesky.disky5.events.voice;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Voice Deafen Event",
        patterns = {"[discord] guild [voice] deafen[ed]"},
        jda = GuildVoiceGuildDeafenEvent.class
)
@Documentation(
        name = "Guild Voice Deafen Event",
        description = {
                "Fired when a member is deafened or undeafened by the guild. Can be used to track moderation actions in voice channels."
        },
        examples = {
                "on guild voice deafen:",
                "\tif event-boolean is true:",
                "\t\tbroadcast \"%event-member% was deafened in %event-guild%\"",
                "\telse:",
                "\t\tbroadcast \"%event-member% was undeafened in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildVoiceDeafen {

    @EventValue
    public static Boolean deafened(GuildVoiceGuildDeafenEvent event) {
        return event.isGuildDeafened();
    }

    @EventValue
    public static Member member(GuildVoiceGuildDeafenEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(GuildVoiceGuildDeafenEvent event) {
        return event.getGuild();
    }
}
