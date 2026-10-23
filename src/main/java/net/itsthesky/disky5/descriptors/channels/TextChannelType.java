package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.itsthesky.disky5.annotations.SkriptType;

@SkriptType(name = "textchannel", jda = TextChannel.class, parent = StandardGuildMessageChannelType.class)
public final class TextChannelType {
}
