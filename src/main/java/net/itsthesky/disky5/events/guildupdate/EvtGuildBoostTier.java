package net.itsthesky.disky5.events.guildupdate;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Boost Tier Update",
        patterns = {"[discord] guild boost tier (change|update)"},
        jda = GuildUpdateBoostTierEvent.class
)
@Documentation(
        name = "Guild Boost Tier Update",
        description = "Fired when the boost tier of a guild changes. Can be used to get the old/new tier, and the guild.",
        examples = {
            "on guild boost tier change:",
            "\tbroadcast \"Guild %event-guild% boost tier changed from %past boost tier% to %current boost tier%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildBoostTier {

    @EventValue
    public static Guild guild(GuildUpdateBoostTierEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static String boostTier(GuildUpdateBoostTierEvent event) {
        return event.getNewBoostTier().name();
    }

    @EventValue(time = EventTime.PAST)
    public static String pastBoostTier(GuildUpdateBoostTierEvent event) {
        return event.getOldBoostTier().name();
    }
}
