package net.itsthesky.disky5.descriptors.commands;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.commands.application.SlashOptionHolder;
import net.itsthesky.disky5.api.commands.application.SubcommandHolder;

import java.util.ArrayList;
import java.util.List;

@SkriptType(name = "subcommand", jda = SubcommandHolder.class)
@Documentation(
    name = "Subcommand",
    description = {"A '/root sub' entry of a slash command, with its own description and options. "
        + "Add it to 'subcommands of' a slash command or a subcommand group."},
    since = "5.0.0-a2")
public final class SubcommandType {

    private SubcommandType() {}

    @New("[a] new sub[ ]command (named|with [the] name) %string% with [the] desc[ription] %string%")
    @Documentation(
        name = "New Subcommand",
        description = "Creates a subcommand. Same naming rules as a slash command.",
        examples = {"set {_ban} to a new subcommand named \"ban\" with description \"Ban someone\"",
            "add {_ban} to subcommands of {_mod}"},
        since = "5.0.0-a2")
    public static SubcommandHolder newSubcommand(String name, String description) {
        return new SubcommandHolder(new SubcommandData(name, description));
    }

    @Property(value = "name", shared = true)
    public static String name(SubcommandHolder subcommand) {
        return subcommand.data().getName();
    }

    @Property.Setter(value = "name", shared = true)
    public static void setName(SubcommandHolder subcommand, String name) {
        subcommand.data().setName(name);
    }

    @Property("description")
    public static String description(SubcommandHolder subcommand) {
        return subcommand.data().getDescription();
    }

    @Property.Setter("description")
    public static void setDescription(SubcommandHolder subcommand, String description) {
        subcommand.data().setDescription(description);
    }

    @Property("options")
    public static List<SlashOptionHolder> options(SubcommandHolder subcommand) {
        List<SlashOptionHolder> options = new ArrayList<>();
        for (OptionData option : subcommand.data().getOptions())
            options.add(SlashOptionHolder.from(option));
        return options;
    }

    @Property.Setter("options")
    public static void setOptions(SubcommandHolder subcommand, SlashOptionHolder[] options) {
        subcommand.data().removeOptions(o -> true);
        for (SlashOptionHolder option : options)
            subcommand.data().addOptions(option.build());
    }

    @Property.Adder("options")
    public static void addOption(SubcommandHolder subcommand, SlashOptionHolder option) {
        subcommand.data().addOptions(option.build());
    }

    @Property.Remover("options")
    public static void removeOption(SubcommandHolder subcommand, SlashOptionHolder option) {
        subcommand.data().removeOptionByName(option.data().getName());
    }
}
