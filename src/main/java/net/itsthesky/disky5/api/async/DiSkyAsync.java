package net.itsthesky.disky5.api.async;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public final class DiSkyAsync {

    private DiSkyAsync() {}

    private static final Set<Event> AWAITED =
        Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    public static boolean isAwaited(Event event) {
        return event != null && AWAITED.contains(event);
    }

    public static void setAwaited(Event event) {
        if (event != null)
            AWAITED.add(event);
    }

    public static void clearAwaited(Event event) {
        if (event != null)
            AWAITED.remove(event);
    }

    private static final ThreadLocal<Event> CURRENT_EVENT = new ThreadLocal<>();

    public static @Nullable Event currentEvent() {
        return CURRENT_EVENT.get();
    }

    public static @Nullable Event enterEvent(Event event) {
        Event previous = CURRENT_EVENT.get();
        CURRENT_EVENT.set(event);
        return previous;
    }

    public static void exitEvent(@Nullable Event previous) {
        if (previous == null)
            CURRENT_EVENT.remove();
        else
            CURRENT_EVENT.set(previous);
    }

    private static final ThreadLocal<Boolean> PARSING_AWAIT = ThreadLocal.withInitial(() -> false);

    public static boolean isParsingInsideAwait() {
        return PARSING_AWAIT.get();
    }

    public static boolean enterParsingAwait() {
        boolean previous = PARSING_AWAIT.get();
        PARSING_AWAIT.set(true);
        return previous;
    }

    public static void exitParsingAwait(boolean previous) {
        PARSING_AWAIT.set(previous);
    }
}
