package net.itsthesky.disky5.descriptors.automod;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.automod.AutoModEventType;
import net.dv8tion.jda.api.entities.automod.AutoModResponse;
import net.dv8tion.jda.api.entities.automod.AutoModRule;
import net.dv8tion.jda.api.entities.automod.AutoModTriggerType;
import net.dv8tion.jda.api.entities.automod.build.TriggerConfig;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.descriptors.SnowflakeType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SkriptType(name = "automodrule", jda = AutoModRule.class, parent = SnowflakeType.class)
@Documentation(
    name = "AutoMod Rule",
    description = {"A rule of Discord's AutoMod: what it looks for (its trigger), when it is checked and what it does.",
        "Read the rules of a guild with 'automod rules of %guild%' (requires await), then edit them through "
        + "their properties; every change is sent to Discord immediately."},
    since = "5.0.0-a2")
public final class AutoModRuleType {

    private AutoModRuleType() {}

    @Property(value = "name", shared = true)
    public static String discordName(AutoModRule rule) {
        return rule.getName();
    }

    @Property.Setter(value = "name", shared = true)
    public static RestAction<?> setDiscordName(AutoModRule rule, String name) {
        return rule.getManager().setName(name);
    }

    @Property(value = "guild", shared = true)
    public static Guild guild(AutoModRule rule) {
        return rule.getGuild();
    }

    @Property("creator")
    @Documentation(
        name = "AutoMod Rule Creator",
        description = "The member who created the rule, or nothing if they left the guild or are not cached.",
        examples = {"reply with creator of {_rule}"},
        since = "5.0.0-a2")
    public static @Nullable Member creator(AutoModRule rule) {
        return rule.getGuild().getMemberById(rule.getCreatorIdLong());
    }

    @Property("creator id")
    @Documentation(
        name = "AutoMod Rule Creator ID",
        description = "The id of the user who created the rule.",
        examples = {"reply with creator id of {_rule}"},
        since = "5.0.0-a2")
    public static String creatorId(AutoModRule rule) {
        return rule.getCreatorId();
    }

    @Property("enabled")
    @Documentation(
        name = "AutoMod Rule Enabled",
        description = "Whether the rule is currently enforced.",
        examples = {"set enabled of {_rule} to false"},
        since = "5.0.0-a2")
    public static Boolean enabled(AutoModRule rule) {
        return rule.isEnabled();
    }

    @Property.Setter("enabled")
    public static RestAction<?> setEnabled(AutoModRule rule, Boolean enabled) {
        return rule.getManager().setEnabled(enabled);
    }

    @Property("automod event type")
    @Documentation(
        name = "AutoMod Rule Event Type",
        description = "When the rule is checked: on a sent message or on a member profile update.",
        examples = {"reply with automod event type of {_rule}"},
        since = "5.0.0-a2")
    public static AutoModEventType eventType(AutoModRule rule) {
        return rule.getEventType();
    }

    @Property("automod trigger type")
    @Documentation(
        name = "AutoMod Rule Trigger Type",
        description = "What the rule looks for (keyword, spam, keyword preset, mention spam, member profile keyword).",
        examples = {"reply with automod trigger type of {_rule}"},
        since = "5.0.0-a2")
    public static AutoModTriggerType triggerType(AutoModRule rule) {
        return rule.getTriggerType();
    }

    @Property("exempt roles")
    @Documentation(
        name = "AutoMod Rule Exempt Roles",
        description = "The roles whose members are ignored by the rule (20 max).",
        examples = {"add role with id \"123\" to exempt roles of {_rule}"},
        since = "5.0.0-a2")
    public static List<Role> exemptRoles(AutoModRule rule) {
        return rule.getExemptRoles();
    }

    @Property.Setter("exempt roles")
    public static RestAction<?> setExemptRoles(AutoModRule rule, Role[] roles) {
        return rule.getManager().setExemptRoles(roles);
    }

    @Property.Adder("exempt roles")
    public static RestAction<?> addExemptRoles(AutoModRule rule, Role[] added) {
        List<Role> roles = new ArrayList<>(rule.getExemptRoles());
        for (Role role : added)
            if (!roles.contains(role))
                roles.add(role);
        return rule.getManager().setExemptRoles(roles);
    }

    @Property.Remover("exempt roles")
    public static RestAction<?> removeExemptRoles(AutoModRule rule, Role[] removed) {
        List<Role> roles = new ArrayList<>(rule.getExemptRoles());
        roles.removeAll(List.of(removed));
        return rule.getManager().setExemptRoles(roles);
    }

    @Property("exempt channels")
    @Documentation(
        name = "AutoMod Rule Exempt Channels",
        description = "The channels where the rule does not apply (50 max).",
        examples = {"add event-channel to exempt channels of {_rule}"},
        since = "5.0.0-a2")
    public static List<GuildChannel> exemptChannels(AutoModRule rule) {
        return rule.getExemptChannels();
    }

