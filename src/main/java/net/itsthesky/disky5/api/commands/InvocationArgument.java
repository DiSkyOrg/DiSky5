package net.itsthesky.disky5.api.commands;

import org.jetbrains.annotations.Nullable;

public interface InvocationArgument {

    @Nullable String name();

    int index();

    Class<?> valueType();

    boolean single();
}
