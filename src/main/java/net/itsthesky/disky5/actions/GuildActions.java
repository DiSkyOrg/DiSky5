package net.itsthesky.disky5.actions;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.automod.AutoModRule;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Bot;

public final class GuildActions {

    private GuildActions() {}

    @Effect("destroy [the] %message/role/channel/emote/webhook/scheduledevent/guildsticker/automodrule%")
    @Documentation(
        name = "Destroy Entity",
        description = {"Deletes a Discord entity: a message, a role, any channel, a custom emoji, "
            + "a webhook, a scheduled event, a guild sticker or an automod rule.",
            "To delete several messages at once, use the purge effect instead."},
        examples = {"destroy event-channel", "destroy event-message"},
        since = "5.0.0-a1")
    public static RestAction<Void> destroy(Object entity) {
        if (entity instanceof Message message)
            return message.delete();
        if (entity instanceof Role role)
            return role.delete();
        if (entity instanceof Channel channel)
            return channel.delete();
        if (entity instanceof Webhook webhook)
            return webhook.delete();
        if (entity instanceof ScheduledEvent scheduledEvent)
            return scheduledEvent.delete();
        if (entity instanceof AutoModRule rule)
            return rule.delete();
        if (entity instanceof GuildSticker sticker)
            return sticker.delete();
        if (entity instanceof RichCustomEmoji emoji)
            return emoji.delete();
        if (entity instanceof CustomEmoji)
            throw new IllegalArgumentException("this emoji does not belong to a guild the bot can edit, "
                + "so it cannot be deleted");
        throw new IllegalArgumentException("cannot destroy a " + entity.getClass().getSimpleName());
    }

    @Effect("make [the] %bot% leave [the] [guild] %guild%")
    @Documentation(
        name = "Make Bot Leave Guild",
        description = "Makes a bot leave a guild. It loses every access to that server until re-invited.",
        examples = {"make bot \"MyBot\" leave event-guild"},
        since = "5.0.0-a1")
    public static RestAction<Void> makeBotLeave(Bot bot, Guild guild) {
        Guild own = bot.getGuildById(guild.getId());
        if (own == null)
            throw new IllegalArgumentException("the bot '" + bot + "' is not in the guild '"
                + guild.getName() + "'");
        return own.leave();
    }
}
