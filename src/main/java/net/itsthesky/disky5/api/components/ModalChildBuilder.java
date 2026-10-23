package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.label.LabelChildComponent;
import org.jetbrains.annotations.Nullable;

public interface ModalChildBuilder<C extends LabelChildComponent> extends ComponentBuilder<C> {

    @Nullable String label();

    void label(@Nullable String label);

    @Nullable String description();

    void description(@Nullable String description);

    default Label buildLabel() {
        String label = label();
        if (label == null || label.isBlank())
            throw new IllegalStateException("a component added to a modal needs a label (set label of the input)");
        return Label.of(label, description(), buildWithId());
    }
}
