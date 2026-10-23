package net.itsthesky.disky5.descriptors.builders;

import ch.njol.skript.util.Date;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.ScheduledEventAction;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@SkriptType(name = "scheduledeventaction", jda = ScheduledEventAction.class)
@Documentation(
    name = "Scheduled Event Builder",
    description = {"A scheduled event that does not exist yet: configure it, then create it with "
        + "'the build result of'.",
        "Nothing is sent to Discord until then, so setting its properties costs no request."},
    since = "5.0.0-a1")
public final class ScheduledEventActionType {

    private ScheduledEventActionType() {}

    @New("[a] new scheduled[( |-)]event builder [(named|with [the] name)] %string% at %string% "
        + "starting [at] %date% [and] ending [at] %date% in [the] [guild] %guild%")
    @Documentation(
        name = "New External Scheduled Event Builder",
        description = "Creates a builder for an event happening outside of Discord. It needs both a "
            + "start and an end date, since nothing tells Discord when it is over.",
        examples = {"set {_b} to a new scheduled event builder named \"Concert\" at \"6 route of XXX\" "
            + "starting (1 hour after now) and ending (5 hours after now) in event-guild",
            "set description of {_b} to \"Come as you are\"",
            "await set {_event} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ScheduledEventAction newExternalEventBuilder(String name, String location,
                                                               Date start, Date end, Guild guild) {
        return guild.createScheduledEvent(name, location, offset(start), offset(end));
    }

    @New("[a] new scheduled[( |-)]event builder [(named|with [the] name)] %string% in [the] [channel] "
        + "%guildchannel% starting [at] %date%")
    @Documentation(
        name = "New Channel Scheduled Event Builder",
        description = {"Creates a builder for an event held in a channel. Discord ends it by itself, "
            + "so no end date is given.",
            "Only stage and voice channels can host a scheduled event."},
        examples = {"set {_b} to a new scheduled event builder named \"Let's Talk Together\" in "
            + "stage channel with id \"000\" starting (5 hours after now)",
            "await set {_event} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static ScheduledEventAction newChannelEventBuilder(String name, GuildChannel channel, Date start) {
        return channel.getGuild().createScheduledEvent(name, channel, offset(start));
    }

    @Build
    @Documentation(
        name = "Built Scheduled Event",
        description = {"Creates the scheduled event on Discord and returns it. Requires 'await'.",
            "Building the same builder twice creates two events."},
        examples = {"await set {_event} to the build result of {_b}"},
        since = "5.0.0-a1")
    public static RestAction<ScheduledEvent> build(ScheduledEventAction action) {
        return action;
    }

    @Property.Setter("description")
    @Documentation(
        name = "Scheduled Event Builder Description",
        description = "The description of the scheduled event being built.",
        examples = {"set description of {_b} to \"Bring your own drinks\""},
        since = "5.0.0-a1")
    public static void setDescription(ScheduledEventAction action, @Nullable String value) {
        action.setDescription(value);
    }

    @Property.Setter("start date")
    @Documentation(
        name = "Scheduled Event Builder Start Date",
        description = "Overrides the start date the builder was created with.",
        examples = {"set start date of {_b} to (2 hours after now)"},
        since = "5.0.0-a1")
    public static void setStartDate(ScheduledEventAction action, Date value) {
        if (value != null)
            action.setStartTime(offset(value));
    }

    @Property.Setter("end date")
    @Documentation(
        name = "Scheduled Event Builder End Date",
        description = "The end date of the scheduled event being built. External events only.",
        examples = {"set end date of {_b} to (6 hours after now)"},
        since = "5.0.0-a1")
    public static void setEndDate(ScheduledEventAction action, Date value) {
        if (value != null)
            action.setEndTime(offset(value));
    }

    @Property.Setter("cover")
    @Documentation(
        name = "Scheduled Event Builder Cover",
        description = "The cover image of the scheduled event being built, as a URL or a local file path.",
        examples = {"set cover of {_b} to \"https://example.com/cover.png\""},
        since = "5.0.0-a1")
    public static void setCover(ScheduledEventAction action, String value) {
        action.setImage(value == null ? null : icon(value));
    }

    private static OffsetDateTime offset(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atOffset(ZoneOffset.UTC);
    }

    private static Icon icon(String source) {
        try (InputStream stream = source.startsWith("http://") || source.startsWith("https://")
                ? new URL(source).openStream()
                : new FileInputStream(new File(source))) {
            return Icon.from(stream);
        } catch (IOException ex) {
            throw new IllegalArgumentException("could not read the image from '" + source + "'", ex);
        }
    }
}
