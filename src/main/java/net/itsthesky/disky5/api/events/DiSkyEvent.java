package net.itsthesky.disky5.api.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import net.dv8tion.jda.api.events.GenericEvent;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.core.Bot;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class DiSkyEvent<T extends GenericEvent> extends SelfRegisteringSkriptEvent {

    private @Nullable String botFilter;
    private @Nullable DiSkyEventBus.Subscription subscription;

    protected abstract Class<T> jdaType();

    protected abstract DiSkyBukkitEvent createBukkitEvent(T jdaEvent, @Nullable Bot bot);

    protected boolean accept(T jdaEvent, @Nullable Bot bot) {
        return true;
    }

    protected boolean hasRestValues() {
        return false;
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        for (Literal<?> arg : args) {
            if (arg != null && arg.getSingle() instanceof String name) {
                botFilter = name;
                break;
            }
        }
        return true;
    }

    @Override
    public void register(Trigger trigger) {
        subscription = DiSkyEventBus.INSTANCE.subscribe(jdaType(), (rawEvent, bot) -> {
            T jdaEvent = jdaType().cast(rawEvent);
            if (botFilter != null && (bot == null || !bot.getName().equalsIgnoreCase(botFilter)))
                return;
            if (!accept(jdaEvent, bot))
                return;
            DiSky5 plugin = DiSky5.getInstance();
            if (plugin == null || !plugin.isEnabled())
                return;
            // JDA threads are never the primary one; a dispatch that is (a command invocation) is
            // walked inline so the caller can observe a cancellation.
            Runnable build = () -> {
                DiSkyBukkitEvent event = createBukkitEvent(jdaEvent, bot);
                if (Bukkit.isPrimaryThread())
                    TriggerItem.walk(trigger, event);
                else
                    Bukkit.getScheduler().runTask(plugin, () -> TriggerItem.walk(trigger, event));
            };
            if (hasRestValues())
                Bukkit.getScheduler().runTaskAsynchronously(plugin, build);
            else
                build.run();
        });
    }

    @Override
    public void unregister(Trigger trigger) {
        DiSkyEventBus.INSTANCE.unsubscribe(subscription);
        subscription = null;
    }

    @Override
    public void unregisterAll() {
        DiSkyEventBus.INSTANCE.unsubscribe(subscription);
        subscription = null;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return getClass().getSimpleName();
    }
}
