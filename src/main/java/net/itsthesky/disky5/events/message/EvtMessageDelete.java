package net.itsthesky.disky5.events.message;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Message Delete",
        patterns = {"message delete[d]", "[discord] message delete[d]"},
        jda = MessageDeleteEvent.class
)
@Documentation(
        name = "Message Delete",
        description = {
                "Fired when any message is deleted.",
                "This will be fired, by default, both guild & private messages, use the 'event is from guild' condition to avoid confusion."
        },
        examples = {
                "on message delete:",
                "\tbroadcast \"A message was deleted in %event-channel%!\""
        },
        since = "5.0.0-a1"
)
public final class EvtMessageDelete {

    @EventValue
    public static @Nullable Guild guild(MessageDeleteEvent event) {
        return event.isFromGuild() ? event.getGuild() : null;
    }

    @EventValue
    public static Channel channel(MessageDeleteEvent event) {
        return event.getChannel();
    }

    @EventValue
    public static Number messageId(MessageDeleteEvent event) {
        return event.getMessageIdLong();
    }
}
