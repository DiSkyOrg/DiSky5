package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "standardguildmessagechannel", jda = StandardGuildMessageChannel.class,
        parent = StandardGuildChannelType.class)
public final class StandardGuildMessageChannelType {

    @Property("topic")
    @Documentation(
            name = "Channel Topic",
            description = "The topic of this channel, or null if none is set.",
            examples = "send \"%topic of event-channel%\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String topic(StandardGuildMessageChannel channel) {
        return channel.getTopic();
    }

    @Property.Setter("topic")
    public static RestAction<?> setTopic(StandardGuildMessageChannel channel, @Nullable String value) {
        return channel.getManager().setTopic(value);
    }

    @Property("nsfw")
    @Documentation(
            name = "Channel NSFW",
            description = "Whether this channel is marked as age-restricted (NSFW).",
            examples = "if nsfw of event-channel is true:",
            since = "5.0.0-a1"
    )
    public static boolean nsfw(StandardGuildMessageChannel channel) {
        return channel.isNSFW();
    }

    @Property.Setter("nsfw")
    public static RestAction<?> setNsfw(StandardGuildMessageChannel channel, boolean value) {
        return channel.getManager().setNSFW(value);
    }

    // Not named "webhooks": the guild already owns that name, and both reads being pure-REST
    // means the union property generator can't fold them into one expression anyway.
    @Property.Retrieve("channel webhooks")
    @Documentation(
            name = "Channel Webhooks",
            description = "All webhooks of this channel. Fetched online, so it requires 'await'.",
            examples = "await set {_webhooks::*} to channel webhooks of event-channel",
            since = "5.0.0-a1"
    )
    public static RestAction<List<Webhook>> channelWebhooks(StandardGuildMessageChannel channel) {
        return channel.retrieveWebhooks();
    }
}
