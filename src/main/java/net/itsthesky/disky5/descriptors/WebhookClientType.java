package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Bot;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "webhookclient", jda = WebhookClient.class, parent = SnowflakeType.class)
@Documentation(
    name = "Webhook Client",
    description = {"Something that can post messages through a webhook: either a webhook retrieved from a "
        + "guild or channel, or a client built from a webhook URL alone.",
        "A client built from a URL needs no permission in the guild and can post even where no bot is present; "
            + "it can only send, edit and delete its own messages."},
    since = "5.0.0-a2")
public final class WebhookClientType {

    private WebhookClientType() {}

    @New("[a] new webhook client (from|with) [the] [webhook] url %string% [using [the] [bot] %-bot%]")
    @Documentation(
        name = "New Webhook Client",
        description = {"Creates a webhook client from a webhook URL (including its token).",
            "A bot is only used to carry the HTTP requests; when none is given, the only loaded bot is used."},
        examples = {"set {_hook} to a new webhook client from url \"https://discord.com/api/webhooks/123/abc\"",
            "set {_hook} to a new webhook client with url {url} using bot \"MyBot\""},
        since = "5.0.0-a2")
    public static WebhookClient newWebhookClient(String url, @Nullable Bot bot) {
        Bot carrier = bot != null ? bot : DiSky5.getBots().findAny();
        JDA jda = carrier == null ? null : carrier.anyShard();
        if (jda == null)
            throw new IllegalStateException("a webhook client needs a running bot to carry its requests; "
                + "log a bot in first or name one with 'using bot'");
        return WebhookClient.createClient(jda, url);
    }

    @Property("webhook token")
    @Documentation(
        name = "Webhook Token",
        description = "The secret token of the webhook, or nothing when Discord did not expose it (for example "
            + "a channel follower webhook). Anyone holding it can post through the webhook.",
        examples = "set {_token} to webhook token of {_hook}",
        since = "5.0.0-a2")
    public static @Nullable String webhookToken(WebhookClient client) {
        return client.getToken();
    }
}
