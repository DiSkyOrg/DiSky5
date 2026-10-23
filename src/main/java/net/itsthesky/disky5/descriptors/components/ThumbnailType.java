package net.itsthesky.disky5.descriptors.components;

import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.ThumbnailBuilder;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "thumbnail", jda = ThumbnailBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Thumbnail",
    description = "A small image shown as the accessory of a section, from a url or a file upload.",
    since = "5.0.0-a1")
public final class ThumbnailType {

    private ThumbnailType() {}

    @New("[a] new thumbnail [(with|from) [the] (url|file|source)] %string/fileupload%")
    @Documentation(
        name = "New Thumbnail",
        description = "Creates a thumbnail from an image url or a file upload.",
        examples = {"set {_t} to a new thumbnail from url \"https://example.com/logo.png\"",
            "set accessory of {_section} to {_t}"},
        since = "5.0.0-a1")
    public static ThumbnailBuilder newThumbnail(Object source) {
        return new ThumbnailBuilder(source);
    }

    @Property("description")
    public static @Nullable String description(ThumbnailBuilder thumbnail) {
        return thumbnail.description();
    }

    @Property.Setter("description")
    public static void setDescription(ThumbnailBuilder thumbnail, @Nullable String description) {
        thumbnail.description(description);
    }

    @Property("spoiler")
    public static Boolean spoiler(ThumbnailBuilder thumbnail) {
        return thumbnail.spoiler();
    }

    @Property.Setter("spoiler")
    public static void setSpoiler(ThumbnailBuilder thumbnail, Boolean spoiler) {
        thumbnail.spoiler(Boolean.TRUE.equals(spoiler));
    }
}
