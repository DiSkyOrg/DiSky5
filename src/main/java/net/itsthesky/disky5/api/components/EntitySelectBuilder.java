package net.itsthesky.disky5.api.components;

import net.dv8tion.jda.api.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.components.selections.EntitySelectMenu.DefaultValue;
import net.dv8tion.jda.api.components.selections.EntitySelectMenu.SelectTarget;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public final class EntitySelectBuilder implements RowChildBuilder<EntitySelectMenu>, ModalChildBuilder<EntitySelectMenu> {

    private String id;
    private final EnumSet<SelectTarget> targets = EnumSet.noneOf(SelectTarget.class);
    private final EnumSet<ChannelType> channelTypes = EnumSet.noneOf(ChannelType.class);
    private final List<DefaultValue> defaultValues = new ArrayList<>();
    private @Nullable String placeholder;
    private int minValues = 1;
    private int maxValues = 1;
    private boolean disabled;
    private @Nullable Boolean required;
    private @Nullable String label;
    private @Nullable String description;
    private int uniqueId;

    public EntitySelectBuilder(String id, SelectTarget... targets) {
        this.id = id;
        this.targets.addAll(List.of(targets));
    }

    public static EntitySelectBuilder from(EntitySelectMenu menu) {
        EntitySelectBuilder b = new EntitySelectBuilder(menu.getCustomId());
        b.targets.addAll(menu.getEntityTypes());
        b.channelTypes.addAll(menu.getChannelTypes());
        b.defaultValues.addAll(menu.getDefaultValues());
        b.placeholder = menu.getPlaceholder();
        b.minValues = menu.getMinValues();
        b.maxValues = menu.getMaxValues();
        b.disabled = menu.isDisabled();
        b.required = menu.isRequired();
        b.uniqueId = menu.getUniqueId();
        return b;
    }

    @Override
    public EntitySelectMenu build() {
        if (targets.isEmpty())
            throw new IllegalStateException("the entity dropdown '" + id + "' targets nothing (users, roles or channels)");
        EntitySelectMenu.Builder builder = EntitySelectMenu.create(id, targets)
            .setRequiredRange(minValues, maxValues)
            .setDisabled(disabled);
        if (!channelTypes.isEmpty()) builder.setChannelTypes(channelTypes);
        if (!defaultValues.isEmpty()) builder.setDefaultValues(defaultValues);
        if (placeholder != null) builder.setPlaceholder(placeholder);
        if (required != null) builder.setRequired(required);
        return builder.build();
    }

    public static DefaultValue defaultValue(Object entity) {
        if (entity instanceof Role role) return DefaultValue.from(role);
        if (entity instanceof GuildChannel channel) return DefaultValue.from(channel);
        if (entity instanceof UserSnowflake user) return DefaultValue.from(user);
        throw new IllegalArgumentException("a default value of an entity dropdown must be a user, a member, a role or a channel");
    }

    public String id() { return id; }
    public void id(String id) { this.id = id; }
    public EnumSet<SelectTarget> targets() { return targets; }
    public EnumSet<ChannelType> channelTypes() { return channelTypes; }
    public List<DefaultValue> defaultValues() { return defaultValues; }
    public @Nullable String placeholder() { return placeholder; }
    public void placeholder(@Nullable String placeholder) { this.placeholder = placeholder; }
    public int minValues() { return minValues; }
    public void minValues(int minValues) { this.minValues = minValues; }
    public int maxValues() { return maxValues; }
    public void maxValues(int maxValues) { this.maxValues = maxValues; }
    public boolean disabled() { return disabled; }
    public void disabled(boolean disabled) { this.disabled = disabled; }
    public @Nullable Boolean required() { return required; }
    public void required(@Nullable Boolean required) { this.required = required; }
    @Override public @Nullable String label() { return label; }
    @Override public void label(@Nullable String label) { this.label = label; }
    @Override public @Nullable String description() { return description; }
    @Override public void description(@Nullable String description) { this.description = description; }
    @Override public int uniqueId() { return uniqueId; }
    @Override public void uniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    @Override
    public String toString() {
        return "entity dropdown " + id;
    }
}
