package net.itsthesky.disky5.descriptors.commands;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.commands.application.SubcommandGroupHolder;
import net.itsthesky.disky5.api.commands.application.SubcommandHolder;

import java.util.ArrayList;
import java.util.List;

@SkriptType(name = "subcommandgroup", jda = SubcommandGroupHolder.class)
@Documentation(
    name = "Subcommand Group",
    description = {"A '/root group sub' level: a named group of subcommands under a slash command. "
        + "Add subcommands to it, then add it to 'subcommand groups of' the slash command."},
    since = "5.0.0-a2")
public final class SubcommandGroupType {

    private SubcommandGroupType() {}

    @New("[a] new sub[ ]command group (named|with [the] name) %string% with [the] desc[ription] %string%")
    @Documentation(
        name = "New Subcommand Group",
        description = "Creates a subcommand group. Same naming rules as a slash command.",
        examples = {"set {_color} to a new subcommand group named \"color\" with description \"Colour settings\"",
            "add {_set} to subcommands of {_color}",
            "add {_color} to subcommand groups of {_settings}"},
        since = "5.0.0-a2")
    public static SubcommandGroupHolder newSubcommandGroup(String name, String description) {
        return new SubcommandGroupHolder(new SubcommandGroupData(name, description));
    }

    @Property(value = "name", shared = true)
    public static String name(SubcommandGroupHolder group) {
        return group.data().getName();
    }

    @Property.Setter(value = "name", shared = true)
    public static void setName(SubcommandGroupHolder group, String name) {
        group.data().setName(name);
    }

    @Property("description")
    public static String description(SubcommandGroupHolder group) {
        return group.data().getDescription();
    }

    @Property.Setter("description")
    public static void setDescription(SubcommandGroupHolder group, String description) {
        group.data().setDescription(description);
    }

    @Property("subcommands")
    public static List<SubcommandHolder> subcommands(SubcommandGroupHolder group) {
        List<SubcommandHolder> subcommands = new ArrayList<>();
        for (SubcommandData subcommand : group.data().getSubcommands())
            subcommands.add(SubcommandHolder.from(subcommand));
        return subcommands;
    }

    @Property.Setter("subcommands")
    public static void setSubcommands(SubcommandGroupHolder group, SubcommandHolder[] subcommands) {
        group.data().removeSubcommand(s -> true);
        for (SubcommandHolder subcommand : subcommands)
            group.data().addSubcommands(subcommand.build());
    }

    @Property.Adder("subcommands")
    public static void addSubcommand(SubcommandGroupHolder group, SubcommandHolder subcommand) {
        group.data().addSubcommands(subcommand.build());
    }

    @Property.Remover("subcommands")
    public static void removeSubcommand(SubcommandGroupHolder group, SubcommandHolder subcommand) {
        group.data().removeSubcommandByName(subcommand.data().getName());
    }
}
