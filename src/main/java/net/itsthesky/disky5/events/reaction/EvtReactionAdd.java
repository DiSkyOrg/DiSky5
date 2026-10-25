package net.itsthesky.disky5.events.reaction;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Reaction Add",
        patterns = {"(reaction|emote)[s] add[ed]"},
        jda = MessageReactionAddEvent.class
)
@Documentation(
        name = "Reaction Add",
        description = {
                "Fired when a message, that can be seen by the bot, receive a reaction.",
                "This will be fired, by default, both guild & private messages, use the 'event is from guild' condition to avoid confusion."
        },
        examples = {
                "on reaction add:",
                "\treply with \"You added %reaction emoji%!\""
        },
        since = "5.0.0-a1"
)
public final class EvtReactionAdd {

    @EventValue
    public static Channel channel(MessageReactionAddEvent event) {
        return event.getChannel();
    }

    @EventValue
    public static @Nullable Guild guild(MessageReactionAddEvent event) {
        return event.isFromGuild() ? event.getGuild() : null;
    }

    @EventValue
    public static @Nullable Member member(MessageReactionAddEvent event) {
        return event.getMember();
    }

    @EventValue
    public static @Nullable User user(MessageReactionAddEvent event) {
        return event.getUser();
    }

    @EventValue
    public static Emoji emoji(MessageReactionAddEvent event) {
        return event.getEmoji();
    }

    // What a script reads: Skript parses "event-emoji" as a plural (its "us" -> "i" rule), so it never resolves.
    @EventExpression("[the] reaction (emoji|emote)")
    public static Emoji reactionEmoji(MessageReactionAddEvent event) {
        return event.getEmoji();
    }

    @EventValue
    public static @Nullable CustomEmoji emote(MessageReactionAddEvent event) {
        return event.getEmoji() instanceof CustomEmoji custom ? custom : null;
    }

    @EventValue
    public static MessageReaction reaction(MessageReactionAddEvent event) {
        return event.getReaction();
    }

    @EventValue
    public static long messageAuthorId(MessageReactionAddEvent event) {
        return event.getMessageAuthorIdLong();
    }

    // `reaction` is mandatory: a bare "the message" is Skript's own ExprChatMessage.
    @EventExpression("[the] reaction message")
    public static net.dv8tion.jda.api.requests.RestAction<net.dv8tion.jda.api.entities.Message> message(MessageReactionAddEvent event) {
        return event.retrieveMessage();
    }
}
