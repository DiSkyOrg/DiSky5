package net.itsthesky.disky5.api.emotes;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.FileUpload;

import java.util.ArrayList;
import java.util.List;

public final class StickerBuilder {

    public static final int MAX_FILE_BYTES = 512 * 1024;
    private static final int MAX_TAGS_LENGTH = 200;

    private final Guild guild;
    private String name;
    private final Object file;
    private String description = "";
    private final List<String> tags = new ArrayList<>();

    public StickerBuilder(String name, Object file, Guild guild) {
        this.name = name;
        this.file = file;
        this.guild = guild;
    }

    public RestAction<GuildSticker> build() {
        if (name == null || name.length() < 2 || name.length() > 30)
            throw new IllegalStateException("the sticker name must be 2 to 30 characters long, '" + name + "' is "
                + (name == null ? 0 : name.length()));
        String desc = description == null ? "" : description;
        if (desc.length() == 1 || desc.length() > 100)
            throw new IllegalStateException("the sticker description must be empty or 2 to 100 characters long, "
                + "it is " + desc.length());
        if (tags.isEmpty())
            throw new IllegalStateException("a sticker needs at least one tag (a word or an emoji Discord "
                + "suggests it for)");
        for (String tag : tags)
            if (tag.isBlank() || tag.contains(","))
                throw new IllegalStateException("the sticker tag '" + tag + "' is invalid: a tag cannot be "
                    + "empty or contain a comma");
        int tagsLength = String.join(",", tags).length();
        if (tagsLength > MAX_TAGS_LENGTH)
            throw new IllegalStateException("the sticker tags take " + tagsLength + " characters once joined, "
                + "the maximum is " + MAX_TAGS_LENGTH);
        byte[] data = ImageSource.read(file, MAX_FILE_BYTES, "sticker file");
        ImageSource.Format format = ImageSource.detect(data);
        String extension = format == null ? null : switch (format) {
            case PNG -> "png";
            case GIF -> "gif";
            case LOTTIE -> "json";
            case JPEG, WEBP -> null;
        };
        if (extension == null)
            throw new IllegalStateException("a sticker must be a PNG, APNG, GIF or Lottie JSON file"
                + (format == null ? "" : ", not a " + format));
        return guild.createSticker(name, desc, FileUpload.fromData(data, "sticker." + extension), List.copyOf(tags));
    }

    public Guild guild() { return guild; }
    public String name() { return name; }
    public void name(String name) { this.name = name; }
    public String description() { return description; }
    public void description(String description) { this.description = description; }
    public List<String> tags() { return tags; }

    @Override
    public String toString() {
        return "sticker builder " + name;
    }
}
