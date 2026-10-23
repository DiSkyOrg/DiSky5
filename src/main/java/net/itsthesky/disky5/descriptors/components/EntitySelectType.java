package net.itsthesky.disky5.descriptors.components;

import net.dv8tion.jda.api.components.selections.EntitySelectMenu.DefaultValue;
import net.dv8tion.jda.api.components.selections.EntitySelectMenu.SelectTarget;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.components.EntitySelectBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SkriptType(name = "entitydropdown", jda = EntitySelectBuilder.class, parent = ComponentType.class)
@Documentation(
    name = "Entity Dropdown",
    description = {"A select menu listing the guild's users, roles or channels. Selecting fires "
        + "'on entity dropdown click' with 'selected entities'.",
        "An entity dropdown takes a whole row on its own."},
    since = "5.0.0-a1")
public final class EntitySelectType {

    private EntitySelectType() {}

    @New("[a] new entit(y|ies) drop[( |-)]down [with] [the] id %string% targeting %selecttargets%")
    @Documentation(
        name = "New Entity Dropdown",
        description = "Creates a dropdown listing users, roles and/or channels of the guild.",
        examples = {"set {_d} to a new entity dropdown with id \"pick\" targeting roles",
            "set {_d} to a new entity dropdown with id \"pick\" targeting users and roles"},
        since = "5.0.0-a1")
    public static EntitySelectBuilder newEntityDropdown(String id, SelectTarget[] targets) {
        return new EntitySelectBuilder(id, targets);
    }

    @Property("custom id")
    public static String customId(EntitySelectBuilder menu) {
        return menu.id();
    }

    @Property.Setter("custom id")
    public static void setCustomId(EntitySelectBuilder menu, String id) {
        menu.id(id);
    }

    @Property("select targets")
    @Documentation(
        name = "Entity Dropdown Targets",
        description = "What the dropdown lists: users, roles, channels, or several of them.",
        examples = {"set select targets of {_d} to users and roles"},
        since = "5.0.0-a1")
    public static List<SelectTarget> targets(EntitySelectBuilder menu) {
        return List.copyOf(menu.targets());
    }

    @Property.Setter("select targets")
    public static void setTargets(EntitySelectBuilder menu, SelectTarget[] targets) {
        menu.targets().clear();
        menu.targets().addAll(Arrays.asList(targets));
    }

    @Property("channel types")
    @Documentation(
        name = "Entity Dropdown Channel Types",
        description = "When the dropdown lists channels, restricts them to these kinds.",
        examples = {"set channel types of {_d} to text and voice"},
        since = "5.0.0-a1")
    public static List<ChannelType> channelTypes(EntitySelectBuilder menu) {
        return List.copyOf(menu.channelTypes());
    }

    @Property.Setter("channel types")
    public static void setChannelTypes(EntitySelectBuilder menu, ChannelType[] types) {
        menu.channelTypes().clear();
        menu.channelTypes().addAll(Arrays.asList(types));
    }

    @Property("default values")
    public static List<String> defaultValues(EntitySelectBuilder menu) {
        return menu.defaultValues().stream().map(DefaultValue::getId).toList();
    }

    @Property.Setter("default values")
    public static void setDefaultValues(EntitySelectBuilder menu, ISnowflake[] entities) {
        menu.defaultValues().clear();
        for (ISnowflake entity : entities)
            menu.defaultValues().add(EntitySelectBuilder.defaultValue(entity));
    }

    @Property.Adder("default values")
    public static void addDefaultValue(EntitySelectBuilder menu, ISnowflake entity) {
        menu.defaultValues().add(EntitySelectBuilder.defaultValue(entity));
    }

    @Property.Remover("default values")
    public static void removeDefaultValue(EntitySelectBuilder menu, ISnowflake entity) {
        menu.defaultValues().removeIf(value -> value.getIdLong() == entity.getIdLong());
    }

    @Property("placeholder")
    public static @Nullable String placeholder(EntitySelectBuilder menu) {
        return menu.placeholder();
    }

    @Property.Setter("placeholder")
    public static void setPlaceholder(EntitySelectBuilder menu, @Nullable String placeholder) {
        menu.placeholder(placeholder);
    }

    @Property("min range")
    public static Number minRange(EntitySelectBuilder menu) {
        return menu.minValues();
    }

    @Property.Setter("min range")
    public static void setMinRange(EntitySelectBuilder menu, Number value) {
        menu.minValues(value.intValue());
    }

    @Property("max range")
    public static Number maxRange(EntitySelectBuilder menu) {
        return menu.maxValues();
    }

    @Property.Setter("max range")
    public static void setMaxRange(EntitySelectBuilder menu, Number value) {
        menu.maxValues(value.intValue());
    }

    @Property("disabled")
    public static Boolean disabled(EntitySelectBuilder menu) {
        return menu.disabled();
    }

    @Property.Setter("disabled")
    public static void setDisabled(EntitySelectBuilder menu, Boolean disabled) {
        menu.disabled(Boolean.TRUE.equals(disabled));
    }

    @Property("required")
    public static @Nullable Boolean required(EntitySelectBuilder menu) {
        return menu.required();
    }

    @Property.Setter("required")
    public static void setRequired(EntitySelectBuilder menu, @Nullable Boolean required) {
        menu.required(required);
    }

    @Property("label")
    public static @Nullable String label(EntitySelectBuilder menu) {
        return menu.label();
    }

    @Property.Setter("label")
    public static void setLabel(EntitySelectBuilder menu, @Nullable String label) {
        menu.label(label);
    }

    @Property("description")
    public static @Nullable String description(EntitySelectBuilder menu) {
        return menu.description();
    }

    @Property.Setter("description")
    public static void setDescription(EntitySelectBuilder menu, @Nullable String description) {
        menu.description(description);
    }
}
