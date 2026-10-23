package net.itsthesky.disky5.events.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventFilter;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.api.commands.application.ApplicationInvocation;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Slash Command",
        patterns = {"slash command [receive[d]]"},
        jda = SlashCommandInteractionEvent.class
)
@Documentation(
        name = "Slash Command",
        description = {
                "Fired when a user execute a specific slash command.",
                "Use 'used command' to get the command name. Don't forget to either reply or defer the interaction, You can only defer using the wait pattern  e.g: 'defer the interaction and wait [silently].",
                "You can get value of arguments using 'argument \"name\" as string' for example."
        },
        examples = {
                "on slash command:",
                "\tif used slash command is \"ping\":",
                "\t\treply with \"pong!\""
        },
        since = "5.0.0-a1"
)
public final class EvtSlashCommand {

    // A structure-declared invocation goes through the bus for its cooldown event only; the generic
    // event already fired on the original interaction.
    @EventFilter
    public static boolean accept(SlashCommandInteractionEvent event) {
        return !(event instanceof ApplicationInvocation);
    }

    @EventValue
    public static String fullCommandName(SlashCommandInteractionEvent event) {
        return event.getFullCommandName();
    }

    @EventValue
    public static User user(SlashCommandInteractionEvent event) {
        return event.getUser();
    }

    @EventValue
    public static @Nullable Guild guild(SlashCommandInteractionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable Member member(SlashCommandInteractionEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Channel channel(SlashCommandInteractionEvent event) {
        return event.getChannel();
    }

    @EventExpression("(execute|use)[d] [slash] command")
    public static String executedCommand(SlashCommandInteractionEvent event) {
        return event.getFullCommandName();
    }

    @EventExpression("[the] [slash] command id")
    public static Long commandId(SlashCommandInteractionEvent event) {
        return event.getCommandIdLong();
    }
}
