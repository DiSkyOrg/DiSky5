package net.itsthesky.disky5.events.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Bot Leave Event",
        patterns = {"bot [guild] (leave|left)"},
        jda = GuildLeaveEvent.class
)
@Documentation(
        name = "Bot Leave Event",
        description = {
                "Fired when the bot leaves a guild/server.",
                "This event can be used for cleanup operations or logging when the bot is removed from a server,",
                "either by being kicked, the server being deleted, or the bot owner removing it manually."
        },
        examples = {
                "on bot leave:",
                "\tbroadcast \"Bot left the server: %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtBotLeave {

    @EventValue
    public static Guild guild(GuildLeaveEvent event) {
        return event.getGuild();
    }
}
