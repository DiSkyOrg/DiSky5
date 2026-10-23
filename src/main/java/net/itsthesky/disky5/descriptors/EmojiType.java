package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.annotations.ConditionType;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.List;

@SkriptType(name = "emote", jda = CustomEmoji.class)
public final class EmojiType {

    @Property("emote image url")
    @Documentation(
            name = "Emote Image URL",
            description = "The URL of the custom emoji's image.",
            examples = "reply with emote image url of {_emote}",
            since = "5.0.0-a1"
    )
    public static String imageUrl(CustomEmoji emote) {
        return emote.getImageUrl();
    }

    @Property("allowed roles")
    @Documentation(
            name = "Emote Allowed Roles",
            description = "The roles allowed to use a guild emote. Empty means everyone can use it.",
            examples = "send \"%size of allowed roles of {_emote}% role(s) can use it\" to console",
            since = "5.0.0-b1"
    )
    public static List<Role> allowedRoles(CustomEmoji emote) {
        return emote instanceof RichCustomEmoji rich ? rich.getRoles() : List.of();
    }

    @Property(value = "name", shared = true)
    public static String discordName(CustomEmoji emote) {
        return emote.getName();
    }

    // Explicit patterns, not a PropertyCondition: that would derive the subject from the class
    // name (%emojis%), not the registered type name (emote).
    @Condition(property = "animated", type = ConditionType.BE)
    @Documentation(
            name = "Emote is Animated",
            description = "Whether the custom emoji is animated.",
            examples = "if {_emote} is animated:",
            since = "5.0.0-a1"
    )
    public static boolean animated(CustomEmoji emote) {
        return emote.isAnimated();
    }
}
