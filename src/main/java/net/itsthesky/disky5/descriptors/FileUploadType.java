package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.utils.FileUpload;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptType(name = "fileupload", jda = FileUpload.class)
@Documentation(
    name = "File Upload",
    description = {"A file to send to Discord, created from a local file, a URL or a received attachment.",
        "Attach it to a message builder with 'add {_file} to attachments of {_message}'."},
    since = "5.0.0-a1")
public final class FileUploadType {

    private FileUploadType() {}

    private static final String SPOILER_PREFIX = "SPOILER_";

    @Property("file name")
    @Documentation(
        name = "File Upload Name",
        description = "The name the file will carry on Discord, extension included. Can be changed.",
        examples = {"set file name of {_file} to \"report.txt\""},
        since = "5.0.0-a1")
    public static String fileName(FileUpload upload) {
        return upload.getName();
    }

    @Property.Setter("file name")
    public static void setFileName(FileUpload upload, String name) {
        upload.setName(name);
    }

    @Property.Setter("spoiler")
    @Documentation(
        name = "File Upload Spoiler",
        description = "Whether Discord hides the file behind a spoiler. It works by prefixing the "
            + "file name with 'SPOILER_', so rename the file before, not after.",
        examples = {"set spoiler of {_file} to true"},
        since = "5.0.0-a1")
    public static void setSpoiler(FileUpload upload, Boolean spoiler) {
        String name = upload.getName();
        boolean isSpoiler = name.startsWith(SPOILER_PREFIX);
        if (Boolean.TRUE.equals(spoiler) && !isSpoiler)
            upload.asSpoiler();
        else if (!Boolean.TRUE.equals(spoiler) && isSpoiler)
            upload.setName(name.substring(SPOILER_PREFIX.length()));
    }

    @Property.Setter("file description")
    @Documentation(
        name = "File Upload Description",
        description = "The alt text of the file, shown by screen readers and on hover for images.",
        examples = {"set file description of {_file} to \"the server logo\""},
        since = "5.0.0-a1")
    public static void setDescription(FileUpload upload, String description) {
        upload.setDescription(description);
    }
}
