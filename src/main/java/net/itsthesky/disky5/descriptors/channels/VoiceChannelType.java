package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptType(name = "voicechannel", jda = VoiceChannel.class, parent = AudioChannelType.class)
public final class VoiceChannelType {

    @Property("voice status")
    @Documentation(
            name = "Voice Channel Status",
            description = "The custom voice status text currently set on this voice channel.",
            examples = "send \"%voice status of event-channel%\"",
            since = "5.0.0-a1"
    )
    public static String voiceStatus(VoiceChannel channel) {
        return channel.getStatus();
    }
}
