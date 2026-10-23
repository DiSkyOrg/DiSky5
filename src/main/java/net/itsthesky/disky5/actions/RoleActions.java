package net.itsthesky.disky5.actions;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.order.RoleOrderAction;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class RoleActions {

    private RoleActions() {}

    @Effect("move [the] [discord] role %role% above [the] [discord] [role] %role%")
    @Documentation(
        name = "Move Role Above",
        description = "Moves a role directly above another one of the same guild. Indexes update automatically.",
        examples = {"move role {_role} above role with id \"000000000000000000\""},
        since = "5.0.0-a1")
    public static RestAction<Void> moveRoleAbove(Role role, Role target) {
        return nextTo(role, target, true);
    }

    @Effect("move [the] [discord] role %role% under [the] [discord] [role] %role%")
    @Documentation(
        name = "Move Role Under",
        description = "Moves a role directly below another one of the same guild. Indexes update automatically.",
        examples = {"move role {_role} under role with id \"000000000000000000\""},
        since = "5.0.0-a1")
    public static RestAction<Void> moveRoleUnder(Role role, Role target) {
        return nextTo(role, target, false);
    }

    @Effect("move [the] [discord] role %role% up [%-number% time[s]]")
    @Documentation(
        name = "Move Role Up",
        description = "Moves a role up by a number of positions (one by default).",
        examples = {"move role {_role} up 3 times"},
        since = "5.0.0-a1")
    public static RestAction<Void> moveRoleUp(Role role, @Nullable Number times) {
        return shift(role, times, true);
    }

    @Effect("move [the] [discord] role %role% down [%-number% time[s]]")
    @Documentation(
        name = "Move Role Down",
        description = "Moves a role down by a number of positions (one by default).",
        examples = {"move role {_role} down 3 times"},
        since = "5.0.0-a1")
    public static RestAction<Void> moveRoleDown(Role role, @Nullable Number times) {
        return shift(role, times, false);
    }

    // Uses moveTo on JDA's own order list, not moveUp/moveDown: that list is ascending and excludes
    // @everyone, so JDA's "up" is a position decrease. Target index is clamped to the roles the bot
    // can actually reorder, so hitting the top or bottom is a no-op instead of a throw.
    private static RestAction<Void> shift(Role role, @Nullable Number times, boolean up) {
        Guild guild = role.getGuild();
        Member self = guild.getSelfMember();
        RoleOrderAction action = guild.modifyRolePositions().selectPosition(role);
        List<Role> order = action.getCurrentOrder();

        int highest = -1;
        for (int i = 0; i < order.size(); i++)
            if (self.canInteract(order.get(i)))
                highest = i;
        if (highest < 0)
            throw new IllegalArgumentException("the bot cannot reorder any role of '"
                + guild.getName() + "'");

        int selected = action.getSelectedPosition();
        int target = Math.max(0, Math.min(highest, selected + (up ? steps(times) : -steps(times))));
        return target == selected ? action : action.moveTo(target);
    }

    // Uses moveTo, not moveAbove/moveBelow: those append a moveUp/moveDown of one, which throws
    // whenever the target sits at the end of the order list.
    private static RestAction<Void> nextTo(Role role, Role target, boolean above) {
        RoleOrderAction action = order(role, target).selectPosition(role);
        List<Role> current = action.getCurrentOrder();
        int targetIndex = current.indexOf(target);
        if (targetIndex < 0)
            throw new IllegalArgumentException("the role '" + target.getName()
                + "' cannot be used as a reference: the bot cannot reorder it");

        // The list is ascending, and moveTo removes before it inserts, so the destination depends on
        // which side the role starts from.
        int selected = action.getSelectedPosition();
        int destination = above
            ? (selected < targetIndex ? targetIndex : targetIndex + 1)
            : (selected > targetIndex ? targetIndex : targetIndex - 1);
        destination = Math.max(0, Math.min(current.size() - 1, destination));
        return destination == selected ? action : action.moveTo(destination);
    }

    private static RoleOrderAction order(Role role, Role target) {
        if (role.getGuild().getIdLong() != target.getGuild().getIdLong())
            throw new IllegalArgumentException("both roles must belong to the same guild (got '"
                + role.getGuild().getName() + "' and '" + target.getGuild().getName() + "')");
        return role.getGuild().modifyRolePositions();
    }

    private static int steps(@Nullable Number times) {
        int amount = times == null ? 1 : times.intValue();
        if (amount < 1)
            throw new IllegalArgumentException("a role can only be moved by 1 position or more, got " + amount);
        return amount;
    }
}
