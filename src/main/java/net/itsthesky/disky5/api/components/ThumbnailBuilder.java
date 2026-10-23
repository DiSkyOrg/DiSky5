package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.thumbnail.Thumbnail;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.Nullable;

public final class ThumbnailBuilder implements SectionAccessoryBuilder<Thumbnail> {

    private @Nullable String url;
    private @Nullable FileUpload file;
    private @Nullable String description;
    private boolean spoiler;
    private int uniqueId;

    public ThumbnailBuilder(Object source) {
        source(source);
    }

    public static ThumbnailBuilder from(Thumbnail thumbnail) {
        ThumbnailBuilder b = new ThumbnailBuilder(thumbnail.getUrl());
        b.description = thumbnail.getDescription();
        b.spoiler = thumbnail.isSpoiler();
        b.uniqueId = thumbnail.getUniqueId();
        return b;
    }

    public void source(Object source) {
        if (source instanceof FileUpload upload) {
            this.file = upload;
            this.url = null;
        } else {
            this.url = String.valueOf(source);
            this.file = null;
        }
    }

    @Override
    public Thumbnail build() {
        Thumbnail thumbnail = file != null ? Thumbnail.fromFile(file) : Thumbnail.fromUrl(url);
        if (description != null) thumbnail = thumbnail.withDescription(description);
        return thumbnail.withSpoiler(spoiler);
    }

    public @Nullable FileUpload file() { return file; }
    public @Nullable String description() { return description; }
    public void description(@Nullable String description) { this.description = description; }
    public boolean spoiler() { return spoiler; }
    public void spoiler(boolean spoiler) { this.spoiler = spoiler; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "thumbnail";
    }
}
