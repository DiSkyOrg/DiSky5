package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.itsthesky.disky5.core.Bot;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record CommandScope(@Nullable JDA jda, @Nullable Guild guild) {

    public static CommandScope global(Bot bot) {
        JDA shard = bot.anyShard();
        if (shard == null)
            throw new IllegalStateException("the bot '" + bot.getName() + "' is not connected, it cannot manage commands yet");
        return new CommandScope(shard, null);
    }

    public static CommandScope guild(Guild guild) {
        return new CommandScope(null, guild);
    }

    public static CommandScope of(@Nullable Bot bot, @Nullable Guild guild) {
        if (guild != null) return guild(guild);
        if (bot != null) return global(bot);
        throw new IllegalArgumentException("no bot or guild to manage commands in (the variable is not set)");
    }

    public static CommandScope of(@Nullable Object target) {
        if (target instanceof Guild guild) return guild(guild);
        if (target instanceof Bot bot) return global(bot);
        throw new IllegalArgumentException("expected a bot (global commands) or a guild, got " + target);
    }

    public boolean isGlobal() { return guild == null; }

    public CommandListUpdateAction updateCommands() {
        return guild != null ? guild.updateCommands() : jda.updateCommands();
    }

    public RestAction<Command> upsert(CommandData data) {
        return guild != null ? guild.upsertCommand(data) : jda.upsertCommand(data);
    }

    public RestAction<List<Command>> retrieve() {
        return guild != null ? guild.retrieveCommands(true) : jda.retrieveCommands(true);
    }

    public RestAction<Void> delete(long id) {
        return guild != null ? guild.deleteCommandById(id) : jda.deleteCommandById(id);
    }

    @Override
    public String toString() {
        return guild != null ? "guild " + guild.getName() : "global";
    }
}
