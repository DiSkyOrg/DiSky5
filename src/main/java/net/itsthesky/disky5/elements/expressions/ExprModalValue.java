package net.itsthesky.disky5.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.itsthesky.disky5.api.events.DiSkyBukkitEvent;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_ModalReceived;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Modal Value")
@Description({
    "What the user submitted in a modal, by the id of the component.",
    "A text input gives its text; a dropdown, a radio group or a checkbox group give the selected "
        + "values; a checkbox gives true or false; an entity dropdown gives the picked users, roles or "
        + "channels ('selected entities of'); a file upload input gives the uploaded attachments "
        + "('uploaded files of')."
})
@Examples({
    "on modal received:",
    "\tif received id is \"feedback\":",
    "\t\treply with hidden \"Thanks for: %value of text input with id \"body\"%\"",
    "\t\tbroadcast \"%values of checkbox group with id \"topics\"%\"",
    "\t\tif value of checkbox with id \"agree\" is true:",
    "\t\t\tbroadcast \"%uploaded files of file upload with id \"proof\"%\""
})
@Since("5.0.0-a1")
public final class ExprModalValue extends SimpleExpression<Object> {

    private static final String COMPONENT = "(component|input|text[( |-)]input|drop[( |-)]down|entity drop[( |-)]down"
        + "|radio group|checkbox group|checkbox|file upload [input]|upload)";

    private Expression<String> id;
    private boolean plural;
    private Node node;

    public static void register() {
        ManualSyntax.expression(ExprModalValue.class, Object.class, true, ExpressionType.COMBINED,
            "[the] [modal] value[plural:s] of [the] " + COMPONENT + " [with [the] id] %string%",
            "[the] [selected] entit(y|plural:ies) of [the] " + COMPONENT + " [with [the] id] %string%",
            "[the] (uploaded|received) (file|attachment)[plural:s] of [the] " + COMPONENT + " [with [the] id] %string%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(GeneratedBukkitEvt_ModalReceived.class)) {
            Skript.error("The values of a modal can only be read in the 'modal received' event");
            return false;
        }
        id = (Expression<String>) expressions[0];
        plural = parseResult.hasTag("plural");
        node = getParser().getNode();
        return true;
    }

    @Override
    protected Object @Nullable [] get(@NotNull Event event) {
        String customId = id.getSingle(event);
        if (customId == null || !(event instanceof DiSkyBukkitEvent bukkit)
                || !(bukkit.getJdaEvent() instanceof ModalInteractionEvent modal))
            return new Object[0];
        ModalMapping mapping = modal.getValue(customId);
        if (mapping == null)
            return new Object[0];
        try {
            return read(mapping);
        } catch (Throwable t) {
            DiSkyRuntimeHandler.error(t, node, event);
            return new Object[0];
        }
    }

    private static Object[] read(ModalMapping mapping) {
        return switch (mapping.getType()) {
            case TEXT_INPUT, RADIO_GROUP -> {
                String text = mapping.getAsOptionalString();
                yield text == null ? new String[0] : new String[]{text};
            }
            case STRING_SELECT, CHECKBOX_GROUP -> mapping.getAsStringList().toArray(new String[0]);
            case CHECKBOX -> new Boolean[]{mapping.getAsBoolean()};
            case USER_SELECT, ROLE_SELECT, MENTIONABLE_SELECT, CHANNEL_SELECT -> {
                List<IMentionable> entities = new ArrayList<>();
                entities.addAll(mapping.getAsMentions().getUsers());
                entities.addAll(mapping.getAsMentions().getRoles());
                entities.addAll(mapping.getAsMentions().getChannels());
                yield entities.toArray(new IMentionable[0]);
            }
            case FILE_UPLOAD -> mapping.getAsAttachmentList().toArray(new Message.Attachment[0]);
            default -> new Object[0];
        };
    }

    @Override
    public boolean isSingle() {
        return !plural;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "modal value" + (plural ? "s" : "") + " of component with id " + id.toString(event, debug);
    }
}
