package net.itsthesky.disky5.api.events;

public interface CancellationAware {

    boolean isCancelled();

    void setCancelled(boolean cancelled);
}
