package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.automod.AutoModExecution;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@SkriptType(name = "automod", jda = AutoModExecution.class)
public final class AutoModExecutionType {

    @Property("automod message content")
    @Documentation(
            name = "AutoMod Message Content",
            description = "The message content that triggered the AutoMod execution.",
            examples = "reply with automod message content of event-automod",
            since = "5.0.0-a1"
    )
    public static @Nullable String messageContent(AutoModExecution e) {
        return e.getContent();
    }

    @Property("matched content")
    @Documentation(
            name = "AutoMod Matched Content",
            description = "The substring of the message that matched the AutoMod rule, or null if not provided.",
            examples = "reply with matched content of event-automod",
            since = "5.0.0-a1"
    )
    public static @Nullable String matchedContent(AutoModExecution e) {
        return e.getMatchedContent();
    }

    @Property("matched keyword")
    @Documentation(
            name = "AutoMod Matched Keyword",
            description = "The keyword that matched the AutoMod rule, or null if not provided.",
            examples = "reply with matched keyword of event-automod",
            since = "5.0.0-a1"
    )
    public static @Nullable String matchedKeyword(AutoModExecution e) {
        return e.getMatchedKeyword();
    }

    @Property("automod rule id")
    @Documentation(
            name = "AutoMod Rule ID",
            description = "The ID of the AutoMod rule that was triggered.",
            examples = "reply with automod rule id of event-automod",
            since = "5.0.0-a1"
    )
    public static String automodRuleId(AutoModExecution e) {
        return e.getRuleId();
    }

    @Property("automod action type")
    @Documentation(
            name = "AutoMod Action Type",
            description = "The trigger type of the AutoMod rule that was triggered (keyword, spam, mention spam...).",
            examples = "reply with automod action type of event-automod",
            since = "5.0.0-a1"
    )
    public static String automodActionType(AutoModExecution e) {
        return e.getTriggerType().name().toLowerCase(Locale.ROOT).replace('_', ' ');
    }

    @Property("alert message id")
    @Documentation(
            name = "AutoMod Alert Message ID",
            description = "The ID of the alert message sent to moderators, or null if no alert was sent.",
            examples = "reply with alert message id of event-automod",
            since = "5.0.0-a1"
    )
    public static String alertMessageId(AutoModExecution e) {
        return e.getAlertMessageId();
    }
}
