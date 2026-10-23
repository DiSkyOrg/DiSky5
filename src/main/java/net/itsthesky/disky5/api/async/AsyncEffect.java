package net.itsthesky.disky5.api.async;

import ch.njol.skript.Skript;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.timings.SkriptTimings;
import ch.njol.skript.variables.Variables;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class AsyncEffect extends Effect {

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        debug(event, true);

        Delay.addDelayedEvent(event);
        Object localVars = Variables.removeLocals(event);

        if (!Skript.getInstance().isEnabled()) // https://github.com/SkriptLang/Skript/issues/3702
            return null;

        Bukkit.getScheduler().runTaskAsynchronously(Skript.getInstance(), () -> {
            if (localVars != null)
                Variables.setLocalVariables(event, localVars);

            Event previous = DiSkyAsync.enterEvent(event);
            try {
                execute(event);
            } catch (Throwable t) {
                Skript.exception(t, "Uncaught exception in DiSky async effect: " + toString(event, true));
            } finally {
                DiSkyAsync.exitEvent(previous);
            }

            final TriggerItem next = getNext();
            if (next != null) {
                Bukkit.getScheduler().runTask(Skript.getInstance(), () -> {
                    Object timing = null;
                    if (SkriptTimings.enabled()) {
                        Trigger trigger = getTrigger();
                        if (trigger != null)
                            timing = SkriptTimings.start(trigger.getDebugLabel());
                    }
                    TriggerItem.walk(next, event);
                    Variables.removeLocals(event);
                    SkriptTimings.stop(timing);
                });
            } else {
                Bukkit.getScheduler().runTask(Skript.getInstance(), () -> Variables.removeLocals(event));
            }
        });

        return null;
    }
}
