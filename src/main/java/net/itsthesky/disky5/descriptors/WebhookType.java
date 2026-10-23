package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.IWebhookContainerUnion;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.descriptors.builders.WebhookActionType;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "webhook", jda = Webhook.class, parent = WebhookClientType.class)
public final class WebhookType {

    @Property("webhook url")
    @Documentation(
            name = "Webhook URL",
            description = "The URL of the webhook (including its token).",
            examples = "reply with webhook url of {_webhook}",
            since = "5.0.0-a1"
    )
    public static String webhookUrl(Webhook w) {
        return w.getUrl();
    }

    @Property("webhook channel")
    @Documentation(
            name = "Webhook Channel",
            description = {"The channel the webhook posts to.",
                "Set it to a text channel to move the webhook."},
            examples = {"reply with webhook channel of {_webhook}",
                "set webhook channel of {_webhook} to event-channel"},
            since = "5.0.0-a1"
    )
    public static IWebhookContainerUnion webhookChannel(Webhook w) {
        return w.getChannel();
    }

    @Property.Setter("webhook channel")
    public static RestAction<Void> setWebhookChannel(Webhook w, TextChannel channel) {
        return w.getManager().setChannel(channel);
    }

    @Property("webhook owner")
    @Documentation(
            name = "Webhook Owner",
            description = "The member who owns/created the webhook, or null if it is unknown.",
            examples = "reply with webhook owner of {_webhook}",
            since = "5.0.0-a1"
    )
    public static @Nullable Member webhookOwner(Webhook w) {
        return w.getOwner();
    }

    @Property(value = "name", shared = true)
    public static String discordName(Webhook w) {
        return w.getName();
    }

    @Property.Setter(value = "name", shared = true)
    public static RestAction<Void> setName(Webhook w, String name) {
        return w.getManager().setName(name);
    }

    @Property(value = "guild", shared = true)
    public static Guild guild(Webhook w) {
        return w.getGuild();
    }

    @Property("webhook type")
    @Documentation(
            name = "Webhook Type",
            description = "The kind of the webhook: INCOMING (posts through its URL) or CHANNEL_FOLLOWER "
                + "(created by following an announcement channel).",
            examples = "reply with webhook type of {_webhook}",
            since = "5.0.0-a2"
    )
    public static String webhookType(Webhook w) {
        return w.getType().name();
    }

    @Property("webhook avatar")
    @Documentation(
            name = "Webhook Avatar",
            description = {"The avatar URL of the webhook.",
                "Set it to an image URL or a local file path."},
            examples = {"reply with webhook avatar of {_webhook}",
                "set webhook avatar of {_webhook} to \"https://example.com/avatar.png\""},
            since = "5.0.0-a2"
    )
    public static String webhookAvatar(Webhook w) {
        return w.getDefaultUser().getEffectiveAvatarUrl();
    }

    @Property.Setter("webhook avatar")
    public static RestAction<Void> setWebhookAvatar(Webhook w, @Nullable String source) {
        return w.getManager().setAvatar(source == null ? null : WebhookActionType.icon(source));
    }

    @Property("webhook source guild id")
    @Documentation(
            name = "Webhook Source Guild ID",
            description = "For a channel follower webhook, the id of the guild whose announcements it relays; "
                + "nothing otherwise.",
            examples = "reply with webhook source guild id of {_webhook}",
            since = "5.0.0-a2"
    )
    public static @Nullable String webhookSourceGuildId(Webhook w) {
        Webhook.GuildReference source = w.getSourceGuild();
        return source == null ? null : source.getId();
    }

    @Property("webhook source channel id")
    @Documentation(
            name = "Webhook Source Channel ID",
            description = "For a channel follower webhook, the id of the announcement channel it relays; "
                + "nothing otherwise.",
            examples = "reply with webhook source channel id of {_webhook}",
            since = "5.0.0-a2"
    )
    public static @Nullable String webhookSourceChannelId(Webhook w) {
        Webhook.ChannelReference source = w.getSourceChannel();
        return source == null ? null : source.getId();
    }
}
