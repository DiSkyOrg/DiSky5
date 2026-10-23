package net.itsthesky.disky5.core;

import net.itsthesky.disky5.annotations.SharedProperty;
import net.itsthesky.disky5.annotations.docs.Documentation;

public final class SharedProperties {
    private SharedProperties() {}

    @SharedProperty("name")
    @Documentation(
            name = "Discord Name",
            description = {
                "The name of a Discord entity: a user's or member's account name, a guild's name, a "
                    + "role/channel name, a bot's define-name. Registered on Skript's native "
                    + "Property.NAME, so the idiomatic \"name of %x%\" works without colliding. For a "
                    + "member's server nickname or display name, use \"effective name\"."
            },
            examples = "broadcast \"%name of event-member%\"",
            since = "5.0.0-a1"
    )
    public static void discordName() {
    }

    @SharedProperty("color")
    @Documentation(
            name = "Discord Color",
            description = {
                "The color of a Discord entity: a role's color, an embed's side bar, or the color a "
                    + "role builder will be created with. On a Skript that has the native Property.COLOR, "
                    + "\"color of %x%\" is the same expression that colors a block or a firework, "
                    + "dispatched per runtime type; on one that does not, it is \"discord color of %x%\".",
                "Write-only on a role builder and on an embed builder: JDA exposes no getter on "
                    + "either, so reading them gives <none>."
            },
            examples = {"set color of event-role to orange", "set color of {_embed} to red"},
            since = "5.0.0-a1"
    )
    public static void discordColor() {
    }

    @SharedProperty("style")
    @Documentation(
            name = "Style",
            description = "The style of a button (its color) or of a text input (short or paragraph). "
                + "Co-owned with Skript's native Property.STYLE.",
            examples = "set style of {_button} to danger",
            since = "5.0.0-a1"
    )
    public static void style() {
    }

    @SharedProperty("title")
    @Documentation(
            name = "Title",
            description = "The title of an embed. Co-owned with Skript's native Property.TITLE.",
            examples = "set title of {_embed} to \"Hello\"",
            since = "5.0.0-a1"
    )
    public static void title() {
    }

    @SharedProperty("value")
    @Documentation(
            name = "Value",
            description = "The value of an embed field. Co-owned with Skript's native "
                + "Property.TYPED_VALUE, the same property that reads the value of a sign line.",
            examples = "send \"%value of loop-value%\"",
            since = "5.0.0-a1"
    )
    public static void value() {
    }

    @SharedProperty("nickname")
    @Documentation(
            name = "Nickname",
            description = {
                "A guild member's nickname, which is their display name on that guild. Co-owned with "
                    + "Skript's native Property.DISPLAY_NAME, so \"nickname of\", \"display name of\" "
                    + "and \"custom name of\" all reach it.",
                "For the name that falls back to the account name when no nickname is set, use "
                    + "\"effective name\"."
            },
            examples = "set nickname of event-member to \"Sky\"",
            since = "5.0.0-a1"
    )
    public static void nickname() {
    }

    @SharedProperty("online status")
    @Documentation(
            name = "Online Status",
            description = {
                "The online status of a Discord entity. Read-only on a member (the presence Discord "
                    + "reports: online, idle, do not disturb, offline...). On a bot it is the live "
                    + "presence and can be set (online / idle / dnd / invisible). One syntax, "
                    + "dispatched per type — \"online status of %member/bot%\"."
            },
            examples = {
                "reply with online status of event-member",
                "set online status of {_bot} to \"dnd\""
            },
            since = "5.0.0-a1"
    )
    public static void onlineStatus() {
    }

    @SharedProperty("jump url")
    @Documentation(
            name = "Jump URL",
            description = {
                "The jump-to URL of a message or a guild channel. Clicking it in the Discord client "
                    + "jumps straight to the message/channel. Co-owned across both types, so "
                    + "\"jump url of %message/channel%\" works on either."
            },
            examples = "reply with jump url of event-message",
            since = "5.0.0-a1"
    )
    public static void jumpUrl() {
    }

    @SharedProperty("avatar url")
    @Documentation(
            name = "Avatar URL",
            description = {
                "The avatar/icon URL of a Discord entity: a user/member's avatar, a guild's icon, a "
                    + "sticker's image, an application's icon... Co-owned across every type that has one, "
                    + "so \"avatar url of %x%\" works on any of them. Always a string (the image URL)."
            },
            examples = "reply with avatar url of event-user",
            since = "5.0.0-a1"
    )
    public static void avatarUrl() {
    }

    @SharedProperty("guild")
    @Documentation(
            name = "Guild Of",
            description = {
                "The guild a Discord entity belongs to: a member's, message's, role's, channel's, "
                    + "webhook's, sticker's, emoji's or scheduled event's guild. Co-owned across every "
                    + "such type, so \"guild of %x%\" works on any of them (null when the entity is not "
                    + "tied to a guild, e.g. a message from DMs)."
            },
            examples = "reply with name of guild of event-message",
            since = "5.0.0-a1"
    )
    public static void guild() {
    }
}
