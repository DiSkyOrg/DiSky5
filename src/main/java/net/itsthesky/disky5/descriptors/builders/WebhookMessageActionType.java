package net.itsthesky.disky5.descriptors.builders;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.Component;
import net.dv8tion.jda.api.components.MessageTopLevelComponent;
import net.dv8tion.jda.api.components.utils.ComponentSerializer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.ComponentBuilder;
import net.itsthesky.disky5.api.components.MessageComponents;
import net.itsthesky.disky5.core.MessagePayloads;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SkriptType(name = "webhookmessageaction", jda = WebhookMessageCreateAction.class)
@Documentation(
    name = "Webhook Message Builder",
    description = {"A message a webhook has not sent yet: configure it, then send it with 'the build result of'.",
        "Nothing reaches Discord until then, and the result is the sent message itself, which can later be "
            + "edited or deleted through the same webhook."},
    since = "5.0.0-a2")
@SuppressWarnings({"rawtypes", "unchecked"})
public final class WebhookMessageActionType {

    private WebhookMessageActionType() {}

    @New("[a] new webhook message with [the] content %string% (for|through|using) [the] [webhook] [client] %webhookclient%")
    @Documentation(
        name = "New Webhook Message Builder",
        description = {"Creates a webhook message builder from a piece of text. The message is only sent once "
            + "you read 'the build result of' it, which requires 'await'."},
        examples = {"set {_m} to a new webhook message with content \"hello\" through {_hook}",
            "set webhook username of {_m} to \"Announcer\"",
            "await set {_sent} to the build result of {_m}"},
        since = "5.0.0-a2")
    public static WebhookMessageCreateAction newWebhookMessageWithContent(String content, WebhookClient client) {
        return client.sendMessage(content);
    }

    @New("[a] new webhook message with [the] embed %embedbuilder% (for|through|using) [the] [webhook] [client] %webhookclient%")
    @Documentation(
        name = "New Webhook Message Builder from an Embed",
        description = {"Creates a webhook message builder from an embed. The message is only sent once you "
            + "read 'the build result of' it, which requires 'await'."},
        examples = {"set {_m} to a new webhook message with embed {_embed} through {_hook}",
            "await set {_sent} to the build result of {_m}"},
        since = "5.0.0-a2")
    public static WebhookMessageCreateAction newWebhookMessageWithEmbed(EmbedBuilder embed, WebhookClient client) {
        return client.sendMessageEmbeds(embed.build());
    }

    @Build
    @Documentation(
        name = "Sent Webhook Message",
        description = {"Sends the message through the webhook and returns it. Requires 'await'.",
            "Sending the same builder twice sends two messages."},
        examples = {"await set {_sent} to the build result of {_m}"},
        since = "5.0.0-a2")
    public static RestAction<Message> build(WebhookMessageCreateAction action) {
        String content = action.getContent();
        if ((content == null || content.isEmpty()) && action.getEmbeds().isEmpty()
            && action.getAttachments().isEmpty() && action.getComponents().isEmpty())
            throw new IllegalStateException("this message has no content, no embed, no attachment and "
                + "no component; Discord refuses an empty message");
        if (action.isUsingComponentsV2() && ((content != null && !content.isEmpty()) || !action.getEmbeds().isEmpty()))
            throw new IllegalStateException("a message holding a container, section, text display, separator, "
                + "media gallery or file display cannot carry content or embeds; put the text in a text display");
        if (!action.getAttachments().isEmpty())
            action.setFiles(MessagePayloads.materialize(action.getAttachments()));
        if (!action.getComponents().isEmpty())
            action.addFiles(MessagePayloads.materialize(new ComponentSerializer().getFileUploads(action.getComponents())));
        if (content != null && content.length() > Message.MAX_CONTENT_LENGTH)
            throw new IllegalStateException("this message's content is " + content.length()
                + " characters long, the maximum is " + Message.MAX_CONTENT_LENGTH);
        return action;
    }

    @Property.Setter("webhook username")
    @Documentation(
        name = "Webhook Message Username",
        description = "The name the webhook displays for this message only, instead of its own.",
        examples = {"set webhook username of {_m} to \"Announcer\""},
        since = "5.0.0-a2")
    public static void setUsername(WebhookMessageCreateAction action, @Nullable String value) {
        action.setUsername(value);
    }

