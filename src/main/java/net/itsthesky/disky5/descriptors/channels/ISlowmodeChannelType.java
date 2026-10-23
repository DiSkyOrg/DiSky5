package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.channel.attribute.ISlowmodeChannel;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

// Declared once on ISlowmodeChannel instead of per concrete type: duplicate per-type `slowmode`
// properties collide on a variable operand (first-registered wins, the rest silently return none).
@SkriptType(name = "slowmodechannel", jda = ISlowmodeChannel.class, parent = GuildChannelType.class)
public final class ISlowmodeChannelType {

    @Property("slowmode")
    @Documentation(
            name = "Channel Slowmode",
            description = "The slowmode of the channel — the number of seconds a member must wait between messages.",
            examples = "send \"%slowmode of event-channel% seconds\"",
            since = "5.0.0-a1"
    )
    public static int slowmode(ISlowmodeChannel channel) {
        return channel.getSlowmode();
    }
}
