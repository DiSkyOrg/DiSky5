package net.itsthesky.disky5.descriptors.automod;

import net.dv8tion.jda.api.entities.automod.AutoModRule;
import net.dv8tion.jda.api.entities.automod.build.AbstractKeywordTriggerConfig;
import net.dv8tion.jda.api.entities.automod.build.CustomKeywordTriggerConfig;
import net.dv8tion.jda.api.entities.automod.build.MentionSpamTriggerConfig;
import net.dv8tion.jda.api.entities.automod.build.PresetKeywordTriggerConfig;
import net.dv8tion.jda.api.entities.automod.build.TriggerConfig;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.Arrays;

@SkriptType(name = "triggerconfig", jda = TriggerConfig.class)
@Documentation(
    name = "AutoMod Trigger Config",
    description = {"What an automod rule looks for: custom keywords or regex patterns, a preset keyword list, "
        + "mention spam or generic spam.",
        "Create one with the 'new automod ... trigger' expressions, tune it with its properties, then give it "
        + "to an automod rule builder (or set 'trigger config of' an existing rule)."},
    since = "5.0.0-a2")
public final class TriggerConfigType {

    private TriggerConfigType() {}

    @New("[a] new automod keyword trigger [with [the] keywords %strings%]")
    @Documentation(
        name = "New AutoMod Keyword Trigger",
        description = {"Matches messages containing one of the keywords. Keywords accept the wildcards "
            + "'*' at the start, the end or both (\"*bad*\").",
            "A keyword trigger needs at least one keyword or one pattern when the rule is built."},
        examples = {"set {_t} to a new automod keyword trigger with the keywords \"badword\", \"*spoiler*\""},
        since = "5.0.0-a2")
    public static TriggerConfig newKeywordTrigger(String[] keywords) {
        return TriggerConfig.keywordFilter(keywords == null ? new String[0] : keywords);
    }

    @New("[a] new automod pattern trigger with [the] [regex] patterns %strings%")
    @Documentation(
        name = "New AutoMod Pattern Trigger",
        description = "Matches messages against regex patterns (Rust flavored, 10 patterns of 260 characters max).",
        examples = {"set {_t} to a new automod pattern trigger with the patterns \"discord[.]gg/.+\""},
        since = "5.0.0-a2")
    public static TriggerConfig newPatternTrigger(String[] patterns) {
        return TriggerConfig.patternFilter(patterns);
    }

    @New("[a] new automod preset trigger with [the] [presets] %keywordpresets%")
    @Documentation(
        name = "New AutoMod Preset Trigger",
        description = "Matches messages against Discord's maintained keyword lists. Only one rule of this kind per guild.",
        examples = {"set {_t} to a new automod preset trigger with the presets profanity and slurs"},
        since = "5.0.0-a2")
    public static TriggerConfig newPresetTrigger(AutoModRule.KeywordPreset[] presets) {
        return TriggerConfig.presetKeywordFilter(presets);
    }

    @New("[a] new automod mention spam trigger with [a] [mention] limit [of] %number%")
    @Documentation(
        name = "New AutoMod Mention Spam Trigger",
        description = "Matches messages mentioning more unique users and roles than the limit (50 max). Only one rule of this kind per guild.",
        examples = {"set {_t} to a new automod mention spam trigger with a limit of 5"},
        since = "5.0.0-a2")
    public static TriggerConfig newMentionSpamTrigger(Number limit) {
        return TriggerConfig.mentionSpam(limit.intValue());
    }

    @New("[a] new automod spam trigger")
    @Documentation(
        name = "New AutoMod Spam Trigger",
        description = "Matches messages Discord considers spam. Only one rule of this kind per guild.",
        examples = {"set {_t} to a new automod spam trigger"},
        since = "5.0.0-a2")
    public static TriggerConfig newSpamTrigger() {
        return TriggerConfig.antiSpam();
    }

    @Property.Setter("filtered keywords")
    @Documentation(
        name = "AutoMod Trigger Keywords",
        description = "Replaces the keywords of a keyword trigger.",
        examples = {"set filtered keywords of {_t} to \"a\" and \"b\""},
        since = "5.0.0-a2")
    public static void setKeywords(TriggerConfig config, String[] keywords) {
        custom(config).setKeywords(Arrays.asList(keywords));
    }

