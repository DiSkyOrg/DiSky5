package net.itsthesky.disky5.events.user;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "User Avatar Update Event",
        patterns = {"[discord] user avatar (change|update)"},
        jda = UserUpdateAvatarEvent.class
)
@Documentation(
        name = "User Avatar Update Event",
        description = {
                "Fired when a user changes their avatar.",
                "This event provides access to both the old and new avatar URLs.",
                "It can be used for monitoring profile changes or updating cached user information."
        },
        examples = {
                "on user avatar change:",
                "\tbroadcast \"%event-user% changed their avatar from %previous avatar url% to %current avatar url%\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserAvatarUpdate {

    @EventValue
    public static User user(UserUpdateAvatarEvent event) {
        return event.getUser();
    }

    @EventExpression("avatar [url]")
    public static @Nullable String avatarUrl(UserUpdateAvatarEvent event) {
        return event.getNewAvatarUrl();
    }

    @EventExpression("(old|past|previous) avatar [url]")
    public static @Nullable String oldAvatarUrl(UserUpdateAvatarEvent event) {
        return event.getOldAvatarUrl();
    }
}
