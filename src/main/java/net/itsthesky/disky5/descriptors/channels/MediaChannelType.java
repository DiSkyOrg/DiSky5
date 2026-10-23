package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.channel.concrete.MediaChannel;
import net.itsthesky.disky5.annotations.SkriptType;

@SkriptType(name = "mediachannel", jda = MediaChannel.class, parent = StandardGuildChannelType.class)
public final class MediaChannelType {
}
