package net.itsthesky.disky5.events.bot;

import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Bot Shutdown Event",
        patterns = {"bot (shutdown|stop)"},
        jda = ShutdownEvent.class
)
@Documentation(
        name = "Bot Shutdown Event",
        description = {
                "Fired when a bot is shutting down or being stopped.",
                "This event is triggered when the bot's connection to Discord is closing,",
                "which can happen during server restarts, plugin reloads, or manual bot shutdowns.",
                "It provides an opportunity to perform cleanup operations or save data before the bot goes offline."
        },
        examples = {
                "on bot shutdown:",
                "\tbroadcast \"Bot %event-bot% is shutting down!\""
        },
        since = "5.0.0-a1"
)
public final class EvtBotShutdown {
}
