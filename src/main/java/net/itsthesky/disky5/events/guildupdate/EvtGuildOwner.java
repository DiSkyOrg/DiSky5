package net.itsthesky.disky5.events.guildupdate;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Guild Owner Event",
        patterns = {"[discord] guild owner (change|update)"},
        jda = GuildUpdateOwnerEvent.class
)
@Documentation(
        name = "Guild Owner Event",
        description = "Fired when the owner of a guild changes. Can be used to get the old/new owner, the author and the guild.",
        examples = {
            "on guild owner change:",
            "\tbroadcast \"Guild %event-guild% owner changed from %past owner% to %current owner%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildOwner {

    @EventValue
    public static Guild guild(GuildUpdateOwnerEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable Member owner(GuildUpdateOwnerEvent event) {
        return event.getNewOwner();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable Member pastOwner(GuildUpdateOwnerEvent event) {
        return event.getOldOwner();
    }
}
