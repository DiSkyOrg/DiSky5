package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.entities.automod.AutoModRule;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptEnum(name = "keywordpreset", jda = AutoModRule.KeywordPreset.class)
@Documentation(
    name = "AutoMod Keyword Preset",
    description = "A keyword list maintained by Discord: profanity, sexual content or slurs.",
    examples = "set {_t} to a new automod preset trigger with the presets profanity and slurs",
    since = "5.0.0-a2")
public final class KeywordPresetType {
}
