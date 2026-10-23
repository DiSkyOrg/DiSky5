package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Date;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@SkriptType(name = "scheduledevent", jda = ScheduledEvent.class)
public final class ScheduledEventType {

    @Property("description")
    @Documentation(
            name = "Scheduled Event Description",
            description = "The description of a scheduled event, or null if it has none.",
            examples = "reply with description of {_event}",
            since = "5.0.0-a1"
    )
    public static @Nullable String description(ScheduledEvent event) {
        return event.getDescription();
    }

    @Property("event type")
    @Documentation(
            name = "Scheduled Event Type",
            description = "The type of a scheduled event (stage instance, voice or external).",
            examples = "reply with event type of {_event}",
            since = "5.0.0-a1"
    )
    public static String eventType(ScheduledEvent event) {
        return event.getType().name().toLowerCase().replace("_", " ");
    }

    @Property("event status")
    @Documentation(
            name = "Scheduled Event Status",
            description = {
                    "The status of a scheduled event (scheduled, active, completed or cancelled).",
                    "Settable to transition the event (e.g. set to \"active\" to start it)."
            },
            examples = {
                    "reply with event status of {_event}",
                    "set event status of {_event} to \"active\""
            },
            since = "5.0.0-a1"
    )
    public static String eventStatus(ScheduledEvent event) {
        return event.getStatus().name().toLowerCase().replace("_", " ");
    }

    @Property.Setter("event status")
    public static RestAction<?> setEventStatus(ScheduledEvent event, String value) {
        final ScheduledEvent.Status status = ScheduledEvent.Status.valueOf(value.toUpperCase().replace(" ", "_"));
        return event.getManager().setStatus(status);
    }

    @Property("discord location")
    @Documentation(
            name = "Scheduled Event Location",
            description = "The location of a scheduled event: a channel id for channel-bound events, or the external place.",
            examples = {
                    "reply with location of {_event}",
                    "set location of {_event} to \"My House\""
            },
            since = "5.0.0-a1"
    )
    public static @Nullable String location(ScheduledEvent event) {
        return event.getLocation();
    }

    @Property.Setter("discord location")
    public static RestAction<?> setLocation(ScheduledEvent event, String value) {
        return event.getManager().setLocation(value);
    }

    @Property("start date")
    @Documentation(
            name = "Scheduled Event Start Date",
            description = "The start date of a scheduled event.",
            examples = "reply with start date of {_event}",
            since = "5.0.0-a1"
    )
    public static Date startDate(ScheduledEvent event) {
        return Conversions.date(event.getStartTime());
    }

    @Property("end date")
    @Documentation(
            name = "Scheduled Event End Date",
            description = {
                    "The end date of a scheduled event, or null for channel-bound events.",
                    "Settable for external events."
            },
            examples = {
                    "reply with end date of {_event}",
                    "set end date of {_event} to now"
            },
            since = "5.0.0-a1"
    )
    public static @Nullable Date endDate(ScheduledEvent event) {
        return Conversions.date(event.getEndTime());
    }

    @Property.Setter("end date")
    public static RestAction<?> setEndDate(ScheduledEvent event, Date value) {
        return event.getManager().setEndTime(Instant.ofEpochMilli(value.getTime()));
    }

    @Property("cover")
    @Documentation(
            name = "Scheduled Event Cover",
            description = {
                    "The cover image URL of a scheduled event, or null if no cover is set.",
                    "Settable to a URL or local file path."
            },
            examples = {
                    "reply with cover of {_event}",
                    "set cover of {_event} to \"https://example.com/cover.png\""
            },
            since = "5.0.0-a1"
    )
    public static @Nullable String cover(ScheduledEvent event) {
        return event.getImageUrl();
    }

    @Property.Setter("cover")
    public static RestAction<?> setCover(ScheduledEvent event, String value) {
        final Icon icon;
        try {
            final java.io.InputStream stream;
            if (value.startsWith("http://") || value.startsWith("https://")) {
                stream = new java.net.URL(value).openStream();
            } else {
                stream = new java.io.FileInputStream(new java.io.File(value));
            }
            icon = Icon.from(stream);
        } catch (java.io.IOException ex) {
            throw new RuntimeException("Could not read cover image from '" + value + "'", ex);
        }
        return event.getManager().setImage(icon);
    }

    @Property("event channel")
    @Documentation(
            name = "Scheduled Event Channel",
            description = "The guild channel a scheduled event is bound to, or null for external events.",
            examples = "reply with event channel of {_event}",
            since = "5.0.0-a1"
    )
    public static @Nullable GuildChannel eventChannel(ScheduledEvent event) {
        return event.getChannel();
    }

    @Property("creator")
    @Documentation(
            name = "Scheduled Event Creator",
            description = "The user who created a scheduled event, or null if unknown.",
            examples = "reply with creator of {_event}",
            since = "5.0.0-a1"
    )
    public static @Nullable User creator(ScheduledEvent event) {
        return event.getCreator();
    }

    @Property.Retrieve("interested members")
    @Documentation(
            name = "Scheduled Event Interested Members",
            description = {
                    "The members interested in a scheduled event. Fetched online, so it requires 'await'.",
                    "Only the first page (up to 100 members) is returned."
            },
            examples = "await set {_members::*} to interested members of {_event}",
            since = "5.0.0-a1"
    )
    public static RestAction<List<Member>> interestedMembers(ScheduledEvent event) {
        return event.retrieveInterestedMembers();
    }

    @Property(value = "name", shared = true)
    public static String discordName(ScheduledEvent event) {
        return event.getName();
    }

    @Property(value = "guild", shared = true)
    public static Guild guild(ScheduledEvent event) {
        return event.getGuild();
    }
}
