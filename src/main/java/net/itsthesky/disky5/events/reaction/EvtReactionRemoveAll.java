package net.itsthesky.disky5.events.reaction;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Reaction Remove All",
        patterns = {"(reaction|emote)[s] (remove[d] all|clear|reset)"},
        jda = MessageReactionRemoveAllEvent.class
)
@Documentation(
        name = "Reaction Remove All",
        description = {
                "Fired when an user remove every reactions from a message.",
                "This will be fired, by default, both guild & private messages, use the 'event is from guild' condition to avoid confusion."
        },
        examples = {
                "on reaction remove all:",
                "\tbroadcast \"All reactions were cleared!\""
        },
        since = "5.0.0-a1"
)
public final class EvtReactionRemoveAll {

    @EventValue
    public static Channel channel(MessageReactionRemoveAllEvent event) {
        return event.getChannel();
    }

    @EventValue
    public static @Nullable Guild guild(MessageReactionRemoveAllEvent event) {
        return event.isFromGuild() ? event.getGuild() : null;
    }

    // `reaction` is mandatory: a bare "the message" is Skript's own ExprChatMessage.
    @EventExpression("[the] reaction message")
    public static net.dv8tion.jda.api.requests.RestAction<net.dv8tion.jda.api.entities.Message> message(MessageReactionRemoveAllEvent event) {
        return event.getChannel().retrieveMessageById(event.getMessageId());
    }
}
