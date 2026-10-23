package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.entities.automod.AutoModTriggerType;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptEnum(name = "automodtriggertype", jda = AutoModTriggerType.class)
@Documentation(
    name = "AutoMod Trigger Type",
    description = "What an automod rule looks for: keyword, spam, keyword preset, mention spam or member profile keyword.",
    examples = "if automod trigger type of {_rule} is mention spam:",
    since = "5.0.0-a2")
public final class AutoModTriggerTypeType {
}
