package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.messages.MessagePoll;
import net.dv8tion.jda.api.components.Component;
import net.dv8tion.jda.api.components.utils.ComponentIterator;
import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.annotations.ConditionType;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SkriptType(name = "message", jda = Message.class)
public final class MessageType {

    @Property("discord content")
    @Documentation(
            name = "Message Content",
            description = "Get the raw (non formatted) content of a sent message.",
            examples = "content of event-message",
            since = "5.0.0-a1"
    )
    public static @Nullable String content(Message message) {
        return message.getContentRaw();
    }

    @Property("discord author")
    @Documentation(
            name = "Message User Author",
            description = "Get the user instance of the message's author. Can be null in case of the "
                    + "message was sent by a webhook.",
            examples = "author of event-message",
            since = "5.0.0-a1"
    )
    public static @Nullable User author(Message message) {
        return message.getAuthor();
    }

    @Property("member author")
    @Documentation(
            name = "Message Member Author",
            description = "Get the member instance of the message's author. Can be null if it's in PM "
                    + "or not in guild!",
            examples = "member writer of event-message",
            since = "5.0.0-a1"
    )
    public static @Nullable Member memberAuthor(Message message) {
        if (message.isFromGuild())
            return message.getMember();
        return null;
    }

    @Property(value = "jump url", shared = true)
    public static String jumpUrl(Message message) {
        return message.getJumpUrl();
    }

    @Property("referenced message")
    @Documentation(
            name = "Message Referenced Message",
            description = "Get the message referenced (replied to) by a specific message, if any.",
            examples = "set {_replied} to referenced message of event-message",
            since = "5.0.0-a1"
    )
    public static @Nullable Message referencedMessage(Message message) {
        return message.getReferencedMessage();
    }

    @Property("poll")
    @Documentation(
            name = "Message Poll",
            description = "Get the poll attached to a specific message, if any.",
            examples = "set {_poll} to poll of event-message",
            since = "5.0.0-a1"
    )
    public static @Nullable MessagePoll poll(Message message) {
        return message.getPoll();
    }

    @Property("embeds")
    @Documentation(
            name = "Message Embeds",
            description = "Get every embed of a specific message, in display order (10 at most).",
            examples = "embeds of event-message",
            since = "5.0.0-a1"
    )
    public static List<MessageEmbed> embeds(Message message) {
        return message.getEmbeds();
    }

    @Property("discord attachments")
    @Documentation(
            name = "Message Attachments",
            description = "Get every attachment as custom object of a message.",
            examples = "attachments of event-message",
            since = "5.0.0-a1"
    )
    public static List<Message.Attachment> attachments(Message message) {
        return message.getAttachments();
    }

    @Property("reactions")
    @Documentation(
            name = "Message Reactions",
            description = {
                "Get every reaction of a message.",
                "Because of Discord's limitation, we cannot get which user reacted with which "
                    + "reaction, you'll have to count them yourself."
            },
            examples = "reactions of event-message",
            since = "5.0.0-a1"
    )
    public static List<MessageReaction> reactions(Message message) {
        return message.getReactions();
    }

    @Property("mentioned users")
    @Documentation(
            name = "Message Mentioned Users",
            description = "Get every mentioned user in a message.",
            examples = "mentioned users of event-message",
            since = "5.0.0-a1"
    )
    public static List<User> mentionedUsers(Message message) {
        return message.getMentions().getUsers();
    }

    @Property("mentioned members")
    @Documentation(
            name = "Message Mentioned Members",
            description = "Get every mentioned member in a message. If the message doesn't come from "
                    + "a guild it will return an empty list!",
            examples = "mentioned members of event-message",
            since = "5.0.0-a1"
    )
    public static List<Member> mentionedMembers(Message message) {
        return message.getMentions().getMembers();
    }

    @Property("mentioned roles")
    @Documentation(
            name = "Message Mentioned Roles",
            description = "Get every mentioned role in a message. If the message doesn't come from a "
                    + "guild it will return an empty list!",
            examples = "mentioned roles of event-message",
            since = "5.0.0-a1"
    )
    public static List<Role> mentionedRoles(Message message) {
        return message.getMentions().getRoles();
    }

    @Property("channel")
    @Documentation(
            name = "Message Channel",
            description = "The channel the message was sent in (guild channel, thread or private channel).",
            examples = "set {_c} to channel of {_message}",
            since = "5.0.0-b1"
    )
    public static MessageChannel channel(Message message) {
        return message.getChannel();
    }

    @Property("mentioned channels")
    @Documentation(
            name = "Message Mentioned Channels",
            description = "Get every mentioned text channel in a message.",
            examples = "mentioned text channels of event-message",
            since = "5.0.0-a1"
    )
    public static List<TextChannel> mentionedChannels(Message message) {
        return message.getMentions().getChannels(TextChannel.class);
    }

