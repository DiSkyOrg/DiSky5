package net.itsthesky.disky5.events.guildupdate;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Name Event",
        patterns = {"[discord] guild name (update|change)"},
        jda = GuildUpdateNameEvent.class
)
@Documentation(
        name = "Guild Name Event",
        description = "Fired when the name of a guild is changed. Can be used to get the old/new name, the author and the guild.",
        examples = {
            "on guild name change:",
            "\tbroadcast \"Guild name changed from '%past guild name%' to '%current guild name%'\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildName {

    @EventValue
    public static Guild guild(GuildUpdateNameEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static String guildName(GuildUpdateNameEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static String pastGuildName(GuildUpdateNameEvent event) {
        return event.getOldValue();
    }
}
