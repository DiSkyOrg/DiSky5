package net.itsthesky.disky5.api.commands;

import org.jetbrains.annotations.Nullable;

public interface ArgumentType<T> {

    String name();

    Class<T> type();

    @Nullable T resolve(String raw, CommandInvocation invocation);
}
