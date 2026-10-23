package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.Nullable;

public final class ApplicationCommandHolders {

    private ApplicationCommandHolders() {}

    public static @Nullable ApplicationCommandHolder from(Command command) {
        return switch (command.getType()) {
            case SLASH -> SlashCommandHolder.from(command);
            case USER -> UserCommandHolder.from(command);
            case MESSAGE -> MessageCommandHolder.from(command);
            default -> null;
        };
    }

    public static @Nullable ApplicationCommandHolder from(CommandData data) {
        return switch (data.getType()) {
            case SLASH -> new SlashCommandHolder((SlashCommandData) data);
            case USER -> new UserCommandHolder(data);
            case MESSAGE -> new MessageCommandHolder(data);
            default -> null;
        };
    }
}
