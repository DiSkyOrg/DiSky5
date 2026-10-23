package net.itsthesky.disky5.events.user;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "User Name Update Event",
        patterns = {"[discord] user name (change|update)"},
        jda = UserUpdateNameEvent.class
)
@Documentation(
        name = "User Name Update Event",
        description = {
                "Fired when a user changes their username (not nickname).",
                "This event provides access to both the old and new usernames.",
                "It can be used for monitoring identity changes or updating user databases."
        },
        examples = {
                "on user name change:",
                "\tbroadcast \"User changed their name from %previous name% to %current name%\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserNameUpdate {

    @EventValue
    public static User user(UserUpdateNameEvent event) {
        return event.getUser();
    }

    @EventExpression("name")
    public static @Nullable String name(UserUpdateNameEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) name")
    public static @Nullable String oldName(UserUpdateNameEvent event) {
        return event.getOldValue();
    }
}
