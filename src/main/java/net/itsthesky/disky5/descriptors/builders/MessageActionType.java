package net.itsthesky.disky5.descriptors.builders;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.Component;
import net.dv8tion.jda.api.components.MessageTopLevelComponent;
import net.dv8tion.jda.api.components.utils.ComponentSerializer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.ComponentBuilder;
import net.itsthesky.disky5.api.components.MessageComponents;
import net.itsthesky.disky5.api.polls.PollBuilder;
import net.itsthesky.disky5.core.MessagePayloads;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SkriptType(name = "messageaction", jda = MessageCreateAction.class)
@Documentation(
    name = "Message Builder",
    description = {"A message that has not been sent yet: configure it, then send it with 'the build result of'.",
        "Nothing reaches Discord until then, so setting its properties costs no request, and the "
            + "result is the sent message itself."},
    since = "5.0.0-a1")
public final class MessageActionType {

    private MessageActionType() {}

    @New("[a] new message with [the] content %string% in [the] [channel] %messagechannel%")
    @Documentation(
        name = "New Message Builder",
        description = {"Creates a message builder from a piece of text. The message is only sent once "
            + "you read 'the build result of' it, which requires 'await'."},
        examples = {"set {_m} to a new message with content \"hello\" in event-channel",
            "set tts of {_m} to true",
            "await set {_sent} to the build result of {_m}"},
        since = "5.0.0-a1")
    public static MessageCreateAction newMessageWithContent(String content, MessageChannel channel) {
        return channel.sendMessage(content);
    }

    @New("[a] new message with [the] embed %embedbuilder% in [the] [channel] %messagechannel%")
    @Documentation(
        name = "New Message Builder from an Embed",
        description = {"Creates a message builder from an embed. The message is only sent once you "
            + "read 'the build result of' it, which requires 'await'."},
        examples = {"set {_e} to a new discord embed",
            "set title of {_e} to \"Hello\"",
            "set {_m} to a new message with embed {_e} in event-channel",
            "await set {_sent} to the build result of {_m}"},
        since = "5.0.0-a1")
    public static MessageCreateAction newMessageWithEmbed(EmbedBuilder embed, MessageChannel channel) {
        return channel.sendMessageEmbeds(embed.build());
    }

    @Build
    @Documentation(
        name = "Sent Message",
        description = {"Sends the message and returns it. Requires 'await'.",
            "Sending the same builder twice sends two messages."},
        examples = {"await set {_sent} to the build result of {_m}",
            "await set {_sent} to the built message from {_m}"},
        since = "5.0.0-a1")
    public static RestAction<Message> build(MessageCreateAction action) {
        String content = action.getContent();
        if ((content == null || content.isEmpty()) && action.getEmbeds().isEmpty()
            && action.getAttachments().isEmpty() && action.getComponents().isEmpty() && action.getPoll() == null)
            throw new IllegalStateException("this message has no content, no embed, no attachment, "
                + "no component and no poll; Discord refuses an empty message");
        if (action.isUsingComponentsV2() && ((content != null && !content.isEmpty()) || !action.getEmbeds().isEmpty()))
            throw new IllegalStateException("a message holding a container, section, text display, separator, "
                + "media gallery or file display cannot carry content or embeds; put the text in a text display");
        if (action.isUsingComponentsV2() && action.getPoll() != null)
            throw new IllegalStateException("a message holding a container, section, text display, separator, "
                + "media gallery or file display cannot carry a poll");
        if (!action.getAttachments().isEmpty())
            action.setFiles(MessagePayloads.materialize(action.getAttachments()));
        if (!action.getComponents().isEmpty())
            action.addFiles(MessagePayloads.materialize(new ComponentSerializer().getFileUploads(action.getComponents())));
        if (content != null && content.length() > Message.MAX_CONTENT_LENGTH)
            throw new IllegalStateException("this message's content is " + content.length()
                + " characters long, the maximum is " + Message.MAX_CONTENT_LENGTH);
        return action;
    }

