package net.itsthesky.disky5.events.guild;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.automod.AutoModResponse;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.automod.AutoModExecutionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "AutoMod Execution",
        patterns = {"[discord] automod (execution|execute)"},
        jda = AutoModExecutionEvent.class
)
@Documentation(
        name = "AutoMod Execution",
        description = {
                "Fired when an automated automod response has been triggered through an automod Rule. ",
                "Can be used to get the channel, user content, keyword that was found, the automod response and the id of the automod rule, ",
                "the user, the id of the message which triggered the rule, the guild it occurred in, and the id of the alert message sent to the alert channel (if configured)."
        },
        examples = {
                "on automod execute:",
                "\tbroadcast \"AutoMod rule triggered by %event-user% in %event-channel%\""
        },
        since = "5.0.0-a1"
)
public final class EvtAutoModExecution {

    @EventValue
    public static Guild guild(AutoModExecutionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(AutoModExecutionEvent event) {
        return event.getChannel();
    }

    @EventValue
    public static AutoModResponse response(AutoModExecutionEvent event) {
        return event.getResponse();
    }

    @EventValue
    public static AutoModExecutionEvent execution(AutoModExecutionEvent event) {
        return event;
    }

    @EventValue
    public static @Nullable User user(AutoModExecutionEvent event) {
        return event.getJDA().getUserById(event.getUserId());
    }
}
