package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.OnlineStatus;
import net.itsthesky.disky5.annotations.EnumAliases;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.Map;

@SkriptEnum(name = "onlinestatus", jda = OnlineStatus.class)
@Documentation(
    name = "Online Status",
    description = "A member's or bot's presence: online, idle, dnd (do not disturb), invisible or offline.",
    examples = "set online status of event-bot to dnd",
    since = "5.0.0-a1")
public final class OnlineStatusType {

    // "do not disturb" contains the reserved word "not", which breaks a Skript comparison
    // (`X is do not disturb`); "dnd" avoids it and matches Discord's own name.
    @EnumAliases
    public static Map<OnlineStatus, String[]> aliases() {
        return Map.of(OnlineStatus.DO_NOT_DISTURB, new String[]{"dnd"});
    }
}
