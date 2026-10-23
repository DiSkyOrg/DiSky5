package net.itsthesky.disky5.descriptors.automod;

import ch.njol.skript.util.Timespan;
import net.dv8tion.jda.api.entities.automod.AutoModResponse;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

@SkriptType(name = "automodresponse", jda = AutoModResponse.class)
@Documentation(
    name = "AutoMod Response",
    description = {"An action an automod rule takes when it triggers: block the message, send an alert, "
        + "time the member out or block the member's interactions.",
        "Responses are immutable values; build one with the 'new automod response' expressions."},
    since = "5.0.0-a2")
public final class AutoModResponseType {

    private AutoModResponseType() {}

    @New("[a] new automod response blocking [the] message [with [the] [custom] message %-string%]")
    @Documentation(
        name = "New AutoMod Block Message Response",
        description = {"Blocks the offending message. The optional custom message (150 characters max) is shown "
            + "to the member whose message was blocked."},
        examples = {"set {_r} to a new automod response blocking the message with the custom message \"No spam here\""},
        since = "5.0.0-a2")
    public static AutoModResponse newBlockMessage(@Nullable String message) {
        return AutoModResponse.blockMessage(message);
    }

    @New("[a] new automod response sending [an] alert to [the] [channel] %standardguildmessagechannel%")
    @Documentation(
        name = "New AutoMod Alert Response",
        description = "Sends an alert message to the given channel when the rule triggers.",
        examples = {"set {_r} to a new automod response sending an alert to the channel event-channel"},
        since = "5.0.0-a2")
    public static AutoModResponse newSendAlert(StandardGuildMessageChannel channel) {
        return AutoModResponse.sendAlert(channel);
    }

    @New("[a] new automod response timing [the] member out for %timespan%")
    @Documentation(
        name = "New AutoMod Timeout Response",
        description = "Times the member out for the given duration (keyword and mention spam rules only).",
        examples = {"set {_r} to a new automod response timing the member out for 10 minutes"},
        since = "5.0.0-a2")
    public static AutoModResponse newTimeout(Timespan duration) {
        Duration value = Duration.ofMillis(duration.getAs(Timespan.TimePeriod.MILLISECOND));
        if (value.isZero() || value.isNegative())
            throw new IllegalArgumentException("the timeout duration must be greater than 0");
        return AutoModResponse.timeoutMember(value);
    }

    @New("[a] new automod response blocking [the] member interaction[s]")
    @Documentation(
        name = "New AutoMod Block Member Interaction Response",
        description = "Blocks the member from interacting with the server (member profile rules only).",
        examples = {"set {_r} to a new automod response blocking the member interactions"},
        since = "5.0.0-a2")
    public static AutoModResponse newBlockInteraction() {
        return AutoModResponse.blockMemberInteraction();
    }

    @Property("response type")
    @Documentation(
        name = "AutoMod Response Type",
        description = "The kind of action of the response.",
        examples = {"if response type of {_r} is timeout:"},
        since = "5.0.0-a2")
    public static AutoModResponse.Type responseType(AutoModResponse response) {
        return response.getType();
    }

    @Property("response channel")
    @Documentation(
        name = "AutoMod Response Channel",
        description = "The channel the alert is sent to, or nothing if the response is not an alert.",
        examples = {"reply with response channel of {_r}"},
        since = "5.0.0-a2")
    public static @Nullable Channel responseChannel(AutoModResponse response) {
        return response.getChannel();
    }

    @Property("response message")
    @Documentation(
        name = "AutoMod Response Message",
        description = "The custom message shown when a message is blocked, or nothing if there is none.",
        examples = {"reply with response message of {_r}"},
        since = "5.0.0-a2")
    public static @Nullable String responseMessage(AutoModResponse response) {
        return response.getCustomMessage();
    }

    @Property("response timeout")
    @Documentation(
        name = "AutoMod Response Timeout",
        description = "The timeout duration of the response, or nothing if the response is not a timeout.",
        examples = {"reply with response timeout of {_r}"},
        since = "5.0.0-a2")
    public static @Nullable Timespan responseTimeout(AutoModResponse response) {
        Duration duration = response.getTimeoutDuration();
        return duration == null ? null : new Timespan(duration.toMillis());
    }
}
