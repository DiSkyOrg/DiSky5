package net.itsthesky.disky5.events.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Slash Command Completion Event",
        patterns = {"slash completion [receive[d]]"},
        jda = CommandAutoCompleteInteractionEvent.class
)
@Documentation(
        name = "Slash Command Completion Event",
        description = {
                "Fired when Discord requests argument autocompletion for a slash command.",
                "Use 'event-string' to get the command name.",
                "Use the 'return' effect to provide completion choices to the user.",
                "You can access the focused argument with 'current argument' and other argument values with 'argument \"name\" as type'."
        },
        examples = {
                "on slash completion:",
                "\tif event-string is \"mycommand\":",
                "\t\tif current argument is \"option\":",
                "\t\t\treturn choice \"Option 1\" with value \"option1\", choice \"Option 2\" with value \"option2\""
        },
        since = "5.0.0-a1"
)
public final class EvtSlashCommandCompletion {

    @EventValue
    public static @Nullable Guild guild(CommandAutoCompleteInteractionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable Member member(CommandAutoCompleteInteractionEvent event) {
        return event.getMember();
    }

    @EventValue
    public static User user(CommandAutoCompleteInteractionEvent event) {
        return event.getUser();
    }

    @EventValue
    public static String fullCommandName(CommandAutoCompleteInteractionEvent event) {
        return event.getFullCommandName();
    }

    @EventValue
    public static Channel channel(CommandAutoCompleteInteractionEvent event) {
        return event.getChannel();
    }

    @EventExpression("current( |-)arg[ument] [name]")
    public static String currentArgument(CommandAutoCompleteInteractionEvent event) {
        return event.getInteraction().getFocusedOption().getName();
    }
}
