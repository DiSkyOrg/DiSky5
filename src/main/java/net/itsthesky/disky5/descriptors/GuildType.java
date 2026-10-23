package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildWelcomeScreen;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.automod.AutoModRule;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

@SkriptType(name = "guild", jda = Guild.class)
public final class GuildType {

    @Property("members")
    @Documentation(
            name = "Guild Members",
            description = {
                "The members of the guild. Without 'await' this is the cached member list, which may "
                    + "be incomplete depending on the bot's cache settings.",
                "With 'await' the full member list is loaded online (this can be slow on large guilds)."
            },
            examples = {
                "set {_cached::*} to members of event-guild",
                "await set {_all::*} to members of event-guild"
            },
            since = "5.0.0-a1"
    )
    public static List<Member> members(Guild guild) {
        return guild.getMembers();
    }

    @Property.Retrieve("members")
    public static List<Member> retrieveMembers(Guild guild) {
        return guild.loadMembers().get();
    }

    @Property(value = "name", shared = true)
    public static String discordName(Guild guild) {
        return guild.getName();
    }

    @Property("afk channel")
    @Documentation(
            name = "Guild AFK Channel",
            description = "The AFK voice channel of the guild, or null if none is set.",
            examples = "set afk channel of event-guild to voice channel with id \"123\"",
            since = "5.0.0-a1"
    )
    public static @Nullable VoiceChannel afkChannel(Guild guild) {
        return guild.getAfkChannel();
    }

    @Property.Setter("afk channel")
    public static RestAction<?> setAfkChannel(Guild guild, VoiceChannel value) {
        return guild.getManager().setAfkChannel(value);
    }

    @Property("afk timeout")
    @Documentation(
            name = "Guild AFK Timeout",
            description = "The AFK timeout of the guild, in seconds (60, 300, 900, 1800 or 3600).",
            examples = "set afk timeout of event-guild to 300",
            since = "5.0.0-a1"
    )
    public static Number afkTimeout(Guild guild) {
        return guild.getAfkTimeout().getSeconds();
    }

    @Property.Setter("afk timeout")
    public static RestAction<?> setAfkTimeout(Guild guild, Number value) {
        final long seconds = value.longValue();
        final Guild.Timeout timeout;
        if (seconds <= 60) {
            timeout = Guild.Timeout.SECONDS_60;
        } else if (seconds <= 300) {
            timeout = Guild.Timeout.SECONDS_300;
        } else if (seconds <= 900) {
            timeout = Guild.Timeout.SECONDS_900;
        } else if (seconds <= 1800) {
            timeout = Guild.Timeout.SECONDS_1800;
        } else {
            timeout = Guild.Timeout.SECONDS_3600;
        }
        return guild.getManager().setAfkTimeout(timeout);
    }

    @Property("banner")
    @Documentation(
            name = "Guild Banner",
            description = "The URL of the guild's banner image, or null if none is set.",
            examples = "set {_banner} to banner of event-guild",
            since = "5.0.0-a1"
    )
    public static @Nullable String banner(Guild guild) {
        return guild.getBannerUrl();
    }

    @Property.Setter("banner")
    public static RestAction<?> setBanner(Guild guild, String value) {
        final net.dv8tion.jda.api.entities.Icon icon;
        try {
            final java.io.InputStream stream;
            if (value.startsWith("http://") || value.startsWith("https://")) {
                stream = new java.net.URL(value).openStream();
            } else {
                stream = new java.io.FileInputStream(new java.io.File(value));
            }
            icon = net.dv8tion.jda.api.entities.Icon.from(stream);
        } catch (java.io.IOException ex) {
            throw new RuntimeException("Could not read banner image from '" + value + "'", ex);
        }
        return guild.getManager().setBanner(icon);
    }

    @Property("boost count")
    @Documentation(
            name = "Guild Boost Count",
            description = "How many members are currently boosting the guild.",
            examples = "reply with \"%boost count of event-guild% booster(s)!\"",
            since = "5.0.0-a1"
    )
    public static Number boostCount(Guild guild) {
        return guild.getBoostCount();
    }

    @Property("boost role")
    @Documentation(
            name = "Guild Booster Role",
            description = "The booster role of the guild (granted to members who boost it), or null if none.",
            examples = "reply with \"Thanks %mention tag of boost role of event-guild%!\"",
            since = "5.0.0-a1"
    )
    public static @Nullable Role boostRole(Guild guild) {
        return guild.getBoostRole();
    }

    @Property("boost tier")
    @Documentation(
            name = "Guild Boost Tier",
            description = "The boost tier of the guild (e.g. 'none', 'tier 1', 'tier 2', 'tier 3').",
            examples = "reply with boost tier of event-guild",
            since = "5.0.0-a1"
    )
    public static String boostTier(Guild guild) {
        return guild.getBoostTier().name().toLowerCase(Locale.ROOT).replace("_", " ");
    }

