package net.itsthesky.disky5.descriptors.components;

import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.AttachmentUploadBuilder;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "fileuploadinput", jda = AttachmentUploadBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "File Upload Input",
    description = {"A modal field where the user picks one or more files from their device. Read them in "
        + "'on modal received' with 'uploaded files of file upload with id'.",
        "It only lives inside a modal and needs a label there."},
    since = "5.0.0-a1")
public final class AttachmentUploadType {

    private AttachmentUploadType() {}

    @New("[a] new file upload input [with] [the] id %string% (named|labeled|with [the] label) %string%")
    @Documentation(
        name = "New File Upload Input",
        description = "Creates a file upload field for a modal (one file by default, see 'max range').",
        examples = {"set {_u} to a new file upload input with id \"proof\" labeled \"Screenshot\"",
            "set max range of {_u} to 3",
            "add {_u} to components of {_modal}"},
        since = "5.0.0-a1")
    public static AttachmentUploadBuilder newFileUploadInput(String id, String label) {
        AttachmentUploadBuilder upload = new AttachmentUploadBuilder(id);
        upload.label(label);
        return upload;
    }

    @Property("custom id")
    public static String customId(AttachmentUploadBuilder upload) {
        return upload.id();
    }

    @Property.Setter("custom id")
    public static void setCustomId(AttachmentUploadBuilder upload, String id) {
        upload.id(id);
    }

    @Property("label")
    public static @Nullable String label(AttachmentUploadBuilder upload) {
        return upload.label();
    }

    @Property.Setter("label")
    public static void setLabel(AttachmentUploadBuilder upload, @Nullable String label) {
        upload.label(label);
    }

    @Property("description")
    public static @Nullable String description(AttachmentUploadBuilder upload) {
        return upload.description();
    }

    @Property.Setter("description")
    public static void setDescription(AttachmentUploadBuilder upload, @Nullable String description) {
        upload.description(description);
    }

    @Property("min range")
    public static Number minRange(AttachmentUploadBuilder upload) {
        return upload.minValues();
    }

    @Property.Setter("min range")
    public static void setMinRange(AttachmentUploadBuilder upload, Number value) {
        upload.minValues(value.intValue());
    }

    @Property("max range")
    public static Number maxRange(AttachmentUploadBuilder upload) {
        return upload.maxValues();
    }

    @Property.Setter("max range")
    public static void setMaxRange(AttachmentUploadBuilder upload, Number value) {
        upload.maxValues(value.intValue());
    }

    @Property("required")
    public static Boolean required(AttachmentUploadBuilder upload) {
        return upload.required();
    }

    @Property.Setter("required")
    public static void setRequired(AttachmentUploadBuilder upload, Boolean required) {
        upload.required(Boolean.TRUE.equals(required));
    }
}
