package net.itsthesky.disky5.events.user;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "User Discriminator Update Event",
        patterns = {"[discord] user discriminator (change|update)"},
        jda = UserUpdateDiscriminatorEvent.class
)
@Documentation(
        name = "User Discriminator Update Event",
        description = {
                "Fired when a user changes their discriminator.",
                "The discriminator is the four-digit number following a username (e.g., #1234).",
                "This event provides access to both the old and new discriminator values.",
                "Note: With Discord's migration to the new username system, this event may become less relevant."
        },
        examples = {
                "on user discriminator change:",
                "\tbroadcast \"%event-user% changed their discriminator from %previous discriminator% to %current discriminator%\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserDiscriminatorUpdate {

    @EventValue
    public static User user(UserUpdateDiscriminatorEvent event) {
        return event.getUser();
    }

    @EventExpression("discriminator")
    public static @Nullable String discriminator(UserUpdateDiscriminatorEvent event) {
        return event.getNewValue();
    }

    @EventExpression("(old|past|previous) discriminator")
    public static @Nullable String oldDiscriminator(UserUpdateDiscriminatorEvent event) {
        return event.getOldValue();
    }
}
