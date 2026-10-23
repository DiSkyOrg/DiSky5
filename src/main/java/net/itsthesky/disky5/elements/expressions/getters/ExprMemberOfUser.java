package net.itsthesky.disky5.elements.expressions.getters;

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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.itsthesky.disky5.api.async.DiSkyAsync;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Member Of User")
@Description({
    "The member a user is in a guild: the same person, with the guild-side data (nickname, roles, join date...). "
        + "Returns nothing when the user is not in that guild.",
    "It reads the member cache, which depends on the bot's member cache policy. With 'await', a member missing "
        + "from the cache is fetched from Discord, and a user who is not in the guild is a readable error."
})
@Examples({
    "set {_member} to member of event-user in event-guild",
    "await set {_member} to the member of {_user} in guild {_guild}"
})
@Since("5.0.0-b1")
public final class ExprMemberOfUser extends SimpleExpression<Member> {

    private Expression<User> user;
    private Expression<Guild> guild;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprMemberOfUser.class, Member.class, false, ExpressionType.COMBINED,
            "[the] member of %user% in [the] [guild] %guild%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        user = (Expression<User>) expressions[0];
        guild = (Expression<Guild>) expressions[1];
        node = getParser().getNode();
        return true;
    }

    @Override
    protected Member @Nullable [] get(@NotNull Event event) {
        User target = user.getSingle(event);
        Guild scope = guild.getSingle(event);
        if (target == null || scope == null) return new Member[0];
        try {
            Member member = scope.getMember(target);
            if (member == null && DiSkyAsync.isAwaited(event))
                member = scope.retrieveMember(target).complete();
            return member == null ? new Member[0] : new Member[] {member};
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
            return new Member[0];
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Member> getReturnType() {
        return Member.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the member of " + user.toString(event, debug) + " in " + guild.toString(event, debug);
    }
}
