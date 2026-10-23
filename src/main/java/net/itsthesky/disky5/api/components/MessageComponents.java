package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.MessageTopLevelComponent;
import net.dv8tion.jda.api.components.Component;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.actionrow.ActionRowChildComponent;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageRequest;

import java.util.ArrayList;
import java.util.List;

public final class MessageComponents {

    private MessageComponents() {}

    public static List<MessageTopLevelComponent> append(List<? extends MessageTopLevelComponent> existing,
                                                        ComponentBuilder<?> child) {
        List<MessageTopLevelComponent> result = new ArrayList<>(existing);
        if (child instanceof RowChildBuilder<?> rowChild) {
            ActionRowChildComponent built = rowChild.buildWithId();
            int last = result.size() - 1;
            if (built instanceof Button && last >= 0 && result.get(last) instanceof ActionRow row
                    && row.getComponents().size() < RowBuilder.MAX_BUTTONS
                    && row.getComponents().stream().allMatch(c -> c instanceof Button)) {
                List<ActionRowChildComponent> children = new ArrayList<>(row.getComponents());
                children.add(built);
                result.set(last, row.withComponents(children));
            } else {
                result.add(ActionRow.of(built));
            }
            return result;
        }
        if (!(child instanceof MessageTopLevelBuilder<?> topLevel))
            throw new IllegalArgumentException("a " + child + " cannot be sent on its own in a message");
        result.add(topLevel.buildWithId());
        return result;
    }

    public static List<MessageTopLevelComponent> group(ComponentBuilder<?>[] children) {
        List<MessageTopLevelComponent> result = List.of();
        for (ComponentBuilder<?> child : children)
            result = append(result, child);
        return result;
    }

    public static boolean needsV2(List<? extends MessageTopLevelComponent> components) {
        return components.stream().anyMatch(c -> c.getType() != Component.Type.ACTION_ROW);
    }

    public static <R extends MessageRequest<R>> R apply(R request, List<? extends MessageTopLevelComponent> components) {
        if (needsV2(components))
            request.useComponentsV2(true);
        return request.setComponents(components);
    }
}
