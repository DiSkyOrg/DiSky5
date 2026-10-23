package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptEnum(name = "channeltype", jda = ChannelType.class)
@Documentation(
    name = "Channel Type",
    description = "The kind of a Discord channel: text, voice, category, news, stage, forum, and so on.",
    examples = "if type of event-channel is text:",
    since = "5.0.0-a1")
public final class ChannelTypeType {
}
