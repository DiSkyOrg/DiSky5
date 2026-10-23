package net.itsthesky.disky5.events.error;

import ch.njol.skript.config.Node;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.core.DiscordError;
import net.itsthesky.disky5.core.UncaughtError;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
    name = "DiSky Error Event",
    patterns = {"(disky|discord) (error|exception)"},
    jda = UncaughtError.class,
    cancellable = true
)
@Documentation(
    name = "DiSky Error Event",
    description = {
        "Fired on the main thread for every DiSky error that no 'try' block caught: a failed Discord "
            + "request, a missing permission, a refused builder, an effect used in the wrong event.",
        "An error raised inside a 'try' block goes to its 'catch' and never reaches this event.",
        "Cancel the event to keep DiSky from printing the error to the console; cancel it before any "
            + "'wait' or Discord effect, the console decision is taken as soon as the trigger pauses.",
        "'event-bot' is set when the error comes from a trigger of a DiSky event, and is empty otherwise "
            + "(a request queued without a trigger behind it does not know its bot), so a 'seen by' "
            + "filter only matches the errors whose bot is known.",
        "An error raised by this event's own trigger is printed and does not fire the event again."
    },
    examples = {
        "on disky exception:",
        "\tcancel event",
        "\tsend \"[DiSky] %event-discorderror% (%error location ? \"unknown location\"%)\" to console",
        "\tif error code of event-discorderror is 50013:",
        "\t\tpost \"Missing permission: %error message of event-discorderror%\" to text channel with id \"1234567890\""
    },
    since = "5.0.0-b1"
)
public final class EvtDiSkyError {

    @EventValue
    public static DiscordError error(UncaughtError uncaught) {
        return uncaught.error();
    }

    @EventExpression("[the] error location")
    public static @Nullable String location(UncaughtError uncaught) {
        Node node = uncaught.error().getNode();
        return node == null ? null : node.getConfig().getFileName() + ", line " + node.getLine();
    }
}