    @Property.Setter("discord content")
    @Documentation(
        name = "Message Builder Content",
        description = "The raw text of the message being built. Setting it replaces whatever content "
            + "the builder already carries.",
        examples = {"set discord content of {_m} to \"hello world\""},
        since = "5.0.0-a1")
    public static void setContent(MessageCreateAction action, @Nullable String value) {
        action.setContent(value);
    }

    @Property.Setter("embed")
    @Documentation(
        name = "Message Builder Embed",
        description = "The embed of the message being built. Setting it replaces every embed the "
            + "builder already carries.",
        examples = {"set embed of {_m} to {_e}"},
        since = "5.0.0-a1")
    public static void setEmbed(MessageCreateAction action, EmbedBuilder value) {
        action.setEmbeds(value.build());
    }

    @Property.Setter("embeds")
    @Documentation(
        name = "Message Builder Embeds",
        description = {"Every embed of the message being built, in display order (10 at most, Discord's limit).",
            "Setting it replaces the embeds the builder already carries, adding appends to them and deleting "
                + "removes them all. Each embed is snapshotted when it is set or added: editing it afterwards "
                + "changes nothing. Embeds have no id, so removing a single one is not supported: set the list again.",
            "The list cannot be read back from a builder; read 'embeds of' the sent message instead."},
        examples = {"set {_m} to a new message with content \"Weekly report\" in event-channel",
            "set embeds of {_m} to {_summary} and {_details}",
            "add {_footer} to embeds of {_m}",
            "await set {_sent} to the build result of {_m}",
            "send \"%size of embeds of {_sent}%\" to console"},
        since = "5.0.0-b1")
    public static void setEmbeds(MessageCreateAction action, EmbedBuilder[] embeds) {
        action.setEmbeds(buildEmbeds(embeds, 0));
    }

    @Property.Adder("embeds")
    public static void addEmbeds(MessageCreateAction action, EmbedBuilder[] embeds) {
        action.addEmbeds(buildEmbeds(embeds, action.getEmbeds().size()));
    }

    private static List<MessageEmbed> buildEmbeds(EmbedBuilder[] embeds, int already) {
        int total = already + embeds.length;
        if (total > Message.MAX_EMBED_COUNT)
            throw new IllegalArgumentException("a message carries " + Message.MAX_EMBED_COUNT
                + " embeds at most, this one would have " + total);
        return Arrays.stream(embeds).map(EmbedBuilder::build).toList();
    }

    @Property.Setter("attached poll")
    @Documentation(
        name = "Message Builder Poll",
        description = {"The poll of the message being built (a message carries one poll at most; setting it "
            + "replaces the previous one). The poll is validated and snapshotted when set.",
            "A poll cannot be combined with components V2 (containers, sections, text displays...)."},
        examples = {"set attached poll of {_m} to {_poll}"},
        since = "5.0.0-a2")
    public static void setPoll(MessageCreateAction action, PollBuilder poll) {
        action.setPoll(poll.build());
    }

    @Property.Setter("tts")
    @Documentation(
        name = "Message Builder TTS",
        description = "Whether the message being built is read out loud by Discord's text-to-speech.",
        examples = {"set tts of {_m} to true"},
        since = "5.0.0-a1")
    public static void setTTS(MessageCreateAction action, Boolean value) {
        action.setTTS(value);
    }

    @Property.Setter("suppressed embeds")
    @Documentation(
        name = "Message Builder Suppressed Embeds",
        description = "Whether Discord hides the embeds of the message being built, including the "
            + "previews it generates from links.",
        examples = {"set suppressed embeds of {_m} to true"},
        since = "5.0.0-a1")
    public static void setSuppressEmbeds(MessageCreateAction action, Boolean value) {
        action.setSuppressEmbeds(value);
    }

