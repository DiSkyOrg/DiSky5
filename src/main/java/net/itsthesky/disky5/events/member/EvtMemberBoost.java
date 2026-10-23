package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventFilter;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Member Boost Event",
        patterns = {"member boost[ed]"},
        jda = MessageReceivedEvent.class
)
@Documentation(
        name = "Member Boost Event",
        description = "Fired when a member boosts a server, which is detected through a system message "
                + "in the server.",
        examples = {
            "on member boost:",
            "\tbroadcast \"Thank you %event-user% for boosting %event-guild%!\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberBoost {

    // Boosts are detected through a system message, so the underlying JDA event is
    // MessageReceivedEvent: without this filter the trigger runs on every message the bot sees.
    @EventFilter
    public static boolean accept(MessageReceivedEvent event) {
        if (!event.isFromGuild())
            return false;
        return switch (event.getMessage().getType()) {
            case GUILD_MEMBER_BOOST, GUILD_BOOST_TIER_1, GUILD_BOOST_TIER_2, GUILD_BOOST_TIER_3 -> true;
            default -> false;
        };
    }

    @EventValue
    public static Message message(MessageReceivedEvent event) {
        return event.getMessage();
    }

    @EventValue
    public static @Nullable Guild guild(MessageReceivedEvent event) {
        return event.isFromGuild() ? event.getGuild() : null;
    }

    @EventValue
    public static User user(MessageReceivedEvent event) {
        return event.getAuthor();
    }

    @EventValue
    public static Channel channel(MessageReceivedEvent event) {
        return event.getChannel();
    }
}
