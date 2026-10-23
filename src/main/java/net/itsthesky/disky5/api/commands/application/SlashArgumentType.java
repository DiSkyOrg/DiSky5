package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public record SlashArgumentType<T>(OptionType optionType, Class<T> type,
                                   Function<OptionMapping, @Nullable T> converter,
                                   @Nullable Supplier<List<Command.Choice>> choices,
                                   @Nullable Function<String, List<Command.Choice>> completer,
                                   @Nullable Set<ChannelType> channelTypes) {

    public static <T> SlashArgumentType<T> of(OptionType optionType, Class<T> type, Function<OptionMapping, @Nullable T> converter) {
        return new SlashArgumentType<>(optionType, type, converter, null, null, null);
    }

    public SlashArgumentType<T> withChoices(Supplier<List<Command.Choice>> choices) {
        return new SlashArgumentType<>(optionType, type, converter, choices, null, channelTypes);
    }

    public SlashArgumentType<T> withCompleter(Function<String, List<Command.Choice>> completer) {
        return new SlashArgumentType<>(optionType, type, converter, null, completer, channelTypes);
    }

    public SlashArgumentType<T> withChannelTypes(Set<ChannelType> channelTypes) {
        return new SlashArgumentType<>(optionType, type, converter, choices, completer, channelTypes);
    }

    public @Nullable T convert(@Nullable OptionMapping mapping) {
        return mapping == null ? null : converter.apply(mapping);
    }

    public void applyTo(OptionData data) {
        if (choices != null) data.addChoices(choices.get());
        if (completer != null) data.setAutoComplete(true);
        if (channelTypes != null && !channelTypes.isEmpty()) data.setChannelTypes(channelTypes);
    }
}
