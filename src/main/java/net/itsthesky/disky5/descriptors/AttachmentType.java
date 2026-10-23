package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Timespan;
import net.dv8tion.jda.api.entities.Message;
import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.annotations.ConditionType;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "attachment", jda = Message.Attachment.class)
public final class AttachmentType {

    @Property("attachment url")
    @Documentation(
            name = "Attachment URL",
            description = "The URL of the attachment.",
            examples = "reply with attachment url of {_attachment}",
            since = "5.0.0-a1"
    )
    public static String attachmentUrl(Message.Attachment a) {
        return a.getUrl();
    }

    @Property("file name")
    @Documentation(
            name = "Attachment File Name",
            description = "The file name of the attachment.",
            examples = "reply with file name of {_attachment}",
            since = "5.0.0-a1"
    )
    public static String fileName(Message.Attachment a) {
        return a.getFileName();
    }

    @Property("file extension")
    @Documentation(
            name = "Attachment File Extension",
            description = "The file extension of the attachment, or null if it has none.",
            examples = "reply with file extension of {_attachment}",
            since = "5.0.0-a1"
    )
    public static @Nullable String fileExtension(Message.Attachment a) {
        return a.getFileExtension();
    }

    @Property("attachment duration")
    @Documentation(
            name = "Attachment Duration",
            description = "The duration of the attachment if it is an audio (voice) file, or null otherwise.",
            examples = "reply with attachment duration of {_attachment}",
            since = "5.0.0-a1"
    )
    public static @Nullable Timespan attachmentDuration(Message.Attachment a) {
        if (a.getDuration() <= 0)
            return null;
        return new Timespan((long) (a.getDuration() * 1000L));
    }

    // JDA has no isAudio(): a non-zero duration is what marks an attachment as a voice file.
    @Condition(property = "[an] audio [attachment]", type = ConditionType.BE)
    @Documentation(
            name = "Attachment is Audio",
            description = "Whether the attachment is an audio file (a voice message). If it is, the "
                    + "'attachment duration' expression can be used on it.",
            examples = "if {_attachment} is an audio attachment:",
            since = "5.0.0-a1"
    )
    public static boolean audio(Message.Attachment a) {
        return a.getDuration() > 0;
    }

    @Condition(property = "[an] image [attachment]", type = ConditionType.BE)
    @Documentation(
            name = "Attachment is an Image",
            description = "Whether the attachment is an image.",
            examples = "if {_attachment} is an image attachment:",
            since = "5.0.0-a1"
    )
    public static boolean image(Message.Attachment a) {
        return a.isImage();
    }

    @Condition(property = "[a] video [attachment]", type = ConditionType.BE)
    @Documentation(
            name = "Attachment is a Video",
            description = "Whether the attachment is a video.",
            examples = "if {_attachment} is a video attachment:",
            since = "5.0.0-a1"
    )
    public static boolean video(Message.Attachment a) {
        return a.isVideo();
    }

    @Condition(property = "[a] spoiler [attachment]", type = ConditionType.BE)
    @Documentation(
            name = "Attachment is a Spoiler",
            description = "Whether the attachment is marked as a spoiler.",
            examples = "if {_attachment} is a spoiler attachment:",
            since = "5.0.0-a1"
    )
    public static boolean spoiler(Message.Attachment a) {
        return a.isSpoiler();
    }
}
