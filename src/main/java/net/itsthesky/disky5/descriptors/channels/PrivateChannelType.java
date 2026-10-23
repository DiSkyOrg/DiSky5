package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "privatechannel", jda = PrivateChannel.class, parent = MessageChannelType.class)
public final class PrivateChannelType {

    @Property("private channel user")
    @Documentation(
            name = "Private Channel User",
            description = "The user this private (DM) channel is with, or null if it cannot be resolved.",
            examples = "reply with private channel user of event-channel",
            since = "5.0.0-a1"
    )
    public static @Nullable User user(PrivateChannel channel) {
        return channel.getUser();
    }
}
