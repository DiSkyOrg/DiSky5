package net.itsthesky.disky5.descriptors.automod;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.automod.AutoModEventType;
import net.dv8tion.jda.api.entities.automod.AutoModResponse;
import net.dv8tion.jda.api.entities.automod.AutoModRule;
import net.dv8tion.jda.api.entities.automod.build.TriggerConfig;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.automod.AutoModRuleBuilder;

import java.util.Arrays;

@SkriptType(name = "automodrulebuilder", jda = AutoModRuleBuilder.class)
@Documentation(
    name = "AutoMod Rule Builder",
    description = {"An automod rule that does not exist yet: configure it, then create it with 'the build result of'.",
        "Nothing is sent to Discord until then. A rule needs at least one response."},
    since = "5.0.0-a2")
public final class AutoModRuleBuilderType {

    private AutoModRuleBuilderType() {}

    @New("[a] new automod rule builder [(named|with [the] name)] %string% in [the] [guild] %guild% "
        + "(for|checking) messages (with|using) [the] [trigger] %triggerconfig%")
    @Documentation(
        name = "New AutoMod Message Rule Builder",
        description = "Creates a builder for a rule checked when a message is sent.",
        examples = {"set {_t} to a new automod keyword trigger with the keywords \"badword\"",
            "set {_b} to a new automod rule builder named \"No bad words\" in event-guild for messages using {_t}",
            "add a new automod response blocking the message to automod responses of {_b}",
            "await set {_rule} to the build result of {_b}"},
        since = "5.0.0-a2")
    public static AutoModRuleBuilder newMessageBuilder(String name, Guild guild, TriggerConfig trigger) {
        return new AutoModRuleBuilder(guild, AutoModEventType.MESSAGE_SEND, name, trigger);
    }

    @New("[a] new automod rule builder [(named|with [the] name)] %string% in [the] [guild] %guild% "
        + "(for|checking) member profiles (with|using) [the] [trigger] %triggerconfig%")
    @Documentation(
        name = "New AutoMod Member Profile Rule Builder",
        description = {"Creates a builder for a rule checked when a member updates their profile (username, nickname, bio).",
            "Only keyword triggers are accepted, and the block-member-interaction response is added for you."},
        examples = {"set {_b} to a new automod rule builder named \"Profiles\" in event-guild for member profiles using {_t}"},
        since = "5.0.0-a2")
    public static AutoModRuleBuilder newProfileBuilder(String name, Guild guild, TriggerConfig trigger) {
        return new AutoModRuleBuilder(guild, AutoModEventType.MEMBER_UPDATE, name, trigger);
    }

    @Build
    @Documentation(
        name = "Built AutoMod Rule",
        description = {"Creates the rule on Discord and returns it. Requires 'await'.",
            "Building the same builder twice creates two rules (Discord limits some trigger types to one per guild)."},
        examples = {"await set {_rule} to the build result of {_b}"},
        since = "5.0.0-a2")
    public static RestAction<AutoModRule> build(AutoModRuleBuilder builder) {
        builder.toData();
        return builder.guild().createAutoModRule(builder);
    }

    @Property.Setter("enabled")
    public static void setEnabled(AutoModRuleBuilder builder, Boolean enabled) {
        builder.setEnabled(enabled);
    }

    @Property.Setter("exempt roles")
    public static void setExemptRoles(AutoModRuleBuilder builder, Role[] roles) {
        builder.setExemptRoles(Arrays.asList(roles));
    }

    @Property.Adder("exempt roles")
    public static void addExemptRole(AutoModRuleBuilder builder, Role role) {
        builder.addExemptRoles(role);
    }

    @Property.Setter("exempt channels")
    public static void setExemptChannels(AutoModRuleBuilder builder, GuildChannel[] channels) {
        builder.setExemptChannels(Arrays.asList(channels));
    }

    @Property.Adder("exempt channels")
    public static void addExemptChannel(AutoModRuleBuilder builder, GuildChannel channel) {
        builder.addExemptChannels(channel);
    }

    @Property.Setter("automod responses")
    public static void setResponses(AutoModRuleBuilder builder, AutoModResponse[] responses) {
        builder.setResponses(Arrays.asList(responses));
    }

    @Property.Adder("automod responses")
    public static void addResponse(AutoModRuleBuilder builder, AutoModResponse response) {
        builder.putResponses(response);
    }

    @Property.Setter("trigger config")
    public static void setTriggerConfig(AutoModRuleBuilder builder, TriggerConfig config) {
        builder.setTriggerConfig(config);
    }
}
