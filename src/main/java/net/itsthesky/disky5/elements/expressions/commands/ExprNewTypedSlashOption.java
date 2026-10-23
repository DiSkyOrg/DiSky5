package net.itsthesky.disky5.elements.expressions.commands;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.itsthesky.disky5.api.commands.application.SlashArgumentType;
import net.itsthesky.disky5.api.commands.application.SlashArgumentTypes;
import net.itsthesky.disky5.api.commands.application.SlashOptionHolder;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("New Typed Slash Option")
@Description({
    "Creates a slash option from a Skript type instead of a Discord option type: a member, a colour, a text "
        + "channel, a player, or any enum (its constants become the choices, or an autocomplete past 25). "
        + "The same types work as '<name: type>' arguments of a 'slash command' structure.",
    "Read the value back with 'slash option \"name\"' (as the Discord type) or, in a structure, as '{_name}'."
})
@Examples({
    "set {_opt} to a new typed member option named \"target\" with description \"Who\"",
    "set {_opt} to a new typed gamemode option named \"mode\" with description \"Which mode\""
})
@Since("5.0.0-a2")
public final class ExprNewTypedSlashOption extends SimpleExpression<SlashOptionHolder> {

    private SlashArgumentType<?> type;
    private ClassInfo<?> info;
    private Expression<String> name;
    private Expression<String> description;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprNewTypedSlashOption.class, SlashOptionHolder.class, false, ExpressionType.COMBINED,
            "[a] new typed %*classinfo% [slash] option (named|with [the] name) %string% with [the] desc[ription] %string%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        info = ((Literal<ClassInfo<?>>) expressions[0]).getSingle();
        type = SlashArgumentTypes.of(info);
        if (type == null) {
            Skript.error("'" + info.getCodeName() + "' cannot be a slash option");
            return false;
        }
        name = (Expression<String>) expressions[1];
        description = (Expression<String>) expressions[2];
        node = getParser().getNode();
        return true;
    }

    @Override
    protected SlashOptionHolder @Nullable [] get(@NotNull Event event) {
        String optionName = name.getSingle(event);
        String optionDescription = description.getSingle(event);
        if (optionName == null || optionDescription == null) return new SlashOptionHolder[0];
        try {
            OptionData data = new OptionData(type.optionType(), optionName, optionDescription);
            type.applyTo(data);
            return new SlashOptionHolder[]{new SlashOptionHolder(data)};
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
            return new SlashOptionHolder[0];
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends SlashOptionHolder> getReturnType() {
        return SlashOptionHolder.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "a new typed " + info.getCodeName() + " option named " + name.toString(event, debug);
    }
}
