package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.itsthesky.disky5.annotations.SkriptType;

@SkriptType(name = "newschannel", jda = NewsChannel.class, parent = StandardGuildMessageChannelType.class)
public final class NewsChannelType {
}
