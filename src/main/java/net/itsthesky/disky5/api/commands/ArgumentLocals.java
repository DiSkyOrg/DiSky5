package net.itsthesky.disky5.api.commands;

import ch.njol.skript.variables.Variables;
import org.bukkit.event.Event;

import java.util.List;

public final class ArgumentLocals {

    private ArgumentLocals() {}

    public static void set(List<? extends InvocationArgument> arguments, ArgumentInvocation invocation, Event event) {
        for (InvocationArgument argument : arguments) {
            String name = argument.name();
            if (name == null) continue;
            Object[] value = invocation.value(argument.index());
            if (argument.single())
                Variables.setVariable(name, value.length == 0 ? null : value[0], event, true);
            else
                for (int k = 0; k < value.length; k++)
                    Variables.setVariable(name + "::" + (k + 1), value[k], event, true);
        }
    }
}
