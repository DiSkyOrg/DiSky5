package net.itsthesky.disky5.descriptors.commands;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.commands.application.SlashChoiceHolder;
import net.itsthesky.disky5.api.commands.application.SlashOptionHolder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@SkriptType(name = "slashoption", jda = SlashOptionHolder.class)
@Documentation(
    name = "Slash Option",
    description = {"One argument of a slash command or subcommand: a Discord type, a name, a description, "
        + "and optionally choices, autocomplete, bounds or channel types.",
        "Read its value in 'on slash command' with 'slash option \"name\"'."},
    since = "5.0.0-a2")
public final class SlashOptionType {

    private SlashOptionType() {}

    @New("[a] new %optiontype% [slash] option (named|with [the] name) %string% with [the] desc[ription] %string%")
    @Documentation(
        name = "New Slash Option",
        description = {"Creates an option of the given Discord type (string, integer, number, boolean, user, channel, "
            + "role, mentionable, attachment). It is optional and without autocomplete until you say otherwise.",
            "For an option typed by a Skript type (a member, a colour, an enum with its choices), see 'a new typed ... option'."},
        examples = {"set {_opt} to a new string option named \"reason\" with description \"Why\"",
            "set required of {_opt} to true",
            "add {_opt} to options of {_cmd}"},
        since = "5.0.0-a2")
    public static SlashOptionHolder newSlashOption(OptionType type, String name, String description) {
        if (type == OptionType.SUB_COMMAND || type == OptionType.SUB_COMMAND_GROUP || type == OptionType.UNKNOWN)
            throw new IllegalArgumentException("'" + type.name().toLowerCase() + "' is not an option type; use a subcommand or a subcommand group instead");
        return new SlashOptionHolder(new OptionData(type, name, description));
    }

    @Property(value = "name", shared = true)
    public static String name(SlashOptionHolder option) {
        return option.data().getName();
    }

    @Property.Setter(value = "name", shared = true)
    public static void setName(SlashOptionHolder option, String name) {
        option.data().setName(name);
    }

    @Property("description")
    public static String description(SlashOptionHolder option) {
        return option.data().getDescription();
    }

    @Property.Setter("description")
    public static void setDescription(SlashOptionHolder option, String description) {
        option.data().setDescription(description);
    }

    @Property("option type")
    @Documentation(
        name = "Slash Option Type",
        description = "The Discord type of the option.",
        examples = {"if option type of {_opt} is string:"},
        since = "5.0.0-a2")
    public static OptionType optionType(SlashOptionHolder option) {
        return option.data().getType();
    }

    @Property("required")
    @Documentation(
        name = "Slash Option Required",
        description = "Whether the user has to fill this option. Required options must come before optional ones.",
        examples = {"set required of {_opt} to true"},
        since = "5.0.0-a2")
    public static Boolean required(SlashOptionHolder option) {
        return option.data().isRequired();
    }

    @Property.Setter("required")
    public static void setRequired(SlashOptionHolder option, Boolean required) {
        option.data().setRequired(required);
    }

    @Property("auto complete")
    @Documentation(
        name = "Slash Option Autocomplete",
        description = {"Whether Discord asks the bot for suggestions while the user types ('on slash completion' + "
            + "'return choices'). Only string, integer and number options; incompatible with fixed choices."},
        examples = {"set auto complete of {_opt} to true"},
        since = "5.0.0-a2")
    public static Boolean autoComplete(SlashOptionHolder option) {
        return option.data().isAutoComplete();
    }

    @Property.Setter("auto complete")
    public static void setAutoComplete(SlashOptionHolder option, Boolean autoComplete) {
        if (autoComplete && !option.data().getType().canSupportChoices())
            throw new IllegalArgumentException("a " + option.data().getType().name().toLowerCase() + " option cannot have autocomplete");
        option.data().setAutoComplete(autoComplete);
    }

