package net.itsthesky.disky5.api.events;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.core.Bot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public final class DiSkyEventBus implements EventListener {

    public static final DiSkyEventBus INSTANCE = new DiSkyEventBus();

    private DiSkyEventBus() {}

    public static final class Subscription {
        final Class<? extends GenericEvent> type;
        final BiConsumer<GenericEvent, Bot> handler;

        Subscription(Class<? extends GenericEvent> type, BiConsumer<GenericEvent, Bot> handler) {
            this.type = type;
            this.handler = handler;
        }
    }

    private final CopyOnWriteArrayList<Subscription> subscriptions = new CopyOnWriteArrayList<>();

    public Subscription subscribe(Class<? extends GenericEvent> type, BiConsumer<GenericEvent, Bot> handler) {
        Subscription subscription = new Subscription(type, handler);
        subscriptions.add(subscription);
        return subscription;
    }

    public void unsubscribe(@Nullable Subscription subscription) {
        if (subscription != null)
            subscriptions.remove(subscription);
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        Bot bot = DiSky5.getBots().resolve(event.getJDA());
        if (bot != null) {
            if (event instanceof ReadyEvent && !bot.onShardReady(event.getJDA()))
                return;
            if (event instanceof ShutdownEvent && !bot.onShardShutdown())
                return;
        }
        for (Subscription subscription : subscriptions) {
            if (!subscription.type.isInstance(event))
                continue;
            try {
                subscription.handler.accept(event, bot);
            } catch (Throwable t) {
                DiSky5.getInstance().getLogger().warning("[DiSky] event dispatch error ("
                    + event.getClass().getSimpleName() + "): " + t.getMessage());
            }
        }
    }
}
