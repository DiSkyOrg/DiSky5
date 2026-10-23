package net.itsthesky.disky5.descriptors.emotes;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.emotes.StickerBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SkriptType(name = "stickerbuilder", jda = StickerBuilder.class)
@Documentation(
    name = "Sticker Builder",
    description = {"A guild sticker that does not exist yet: a name, a file, a description and at least one tag. "
        + "Create it with 'the build result of', under 'await'.",
        "Nothing is read or sent until then: the file is downloaded or read from disk when the sticker is built."},
    since = "5.0.0-b1")
public final class StickerBuilderType {

    private StickerBuilderType() {}

    @New("[a] new sticker builder named %string% with [the] (image|file) %string/fileupload% in [the] [guild] %guild%")
    @Documentation(
        name = "New Sticker Builder",
        description = {"Creates a sticker builder. The file is an http(s) URL, a local file path or a file upload: "
            + "a PNG, APNG, GIF or Lottie JSON of 512 KB at most, ideally 320x320 pixels (Lottie stickers are "
            + "reserved to verified and partnered servers).",
            "The name is 2 to 30 characters. Add at least one tag before building it. The bot needs the "
                + "Create Expressions permission."},
        examples = {"set {_b} to a new sticker builder named \"Party Blob\" with image \"https://example.com/blob.png\" in event-guild",
            "set description of {_b} to \"A blob that parties\"",
            "add \"tada\" to sticker tags of {_b}",
            "await set {_sticker} to the built guildsticker from {_b}"},
        since = "5.0.0-b1")
    public static StickerBuilder newStickerBuilder(String name, Object file, Guild guild) {
        return new StickerBuilder(name, file, guild);
    }

    @Build
    @Documentation(
        name = "Built Sticker",
        description = {"Validates the builder, loads its file and creates the sticker on Discord. Requires 'await'.",
            "Building the same builder twice creates two stickers."},
        examples = {"await set {_sticker} to the built guildsticker from {_b}"},
        since = "5.0.0-b1")
    public static RestAction<GuildSticker> build(StickerBuilder builder) {
        return builder.build();
    }

    @Property(value = "name", shared = true)
    public static String name(StickerBuilder builder) {
        return builder.name();
    }

    @Property.Setter(value = "name", shared = true)
    public static void setName(StickerBuilder builder, String name) {
        builder.name(name);
    }

    @Property("description")
    @Documentation(
        name = "Sticker Builder Description",
        description = "The description of the sticker being built: empty (the default) or 2 to 100 characters.",
        examples = {"set description of {_b} to \"A blob that parties\""},
        since = "5.0.0-b1")
    public static String description(StickerBuilder builder) {
        return builder.description();
    }

    @Property.Setter("description")
    public static void setDescription(StickerBuilder builder, @Nullable String description) {
        builder.description(description == null ? "" : description);
    }

    @Property("sticker tags")
    @Documentation(
        name = "Sticker Builder Tags",
        description = {"The tags of the sticker being built: the words or emojis Discord suggests it for. "
            + "At least one is required, 200 characters at most once joined, and a tag cannot contain a comma."},
        examples = {"add \"tada\" and \"party\" to sticker tags of {_b}",
            "remove \"party\" from sticker tags of {_b}"},
        since = "5.0.0-b1")
    public static List<String> tags(StickerBuilder builder) {
        return List.copyOf(builder.tags());
    }

    @Property.Setter("sticker tags")
    public static void setTags(StickerBuilder builder, String[] tags) {
        builder.tags().clear();
        builder.tags().addAll(Arrays.asList(tags));
    }

    @Property.Adder("sticker tags")
    public static void addTag(StickerBuilder builder, String tag) {
        if (!builder.tags().contains(tag))
            builder.tags().add(tag);
    }

    @Property.Remover("sticker tags")
    public static void removeTag(StickerBuilder builder, String tag) {
        builder.tags().remove(tag);
    }
}
