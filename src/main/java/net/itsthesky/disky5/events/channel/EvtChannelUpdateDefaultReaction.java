package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateDefaultReactionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Channel Default Reaction Update Event",
        patterns = {"[discord] channel default reaction (change|update)"},
        jda = ChannelUpdateDefaultReactionEvent.class
)
@Documentation(
        name = "Channel Default Reaction Update Event",
        description = {"Fired when a forum channel's default reaction is changed."},
        examples = {
            "on channel default reaction change:",
            "\tbroadcast \"Default reaction of %event-channel%: %old default reaction% -> %new default reaction%\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateDefaultReaction {

    // Not an event-value: Skript reads "event-emoji" as a plural (its "us" -> "i" rule), so it never resolves.
    @EventExpression("new default reaction")
    public static @Nullable Emoji newReaction(ChannelUpdateDefaultReactionEvent event) {
        return event.getNewValue();
    }

    @EventExpression("old default reaction")
    public static @Nullable Emoji oldReaction(ChannelUpdateDefaultReactionEvent event) {
        return event.getOldValue();
    }

    @EventValue
    public static Guild guild(ChannelUpdateDefaultReactionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateDefaultReactionEvent event) {
        return event.getChannel();
    }
}
