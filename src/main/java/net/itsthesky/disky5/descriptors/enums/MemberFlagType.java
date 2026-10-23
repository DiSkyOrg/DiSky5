package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.entities.Member;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptEnum(name = "memberflag", jda = Member.MemberFlag.class)
@Documentation(
    name = "Member Flag",
    description = "A guild-member flag (e.g. did rejoin, completed onboarding, bypasses verification).",
    since = "5.0.0-a1")
public final class MemberFlagType {
}
