package net.itsthesky.disky5.events.message;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Message Receive",
        patterns = {"message receive[d]", "[discord] message receive[d]"},
        jda = MessageReceivedEvent.class,
        intents = "MESSAGE_CONTENT"
)
@Documentation(
        name = "Message Receive",
        description = "Fired when a bot receives a message — in a guild or in private messages by default. "
                + "Use 'event is from guild' to filter.",
        examples = {
            "on message received:",
            "\tif event-message contains \"ping\":",
            "\t\treply with \"pong!\""
        },
        since = "5.0.0-a1"
)
public final class EvtMessageReceive {

    @EventValue
    public static Message message(MessageReceivedEvent event) {
        return event.getMessage();
    }

    @EventValue
    public static @Nullable Guild guild(MessageReceivedEvent event) {
        return event.isFromGuild() ? event.getGuild() : null;
    }

    @EventValue
    public static @Nullable Member member(MessageReceivedEvent event) {
        return event.getMember();
    }

    @EventValue
    public static User user(MessageReceivedEvent event) {
        return event.getAuthor();
    }

    @EventValue
    public static Channel channel(MessageReceivedEvent event) {
        return event.getChannel();
    }
}

