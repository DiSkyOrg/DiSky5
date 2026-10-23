package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.StageInstance;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptType(name = "stagechannel", jda = StageChannel.class, parent = AudioChannelType.class)
public final class StageChannelType {

    @Property("stage instance")
    @Documentation(
            name = "Stage Instance",
            description = "The active stage instance of this stage channel, or null when no stage is live.",
            examples = "if stage instance of event-channel is set:",
            since = "5.0.0-a1"
    )
    public static StageInstance stageInstance(StageChannel channel) {
        return channel.getStageInstance();
    }
}
