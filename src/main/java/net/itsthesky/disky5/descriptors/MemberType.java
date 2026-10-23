package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Date;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.RoleColors;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.annotations.ConditionType;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

@SkriptType(name = "member", jda = Member.class)
public final class MemberType {

    @Property(value = "nickname", shared = true)
    @Documentation(
            name = "Member Nickname",
            description = "The nickname of the member, or null if they don't have one.",
            examples = "set nickname of event-member to \"Disky\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String nickname(Member m) {
        return m.getNickname();
    }

    @Property.Setter(value = "nickname", shared = true)
    public static AuditableRestAction<Void> setNickname(Member m, @Nullable String value) {
        return m.modifyNickname(value);
    }

    @Property("effective name")
    public static String effectiveName(Member m) {
        return m.getEffectiveName();
    }

    @Property(value = "name", shared = true)
    public static String discordName(Member m) {
        return m.getUser().getName();
    }

    @Property("roles")
    @Documentation(
            name = "Member Roles",
            description = "The roles of the member. Add or remove roles to grant/revoke them.",
            examples = {
                "set {_roles::*} to roles of event-member",
                "add {_role} to roles of event-member",
                "await remove {_role} from roles of event-member"
            },
            since = "5.0.0-a1"
    )
    public static List<Role> roles(Member m) {
        return m.getRoles();
    }

    @Property.Adder("roles")
    public static AuditableRestAction<Void> addRole(Member m, Role role) {
        return m.getGuild().addRoleToMember(m, role);
    }

    @Property.Remover("roles")
    public static AuditableRestAction<Void> removeRole(Member m, Role role) {
        return m.getGuild().removeRoleFromMember(m, role);
    }

    @Property("join date")
    @Documentation(
            name = "Member Join Date",
            description = "The date at which the member joined the guild.",
            examples = "reply with join date of event-member",
            since = "5.0.0-a1"
    )
    public static Date joinDate(Member m) {
        return Conversions.date(m.getTimeJoined());
    }

    @Property("boost date")
    @Documentation(
            name = "Member Boost Date",
            description = "The date at which the member started boosting the guild, or null if the member is not boosting.",
            examples = "reply with boost date of event-member",
            since = "5.0.0-a1"
    )
    public static @Nullable Date boostDate(Member m) {
        return Conversions.date(m.getTimeBoosted());
    }

    // Member#getFlags rebuilds an EnumSet from the raw bits on every call, so an in-memory
    // add/remove on it never reaches Discord; the mutators below are REST calls instead.
    @Property("flags")
    @Documentation(
            name = "Member Flags",
            description = "The flags of the member (e.g. did rejoin, completed onboarding, bypasses verification...). "
                    + "Add or remove modifiable flags to toggle them.",
            examples = {
                "set {_flags::*} to flags of event-member",
                "add did rejoin to flags of event-member"
            },
            since = "5.0.0-a1"
    )
    public static List<Member.MemberFlag> flags(Member m) {
        return List.copyOf(m.getFlags());
    }

    @Property.Adder("flags")
    public static AuditableRestAction<Void> addFlag(Member m, Member.MemberFlag flag) {
        return modifyFlag(m, flag, true);
    }

    @Property.Remover("flags")
    public static AuditableRestAction<Void> removeFlag(Member m, Member.MemberFlag flag) {
        return modifyFlag(m, flag, false);
    }

    private static AuditableRestAction<Void> modifyFlag(Member m, Member.MemberFlag flag, boolean add) {
        if (!flag.isModifiable())
            throw new IllegalArgumentException("The member flag '"
                + flag.name().toLowerCase(Locale.ROOT).replace('_', ' ')
                + "' cannot be changed; only 'bypasses verification' is modifiable.");
        EnumSet<Member.MemberFlag> flags = EnumSet.copyOf(m.getFlags());
        if (add)
            flags.add(flag);
        else
            flags.remove(flag);
        return m.modifyFlags(flags);
    }

    @Property("discord colors")
    @Documentation(
            name = "Member Colors",
            description = {
                "The role colors of a member's name in the guild.",
                "Determined by the colors of the highest role assigned to the member that does not have the default colors."
            },
            examples = "reply with tertiary role color of (colors of event-member)",
            since = "5.0.0-a1"
    )
    public static @Nullable RoleColors colors(Member m) {
        return m.getColors();
    }

    @Property("activities")
    @Documentation(
            name = "Member Activities",
            description = {
                "All activities of a member (playing, streaming, listening, watching, custom status, competing).",
                "Returns an empty list if the member has no activity.",
                "Requires CacheFlag.ACTIVITY to be enabled on the bot, along with the GUILD_PRESENCES intent."
            },
            examples = {
                "set {_activities::*} to activities of event-member",
                "loop activities of event-member:"
            },
            since = "5.0.0-a1"
    )
    public static List<Activity> activities(Member m) {
        return m.getActivities();
    }

    @Property("voice channel")
    @Documentation(
            name = "Member Voice Channel",
            description = "The audio (voice) channel the member is currently connected to, or null if they are not in a voice channel.",
            examples = "reply with voice channel of event-member",
            since = "5.0.0-a1"
    )
    public static @Nullable AudioChannel voiceChannel(Member m) {
        return m.getVoiceState().getChannel();
    }

