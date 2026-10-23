package net.itsthesky.disky5.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
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
import net.itsthesky.disky5.api.commands.ArgumentInvocation;
import net.itsthesky.disky5.api.commands.InvocationArgument;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.elements.structures.CommandParseData;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_ApplicationCommandCooldown;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_DiscordCommandUsed;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_SlashCommand;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;

@Name("Discord Command Argument")
@Description({
    "An argument of the current 'discord command', by index ('arg-1', 'the 2nd argument'), by type",
    "('user argument', 'text argument-2'), the last one, or all of them. Only inside the trigger of a",
    "discord command; a named argument ('<who: user>') is also available as '{_who}'."
})
@Examples({
    "discord command ban <member> [<reason: text = no reason>]:",
    "    trigger:",
    "        reply with \"Banning %arg-1% for %{_reason}%\""
})
@Since("5.0.0-a1")
public final class ExprCommandArgument extends SimpleExpression<Object> {

    private static final int LAST = 0, INDEXED = 1, ORDINAL = 2, ALL = 3, TYPED = 4, TYPED_SUFFIX = 5;

    public static void register() {
        ManualSyntax.expression(ExprCommandArgument.class, Object.class, true, ExpressionType.SIMPLE,
            "[the] last [discord] arg[ument][s]",
            "[the] [discord] arg[ument][s](-| )<\\d+>",
            "[the] <(\\d*1)st|(\\d*2)nd|(\\d*3)rd|(\\d*[4-90])th> [discord] arg[ument][s]",
            "[the] [discord] arg[ument][s]",
            "[the] %*classinfo%( |-)arg[ument][( |-)<\\d+>]",
            "[the] [discord] arg[ument]( |-)%*classinfo%[( |-)<\\d+>]");
    }

    private @Nullable InvocationArgument argument;
    private List<? extends InvocationArgument> all = List.of();

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(GeneratedBukkitEvt_DiscordCommandUsed.class)
                && !getParser().isCurrentEvent(GeneratedBukkitEvt_SlashCommand.class)
                && !getParser().isCurrentEvent(GeneratedBukkitEvt_ApplicationCommandCooldown.class))
            return false;
        List<? extends InvocationArgument> arguments = getParser().getData(CommandParseData.class).arguments();
        if (arguments == null) {
            Skript.error("The argument expression can only be used inside the trigger of a discord or slash command");
            return false;
        }
        if (arguments.isEmpty()) {
            Skript.error("This discord command has no argument");
            return false;
        }
        switch (matchedPattern) {
            case LAST -> argument = arguments.get(arguments.size() - 1);
            case INDEXED, ORDINAL -> {
                int index = number(parseResult.regexes.get(0));
                if (index < 1 || index > arguments.size()) {
                    Skript.error("This discord command has only " + arguments.size() + " argument(s)");
                    return false;
                }
                argument = arguments.get(index - 1);
            }
            case ALL -> all = arguments;
            default -> {
                ClassInfo<?> info = ((Literal<ClassInfo<?>>) expressions[0]).getSingle();
                int wanted = parseResult.regexes.isEmpty() ? 1 : number(parseResult.regexes.get(0));
                List<InvocationArgument> typed = new ArrayList<>();
                for (InvocationArgument a : arguments)
                    if (info.getC().isAssignableFrom(a.valueType()))
                        typed.add(a);
                if (wanted < 1 || wanted > typed.size()) {
                    Skript.error("This discord command has " + typed.size() + " argument(s) of type " + info.getCodeName());
                    return false;
                }
                argument = typed.get(wanted - 1);
            }
        }
        return true;
    }

    private static int number(MatchResult match) {
        String digits = match.group().replaceAll("\\D", "");
        return digits.isEmpty() ? 1 : Integer.parseInt(digits);
    }

    @Override
    protected Object @Nullable [] get(@NotNull Event event) {
        if (!(event instanceof DiSkyBukkitEvent bukkit) || !(bukkit.getJdaEvent() instanceof ArgumentInvocation invocation))
            return new Object[0];
        if (argument != null)
            return invocation.value(argument.index());
        List<Object> values = new ArrayList<>();
        for (InvocationArgument a : all)
            for (Object value : invocation.value(a.index()))
                values.add(value);
        return values.toArray();
    }

    @Override
    public boolean isSingle() {
        return argument != null && argument.single();
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return argument == null ? Object.class : argument.valueType();
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return argument == null ? "the discord command arguments" : "the discord command argument " + argument;
    }
}
