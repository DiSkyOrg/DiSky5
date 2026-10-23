package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.commands.Command;
import org.skriptlang.skript.lang.script.Script;

public final class ContextCommandModel extends ApplicationCommandModel {

    private final Command.Type type;

    public ContextCommandModel(Script script, Command.Type type, String name) {
        super(script, name);
        this.type = type;
    }

    public Command.Type type() { return type; }
}
