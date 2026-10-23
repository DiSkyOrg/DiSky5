package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.pagination.PinnedMessagePaginationAction;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.List;

@SkriptType(name = "messagechannel", jda = MessageChannel.class, parent = ChannelType.class)
public final class MessageChannelType {

    @Property.Retrieve("pinned messages")
    @Documentation(
            name = "Pinned Messages",
            description = {
                "The pinned messages of a channel. Fetched online, so it requires 'await'.",
                "Only the most recent page (up to 50 pins) is returned."
            },
            examples = "await set {_pins::*} to pinned messages of event-channel",
            since = "5.0.0-a1"
    )
    public static List<Message> pinnedMessages(MessageChannel channel) {
        return channel.retrievePinnedMessages().complete().stream()
                .map(PinnedMessagePaginationAction.PinnedMessage::getMessage)
                .toList();
    }
}
