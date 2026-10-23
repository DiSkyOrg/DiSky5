package net.itsthesky.disky5.events.command;

import ch.njol.skript.util.Timespan;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventFilter;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.api.commands.CommandDefinition;
import net.itsthesky.disky5.api.commands.CommandFailure;
import net.itsthesky.disky5.api.commands.CommandInvocation;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
    name = "Discord Command Failed",
    patterns = {"discord command (fail|failed|denied)"},
    jda = CommandInvocation.class
)
@Documentation(
    name = "Discord Command Failed",
    description = {
        "Fired when a 'discord command' was recognised but refused: wrong channel, missing role or "
            + "permission, cooldown, wrong bot or invalid arguments. 'failure reason' tells which "
            + "(\"invalid arguments\", \"wrong channel\", \"missing role\", \"missing permission\", "
            + "\"cooldown\", \"wrong bot\"); 'remaining cooldown' is set for a cooldown.",
        "The configured 'permission message' / 'cooldown message' / 'usage message' of the command is "
            + "sent before this event fires; use this event for anything more custom."
    },
    examples = {
        "on discord command failed:",
        "\tif failure reason is \"cooldown\":",
        "\t\treply with \"Wait %remaining cooldown% before using %name of event-discordcommand% again.\""
    },
    since = "5.0.0-a1"
)
public final class EvtDiscordCommandFailed {

    @EventFilter
    public static boolean accept(CommandInvocation invocation) {
        return invocation.failure() != null;
    }

    @EventValue
    public static CommandDefinition command(CommandInvocation invocation) {
        return invocation.definition();
    }

    @EventValue
    public static Message message(CommandInvocation invocation) {
        return invocation.message();
    }

    @EventValue
    public static User user(CommandInvocation invocation) {
        return invocation.user();
    }

    @EventValue
    public static @Nullable Member member(CommandInvocation invocation) {
        return invocation.member();
    }

    @EventValue
    public static @Nullable Guild guild(CommandInvocation invocation) {
        return invocation.guild();
    }

    @EventValue
    public static Channel channel(CommandInvocation invocation) {
        return invocation.channel();
    }

    @EventExpression("[the] failure reason")
    public static @Nullable String failureReason(CommandInvocation invocation) {
        CommandFailure failure = invocation.failure();
        return failure == null ? null : failure.display();
    }

    @EventExpression("[the] remaining cooldown")
    public static Timespan remainingCooldown(CommandInvocation invocation) {
        return new Timespan(invocation.remainingCooldown());
    }

    @EventExpression("[the] used prefix")
    public static String usedPrefix(CommandInvocation invocation) {
        return invocation.usedPrefix();
    }

    @EventExpression("[the] used alias")
    public static String usedAlias(CommandInvocation invocation) {
        return invocation.usedAlias();
    }

    @EventExpression("[the] used arg[ument]s")
    public static String usedArguments(CommandInvocation invocation) {
        return invocation.rawArguments();
    }
}
