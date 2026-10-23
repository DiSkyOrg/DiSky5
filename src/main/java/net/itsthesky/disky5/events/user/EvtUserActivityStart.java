package net.itsthesky.disky5.events.user;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "User Activity Start Event",
        patterns = {"[discord] user activity start[ed]"},
        jda = UserActivityStartEvent.class,
        intents = {"GUILD_PRESENCES"}
)
@Documentation(
        name = "User Activity Start Event",
        description = {
                "Fired when a user starts a new activity in a guild.",
                "Activities include playing games, listening to music, streaming, or custom status messages.",
                "This event requires the 'guild presences' intent and the 'activity' cache flag to be enabled.",
                "Note: when a stream's title changes, a start event is fired before an end event to replace the activity."
        },
        examples = {
                "on user activity start:",
                "\tbroadcast \"%event-user% started %event-activity% in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserActivityStart {

    @EventValue
    public static Activity activity(UserActivityStartEvent event) {
        return event.getNewActivity();
    }

    @EventValue
    public static User user(UserActivityStartEvent event) {
        return event.getUser();
    }

    @EventValue
    public static Member member(UserActivityStartEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(UserActivityStartEvent event) {
        return event.getGuild();
    }
}
