package net.itsthesky.disky5.events.user;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "User Typing Event",
        patterns = {"[discord] user typ[e|ing]"},
        jda = UserTypingEvent.class
)
@Documentation(
        name = "User Typing Event",
        description = {
                "Fired when a user starts typing in a channel.",
                "This event is triggered when the typing indicator appears for a user.",
                "It can be used to detect activity in channels or for interactive bot responses."
        },
        examples = {
                "on user typing:",
                "\tif event-channel is text channel with id \"123456789\":",
                "\t\tbroadcast \"%event-user% is typing in the support channel!\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserTyping {

    @EventValue
    public static User user(UserTypingEvent event) {
        return event.getUser();
    }

    @EventValue
    public static Guild guild(UserTypingEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(UserTypingEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Channel channel(UserTypingEvent event) {
        return event.getChannel();
    }
}