    @Property(value = "online status", shared = true)
    public static OnlineStatus onlineStatus(Member m) {
        return m.getOnlineStatus();
    }

    @Property(value = "avatar url", shared = true)
    public static String avatarUrl(Member m) {
        return m.getEffectiveAvatarUrl();
    }

    @Property(value = "guild", shared = true)
    public static Guild guild(Member m) {
        return m.getGuild();
    }

    @Condition(property = "muted", type = ConditionType.BE)
    @Documentation(
            name = "Member is Muted",
            description = "Whether the member is muted (self or by the guild) in a voice channel.",
            examples = "if event-member is muted:",
            since = "5.0.0-a1"
    )
    public static boolean muted(Member m) {
        GuildVoiceState state = m.getVoiceState();
        return state != null && state.isMuted();
    }

    @Condition(property = "deafened", type = ConditionType.BE)
    @Documentation(
            name = "Member is Deafened",
            description = "Whether the member is deafened (self or by the guild) in a voice channel.",
            examples = "if event-member is deafened:",
            since = "5.0.0-a1"
    )
    public static boolean deafened(Member m) {
        GuildVoiceState state = m.getVoiceState();
        return state != null && state.isDeafened();
    }

    @Condition(property = "timed out", type = ConditionType.BE)
    @Documentation(
            name = "Member is Timed Out",
            description = "Whether the member is currently timed out in the guild.",
            examples = "if event-member is timed out:",
            since = "5.0.0-a1"
    )
    public static boolean timedOut(Member m) {
        return m.isTimedOut();
    }

    @Condition(property = "[a] discord bot", type = ConditionType.BE)
    @Documentation(
            name = "Member is a Bot",
            description = "Whether the member's account is a Discord bot.",
            examples = "if event-member is a discord bot:",
            since = "5.0.0-a1"
    )
    public static boolean bot(Member m) {
        return m.getUser().isBot();
    }

    @Condition(patterns = {
            "%members% (has|have) discord [role] %role%",
            "%members% (doesn't|don't|does not|do not) have discord [role] %role%"
    }, negated = {1})
    @Documentation(
            name = "Member has Role",
            description = "Whether the member has the given role.",
            examples = "if event-member has discord role {_role}:",
            since = "5.0.0-a1"
    )
    public static boolean hasRole(Member m, Role role) {
        return m.getRoles().contains(role);
    }

    @Condition(patterns = {
            "%member% can interact with %members/roles%",
            "%member% (can't|cannot|can not) interact with %members/roles%"
    }, type = ConditionType.CAN, negated = {1})
    @Documentation(
            name = "Member can Interact",
            description = "Whether the member can interact with (act upon) any of the given members or roles.",
            examples = "if event-member can interact with {_target}:",
            since = "5.0.0-a1"
    )
    public static boolean canInteract(Member m, Object[] targets) {
        for (Object target : targets) {
            if (target instanceof Member other && m.canInteract(other)) return true;
            if (target instanceof Role role && m.canInteract(role)) return true;
        }
        return false;
    }

    @Property("discord permissions")
    @Documentation(
            name = "Member Permissions",
            description = {
                "The effective guild-wide permissions of the member: every permission given by its roles and by "
                    + "@everyone, or all of them for the owner and for an administrator. Channel overrides are not "
                    + "included; use the discord permissions of the member in a channel for that.",
                "Read-only: a member gets its permissions from its roles, so change the roles or the role permissions instead."
            },
            examples = {
                "set {_perms::*} to discord permissions of event-member",
                "if discord permissions of event-member contain manage messages:"
            },
            since = "5.0.0-b1"
    )
    public static List<Permission> discordPermissions(Member m) {
        return List.copyOf(m.getPermissions());
    }

    @Condition(patterns = {
            "%members% (has|have) [the] discord permission[s] %permissions% [in [the] [channel] %-guildchannel%]",
            "%members% (doesn't|don't|does not|do not) have [the] discord permission[s] %permissions% [in [the] [channel] %-guildchannel%]"
    }, negated = {1})
    @Documentation(
            name = "Member has Permissions",
            description = "Whether the member has all the given permissions, either guild-wide or in a specific channel "
                    + "(channel overrides included). An administrator has every permission.",
            examples = {
                "if event-member has discord permission administrator:",
                "if event-member has discord permissions send messages and embed links in event-channel:"
            },
            since = "5.0.0-a1"
    )
    public static boolean hasPermission(Member m, Permission[] permissions, @Nullable GuildChannel channel) {
        if (channel == null)
            return m.hasPermission(permissions);
        return m.hasPermission(channel, permissions);
    }

    @Condition(patterns = {
            "%members% (is|are) in [the] thread %threadchannel%",
            "%members% (isn't|is not|aren't|are not) in [the] thread %threadchannel%"
    }, negated = {1})
    @Documentation(
            name = "Member is in Thread",
            description = "Whether the member has joined the given thread. Useful to avoid errors when "
                    + "adding a member to a thread they are already in.",
            examples = "if event-member is in the thread {_thread}:",
            since = "5.0.0-a1"
    )
    public static boolean isInThread(Member m, ThreadChannel thread) {
        return thread.getThreadMember(m) != null;
    }
}
