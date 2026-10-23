package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.utils.FileUpload;

import java.util.ArrayList;
import java.util.List;

public final class MediaGalleryBuilder implements MessageTopLevelBuilder<MediaGallery>, ContainerChildBuilder<MediaGallery> {

    private final List<MediaGalleryItem> items = new ArrayList<>();
    private boolean spoiler;
    private int uniqueId;

    public static MediaGalleryBuilder from(MediaGallery gallery) {
        MediaGalleryBuilder b = new MediaGalleryBuilder();
        b.items.addAll(gallery.getItems());
        b.uniqueId = gallery.getUniqueId();
        return b;
    }

    public static MediaGalleryItem item(Object source) {
        return source instanceof FileUpload upload
            ? MediaGalleryItem.fromFile(upload)
            : MediaGalleryItem.fromUrl(String.valueOf(source));
    }

    public void add(Object source) {
        if (items.size() >= MediaGallery.MAX_ITEMS)
            throw new IllegalStateException("a media gallery holds at most " + MediaGallery.MAX_ITEMS + " items");
        items.add(item(source));
    }

    @Override
    public MediaGallery build() {
        if (items.isEmpty())
            throw new IllegalStateException("this media gallery is empty");
        return MediaGallery.of(spoiler ? items.stream().map(i -> i.withSpoiler(true)).toList() : items);
    }

    public List<MediaGalleryItem> items() { return items; }
    public boolean spoiler() { return spoiler; }
    public void spoiler(boolean spoiler) { this.spoiler = spoiler; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "media gallery of " + items.size() + " item(s)";
    }
}
