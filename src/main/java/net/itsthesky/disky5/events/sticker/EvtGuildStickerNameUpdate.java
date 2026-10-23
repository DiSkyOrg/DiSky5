package net.itsthesky.disky5.events.sticker;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateNameEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Sticker Name Update",
        patterns = {"[discord] guild sticker name update[d]"},
        jda = GuildStickerUpdateNameEvent.class
)
@Documentation(
        name = "Guild Sticker Name Update",
        description = {
                "Fired when the Name of a sticker is updated.",
                "",
                "For this event to work, the `sticker` cache flag and `guild expressions` intent are required."
        },
        examples = {
                "on guild sticker name update:",
                "\tbroadcast \"Name of %event-sticker% changed! %past event-string% -> %event-string%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildStickerNameUpdate {

    @EventValue
    public static String newName(GuildStickerUpdateNameEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static String oldName(GuildStickerUpdateNameEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(GuildStickerUpdateNameEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static GuildSticker sticker(GuildStickerUpdateNameEvent event) {
        return event.getSticker();
    }
}
