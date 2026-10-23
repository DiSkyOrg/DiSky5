package net.itsthesky.disky5.events.bot;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.SkriptEvent;

@SkriptEvent(
        name = "Bot Ready Event",
        patterns = {"(ready|bot load[ed])"},
        jda = ReadyEvent.class
)
@Documentation(
        name = "Bot Ready Event",
        description = {
                "Fired when a bot is fully loaded and connected to Discord.",
                "This event is triggered once all guilds are ready and the bot's connection to Discord",
                "is completely established. This is the ideal event to use for initialization code that",
                "needs to run once when the bot starts up, such as scheduling tasks or initializing resources."
        },
        examples = {
                "on bot loaded:",
                "\tbroadcast \"Bot %event-bot% is now online and ready!\""
        },
        since = "5.0.0-a1"
)
public final class EvtBotReady {
}
