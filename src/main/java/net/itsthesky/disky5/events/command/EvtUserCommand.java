package net.itsthesky.disky5.events.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventFilter;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.api.commands.application.ApplicationInvocation;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "User Command",
        patterns = {"user command [receive[d]]"},
        jda = UserContextInteractionEvent.class
)
@Documentation(
        name = "User Command",
        description = {
                "Fired when someone click on a user application command.",
                "Use `used command` to get the command name and `target user` for the targeted user. Don't forget to either reply to the interaction. Defer doesn't work here."
        },
        examples = {
                "on user command:",
                "\treply with \"You used %used user command% on %target user%\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserCommand {

    // A structure-declared invocation goes through the bus for its cooldown event only; the generic
    // event already fired on the original interaction.
    @EventFilter
    public static boolean accept(UserContextInteractionEvent event) {
        return !(event instanceof ApplicationInvocation);
    }

    @EventValue
    public static String name(UserContextInteractionEvent event) {
        return event.getName();
    }

    @EventValue
    public static User user(UserContextInteractionEvent event) {
        return event.getUser();
    }

    @EventValue
    public static @Nullable Guild guild(UserContextInteractionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable Member member(UserContextInteractionEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Channel channel(UserContextInteractionEvent event) {
        return event.getChannel();
    }

    @EventExpression("(execute|use)[d] [user] command")
    public static String executedCommand(UserContextInteractionEvent event) {
        return event.getFullCommandName();
    }

    @EventExpression("[the] target user")
    public static User targetUser(UserContextInteractionEvent event) {
        return event.getTarget();
    }
}
