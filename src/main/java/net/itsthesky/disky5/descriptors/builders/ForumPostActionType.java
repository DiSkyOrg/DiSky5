package net.itsthesky.disky5.descriptors.builders;

import net.dv8tion.jda.api.entities.channel.attribute.IPostContainer;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.forums.ForumPost;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import net.dv8tion.jda.api.entities.channel.forums.ForumTagSnowflake;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.ForumPostAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptType(name = "forumpostaction", jda = ForumPostAction.class)
@Documentation(
    name = "Forum Post Builder",
    description = {"A forum post that does not exist yet: configure it, then create it with 'the build result of'.",
        "Nothing is sent to Discord until then, so setting its properties costs no request."},
    since = "5.0.0-a1")
public final class ForumPostActionType {

    private ForumPostActionType() {}

    @New("[a] new [forum] post builder [(named|with [the] name)] %string% with [the] [message] "
        + "[content] %string% in [the] [channel] %forumchannel/mediachannel%")
    @Documentation(
        name = "New Forum Post Builder",
        description = "Creates a forum post builder. The post only exists once you read "
            + "'the build result of' it, which requires 'await'.",
        examples = {"set {_b} to a new post builder named \"I need help!\" with message "
            + "\"please help me\" in forum channel with id \"000\"",
            "set post tag of {_b} to {_tag}",
            "await set {_thread} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ForumPostAction newForumPostBuilder(String name, String content, IPostContainer channel) {
        return channel.createForumPost(name, MessageCreateData.fromContent(content));
    }

    @Build
    @Documentation(
        name = "Built Forum Post",
        description = {"Creates the post on Discord and returns its thread channel. Requires 'await'.",
            "Building the same builder twice creates two posts."},
        examples = {"await set {_thread} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static RestAction<ThreadChannel> build(ForumPostAction action) {
        return action.map(ForumPost::getThreadChannel);
    }

    @Property.Setter("post tag")
    @Documentation(
        name = "Forum Post Builder Tag",
        description = {"The tag applied to the post being built.",
            "Only one tag can be set this way; multi-valued write-only properties are not supported yet."},
        examples = {"set post tag of {_b} to first element of available tags of {_forum}"},
        since = "5.0.0-a1")
    public static void setPostTag(ForumPostAction action, ForumTag value) {
        if (value != null)
            action.setTags(ForumTagSnowflake.fromId(value.getId()));
    }

    @Property.Setter("auto archive duration")
    @Documentation(
        name = "Forum Post Builder Auto Archive Duration",
        description = {"How long the post being built stays active without a message, in minutes.",
            "Discord only accepts 60 (1 hour), 1440 (1 day), 4320 (3 days) or 10080 (1 week)."},
        examples = {"set auto archive duration of {_b} to 10080"},
        since = "5.0.0-a1")
    public static void setAutoArchiveDuration(ForumPostAction action, Number value) {
        if (value != null)
            action.setAutoArchiveDuration(ThreadActionType.archiveDuration(value.intValue()));
    }

    @Property.Setter("slowmode")
    @Documentation(
        name = "Forum Post Builder Slowmode",
        description = "The slowmode of the post being built, in seconds (0 disables it).",
        examples = {"set slowmode of {_b} to 5"},
        since = "5.0.0-a1")
    public static void setSlowmode(ForumPostAction action, Integer value) {
        if (value != null)
            action.setSlowmode(value);
    }
}
