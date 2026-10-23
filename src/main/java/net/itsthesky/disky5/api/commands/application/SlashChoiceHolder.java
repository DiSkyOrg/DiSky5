package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SlashChoiceHolder {

    private String name;
    private Object value;
    private final Map<DiscordLocale, String> nameLocalizations = new LinkedHashMap<>();

    public SlashChoiceHolder(String name, Object value) {
        this.name = name;
        this.value = normalize(value);
    }

    public static SlashChoiceHolder from(Command.Choice choice) {
        Object value = switch (choice.getType()) {
            case INTEGER -> choice.getAsLong();
            case NUMBER -> choice.getAsDouble();
            default -> choice.getAsString();
        };
        SlashChoiceHolder holder = new SlashChoiceHolder(choice.getName(), value);
        holder.nameLocalizations.putAll(choice.getNameLocalizations().toMap());
        return holder;
    }

    private static Object normalize(Object value) {
        if (value instanceof Number number)
            return number.doubleValue() == Math.rint(number.doubleValue()) && !(number instanceof Double || number instanceof Float)
                ? number.longValue() : number;
        return String.valueOf(value);
    }

    public String name() { return name; }
    public Object value() { return value; }
    public Map<DiscordLocale, String> nameLocalizations() { return nameLocalizations; }

    public void name(String name) { this.name = name; }
    public void value(Object value) { this.value = normalize(value); }

    public Command.Choice build(OptionType target) {
        Command.Choice choice = switch (target) {
            case INTEGER -> {
                if (!(value instanceof Number number))
                    throw new IllegalArgumentException("the choice '" + name + "' has a text value but its option is an integer option");
                yield new Command.Choice(name, number.longValue());
            }
            case NUMBER -> {
                if (!(value instanceof Number number))
                    throw new IllegalArgumentException("the choice '" + name + "' has a text value but its option is a number option");
                yield new Command.Choice(name, number.doubleValue());
            }
            case STRING -> new Command.Choice(name, String.valueOf(value));
            default -> throw new IllegalArgumentException("a " + target.name().toLowerCase() + " option cannot have choices");
        };
        if (!nameLocalizations.isEmpty()) choice.setNameLocalizations(nameLocalizations);
        return choice;
    }

    @Override
    public String toString() {
        return "choice '" + name + "' = " + value;
    }
}
