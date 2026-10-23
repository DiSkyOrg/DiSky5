package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Date;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.RichPresence;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@SkriptType(name = "activity", jda = Activity.class)
public final class ActivityType {

    @Property("activity text")
    @Documentation(
            name = "Activity Text",
            description = "The name/text of the activity (e.g. the game name, the custom status text).",
            examples = "reply with activity text of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable String activityText(Activity a) {
        return a.getName();
    }

    @Property("activity type")
    @Documentation(
            name = "Activity Type",
            description = "The type of the activity (playing, streaming, listening, watching, custom status, competing).",
            examples = "reply with activity type of {_activity}",
            since = "5.0.0-a1"
    )
    public static String activityType(Activity a) {
        return a.getType().name().toLowerCase(Locale.ROOT).replace('_', ' ');
    }

    @Property("activity state")
    @Documentation(
            name = "Activity State",
            description = "The state of the activity, or null if the activity has no state set.",
            examples = "reply with activity state of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable String activityState(Activity a) {
        return a.getState();
    }

    @Property("activity url")
    @Documentation(
            name = "Activity URL",
            description = "The URL of the activity (used for streaming activities), or null if it has none.",
            examples = "reply with activity url of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable String activityUrl(Activity a) {
        return a.getUrl();
    }

    @Property("activity emote")
    @Documentation(
            name = "Activity Emote",
            description = "The emoji of the activity (used for custom statuses), or null if it has none.",
            examples = "reply with activity emote of {_activity}",
            since = "5.0.0-a1"
    )
    public static net.dv8tion.jda.api.entities.emoji.@Nullable EmojiUnion activityEmote(Activity a) {
        return a.getEmoji();
    }

    @Property("activity start")
    @Documentation(
            name = "Activity Start",
            description = "The date at which the activity started, or null if it has no start timestamp.",
            examples = "reply with activity start of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable Date activityStart(Activity a) {
        final Activity.Timestamps timestamps = a.getTimestamps();
        if (timestamps == null || timestamps.getStartTime() == null)
            return null;
        return Conversions.date(timestamps.getStartTime());
    }

    @Property("activity end")
    @Documentation(
            name = "Activity End",
            description = "The date at which the activity ends, or null if it has no end timestamp.",
            examples = "reply with activity end of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable Date activityEnd(Activity a) {
        final Activity.Timestamps timestamps = a.getTimestamps();
        if (timestamps == null || timestamps.getEndTime() == null)
            return null;
        return Conversions.date(timestamps.getEndTime());
    }

    @Property("activity details")
    @Documentation(
            name = "Activity Details",
            description = "The rich-presence details of the activity, or null if the activity is not a rich presence.",
            examples = "reply with activity details of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable String activityDetails(Activity a) {
        final RichPresence rich = a.asRichPresence();
        return rich == null ? null : rich.getDetails();
    }

    @Property("activity large image")
    @Documentation(
            name = "Activity Large Image",
            description = "The large image URL of the rich-presence activity, or null if it has none.",
            examples = "reply with activity large image of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable String activityLargeImage(Activity a) {
        final RichPresence rich = a.asRichPresence();
        if (rich == null || rich.getLargeImage() == null)
            return null;
        return rich.getLargeImage().getUrl();
    }

    @Property("activity large text")
    @Documentation(
            name = "Activity Large Text",
            description = "The large image hover text of the rich-presence activity, or null if it has none.",
            examples = "reply with activity large text of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable String activityLargeText(Activity a) {
        final RichPresence rich = a.asRichPresence();
        if (rich == null || rich.getLargeImage() == null)
            return null;
        return rich.getLargeImage().getText();
    }

    @Property("activity small image")
    @Documentation(
            name = "Activity Small Image",
            description = "The small image URL of the rich-presence activity, or null if it has none.",
            examples = "reply with activity small image of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable String activitySmallImage(Activity a) {
        final RichPresence rich = a.asRichPresence();
        if (rich == null || rich.getSmallImage() == null)
            return null;
        return rich.getSmallImage().getUrl();
    }

    @Property("activity small text")
    @Documentation(
            name = "Activity Small Text",
            description = "The small image hover text of the rich-presence activity, or null if it has none.",
            examples = "reply with activity small text of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable String activitySmallText(Activity a) {
        final RichPresence rich = a.asRichPresence();
        if (rich == null || rich.getSmallImage() == null)
            return null;
        return rich.getSmallImage().getText();
    }

    @Property("activity party size")
    @Documentation(
            name = "Activity Party Size",
            description = "The current party size of the rich-presence activity, or null if it has no party.",
            examples = "reply with activity party size of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable Number activityPartySize(Activity a) {
        final RichPresence rich = a.asRichPresence();
        if (rich == null || rich.getParty() == null)
            return null;
        return rich.getParty().getSize();
    }

    @Property("activity party max size")
    @Documentation(
            name = "Activity Party Max Size",
            description = "The maximum party size of the rich-presence activity, or null if it has no party.",
            examples = "reply with activity party max size of {_activity}",
            since = "5.0.0-a1"
    )
    public static @Nullable Number activityPartyMaxSize(Activity a) {
        final RichPresence rich = a.asRichPresence();
        if (rich == null || rich.getParty() == null)
            return null;
        return rich.getParty().getMax();
    }
}
