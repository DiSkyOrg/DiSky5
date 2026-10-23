package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.MediaGalleryBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "mediagallery", jda = MediaGalleryBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Media Gallery",
    description = {"A components V2 grid of up to 10 images or videos, each from a url or a file upload."},
    since = "5.0.0-a1")
public final class MediaGalleryType {

    private MediaGalleryType() {}

    @New("[a] new media gallery [with [the] (items|files|images) %-fileuploads/strings%]")
    @Documentation(
        name = "New Media Gallery",
        description = "Creates a media gallery, optionally filled with urls and file uploads.",
        examples = {"set {_g} to a new media gallery with images \"https://example.com/a.png\" and {_upload}",
            "add {_g} to components of {_container}"},
        since = "5.0.0-a1")
    public static MediaGalleryBuilder newMediaGallery(Object @Nullable [] items) {
        MediaGalleryBuilder gallery = new MediaGalleryBuilder();
        if (items != null)
            for (Object item : items)
                gallery.add(item);
        return gallery;
    }

    @Property("gallery items")
    @Documentation(
        name = "Media Gallery Items",
        description = "The images and videos of the gallery, read back as their urls. Add a url or a file upload.",
        examples = {"add \"https://example.com/b.png\" to gallery items of {_g}",
            "add {_upload} to gallery items of {_g}"},
        since = "5.0.0-a1")
    public static List<String> items(MediaGalleryBuilder gallery) {
        return gallery.items().stream().map(MediaGalleryItem::getUrl).toList();
    }

    @Property.Setter("gallery items")
    public static void setItems(MediaGalleryBuilder gallery, Object[] items) {
        gallery.items().clear();
        for (Object item : items)
            gallery.add(item);
    }

    @Property.Adder("gallery items")
    public static void addItem(MediaGalleryBuilder gallery, Object item) {
        gallery.add(item);
    }

    @Property.Remover("gallery items")
    public static void removeItem(MediaGalleryBuilder gallery, String url) {
        gallery.items().removeIf(item -> item.getUrl().equals(url));
    }

    @Property("spoiler")
    public static Boolean spoiler(MediaGalleryBuilder gallery) {
        return gallery.spoiler();
    }

    @Property.Setter("spoiler")
    public static void setSpoiler(MediaGalleryBuilder gallery, Boolean spoiler) {
        gallery.spoiler(Boolean.TRUE.equals(spoiler));
    }
}
