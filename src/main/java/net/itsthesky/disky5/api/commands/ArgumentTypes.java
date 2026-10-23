package net.itsthesky.disky5.api.commands;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.itsthesky.disky5.DiSky5;
import net.itsthesky.disky5.core.Bot;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ArgumentTypes {

    private static final Pattern SNOWFLAKE = Pattern.compile("^<?[@#&!]*(\\d{15,21})>?$");

    private record Entry(Class<?> type, BiFunction<String, CommandInvocation, Object> resolver) {}

    private static final List<Entry> ENTRIES = new ArrayList<>();

    static {
        register(User.class, ArgumentTypes::user);
        register(Member.class, ArgumentTypes::member);
        register(Role.class, ArgumentTypes::role);
        register(Guild.class, ArgumentTypes::guild);
        register(Message.class, ArgumentTypes::message);
        register(CustomEmoji.class, ArgumentTypes::emote);
        register(Bot.class, (raw, inv) -> DiSky5.getBots().byName(raw));
        register(Channel.class, ArgumentTypes::channel);
    }

    private ArgumentTypes() {}

    public static <T> void register(Class<T> type, BiFunction<String, CommandInvocation, T> resolver) {
        ENTRIES.add(new Entry(type, (BiFunction<String, CommandInvocation, Object>) resolver));
    }

    public static @Nullable ArgumentType<?> of(ClassInfo<?> info) {
        Class<?> c = info.getC();
        for (Entry entry : ENTRIES)
            if (entry.type().isAssignableFrom(c) || c.isAssignableFrom(entry.type()))
                return entityType(info, c, entry);
        Parser<?> parser = info.getParser();
        if (parser != null && parser.canParse(ParseContext.COMMAND))
            return skriptType(info);
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static ArgumentType<?> entityType(ClassInfo<?> info, Class<?> c, Entry entry) {
        return new ArgumentType() {
            @Override public String name() { return info.getCodeName(); }
            @Override public Class type() { return c; }
            @Override public @Nullable Object resolve(String raw, CommandInvocation invocation) {
                Object value = entry.resolver().apply(raw, invocation);
                return c.isInstance(value) ? value : null;
            }
        };
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static ArgumentType<?> skriptType(ClassInfo<?> info) {
        return new ArgumentType() {
            @Override public String name() { return info.getCodeName(); }
            @Override public Class type() { return info.getC(); }
            @Override public @Nullable Object resolve(String raw, CommandInvocation invocation) {
                return Classes.parse(raw, info.getC(), ParseContext.COMMAND);
            }
        };
    }

    public static @Nullable Long snowflake(String raw) {
        Matcher m = SNOWFLAKE.matcher(raw.trim());
        if (!m.matches()) return null;
        try {
            return Long.parseLong(m.group(1));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static @Nullable User user(String raw, CommandInvocation inv) {
        Long id = snowflake(raw);
        JDA jda = inv.getJDA();
        if (id != null) {
            User cached = jda.getUserById(id);
            return cached != null ? cached : jda.retrieveUserById(id).complete();
        }
        Member member = memberByName(raw, inv.guild());
        return member == null ? null : member.getUser();
    }

    private static @Nullable Member member(String raw, CommandInvocation inv) {
        Guild guild = inv.guild();
        if (guild == null) return null;
        Long id = snowflake(raw);
        if (id != null) {
            Member cached = guild.getMemberById(id);
            return cached != null ? cached : guild.retrieveMemberById(id).complete();
        }
        return memberByName(raw, guild);
    }

    private static @Nullable Member memberByName(String raw, @Nullable Guild guild) {
        if (guild == null) return null;
        String name = raw.startsWith("@") ? raw.substring(1) : raw;
        List<Member> found = guild.getMembersByEffectiveName(name, true);
        if (found.isEmpty()) found = guild.getMembersByName(name, true);
        return found.isEmpty() ? null : found.get(0);
    }

    private static @Nullable Role role(String raw, CommandInvocation inv) {
        Guild guild = inv.guild();
        if (guild == null) return null;
        Long id = snowflake(raw);
        if (id != null) return guild.getRoleById(id);
        String name = raw.startsWith("@") ? raw.substring(1) : raw;
        List<Role> found = guild.getRolesByName(name, true);
        return found.isEmpty() ? null : found.get(0);
    }

    private static @Nullable Guild guild(String raw, CommandInvocation inv) {
        Long id = snowflake(raw);
        if (id != null) return inv.getJDA().getGuildById(id);
        List<Guild> found = inv.getJDA().getGuildsByName(raw, true);
        return found.isEmpty() ? null : found.get(0);
    }

    private static @Nullable Channel channel(String raw, CommandInvocation inv) {
        Long id = snowflake(raw);
        JDA jda = inv.getJDA();
        if (id != null) {
            Channel channel = jda.getChannelById(Channel.class, id);
            return channel != null ? channel : jda.getPrivateChannelById(id);
        }
        Guild guild = inv.guild();
        if (guild == null) return null;
        String name = raw.startsWith("#") ? raw.substring(1) : raw;
        for (GuildChannel channel : guild.getChannels())
            if (channel.getName().equalsIgnoreCase(name))
                return channel;
        return null;
    }

    private static @Nullable Message message(String raw, CommandInvocation inv) {
        Matcher link = Message.JUMP_URL_PATTERN.matcher(raw.trim());
        if (link.matches()) {
            Channel channel = inv.getJDA().getChannelById(Channel.class, link.group("channel"));
            if (channel instanceof net.dv8tion.jda.api.entities.channel.middleman.MessageChannel mc)
                return mc.retrieveMessageById(link.group("message")).complete();
            return null;
        }
        Long id = snowflake(raw);
        return id == null ? null : inv.channel().retrieveMessageById(id).complete();
    }

    private static @Nullable CustomEmoji emote(String raw, CommandInvocation inv) {
        try {
            EmojiUnion emoji = Emoji.fromFormatted(raw.trim());
            return emoji.getType() == Emoji.Type.CUSTOM ? emoji.asCustom() : null;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
