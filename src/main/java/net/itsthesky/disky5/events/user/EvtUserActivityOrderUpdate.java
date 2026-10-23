package net.itsthesky.disky5.events.user;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "User Activity Order Update Event",
        patterns = {"[discord] user activity [order] (change|update)"},
        jda = UserUpdateActivityOrderEvent.class,
        intents = {"GUILD_PRESENCES"}
)
@Documentation(
        name = "User Activity Order Update Event",
        description = {
                "Fired when a user changes their activity order.",
                "This event is triggered when a user starts a new activity or changes between activities.",
                "Activities include playing games, listening to music, streaming, or custom status messages."
        },
        examples = {
                "on user activity change:",
                "\tbroadcast \"%event-user% is now %first element of user activities%\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserActivityOrderUpdate {

    @EventValue
    public static User user(UserUpdateActivityOrderEvent event) {
        return event.getUser();
    }

    @EventValue
    public static Guild guild(UserUpdateActivityOrderEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(UserUpdateActivityOrderEvent event) {
        return event.getMember();
    }

    @EventExpression("user activit(y|ies)")
    public static Activity[] activities(UserUpdateActivityOrderEvent event) {
        return event.getNewValue().toArray(Activity[]::new);
    }
}
