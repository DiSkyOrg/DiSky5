package net.itsthesky.disky5.events.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Ready Event",
        patterns = {"guild (ready|load[ed])"},
        jda = GuildReadyEvent.class
)
@Documentation(
        name = "Guild Ready Event",
        description = {
                "Fired when a guild is fully loaded and all its data is accessible.",
                "This event occurs for each guild the bot is connected to when starting up.",
                "It's fired before the global Ready event and indicates that guild-specific data",
                "like members, channels, and roles have been loaded and are available for use."
        },
        examples = {
                "on guild ready:",
                "\tbroadcast \"Guild %event-guild% is now fully loaded!\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildReady {

    @EventValue
    public static Guild guild(GuildReadyEvent event) {
        return event.getGuild();
    }
}
