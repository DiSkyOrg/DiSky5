package net.itsthesky.disky5.descriptors.welcome;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildWelcomeScreen;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "welcomescreen", jda = GuildWelcomeScreen.class)
@Documentation(
    name = "Welcome Screen",
    description = {"The welcome screen of a community guild, as it was when it was retrieved.",
        "It is a snapshot: edit it with a welcome screen builder, then retrieve it again."},
    since = "5.0.0-a2")
public final class WelcomeScreenType {

    private WelcomeScreenType() {}

    @Property("description")
    @Documentation(
        name = "Welcome Screen Description",
        description = "The description shown at the top of the welcome screen, if any.",
        examples = {"await set {_screen} to the welcome screen of event-guild",
            "reply with description of {_screen}"},
        since = "5.0.0-a2")
    public static @Nullable String description(GuildWelcomeScreen screen) {
        return screen.getDescription();
    }

    @Property("welcome channels")
    @Documentation(
        name = "Welcome Screen Channels",
        description = "The channels listed on the welcome screen (Discord allows at most 5).",
        examples = {"loop welcome channels of {_screen}:",
            "    reply with description of loop-value"},
        since = "5.0.0-a2")
    public static List<GuildWelcomeScreen.Channel> channels(GuildWelcomeScreen screen) {
        return screen.getChannels();
    }

    @Property(value = "guild", shared = true)
    public static @Nullable Guild guild(GuildWelcomeScreen screen) {
        return screen.getGuild();
    }
}
