package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.List;

@SkriptType(name = "guildsticker", jda = GuildSticker.class, parent = StickerType.class)
public final class GuildStickerType {

    @Property(value = "guild", shared = true)
    public static Guild guild(GuildSticker sticker) {
        return sticker.getGuild();
    }

    @Property("description")
    @Documentation(
        name = "Guild Sticker Description",
        description = "The description of a guild sticker, empty when it has none.",
        examples = {"send \"%description of {_sticker}%\" to console"},
        since = "5.0.0-b1")
    public static String description(GuildSticker sticker) {
        return sticker.getDescription();
    }

    @Property("sticker tags")
    @Documentation(
        name = "Guild Sticker Tags",
        description = "The tags of a guild sticker: the words or emojis Discord suggests it for.",
        examples = {"send \"%sticker tags of {_sticker}%\" to console"},
        since = "5.0.0-b1")
    public static List<String> tags(GuildSticker sticker) {
        return List.copyOf(sticker.getTags());
    }
}
