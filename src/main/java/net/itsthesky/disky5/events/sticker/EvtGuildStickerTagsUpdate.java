package net.itsthesky.disky5.events.sticker;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateTagsEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Guild Sticker Tags Update",
        patterns = {"[discord] guild sticker tags update[d]"},
        jda = GuildStickerUpdateTagsEvent.class
)
@Documentation(
        name = "Guild Sticker Tags Update",
        description = {
                "Fired when the tags of a sticker is updated.",
                "",
                "For this event to work, the `sticker` cache flag and `guild expressions` intent are required."
        },
        examples = {
                "on guild sticker tags update:",
                "\tbroadcast \"Tags of %event-sticker% changed! %past event-strings% -> %event-strings%\""
        },
        since = "5.0.0-a1"
)
public final class EvtGuildStickerTagsUpdate {

    // `sticker` is mandatory: a bare "tags" is Skript's own ExprTagsOfType.
    @EventExpression("[the] [new] sticker tags")
    public static String[] newTags(GuildStickerUpdateTagsEvent event) {
        return event.getNewValue().toArray(new String[0]);
    }

    @EventExpression("[the] (old|previous) sticker tags")
    public static String[] oldTags(GuildStickerUpdateTagsEvent event) {
        return event.getOldValue().toArray(new String[0]);
    }

    @EventValue
    public static Guild guild(GuildStickerUpdateTagsEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static GuildSticker sticker(GuildStickerUpdateTagsEvent event) {
        return event.getSticker();
    }
}
