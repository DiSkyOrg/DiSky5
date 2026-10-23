package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.itsthesky.disky5.api.commands.ArgumentInvocation;
import net.itsthesky.disky5.core.Bot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SlashInvocation extends SlashCommandInteractionEvent implements ApplicationInvocation, ArgumentInvocation {

    private final SlashCommandModel model;
    private final Object[][] values;
    private final @Nullable Bot bot;
    private long remainingCooldown;
    private volatile boolean cancelled;

    public SlashInvocation(SlashCommandInteractionEvent source, SlashCommandModel model, Object[][] values, @Nullable Bot bot) {
        super(source.getJDA(), source.getResponseNumber(), source.getInteraction());
        this.model = model;
        this.values = values;
        this.bot = bot;
    }

    @Override
    public SlashCommandModel model() { return model; }

    @Override
    public @Nullable Bot bot() { return bot; }

    @Override
    public long remainingCooldown() { return remainingCooldown; }

    public void remainingCooldown(long millis) { this.remainingCooldown = millis; }

    @Override
    public Object @NotNull [] value(int index) {
        return index >= 0 && index < values.length && values[index] != null ? values[index] : new Object[0];
    }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
