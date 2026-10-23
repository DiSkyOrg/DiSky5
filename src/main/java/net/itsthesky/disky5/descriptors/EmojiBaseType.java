package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptType(name = "emoji", jda = Emoji.class)
@Documentation(
    name = "Emoji",
    description = {"Any Discord emoji: a unicode emoji or a custom one (an 'emote').",
        "It displays as the emoji itself, or as <:name:id> for a custom emoji, so it renders when sent."},
    since = "5.0.0-a2")
public final class EmojiBaseType {
}
