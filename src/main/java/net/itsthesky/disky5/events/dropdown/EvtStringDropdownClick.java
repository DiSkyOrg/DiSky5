package net.itsthesky.disky5.events.dropdown;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.api.components.StringSelectBuilder;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "String Dropdown Click Event",
        patterns = {"drop[( |-)]down click[ed]"},
        jda = StringSelectInteractionEvent.class
)
@Documentation(
        name = "String Dropdown Click Event",
        description = {
                "Fired when a user selects one or more choices in a string dropdown menu.",
                "This event provides access to the selected string values and dropdown details.",
                "Don't forget to either reply to or defer the interaction to acknowledge it.",
                "You can show a modal in response to this interaction."
        },
        examples = {
                "on dropdown clicked:",
                "\treply with \"You selected: %selected values%\""
        },
        since = "5.0.0-a1"
)
public final class EvtStringDropdownClick {

    @EventValue
    public static Channel channel(StringSelectInteractionEvent event) {
        return event.getChannel();
    }

    @EventValue
    public static @Nullable Message message(StringSelectInteractionEvent event) {
        return event.getMessage();
    }

    @EventValue
    public static @Nullable Guild guild(StringSelectInteractionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable Member member(StringSelectInteractionEvent event) {
        return event.getMember();
    }

    @EventValue
    public static User user(StringSelectInteractionEvent event) {
        return event.getUser();
    }

    @EventValue
    public static StringSelectBuilder dropdown(StringSelectInteractionEvent event) {
        return StringSelectBuilder.from(event.getComponent());
    }

    @EventValue
    public static String customId(StringSelectInteractionEvent event) {
        return event.getComponent().getCustomId();
    }

    @EventExpression("select[ed] value[s]")
    public static String[] selectedValues(StringSelectInteractionEvent event) {
        return event.getValues().toArray(new String[0]);
    }
}
