package net.itsthesky.disky5.events.reaction;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
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
                "\treply with \"You removed %reaction emoji%!\""
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
    public static Emoji emoji(MessageReactionRemoveEvent event) {
        return event.getEmoji();
    }

    // What a script reads: Skript parses "event-emoji" as a plural (its "us" -> "i" rule), so it never resolves.
    @EventExpression("[the] reaction (emoji|emote)")
    public static Emoji reactionEmoji(MessageReactionRemoveEvent event) {
        return event.getEmoji();
    }

    @EventValue
    public static @Nullable CustomEmoji emote(MessageReactionRemoveEvent event) {
        return event.getEmoji() instanceof CustomEmoji custom ? custom : null;
    }

    // `reaction` is mandatory: a bare "the message" is Skript's own ExprChatMessage.
    @EventExpression("[the] reaction message")
    public static net.dv8tion.jda.api.requests.RestAction<net.dv8tion.jda.api.entities.Message> message(MessageReactionRemoveEvent event) {
        return event.retrieveMessage();
    }
}
