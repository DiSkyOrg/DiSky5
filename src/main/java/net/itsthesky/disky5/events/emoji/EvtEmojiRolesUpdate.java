package net.itsthesky.disky5.events.emoji;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.emoji.update.EmojiUpdateRolesEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

import java.util.List;

@SkriptEvent(
        name = "Emoji Roles Update",
        patterns = {"[discord] emoji roles update[d]"},
        jda = EmojiUpdateRolesEvent.class
)
@Documentation(
        name = "Emoji Roles Update",
        description = {
                "Fired when the roles of an emoji are updated.",
                "Requires the `emoji` cache flag and the `guild expressions` intent."
        },
        examples = {
                "on emoji roles updated:",
                "\tsend \"Emoji %event-emote% roles updated in %event-guild%\" to console"
        },
        since = "5.0.0-a1"
)
public final class EvtEmojiRolesUpdate {

    @EventValue
    public static Guild guild(EmojiUpdateRolesEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static RichCustomEmoji emoji(EmojiUpdateRolesEvent event) {
        return event.getEmoji();
    }

    @EventExpression("[the] [new] [emoji] roles")
    public static List<Role> newRoles(EmojiUpdateRolesEvent event) {
        return event.getNewValue();
    }

    @EventExpression("[the] (old|previous) [emoji] roles")
    public static List<Role> oldRoles(EmojiUpdateRolesEvent event) {
        return event.getOldValue();
    }
}
