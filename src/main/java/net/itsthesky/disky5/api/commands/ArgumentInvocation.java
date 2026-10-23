package net.itsthesky.disky5.api.commands;

import org.jetbrains.annotations.NotNull;

public interface ArgumentInvocation {

    Object @NotNull [] value(int index);
}