    @Property.Setter("exempt channels")
    public static RestAction<?> setExemptChannels(AutoModRule rule, GuildChannel[] channels) {
        return rule.getManager().setExemptChannels(channels);
    }

    @Property.Adder("exempt channels")
    public static RestAction<?> addExemptChannels(AutoModRule rule, GuildChannel[] added) {
        List<GuildChannel> channels = new ArrayList<>(rule.getExemptChannels());
        for (GuildChannel channel : added)
            if (!channels.contains(channel))
                channels.add(channel);
        return rule.getManager().setExemptChannels(channels);
    }

    @Property.Remover("exempt channels")
    public static RestAction<?> removeExemptChannels(AutoModRule rule, GuildChannel[] removed) {
        List<GuildChannel> channels = new ArrayList<>(rule.getExemptChannels());
        channels.removeAll(List.of(removed));
        return rule.getManager().setExemptChannels(channels);
    }

    @Property("automod responses")
    @Documentation(
        name = "AutoMod Rule Responses",
        description = "The actions the rule takes when it triggers. Adding a response replaces the one of the same type.",
        examples = {"add a new automod response blocking the message to automod responses of {_rule}"},
        since = "5.0.0-a2")
    public static List<AutoModResponse> responses(AutoModRule rule) {
        return rule.getActions();
    }

    @Property.Setter("automod responses")
    public static RestAction<?> setResponses(AutoModRule rule, AutoModResponse[] responses) {
        return rule.getManager().setResponses(responses);
    }

    @Property.Adder("automod responses")
    public static RestAction<?> addResponses(AutoModRule rule, AutoModResponse[] added) {
        List<AutoModResponse> responses = new ArrayList<>(rule.getActions());
        for (AutoModResponse response : added) {
            responses.removeIf(other -> other.getType() == response.getType());
            responses.add(response);
        }
        return rule.getManager().setResponses(responses);
    }

    @Property.Remover("automod responses")
    public static RestAction<?> removeResponses(AutoModRule rule, AutoModResponse[] removed) {
        List<AutoModResponse> responses = new ArrayList<>(rule.getActions());
        for (AutoModResponse response : removed)
            responses.removeIf(other -> other.getType() == response.getType());
        return rule.getManager().setResponses(responses);
    }

    @Property("filtered keywords")
    @Documentation(
        name = "AutoMod Rule Keywords",
        description = "The keywords of a keyword rule.",
        examples = {"reply with filtered keywords of {_rule}"},
        since = "5.0.0-a2")
    public static List<String> filteredKeywords(AutoModRule rule) {
        return rule.getFilteredKeywords();
    }

    @Property("filtered patterns")
    @Documentation(
        name = "AutoMod Rule Patterns",
        description = "The regex patterns of a keyword rule.",
        examples = {"reply with filtered patterns of {_rule}"},
        since = "5.0.0-a2")
    public static List<String> filteredPatterns(AutoModRule rule) {
        return rule.getFilteredRegex();
    }

    @Property("allow list")
    @Documentation(
        name = "AutoMod Rule Allow List",
        description = "The words that never trigger a keyword or preset rule.",
        examples = {"reply with allow list of {_rule}"},
        since = "5.0.0-a2")
    public static List<String> allowList(AutoModRule rule) {
        return rule.getAllowlist();
    }

    @Property("presets")
    @Documentation(
        name = "AutoMod Rule Presets",
        description = "The keyword presets of a preset rule.",
        examples = {"reply with presets of {_rule}"},
        since = "5.0.0-a2")
    public static List<AutoModRule.KeywordPreset> presets(AutoModRule rule) {
        return new ArrayList<>(rule.getFilteredPresets());
    }

    @Property("mention limit")
    @Documentation(
        name = "AutoMod Rule Mention Limit",
        description = "The mention count above which a mention spam rule fires.",
        examples = {"reply with mention limit of {_rule}"},
        since = "5.0.0-a2")
    public static Integer mentionLimit(AutoModRule rule) {
        return rule.getMentionLimit();
    }

    @Property("mention raid protection")
    @Documentation(
        name = "AutoMod Rule Mention Raid Protection",
        description = "Whether a mention spam rule also detects mention raids.",
        examples = {"reply with mention raid protection of {_rule}"},
        since = "5.0.0-a2")
    public static Boolean mentionRaidProtection(AutoModRule rule) {
        return rule.isMentionRaidProtectionEnabled();
    }

    @Property.Setter("trigger config")
    @Documentation(
        name = "AutoMod Rule Trigger Config",
        description = {"Replaces what the rule looks for. The trigger must be of the same type as the rule's current one.",
            "This property cannot be read: use the keyword, pattern, allow list, presets and mention properties."},
        examples = {"set trigger config of {_rule} to a new automod keyword trigger with the keywords \"spam\""},
        since = "5.0.0-a2")
    public static RestAction<?> setTriggerConfig(AutoModRule rule, TriggerConfig config) {
        return rule.getManager().setTriggerConfig(config);
    }
}
