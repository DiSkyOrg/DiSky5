package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.List;

public final class SlashCommandHolder extends ApplicationCommandHolder {

    private final SlashCommandData data;
    private final List<SlashCommandModel> models = new ArrayList<>();

    public SlashCommandHolder(SlashCommandData data) {
        this.data = data;
    }

    public static SlashCommandHolder from(Command command) {
        SlashCommandHolder holder = new SlashCommandHolder(SlashCommandData.fromCommand(command));
        holder.id(command.getIdLong());
        return holder;
    }

    @Override
    public SlashCommandData data() { return data; }

    public List<SlashCommandModel> models() { return models; }
}
