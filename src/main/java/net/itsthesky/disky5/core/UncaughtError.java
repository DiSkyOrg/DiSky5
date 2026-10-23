package net.itsthesky.disky5.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.itsthesky.disky5.api.events.CancellationAware;
import org.jetbrains.annotations.Nullable;

public final class UncaughtError implements GenericEvent, CancellationAware {

    private final DiscordError error;
    private final @Nullable Bot bot;
    private boolean cancelled;

    public UncaughtError(DiscordError error, @Nullable Bot bot) {
        this.error = error;
        this.bot = bot;
    }

    public DiscordError error() { return error; }
    public @Nullable Bot bot() { return bot; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    @Override
    @SuppressWarnings("NullableProblems")
    public @Nullable JDA getJDA() { return bot == null ? null : bot.anyShard(); }

    @Override
    public long getResponseNumber() { return -1; }

    @Override
    public @Nullable DataObject getRawData() { return null; }
}
