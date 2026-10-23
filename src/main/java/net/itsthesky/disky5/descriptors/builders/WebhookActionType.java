package net.itsthesky.disky5.descriptors.builders;

import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@SkriptType(name = "webhookaction", jda = WebhookAction.class)
@Documentation(
    name = "Webhook Builder",
    description = {"A webhook that does not exist yet: configure it, then create it with 'the build result of'.",
        "Nothing is sent to Discord until then, so setting its properties costs no request."},
    since = "5.0.0-a1")
public final class WebhookActionType {

    private WebhookActionType() {}

    @New("[a] new webhook builder [(named|with [the] name)] %string% in [the] [channel] %guildchannel%")
    @Documentation(
        name = "New Webhook Builder",
        description = "Creates a webhook builder for a channel. The webhook only exists once you read "
            + "'the build result of' it, which requires 'await'.",
        examples = {"set {_b} to a new webhook builder named \"Announcer\" in event-channel",
            "set webhook avatar of {_b} to \"https://example.com/avatar.png\"",
            "await set {_webhook} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static WebhookAction newWebhookBuilder(String name, GuildChannel channel) {
        if (!(channel instanceof IWebhookContainer container))
            throw new IllegalArgumentException("the channel '" + channel.getName()
                + "' cannot hold webhooks");
        return container.createWebhook(name);
    }

    @Build
    @Documentation(
        name = "Built Webhook",
        description = {"Creates the webhook on Discord and returns it. Requires 'await'.",
            "Building the same builder twice creates two webhooks."},
        examples = {"await set {_webhook} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static RestAction<Webhook> build(WebhookAction action) {
        return action;
    }

    @Property.Setter("webhook avatar")
    @Documentation(
        name = "Webhook Builder Avatar",
        description = "The avatar of the webhook being built, as a URL or a local file path.",
        examples = {"set webhook avatar of {_b} to \"https://example.com/avatar.png\""},
        since = "5.0.0-a1")
    public static void setWebhookAvatar(WebhookAction action, String value) {
        action.setAvatar(value == null ? null : icon(value));
    }

    public static Icon icon(String source) {
        try (InputStream stream = source.startsWith("http://") || source.startsWith("https://")
                ? new URL(source).openStream()
                : new FileInputStream(new File(source))) {
            return Icon.from(stream);
        } catch (IOException ex) {
            throw new IllegalArgumentException("could not read the image from '" + source + "'", ex);
        }
    }
}
