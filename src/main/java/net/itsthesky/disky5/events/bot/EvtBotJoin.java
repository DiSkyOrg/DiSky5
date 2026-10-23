package net.itsthesky.disky5.events.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Bot Join Event",
        patterns = {"bot [guild] join[ed]"},
        jda = GuildJoinEvent.class
)
@Documentation(
        name = "Bot Join Event",
        description = {
                "Fired when the bot joins a new guild/server.",
                "This event is useful for setting up initial configurations when the bot enters a new server,",
                "such as adding default roles, sending welcome messages, or initializing server-specific settings."
        },
        examples = {
                "on bot join:",
                "\tbroadcast \"Bot joined a new server: %event-guild%!\""
        },
        since = "5.0.0-a1"
)
public final class EvtBotJoin {

    @EventValue
    public static Guild guild(GuildJoinEvent event) {
        return event.getGuild();
    }
}
