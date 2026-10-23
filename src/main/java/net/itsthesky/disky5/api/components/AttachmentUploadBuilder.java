package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.attachmentupload.AttachmentUpload;
import org.jetbrains.annotations.Nullable;

public final class AttachmentUploadBuilder implements ModalChildBuilder<AttachmentUpload> {

    private String id;
    private int minValues = 1;
    private int maxValues = 1;
    private boolean required = true;
    private @Nullable String label;
    private @Nullable String description;
    private int uniqueId;

    public AttachmentUploadBuilder(String id) {
        this.id = id;
    }

    @Override
    public AttachmentUpload build() {
        return AttachmentUpload.create(id)
            .setRequiredRange(minValues, maxValues)
            .setRequired(required)
            .build();
    }

    public String id() { return id; }
    public void id(String id) { this.id = id; }
    public int minValues() { return minValues; }
    public void minValues(int minValues) { this.minValues = minValues; }
    public int maxValues() { return maxValues; }
    public void maxValues(int maxValues) { this.maxValues = maxValues; }
    public boolean required() { return required; }
    public void required(boolean required) { this.required = required; }
    @Override public @Nullable String label() { return label; }
    @Override public void label(@Nullable String label) { this.label = label; }
    @Override public @Nullable String description() { return description; }
    @Override public void description(@Nullable String description) { this.description = description; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "file upload input " + id;
    }
}
