package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;

@SkriptType(name = "guildchannel", jda = GuildChannel.class, parent = ChannelType.class)
public final class GuildChannelType {

    @Property(value = "jump url", shared = true)
    public static String jumpUrl(GuildChannel channel) {
        return channel.getJumpUrl();
    }

    @Property(value = "guild", shared = true)
    public static Guild guild(GuildChannel channel) {
        return channel.getGuild();
    }
}
