package net.itsthesky.disky5.descriptors.channels;

import net.dv8tion.jda.api.entities.channel.attribute.ICategorizableChannel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "standardguildchannel", jda = StandardGuildChannel.class, parent = GuildChannelType.class)
public final class StandardGuildChannelType {

    // Named "channel position", not bare "position": a bare `position of {_var}` binds to
    // Skript's/addons' typed `position of %player/entity%` and never resolves a channel.
    @Property("channel position")
    @Documentation(
            name = "Channel Position",
            description = "The position (ordering index) of this channel within its guild.",
            examples = "send \"%channel position of event-channel%\"",
            since = "5.0.0-a1"
    )
    public static int position(StandardGuildChannel channel) {
        return channel.getPosition();
    }

    @Property.Setter("channel position")
    public static RestAction<?> setPosition(StandardGuildChannel channel, int value) {
        return channel.getManager().setPosition(value);
    }

    @Property("category")
    @Documentation(
            name = "Channel Category",
            description = "The parent category of this channel, or null if it is not inside a category.",
            examples = {
                "reply with name of category of event-channel",
                "set category of event-channel to {_cat}"
            },
            since = "5.0.0-a1"
    )
    public static @Nullable Category category(StandardGuildChannel channel) {
        return channel.getParentCategory();
    }

    @Property.Setter("category")
    public static RestAction<?> setCategory(StandardGuildChannel channel, @Nullable Category value) {
        return ((ICategorizableChannel) channel).getManager().setParent(value);
    }
}
