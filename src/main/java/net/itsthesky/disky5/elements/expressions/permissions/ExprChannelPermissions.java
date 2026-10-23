package net.itsthesky.disky5.elements.expressions.permissions;

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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Discord Permissions in Channel")
@Description({
    "The effective permissions of a member or a role in one channel: its guild-wide permissions, "
        + "with the channel's overrides applied (@everyone first, then the roles, then the member's own override). "
        + "An administrator, and the guild owner, have every permission everywhere.",
    "In a thread, the overrides of the parent channel apply.",
    "Read-only; change the allowed or denied discord permissions of the channel instead."
})
@Examples({
    "set {_perms::*} to discord permissions of event-member in event-channel",
    "if discord permissions of event-member in event-channel contain send messages:",
    "\treply with \"You can talk here.\""
})
@Since("5.0.0-b1")
public final class ExprChannelPermissions extends SimpleExpression<Permission> {

    private Expression<?> holder;
    private Expression<GuildChannel> channel;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprChannelPermissions.class, Permission.class, true, ExpressionType.COMBINED,
            "[the] discord permissions of %member/role% in [the] [channel] %guildchannel%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        holder = expressions[0];
        channel = (Expression<GuildChannel>) expressions[1];
        node = getParser().getNode();
        return true;
    }

    @Override
    protected Permission @Nullable [] get(@NotNull Event event) {
        Object rawHolder = holder.getSingle(event);
        GuildChannel target = channel.getSingle(event);
        if (!(rawHolder instanceof IPermissionHolder permissionHolder) || target == null) return new Permission[0];
        try {
            return permissionHolder.getPermissions(target).toArray(new Permission[0]);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
            return new Permission[0];
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends Permission> getReturnType() {
        return Permission.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the discord permissions of " + holder.toString(event, debug) + " in " + channel.toString(event, debug);
    }
}
