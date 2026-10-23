package net.itsthesky.disky5.descriptors.welcome;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildWelcomeScreen;
import net.dv8tion.jda.api.managers.GuildWelcomeScreenManager;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SkriptType(name = "welcomescreenbuilder", jda = GuildWelcomeScreenManager.class)
@Documentation(
    name = "Welcome Screen Builder",
    description = {"A pending edit of a guild's welcome screen. Only the fields you set are sent.",
        "Nothing reaches Discord until you use 'update the welcome screen using'.",
        "Welcome channels set on a builder replace the whole list on Discord."},
    since = "5.0.0-a2")
public final class WelcomeScreenBuilderType {

    private WelcomeScreenBuilderType() {}

    @New("[a] new welcome screen builder (for|of) [the] [guild] %guild%")
    @Documentation(
        name = "New Welcome Screen Builder",
        description = "Creates a builder for the welcome screen of a guild. Needs the Manage Server permission.",
        examples = {"set {_b} to a new welcome screen builder for event-guild",
            "set welcome screen enabled of {_b} to true",
            "set description of {_b} to \"Welcome!\"",
            "update the welcome screen using {_b}"},
        since = "5.0.0-a2")
    public static GuildWelcomeScreenManager newWelcomeScreenBuilder(Guild guild) {
        return guild.modifyWelcomeScreen();
    }

    @Property.Setter("welcome screen enabled")
    @Documentation(
        name = "Welcome Screen Enabled",
        description = "Whether the welcome screen is shown to new members of the guild.",
        examples = {"set welcome screen enabled of {_b} to true"},
        since = "5.0.0-a2")
    public static void setEnabled(GuildWelcomeScreenManager manager, @Nullable Boolean value) {
        manager.setEnabled(value != null && value);
    }

    @Property.Setter("description")
    public static void setDescription(GuildWelcomeScreenManager manager, @Nullable String value) {
        manager.setDescription(value);
    }

    @Property.Setter("welcome channels")
    @Documentation(
        name = "Welcome Screen Builder Channels",
        description = {"The channels the welcome screen will list (at most 5). Setting replaces the list; "
            + "'add' and 'remove' edit what the builder holds so far, and 'delete' empties it.",
            "A fresh builder starts empty, not from the current screen: to extend the current list, "
            + "retrieve the screen and set its channels first."},
        examples = {"set welcome channels of {_b} to {_a} and {_b}",
            "add {_c} to welcome channels of {_b}",
            "delete welcome channels of {_b}"},
        since = "5.0.0-a2")
    public static void setChannels(GuildWelcomeScreenManager manager, GuildWelcomeScreen.Channel[] channels) {
        manager.setWelcomeChannels(channels);
    }

    @Property.Adder("welcome channels")
    public static void addChannel(GuildWelcomeScreenManager manager, GuildWelcomeScreen.Channel channel) {
        List<GuildWelcomeScreen.Channel> current = new ArrayList<>(manager.getWelcomeChannels());
        current.add(channel);
        manager.setWelcomeChannels(current);
    }

    @Property.Remover("welcome channels")
    public static void removeChannel(GuildWelcomeScreenManager manager, GuildWelcomeScreen.Channel channel) {
        List<GuildWelcomeScreen.Channel> current = new ArrayList<>(manager.getWelcomeChannels());
        current.removeIf(c -> c.getIdLong() == channel.getIdLong());
        manager.setWelcomeChannels(current);
    }
}
