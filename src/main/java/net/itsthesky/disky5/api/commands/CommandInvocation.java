package net.itsthesky.disky5.api.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.itsthesky.disky5.api.events.CancellationAware;
import net.itsthesky.disky5.core.Bot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CommandInvocation implements GenericEvent, CancellationAware, ArgumentInvocation {

    private final CommandDefinition definition;
    private final MessageReceivedEvent source;
    private final @Nullable Bot bot;

    private String usedPrefix = "";
    private String usedAlias = "";
    private String rawArguments = "";
    private Object[][] values = new Object[0][];
    private @Nullable CommandFailure failure;
    private long remainingCooldown;
    private volatile boolean cancelled;

    public CommandInvocation(CommandDefinition definition, MessageReceivedEvent source, @Nullable Bot bot) {
        this.definition = definition;
        this.source = source;
        this.bot = bot;
    }

    public CommandDefinition definition() { return definition; }
    public MessageReceivedEvent source() { return source; }
    public @Nullable Bot bot() { return bot; }
    public Message message() { return source.getMessage(); }
    public User user() { return source.getAuthor(); }
    public @Nullable Member member() { return source.getMember(); }
    public @Nullable Guild guild() { return source.isFromGuild() ? source.getGuild() : null; }
    public MessageChannelUnion channel() { return source.getChannel(); }

    public String usedPrefix() { return usedPrefix; }
    public String usedAlias() { return usedAlias; }
    public String rawArguments() { return rawArguments; }
    public Object[][] values() { return values; }
    public @Nullable CommandFailure failure() { return failure; }
    public long remainingCooldown() { return remainingCooldown; }

    void matched(String prefix, String alias, String rawArguments) {
        this.usedPrefix = prefix;
        this.usedAlias = alias;
        this.rawArguments = rawArguments;
    }

    void values(Object[][] values) { this.values = values; }

    void fail(CommandFailure failure, long remainingCooldown) {
        this.failure = failure;
        this.remainingCooldown = remainingCooldown;
    }

    @Override
    public Object @NotNull [] value(int index) {
        return index >= 0 && index < values.length && values[index] != null ? values[index] : new Object[0];
    }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    @Override
    public @NotNull JDA getJDA() { return source.getJDA(); }

    @Override
    public long getResponseNumber() { return source.getResponseNumber(); }

    @Override
    public @Nullable DataObject getRawData() { return source.getRawData(); }
}
