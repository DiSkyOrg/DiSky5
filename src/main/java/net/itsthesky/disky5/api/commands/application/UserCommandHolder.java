package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public final class UserCommandHolder extends ApplicationCommandHolder {

    private final CommandData data;

    public UserCommandHolder(CommandData data) {
        if (data.getType() != Command.Type.USER)
            throw new IllegalArgumentException("not a user command: " + data.getType());
        this.data = data;
    }

    public static UserCommandHolder from(Command command) {
        UserCommandHolder holder = new UserCommandHolder(CommandData.fromCommand(command));
        holder.id(command.getIdLong());
        return holder;
    }

    @Override
    public CommandData data() { return data; }
}
