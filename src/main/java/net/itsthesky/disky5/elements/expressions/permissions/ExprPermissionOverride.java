package net.itsthesky.disky5.elements.expressions.permissions;

import ch.njol.skript.classes.Changer.ChangeMode;
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
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.channel.attribute.IPermissionContainer;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Channel Permission Override")
@Description({
    "The permissions a channel explicitly allows or denies to one member or role (its permission override). "
        + "Empty when the channel has no override for it; anything neither allowed nor denied is inherited "
        + "from the guild-wide permissions.",
    "Add, remove and set change the override on Discord, creating it if needed. Adding to the allowed permissions "
        + "also takes them out of the denied ones (a permission cannot be both), and the other way around. "
        + "Removing only clears them from the list you name: removing from the allowed permissions does not deny them, "
        + "it makes them inherited again. Reset (or delete) removes the whole override, allowed and denied together.",
    "In a thread, the override is the parent channel's: changing it changes the parent channel.",
    "The bot needs the 'manage permissions' permission in the channel (or 'manage roles' guild-wide), "
        + "and can only allow or deny permissions it has itself. Under 'await' the change waits for Discord."
})
@Examples({
    "await add send messages to allowed discord permissions of event-member in event-channel",
    "await add view channel to denied discord permissions of {_role} in {_channel}",
    "remove send messages from denied discord permissions of event-member in event-channel",
    "set {_denied::*} to denied discord permissions of {_role} in event-channel",
    "await reset allowed discord permissions of event-member in event-channel"
})
@Since("5.0.0-b1")
public final class ExprPermissionOverride extends SimpleExpression<Permission> {

    private Expression<?> holder;
    private Expression<GuildChannel> channel;
    private boolean allowed;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprPermissionOverride.class, Permission.class, true, ExpressionType.COMBINED,
            new net.itsthesky.disky5.api.syntax.ChangeMode[]{
                net.itsthesky.disky5.api.syntax.ChangeMode.SET, net.itsthesky.disky5.api.syntax.ChangeMode.ADD,
                net.itsthesky.disky5.api.syntax.ChangeMode.REMOVE, net.itsthesky.disky5.api.syntax.ChangeMode.DELETE,
                net.itsthesky.disky5.api.syntax.ChangeMode.RESET},
            "[the] (:allowed|:denied) discord permissions of %member/role% in [the] [channel] %guildchannel%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        holder = expressions[0];
        channel = (Expression<GuildChannel>) expressions[1];
        allowed = parseResult.hasTag("allowed");
        node = getParser().getNode();
        return true;
    }

    @Override
    protected Permission @Nullable [] get(@NotNull Event event) {
        Object rawHolder = holder.getSingle(event);
        GuildChannel target = channel.getSingle(event);
        if (rawHolder == null || target == null) return new Permission[0];
        try {
            PermissionOverride override = container(target).getPermissionOverride(permissionHolder(rawHolder));
            if (override == null) return new Permission[0];
            return (allowed ? override.getAllowed() : override.getDenied()).toArray(new Permission[0]);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
            return new Permission[0];
        }
    }

    @Override
    public Class<?> @Nullable [] acceptChange(@NotNull ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE -> new Class[]{Permission[].class};
            case DELETE, RESET -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(@NotNull Event event, Object @Nullable [] delta, @NotNull ChangeMode mode) {
        Object rawHolder = holder.getSingle(event);
        GuildChannel target = channel.getSingle(event);
        if (rawHolder == null || target == null) return;
        try {
            IPermissionHolder permissionHolder = permissionHolder(rawHolder);
            IPermissionContainer container = container(target);
            PermissionOverride existing = container.getPermissionOverride(permissionHolder);
            RestAction<?> action;
            if (mode == ChangeMode.DELETE || mode == ChangeMode.RESET) {
                if (existing == null) return;
                action = existing.delete();
            } else {
                if (existing == null && mode == ChangeMode.REMOVE) return;
                long bits = delta == null ? 0
                    : Permission.getRaw(Arrays.copyOf(delta, delta.length, Permission[].class));
                PermissionOverrideAction upsert = container.upsertPermissionOverride(permissionHolder);
                long current = allowed ? upsert.getAllowed() : upsert.getDenied();
                long next = switch (mode) {
                    case ADD -> current | bits;
                    case REMOVE -> current & ~bits;
                    default -> bits;
                };
                action = allowed ? upsert.setAllowed(next) : upsert.setDenied(next);
            }
            if (Bukkit.isPrimaryThread())
                action.queue(null, t -> DiSkyRuntimeHandler.error(t, node, event));
            else
                action.complete();
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
        }
    }

    private static IPermissionHolder permissionHolder(Object raw) {
        if (raw instanceof IPermissionHolder permissionHolder) return permissionHolder;
        throw new IllegalArgumentException("a permission override needs a member or a role, not " + raw);
    }

    private static IPermissionContainer container(GuildChannel channel) {
        IPermissionContainer container = channel.getPermissionContainer();
        if (container == null)
            throw new IllegalArgumentException("the channel '" + channel.getName() + "' has no permission overrides");
        return container;
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
        return "the " + (allowed ? "allowed" : "denied") + " discord permissions of " + holder.toString(event, debug)
            + " in " + channel.toString(event, debug);
    }
}
