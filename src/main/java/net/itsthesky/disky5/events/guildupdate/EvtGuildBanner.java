package net.itsthesky.disky5.events.guildupdate;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBannerEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Guild Banner Event",
        patterns = {"[discord] guild banner (change|update)"},
        jda = GuildUpdateBannerEvent.class
)
@Documentation(
        name = "Guild Banner Event",
        description = "Fired when the banner of a guild changes. Can be used to get the old/new banner URL, the author and the guild.",
        examples = {
            "on guild banner change:",
            "\tbroadcast \"Guild %event-guild% changed banner from %past banner% to %current banner%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildBanner {

    @EventValue
    public static Guild guild(GuildUpdateBannerEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable String banner(GuildUpdateBannerEvent event) {
        return event.getNewBannerUrl();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable String pastBanner(GuildUpdateBannerEvent event) {
        return event.getOldBannerUrl();
    }
}
