package net.itsthesky.disky5.events.sticker;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.events.sticker.GuildStickerRemovedEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Sticker Remove",
        patterns = {"[discord] guild sticker remove[d]"},
        jda = GuildStickerRemovedEvent.class
)
@Documentation(
        name = "Guild Sticker Remove",
        description = {
                "Fired when someone or something removes a sticker from a guild.",
                "",
                "For this event to work, the `sticker` cache flag and `guild expressions` intent are required."
        },
        examples = {
                "on guild sticker remove:",
                "\tbroadcast \"Sticker %event-sticker% was removed from %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildStickerRemove {

    @EventValue
    public static Guild guild(GuildStickerRemovedEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static GuildSticker sticker(GuildStickerRemovedEvent event) {
        return event.getSticker();
    }

    @EventExpression("sticker id")
    public static String stickerId(GuildStickerRemovedEvent event) {
        return event.getSticker().getId();
    }
}
