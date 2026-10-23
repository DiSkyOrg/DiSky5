package net.itsthesky.disky5.events.guild;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Unban Event",
        patterns = {"[discord] guild [user] unban"},
        jda = GuildUnbanEvent.class
)
@Documentation(
        name = "Guild Unban Event",
        description = {
                "Fired when a user is unbanned from a guild. Can be used to get the unbanned user, the author and the guild."
        },
        examples = {
                "on guild unban:",
                "\tbroadcast \"%event-user% was unbanned from %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildUnban {

    @EventValue
    public static User user(GuildUnbanEvent event) {
        return event.getUser();
    }

    @EventValue
    public static Guild guild(GuildUnbanEvent event) {
        return event.getGuild();
    }
}
