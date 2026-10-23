package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.Component;

public interface ComponentBuilder<C extends Component> {

    C build();

    int uniqueId();

    void uniqueId(int uniqueId);

    @SuppressWarnings("unchecked")
    default C buildWithId() {
        C built = build();
        return uniqueId() > 0 ? (C) built.withUniqueId(uniqueId()) : built;
    }
}
