package net.itsthesky.disky5.api.commands;

import ch.njol.skript.Skript;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class CommandRegistry {

    public static final CommandRegistry INSTANCE = new CommandRegistry();

    private final CopyOnWriteArrayList<CommandDefinition> commands = new CopyOnWriteArrayList<>();

    private CommandRegistry() {}

    public void register(CommandDefinition definition) {
        for (CommandDefinition other : commands)
            if (clashes(other, definition))
                Skript.warning("The discord command '" + definition.name() + "' is already declared in "
                    + other.script().getConfig().getFileName() + "; both will run on the same message.");
        commands.add(definition);
    }

    public void unregister(CommandDefinition definition) {
        commands.remove(definition);
    }

    public List<CommandDefinition> all() {
        return commands;
    }

    private static boolean clashes(CommandDefinition a, CommandDefinition b) {
        boolean sameName = a.names().stream().anyMatch(n -> b.names().stream().anyMatch(n::equalsIgnoreCase));
        if (!sameName) return false;
        List<String> pa = literalPrefixes(a), pb = literalPrefixes(b);
        if (pa == null || pb == null) return true;
        return pa.stream().anyMatch(p -> pb.stream().anyMatch(p::equalsIgnoreCase));
    }

    private static List<String> literalPrefixes(CommandDefinition definition) {
        try {
            return definition.prefixes(null);
        } catch (RuntimeException ex) {
            return null;
        }
    }
}
