package net.itsthesky.disky5.descriptors.builders;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.attribute.IThreadContainer;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.ThreadChannelAction;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptType(name = "threadaction", jda = ThreadChannelAction.class)
@Documentation(
    name = "Thread Builder",
    description = {"A thread that does not exist yet: configure it, then create it with 'the build result of'.",
        "Nothing is sent to Discord until then, so setting its properties costs no request."},
    since = "5.0.0-a1")
public final class ThreadActionType {

    private static final int[] ARCHIVE_MINUTES = {60, 1440, 4320, 10080};

    private ThreadActionType() {}

    @New("[a] new [public] thread builder [(named|with [the] name)] %string% in [the] [channel] %threadcontainer%")
    @Documentation(
        name = "New Thread Builder",
        description = "Creates a public thread builder. The thread only exists once you read "
            + "'the build result of' it, which requires 'await'.",
        examples = {"set {_b} to a new thread builder named \"Discussion\" in event-channel",
            "set auto archive duration of {_b} to 1440",
            "await set {_thread} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ThreadChannelAction newThreadBuilder(String name, IThreadContainer container) {
        return container.createThreadChannel(name);
    }

    @New("[a] new private thread builder [(named|with [the] name)] %string% in [the] [channel] %threadcontainer%")
    @Documentation(
        name = "New Private Thread Builder",
        description = {"Creates a private thread builder.",
            "Private threads require the guild to be boost level 2 or higher; building one in a "
                + "lower-level guild fails."},
        examples = {"set {_b} to a new private thread builder named \"Staff Room\" in event-channel",
            "await set {_thread} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ThreadChannelAction newPrivateThreadBuilder(String name, IThreadContainer container) {
        return container.createThreadChannel(name, true);
    }

    @New("[a] new thread builder [(named|with [the] name)] %string% from [the] [message] %message%")
    @Documentation(
        name = "New Thread Builder From Message",
        description = "Creates a public thread builder anchored to an existing message, in the "
            + "channel that message was sent in.",
        examples = {"set {_b} to a new thread builder named \"About this\" from event-message",
            "await set {_thread} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ThreadChannelAction newThreadBuilderFromMessage(String name, Message message) {
        if (!(message.getChannel() instanceof IThreadContainer container))
            throw new IllegalArgumentException("the channel of that message cannot hold threads");
        return container.createThreadChannel(name, message.getId());
    }

    @Build
    @Documentation(
        name = "Built Thread",
        description = {"Creates the thread on Discord and returns it. Requires 'await'.",
            "Building the same builder twice creates two threads."},
        examples = {"await set {_thread} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static RestAction<ThreadChannel> build(ThreadChannelAction action) {
        return action;
    }

    @Property.Setter("auto archive duration")
    @Documentation(
        name = "Thread Builder Auto Archive Duration",
        description = {"How long the thread being built stays active without a message, in minutes.",
            "Discord only accepts 60 (1 hour), 1440 (1 day), 4320 (3 days) or 10080 (1 week)."},
        examples = {"set auto archive duration of {_b} to 4320"},
        since = "5.0.0-a1")
    public static void setAutoArchiveDuration(ThreadChannelAction action, Number value) {
        if (value != null)
            action.setAutoArchiveDuration(archiveDuration(value.intValue()));
    }

    @Property.Setter("invitable")
    @Documentation(
        name = "Thread Builder Invitable",
        description = "Whether non-moderators can add other members to the private thread being built.",
        examples = {"set invitable of {_b} to false"},
        since = "5.0.0-a1")
    public static void setInvitable(ThreadChannelAction action, Boolean value) {
        if (value != null)
            action.setInvitable(value);
    }

    @Property.Setter("slowmode")
    @Documentation(
        name = "Thread Builder Slowmode",
        description = "The slowmode of the thread being built, in seconds (0 disables it).",
        examples = {"set slowmode of {_b} to 5"},
        since = "5.0.0-a1")
    public static void setSlowmode(ThreadChannelAction action, Integer value) {
        if (value != null)
            action.setSlowmode(value);
    }

    static ThreadChannel.AutoArchiveDuration archiveDuration(int minutes) {
        for (int allowed : ARCHIVE_MINUTES)
            if (allowed == minutes)
                return ThreadChannel.AutoArchiveDuration.fromKey(minutes);
        throw new IllegalArgumentException(minutes + " is not a valid auto archive duration; "
            + "Discord only accepts 60, 1440, 4320 or 10080 minutes");
    }
}
