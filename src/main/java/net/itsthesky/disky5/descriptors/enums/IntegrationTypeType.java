package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.interactions.IntegrationType;
import net.itsthesky.disky5.annotations.EnumAliases;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.Map;

@SkriptEnum(name = "integrationtype", jda = IntegrationType.class)
@Documentation(
    name = "Integration Type",
    description = "How an application command reaches its users: installed on a guild, or installed on "
        + "a user's account (usable everywhere that user goes).",
    examples = "set integration types of {_cmd} to guild install and user install",
    since = "5.0.0-a2")
public final class IntegrationTypeType {

    @EnumAliases
    public static Map<IntegrationType, String[]> aliases() {
        return Map.of(
            IntegrationType.GUILD_INSTALL, new String[]{"server install"},
            IntegrationType.USER_INSTALL, new String[]{"user"}
        );
    }
}