    @Property("bot role")
    @Documentation(
            name = "Guild Bot Role",
            description = "The integration role of this bot in the guild, or null if it has none.",
            examples = "reply with mention tag of bot role of event-guild",
            since = "5.0.0-a1"
    )
    public static @Nullable Role botRole(Guild guild) {
        return guild.getBotRole();
    }

    @Property("everyone role")
    @Documentation(
            name = "Everyone Role",
            description = "The @everyone (public) role of the guild.",
            examples = "reply with mention tag of everyone role of event-guild",
            since = "5.0.0-a1"
    )
    public static Role everyoneRole(Guild guild) {
        return guild.getPublicRole();
    }

    @Property("member count")
    @Documentation(
            name = "Guild Member Count",
            description = "The total number of members in the guild.",
            examples = "reply with \"%member count of event-guild% members\"",
            since = "5.0.0-a1"
    )
    public static Number memberCount(Guild guild) {
        return guild.getMemberCount();
    }

    @Property("verification level")
    @Documentation(
            name = "Guild Verification Level",
            description = "The verification level of the guild ('none', 'low', 'medium', 'high', 'very high').",
            examples = {
                "reply with verification level of event-guild",
                "set verification level of event-guild to \"high\""
            },
            since = "5.0.0-a1"
    )
    public static String verificationLevel(Guild guild) {
        return guild.getVerificationLevel().name().toLowerCase(Locale.ROOT).replace("_", " ");
    }

    @Property.Setter("verification level")
    public static RestAction<?> setVerificationLevel(Guild guild, String value) {
        final Guild.VerificationLevel level =
                Guild.VerificationLevel.valueOf(value.toUpperCase(Locale.ROOT).replace(" ", "_"));
        return guild.getManager().setVerificationLevel(level);
    }

    @Property("boosters")
    @Documentation(
            name = "Guild Boosters",
            description = "The members currently boosting the guild.",
            examples = "reply with \"Boosters: %boosters of event-guild%!\"",
            since = "5.0.0-a1"
    )
    public static List<Member> boosters(Guild guild) {
        return guild.getBoosters();
    }

    @Property("scheduled events")
    @Documentation(
            name = "Guild Scheduled Events",
            description = "All scheduled events of the guild.",
            examples = "set {_events::*} to scheduled events of event-guild",
            since = "5.0.0-a1"
    )
    public static List<ScheduledEvent> scheduledEvents(Guild guild) {
        return guild.getScheduledEvents();
    }

    @Property("text channels")
    @Documentation(
            name = "Guild Text Channels",
            description = "All text channels of the guild.",
            examples = "set {_channels::*} to text channels of event-guild",
            since = "5.0.0-a1"
    )
    public static List<TextChannel> textChannels(Guild guild) {
        return guild.getTextChannels();
    }

    @Property("voice channels")
    @Documentation(
            name = "Guild Voice Channels",
            description = "All voice channels of the guild.",
            examples = "set {_channels::*} to voice channels of event-guild",
            since = "5.0.0-a1"
    )
    public static List<VoiceChannel> voiceChannels(Guild guild) {
        return guild.getVoiceChannels();
    }

    @Property("news channels")
    @Documentation(
            name = "Guild News Channels",
            description = "All news (announcement) channels of the guild.",
            examples = "set {_channels::*} to news channels of event-guild",
            since = "5.0.0-a1"
    )
    public static List<NewsChannel> newsChannels(Guild guild) {
        return guild.getNewsChannels();
    }

    @Property("stage channels")
    @Documentation(
            name = "Guild Stage Channels",
            description = "All stage channels of the guild.",
            examples = "set {_channels::*} to stage channels of event-guild",
            since = "5.0.0-a1"
    )
    public static List<StageChannel> stageChannels(Guild guild) {
        return guild.getStageChannels();
    }

    @Property("categories")
    @Documentation(
            name = "Guild Categories",
            description = "All channel categories of the guild.",
            examples = "set {_categories::*} to categories of event-guild",
            since = "5.0.0-a1"
    )
    public static List<Category> categories(Guild guild) {
        return guild.getCategories();
    }

    @Property("guild channels")
    @Documentation(
            name = "All Guild Channels",
            description = "Every guild channel, including text, voice, stage, news and category channels.",
            examples = "set {_channels::*} to guild channels of event-guild",
            since = "5.0.0-a1"
    )
    public static List<GuildChannel> guildChannels(Guild guild) {
        return guild.getChannels();
    }

    @Property("roles")
    @Documentation(
            name = "Guild Roles",
            description = "All roles of the guild, ordered by position (highest first).",
            examples = "set {_roles::*} to roles of event-guild",
            since = "5.0.0-a1"
    )
    public static List<Role> roles(Guild guild) {
        return guild.getRoles();
    }

    @Property("emojis")
    @Documentation(
            name = "Guild Emojis",
            description = "All custom emojis of the guild.",
            examples = "set {_emojis::*} to emojis of event-guild",
            since = "5.0.0-a1"
    )
    public static List<RichCustomEmoji> emojis(Guild guild) {
        return guild.getEmojis();
    }

