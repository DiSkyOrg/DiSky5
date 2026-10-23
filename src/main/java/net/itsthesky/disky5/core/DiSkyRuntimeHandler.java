package net.itsthesky.disky5.core;

import ch.njol.skript.config.Node;
import dev.faststats.ErrorTracker;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.api.async.DiSkyAsync;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.api.events.DiSkyEventBus;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class DiSkyRuntimeHandler {

    private DiSkyRuntimeHandler() {}

    private static final Map<Event, List<DiscordError>> BUFFERS =
        Collections.synchronizedMap(new WeakHashMap<>());
    private static volatile @Nullable DiscordError lastError;
    private static final ThreadLocal<Boolean> DISPATCHING = ThreadLocal.withInitial(() -> false);

    public static void start(@Nullable Event event) {
        if (event != null)
            BUFFERS.put(event, Collections.synchronizedList(new ArrayList<>()));
    }

    public static List<DiscordError> end(@Nullable Event event) {
        List<DiscordError> collected = event == null ? null : BUFFERS.remove(event);
        return collected == null ? List.of() : new ArrayList<>(collected);
    }

    public static void error(Throwable t) {
        error(t, null, null, true);
    }

    public static void error(Throwable t, @Nullable Node node) {
        error(t, node, null, true);
    }

    public static void error(Throwable t, @Nullable Node node, @Nullable Event event) {
        error(t, node, event, true);
    }

    public static void error(Throwable t, @Nullable Node node, @Nullable Event event,
                             boolean displayStacktrace) {
        DiscordError error = DiscordError.from(t, node);
        lastError = error;

        Event target = event != null ? event : DiSkyAsync.currentEvent();
        List<DiscordError> buffer = target == null ? null : BUFFERS.get(target);
        if (buffer != null) {
            buffer.add(error);
            return;
        }

        boolean fromHandler = raisedByErrorHandler(target);
        Bot bot = target instanceof DiSkyBukkitEvent bukkit ? bukkit.getBot() : null;
        runOnMain(() -> {
            DiSkyRuntimeErrorEvent errorEvent = new DiSkyRuntimeErrorEvent(error);
            Bukkit.getPluginManager().callEvent(errorEvent);
            boolean silenced = errorEvent.isCancelled();
            if (!fromHandler && dispatch(new UncaughtError(error, bot)))
                silenced = true;
            if (!silenced)
                print(error, displayStacktrace);

            DiSky5.ERROR_TRACKER.trackError(error.getCause());
        });
    }

    static boolean raisedByErrorHandler(@Nullable Event target) {
        return DISPATCHING.get()
            || target instanceof DiSkyBukkitEvent bukkit && bukkit.getJdaEvent() instanceof UncaughtError;
    }

    static boolean dispatch(UncaughtError uncaught) {
        DISPATCHING.set(true);
        try {
            DiSkyEventBus.INSTANCE.onEvent(uncaught);
        } finally {
            DISPATCHING.set(false);
        }
        return uncaught.isCancelled();
    }

    public static @Nullable DiscordError getLastError() {
        return lastError;
    }

    private static void runOnMain(Runnable runnable) {
        DiSky5 plugin = DiSky5.getInstance();
        if (plugin == null || !plugin.isEnabled() || Bukkit.isPrimaryThread())
            runnable.run();
        else
            Bukkit.getScheduler().runTask(plugin, runnable);
    }

    private static void print(DiscordError error, boolean displayStacktrace) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage("[DiSky] ---------------- Discord error ----------------");
        console.sendMessage("[DiSky] " + error);
        Node node = error.getNode();
        if (node != null)
            console.sendMessage("[DiSky] at " + node.getKey() + " (" + node.getConfig().getFileName()
                + ", line " + node.getLine() + ")");
        if (displayStacktrace && error.getCause() != null)
            for (StackTraceElement element : error.getCause().getStackTrace())
                console.sendMessage("[DiSky]     " + element);
        console.sendMessage("[DiSky] -----------------------------------------------");
    }

    public static final class DiSkyRuntimeErrorEvent extends Event implements Cancellable {

        private static final HandlerList HANDLERS = new HandlerList();

        private final DiscordError error;
        private boolean cancelled;

        public DiSkyRuntimeErrorEvent(DiscordError error) {
            this.error = error;
        }

        public DiscordError getError() {
            return error;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            return HANDLERS;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS;
        }
    }
}
