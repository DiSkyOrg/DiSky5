package net.itsthesky.disky5.api.async;

import ch.njol.skript.Skript;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Await")
@Description({
    "Runs an effect on a background thread and waits for it to fully complete before continuing.",
    "On a DiSky REST property change this blocks until Discord confirms the change (instead of "
        + "fire-and-forget); on a dual cache/online property read it uses the online value.",
    "Redundant on effects that are already async."
})
@Examples({
    "await set nickname of event-member to \"DiSky\"",
    "await set {_members::*} to members of event-guild"
})
@Since("5.0.0-a1")
public final class EffAwait extends AsyncEffect {

    private Effect inner;

    public static void register() {
        ManualSyntax.effect(EffAwait.class, "await <.+>");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        final String raw = parseResult.regexes.get(0).group();
        final boolean previousParseState = DiSkyAsync.enterParsingAwait();
        try {
            inner = Effect.parse(raw, "DiSky couldn't parse this effect: " + raw);
        } finally {
            DiSkyAsync.exitParsingAwait(previousParseState);
        }
        if (inner == null)
            return false;

        if (inner instanceof AsyncEffect)
            Skript.warning("This effect is already async; 'await' is redundant here.");

        getParser().setHasDelayBefore(Kleenean.TRUE);
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        DiSkyAsync.setAwaited(event);
        try {
            inner.run(event);
        } finally {
            DiSkyAsync.clearAwaited(event);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "await " + (inner == null ? "?" : inner.toString(event, debug));
    }
}