    @Property("mentioned voice channels")
    @Documentation(
            name = "Message Mentioned Voice Channels",
            description = "Get every mentioned voice channel in a message.",
            examples = "mentioned voice channels of event-message",
            since = "5.0.0-a1"
    )
    public static List<VoiceChannel> mentionedVoiceChannels(Message message) {
        return message.getMentions().getChannels(VoiceChannel.class);
    }

    @Property("mentioned emotes")
    @Documentation(
            name = "Message Mentioned Emotes",
            description = {
                "Get every mentioned emote in a message.",
                "This will only return custom emotes, and will therefore not include discord (unicode) "
                    + "emotes."
            },
            examples = "mentioned emotes of event-message",
            since = "5.0.0-a1"
    )
    public static List<CustomEmoji> mentionedEmotes(Message message) {
        return message.getMentions().getCustomEmojis();
    }

    @Property(value = "guild", shared = true)
    public static Guild guild(Message message) {
        return message.isFromGuild() ? message.getGuild() : null;
    }

    @Property("discord components")
    @Documentation(
            name = "Message Components",
            description = {"Every component of a sent message, rows and containers flattened: the buttons, "
                + "dropdowns, text displays and so on it carries.",
                "These are the sent components, not builders; use 'replace component' to change one."},
            examples = {"loop discord components of event-message:"},
            since = "5.0.0-a1"
    )
    public static List<Component> components(Message message) {
        return ComponentIterator.createStream(message.getComponents()).toList();
    }

    @Condition(property = "using components v2", type = ConditionType.BE)
    @Documentation(
            name = "Message is Using Components V2",
            description = "Whether the message is a components V2 message (containers, sections, text displays...).",
            examples = "if event-message is using components v2:",
            since = "5.0.0-a1"
    )
    public static boolean usingComponentsV2(Message m) {
        return m.isUsingComponentsV2();
    }

    @Condition(property = "edited", type = ConditionType.BE)
    @Documentation(
            name = "Message is Edited",
            description = "Whether the message has been edited.",
            examples = "if event-message is edited:",
            since = "5.0.0-a1"
    )
    public static boolean edited(Message m) {
        return m.isEdited();
    }

    @Condition(property = "pinned", type = ConditionType.BE)
    @Documentation(
            name = "Message is Pinned",
            description = "Whether the message is pinned in its channel.",
            examples = "if event-message is pinned:",
            since = "5.0.0-a1"
    )
    public static boolean pinned(Message m) {
        return m.isPinned();
    }

    @Condition(property = "ephemeral", type = ConditionType.BE)
    @Documentation(
            name = "Message is Ephemeral",
            description = "Whether the message is ephemeral (only visible to one user).",
            examples = "if event-message is ephemeral:",
            since = "5.0.0-a1"
    )
    public static boolean ephemeral(Message m) {
        return m.isEphemeral();
    }

    @Condition(property = "tts", type = ConditionType.BE)
    @Documentation(
            name = "Message is TTS",
            description = "Whether the message was sent using text-to-speech.",
            examples = "if event-message is tts:",
            since = "5.0.0-a1"
    )
    public static boolean tts(Message m) {
        return m.isTTS();
    }

    @Condition(property = "from [a] guild", type = ConditionType.BE)
    @Documentation(
            name = "Message is from a Guild",
            description = "Whether the message was sent in a guild channel (rather than a private/DM channel).",
            examples = "if event-message is from a guild:",
            since = "5.0.0-a1"
    )
    public static boolean fromGuild(Message m) {
        return m.isFromGuild();
    }

    @Condition(property = "from [a] (dm|private message|direct message)", type = ConditionType.BE)
    @Documentation(
            name = "Message is from a DM",
            description = "Whether the message was sent in a private (direct message) channel rather than in a guild.",
            examples = "if event-message is from a dm:",
            since = "5.0.0-a1"
    )
    public static boolean fromDm(Message m) {
        return !m.isFromGuild();
    }

    @Condition(property = "[a] voice message", type = ConditionType.BE)
    @Documentation(
            name = "Message is a Voice Message",
            description = "Whether the message is a voice message. If it is, the 'attachment duration' "
                    + "expression can be used on its attachment.",
            examples = "if event-message is a voice message:",
            since = "5.0.0-a1"
    )
    public static boolean voiceMessage(Message m) {
        return m.isVoiceMessage();
    }

    @Condition(property = "forwarded", type = ConditionType.BE)
    @Documentation(
            name = "Message is Forwarded",
            description = "Whether the message is a forwarded message, that is, a message originally sent "
                    + "in another channel and forwarded to this one.",
            examples = "if event-message is forwarded:",
            since = "5.0.0-a1"
    )
    public static boolean forwarded(Message m) {
        MessageReference reference = m.getMessageReference();
        return reference != null && reference.getType() == MessageReference.MessageReferenceType.FORWARD;
    }

    @Condition(property = "[a] poll", type = ConditionType.HAVE)
    @Documentation(
            name = "Message has a Poll",
            description = "Whether the message has a poll attached to it.",
            examples = "if event-message has a poll:",
            since = "5.0.0-a1"
    )
    public static boolean hasPoll(Message m) {
        return m.getPoll() != null;
    }
}
