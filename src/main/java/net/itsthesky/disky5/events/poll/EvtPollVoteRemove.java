package net.itsthesky.disky5.events.poll;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.poll.MessagePollVoteRemoveEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Poll Vote Remove",
        patterns = {"[message] poll vote remove[d]"},
        jda = MessagePollVoteRemoveEvent.class
)
@Documentation(
        name = "Poll Vote Remove",
        description = "Fired when a user removes their vote from a poll in a message.",
        examples = {
            "on poll vote remove:",
            "    send \"User %event-user% removed their vote from a poll in %event-channel%!\" to console"
        },
        since = "5.0.0-a1"
)
public final class EvtPollVoteRemove {

    @EventValue
    public static RestAction<Message> message(MessagePollVoteRemoveEvent event) {
        return event.retrieveMessage();
    }

    @EventValue
    public static @Nullable RestAction<Member> member(MessagePollVoteRemoveEvent event) {
        return event.isFromGuild() ? event.retrieveMember() : null;
    }

    @EventValue
    public static RestAction<User> user(MessagePollVoteRemoveEvent event) {
        return event.retrieveUser();
    }

    @EventValue
    public static Channel channel(MessagePollVoteRemoveEvent event) {
        return event.getChannel();
    }

    @EventValue
    public static @Nullable Guild guild(MessagePollVoteRemoveEvent event) {
        return event.isFromGuild() ? event.getGuild() : null;
    }

    @EventValue
    public static Number messageId(MessagePollVoteRemoveEvent event) {
        return event.getMessageIdLong();
    }
}
