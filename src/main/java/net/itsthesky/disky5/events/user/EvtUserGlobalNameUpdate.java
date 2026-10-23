package net.itsthesky.disky5.events.user;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.UserUpdateGlobalNameEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "User Global Name Update Event",
        patterns = {"[discord] user global name (change|update)"},
        jda = UserUpdateGlobalNameEvent.class
)
@Documentation(
        name = "User Global Name Update Event",
        description = {
                "Fired when a user changes their global display name.",
                "This event provides access to both the old and new global names.",
                "With Discord's new username system, this tracks the display name shown across all servers."
        },
        examples = {
                "on user global name change:",
                "\tbroadcast \"%event-user% changed their display name from '%previous global name%' to '%current global name%'\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserGlobalNameUpdate {

    @EventValue
    public static User user(UserUpdateGlobalNameEvent event) {
        return event.getUser();
    }

    @EventExpression("global name")
    public static @Nullable String globalName(UserUpdateGlobalNameEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) global name")
    public static @Nullable String oldGlobalName(UserUpdateGlobalNameEvent event) {
        return event.getOldValue();
    }
}
