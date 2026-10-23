package net.itsthesky.disky5.events.command;

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
import net.itsthesky.disky5.api.commands.CommandInvocation;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
    name = "Discord Command Used",
    patterns = {"discord command (use|used|invoked)"},
    jda = CommandInvocation.class,
    cancellable = true
)
@Documentation(
    name = "Discord Command Used",
    description = {
        "Fired when a message matches a 'discord command' structure, before its checks (channel, roles, "
            + "permissions, cooldown) and before its trigger runs. Cancel it to stop the command entirely.",
        "'event-discordcommand' is the command, 'used prefix' / 'used alias' / 'used arguments' tell how "
            + "it was typed."
    },
    examples = {
        "on discord command used:",
        "\tif name of event-discordcommand is \"eval\":",
        "\t\tcancel event"
    },
    since = "5.0.0-a1"
)
public final class EvtDiscordCommandUsed {

    @EventFilter
    public static boolean accept(CommandInvocation invocation) {
        return invocation.failure() == null;
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
