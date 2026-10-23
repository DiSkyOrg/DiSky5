package net.itsthesky.disky5.events.reaction;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Reaction Remove",
        patterns = {"(reaction|emote)[s] remove[d]"},
        jda = MessageReactionRemoveEvent.class
)
@Documentation(
        name = "Reaction Remove",
        description = {
                "Fired when an user remove a reaction from a specific message.",
                "This will be fired, by default, both guild & private messages, use the 'event is from guild' condition to avoid confusion."
        },
        examples = {
                "on reaction remove:",
                "\treply with \"You removed %event-emote%!\""
        },
        since = "5.0.0-a1"
)
public final class EvtReactionRemove {

    @EventValue
    public static Channel channel(MessageReactionRemoveEvent event) {
        return event.getChannel();
    }

    @EventValue
    public static @Nullable Guild guild(MessageReactionRemoveEvent event) {
        return event.isFromGuild() ? event.getGuild() : null;
    }

    @EventValue
    public static @Nullable Member member(MessageReactionRemoveEvent event) {
        return event.getMember();
    }

    @EventValue
    public static @Nullable User user(MessageReactionRemoveEvent event) {
        return event.getUser();
    }

    @EventValue
    public static EmojiUnion emoji(MessageReactionRemoveEvent event) {
        return event.getEmoji();
    }

    // `reaction` is mandatory: a bare "the message" is Skript's own ExprChatMessage.
    @EventExpression("[the] reaction message")
    public static net.dv8tion.jda.api.requests.RestAction<net.dv8tion.jda.api.entities.Message> message(MessageReactionRemoveEvent event) {
        return event.retrieveMessage();
    }
}
