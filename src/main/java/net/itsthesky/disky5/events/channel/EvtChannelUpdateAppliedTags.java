package net.itsthesky.disky5.events.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateAppliedTagsEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

import java.util.List;

@SkriptEvent(
        name = "Channel Applied Tags Update Event",
        patterns = {"[discord] channel [applied] tags (change|update)"},
        jda = ChannelUpdateAppliedTagsEvent.class
)
@Documentation(
        name = "Channel Applied Tags Update Event",
        description = {"Fired when a forum channel's applied tags are changed."},
        examples = {
            "on channel tags change:",
            "\tbroadcast \"Channel %event-channel% applied tags changed\""
        },
        since = "5.0.0-a1"
)
public final class EvtChannelUpdateAppliedTags {

    @EventExpression("new [applied] tags")
    public static List<ForumTag> addedTags(ChannelUpdateAppliedTagsEvent event) {
        return event.getAddedTags();
    }

    @EventExpression("old [applied] tags")
    public static List<ForumTag> removedTags(ChannelUpdateAppliedTagsEvent event) {
        return event.getRemovedTags();
    }

    @EventValue
    public static Guild guild(ChannelUpdateAppliedTagsEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Channel channel(ChannelUpdateAppliedTagsEvent event) {
        return event.getChannel();
    }
}
