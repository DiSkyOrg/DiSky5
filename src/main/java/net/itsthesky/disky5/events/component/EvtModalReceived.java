package net.itsthesky.disky5.events.component;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Modal Received",
        patterns = {"modal (click[ed]|receive[d])"},
        jda = ModalInteractionEvent.class
)
@Documentation(
        name = "Modal Received",
        description = {
                "Fired when a modal has been sent to the bot from any user.",
                "Use 'received modal' to get the modal id. Don't forget to either reply or defer the interaction.",
                "",
                "!!! warning \"Modal can NOT be shown in this interaction!\""
        },
        examples = {
                "on modal received:",
                "\treply with hidden \"You submitted the modal with id '%received modal%'!\""
        },
        since = "5.0.0-a1"
)
public final class EvtModalReceived {

    @EventValue
    public static String modalId(ModalInteractionEvent event) {
        return event.getModalId();
    }

    @EventValue
    public static @Nullable Guild guild(ModalInteractionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static User user(ModalInteractionEvent event) {
        return event.getUser();
    }

    @EventValue
    public static @Nullable Member member(ModalInteractionEvent event) {
        return event.getMember();
    }

    @EventValue
    public static @Nullable Number messageId(ModalInteractionEvent event) {
        final Message message = event.getMessage();
        return message == null ? null : message.getIdLong();
    }

    @EventValue
    public static @Nullable Message message(ModalInteractionEvent event) {
        return event.getMessage();
    }

    @EventValue
    public static @Nullable Channel channel(ModalInteractionEvent event) {
        return event.getChannel();
    }

    @EventExpression("receive[d] (id|modal)")
    public static String receivedId(ModalInteractionEvent event) {
        return event.getModalId();
    }
}
