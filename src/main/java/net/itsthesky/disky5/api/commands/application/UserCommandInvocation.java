package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.itsthesky.disky5.core.Bot;
import org.jetbrains.annotations.Nullable;

public final class UserCommandInvocation extends UserContextInteractionEvent implements ApplicationInvocation {

    private final ContextCommandModel model;
    private final @Nullable Bot bot;
    private long remainingCooldown;
    private volatile boolean cancelled;

    public UserCommandInvocation(UserContextInteractionEvent source, ContextCommandModel model, @Nullable Bot bot) {
        super(source.getJDA(), source.getResponseNumber(), source.getInteraction());
        this.model = model;
        this.bot = bot;
    }

    @Override
    public ContextCommandModel model() { return model; }

    @Override
    public @Nullable Bot bot() { return bot; }

    @Override
    public long remainingCooldown() { return remainingCooldown; }

    public void remainingCooldown(long millis) { this.remainingCooldown = millis; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
