package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.Nullable;

public abstract class ApplicationCommandHolder {

    private @Nullable Long id;

    public abstract CommandData data();

    public Command.Type type() { return data().getType(); }

    public String name() { return data().getName(); }

    public @Nullable Long id() { return id; }

    public void id(@Nullable Long id) { this.id = id; }

    public CommandData build() { return data(); }

    @Override
    public String toString() {
        return type().name().toLowerCase() + " command '" + name() + "'";
    }
}
