package net.itsthesky.disky5.events.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventFilter;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.api.commands.application.ApplicationInvocation;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Message Command",
        patterns = {"message command [receive[d]]"},
        jda = MessageContextInteractionEvent.class
)
@Documentation(
        name = "Message Command",
        description = {
                "Fired when someone click on a message application command.",
                "Use `used command` to get the command name and `target message` for the targeted message. Don't forget to either reply to the interaction. Defer doesn't work here."
        },
        examples = {
                "on message command:",
                "\treply with \"You used %used message command% on %target message%\""
        },
        since = "5.0.0-a1"
)
public final class EvtMessageCommand {

    // A structure-declared invocation goes through the bus for its cooldown event only; the generic
    // event already fired on the original interaction.
    @EventFilter
    public static boolean accept(MessageContextInteractionEvent event) {
        return !(event instanceof ApplicationInvocation);
    }

    @EventValue
    public static String name(MessageContextInteractionEvent event) {
        return event.getName();
    }

    @EventValue
    public static User user(MessageContextInteractionEvent event) {
        return event.getUser();
    }

    @EventValue
    public static @Nullable Guild guild(MessageContextInteractionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable Member member(MessageContextInteractionEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Message target(MessageContextInteractionEvent event) {
        return event.getTarget();
    }

    @EventValue
    public static Channel channel(MessageContextInteractionEvent event) {
        return event.getChannel();
    }

    @EventExpression("(execute|use)[d] [message] command")
    public static String executedCommand(MessageContextInteractionEvent event) {
        return event.getFullCommandName();
    }

    @EventExpression("[the] target message")
    public static Message targetMessage(MessageContextInteractionEvent event) {
        return event.getTarget();
    }
}
