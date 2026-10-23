package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.filedisplay.FileDisplay;
import net.dv8tion.jda.api.utils.FileUpload;

public final class FileDisplayBuilder implements MessageTopLevelBuilder<FileDisplay>, ContainerChildBuilder<FileDisplay> {

    private FileUpload file;
    private boolean spoiler;
    private int uniqueId;

    public FileDisplayBuilder(FileUpload file) {
        this.file = file;
    }

    @Override
    public FileDisplay build() {
        return FileDisplay.fromFile(file).withSpoiler(spoiler);
    }

    public FileUpload file() { return file; }
    public void file(FileUpload file) { this.file = file; }
    public boolean spoiler() { return spoiler; }
    public void spoiler(boolean spoiler) { this.spoiler = spoiler; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "file display " + file.getName();
    }
}
