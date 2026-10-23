package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.ThreadMember;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.annotations.ConditionType;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@SkriptType(name = "threadchannel", jda = ThreadChannel.class, parent = GuildChannelType.class)
public final class ThreadChannelType {

    @Property("discord owner")
    @Documentation(
            name = "Thread Owner",
            description = "The member who created this thread, or null if they are no longer in the guild.",
            examples = "send \"%owner of event-channel%\"",
            since = "5.0.0-a1"
    )
    public static @Nullable Member owner(ThreadChannel channel) {
        return channel.getOwner();
    }

    @Property("message count")
    @Documentation(
            name = "Thread Message Count",
            description = "The number of messages currently tracked in this thread.",
            examples = "send \"%message count of event-channel%\"",
            since = "5.0.0-a1"
    )
    public static int messageCount(ThreadChannel channel) {
        return channel.getMessageCount();
    }

    @Property("archived")
    @Documentation(
            name = "Thread Archived",
            description = "Whether this thread is archived.",
            examples = "if archived of event-channel is true:",
            since = "5.0.0-a1"
    )
    public static boolean archived(ThreadChannel channel) {
        return channel.isArchived();
    }

    @Property("locked")
    @Documentation(
            name = "Thread Locked",
            description = "Whether this thread is locked.",
            examples = "if locked of event-channel is true:",
            since = "5.0.0-a1"
    )
    public static boolean locked(ThreadChannel channel) {
        return channel.isLocked();
    }

    @Property.Retrieve("thread members")
    @Documentation(
            name = "Thread Members",
            description = {
                "The members who joined this thread. Fetched online, so it requires 'await'.",
                "The 'guild members' intent is required."
            },
            examples = "await set {_members::*} to thread members of event-channel",
            since = "5.0.0-a1"
    )
    public static List<Member> threadMembers(ThreadChannel channel) {
        return channel.retrieveThreadMembers().complete().stream()
                .map(ThreadMember::getMember)
                .filter(Objects::nonNull)
                .toList();
    }

    @Property.Retrieve("start message")
    @Documentation(
            name = "Thread Start Message",
            description = {
                "The message this thread started from. Fetched online, so it requires 'await'.",
                "For a forum post, this is the post's first message."
            },
            examples = "await set {_message} to start message of event-channel",
            since = "5.0.0-a1"
    )
    public static RestAction<Message> startMessage(ThreadChannel channel) {
        return channel.retrieveStartMessage();
    }

    @Condition(property = "public", type = ConditionType.BE)
    @Documentation(
            name = "Thread is Public",
            description = "Whether this thread is a public one (anyone who can see the parent channel can join it).",
            examples = "if event-channel is public:",
            since = "5.0.0-a1"
    )
    public static boolean publicThread(ThreadChannel channel) {
        return channel.isPublic();
    }
}
