package net.itsthesky.disky5.events.poll;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.poll.MessagePollVoteAddEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Poll Vote Add",
        patterns = {"[message] poll vote add[ed]"},
        jda = MessagePollVoteAddEvent.class
)
@Documentation(
        name = "Poll Vote Add",
        description = "Fired when a user adds their vote to a poll in a message.",
        examples = {
            "on poll vote add:"
        },
        since = "5.0.0-a1"
)
public final class EvtPollVoteAdd {

    @EventValue
    public static RestAction<Message> message(MessagePollVoteAddEvent event) {
        return event.retrieveMessage();
    }

    @EventValue
    public static @Nullable RestAction<Member> member(MessagePollVoteAddEvent event) {
        return event.isFromGuild() ? event.retrieveMember() : null;
    }

    @EventValue
    public static RestAction<User> user(MessagePollVoteAddEvent event) {
        return event.retrieveUser();
    }

    @EventValue
    public static Channel channel(MessagePollVoteAddEvent event) {
        return event.getChannel();
    }

    @EventValue
    public static @Nullable Guild guild(MessagePollVoteAddEvent event) {
        return event.isFromGuild() ? event.getGuild() : null;
    }

    @EventValue
    public static Number messageId(MessagePollVoteAddEvent event) {
        return event.getMessageIdLong();
    }
}
