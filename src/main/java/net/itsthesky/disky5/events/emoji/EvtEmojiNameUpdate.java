package net.itsthesky.disky5.events.emoji;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.emoji.update.EmojiUpdateNameEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Emoji Name Update",
        patterns = {"[discord] emoji name update[d]"},
        jda = EmojiUpdateNameEvent.class
)
@Documentation(
        name = "Emoji Name Update",
        description = {
                "Fired when the name of an emoji is updated.",
                "Requires the `emoji` cache flag and the `guild expressions` intent."
        },
        examples = {
                "on emoji name updated:",
                "\tsend \"Emoji %event-emote% was renamed in %event-guild%: %past event-string% -> %event-string%\" to console"
        },
        since = "5.0.0-a1"
)
public final class EvtEmojiNameUpdate {

    @EventValue
    public static Guild guild(EmojiUpdateNameEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static RichCustomEmoji emoji(EmojiUpdateNameEvent event) {
        return event.getEmoji();
    }

    @EventValue
    public static String name(EmojiUpdateNameEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static String oldName(EmojiUpdateNameEvent event) {
        return event.getOldValue();
    }
}
