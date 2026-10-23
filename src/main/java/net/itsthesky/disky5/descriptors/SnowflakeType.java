package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Date;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;

@SkriptType(name = "snowflake", jda = ISnowflake.class)
public final class SnowflakeType {

    @Property("discord id")
    @Documentation(
            name = "Discord ID",
            description = {
                "The unique Discord snowflake id of any entity (member, user, guild, role, channel, "
                    + "message, emoji...). Always a string, since ids exceed Skript's number range."
            },
            examples = "reply with \"Your id is %discord id of event-member%\"",
            since = "5.0.0-a1"
    )
    public static String discordId(ISnowflake snowflake) {
        return snowflake.getId();
    }

    @Property("creation date")
    @Documentation(
            name = "Creation Date",
            description = "The date at which any Discord entity was created, derived from its snowflake id.",
            examples = "reply with creation date of event-channel",
            since = "5.0.0-a1"
    )
    public static Date creationDate(ISnowflake snowflake) {
        return Conversions.date(snowflake.getTimeCreated());
    }
}
