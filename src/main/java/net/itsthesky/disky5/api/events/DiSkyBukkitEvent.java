package net.itsthesky.disky5.api.events;

import net.dv8tion.jda.api.events.GenericEvent;
import net.itsthesky.disky5.core.Bot;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DiSkyBukkitEvent extends Event {

    private final GenericEvent jdaEvent;
    private final @Nullable Bot bot;

    protected DiSkyBukkitEvent(GenericEvent jdaEvent, @Nullable Bot bot) {
        this.jdaEvent = jdaEvent;
        this.bot = bot;
    }

    public GenericEvent getJdaEvent() {
        return jdaEvent;
    }

    public @Nullable Bot getBot() {
        return bot;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
