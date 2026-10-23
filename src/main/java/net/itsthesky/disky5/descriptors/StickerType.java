package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.sticker.Sticker;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;

@SkriptType(name = "sticker", jda = Sticker.class)
public final class StickerType {

    @Property(value = "name", shared = true)
    public static String discordName(Sticker sticker) {
        return sticker.getName();
    }

    @Property(value = "avatar url", shared = true)
    public static String avatarUrl(Sticker sticker) {
        return sticker.getIconUrl();
    }
}
