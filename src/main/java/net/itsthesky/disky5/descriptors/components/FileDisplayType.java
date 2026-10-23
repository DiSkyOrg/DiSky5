package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.utils.FileUpload;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.FileDisplayBuilder;

@SkriptType(name = "filedisplay", jda = FileDisplayBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "File Display",
    description = "A components V2 block showing an uploaded file, downloadable by the reader.",
    since = "5.0.0-a1")
public final class FileDisplayType {

    private FileDisplayType() {}

    @New("[a] new file display [(with|from) [the] file] %fileupload%")
    @Documentation(
        name = "New File Display",
        description = "Creates a file display from a file upload (see 'a new file upload').",
        examples = {"set {_f} to a new file upload from file \"plugins/report.txt\"",
            "add a new file display from file {_f} to components of {_container}"},
        since = "5.0.0-a1")
    public static FileDisplayBuilder newFileDisplay(FileUpload file) {
        return new FileDisplayBuilder(file);
    }

    @Property("spoiler")
    public static Boolean spoiler(FileDisplayBuilder display) {
        return display.spoiler();
    }

    @Property.Setter("spoiler")
    public static void setSpoiler(FileDisplayBuilder display, Boolean spoiler) {
        display.spoiler(Boolean.TRUE.equals(spoiler));
    }
}
