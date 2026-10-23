package net.itsthesky.disky5.api.commands;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.Expression;
import org.jetbrains.annotations.Nullable;

public final class CommandArgument implements InvocationArgument {

    private final @Nullable String name;
    private final ArgumentType<?> type;
    private final ClassInfo<?> classInfo;
    private final boolean single;
    private final boolean optional;
    private final @Nullable Expression<?> defaultValue;
    private final int index;

    public CommandArgument(@Nullable String name, ArgumentType<?> type, ClassInfo<?> classInfo, boolean single,
                           boolean optional, @Nullable Expression<?> defaultValue, int index) {
        this.name = name;
        this.type = type;
        this.classInfo = classInfo;
        this.single = single;
        this.optional = optional;
        this.defaultValue = defaultValue;
        this.index = index;
    }

    @Override
    public @Nullable String name() { return name; }
    public ArgumentType<?> type() { return type; }
    public ClassInfo<?> classInfo() { return classInfo; }
    @Override
    public boolean single() { return single; }
    public boolean optional() { return optional; }
    public @Nullable Expression<?> defaultValue() { return defaultValue; }
    @Override
    public int index() { return index; }

    @Override
    public Class<?> valueType() { return type.type(); }

    public boolean greedyText() {
        return single && type.type() == String.class;
    }

    @Override
    public String toString() {
        String inner = (name == null ? "" : name + ": ") + type.name() + (single ? "" : "s");
        return optional ? "[<" + inner + ">]" : "<" + inner + ">";
    }
}
