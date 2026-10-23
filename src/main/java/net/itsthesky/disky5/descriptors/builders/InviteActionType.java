package net.itsthesky.disky5.descriptors.builders;

import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.channel.attribute.IInviteContainer;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.InviteAction;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "inviteaction", jda = InviteAction.class)
@Documentation(
    name = "Invite Builder",
    description = {"An invite that does not exist yet: configure it, then create it with 'the build result of'.",
        "Nothing is sent to Discord until then, so setting its properties costs no request."},
    since = "5.0.0-a1")
public final class InviteActionType {

    private InviteActionType() {}

    @New("[a] new invite builder (in|for) [the] [channel] %guildchannel%")
    @Documentation(
        name = "New Invite Builder",
        description = "Creates an invite builder for a channel. The invite is only created on Discord "
            + "once you read 'the build result of' it, which requires 'await'.",
        examples = {"set {_b} to a new invite builder in event-channel",
            "set invite max uses of {_b} to 10",
            "set invite max age of {_b} to 3600",
            "await set {_invite} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static InviteAction newInviteBuilder(GuildChannel channel) {
        if (!(channel instanceof IInviteContainer container))
            throw new IllegalArgumentException("the channel '" + channel.getName()
                + "' cannot hold invites");
        return container.createInvite();
    }

    @Build
    @Documentation(
        name = "Built Invite",
        description = {"Creates the invite on Discord and returns it. Requires 'await'.",
            "Building the same builder twice creates two invites."},
        examples = {"await set {_invite} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static RestAction<Invite> build(InviteAction action) {
        return action;
    }

    @Property.Setter("invite max uses")
    @Documentation(
        name = "Invite Builder Max Uses",
        description = "How many times the invite being built may be used before it expires (0 = unlimited).",
        examples = {"set invite max uses of {_b} to 1"},
        since = "5.0.0-a1")
    public static void setInviteMaxUses(InviteAction action, @Nullable Number value) {
        action.setMaxUses(value == null ? null : value.intValue());
    }

    @Property.Setter("invite max age")
    @Documentation(
        name = "Invite Builder Max Age",
        description = "How long the invite being built stays valid, in seconds (0 = never expires).",
        examples = {"set invite max age of {_b} to 86400"},
        since = "5.0.0-a1")
    public static void setInviteMaxAge(InviteAction action, @Nullable Number value) {
        action.setMaxAge(value == null ? null : value.intValue());
    }

    @Property.Setter("invite temporary")
    @Documentation(
        name = "Invite Builder Temporary",
        description = "Whether members joining through the invite being built are kicked once they "
            + "disconnect, unless a role is assigned to them first.",
        examples = {"set invite temporary of {_b} to true"},
        since = "5.0.0-a1")
    public static void setInviteTemporary(InviteAction action, @Nullable Boolean value) {
        action.setTemporary(value);
    }

    @Property.Setter("invite unique")
    @Documentation(
        name = "Invite Builder Unique",
        description = "Whether a brand new invite is created instead of reusing an existing similar one.",
        examples = {"set invite unique of {_b} to true"},
        since = "5.0.0-a1")
    public static void setInviteUnique(InviteAction action, @Nullable Boolean value) {
        action.setUnique(value);
    }
}
