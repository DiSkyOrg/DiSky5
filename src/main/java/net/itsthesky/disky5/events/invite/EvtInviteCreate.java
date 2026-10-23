package net.itsthesky.disky5.events.invite;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Invite Create Event",
        patterns = {"[discord] guild invite create"},
        jda = GuildInviteCreateEvent.class,
        intents = {"GUILD_INVITES"}
)
@Documentation(
        name = "Invite Create Event",
        description = {
                "Fired when an invite is created in a guild. Can be used to get the invite properties, ",
                "the channel, the author and the guild."
        },
        examples = {
                "on guild invite create:",
                "\tbroadcast \"New invite created in %event-channel% with code %event-invite's code%\""
        },
        since = "5.0.0-a1"
)
public final class EvtInviteCreate {

    @EventValue
    public static Invite invite(GuildInviteCreateEvent event) {
        return event.getInvite();
    }

    @EventValue
    public static Guild guild(GuildInviteCreateEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(GuildInviteCreateEvent event) {
        return event.getChannel();
    }
}
