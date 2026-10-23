package net.itsthesky.disky5.api.commands;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Timespan;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CooldownTracker {

    public enum Scope { USER, GUILD, CHANNEL }

    public static final CooldownTracker NONE = new CooldownTracker(null, Scope.USER);

    private final @Nullable Timespan cooldown;
    private final Scope scope;
    private final Map<Long, Long> lastUse = new ConcurrentHashMap<>();

    public CooldownTracker(@Nullable Timespan cooldown, Scope scope) {
        this.cooldown = cooldown;
        this.scope = scope;
    }

    public @Nullable Timespan cooldown() { return cooldown; }
    public Scope scope() { return scope; }
    public boolean enabled() { return cooldown != null; }

    public long key(User user, @Nullable Guild guild, @Nullable Channel channel) {
        return switch (scope) {
            case GUILD -> guild != null ? guild.getIdLong() : user.getIdLong();
            case CHANNEL -> channel != null ? channel.getIdLong() : user.getIdLong();
            case USER -> user.getIdLong();
        };
    }

    public long remaining(long key) {
        if (cooldown == null) return 0;
        Long last = lastUse.get(key);
        if (last == null) return 0;
        return Math.max(0, last + cooldown.getAs(Timespan.TimePeriod.MILLISECOND) - System.currentTimeMillis());
    }

    public void markUsed(long key) {
        if (cooldown != null)
            lastUse.put(key, System.currentTimeMillis());
    }

    public static @Nullable Scope parseScope(@Nullable String value) {
        if (value == null) return Scope.USER;
        return switch (value.trim().toLowerCase()) {
            case "user", "member" -> Scope.USER;
            case "guild", "server" -> Scope.GUILD;
            case "channel" -> Scope.CHANNEL;
            default -> {
                Skript.error("'" + value + "' is not a valid cooldown scope (expected user, guild or channel)");
                yield null;
            }
        };
    }
}
