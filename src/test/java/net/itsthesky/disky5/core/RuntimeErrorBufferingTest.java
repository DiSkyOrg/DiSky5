package net.itsthesky.disky5.core;

import net.itsthesky.disky5.api.async.DiSkyAsync;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RuntimeErrorBufferingTest {

    private static final HandlerList HANDLERS = new HandlerList();

    private static Event event() {
        return new Event() {
            @Override
            public @NotNull HandlerList getHandlers() {
                return HANDLERS;
            }
        };
    }

    @Test
    void anErrorRaisedOnAnotherThreadReachesTheTryBlock() throws InterruptedException {
        Event event = event();
        DiSkyRuntimeHandler.start(event);

        Thread worker = new Thread(() ->
            DiSkyRuntimeHandler.error(new IllegalStateException("boom"), null, event));
        worker.start();
        worker.join();

        List<DiscordError> errors = DiSkyRuntimeHandler.end(event);
        assertThat(errors).hasSize(1);
        assertThat(errors.get(0).getMessage()).isEqualTo("boom");
    }

    @Test
    void twoTriggersRunningAtOnceDoNotSeeEachOthersErrors() {
        Event mine = event();
        Event other = event();
        DiSkyRuntimeHandler.start(mine);
        DiSkyRuntimeHandler.start(other);

        DiSkyRuntimeHandler.error(new IllegalStateException("mine"), null, mine);
        DiSkyRuntimeHandler.error(new IllegalStateException("theirs"), null, other);

        List<DiscordError> errors = DiSkyRuntimeHandler.end(mine);
        assertThat(errors).hasSize(1);
        assertThat(errors.get(0).getMessage()).isEqualTo("mine");
        assertThat(DiSkyRuntimeHandler.end(other)).hasSize(1);
    }

    @Test
    void anEventlessErrorFallsBackToTheThreadsCurrentEvent() throws InterruptedException {
        Event event = event();
        DiSkyRuntimeHandler.start(event);

        Thread worker = new Thread(() -> {
            Event previous = DiSkyAsync.enterEvent(event);
            try {
                DiSkyRuntimeHandler.error(new IllegalStateException("from a property handler"));
            } finally {
                DiSkyAsync.exitEvent(previous);
            }
        });
        worker.start();
        worker.join();

        List<DiscordError> errors = DiSkyRuntimeHandler.end(event);
        assertThat(errors).hasSize(1);
        assertThat(errors.get(0).getMessage()).isEqualTo("from a property handler");
    }

    @Test
    void endingWithoutATryBlockCollectsNothing() {
        assertThat(DiSkyRuntimeHandler.end(event())).isEmpty();
        assertThat(DiSkyRuntimeHandler.end(null)).isEmpty();
    }

    @Test
    void aBufferIsNotLeftBehindAfterTheBlockEnds() {
        Event event = event();
        DiSkyRuntimeHandler.start(event);
        DiSkyRuntimeHandler.error(new IllegalStateException("boom"), null, event);
        assertThat(DiSkyRuntimeHandler.end(event)).hasSize(1);

        assertThat(DiSkyRuntimeHandler.end(event)).isEmpty();
    }
}
