package net.itsthesky.disky5.events.user;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "User Online Status Update Event",
        patterns = {"[discord] user online status (change|update)"},
        jda = UserUpdateOnlineStatusEvent.class,
        intents = {"GUILD_PRESENCES"}
)
@Documentation(
        name = "User Online Status Update Event",
        description = {
                "Fired when a user changes their online status.",
                "This event provides access to both the old and new online status values.",
                "It can be used for tracking user presence, activity patterns, or triggering actions when users come online."
        },
        examples = {
                "on user online status change:",
                "\tif current online status = online:",
                "\t\tbroadcast \"%event-user% has come online\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserOnlineStatusUpdate {

    @EventValue
    public static User user(UserUpdateOnlineStatusEvent event) {
        return event.getUser();
    }

    @EventValue
    public static Member member(UserUpdateOnlineStatusEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(UserUpdateOnlineStatusEvent event) {
        return event.getGuild();
    }

    @EventExpression("online status")
    public static OnlineStatus onlineStatus(UserUpdateOnlineStatusEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) online status")
    public static OnlineStatus oldOnlineStatus(UserUpdateOnlineStatusEvent event) {
        return event.getOldValue();
    }
}
