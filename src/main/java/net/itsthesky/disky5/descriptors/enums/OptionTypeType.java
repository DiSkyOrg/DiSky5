package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.itsthesky.disky5.annotations.EnumAliases;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.Map;

@SkriptEnum(name = "optiontype", jda = OptionType.class)
@Documentation(
    name = "Option Type",
    description = "The Discord type of a slash command option: string, integer, number, boolean, user, "
        + "channel, role, mentionable or attachment.",
    examples = "set {_opt} to a new string option named \"reason\" with description \"Why\"",
    since = "5.0.0-a2")
public final class OptionTypeType {

    @EnumAliases
    public static Map<OptionType, String[]> aliases() {
        return Map.of(
            OptionType.STRING, new String[]{"text"},
            OptionType.INTEGER, new String[]{"int"},
            OptionType.BOOLEAN, new String[]{"bool"},
            OptionType.NUMBER, new String[]{"decimal"}
        );
    }
}
