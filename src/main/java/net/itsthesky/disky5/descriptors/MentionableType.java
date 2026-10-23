package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.IMentionable;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

// IMentionable extends ISnowflake, so SnowflakeType is listed as parent purely to order
// ClassInfo registration (snowflake before mentionable), not for property inheritance.
@SkriptType(name = "mentionable", jda = IMentionable.class, parent = SnowflakeType.class)
public final class MentionableType {

    @Property("mention tag")
    @Documentation(
            name = "Mention Tag",
            description = {
                "The mention (ping) tag of a Discord entity (user, member, role, channel, emoji).",
                "Sending this string in a message renders as a clickable mention."
            },
            examples = "reply with \"Hello %mention tag of event-member%!\"",
            since = "5.0.0-a1"
    )
    public static String mentionTag(IMentionable mentionable) {
        return mentionable.getAsMention();
    }
}
