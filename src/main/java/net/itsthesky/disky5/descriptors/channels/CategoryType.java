package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.List;

@SkriptType(name = "category", jda = Category.class, parent = GuildChannelType.class)
public final class CategoryType {

    @Property("child channels")
    @Documentation(
            name = "Category Child Channels",
            description = "All guild channels that belong to this category.",
            examples = "loop child channels of event-channel:",
            since = "5.0.0-a1"
    )
    public static List<GuildChannel> childChannels(Category category) {
        return category.getChannels();
    }
}
