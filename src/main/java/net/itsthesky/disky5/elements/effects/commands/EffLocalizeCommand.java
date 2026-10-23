package net.itsthesky.disky5.elements.effects.commands;

import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandHolder;
import net.itsthesky.disky5.api.commands.application.Localizations;
import net.itsthesky.disky5.api.commands.application.SlashChoiceHolder;
import net.itsthesky.disky5.api.commands.application.SlashOptionHolder;
import net.itsthesky.disky5.api.commands.application.SubcommandGroupHolder;
import net.itsthesky.disky5.api.commands.application.SubcommandHolder;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Localize Command")
@Description({
    "Sets the name or the description shown to users of a given Discord locale, for an application command, "
        + "a slash option, a subcommand, a subcommand group or a choice (choices only have a name).",
    "Locales are Discord's codes (\"fr\", \"de\", \"en-GB\", \"pt-BR\", ...) or language names (\"french\")."
})
@Examples({
    "set {_cmd} to a new slash command named \"ban\" with description \"Ban someone\"",
    "localize the name of {_cmd} in \"fr\" to \"bannir\"",
    "localize the description of {_cmd} in \"fr\" to \"Bannir quelqu'un\""
})
@Since("5.0.0-a2")
public final class EffLocalizeCommand extends Effect {

    private Expression<?> target;
    private Expression<String> locale;
    private Expression<String> text;
    private boolean description;
    private Node node;

    public static void register() {
        ManualSyntax.effect(EffLocalizeCommand.class,
            "localize [the] (name|:description) of %applicationcommand/slashoption/subcommand/subcommandgroup/slashchoice% (in|for) [the] [locale] %string% (as|to) %string%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        target = expressions[0];
        locale = (Expression<String>) expressions[1];
        text = (Expression<String>) expressions[2];
        description = parseResult.hasTag("description");
        node = getParser().getNode();
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        Object holder = target.getSingle(event);
        String code = locale.getSingle(event);
        String value = text.getSingle(event);
        if (holder == null || code == null || value == null) return;
        try {
            DiscordLocale discordLocale = Localizations.parse(code);
            if (discordLocale == null)
                throw new IllegalArgumentException("'" + code + "' is not a Discord locale (available: " + Localizations.available() + ")");
            apply(holder, discordLocale, value);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
        }
    }

    private void apply(Object holder, DiscordLocale locale, String value) {
        if (holder instanceof ApplicationCommandHolder command) {
            if (!description) command.data().setNameLocalization(locale, value);
            else if (command.data() instanceof SlashCommandData slash) slash.setDescriptionLocalization(locale, value);
            else throw new IllegalArgumentException("a " + command.type().name().toLowerCase() + " command has no description");
        } else if (holder instanceof SlashOptionHolder option) {
            if (description) option.data().setDescriptionLocalization(locale, value);
            else option.data().setNameLocalization(locale, value);
        } else if (holder instanceof SubcommandHolder subcommand) {
            if (description) subcommand.data().setDescriptionLocalization(locale, value);
            else subcommand.data().setNameLocalization(locale, value);
        } else if (holder instanceof SubcommandGroupHolder group) {
            if (description) group.data().setDescriptionLocalization(locale, value);
            else group.data().setNameLocalization(locale, value);
        } else if (holder instanceof SlashChoiceHolder choice) {
            if (description) throw new IllegalArgumentException("a choice has no description to localize");
            choice.nameLocalizations().put(locale, value);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "localize the " + (description ? "description" : "name") + " of " + target.toString(event, debug)
            + " in " + locale.toString(event, debug) + " to " + text.toString(event, debug);
    }
}
