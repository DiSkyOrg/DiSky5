package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.channel.Channel;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptType(name = "channel", jda = Channel.class)
public final class ChannelType {

    @Property(value = "name", shared = true)
    public static String discordName(Channel channel) {
        return channel.getName();
    }
}
