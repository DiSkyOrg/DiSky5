package net.itsthesky.disky5.api.commands.application;

import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.util.Timespan;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.api.commands.ArgumentLocals;
import net.itsthesky.disky5.api.commands.CooldownTracker;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.api.events.DiSkyEventBus;
import net.itsthesky.disky5.core.Bot;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_ApplicationCommandCooldown;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_MessageCommand;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_SlashCommand;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_SlashCommandCompletionEvent;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_UserCommand;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

public final class ApplicationCommandDispatcher {

    private ApplicationCommandDispatcher() {}

    public static void install() {
        DiSkyEventBus bus = DiSkyEventBus.INSTANCE;
        bus.subscribe(SlashCommandInteractionEvent.class, ApplicationCommandDispatcher::onSlash);
        bus.subscribe(CommandAutoCompleteInteractionEvent.class, ApplicationCommandDispatcher::onAutoComplete);
        bus.subscribe(UserContextInteractionEvent.class, (raw, bot) -> onContext((UserContextInteractionEvent) raw, bot,
            Command.Type.USER, (event, model) -> new UserCommandInvocation(event, model, bot), GeneratedBukkitEvt_UserCommand::new));
        bus.subscribe(MessageContextInteractionEvent.class, (raw, bot) -> onContext((MessageContextInteractionEvent) raw, bot,
            Command.Type.MESSAGE, (event, model) -> new MessageCommandInvocation(event, model, bot), GeneratedBukkitEvt_MessageCommand::new));
    }

    private static void onSlash(GenericEvent raw, @Nullable Bot bot) {
        if (raw instanceof ApplicationInvocation) return;
        SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) raw;
        SlashCommandModel model = ApplicationCommandRegistry.INSTANCE.slash(event.getFullCommandName());
        if (model == null || model.trigger() == null) return;
        DiSky5 plugin = DiSky5.getInstance();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<SlashArgument> arguments = model.arguments();
            Object[][] values = new Object[arguments.size()][];
            DiSkyBukkitEvent errorScope = new GeneratedBukkitEvt_SlashCommand(event, bot);
            for (int i = 0; i < arguments.size(); i++) {
                SlashArgument argument = arguments.get(i);
                try {
                    values[i] = argument.resolve(event.getOption(argument.name()));
                } catch (Throwable t) {
                    DiSkyRuntimeHandler.error(t, null, errorScope);
                    values[i] = new Object[0];
                }
            }
            SlashInvocation invocation = new SlashInvocation(event, model, values, bot);
            Bukkit.getScheduler().runTask(plugin, () -> run(invocation, model, arguments,
                new GeneratedBukkitEvt_SlashCommand(invocation, bot), bot));
        });
    }

    private static <E extends GenericCommandInteractionEvent> void onContext(
            E event, @Nullable Bot bot, Command.Type type,
            BiFunction<E, ContextCommandModel, GenericCommandInteractionEvent> invocationFactory,
            BiFunction<GenericEvent, Bot, DiSkyBukkitEvent> wrapperFactory) {
        if (event instanceof ApplicationInvocation) return;
        ContextCommandModel model = ApplicationCommandRegistry.INSTANCE.context(type, event.getName());
        if (model == null || model.trigger() == null) return;
        GenericCommandInteractionEvent invocation = invocationFactory.apply(event, model);
        Bukkit.getScheduler().runTask(DiSky5.getInstance(), () -> run(invocation, model, null, wrapperFactory.apply(invocation, bot), bot));
    }

    private static void run(GenericCommandInteractionEvent invocation, ApplicationCommandModel model,
                            @Nullable List<SlashArgument> arguments, DiSkyBukkitEvent bukkit, @Nullable Bot bot) {
        DiSky5 plugin = DiSky5.getInstance();
        if (plugin == null || !plugin.isEnabled()) return;
        Trigger trigger = model.trigger();
        if (trigger == null) return;
        ApplicationInvocation state = (ApplicationInvocation) invocation;

        CooldownTracker cooldowns = model.cooldowns();
        long key = cooldowns.key(invocation.getUser(), invocation.getGuild(), invocation.getChannel());
        long remaining = cooldowns.remaining(key);
        if (remaining > 0) {
            setRemaining(state, remaining);
            DiSkyEventBus.INSTANCE.onEvent(invocation);
            Trigger onCooldown = model.onCooldown();
            if (onCooldown != null) {
                DiSkyBukkitEvent cooldownEvent = new GeneratedBukkitEvt_ApplicationCommandCooldown(invocation, bot);
                if (arguments != null && invocation instanceof SlashInvocation slash)
                    ArgumentLocals.set(arguments, slash, cooldownEvent);
                TriggerItem.walk(onCooldown, cooldownEvent);
            }
            if (!state.isCancelled()) {
                if (!invocation.isAcknowledged())
                    invocation.reply("You can use this command again in " + new Timespan(remaining) + ".")
                        .setEphemeral(true).queue(null, t -> {});
                return;
            }
        }
        cooldowns.markUsed(key);
        if (arguments != null && invocation instanceof SlashInvocation slash)
            ArgumentLocals.set(arguments, slash, bukkit);
        TriggerItem.walk(trigger, bukkit);
    }

    private static void setRemaining(ApplicationInvocation invocation, long remaining) {
        if (invocation instanceof SlashInvocation slash) slash.remainingCooldown(remaining);
        else if (invocation instanceof UserCommandInvocation user) user.remainingCooldown(remaining);
        else if (invocation instanceof MessageCommandInvocation message) message.remainingCooldown(remaining);
    }

    private static void onAutoComplete(GenericEvent raw, @Nullable Bot bot) {
        CommandAutoCompleteInteractionEvent event = (CommandAutoCompleteInteractionEvent) raw;
        SlashCommandModel model = ApplicationCommandRegistry.INSTANCE.slash(event.getFullCommandName());
        if (model == null) return;
        String focused = event.getFocusedOption().getName();
        SlashArgument argument = null;
        for (SlashArgument candidate : model.arguments())
            if (candidate.name().equals(focused)) argument = candidate;
        if (argument == null) return;
        Trigger completion = argument.completion();
        DiSky5 plugin = DiSky5.getInstance();
        if (completion != null) {
            Bukkit.getScheduler().runTask(plugin, () -> TriggerItem.walk(completion, new GeneratedBukkitEvt_SlashCommandCompletionEvent(event, bot)));
            return;
        }
        var completer = argument.type().completer();
        if (completer == null) return;
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                List<Command.Choice> choices = completer.apply(event.getFocusedOption().getValue());
                if (choices.size() > OptionData.MAX_CHOICES) choices = choices.subList(0, OptionData.MAX_CHOICES);
                event.replyChoices(choices).queue();
            } catch (Throwable t) {
                DiSkyRuntimeHandler.error(t, null, new GeneratedBukkitEvt_SlashCommandCompletionEvent(event, bot));
            }
        });
    }
}
