package net.itsthesky.disky5.events.member;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfDeafenEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Member Self Deafen Event",
        patterns = {"member [self] [un]deafen[ed]"},
        jda = GuildVoiceSelfDeafenEvent.class
)
@Documentation(
        name = "Member Self Deafen Event",
        description = "Fired when a member deafens or undeafens themselves in a voice channel.",
        examples = {
            "on member deafen:",
            "\tif member deafen state is true:",
            "\t\tbroadcast \"%event-member% deafened themselves\"",
            "\telse:",
            "\t\tbroadcast \"%event-member% undeafened themselves\""
        },
        since = "5.0.0-a1"
)
public final class EvtMemberSelfDeafen {

    @EventValue
    public static Guild guild(GuildVoiceSelfDeafenEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static Member member(GuildVoiceSelfDeafenEvent event) {
        return event.getMember();
    }

    @EventExpression("[member] deafen[ed] state")
    public static Boolean deafenState(GuildVoiceSelfDeafenEvent event) {
        return event.isSelfDeafened();
    }

    @EventExpression("(old|past|previous) [member] deafen[ed] state")
    public static Boolean oldDeafenState(GuildVoiceSelfDeafenEvent event) {
        return !event.isSelfDeafened();
    }
}
