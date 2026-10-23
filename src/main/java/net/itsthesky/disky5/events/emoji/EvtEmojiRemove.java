package net.itsthesky.disky5.events.emoji;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.emoji.EmojiRemovedEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Emoji Remove",
        patterns = {"[discord] emoji remove[d]"},
        jda = EmojiRemovedEvent.class
)
@Documentation(
        name = "Emoji Remove",
        description = {
                "Fired when an emoji is removed from a guild.",
                "Requires the `emoji` cache flag and the `guild expressions` intent."
        },
        examples = {
                "on emoji removed:",
                "\tsend \"Emoji %event-emote% was removed from %event-guild%\" to console"
        },
        since = "5.0.0-a1"
)
public final class EvtEmojiRemove {

    @EventValue
    public static Guild guild(EmojiRemovedEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static RichCustomEmoji emoji(EmojiRemovedEvent event) {
        return event.getEmoji();
    }

    @EventExpression("managed [state]")
    public static Boolean managed(EmojiRemovedEvent event) {
        return event.isManaged();
    }
}
