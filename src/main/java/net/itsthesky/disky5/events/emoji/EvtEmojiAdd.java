package net.itsthesky.disky5.events.emoji;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.emoji.EmojiAddedEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Emoji Add",
        patterns = {"[discord] emoji add[ed]"},
        jda = EmojiAddedEvent.class
)
@Documentation(
        name = "Emoji Add",
        description = {
                "Fired when a new emoji is added to a guild.",
                "Requires the `emoji` cache flag and the `guild expressions` intent."
        },
        examples = {
                "on emoji added:",
                "\tsend \"New emoji added in %event-guild%: %event-emote%\" to console"
        },
        since = "5.0.0-a1"
)
public final class EvtEmojiAdd {

    @EventValue
    public static Guild guild(EmojiAddedEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static RichCustomEmoji emoji(EmojiAddedEvent event) {
        return event.getEmoji();
    }

    @EventExpression("managed [state]")
    public static Boolean managed(EmojiAddedEvent event) {
        return event.isManaged();
    }
}
