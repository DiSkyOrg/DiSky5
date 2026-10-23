package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.annotations.ConditionType;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Bot;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "bot", jda = Bot.class)
public final class BotType {

    @Property(value = "name", shared = true)
    public static String discordName(Bot bot) {
        return bot.getName();
    }

    @Property("token")
    @Documentation(name = "Bot Token", description = "The bot's login token. Build-time only.",
            examples = "set token of {_bot} to \"...\"", since = "5.0.0-a1")
    public static @Nullable String token(Bot bot) { return bot.getToken(); }
    @Property.Setter("token")
    public static void setToken(Bot bot, String token) { bot.setToken(token); }

    @Property("intents")
    @Documentation(name = "Bot Intents", description = "The gateway intents, comma-separated by name "
            + "(or \"default intents\" / \"all\"). Build-time only.",
            examples = "set intents of {_bot} to \"guild_members, message_content\"", since = "5.0.0-a1")
    public static String intents(Bot bot) { return bot.getIntents(); }
    @Property.Setter("intents")
    public static void setIntents(Bot bot, String intents) { bot.setIntents(intents); }

    @Property("member cache policy")
    @Documentation(name = "Bot Member Cache Policy", description = "all / default / none / online / owner / voice. Build-time only.",
            examples = "set member cache policy of {_bot} to \"all\"", since = "5.0.0-a1")
    public static String memberCachePolicy(Bot bot) { return bot.getMemberCachePolicy(); }
    @Property.Setter("member cache policy")
    public static void setMemberCachePolicy(Bot bot, String policy) { bot.setMemberCachePolicy(policy); }

    @Property("cache flags")
    @Documentation(name = "Bot Cache Flags", description = "The enabled cache flags, comma-separated by name "
            + "(or \"all\" / \"default\" / \"none\"). Build-time only.",
            examples = "set cache flags of {_bot} to \"activity, voice_state\"", since = "5.0.0-a1")
    public static String cacheFlags(Bot bot) { return bot.getCacheFlags(); }
    @Property.Setter("cache flags")
    public static void setCacheFlags(Bot bot, String flags) { bot.setCacheFlags(flags); }

    @Property("compression")
    @Documentation(name = "Bot Compression", description = "zlib / none. Build-time only.",
            examples = "set compression of {_bot} to \"none\"", since = "5.0.0-a1")
    public static @Nullable String compression(Bot bot) { return bot.getCompression(); }
    @Property.Setter("compression")
    public static void setCompression(Bot bot, String compression) { bot.setCompression(compression); }

    @Property("shard count")
    @Documentation(name = "Bot Shard Count",
            description = "The number of shards. -1 (default) = Discord's recommended count, 1 = single shard, "
                    + "N>=2 = N shards. Build-time only; once running, returns the real shard total.",
            examples = "set shard count of {_bot} to 4", since = "5.0.0-a1")
    public static int shardCount(Bot bot) { return bot.getShardCount(); }
    @Property.Setter("shard count")
    public static void setShardCount(Bot bot, int count) { bot.setShardCount(count); }

    @Property("activity")
    @Documentation(name = "Bot Activity", description = "The bot's activity text (e.g. \"playing\"). Live-updatable.",
            examples = "set activity of {_bot} to \"with Skript\"", since = "5.0.0-a1")
    public static @Nullable String activity(Bot bot) { return bot.getActivity(); }
    @Property.Setter("activity")
    public static void setActivity(Bot bot, @Nullable String activity) { bot.setActivity(activity); }

    @Property(value = "online status", shared = true)
    public static @Nullable OnlineStatus onlineStatus(Bot bot) { return bot.getOnlineStatus(); }
    @Property.Setter(value = "online status", shared = true)
    public static void setOnlineStatus(Bot bot, OnlineStatus status) { bot.setOnlineStatus(status); }

    @Property("gateway ping")
    @Documentation(name = "Bot Gateway Ping", description = "Average gateway ping across shards, in ms (-1 if not running).",
            examples = "send \"Ping: %gateway ping of event-bot%ms\"", since = "5.0.0-a1")
    public static long gatewayPing(Bot bot) { return bot.getGatewayPing(); }

    @Property("guild count")
    @Documentation(name = "Bot Guild Count", description = "Number of guilds the bot is in (0 if not running).",
            examples = "send \"%guild count of event-bot% guilds\"", since = "5.0.0-a1")
    public static int guildCount(Bot bot) { return bot.getGuildCount(); }

    @Property("running shards")
    @Documentation(name = "Bot Running Shards", description = "Number of currently-connected shards (0 if not running).",
            examples = "send \"%running shards of event-bot% shards up\"", since = "5.0.0-a1")
    public static int runningShards(Bot bot) { return bot.getRunningShards(); }

    @Property("guilds")
    @Documentation(name = "Bot Guilds", description = "All guilds the bot is a member of (empty if not running).",
            examples = "set {_g} to first element of guilds of event-bot", since = "5.0.0-a1")
    public static List<Guild> guilds(Bot bot) { return bot.getGuilds(); }

    @Property("self user")
    @Documentation(name = "Bot Self User", description = "The bot's own user account (none if not running).",
            examples = "send \"%name of self user of event-bot%\"", since = "5.0.0-a1")
    public static @Nullable User selfUser(Bot bot) { return bot.getSelfUser(); }

    @Property(value = "avatar url", shared = true)
    public static @Nullable String avatarUrl(Bot bot) {
        return bot.getSelfUser() == null ? null : bot.getSelfUser().getEffectiveAvatarUrl();
    }

    @Property.Retrieve("application info")
    @Documentation(name = "Bot Application Info",
            description = "The application (developer portal) info of the bot. Fetched online, so it requires 'await'.",
            examples = "await set {_info} to application info of event-bot", since = "5.0.0-a1")
    public static RestAction<ApplicationInfo> applicationInfo(Bot bot) {
        final ShardManager manager = bot.getShardManager();
        final List<JDA> shards = manager == null ? List.of() : manager.getShards();
        if (shards.isEmpty())
            throw new IllegalStateException("the bot '" + bot + "' is not connected yet");
        return shards.get(0).retrieveApplicationInfo();
    }

    @Condition(property = "connected", type = ConditionType.BE)
    @Documentation(name = "Bot is Connected",
            description = "Whether the bot is logged in and running (its shards have been built).",
            examples = "if {_bot} is connected:", since = "5.0.0-a1")
    public static boolean connected(Bot bot) {
        return bot.getState() == Bot.State.RUNNING;
    }
}
