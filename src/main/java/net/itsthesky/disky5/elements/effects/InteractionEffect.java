package net.itsthesky.disky5.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.Interaction;
import net.itsthesky.disky5.api.async.AsyncEffect;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class InteractionEffect extends AsyncEffect {

    protected Node node;

    protected boolean initInteraction(String syntax) {
        Class<? extends Event>[] events = getParser().getCurrentEvents();
        if (events == null || events.length == 0) {
            Skript.error("'" + syntax + "' can only be used inside a DiSky interaction event.");
            return false;
        }
        for (Class<? extends Event> event : events) {
            if (!DiSkyBukkitEvent.class.isAssignableFrom(event)) {
                Skript.error("'" + syntax + "' can only be used inside a DiSky interaction event "
                    + "(a button click, a dropdown click, a modal, a slash command...).");
                return false;
            }
        }
        node = getParser().getNode();
        getParser().setHasDelayBefore(Kleenean.TRUE);
        return true;
    }

    protected <T extends Interaction> @Nullable T interaction(Event event, Class<T> kind, String what) {
        Object jda = event instanceof DiSkyBukkitEvent bukkit ? bukkit.getJdaEvent() : null;
        if (kind.isInstance(jda))
            return kind.cast(jda);
        DiSkyRuntimeHandler.error(new IllegalStateException("this event is not an interaction that can " + what), node, event);
        return null;
    }

    @Override
    protected final void execute(Event event) {
        try {
            answer(event);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
        }
    }

    protected abstract void answer(Event event) throws Exception;
}
