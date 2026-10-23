package net.itsthesky.disky5.core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.utils.ComponentSerializer;
import net.itsthesky.disky5.api.components.ComponentBuilder;
import net.itsthesky.disky5.api.components.MessageComponents;
import net.itsthesky.disky5.api.polls.PollBuilder;
import net.dv8tion.jda.api.utils.AttachedFile;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageCreateRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class MessagePayloads {

    private MessagePayloads() {}

    public static MessageCreateData toCreateData(Object payload) {
        MessageCreateBuilder builder = new MessageCreateBuilder();
        if (payload instanceof EmbedBuilder embed)
            builder.setEmbeds(embed.build());
        else if (payload instanceof MessageCreateRequest<?> request)
            copy(request, builder);
        else if (payload instanceof PollBuilder poll)
            builder.setPoll(poll.build());
        else if (payload instanceof ComponentBuilder<?> component)
            MessageComponents.apply(builder, MessageComponents.group(new ComponentBuilder<?>[]{component}));
        else
            builder.setContent(String.valueOf(payload));
        if (!builder.getComponents().isEmpty() && !(payload instanceof MessageCreateRequest<?>))
            builder.addFiles(materialize(new ComponentSerializer().getFileUploads(builder.getComponents())));
        return builder.build();
    }

    public static List<FileUpload> materialize(Collection<? extends AttachedFile> files) {
        List<FileUpload> opened = new ArrayList<>(files.size());
        for (AttachedFile file : files) {
            FileUpload upload = (FileUpload) file;
            FileUpload open = FileUpload.fromData(upload.getData(), upload.getName());
            if (upload.getDescription() != null)
                open.setDescription(upload.getDescription());
            opened.add(open);
        }
        return opened;
    }

    private static void copy(MessageCreateRequest<?> from, MessageCreateBuilder to) {
        to.setContent(from.getContent());
        to.setEmbeds(from.getEmbeds());
        if (from.isUsingComponentsV2())
            to.useComponentsV2();
        to.setComponents(from.getComponents());
        to.setFiles(materialize(from.getAttachments()));
        to.addFiles(materialize(new ComponentSerializer().getFileUploads(from.getComponents())));
        to.setPoll(from.getPoll());
        to.setSuppressEmbeds(from.isSuppressEmbeds());
        to.setAllowedMentions(from.getAllowedMentions());
        to.mentionUsers(from.getMentionedUsers());
        to.mentionRoles(from.getMentionedRoles());
        to.mentionRepliedUser(from.isMentionRepliedUser());
    }
}
