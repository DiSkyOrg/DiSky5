package net.itsthesky.disky5.events.guild;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Ban Event",
        patterns = {"[discord] guild [user] ban"},
        jda = GuildBanEvent.class
)
@Documentation(
        name = "Guild Ban Event",
        description = {
                "Fired when a user is banned from a guild. A member doesn't exist here because the member is not in the guild anymore! ",
                "Can be used to get the banned user, the author and the guild."
        },
        examples = {
                "on guild ban:",
                "\tbroadcast \"%event-user% was banned from %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildBan {

    @EventValue
    public static User user(GuildBanEvent event) {
        return event.getUser();
    }

    @EventValue
    public static Guild guild(GuildBanEvent event) {
        return event.getGuild();
    }
}
