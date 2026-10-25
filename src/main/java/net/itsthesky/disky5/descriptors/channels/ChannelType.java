package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
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

    @Property.Setter(value = "name", shared = true)
    public static RestAction<?> setName(Channel channel, String value) {
        if (!(channel instanceof GuildChannel guildChannel))
            throw new IllegalStateException("Only a guild channel can be renamed, not a " + channel.getType() + " channel.");
        return guildChannel.getManager().setName(value);
    }
}
