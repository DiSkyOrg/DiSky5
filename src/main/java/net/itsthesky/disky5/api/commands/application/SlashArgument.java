package net.itsthesky.disky5.api.commands.application;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.Trigger;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.itsthesky.disky5.api.commands.InvocationArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SlashArgument implements InvocationArgument {

    private final String name;
    private final SlashArgumentType<?> type;
    private final ClassInfo<?> classInfo;
    private final boolean required;
    private final int index;
    private String description = "No description provided";
    private final Map<DiscordLocale, String> descriptionLocalizations = new LinkedHashMap<>();
    private final List<Command.Choice> choices = new ArrayList<>();
    private @Nullable Trigger completion;

    public SlashArgument(String name, SlashArgumentType<?> type, ClassInfo<?> classInfo, boolean required, int index) {
        this.name = name;
        this.type = type;
        this.classInfo = classInfo;
        this.required = required;
        this.index = index;
    }

    @Override
    public @NotNull String name() { return name; }

    @Override
    public int index() { return index; }

    @Override
    public Class<?> valueType() { return type.type(); }

    @Override
    public boolean single() { return true; }

    public SlashArgumentType<?> type() { return type; }
    public ClassInfo<?> classInfo() { return classInfo; }
    public boolean required() { return required; }
    public String description() { return description; }
    public Map<DiscordLocale, String> descriptionLocalizations() { return descriptionLocalizations; }
    public List<Command.Choice> choices() { return choices; }
    public @Nullable Trigger completion() { return completion; }

    public void description(String description) { this.description = description; }
    public void completion(@Nullable Trigger completion) { this.completion = completion; }

    public OptionData toOptionData() {
        OptionData data = new OptionData(type.optionType(), name, description, required);
        if (!descriptionLocalizations.isEmpty()) data.setDescriptionLocalizations(descriptionLocalizations);
        if (!choices.isEmpty()) {
            data.addChoices(choices);
            if (type.channelTypes() != null) data.setChannelTypes(type.channelTypes());
        } else if (completion != null) {
            data.setAutoComplete(true);
            if (type.channelTypes() != null) data.setChannelTypes(type.channelTypes());
        } else {
            type.applyTo(data);
        }
        return data;
    }

    public Object @NotNull [] resolve(@Nullable OptionMapping mapping) {
        Object value = type.convert(mapping);
        return value == null ? new Object[0] : new Object[]{value};
    }

    @Override
    public String toString() {
        String inner = name + ": " + classInfo.getCodeName();
        return required ? "<" + inner + ">" : "[<" + inner + ">]";
    }
}
