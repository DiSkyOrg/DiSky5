package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateDefaultReactionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventTime;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import org.jetbrains.annotations.Nullable;

// TODO: no Emote type yet, so this returns the native JDA EmojiUnion.
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
            "\tbroadcast \"Channel %event-channel% default reaction changed\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateDefaultReaction {

    @EventValue
    public static @Nullable EmojiUnion newReaction(ChannelUpdateDefaultReactionEvent event) {
        return event.getNewValue();
    }

    @EventValue(time = EventTime.PAST)
    public static @Nullable EmojiUnion oldReaction(ChannelUpdateDefaultReactionEvent event) {
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
