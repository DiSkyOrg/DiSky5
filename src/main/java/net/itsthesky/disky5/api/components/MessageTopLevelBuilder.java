package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.MessageTopLevelComponent;

public interface MessageTopLevelBuilder<C extends MessageTopLevelComponent> extends ComponentBuilder<C> {

    default boolean requiresComponentsV2() {
        return true;
    }
}
