package net.itsthesky.disky5.descriptors;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

@SkriptType(name = "embedfield", jda = MessageEmbed.Field.class)
public final class EmbedFieldType {

    @Property(value = "name", shared = true)
    @Documentation(
            name = "Embed Field Name",
            description = "The name (title) of the embed field.",
            examples = "send \"%name of {_field}%\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String name(MessageEmbed.Field field) {
        return field.getName();
    }

    @Property(value = "value", shared = true)
    @Documentation(
            name = "Embed Field Value",
            description = "The value (body text) of the embed field.",
            examples = "send \"%value of {_field}%\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String value(MessageEmbed.Field field) {
        return field.getValue();
    }

    @Property("inline")
    @Documentation(
            name = "Embed Field Inline",
            description = "Whether the embed field is displayed inline.",
            examples = "if inline of {_field} is true:",
            since = "5.0.0-a1"
    )
    public static boolean inline(MessageEmbed.Field field) {
        return field.isInline();
    }
}
