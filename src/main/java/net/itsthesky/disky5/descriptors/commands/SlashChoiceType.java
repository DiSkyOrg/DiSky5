package net.itsthesky.disky5.descriptors.commands;

import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.commands.application.SlashChoiceHolder;

@SkriptType(name = "slashchoice", jda = SlashChoiceHolder.class)
@Documentation(
    name = "Slash Choice",
    description = {"One fixed choice of a slash option: the name the user sees and the value the script gets. "
        + "Also what 'return choices' sends back from 'on slash completion'."},
    since = "5.0.0-a2")
public final class SlashChoiceType {

    private SlashChoiceType() {}

    @New("[a] new [option] choice (named|with [the] name) %string% with [the] value %string/number%")
    @Documentation(
        name = "New Slash Choice",
        description = "Creates a choice. Give a text value for a string option, a number for an integer or number option.",
        examples = {"add a new choice named \"Red\" with value \"red\" to choices of {_opt}",
            "return choices (a new choice named \"One\" with value 1)"},
        since = "5.0.0-a2")
    public static SlashChoiceHolder newChoice(String name, Object value) {
        return new SlashChoiceHolder(name, value);
    }

    @Property(value = "name", shared = true)
    public static String name(SlashChoiceHolder choice) {
        return choice.name();
    }

    @Property.Setter(value = "name", shared = true)
    public static void setName(SlashChoiceHolder choice, String name) {
        choice.name(name);
    }

    @Property("choice value")
    @Documentation(
        name = "Slash Choice Value",
        description = "The value sent to the bot when this choice is picked (a text or a number).",
        examples = {"set choice value of {_c} to \"blue\""},
        since = "5.0.0-a2")
    public static Object choiceValue(SlashChoiceHolder choice) {
        return choice.value();
    }

    @Property.Setter("choice value")
    public static void setChoiceValue(SlashChoiceHolder choice, Object value) {
        choice.value(value);
    }
}
