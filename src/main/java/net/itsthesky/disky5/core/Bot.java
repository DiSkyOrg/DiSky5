package net.itsthesky.disky5.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.api.events.DiSkyEventBus;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class Bot {

    public enum State { CONFIGURING, RUNNING }

    public static final EnumSet<GatewayIntent> DEFAULT_INTENTS = EnumSet.of(
        GatewayIntent.GUILD_MODERATION, GatewayIntent.GUILD_EXPRESSIONS, GatewayIntent.GUILD_WEBHOOKS,
        GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS,
        GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT,
        GatewayIntent.SCHEDULED_EVENTS);

    private volatile State state = State.CONFIGURING;

    private String name;
    private String token;
    private final Set<GatewayIntent> intents = EnumSet.copyOf(DEFAULT_INTENTS);
    private final Set<CacheFlag> cacheFlags = EnumSet.noneOf(CacheFlag.class);
    private MemberCachePolicy memberCachePolicy = MemberCachePolicy.DEFAULT;
    private String memberCachePolicyName = "DEFAULT";
    private Compression compression = Compression.ZLIB;
    private boolean autoReconnect = true;
    private int shardCount = -1;
    private @Nullable Activity activity;
    private @Nullable OnlineStatus onlineStatus;

    private volatile @Nullable ShardManager shardManager;
    private volatile int actualShardCount = -1;
    private final Set<JDA> readyShards = ConcurrentHashMap.newKeySet();
    private final AtomicInteger downShards = new AtomicInteger();
    private volatile boolean firedReady, firedShutdown;

    public Bot() {}

    public void login(String name) {
        if (state == State.RUNNING)
            throw new IllegalStateException("the bot '" + this.name + "' is already connected");
        if (token == null || token.isBlank())
            throw new IllegalStateException("a bot needs a token before login (set token of %bot% to ...)");
        this.name = name;

        DiSky5 plugin = DiSky5.getInstance();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token)
                    .setShardsTotal(shardCount)
                    .setAutoReconnect(autoReconnect)
                    .setMemberCachePolicy(memberCachePolicy)
                    .enableIntents(intents)
                    .addEventListeners(DiSkyEventBus.INSTANCE);
                if (compression != null) builder.setCompression(compression);
                if (!cacheFlags.isEmpty()) builder.enableCache(cacheFlags);
                if (activity != null) builder.setActivity(activity);
                if (onlineStatus != null) builder.setStatus(onlineStatus);

                ShardManager manager = builder.build();
                this.shardManager = manager;
                this.actualShardCount = manager.getShardsTotal();
                this.state = State.RUNNING;
                DiSky5.getBots().register(this);
            } catch (Throwable t) {
                DiSkyRuntimeHandler.error(new RuntimeException(
                    "Failed to start the bot '" + name + "': " + t.getMessage(), t));
            }
        });
    }

    public void shutdown() {
        ShardManager manager = shardManager;
        if (manager != null)
            manager.shutdown();
        DiSky5.getBots().unregister(this);
    }

    public boolean onShardReady(JDA shard) {
        readyShards.add(shard);
        int total = actualShardCount > 0 ? actualShardCount : 1;
        if (!firedReady && readyShards.size() >= total) {
            firedReady = true;
            return true;
        }
        return false;
    }

    public boolean onShardShutdown() {
        int total = actualShardCount > 0 ? actualShardCount : 1;
        if (!firedShutdown && downShards.incrementAndGet() >= total) {
            firedShutdown = true;
            return true;
        }
        return false;
    }

    private void requireConfiguring(String what) {
        if (state == State.RUNNING)
            throw new IllegalStateException(what + " can't be changed on a running bot; set it before login");
    }

    public void setToken(String token) {
        requireConfiguring("token");
        this.token = unquote(token);
    }

    public void setIntents(String csv) {
        requireConfiguring("intents");
        intents.clear();
        intents.addAll(parseIntents(splitCsv(csv)));
    }

    public void setCacheFlags(String csv) {
        requireConfiguring("cache flags");
        cacheFlags.clear();
        cacheFlags.addAll(parseFlags(splitCsv(csv)));
    }

    public void setMemberCachePolicy(String policy) {
        requireConfiguring("member cache policy");
        this.memberCachePolicy = parsePolicy(policy);
        this.memberCachePolicyName = norm(policy);
    }

    public void setCompression(String compression) {
        requireConfiguring("compression");
        this.compression = Compression.valueOf(norm(compression));
    }

    public void setShardCount(int count) {
        requireConfiguring("shard count");
        this.shardCount = count;
    }

    public void setActivity(@Nullable String activity) {
        Activity parsed = (activity == null || activity.isBlank()) ? null : Activity.playing(activity);
        this.activity = parsed;
        ShardManager manager = shardManager;
        if (state == State.RUNNING && manager != null)
            manager.setActivity(parsed);
    }

    public void setOnlineStatus(OnlineStatus status) {
        this.onlineStatus = status;
        ShardManager manager = shardManager;
        if (state == State.RUNNING && manager != null)
            manager.setStatus(status);
    }

    public State getState() { return state; }
    public String getName() { return name; }
    public @Nullable ShardManager getShardManager() { return shardManager; }

    public @Nullable String getToken() { return token; }
    public String getIntents() { return String.join(", ", intents.stream().map(Enum::name).toList()); }
    public String getCacheFlags() { return String.join(", ", cacheFlags.stream().map(Enum::name).toList()); }
    public String getMemberCachePolicy() { return memberCachePolicyName; }
    public @Nullable String getCompression() { return compression == null ? null : compression.name(); }
    public @Nullable String getActivity() { return activity == null ? null : activity.getName(); }
    public @Nullable OnlineStatus getOnlineStatus() { return onlineStatus; }

    public int getShardCount() {
        return state == State.RUNNING ? actualShardCount : shardCount;
    }

    public int getRunningShards() {
        ShardManager manager = shardManager;
        return manager == null ? 0 : manager.getShardsRunning();
    }

    public long getGatewayPing() {
        ShardManager manager = shardManager;
        return manager == null ? -1 : (long) manager.getAverageGatewayPing();
    }

    public int getGuildCount() {
        ShardManager manager = shardManager;
        return manager == null ? 0 : (int) manager.getGuildCache().size();
    }

    public @Nullable JDA anyShard() {
        ShardManager manager = shardManager;
        if (manager == null) return null;
        for (JDA shard : manager.getShardCache())
            return shard;
        return null;
    }

    public List<Guild> getGuilds() {
        ShardManager manager = shardManager;
        return manager == null ? List.of() : manager.getGuilds();
    }

    public @Nullable Guild getGuildById(String id) {
        ShardManager manager = shardManager;
        return manager == null ? null : manager.getGuildById(id);
    }

    public @Nullable RestAction<User> retrieveUserById(String id) {
        ShardManager manager = shardManager;
        return manager == null ? null : manager.retrieveUserById(id);
    }

    public @Nullable SelfUser getSelfUser() {
        ShardManager manager = shardManager;
        if (manager == null) return null;
        for (JDA shard : manager.getShards())
            if (shard.getSelfUser() != null)
                return shard.getSelfUser();
        return null;
    }

    private static String norm(String s) { return s.trim().toUpperCase(Locale.ROOT).replace(' ', '_'); }

    private static String[] splitCsv(String csv) {
        if (csv == null || csv.isBlank()) return new String[0];
        return java.util.Arrays.stream(csv.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }

    private static String unquote(String text) {
        if (text == null) return null;
        while (text.contains("\"\"")) text = text.replace("\"\"", "\"");
        if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\""))
            text = text.substring(1, text.length() - 1);
        return text;
    }

    private static Set<GatewayIntent> parseIntents(String... names) {
        EnumSet<GatewayIntent> result = EnumSet.noneOf(GatewayIntent.class);
        for (String raw : names) {
            if (raw == null) continue;
            String n = raw.trim();
            if (n.equalsIgnoreCase("default") || n.equalsIgnoreCase("default intents")) { result.addAll(DEFAULT_INTENTS); continue; }
            if (n.equalsIgnoreCase("all")) { result.addAll(EnumSet.allOf(GatewayIntent.class)); continue; }
            result.add(GatewayIntent.valueOf(norm(n)));
        }
        return result;
    }

    private static Set<CacheFlag> parseFlags(String... names) {
        EnumSet<CacheFlag> result = EnumSet.noneOf(CacheFlag.class);
        for (String raw : names) {
            if (raw == null) continue;
            String n = raw.trim();
            if (n.equalsIgnoreCase("none")) continue;
            if (n.equalsIgnoreCase("all")) { result.addAll(EnumSet.allOf(CacheFlag.class)); continue; }
            if (n.equalsIgnoreCase("default")) { result.addAll(Set.of(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.ONLINE_STATUS)); continue; }
            result.add(CacheFlag.valueOf(norm(n)));
        }
        return result;
    }

    private static MemberCachePolicy parsePolicy(String policy) {
        String n = norm(policy);
        try {
            return (MemberCachePolicy) MemberCachePolicy.class.getDeclaredField(n).get(null);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException("unknown member cache policy '" + policy
                + "' (try: all, default, none, online, owner, voice)");
        }
    }

    @Override
    public String toString() {
        return name == null ? "(unnamed bot)" : name;
    }
}
