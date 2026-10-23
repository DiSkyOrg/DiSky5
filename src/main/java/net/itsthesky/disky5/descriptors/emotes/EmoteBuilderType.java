package net.itsthesky.disky5.descriptors.emotes;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.emotes.EmoteBuilder;

import java.util.Arrays;
import java.util.List;

@SkriptType(name = "emotebuilder", jda = EmoteBuilder.class)
@Documentation(
    name = "Emote Builder",
    description = {"A custom emote that does not exist yet: a name, an image and optionally the roles allowed "
        + "to use it. Create it with 'the build result of', under 'await'.",
        "Nothing is read or sent until then: the image is downloaded or read from disk when the emote is built."},
    since = "5.0.0-b1")
public final class EmoteBuilderType {

    private EmoteBuilderType() {}

    @New("[a] new emote builder named %string% with [the] image %string/fileupload% in [the] [guild] %guild%")
    @Documentation(
        name = "New Emote Builder",
        description = {"Creates an emote builder. The image is an http(s) URL, a local file path or a file upload; "
            + "it must be a PNG, JPEG, GIF or WEBP of 256 KB at most (a GIF makes an animated emote).",
            "The name is 2 to 32 letters, digits or underscores. The bot needs the Create Expressions permission."},
        examples = {"set {_b} to a new emote builder named \"party_blob\" with image \"https://example.com/blob.png\" in event-guild",
            "await set {_emote} to the built emote from {_b}",
            "reply with \"Created %{_emote}%!\""},
        since = "5.0.0-b1")
    public static EmoteBuilder newEmoteBuilder(String name, Object image, Guild guild) {
        return new EmoteBuilder(name, image, guild);
    }

    @Build
    @Documentation(
        name = "Built Emote",
        description = {"Validates the builder, loads its image and creates the emote on Discord. Requires 'await'.",
            "Building the same builder twice creates two emotes."},
        examples = {"await set {_emote} to the built emote from {_b}"},
        since = "5.0.0-b1")
    public static RestAction<CustomEmoji> build(EmoteBuilder builder) {
        return builder.build();
    }

    @Property(value = "name", shared = true)
    public static String name(EmoteBuilder builder) {
        return builder.name();
    }

    @Property.Setter(value = "name", shared = true)
    public static void setName(EmoteBuilder builder, String name) {
        builder.name(name);
    }

    @Property("allowed roles")
    @Documentation(
        name = "Emote Builder Allowed Roles",
        description = {"The roles allowed to use the emote being built. Empty (the default) means everyone can use it.",
            "The roles must belong to the builder's guild."},
        examples = {"add role with id \"123456789012345678\" to allowed roles of {_b}",
            "remove event-role from allowed roles of {_b}"},
        since = "5.0.0-b1")
    public static List<Role> allowedRoles(EmoteBuilder builder) {
        return List.copyOf(builder.roles());
    }

    @Property.Setter("allowed roles")
    public static void setAllowedRoles(EmoteBuilder builder, Role[] roles) {
        builder.roles().clear();
        builder.roles().addAll(Arrays.asList(roles));
    }

    @Property.Adder("allowed roles")
    public static void addAllowedRole(EmoteBuilder builder, Role role) {
        if (!builder.roles().contains(role))
            builder.roles().add(role);
    }

    @Property.Remover("allowed roles")
    public static void removeAllowedRole(EmoteBuilder builder, Role role) {
        builder.roles().remove(role);
    }
}
