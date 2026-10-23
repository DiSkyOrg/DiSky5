package net.itsthesky.disky5.events.sticker;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateDescriptionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Sticker Description Update",
        patterns = {"[discord] guild sticker description update[d]"},
        jda = GuildStickerUpdateDescriptionEvent.class
)
@Documentation(
        name = "Guild Sticker Description Update",
        description = {
                "Fired when the Description of a sticker is updated.",
                "",
                "For this event to work, the `sticker` cache flag and `guild expressions` intent are required."
        },
        examples = {
                "on guild sticker description update:",
                "\tbroadcast \"Description of %event-sticker% changed! %past event-string% -> %event-string%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildStickerDescriptionUpdate {

    @EventValue
    public static String newDescription(GuildStickerUpdateDescriptionEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static String oldDescription(GuildStickerUpdateDescriptionEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(GuildStickerUpdateDescriptionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static GuildSticker sticker(GuildStickerUpdateDescriptionEvent event) {
        return event.getSticker();
    }
}
