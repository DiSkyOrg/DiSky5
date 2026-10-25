package net.itsthesky.disky5.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public final class BotManager {

    private static final Logger LOGGER = Logger.getLogger("DiSky");

    private final Map<String, Bot> byName = new ConcurrentHashMap<>();
    private final Map<ShardManager, Bot> byManager = new ConcurrentHashMap<>();

    public void register(Bot bot) {
        if (bot.getName() != null)
            byName.put(bot.getName().toLowerCase(java.util.Locale.ROOT), bot);
        ShardManager manager = bot.getShardManager();
        if (manager != null)
            byManager.put(manager, bot);
    }

    public void unregister(Bot bot) {
        if (bot.getName() != null)
            byName.remove(bot.getName().toLowerCase(java.util.Locale.ROOT));
        ShardManager manager = bot.getShardManager();
        if (manager != null)
            byManager.remove(manager);
    }

    public boolean exists(String name) {
        return byName(name) != null;
    }

    public @Nullable Bot byName(String name) {
        return name == null ? null : byName.get(name.toLowerCase(java.util.Locale.ROOT));
    }

    public @Nullable Bot resolve(@Nullable JDA jda) {
        if (jda == null) return null;
        ShardManager manager = jda.getShardManager();
        return manager == null ? null : byManager.get(manager);
    }

    public @Nullable Bot findAny() {
        int size = byManager.size();
        if (size == 0) return null;
        Bot any = byManager.values().stream()
            .min(Comparator.comparing(Bot::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
            .orElseThrow();
        if (size > 1)
            LOGGER.warning("Several bots are loaded but none was specified; using '" + any.getName()
                + "'. Specify the bot explicitly to remove this ambiguity.");
        return any;
    }

    public Collection<Bot> all() {
        return Collections.unmodifiableCollection(byManager.values());
    }
}
