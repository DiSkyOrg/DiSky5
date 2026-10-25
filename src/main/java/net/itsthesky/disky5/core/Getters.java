package net.itsthesky.disky5.core;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.automod.AutoModRule;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.itsthesky.disky5.annotations.Getter;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

public final class Getters {

    private Getters() {}

    @Getter("role")
    @Documentation(name = "Role With Id", description = "The role with the given id, looked up in cache.",
        examples = "set {_r} to role with id \"123\"", since = "5.0.0-a1")
    public static @Nullable Role role(ShardManager bot, String id) {
        return bot.getRoleById(id);
    }

    @Getter("user")
    @Documentation(name = "User With Id", description = "The user with the given id; cache, or online with await.",
        examples = "await set {_u} to user with id \"123\"", since = "5.0.0-a1")
    public static @Nullable User user(ShardManager bot, String id) {
        return bot.getUserById(id);
    }

    @Getter.Retrieve("user")
    public static RestAction<User> retrieveUser(ShardManager bot, String id) {
        return bot.retrieveUserById(id);
    }

    @Getter("guild")
    @Documentation(name = "Guild With Id", description = "The guild with the given id, looked up in cache.",
        examples = "set {_g} to guild with id \"123\"", since = "5.0.0-a1")
    public static @Nullable Guild guild(ShardManager bot, String id) {
        return bot.getGuildById(id);
    }

    @Getter("channel")
    @Documentation(name = "Channel With Id", description = "The channel of any kind with the given id, from cache.",
        examples = "set {_c} to channel with id \"123\"", since = "5.0.0-b1")
    public static @Nullable Channel channel(ShardManager bot, String id) {
        return bot.getChannelById(Channel.class, id);
    }

    @Getter("guild channel")
    @Documentation(name = "Guild Channel With Id", description = "The guild channel of any kind with the given id, from cache.",
        examples = "set {_c} to guild channel with id \"123\"", since = "5.0.0-b1")
    public static @Nullable GuildChannel guildChannel(ShardManager bot, String id) {
        return bot.getGuildChannelById(id);
    }

    @Getter("message channel")
    @Documentation(name = "Message Channel With Id", description = "The channel messages can be sent in (text, news, voice, thread, private...) with the given id, from cache.",
        examples = "set {_c} to message channel with id \"123\"", since = "5.0.0-b1")
    public static @Nullable MessageChannel messageChannel(ShardManager bot, String id) {
        return bot.getChannelById(MessageChannel.class, id);
    }

    @Getter("audio channel")
    @Documentation(name = "Audio Channel With Id", description = "The voice or stage channel with the given id, from cache.",
        examples = "set {_c} to audio channel with id \"123\"", since = "5.0.0-b1")
    public static @Nullable AudioChannel audioChannel(ShardManager bot, String id) {
        return bot.getChannelById(AudioChannel.class, id);
    }

    @Getter("text channel")
    @Documentation(name = "Text Channel With Id", description = "The text channel with the given id, from cache.",
        examples = "set {_c} to text channel with id \"123\"", since = "5.0.0-a1")
    public static @Nullable TextChannel textChannel(ShardManager bot, String id) {
        return bot.getTextChannelById(id);
    }

    @Getter("voice channel")
    @Documentation(name = "Voice Channel With Id", description = "The voice channel with the given id, from cache.",
        examples = "set {_c} to voice channel with id \"123\"", since = "5.0.0-a1")
    public static @Nullable VoiceChannel voiceChannel(ShardManager bot, String id) {
        return bot.getVoiceChannelById(id);
    }

    @Getter("stage channel")
    @Documentation(name = "Stage Channel With Id", description = "The stage channel with the given id, from cache.",
        examples = "set {_c} to stage channel with id \"123\"", since = "5.0.0-a1")
    public static @Nullable StageChannel stageChannel(ShardManager bot, String id) {
        return bot.getStageChannelById(id);
    }

    @Getter("news channel")
    @Documentation(name = "News Channel With Id", description = "The news channel with the given id, from cache.",
        examples = "set {_c} to news channel with id \"123\"", since = "5.0.0-a1")
    public static @Nullable NewsChannel newsChannel(ShardManager bot, String id) {
        return bot.getNewsChannelById(id);
    }

    @Getter("forum channel")
    @Documentation(name = "Forum Channel With Id", description = "The forum channel with the given id, from cache.",
        examples = "set {_c} to forum channel with id \"123\"", since = "5.0.0-a1")
    public static @Nullable ForumChannel forumChannel(ShardManager bot, String id) {
        return bot.getForumChannelById(id);
    }

    @Getter("category")
    @Documentation(name = "Category With Id", description = "The category with the given id, from cache.",
        examples = "set {_c} to category with id \"123\"", since = "5.0.0-a1")
    public static @Nullable Category category(ShardManager bot, String id) {
        return bot.getCategoryById(id);
    }

    @Getter("thread channel")
    @Documentation(name = "Thread Channel With Id", description = "The thread channel with the given id, from cache.",
        examples = "set {_c} to thread channel with id \"123\"", since = "5.0.0-a1")
    public static @Nullable ThreadChannel threadChannel(ShardManager bot, String id) {
        return bot.getThreadChannelById(id);
    }

    @Getter("member")
    @Documentation(name = "Member With Id", description = "The member with the given id; cache, or online with await.",
        examples = "await set {_m} to member with id \"123\" in event-guild", since = "5.0.0-a1")
    public static @Nullable Member member(Guild guild, String id) {
        return guild.getMemberById(id);
    }

    @Getter.Retrieve("member")
    public static RestAction<Member> retrieveMember(Guild guild, String id) {
        return guild.retrieveMemberById(id);
    }

    @Getter("scheduled event")
    @Documentation(name = "Scheduled Event With Id", description = "The scheduled event with the given id, from cache.",
        examples = "set {_e} to scheduled event with id \"123\" in event-guild", since = "5.0.0-a1")
    public static @Nullable ScheduledEvent scheduledEvent(Guild guild, String id) {
        return guild.getScheduledEventById(id);
    }

    @Getter.Retrieve("automod rule")
    @Documentation(name = "AutoMod Rule With Id", description = "The automod rule with the given id (requires await).",
        examples = "await set {_rule} to automod rule with id \"123\" in event-guild", since = "5.0.0-a1")
    public static RestAction<AutoModRule> automodRule(Guild guild, String id) {
        return guild.retrieveAutoModRuleById(id);
    }

    @Getter.Retrieve("message")
    @Documentation(name = "Message With Id", description = "The message with the given id in a channel (requires await).",
        examples = "await set {_msg} to message with id \"123\" in event-channel", since = "5.0.0-a1")
    public static RestAction<Message> message(MessageChannel channel, String id) {
        return channel.retrieveMessageById(id);
    }
}
