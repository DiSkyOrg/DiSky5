package net.itsthesky.disky5.actions;

import net.dv8tion.jda.api.managers.GuildWelcomeScreenManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.docs.Documentation;

public final class WelcomeScreenActions {

    private WelcomeScreenActions() {}

    @Effect("update [the] welcome screen using %welcomescreenbuilder%")
    @Documentation(
        name = "Update Welcome Screen",
        description = {"Sends a welcome screen builder to Discord, applying every field that was set on it.",
            "The effect waits for Discord to answer, so the next line sees the new screen."},
        examples = {"update the welcome screen using {_b}",
            "await set {_screen} to the welcome screen of event-guild"},
        since = "5.0.0-a2")
    public static RestAction<Void> updateWelcomeScreen(GuildWelcomeScreenManager builder) {
        return builder;
    }
}