    @Property("choices")
    @Documentation(
        name = "Slash Option Choices",
        description = {"The fixed choices (up to 25) the user picks from. Only string, integer and number options; "
            + "a choice's value must match the option type."},
        examples = {"add a new choice named \"Red\" with value \"red\" to choices of {_opt}",
            "set choices of {_opt} to {_c1} and {_c2}"},
        since = "5.0.0-a2")
    public static List<SlashChoiceHolder> choices(SlashOptionHolder option) {
        List<SlashChoiceHolder> choices = new ArrayList<>();
        for (Command.Choice choice : option.data().getChoices())
            choices.add(SlashChoiceHolder.from(choice));
        return choices;
    }

    @Property.Setter("choices")
    public static void setChoices(SlashOptionHolder option, SlashChoiceHolder[] choices) {
        List<Command.Choice> built = new ArrayList<>();
        for (SlashChoiceHolder choice : choices)
            built.add(choice.build(option.data().getType()));
        option.choices(built);
    }

    @Property.Adder("choices")
    public static void addChoice(SlashOptionHolder option, SlashChoiceHolder choice) {
        option.data().addChoices(choice.build(option.data().getType()));
    }

    @Property("min value")
    @Documentation(
        name = "Slash Option Minimum Value",
        description = "The lowest value accepted by an integer or number option.",
        examples = {"set min value of {_opt} to 1"},
        since = "5.0.0-a2")
    public static Number minValue(SlashOptionHolder option) {
        return option.data().getMinValue();
    }

    @Property.Setter("min value")
    public static void setMinValue(SlashOptionHolder option, Number value) {
        SlashOptionHolder.applyMin(option.data(), value);
    }

    @Property("max value")
    @Documentation(
        name = "Slash Option Maximum Value",
        description = "The highest value accepted by an integer or number option.",
        examples = {"set max value of {_opt} to 100"},
        since = "5.0.0-a2")
    public static Number maxValue(SlashOptionHolder option) {
        return option.data().getMaxValue();
    }

    @Property.Setter("max value")
    public static void setMaxValue(SlashOptionHolder option, Number value) {
        SlashOptionHolder.applyMax(option.data(), value);
    }

    @Property("min length")
    @Documentation(
        name = "Slash Option Minimum Length",
        description = "The shortest text accepted by a string option (0 to 6000).",
        examples = {"set min length of {_opt} to 3"},
        since = "5.0.0-a2")
    public static Number minLength(SlashOptionHolder option) {
        return option.data().getMinLength();
    }

    @Property.Setter("min length")
    public static void setMinLength(SlashOptionHolder option, Number length) {
        option.data().setMinLength(length.intValue());
    }

    @Property("max length")
    @Documentation(
        name = "Slash Option Maximum Length",
        description = "The longest text accepted by a string option (1 to 6000).",
        examples = {"set max length of {_opt} to 200"},
        since = "5.0.0-a2")
    public static Number maxLength(SlashOptionHolder option) {
        return option.data().getMaxLength();
    }

    @Property.Setter("max length")
    public static void setMaxLength(SlashOptionHolder option, Number length) {
        option.data().setMaxLength(length.intValue());
    }

    @Property("channel types")
    @Documentation(
        name = "Slash Option Channel Types",
        description = "The kinds of channel a channel option accepts (text, voice, category, ...). Empty means any.",
        examples = {"set channel types of {_opt} to text and voice"},
        since = "5.0.0-a2")
    public static List<ChannelType> channelTypes(SlashOptionHolder option) {
        return new ArrayList<>(option.data().getChannelTypes());
    }

    @Property.Setter("channel types")
    public static void setChannelTypes(SlashOptionHolder option, ChannelType[] types) {
        option.data().setChannelTypes(types);
    }

    @Property.Adder("channel types")
    public static void addChannelType(SlashOptionHolder option, ChannelType type) {
        Set<ChannelType> current = EnumSet.noneOf(ChannelType.class);
        current.addAll(option.data().getChannelTypes());
        current.add(type);
        option.data().setChannelTypes(current);
    }

    @Property.Remover("channel types")
    public static void removeChannelType(SlashOptionHolder option, ChannelType type) {
        Set<ChannelType> current = EnumSet.noneOf(ChannelType.class);
        current.addAll(option.data().getChannelTypes());
        current.remove(type);
        option.data().setChannelTypes(current);
    }
}
