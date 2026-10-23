package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public final class MessageCommandHolder extends ApplicationCommandHolder {

    private final CommandData data;

    public MessageCommandHolder(CommandData data) {
        if (data.getType() != Command.Type.MESSAGE)
            throw new IllegalArgumentException("not a message command: " + data.getType());
        this.data = data;
    }

    public static MessageCommandHolder from(Command command) {
        MessageCommandHolder holder = new MessageCommandHolder(CommandData.fromCommand(command));
        holder.id(command.getIdLong());
        return holder;
    }

    @Override
    public CommandData data() { return data; }
}
