package net.itsthesky.disky5.events.user;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "User Activity End Event",
        patterns = {"[discord] user activity (end[ed]|stop[ped])"},
        jda = UserActivityEndEvent.class,
        intents = {"GUILD_PRESENCES"}
)
@Documentation(
        name = "User Activity End Event",
        description = {
                "Fired when a user stops an activity in a guild.",
                "Activities include playing games, listening to music, streaming, or custom status messages.",
                "This event requires the 'guild presences' intent and the 'activity' cache flag to be enabled.",
                "Note: to check whether an activity actually concluded (vs. was replaced due to a property update),",
                "iterate the member's current activities and check if one with the same type still exists."
        },
        examples = {
                "on user activity end:",
                "\tbroadcast \"%event-user% stopped %event-activity% in %event-guild%\""
        },
        since = "5.0.0-a1"
)
public final class EvtUserActivityEnd {

    @EventValue
    public static Activity activity(UserActivityEndEvent event) {
        return event.getOldActivity();
    }

    @EventValue
    public static User user(UserActivityEndEvent event) {
        return event.getUser();
    }

    @EventValue
    public static Member member(UserActivityEndEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Guild guild(UserActivityEndEvent event) {
        return event.getGuild();
    }
}
