package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.channel.attribute.IThreadContainer;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.List;

@SkriptType(name = "threadcontainer", jda = IThreadContainer.class, parent = GuildChannelType.class)
public final class ThreadContainerType {

    @Property("threads")
    @Documentation(
            name = "Thread Channels",
            description = "The thread channels currently held by this channel.",
            examples = "loop threads of event-channel:",
            since = "5.0.0-a1"
    )
    public static List<ThreadChannel> threads(IThreadContainer container) {
        return container.getThreadChannels();
    }

    @Property.Retrieve("archived threads")
    @Documentation(
            name = "Archived Thread Channels",
            description = {
                "The archived public thread channels of this channel. Fetched online, so it requires 'await'.",
                "Only the most recent page is returned."
            },
            examples = "await set {_threads::*} to archived threads of event-channel",
            since = "5.0.0-a1"
    )
    public static RestAction<List<ThreadChannel>> archivedThreads(IThreadContainer container) {
        return container.retrieveArchivedPublicThreadChannels();
    }

    @Property.Retrieve("archived private threads")
    @Documentation(
            name = "Archived Private Thread Channels",
            description = {
                "The archived private thread channels of this channel. Fetched online, so it requires 'await'.",
                "The bot needs the 'manage threads' permission, and only the most recent page is returned."
            },
            examples = "await set {_threads::*} to archived private threads of event-channel",
            since = "5.0.0-a1"
    )
    public static RestAction<List<ThreadChannel>> archivedPrivateThreads(IThreadContainer container) {
        return container.retrieveArchivedPrivateThreadChannels();
    }
}
