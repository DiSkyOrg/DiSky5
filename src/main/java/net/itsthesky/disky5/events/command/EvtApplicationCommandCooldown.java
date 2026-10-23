package net.itsthesky.disky5.events.command;

import ch.njol.skript.util.Timespan;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventFilter;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.api.commands.application.ApplicationInvocation;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
    name = "Application Command Cooldown",
    patterns = {"(slash|user|message|application) command cooldown"},
    jda = GenericCommandInteractionEvent.class,
    cancellable = true
)
@Documentation(
    name = "Application Command Cooldown",
    description = {
        "Fired when a slash, user or message command declared by a structure is used while its 'cooldown' is "
            + "still running. 'remaining cooldown' is the time left. It is also the event the 'on cooldown:' "
            + "section of the structure runs in.",
        "Cancelling it lets the command run anyway. The interaction still has to be answered: reply "
            + "to it here, or cancel the event and let the trigger answer."
    },
    examples = {
        "on slash command cooldown:",
        "\treply with hidden \"Wait %remaining cooldown% before using /%used command% again.\""
    },
    since = "5.0.0-a2"
)
public final class EvtApplicationCommandCooldown {

    @EventFilter
    public static boolean accept(GenericCommandInteractionEvent event) {
        return event instanceof ApplicationInvocation invocation && invocation.onCooldown();
    }

    @EventValue
    public static User user(GenericCommandInteractionEvent event) {
        return event.getUser();
    }

    @EventValue
    public static @Nullable Member member(GenericCommandInteractionEvent event) {
        return event.getMember();
    }

    @EventValue
    public static @Nullable Guild guild(GenericCommandInteractionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(GenericCommandInteractionEvent event) {
        return event.getChannel();
    }

    @EventValue
    public static String fullCommandName(GenericCommandInteractionEvent event) {
        return event.getFullCommandName();
    }

    @EventExpression("[the] remaining cooldown")
    public static Timespan remainingCooldown(GenericCommandInteractionEvent event) {
        long millis = event instanceof ApplicationInvocation invocation ? invocation.remainingCooldown() : 0;
        return new Timespan(millis);
    }

    @EventExpression("(execute|use)[d] command")
    public static String executedCommand(GenericCommandInteractionEvent event) {
        return event.getFullCommandName();
    }
}
