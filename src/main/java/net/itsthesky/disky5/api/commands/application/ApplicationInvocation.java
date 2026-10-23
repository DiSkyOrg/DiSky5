package net.itsthesky.disky5.api.commands.application;

import net.itsthesky.disky5.api.events.CancellationAware;
import net.itsthesky.disky5.core.Bot;
import org.jetbrains.annotations.Nullable;

public interface ApplicationInvocation extends CancellationAware {

    ApplicationCommandModel model();

    @Nullable Bot bot();

    long remainingCooldown();

    default boolean onCooldown() { return remainingCooldown() > 0; }
}
