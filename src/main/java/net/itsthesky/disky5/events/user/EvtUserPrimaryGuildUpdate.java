package net.itsthesky.disky5.events.user;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.UserUpdatePrimaryGuildEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "User Primary Guild Update Event",
        patterns = {"[discord] user primary guild (change|update)"},
        jda = UserUpdatePrimaryGuildEvent.class
)
@Documentation(
        name = "User Primary Guild Update Event",
        description = {
                "Fired when a user changes their primary guild.",
                "This event provides access to both the old and new primary guilds.",
                "The primary guild is the server that appears as the user's main server on their profile."
        },
        examples = {
                "on user primary guild change:",
                "\tbroadcast \"%event-user% changed their primary guild from '%previous primary guild id%' to '%primary guild id%' (new tag: %new user tag%)\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserPrimaryGuildUpdate {

    @EventValue
    public static User user(UserUpdatePrimaryGuildEvent event) {
        return event.getUser();
    }

    @EventExpression("primary guild id")
    public static @Nullable String primaryGuildId(UserUpdatePrimaryGuildEvent event) {
        return event.getNewPrimaryGuild() == null ? null : event.getNewPrimaryGuild().getId();
    }

    @EventExpression("(old|past|previous) primary guild id")
    public static @Nullable String oldPrimaryGuildId(UserUpdatePrimaryGuildEvent event) {
        return event.getOldPrimaryGuild() == null ? null : event.getOldPrimaryGuild().getId();
    }

    @EventExpression("user tag")
    public static @Nullable String userTag(UserUpdatePrimaryGuildEvent event) {
        return event.getNewPrimaryGuild() == null ? null : event.getNewPrimaryGuild().getTag();
    }

    @EventExpression("(old|past|previous) user tag")
    public static @Nullable String oldUserTag(UserUpdatePrimaryGuildEvent event) {
        return event.getOldPrimaryGuild() == null ? null : event.getOldPrimaryGuild().getTag();
    }
}
