package net.itsthesky.disky5.events.guildupdate;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSplashEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Guild Splash Event",
        patterns = {"[discord] guild splash (change|update)"},
        jda = GuildUpdateSplashEvent.class
)
@Documentation(
        name = "Guild Splash Event",
        description = "Fired when the splash image of a guild changes. Can be used to get the old/new splash URL, the author and the guild.",
        examples = {
            "on guild splash change:",
            "\tbroadcast \"Guild %event-guild% splash changed from %past splash% to %current splash%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildSplash {

    @EventValue
    public static Guild guild(GuildUpdateSplashEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable String splash(GuildUpdateSplashEvent event) {
        return event.getNewSplashUrl();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable String pastSplash(GuildUpdateSplashEvent event) {
        return event.getOldSplashUrl();
    }
}
