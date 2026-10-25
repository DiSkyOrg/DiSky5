package net.itsthesky.disky5.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.Activity;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Activity")
@Description({
    "Builds a Discord activity (what a bot is shown doing) to give to the activity of a bot.",
    "A streaming activity needs a valid Twitch or YouTube url, otherwise Discord shows it as playing."
})
@Examples({
    "set activity of {_bot} to watching \"the server\"",
    "set activity of {_bot} to listening to \"music\"",
    "set activity of {_bot} to streaming \"live\" with the url \"https://twitch.tv/someone\"",
    "set activity of {_bot} to custom status of \"Open for tickets\""
})
@Since("5.0.0-b1")
public final class ExprActivity extends SimpleExpression<Activity> {

    private static final String[] PATTERNS = {
        "listening [to] %string%",
        "watching [to] %string%",
        "playing [to] %string%",
        "streaming [to] %string% with [the] url %string%",
        "competing [in|to] %string%",
        "custom status [of] %string%"
    };

    private int pattern;
    private Expression<String> text;
    private @Nullable Expression<String> url;

    public static void register() {
        ManualSyntax.expression(ExprActivity.class, Activity.class, false, ExpressionType.COMBINED, PATTERNS);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        pattern = matchedPattern;
        text = (Expression<String>) expressions[0];
        url = matchedPattern == 3 ? (Expression<String>) expressions[1] : null;
        return true;
    }

    @Override
    protected Activity @NotNull [] get(@NotNull Event event) {
        String value = text.getSingle(event);
        if (value == null || value.isBlank())
            return new Activity[0];
        Activity activity = switch (pattern) {
            case 0 -> Activity.listening(value);
            case 1 -> Activity.watching(value);
            case 2 -> Activity.playing(value);
            case 3 -> Activity.streaming(value, url.getSingle(event));
            case 4 -> Activity.competing(value);
            default -> Activity.customStatus(value);
        };
        return new Activity[]{ activity };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Activity> getReturnType() {
        return Activity.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String t = text.toString(event, debug);
        return switch (pattern) {
            case 0 -> "listening to " + t;
            case 1 -> "watching " + t;
            case 2 -> "playing " + t;
            case 3 -> "streaming " + t + " with the url " + url.toString(event, debug);
            case 4 -> "competing in " + t;
            default -> "custom status of " + t;
        };
    }
}
