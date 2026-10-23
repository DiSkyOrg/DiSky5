package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.List;

@SkriptType(name = "audiochannel", jda = AudioChannel.class, parent = StandardGuildChannelType.class)
public final class AudioChannelType {

    @Property("bitrate")
    @Documentation(
            name = "Channel Bitrate",
            description = "The audio bitrate (in bits per second) of this voice/stage channel.",
            examples = "send \"%bitrate of event-channel%\"",
            since = "5.0.0-a1"
    )
    public static int bitrate(AudioChannel channel) {
        return channel.getBitrate();
    }

    @Property.Setter("bitrate")
    public static RestAction<?> setBitrate(AudioChannel channel, int value) {
        return channel.getManager().setBitrate(value);
    }

    @Property("user limit")
    @Documentation(
            name = "Channel User Limit",
            description = "The maximum number of members that can connect to this audio channel at once (0 = no limit).",
            examples = "send \"%user limit of event-channel%\"",
            since = "5.0.0-a1"
    )
    public static int userLimit(AudioChannel channel) {
        return channel.getUserLimit();
    }

    @Property.Setter("user limit")
    public static RestAction<?> setUserLimit(AudioChannel channel, int value) {
        return channel.getManager().setUserLimit(value);
    }

    @Property("region")
    @Documentation(
            name = "Channel Region",
            description = "The raw voice region of this audio channel, or null when it is automatic.",
            examples = "send \"%region of event-channel%\"",
            since = "5.0.0-a1"
    )
    public static String region(AudioChannel channel) {
        return channel.getRegionRaw();
    }

    @Property.Setter("region")
    public static RestAction<?> setRegion(AudioChannel channel, String value) {
        return channel.getManager().setRegion(Region.fromKey(value));
    }

    @Property("members")
    @Documentation(
            name = "Audio Channel Members",
            description = "The members currently connected to this audio (voice/stage) channel.",
            examples = "loop members of event-channel:",
            since = "5.0.0-a1"
    )
    public static List<Member> members(AudioChannel channel) {
        return channel.getMembers();
    }
}
