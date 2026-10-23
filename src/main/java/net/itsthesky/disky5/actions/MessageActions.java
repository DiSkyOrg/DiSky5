package net.itsthesky.disky5.actions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.messages.MessagePoll;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.MessagePayloads;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class MessageActions {

    private MessageActions() {}

    @Effect("(post|dispatch) %string/embedbuilder/component/pollbuilder% (in|to) [the] [channel] %messagechannel%")
    @Documentation(
        name = "Post Message",
        description = {"Posts a text, an embed, a component or a poll in a message channel.",
            "The sent message is discarded; build a message and read 'the result of' it to keep it."},
        examples = {"post \"Hello world!\" to event-channel", "post {_embed} to event-channel", "post {_container} to event-channel"},
        since = "5.0.0-a1")
    public static RestAction<Message> post(Object content, MessageChannel channel) {
        return channel.sendMessage(MessagePayloads.toCreateData(content));
    }

    @Effect("edit [the] [message] %message% (with|to show) %string/embedbuilder/component%")
    @Documentation(
        name = "Edit Message",
        description = {"Replaces a message's content or embeds. Only the bot's own messages can be edited.",
            "A text replaces the content, an embed replaces the embeds."},
        examples = {"edit event-message to show \"Abracadabra!\"", "edit {_msg} to show {_embed}"},
        since = "5.0.0-a1")
    public static RestAction<Message> edit(Message message, Object content) {
        return message.editMessage(MessageEditData.fromCreateData(MessagePayloads.toCreateData(content)));
    }

    @Effect("pin [the] [message] %message%")
    @Documentation(
        name = "Pin Message",
        description = "Pins a message in its channel. A channel holds up to 50 pinned messages.",
        examples = {"pin event-message"},
        since = "5.0.0-a1")
    public static RestAction<Void> pin(Message message) {
        // No isPinned() pre-check: that flag comes from the snapshot the script holds, so it is
        // already stale the moment anything else pins the message - including this script.
        return message.pin();
    }

    @Effect("unpin [the] [message] %message%")
    @Documentation(
        name = "Unpin Message",
        description = "Removes a message from its channel's pins.",
        examples = {"unpin event-message"},
        since = "5.0.0-a1")
    public static RestAction<Void> unpin(Message message) {
        return message.unpin();
    }

    @Effect("[discord] (publish|crosspost) [the] [message] %message%")
    @Documentation(
        name = "Publish Message",
        description = {"Publishes (crossposts) a message to every guild following its channel.",
            "Only works in a news/announcement channel."},
        examples = {"publish event-message"},
        since = "5.0.0-a1")
    public static RestAction<Message> publish(Message message) {
        return message.crosspost();
    }

    @Effect("[discord] (send|show) typing [status] (in|to) [the] [channel] %messagechannel%")
    @Documentation(
        name = "Send Typing",
        description = {"Sends the typing status to a channel - what makes the \"X is typing...\" indicator appear.",
            "It lasts 10 seconds, or until the bot posts a message."},
        examples = {"show typing status in event-channel"},
        since = "5.0.0-a1")
    public static RestAction<Void> sendTyping(MessageChannel channel) {
        return channel.sendTyping();
    }

    @Effect("purge [the] [message[s]] %messages%")
    @Documentation(
        name = "Purge Messages",
        description = {"Deletes several messages at once, using Discord's bulk-delete endpoint.",
            "Messages are grouped per channel; a channel with a single message falls back to a plain delete.",
            "To delete one message, use the destroy effect instead."},
        examples = {"await set {_msgs::*} to the last 50 messages of event-channel",
            "purge {_msgs::*}"},
        since = "5.0.0-a1")
    public static void purge(Message[] messages) {
        Map<MessageChannel, List<Message>> byChannel = new LinkedHashMap<>();
        for (Message message : messages)
            byChannel.computeIfAbsent(message.getChannel(), key -> new ArrayList<>()).add(message);

        // purgeMessages hands back one future per batch rather than a RestAction, so the effect waits
        // on them itself; it already runs off the main thread.
        for (Map.Entry<MessageChannel, List<Message>> entry : byChannel.entrySet()) {
            List<Message> batch = entry.getValue();
            if (batch.size() == 1)
                batch.get(0).delete().complete();
            else
                entry.getKey().purgeMessages(batch).forEach(CompletableFuture::join);
        }
    }

    @Effect("end [the] poll of [the] [message] %message% [now]")
    @Documentation(
        name = "End Poll",
        description = "Ends the poll attached to a message immediately, preventing any further vote.",
        examples = {"end the poll of event-message now"},
        since = "5.0.0-a1")
    public static RestAction<Message> endPoll(Message message) {
        MessagePoll poll = message.getPoll();
        if (poll == null)
            throw new IllegalStateException("this message carries no poll");
        if (poll.isExpired() || poll.isFinalizedVotes())
            throw new IllegalStateException("this poll is already over");
        return message.endPoll();
    }

    @Effect("(suppress|hide) [the] [discord] [link] embed[s] [message] (of|from) [the] [discord] [message] %message%")
    @Documentation(
        name = "Suppress Embeds",
        description = "Hides the link embeds Discord generated for a message.",
        examples = {"suppress embeds from event-message"},
        since = "5.0.0-a1")
    public static RestAction<Void> suppressEmbeds(Message message) {
        return message.suppressEmbeds(true);
    }

    @Effect("suppress [the] %emotes% [(of|from) [the] %-user%] (of|from) [the] %message%")
    @Documentation(
        name = "Suppress Reactions",
        description = {"Removes one or more reactions from a message.",
            "Naming a user removes that user's reaction; without one, the bot's own reaction is removed."},
        examples = {"suppress reaction emoji \"x\" of event-user from event-message"},
        since = "5.0.0-a1")
    public static @Nullable RestAction<List<Void>> suppressReactions(CustomEmoji[] emotes,
                                                                     @Nullable User user, Message message) {
        if (emotes.length == 0)
            return null;
        List<RestAction<Void>> removals = new ArrayList<>(emotes.length);
        for (CustomEmoji emote : emotes)
            removals.add(user == null ? message.removeReaction(emote) : message.removeReaction(emote, user));
        return RestAction.allOf(removals);
    }

    @Effect("react to [the] [message] %message% with [the] %emojis/strings%")
    @Documentation(
        name = "React To Message",
        description = {"Adds one or more reactions to a message, as the bot.",
            "An emoji or emote object reacts with that emoji; a text is either a unicode emoji or the "
                + "formatted '<:name:id>' form of a custom one."},
        examples = {"react to event-message with \"✅\"", "react to {_msg} with {_emote}",
            "react to event-message with emoji \"joy\""},
        since = "5.0.0-a1")
    public static @Nullable RestAction<List<Void>> react(Message message, Object[] emotes) {
        if (emotes.length == 0)
            return null;
        List<RestAction<Void>> reactions = new ArrayList<>(emotes.length);
        for (Object raw : emotes) {
            Emoji emoji = raw instanceof Emoji e ? e : Emoji.fromFormatted(String.valueOf(raw));
            reactions.add(message.addReaction(emoji));
        }
        return RestAction.allOf(reactions);
    }

    @Effect("forward [the] [message] %message% to [the] [channel] %messagechannel%")
    @Documentation(
        name = "Forward Message",
        description = "Forwards a message to another channel, creating a copy carrying the 'forwarded' marker.",
        examples = {"forward event-message to {_channel}"},
        since = "5.0.0-a1")
    public static RestAction<Message> forward(Message message, MessageChannel channel) {
        return message.forwardTo(channel);
    }
}
