package net.itsthesky.disky5.actions;

import net.dv8tion.jda.api.entities.Message;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AttachmentActions {

    private AttachmentActions() {}

    @Effect("download [the] [attachment] %attachment% (in|to) [the] [(folder|file|path)] %string%")
    @Documentation(
        name = "Download Attachment",
        description = {"Saves a message attachment to a file on the server, waiting until the download is complete.",
            "A path ending with '/' or '\\', or naming an existing folder, is a folder: the file is saved in it "
                + "under the attachment's file name. Any other path is the file itself. Missing folders are created, "
                + "and an existing file is overwritten.",
            "A relative path starts from the server's root folder (where the server jar is)."},
        examples = {"download {_attachment} to folder \"plugins/MyScript/uploads/\"",
            "loop discord attachments of event-message:",
            "\tdownload loop-value to file \"plugins/MyScript/last-%file name of loop-value%\""},
        since = "5.0.0-b1")
    public static void download(Message.Attachment attachment, String target) throws IOException {
        Path path = Path.of(target);
        boolean folder = target.endsWith("/") || target.endsWith("\\") || Files.isDirectory(path);
        // Discord already strips folders from upload names; getFileName() keeps a crafted one inside the folder.
        Path file = folder ? path.resolve(Path.of(attachment.getFileName()).getFileName()) : path;
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null)
            Files.createDirectories(parent);
        attachment.getProxy().downloadToPath(file).join();
    }
}
