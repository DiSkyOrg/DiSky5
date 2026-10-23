package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.itsthesky.disky5.annotations.EnumAliases;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.Map;

@SkriptEnum(name = "interactioncontexttype", jda = InteractionContextType.class)
@Documentation(
    name = "Interaction Context",
    description = "Where an application command can be used: in a guild, in the bot's DMs, or in a "
        + "group DM / private channel.",
    examples = "set contexts of {_cmd} to guild and bot dm",
    since = "5.0.0-a2")
public final class InteractionContextTypeType {

    @EnumAliases
    public static Map<InteractionContextType, String[]> aliases() {
        return Map.of(
            InteractionContextType.GUILD, new String[]{"server"},
            InteractionContextType.BOT_DM, new String[]{"dm", "private message"},
            InteractionContextType.PRIVATE_CHANNEL, new String[]{"group dm"}
        );
    }
}
