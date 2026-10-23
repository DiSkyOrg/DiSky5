package net.itsthesky.disky5.api.commands;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.VariableString;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.api.events.DiSkyEventBus;
import net.itsthesky.disky5.core.Bot;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_DiscordCommandFailed;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_DiscordCommandUsed;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class CommandDispatcher {

    private CommandDispatcher() {}

    public static void install() {
        DiSkyEventBus.INSTANCE.subscribe(MessageReceivedEvent.class, CommandDispatcher::onMessage);
    }

    private static void onMessage(GenericEvent raw, @Nullable Bot bot) {
        MessageReceivedEvent event = (MessageReceivedEvent) raw;
        if (event.getAuthor().isBot() || event.isWebhookMessage())
            return;
        String content = event.getMessage().getContentRaw().trim();
        if (content.isEmpty())
            return;
        for (CommandDefinition definition : CommandRegistry.INSTANCE.all()) {
            CommandInvocation invocation = new CommandInvocation(definition, event, bot);
            DiSkyBukkitEvent bukkit = new GeneratedBukkitEvt_DiscordCommandUsed(invocation, bot);
            if (match(content, definition, invocation, bukkit)) {
                DiSky5 plugin = DiSky5.getInstance();
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    CommandFailure argumentFailure = resolveArguments(invocation, bukkit);
                    Bukkit.getScheduler().runTask(plugin, () -> execute(invocation, bukkit, argumentFailure));
                });
            }
        }
    }

    private static boolean match(String content, CommandDefinition definition, CommandInvocation invocation,
                                 DiSkyBukkitEvent bukkit) {
        for (String prefix : definition.prefixes(bukkit)) {
            for (String name : definition.names()) {
                String head = prefix + name;
                if (content.length() < head.length() || !content.regionMatches(true, 0, head, 0, head.length()))
                    continue;
                if (content.length() > head.length() && !Character.isWhitespace(content.charAt(head.length())))
                    continue;
                invocation.matched(prefix, name, content.substring(head.length()).trim());
                return true;
            }
        }
        return false;
    }

    private static @Nullable CommandFailure resolveArguments(CommandInvocation invocation, DiSkyBukkitEvent bukkit) {
        List<CommandArgument> arguments = invocation.definition().arguments();
        List<List<String>> raws = CommandTokenizer.assign(invocation.rawArguments(), arguments);
        Object[][] values = new Object[arguments.size()][];
        CommandFailure failure = null;
        for (int i = 0; i < arguments.size(); i++) {
            CommandArgument argument = arguments.get(i);
            List<String> raw = raws.get(i);
            if (raw == null) {
                if (!argument.optional()) {
                    failure = CommandFailure.INVALID_ARGUMENTS;
                    break;
                }
                Expression<?> def = argument.defaultValue();
                values[i] = def == null ? new Object[0] : def.getArray(bukkit);
                continue;
            }
            Object[] resolved = new Object[raw.size()];
            int count = 0;
            for (String token : raw) {
                Object value = resolve(argument, token, invocation, bukkit);
                if (value != null)
                    resolved[count++] = value;
            }
            if (count == 0 && !argument.optional()) {
                failure = CommandFailure.INVALID_ARGUMENTS;
                break;
            }
            values[i] = java.util.Arrays.copyOf(resolved, count);
        }
        invocation.values(values);
        return failure;
    }

    private static @Nullable Object resolve(CommandArgument argument, String token, CommandInvocation invocation,
                                            DiSkyBukkitEvent bukkit) {
        try {
            return argument.type().resolve(token, invocation);
        } catch (Throwable t) {
            if (!(t instanceof net.dv8tion.jda.api.exceptions.ErrorResponseException))
                DiSkyRuntimeHandler.error(t, null, bukkit);
            return null;
        }
    }

    private static void execute(CommandInvocation invocation, DiSkyBukkitEvent bukkit,
                                @Nullable CommandFailure argumentFailure) {
        DiSky5 plugin = DiSky5.getInstance();
        if (plugin == null || !plugin.isEnabled())
            return;
        CommandDefinition definition = invocation.definition();

        DiSkyEventBus.INSTANCE.onEvent(invocation);
        if (invocation.isCancelled())
            return;

        Bot bot = invocation.bot();
        if (!definition.bots().isEmpty()
                && (bot == null || definition.bots().stream().noneMatch(n -> n.equalsIgnoreCase(bot.getName())))) {
            fail(invocation, bukkit, CommandFailure.WRONG_BOT, null);
            return;
        }
        if (!definition.executableIn().contains(invocation.source().getChannelType())) {
            fail(invocation, bukkit, CommandFailure.WRONG_CHANNEL, null);
            return;
        }
        Member member = invocation.member();
        if (!definition.roles().isEmpty() && (member == null || !hasRole(member, definition.roles()))) {
            fail(invocation, bukkit, CommandFailure.MISSING_ROLE, null);
            return;
        }
        if (!definition.permissions().isEmpty() && (member == null || !member.hasPermission(definition.permissions()))) {
            fail(invocation, bukkit, CommandFailure.MISSING_PERMISSION, definition.permissionMessage());
            return;
        }
        CooldownTracker cooldowns = definition.cooldowns();
        long cooldownKey = cooldowns.key(invocation.user(), invocation.guild(), invocation.channel());
        long remaining = cooldowns.remaining(cooldownKey);
        if (remaining > 0) {
            invocation.fail(CommandFailure.COOLDOWN, remaining);
            fail(invocation, bukkit, CommandFailure.COOLDOWN, definition.cooldownMessage());
            return;
        }
        if (argumentFailure != null) {
            fail(invocation, bukkit, argumentFailure, definition.usageMessage());
            return;
        }

        cooldowns.markUsed(cooldownKey);
        ArgumentLocals.set(definition.arguments(), invocation, bukkit);
        TriggerItem.walk(definition.trigger(), bukkit);
    }

    private static void fail(CommandInvocation invocation, DiSkyBukkitEvent bukkit, CommandFailure failure,
                             @Nullable VariableString message) {
        if (invocation.failure() == null)
            invocation.fail(failure, 0);
        if (message != null) {
            String text = message.toString(new GeneratedBukkitEvt_DiscordCommandFailed(invocation, invocation.bot()));
            if (text != null && !text.isBlank())
                invocation.channel().sendMessage(text).queue();
        }
        DiSkyEventBus.INSTANCE.onEvent(invocation);
    }

    private static boolean hasRole(Member member, java.util.Set<String> roles) {
        for (Role role : member.getRoles())
            for (String wanted : roles)
                if (wanted.equalsIgnoreCase(role.getName()) || wanted.equals(role.getId()))
                    return true;
        return false;
    }
}
