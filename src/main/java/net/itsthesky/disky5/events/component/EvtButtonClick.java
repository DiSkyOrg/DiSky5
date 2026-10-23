package net.itsthesky.disky5.events.component;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.api.components.ButtonBuilder;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Button Click",
        patterns = {"button click[ed]"},
        jda = ButtonInteractionEvent.class
)
@Documentation(
        name = "Button Click",
        description = {
                "Fired when any button sent by the bot is clicked.",
                "You can use the `clicked id` to get the clicked button id, and `event-button` is the clicked button itself.",
                "",
                "!!! info \"Modal can be shown in this interaction!\""
        },
        examples = {
                "on button clicked:",
                "\treply with hidden \"You clicked the button with id '%clicked id%'!\" # This will defer the interaction!"
        },
        since = "5.0.0-a1"
)
public final class EvtButtonClick {

    @EventValue
    public static String componentId(ButtonInteractionEvent event) {
        return event.getComponentId();
    }

    @EventValue
    public static ButtonBuilder button(ButtonInteractionEvent event) {
        return ButtonBuilder.from(event.getButton());
    }

    @EventValue
    public static @Nullable Guild guild(ButtonInteractionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static User user(ButtonInteractionEvent event) {
        return event.getUser();
    }

    @EventValue
    public static @Nullable Member member(ButtonInteractionEvent event) {
        return event.getMember();
    }

    @EventValue
    public static Number messageId(ButtonInteractionEvent event) {
        return event.getMessageIdLong();
    }

    @EventValue
    public static Message message(ButtonInteractionEvent event) {
        return event.getMessage();
    }

    @EventValue
    public static Channel channel(ButtonInteractionEvent event) {
        return event.getChannel();
    }

    // "clicked button" alone is Skript's own ExprClicked, so the component word carries it.
    @EventExpression("click[ed] (id|component id|button id)")
    public static String clickedId(ButtonInteractionEvent event) {
        return event.getComponentId();
    }
}
