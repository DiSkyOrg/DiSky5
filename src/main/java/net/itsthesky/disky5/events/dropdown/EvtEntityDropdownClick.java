package net.itsthesky.disky5.events.dropdown;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.EventExpression;
import net.itsthesky.disky5.annotations.events.EventValue;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.api.components.EntitySelectBuilder;
import org.jetbrains.annotations.Nullable;

@SkriptEvent(
        name = "Entity Dropdown Click Event",
        patterns = {"entit(y|ies) drop[( |-)]down click[ed]"},
        jda = EntitySelectInteractionEvent.class
)
@Documentation(
        name = "Entity Dropdown Click Event",
        description = {
                "Fired when a user selects one or more entities in an entity dropdown menu.",
                "This event provides access to the selected entities (users, roles, channels, etc.).",
                "Don't forget to either reply to or defer the interaction to acknowledge it.",
                "You can show a modal in response to this interaction."
        },
        examples = {
                "on entity dropdown clicked:",
                "\tbroadcast \"User %event-user% selected entities: %selected entities%\""
        },
        since = "5.0.0-a1"
)
public final class EvtEntityDropdownClick {

    @EventValue
    public static Channel channel(EntitySelectInteractionEvent event) {
        return event.getChannel();
    }

    @EventValue
    public static @Nullable Message message(EntitySelectInteractionEvent event) {
        return event.getMessage();
    }

    @EventValue
    public static @Nullable Guild guild(EntitySelectInteractionEvent event) {
        return event.getGuild();
    }

    @EventValue
    public static @Nullable Member member(EntitySelectInteractionEvent event) {
        return event.getMember();
    }

    @EventValue
    public static User user(EntitySelectInteractionEvent event) {
        return event.getUser();
    }

    @EventValue
    public static EntitySelectBuilder dropdown(EntitySelectInteractionEvent event) {
        return EntitySelectBuilder.from(event.getComponent());
    }

    @EventValue
    public static String customId(EntitySelectInteractionEvent event) {
        return event.getComponent().getCustomId();
    }

    @EventExpression("select[ed] entit(y|ies)")
    public static IMentionable[] selectedEntities(EntitySelectInteractionEvent event) {
        return event.getValues().toArray(new IMentionable[0]);
    }
}