    @Property.Setter("webhook avatar url")
    @Documentation(
        name = "Webhook Message Avatar URL",
        description = "The image URL the webhook displays as its avatar for this message only.",
        examples = {"set webhook avatar url of {_m} to \"https://example.com/avatar.png\""},
        since = "5.0.0-a2")
    public static void setAvatarUrl(WebhookMessageCreateAction action, @Nullable String value) {
        action.setAvatarUrl(value);
    }

    @Property.Setter("webhook thread")
    @Documentation(
        name = "Webhook Message Thread",
        description = "The thread the message is posted in. The webhook must belong to the thread's parent channel.",
        examples = {"set webhook thread of {_m} to {_thread}"},
        since = "5.0.0-a2")
    public static void setThread(WebhookMessageCreateAction action, @Nullable ThreadChannel value) {
        action.setThread(value);
    }

    @Property.Setter("webhook thread id")
    @Documentation(
        name = "Webhook Message Thread ID",
        description = "The id of the thread the message is posted in, for when only the id is known.",
        examples = {"set webhook thread id of {_m} to \"123456789012345678\""},
        since = "5.0.0-a2")
    public static void setThreadId(WebhookMessageCreateAction action, @Nullable String value) {
        action.setThreadId(value);
    }

    @Property.Setter("discord content")
    public static void setContent(WebhookMessageCreateAction action, @Nullable String value) {
        action.setContent(value);
    }

    @Property.Setter("embed")
    public static void setEmbed(WebhookMessageCreateAction action, EmbedBuilder value) {
        action.setEmbeds(value.build());
    }

    @Property.Setter("tts")
    public static void setTTS(WebhookMessageCreateAction action, Boolean value) {
        action.setTTS(value);
    }

    @Property.Setter("suppressed embeds")
    public static void setSuppressEmbeds(WebhookMessageCreateAction action, Boolean value) {
        action.setSuppressEmbeds(value);
    }

    @Property.Setter("suppressed notifications")
    public static void setSuppressedNotifications(WebhookMessageCreateAction action, Boolean value) {
        action.setSuppressedNotifications(value);
    }

    @Property("attachments")
    public static List<String> attachments(WebhookMessageCreateAction action) {
        List<FileUpload> uploads = action.getAttachments();
        return uploads.stream().map(FileUpload::getName).toList();
    }

    @Property.Setter("attachments")
    public static void setAttachments(WebhookMessageCreateAction action, FileUpload[] files) {
        action.setFiles(Arrays.asList(files));
    }

    @Property.Adder("attachments")
    public static void addAttachment(WebhookMessageCreateAction action, FileUpload file) {
        action.addFiles(file);
    }

    @Property.Remover("attachments")
    public static void removeAttachment(WebhookMessageCreateAction action, String name) {
        List<FileUpload> kept = new ArrayList<>(action.getAttachments());
        kept.removeIf(upload -> upload.getName().equals(name));
        action.setFiles(kept);
    }

    @Property("components")
    public static List<Component> components(WebhookMessageCreateAction action) {
        return List.copyOf(action.getComponents());
    }

    @Property.Setter("components")
    public static void setComponents(WebhookMessageCreateAction action, ComponentBuilder<?>[] components) {
        List<MessageTopLevelComponent> grouped = MessageComponents.group(components);
        action.useComponentsV2(MessageComponents.needsV2(grouped));
        action.setComponents(grouped);
    }

    @Property.Adder("components")
    public static void addComponent(WebhookMessageCreateAction action, ComponentBuilder<?> component) {
        List<MessageTopLevelComponent> appended = MessageComponents.append(action.getComponents(), component);
        if (MessageComponents.needsV2(appended))
            action.useComponentsV2(true);
        action.setComponents(appended);
    }

    @Property.Remover("components")
    public static void removeComponent(WebhookMessageCreateAction action, Number uniqueId) {
        List<MessageTopLevelComponent> kept = new ArrayList<>(action.getComponents());
        kept.removeIf(c -> c.getUniqueId() == uniqueId.intValue());
        action.setComponents(kept);
    }
}
