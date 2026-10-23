package net.itsthesky.disky5.events.invite;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Invite Delete Event",
        patterns = {"[discord] guild invite delete"},
        jda = GuildInviteDeleteEvent.class,
        intents = {"GUILD_INVITES"}
)
@Documentation(
        name = "Invite Delete Event",
        description = {
                "Fired when an invite is deleted from a guild. Can be used to get the channel and the guild."
        },
        examples = {
                "on guild invite delete:",
                "\tbroadcast \"Invite deleted from %event-channel% in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtInviteDelete {

    @EventValue
    public static Guild guild(GuildInviteDeleteEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(GuildInviteDeleteEvent event) {
        return event.getChannel();
    }
}