    @Property("stickers")
    @Documentation(
            name = "Guild Stickers",
            description = "All custom stickers of the guild.",
            examples = "set {_stickers::*} to stickers of event-guild",
            since = "5.0.0-a1"
    )
    public static List<GuildSticker> stickers(Guild guild) {
        return guild.getStickers();
    }

    @Property.Retrieve("invites")
    @Documentation(
            name = "Guild Invites",
            description = "All active invites of the guild. Fetched online, so it requires 'await'.",
            examples = "await set {_invites::*} to invites of event-guild",
            since = "5.0.0-a1"
    )
    public static RestAction<List<Invite>> invites(Guild guild) {
        return guild.retrieveInvites();
    }

    @Property.Retrieve("webhooks")
    @Documentation(
            name = "Guild Webhooks",
            description = "All webhooks of the guild. Fetched online, so it requires 'await'.",
            examples = "await set {_webhooks::*} to webhooks of event-guild",
            since = "5.0.0-a1"
    )
    public static RestAction<List<Webhook>> webhooks(Guild guild) {
        return guild.retrieveWebhooks();
    }

    @Property.Retrieve("welcome screen")
    @Documentation(
            name = "Guild Welcome Screen",
            description = {"The welcome screen of the guild, fetched online, so it requires 'await'.",
                "Fails when the guild is not a community guild."},
            examples = "await set {_screen} to the welcome screen of event-guild",
            since = "5.0.0-a2"
    )
    public static RestAction<GuildWelcomeScreen> welcomeScreen(Guild guild) {
        return guild.retrieveWelcomeScreen();
    }

    @Property.Retrieve("automod rules")
    @Documentation(
            name = "Guild AutoMod Rules",
            description = "All AutoMod rules of the guild. Fetched online, so it requires 'await'.",
            examples = "await set {_rules::*} to automod rules of event-guild",
            since = "5.0.0-a2"
    )
    public static RestAction<List<AutoModRule>> automodRules(Guild guild) {
        return guild.retrieveAutoModRules();
    }

    @Property.Retrieve("stickers")
    public static RestAction<List<GuildSticker>> retrieveStickers(Guild guild) {
        return guild.retrieveStickers();
    }

    @Property.Retrieve("emotes")
    @Documentation(
            name = "Guild Emotes",
            description = {
                "All custom emotes (emojis) of the guild, fetched online, so it requires 'await'.",
                "Unlike 'emojis of %guild%' this never depends on the bot's cache."
            },
            examples = "await set {_emotes::*} to emotes of event-guild",
            since = "5.0.0-a1"
    )
    public static RestAction<List<RichCustomEmoji>> emotes(Guild guild) {
        return guild.retrieveEmojis();
    }

    @Property.Retrieve("bans")
    @Documentation(
            name = "Guild Bans",
            description = {
                "All bans of the guild. Fetched online, so it requires 'await'.",
                "Only the first page (up to 1000 bans) is returned."
            },
            examples = "await set {_bans::*} to bans of event-guild",
            since = "5.0.0-a1"
    )
    public static RestAction<List<Guild.Ban>> bans(Guild guild) {
        return guild.retrieveBanList();
    }

    @Property.Retrieve("active threads")
    @Documentation(
            name = "Guild Active Threads",
            description = "Every non-archived thread channel of the guild. Fetched online, so it requires 'await'.",
            examples = "await set {_threads::*} to active threads of event-guild",
            since = "5.0.0-a1"
    )
    public static RestAction<List<ThreadChannel>> activeThreads(Guild guild) {
        return guild.retrieveActiveThreads();
    }

    @Property.Retrieve("audit logs")
    @Documentation(
            name = "Guild Audit Logs",
            description = {
                "The audit log entries of the guild. Fetched online, so it requires 'await'.",
                "Only the most recent page (up to 100 entries) is returned."
            },
            examples = "await set {_logs::*} to audit logs of event-guild",
            since = "5.0.0-a1"
    )
    public static RestAction<List<AuditLogEntry>> auditLogs(Guild guild) {
        return guild.retrieveAuditLogs();
    }

    @Property("discord owner")
    @Documentation(
            name = "Guild Owner",
            description = {
                "The owner of the guild, as a member.",
                "Without 'await' this is the cached member, which may be null if the owner is not cached; "
                    + "with 'await' it is fetched online."
            },
            examples = {
                "reply with discord owner of event-guild",
                "await set {_owner} to discord owner of event-guild"
            },
            since = "5.0.0-a1"
    )
    public static @Nullable Member owner(Guild guild) {
        return guild.getOwner();
    }

    @Property.Retrieve("discord owner")
    public static RestAction<Member> retrieveOwner(Guild guild) {
        return guild.retrieveOwner();
    }

    @Property(value = "avatar url", shared = true)
    public static @Nullable String avatarUrl(Guild guild) {
        return guild.getIconUrl();
    }
}
