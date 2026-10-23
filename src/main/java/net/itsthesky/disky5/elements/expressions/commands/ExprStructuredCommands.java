package net.itsthesky.disky5.elements.expressions.commands;

import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandHolder;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandRegistry;
import net.itsthesky.disky5.api.commands.application.ContextCommandModel;
import net.itsthesky.disky5.api.commands.application.SlashCommandMerger;
import net.itsthesky.disky5.api.commands.application.SlashCommandModel;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Name("Structured Discord Commands")
@Description({
    "The commands declared by 'slash command' / 'user command' / 'message command' structures, as command "
        + "objects ready to be pushed: every 'slash command mod ...' structure is merged into one '/mod' command.",
    "By name (the root word of a slash command, the full name of a context command), or all of them."
})
@Examples({
    "on guild ready:",
    "\tupdate the discord commands (all structured application commands) in event-guild",
    "",
    "set {_mod} to structured slash command \"mod\"",
    "send \"%size of subcommands of {_mod}% subcommands\" to console"
})
@Since("5.0.0-a2")
public final class ExprStructuredCommands extends SimpleExpression<ApplicationCommandHolder> {

    private @Nullable Expression<String> names;
    private @Nullable Command.Type type;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprStructuredCommands.class, ApplicationCommandHolder.class, true, ExpressionType.SIMPLE,
            "[the] structured (slash|:user|:message) command[s] %strings%",
            "[all [of]] [the] structured (slash|:user|:message|:application) commands");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (matchedPattern == 0)
            names = (Expression<String>) expressions[0];
        if (parseResult.hasTag("user")) type = Command.Type.USER;
        else if (parseResult.hasTag("message")) type = Command.Type.MESSAGE;
        else if (!parseResult.hasTag("application")) type = Command.Type.SLASH;
        node = getParser().getNode();
        return true;
    }

    @Override
    protected ApplicationCommandHolder @Nullable [] get(@NotNull Event event) {
        ApplicationCommandRegistry registry = ApplicationCommandRegistry.INSTANCE;
        List<ApplicationCommandHolder> result = new ArrayList<>();
        try {
            if (names != null) {
                for (String name : names.getArray(event)) {
                    ApplicationCommandHolder holder = one(registry, name);
                    if (holder != null) result.add(holder);
                }
                return result.toArray(new ApplicationCommandHolder[0]);
            }
            if (type == null || type == Command.Type.SLASH)
                for (String root : registry.roots())
                    result.add(SlashCommandMerger.merge(root, registry.byRoot(root)));
            if (type != Command.Type.SLASH)
                for (ContextCommandModel model : registry.contexts())
                    if (type == null || model.type() == type) {
                        ApplicationCommandHolder holder = SlashCommandMerger.toHolder(model);
                        if (holder != null) result.add(holder);
                    }
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
        }
        return result.toArray(new ApplicationCommandHolder[0]);
    }

    private @Nullable ApplicationCommandHolder one(ApplicationCommandRegistry registry, String name) {
        if (type == Command.Type.SLASH) {
            List<SlashCommandModel> models = registry.byRoot(name.toLowerCase(Locale.ROOT).trim());
            return models.isEmpty() ? null : SlashCommandMerger.merge(models.get(0).root(), models);
        }
        ContextCommandModel model = registry.context(type, name);
        return model == null ? null : SlashCommandMerger.toHolder(model);
    }

    @Override
    public boolean isSingle() {
        return names != null && names.isSingle();
    }

    @Override
    public @NotNull Class<? extends ApplicationCommandHolder> getReturnType() {
        return ApplicationCommandHolder.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String kind = type == null ? "application" : type.name().toLowerCase();
        return names == null ? "all structured " + kind + " commands" : "structured " + kind + " commands " + names.toString(event, debug);
    }
}
