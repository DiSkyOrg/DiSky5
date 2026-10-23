package net.itsthesky.disky5.events.sticker;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.events.sticker.GuildStickerAddedEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Sticker Add",
        patterns = {"[discord] guild sticker add[ed]"},
        jda = GuildStickerAddedEvent.class
)
@Documentation(
        name = "Guild Sticker Add",
        description = {
                "Fired when someone or something adds a sticker to a guild.",
                "",
                "For this event to work, the `sticker` cache flag and `guild expressions` intent are required."
        },
        examples = {
                "on guild sticker add:",
                "\tsend \"New sticker added in %event-guild%: %event-sticker%\" to console"
        },
        since = "5.0.0-a1"
)
public final class EvtGuildStickerAdd {

    @EventValue
    public static Guild guild(GuildStickerAddedEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static GuildSticker sticker(GuildStickerAddedEvent event) {
        return event.getSticker();
    }

    @EventExpression("sticker id")
    public static String stickerId(GuildStickerAddedEvent event) {
        return event.getSticker().getId();
    }
}
