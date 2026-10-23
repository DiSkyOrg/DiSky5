package net.itsthesky.disky5.events.message;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Message Edit",
        patterns = {"message edit[ed]", "[discord] message edit[ed]"},
        jda = MessageUpdateEvent.class,
        intents = "MESSAGE_CONTENT"
)
@Documentation(
        name = "Message Edit",
        description = {
                "Fired when any message is edited / updated.",
                "This will be fired, by default, both guild & private messages, use the 'event is from guild' condition to avoid confusion."
        },
        examples = {
                "on message edit:",
                "\tbroadcast \"%event-message% was edited!\""
        },
        since = "5.0.0-a1"
)
public final class EvtMessageEdit {

    @EventValue
    public static @Nullable Guild guild(MessageUpdateEvent event) {
        return event.isFromGuild() ? event.getGuild() : null;
    }

    @EventValue
    public static Message message(MessageUpdateEvent event) {
        return event.getMessage();
    }

    @EventValue
    public static Channel channel(MessageUpdateEvent event) {
        return event.getChannel();
    }
}
