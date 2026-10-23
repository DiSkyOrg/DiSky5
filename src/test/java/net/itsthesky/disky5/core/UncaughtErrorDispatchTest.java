package net.itsthesky.disky5.core;

import net.dv8tion.jda.api.events.GenericEvent;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.api.events.DiSkyEventBus;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UncaughtErrorDispatchTest {

    private static UncaughtError uncaught() {
        return new UncaughtError(DiscordError.from(new IllegalStateException("boom"), null), null);
    }

    @Test
    void anErrorOutsideTheHandlerFiresTheEvent() {
        assertThat(DiSkyRuntimeHandler.raisedByErrorHandler(null)).isFalse();
        DiSkyBukkitEvent otherEvent = new DiSkyBukkitEvent(mock(GenericEvent.class), null) {};
        assertThat(DiSkyRuntimeHandler.raisedByErrorHandler(otherEvent)).isFalse();
    }

    @Test
    void anErrorFromTheHandlersTriggerDoesNotFireTheEventAgain() {
        DiSkyBukkitEvent handlerEvent = new DiSkyBukkitEvent(uncaught(), null) {};
        assertThat(DiSkyRuntimeHandler.raisedByErrorHandler(handlerEvent)).isTrue();
    }

    @Test
    void anErrorRaisedWhileTheEventIsDispatchedIsNotDispatchedAgain() {
        AtomicBoolean nested = new AtomicBoolean();
        DiSkyEventBus.Subscription subscription = DiSkyEventBus.INSTANCE.subscribe(UncaughtError.class,
            (event, bot) -> nested.set(DiSkyRuntimeHandler.raisedByErrorHandler(null)));
        try {
            DiSkyRuntimeHandler.dispatch(uncaught());
        } finally {
            DiSkyEventBus.INSTANCE.unsubscribe(subscription);
        }
        assertThat(nested).isTrue();
        assertThat(DiSkyRuntimeHandler.raisedByErrorHandler(null)).isFalse();
    }

    @Test
    void cancellingTheEventSilencesTheError() {
        DiSkyEventBus.Subscription subscription = DiSkyEventBus.INSTANCE.subscribe(UncaughtError.class,
            (event, bot) -> ((UncaughtError) event).setCancelled(true));
        try {
            assertThat(DiSkyRuntimeHandler.dispatch(uncaught())).isTrue();
        } finally {
            DiSkyEventBus.INSTANCE.unsubscribe(subscription);
        }
        assertThat(DiSkyRuntimeHandler.dispatch(uncaught())).isFalse();
    }
}
