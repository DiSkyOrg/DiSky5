package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Date;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.User;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "invite", jda = Invite.class)
public final class InviteType {

    @Property("invite code")
    @Documentation(
            name = "Invite Code",
            description = "The code of the invite (the part after discord.gg/).",
            examples = "reply with invite code of event-invite",
            since = "5.0.0-a1"
    )
    public static String inviteCode(Invite i) {
        return i.getCode();
    }

    @Property("invite url")
    @Documentation(
            name = "Invite URL",
            description = "The full URL of the invite.",
            examples = "reply with invite url of event-invite",
            since = "5.0.0-a1"
    )
    public static String inviteUrl(Invite i) {
        return i.getUrl();
    }

    @Property("invite inviter")
    @Documentation(
            name = "Invite Inviter",
            description = "The user who created the invite, or null if it is unknown.",
            examples = "reply with invite inviter of event-invite",
            since = "5.0.0-a1"
    )
    public static @Nullable User inviter(Invite i) {
        return i.getInviter();
    }

    @Property("invite max age")
    @Documentation(
            name = "Invite Max Age",
            description = "The maximum age (in seconds) this invite can be used for.",
            examples = "reply with invite max age of event-invite",
            since = "5.0.0-a1"
    )
    public static Number inviteMaxAge(Invite i) {
        return i.getMaxAge();
    }

    @Property("invite max uses")
    @Documentation(
            name = "Invite Max Uses",
            description = "The maximum amount of times this invite can be used.",
            examples = "reply with invite max uses of event-invite",
            since = "5.0.0-a1"
    )
    public static Number inviteMaxUses(Invite i) {
        return i.getMaxUses();
    }

    @Property("invite uses")
    @Documentation(
            name = "Invite Uses",
            description = "The amount of times this invite has already been used.",
            examples = "reply with invite uses of event-invite",
            since = "5.0.0-a1"
    )
    public static Number inviteUses(Invite i) {
        return i.getUses();
    }

    @Property("invite creation date")
    @Documentation(
            name = "Invite Creation Date",
            description = "The date at which the invite was created.",
            examples = "reply with invite creation date of event-invite",
            since = "5.0.0-a1"
    )
    public static Date inviteCreationDate(Invite i) {
        return Conversions.date(i.getTimeCreated());
    }
}
