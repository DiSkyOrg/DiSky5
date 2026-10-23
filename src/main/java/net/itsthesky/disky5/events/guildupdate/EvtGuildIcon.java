package net.itsthesky.disky5.events.guildupdate;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Guild Icon Event",
        patterns = {"[discord] guild icon (change|update)"},
        jda = GuildUpdateIconEvent.class
)
@Documentation(
        name = "Guild Icon Event",
        description = "Fired when the icon of a guild changes. Can be used to get the old/new icon URL, the author and the guild.",
        examples = {
            "on guild icon change:",
            "\tbroadcast \"Guild %event-guild% changed icon from %past icon% to %current icon%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildIcon {

    @EventValue
    public static Guild guild(GuildUpdateIconEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable String icon(GuildUpdateIconEvent event) {
        return event.getNewIconUrl();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable String pastIcon(GuildUpdateIconEvent event) {
        return event.getOldIconUrl();
    }
}