    @Property.Setter("suppressed notifications")
    @Documentation(
        name = "Message Builder Suppressed Notifications",
        description = "Whether the message being built is sent silently, without pinging anyone's "
            + "device.",
        examples = {"set suppressed notifications of {_m} to true"},
        since = "5.0.0-a1")
    public static void setSuppressedNotifications(MessageCreateAction action, Boolean value) {
        action.setSuppressedNotifications(value);
    }

    @Property.Setter("referenced message")
    @Documentation(
        name = "Message Builder Referenced Message",
        description = "The message the one being built replies to.",
        examples = {"set referenced message of {_m} to event-message"},
        since = "5.0.0-a1")
    public static void setReferencedMessage(MessageCreateAction action, Message value) {
        action.setMessageReference(value);
    }

    @Property.Setter("mention replied user")
    @Documentation(
        name = "Message Builder Mention Replied User",
        description = "Whether replying pings the author of the referenced message. Has no effect "
            + "unless a referenced message is set.",
        examples = {"set mention replied user of {_m} to false"},
        since = "5.0.0-a1")
    public static void setMentionRepliedUser(MessageCreateAction action, Boolean value) {
        action.mentionRepliedUser(value);
    }

    @Property.Setter("fail on invalid reply")
    @Documentation(
        name = "Message Builder Fail on Invalid Reply",
        description = "Whether sending fails when the referenced message no longer exists. When "
            + "false, the message is sent as a normal one instead.",
        examples = {"set fail on invalid reply of {_m} to false"},
        since = "5.0.0-a1")
    public static void setFailOnInvalidReply(MessageCreateAction action, Boolean value) {
        action.failOnInvalidReply(value);
    }

    @Property("attachments")
    @Documentation(
        name = "Message Builder Attachments",
        description = {"The files attached to the message being built, read back as their names.",
            "Add file uploads to it (see 'a new file upload'); remove one by its name."},
        examples = {"set {_f} to a new file upload from url \"https://example.com/logo.png\"",
            "add {_f} to attachments of {_m}",
            "remove \"logo.png\" from attachments of {_m}"},
        since = "5.0.0-a1")
    public static List<String> attachments(MessageCreateAction action) {
        return action.getAttachments().stream().map(FileUpload::getName).toList();
    }

    @Property.Setter("attachments")
    public static void setAttachments(MessageCreateAction action, FileUpload[] files) {
        action.setFiles(Arrays.asList(files));
    }

    @Property.Adder("attachments")
    public static void addAttachment(MessageCreateAction action, FileUpload file) {
        action.addFiles(file);
    }

    @Property.Remover("attachments")
    public static void removeAttachment(MessageCreateAction action, String name) {
        List<FileUpload> kept = new ArrayList<>(action.getAttachments());
        kept.removeIf(upload -> upload.getName().equals(name));
        action.setFiles(kept);
    }

    @Property("components")
    public static List<Component> components(MessageCreateAction action) {
        return List.copyOf(action.getComponents());
    }

    @Property.Setter("components")
    public static void setComponents(MessageCreateAction action, ComponentBuilder<?>[] components) {
        List<MessageTopLevelComponent> grouped = MessageComponents.group(components);
        action.useComponentsV2(MessageComponents.needsV2(grouped));
        action.setComponents(grouped);
    }

    @Property.Adder("components")
    public static void addComponent(MessageCreateAction action, ComponentBuilder<?> component) {
        List<MessageTopLevelComponent> appended = MessageComponents.append(action.getComponents(), component);
        if (MessageComponents.needsV2(appended))
            action.useComponentsV2(true);
        action.setComponents(appended);
    }

    @Property.Remover("components")
    public static void removeComponent(MessageCreateAction action, Number uniqueId) {
        List<MessageTopLevelComponent> kept = new ArrayList<>(action.getComponents());
        kept.removeIf(c -> c.getUniqueId() == uniqueId.intValue());
        action.setComponents(kept);
    }
}
