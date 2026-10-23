package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public final class SlashOptionHolder {

    private OptionData data;

    public SlashOptionHolder(OptionData data) {
        this.data = data;
    }

    public static SlashOptionHolder from(OptionData data) {
        return new SlashOptionHolder(data);
    }

    public OptionData data() { return data; }

    public OptionType type() { return data.getType(); }

    public OptionData build() { return data; }

    // OptionData has no choice removal, so replacing the choices means rebuilding the option.
    public void choices(List<Command.Choice> choices) {
        OptionData fresh = new OptionData(data.getType(), data.getName(), data.getDescription(), data.isRequired(), data.isAutoComplete());
        fresh.setNameLocalizations(data.getNameLocalizations().toMap());
        fresh.setDescriptionLocalizations(data.getDescriptionLocalizations().toMap());
        if (!data.getChannelTypes().isEmpty()) fresh.setChannelTypes(data.getChannelTypes());
        if (data.getMinValue() != null) applyMin(fresh, data.getMinValue());
        if (data.getMaxValue() != null) applyMax(fresh, data.getMaxValue());
        if (data.getMinLength() != null) fresh.setMinLength(data.getMinLength());
        if (data.getMaxLength() != null) fresh.setMaxLength(data.getMaxLength());
        if (!choices.isEmpty()) fresh.addChoices(choices);
        this.data = fresh;
    }

    public static void applyMin(OptionData data, Number value) {
        if (data.getType() == OptionType.INTEGER) data.setMinValue(value.longValue());
        else if (data.getType() == OptionType.NUMBER) data.setMinValue(value.doubleValue());
        else throw new IllegalArgumentException("only integer and number options have a minimum value");
    }

    public static void applyMax(OptionData data, Number value) {
        if (data.getType() == OptionType.INTEGER) data.setMaxValue(value.longValue());
        else if (data.getType() == OptionType.NUMBER) data.setMaxValue(value.doubleValue());
        else throw new IllegalArgumentException("only integer and number options have a maximum value");
    }

    @Override
    public String toString() {
        return data.getType().name().toLowerCase() + " option '" + data.getName() + "'";
    }
}
