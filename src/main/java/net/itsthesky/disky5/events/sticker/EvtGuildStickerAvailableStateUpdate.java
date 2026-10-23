package net.itsthesky.disky5.events.sticker;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateAvailableEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Sticker Available State Update",
        patterns = {"[discord] guild sticker available state update[d]"},
        jda = GuildStickerUpdateAvailableEvent.class
)
@Documentation(
        name = "Guild Sticker Available State Update",
        description = {
                "Fired when the Available State of a sticker is updated.",
                "",
                "For this event to work, the `sticker` cache flag and `guild expressions` intent are required."
        },
        examples = {
                "on guild sticker available state update:",
                "\tbroadcast \"Available State of %event-sticker% changed! %past event-boolean% -> %event-boolean%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildStickerAvailableStateUpdate {

    @EventValue
    public static Boolean newAvailableState(GuildStickerUpdateAvailableEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static Boolean oldAvailableState(GuildStickerUpdateAvailableEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(GuildStickerUpdateAvailableEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static GuildSticker sticker(GuildStickerUpdateAvailableEvent event) {
        return event.getSticker();
    }
}
