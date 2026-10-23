package net.itsthesky.disky5.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("New Embed Field")
@Description("A new embed field with a name and a value, optionally inline, to add to an embed.")
@Examples({
        "add (a new field named \"Ping\" with value \"42ms\") to fields of {_embed}",
        "set {_field} to a new inline embed field named \"Status\" with value \"OK\""
})
@Since("5.0.0-a1")
public final class ExprNewEmbedField extends SimpleExpression<MessageEmbed.Field> {

    private Expression<String> exprName;
    private Expression<String> exprValue;
    private Expression<Boolean> exprInline;
    private boolean inlineFlag;

    public static void register() {
        ManualSyntax.expression(ExprNewEmbedField.class, MessageEmbed.Field.class, false, ExpressionType.COMBINED,
                "[a] new [embed] field (named|with name) %string% with [the] value %string% [[with] inline %-boolean%]",
                "[a] new inline [embed] field (named|with name) %string% with [the] value %string%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprName = (Expression<String>) expressions[0];
        exprValue = (Expression<String>) expressions[1];
        inlineFlag = matchedPattern == 1;
        exprInline = inlineFlag ? null : (Expression<Boolean>) expressions[2];
        return true;
    }

    @Override
    protected MessageEmbed.Field @NotNull [] get(@NotNull Event event) {
        final var fieldName = exprName.getSingle(event);
        final var fieldValue = exprValue.getSingle(event);
        final var inline = inlineFlag
                || (exprInline != null && Boolean.TRUE.equals(exprInline.getSingle(event)));

        if (fieldName == null || fieldValue == null)
            return new MessageEmbed.Field[0];

        try {
            return new MessageEmbed.Field[]{
                    new MessageEmbed.Field(fieldName, fieldValue, inline)
            };
        } catch (IllegalArgumentException ex) {
            DiSkyRuntimeHandler.error(ex);
            return new MessageEmbed.Field[0];
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends MessageEmbed.Field> getReturnType() {
        return MessageEmbed.Field.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "a new embed field named " + exprName.toString(event, debug)
                + " with value " + exprValue.toString(event, debug)
                + (inlineFlag ? " inline"
                    : exprInline != null ? " inline " + exprInline.toString(event, debug) : "");
    }
}
