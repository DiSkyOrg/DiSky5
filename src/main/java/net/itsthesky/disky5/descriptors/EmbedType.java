package net.itsthesky.disky5.descriptors;

import ch.njol.skript.util.Color;
import ch.njol.skript.util.Date;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.core.Conversions;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

// EmbedBuilder is write-mostly: it exposes no getter for most fields once set, so getters here
// return null except where JDA can actually read back (description, fields). They never call
// build(), which would throw on a partially-filled embed.
@SkriptType(name = "embedbuilder", jda = EmbedBuilder.class)
public final class EmbedType {

    @Property("description")
    @Documentation(
            name = "Embed Description",
            description = "The description (main text) of the embed.",
            examples = "set description of {_embed} to \"Hello world\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String description(EmbedBuilder embed) {
        StringBuilder sb = embed.getDescriptionBuilder();
        return sb.length() == 0 ? null : sb.toString();
    }

    @Property.Setter("description")
    public static void setDescription(EmbedBuilder embed, @Nullable String value) {
        embed.setDescription(value);
    }

    @Property(value = "title", shared = true)
    @Documentation(
            name = "Embed Title",
            description = "The title of the embed. Read back is not supported by JDA (returns null).",
            examples = "set title of {_embed} to \"My title\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String title(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter(value = "title", shared = true)
    public static void setTitle(EmbedBuilder embed, @Nullable String value) {
        embed.setTitle(value);
    }

    @Property("title url")
    @Documentation(
            name = "Embed Title URL",
            description = "The URL the embed title links to. Read back is not supported by JDA (returns null).",
            examples = "set title url of {_embed} to \"https://disky.me\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String titleUrl(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter("title url")
    public static void setTitleUrl(EmbedBuilder embed, @Nullable String value) {
        embed.setUrl(value);
    }

    @Property(value = "color", shared = true)
    @Documentation(
            name = "Embed Color",
            description = "The color of the embed's side bar. Read back is not supported by JDA (returns null).",
            examples = "set color of {_embed} to orange",
            since = "5.0.0-a1"
    )
    public static @Nullable Color color(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter(value = "color", shared = true)
    public static void setColor(EmbedBuilder embed, @Nullable Color value) {
        embed.setColor(Conversions.awt(value));
    }

    @Property("discord author")
    @Documentation(
            name = "Embed Author",
            description = "The author name of the embed. Read back is not supported by JDA (returns null).",
            examples = "set author of {_embed} to \"DiSky\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String author(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter("discord author")
    public static void setAuthor(EmbedBuilder embed, @Nullable String value) {
        embed.setAuthor(value);
    }

    @Property("author icon")
    @Documentation(
            name = "Embed Author Icon",
            description = "The author icon URL of the embed. Read back is not supported by JDA (returns null).",
            examples = "set author icon of {_embed} to \"https://disky.me/icon.png\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String authorIcon(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter("author icon")
    public static void setAuthorIcon(EmbedBuilder embed, @Nullable String value) {
        embed.setAuthor("​", null, value);
    }

    @Property("author url")
    @Documentation(
            name = "Embed Author URL",
            description = "The author URL of the embed. Read back is not supported by JDA (returns null).",
            examples = "set author url of {_embed} to \"https://disky.me\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String authorUrl(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter("author url")
    public static void setAuthorUrl(EmbedBuilder embed, @Nullable String value) {
        embed.setAuthor("​", value, null);
    }

    @Property("footer")
    @Documentation(
            name = "Embed Footer",
            description = "The footer text of the embed. Read back is not supported by JDA (returns null).",
            examples = "set footer of {_embed} to \"Powered by DiSky\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String footer(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter("footer")
    public static void setFooter(EmbedBuilder embed, @Nullable String value) {
        embed.setFooter(value);
    }

    @Property("footer icon")
    @Documentation(
            name = "Embed Footer Icon",
            description = "The footer icon URL of the embed. Read back is not supported by JDA (returns null).",
            examples = "set footer icon of {_embed} to \"https://disky.me/icon.png\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String footerIcon(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter("footer icon")
    public static void setFooterIcon(EmbedBuilder embed, @Nullable String value) {
        embed.setFooter("​", value);
    }

    @Property("image")
    @Documentation(
            name = "Embed Image",
            description = "The image URL of the embed. Read back is not supported by JDA (returns null).",
            examples = "set image of {_embed} to \"https://disky.me/banner.png\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String image(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter("image")
    public static void setImage(EmbedBuilder embed, @Nullable String value) {
        embed.setImage(value);
    }

    @Property("thumbnail")
    @Documentation(
            name = "Embed Thumbnail",
            description = "The thumbnail URL of the embed. Read back is not supported by JDA (returns null).",
            examples = "set thumbnail of {_embed} to \"https://disky.me/thumb.png\"",
            since = "5.0.0-a1"
    )
    public static @Nullable String thumbnail(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter("thumbnail")
    public static void setThumbnail(EmbedBuilder embed, @Nullable String value) {
        embed.setThumbnail(value);
    }

    @Property("timestamp")
    @Documentation(
            name = "Embed Timestamp",
            description = "The timestamp shown in the embed's footer. Read back is not supported by JDA (returns null).",
            examples = "set timestamp of {_embed} to now",
            since = "5.0.0-a1"
    )
    public static @Nullable Date timestamp(EmbedBuilder embed) {
        return null;
    }

    @Property.Setter("timestamp")
    public static void setTimestamp(EmbedBuilder embed, @Nullable Date value) {
        embed.setTimestamp(value == null ? null : Instant.ofEpochMilli(value.getTime()));
    }

    @Property("fields")
    @Documentation(
            name = "Embed Fields",
            description = "The fields of the embed. Add fields with 'add %field% to fields of %embed%'.",
            examples = {
                "set {_fields::*} to fields of {_embed}",
                "add (a new field named \"Ping\" with value \"42ms\") to fields of {_embed}"
            },
            since = "5.0.0-a1"
    )
    public static List<MessageEmbed.Field> fields(EmbedBuilder embed) {
        return embed.getFields();
    }

    @Property.Adder("fields")
    public static void addField(EmbedBuilder embed, MessageEmbed.Field value) {
        embed.addField(value);
    }
}
