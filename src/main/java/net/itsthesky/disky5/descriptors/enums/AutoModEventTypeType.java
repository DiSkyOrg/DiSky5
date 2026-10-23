package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.entities.automod.AutoModEventType;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptEnum(name = "automodeventtype", jda = AutoModEventType.class)
@Documentation(
    name = "AutoMod Event Type",
    description = "When an automod rule is checked: on a message being sent or on a member profile update.",
    examples = "if automod event type of {_rule} is message send:",
    since = "5.0.0-a2")
public final class AutoModEventTypeType {
}
