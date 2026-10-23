package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.annotations.ConditionType;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "user", jda = User.class)
public final class UserType {

    @Property(value = "name", shared = true)
    public static String discordName(User user) {
        return user.getName();
    }

    @Property("discriminator")
    @Documentation(
            name = "User Discriminator",
            description = {
                "The four digit number after the # of a user's name.",
                "These, mixed with the user name itself, are unique.",
                "This DOES NOT include the '#' char, so you have to add it yourself."
            },
            examples = "reply with discriminator of event-user",
            since = "5.0.0-a1"
    )
    public static String discriminator(User user) {
        return user.getDiscriminator();
    }

    @Property("badges")
    @Documentation(
            name = "User Badges",
            description = {
                "Every badge's name a user has.",
                "This does not and cannot contain any intro-related badges (nitro membership or nitro "
                    + "boosting); it needs OAuth2 to be enabled."
            },
            examples = "reply with \"Whoa! You got all of them? %join badges of event-user with nl%\"",
            since = "5.0.0-a1"
    )
    public static List<String> badges(User user) {
        return user.getFlags().stream()
                .map(User.UserFlag::getName)
                .toList();
    }

    @Property("mutual guilds")
    @Documentation(
            name = "User Mutual Guilds",
            description = "Every guild that the bot and the user have in common.",
            examples = "reply with \"Oh boi, we have %size of mutual guilds of event-user% mutual guilds!\"",
            since = "5.0.0-a1"
    )
    public static List<Guild> mutualGuilds(User user) {
        return user.getMutualGuilds();
    }

    @Property.Retrieve("profile")
    @Documentation(
            name = "User Profile",
            description = {
                "The profile of a user, which mainly holds the banner and accent color.",
                "This is fetched online from Discord, so it requires 'await'."
            },
            examples = "await set {_profile} to profile of event-user",
            since = "5.0.0-a1"
    )
    public static User.Profile profile(User user) {
        return user.retrieveProfile().complete();
    }

    @Property.Retrieve("private channel")
    @Documentation(
            name = "User Private Channel",
            description = {
                "The private (DM) channel with a user, opened if it does not exist yet.",
                "This is fetched online from Discord, so it requires 'await'."
            },
            examples = "await set {_channel} to private channel of event-user",
            since = "5.0.0-a1"
    )
    public static RestAction<PrivateChannel> privateChannel(User user) {
        return user.openPrivateChannel();
    }

    @Property("primary guild tag")
    @Documentation(
            name = "User Primary Guild Tag",
            description = {
                "The primary-guild server tag of a user (up to 4 characters).",
                "Null if the user has no primary guild or has the tag hidden."
            },
            examples = "reply with \"Tag: %primary guild tag of event-user%\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String primaryGuildTag(User user) {
        return user.getPrimaryGuild() == null ? null : user.getPrimaryGuild().getTag();
    }

    @Property("primary guild tag icon")
    @Documentation(
            name = "User Primary Guild Tag Icon",
            description = "The icon URL of a user's primary-guild server tag, or null if there is none.",
            examples = "reply with primary guild tag icon of event-user",
            since = "5.0.0-a1"
    )
    public static @Nullable String primaryGuildTagIcon(User user) {
        return user.getPrimaryGuild() == null ? null : user.getPrimaryGuild().getBadgeUrl();
    }

    @Property(value = "avatar url", shared = true)
    public static String avatarUrl(User user) {
        return user.getEffectiveAvatarUrl();
    }

    @Condition(property = "[a] discord bot", type = ConditionType.BE)
    @Documentation(
            name = "User is a Bot",
            description = "Whether the user is a Discord bot account.",
            examples = "if event-user is a discord bot:",
            since = "5.0.0-a1"
    )
    public static boolean bot(User u) {
        return u.isBot();
    }

    @Condition(property = "[a] system [user]", type = ConditionType.BE)
    @Documentation(
            name = "User is a System User",
            description = "Whether the user is an official Discord system account.",
            examples = "if event-user is a system user:",
            since = "5.0.0-a1"
    )
    public static boolean system(User u) {
        return u.isSystem();
    }
}
