package net.itsthesky.disky5.actions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageEditAction;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.MessagePayloads;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class WebhookActions {

    private WebhookActions() {}

    @Effect("make [the] [webhook] [client] %webhookclient% (post|send|speak) %string/embedbuilder/component/messageaction% "
        + "[with [the] username %-string%] [[and] [with] [the] avatar [url] %-string%] [in [the] thread %-threadchannel%]")
    @Documentation(
        name = "Make Webhook Post",
        description = {"Makes a webhook, or a webhook client built from a URL, post a message.",
            "The username and avatar override the webhook's own for this message only. The sent message is "
                + "discarded; build a webhook message and read 'the build result of' it to keep it."},
        examples = {"make webhook client {_hook} post \"Hello from the webhook!\"",
            "make {_hook} post {_embed} with username \"Announcer\" and avatar \"https://example.com/a.png\"",
            "make {_hook} speak \"In a thread\" in thread {_thread}"},
        since = "5.0.0-a2")
    public static RestAction<Message> webhookPost(WebhookClient client, Object content, @Nullable String username,
                                           @Nullable String avatarUrl, @Nullable ThreadChannel thread) {
        WebhookMessageCreateAction<Message> action = client.sendMessage(MessagePayloads.toCreateData(content));
        if (username != null)
            action.setUsername(username);
        if (avatarUrl != null)
            action.setAvatarUrl(avatarUrl);
        if (thread != null)
            action.setThread(thread);
        return action;
    }

    @Effect("edit [the] webhook message %string/message% of [the] [webhook] [client] %webhookclient% "
        + "(with|to show) %string/embedbuilder/component/messageaction% [in [the] thread %-threadchannel%]")
    @Documentation(
        name = "Edit Webhook Message",
        description = {"Edits a message that was sent through a webhook, by its message or its id.",
            "A webhook can only edit its own messages. Name the thread when the message lives in one."},
        examples = {"edit webhook message {_sent} of {_hook} to show \"Updated!\"",
            "edit the webhook message \"123456789012345678\" of {_hook} with {_embed}"},
        since = "5.0.0-a2")
    public static RestAction<Message> webhookEdit(Object message, WebhookClient client, Object content,
                                           @Nullable ThreadChannel thread) {
        WebhookMessageEditAction<Message> action = client.editMessageById(messageId(message),
            MessageEditData.fromCreateData(MessagePayloads.toCreateData(content)));
        if (thread != null)
            action.setThread(thread);
        return action;
    }

    @Effect("[discord] delete [the] webhook message %string/message% of [the] [webhook] [client] %webhookclient% "
        + "[in [the] thread %-threadchannel%]")
    @Documentation(
        name = "Delete Webhook Message",
        description = {"Deletes a message that was sent through a webhook, by its message or its id.",
            "A webhook can only delete its own messages. Name the thread when the message lives in one."},
        examples = {"delete webhook message {_sent} of {_hook}",
            "delete the webhook message \"123456789012345678\" of {_hook}"},
        since = "5.0.0-a2")
    public static RestAction<Void> webhookDelete(Object message, WebhookClient client, @Nullable ThreadChannel thread) {
        var action = client.deleteMessageById(messageId(message));
        if (thread != null)
            action.setThread(thread);
        return action;
    }

    private static String messageId(Object message) {
        return message instanceof Message m ? m.getId() : String.valueOf(message);
    }
}
