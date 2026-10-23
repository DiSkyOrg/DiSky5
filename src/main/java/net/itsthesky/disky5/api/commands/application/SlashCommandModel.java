package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.DiscordLocale;
import org.skriptlang.skript.lang.script.Script;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SlashCommandModel extends ApplicationCommandModel {

    private final String[] path;
    private String description = "No description provided";
    private final Map<DiscordLocale, String> descriptionLocalizations = new LinkedHashMap<>();
    private final List<SlashArgument> arguments = new ArrayList<>();

    public SlashCommandModel(Script script, String[] path) {
        super(script, String.join(" ", path));
        this.path = path.clone();
    }

    public String[] path() { return path.clone(); }
    public int depth() { return path.length; }
    public String root() { return path[0]; }
    public String fullPath() { return name(); }
    public String leaf() { return path[path.length - 1]; }
    public String description() { return description; }
    public Map<DiscordLocale, String> descriptionLocalizations() { return descriptionLocalizations; }
    public List<SlashArgument> arguments() { return arguments; }

    public void description(String description) { this.description = description; }

    public boolean descriptorOnly() { return trigger() == null; }
}
