package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "forumtag", jda = ForumTag.class)
public final class ForumTagType {

    @Property("tag emote")
    @Documentation(
            name = "Forum Tag Emote",
            description = "The emoji of a forum tag, or null if the tag has no emoji.",
            examples = "reply with tag emote of {_tag}",
            since = "5.0.0-a1"
    )
    public static @Nullable EmojiUnion tagEmote(ForumTag tag) {
        return tag.getEmoji();
    }

    @Property(value = "name", shared = true)
    public static String discordName(ForumTag tag) {
        return tag.getName();
    }
}