    @Property.Adder("filtered keywords")
    public static void addKeyword(TriggerConfig config, String keyword) {
        custom(config).addKeywords(keyword);
    }

    @Property.Setter("filtered patterns")
    @Documentation(
        name = "AutoMod Trigger Patterns",
        description = "Replaces the regex patterns of a keyword trigger.",
        examples = {"set filtered patterns of {_t} to \"^a+$\""},
        since = "5.0.0-a2")
    public static void setPatterns(TriggerConfig config, String[] patterns) {
        custom(config).setPatterns(Arrays.asList(patterns));
    }

    @Property.Adder("filtered patterns")
    public static void addPattern(TriggerConfig config, String pattern) {
        custom(config).addPatterns(pattern);
    }

    @Property.Setter("allow list")
    @Documentation(
        name = "AutoMod Trigger Allow List",
        description = "Replaces the words that must never trigger a keyword or preset trigger.",
        examples = {"set allow list of {_t} to \"classic\""},
        since = "5.0.0-a2")
    public static void setAllowList(TriggerConfig config, String[] words) {
        allowing(config).setAllowList(Arrays.asList(words));
    }

    @Property.Adder("allow list")
    public static void addAllowList(TriggerConfig config, String word) {
        allowing(config).addAllowList(word);
    }

    @Property.Setter("presets")
    @Documentation(
        name = "AutoMod Trigger Presets",
        description = "The keyword presets of a preset trigger.",
        examples = {"add slurs to presets of {_t}"},
        since = "5.0.0-a2")
    public static void setPresets(TriggerConfig config, AutoModRule.KeywordPreset[] presets) {
        PresetKeywordTriggerConfig preset = preset(config);
        preset.disablePresets(AutoModRule.KeywordPreset.values());
        preset.enablePresets(presets);
    }

    @Property.Adder("presets")
    public static void addPreset(TriggerConfig config, AutoModRule.KeywordPreset value) {
        preset(config).enablePresets(value);
    }

    @Property.Remover("presets")
    public static void removePreset(TriggerConfig config, AutoModRule.KeywordPreset value) {
        preset(config).disablePresets(value);
    }

    @Property.Setter("mention limit")
    @Documentation(
        name = "AutoMod Trigger Mention Limit",
        description = "The number of mentions above which a mention spam trigger fires (1 to 50).",
        examples = {"set mention limit of {_t} to 10"},
        since = "5.0.0-a2")
    public static void setMentionLimit(TriggerConfig config, Number limit) {
        mention(config).setMentionLimit(limit.intValue());
    }

    @Property.Setter("mention raid protection")
    @Documentation(
        name = "AutoMod Trigger Mention Raid Protection",
        description = "Whether a mention spam trigger also detects mention raids (many members mentioned in a short time).",
        examples = {"set mention raid protection of {_t} to true"},
        since = "5.0.0-a2")
    public static void setMentionRaidProtection(TriggerConfig config, Boolean enabled) {
        mention(config).setMentionRaidProtectionEnabled(enabled);
    }

    private static CustomKeywordTriggerConfig custom(TriggerConfig config) {
        if (config instanceof CustomKeywordTriggerConfig custom)
            return custom;
        throw wrong(config, "keyword or pattern");
    }

    private static AbstractKeywordTriggerConfig<?> allowing(TriggerConfig config) {
        if (config instanceof AbstractKeywordTriggerConfig<?> keyword)
            return keyword;
        throw wrong(config, "keyword, pattern or preset");
    }

    private static PresetKeywordTriggerConfig preset(TriggerConfig config) {
        if (config instanceof PresetKeywordTriggerConfig preset)
            return preset;
        throw wrong(config, "preset");
    }

    private static MentionSpamTriggerConfig mention(TriggerConfig config) {
        if (config instanceof MentionSpamTriggerConfig mention)
            return mention;
        throw wrong(config, "mention spam");
    }

    private static IllegalArgumentException wrong(TriggerConfig config, String expected) {
        return new IllegalArgumentException("this property only applies to a " + expected + " trigger, not a "
            + config.getType().name().toLowerCase().replace('_', ' ') + " one");
    }
}
