package net.itsthesky.disky5.api.commands.application;

import ch.njol.skript.Skript;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ApplicationCommandRegistry {

    public static final ApplicationCommandRegistry INSTANCE = new ApplicationCommandRegistry();

    private final CopyOnWriteArrayList<SlashCommandModel> slash = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<ContextCommandModel> context = new CopyOnWriteArrayList<>();

    private ApplicationCommandRegistry() {}

    public void register(SlashCommandModel model) {
        for (SlashCommandModel other : slash) {
            if (other.script() == model.script()) continue;
            if (other.fullPath().equals(model.fullPath()) && !other.descriptorOnly() && !model.descriptorOnly())
                Skript.warning("The slash command '" + model.fullPath() + "' is already declared in "
                    + other.script().getConfig().getFileName() + "; the last loaded one will run.");
            else if (other.root().equals(model.root()) && (other.depth() == 1) != (model.depth() == 1)
                    && !other.descriptorOnly() && !model.descriptorOnly())
                Skript.warning("'" + model.root() + "' is declared both as a slash command and as a root of subcommands "
                    + "(in " + other.script().getConfig().getFileName() + "); Discord cannot register both.");
        }
        slash.add(model);
    }

    public void register(ContextCommandModel model) {
        for (ContextCommandModel other : context)
            if (other.script() != model.script() && other.type() == model.type() && other.name().equalsIgnoreCase(model.name()))
                Skript.warning("The " + model.type().name().toLowerCase() + " command '" + model.name() + "' is already declared in "
                    + other.script().getConfig().getFileName() + "; the last loaded one will run.");
        context.add(model);
    }

    public void unregister(ApplicationCommandModel model) {
        slash.remove(model);
        context.remove(model);
    }

    public @Nullable SlashCommandModel slash(String fullPath) {
        SlashCommandModel found = null;
        for (SlashCommandModel model : slash)
            if (model.fullPath().equals(fullPath) && !model.descriptorOnly())
                found = model;
        return found;
    }

    public List<SlashCommandModel> byRoot(String root) {
        List<SlashCommandModel> result = new ArrayList<>();
        for (SlashCommandModel model : slash)
            if (model.root().equals(root))
                result.add(model);
        return result;
    }

    public Set<String> roots() {
        Set<String> roots = new LinkedHashSet<>();
        for (SlashCommandModel model : slash)
            roots.add(model.root());
        return roots;
    }

    public @Nullable ContextCommandModel context(Command.Type type, String name) {
        ContextCommandModel found = null;
        for (ContextCommandModel model : context)
            if (model.type() == type && model.name().equalsIgnoreCase(name))
                found = model;
        return found;
    }

    public List<ContextCommandModel> contexts() {
        return new ArrayList<>(context);
    }
}
