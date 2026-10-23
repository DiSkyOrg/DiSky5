package net.itsthesky.disky5.events.guild;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Join Event",
        patterns = {"[discord] bot join guild"},
        jda = GuildJoinEvent.class
)
@Documentation(
        name = "Guild Join Event",
        description = {
                "Fired when the bot joins a guild. Use this to set up initial configurations or welcome messages."
        },
        examples = {
                "on bot join guild:",
                "\tbroadcast \"Bot joined %event-guild%!\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildJoin {

    @EventValue
    public static Guild guild(GuildJoinEvent event) {
        return event.getGuild();
    }
}
