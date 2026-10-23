package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "ban", jda = Guild.Ban.class)
public final class BanType {

    @Property("banned user")
    @Documentation(
            name = "Ban User",
            description = "The user that this ban applies to.",
            examples = "reply with banned user of {_ban}",
            since = "5.0.0-a1"
    )
    public static User bannedUser(Guild.Ban b) {
        return b.getUser();
    }

    @Property("ban reason")
    @Documentation(
            name = "Ban Reason",
            description = "The reason of the ban, or null if none was provided.",
            examples = "reply with ban reason of {_ban}",
            since = "5.0.0-a1"
    )
    public static @Nullable String banReason(Guild.Ban b) {
        return b.getReason();
    }
}
