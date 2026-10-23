package net.itsthesky.disky5.api.emotes;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class EmoteBuilder {

    public static final int MAX_IMAGE_BYTES = 256 * 1024;
    private static final Pattern NAME = Pattern.compile("[A-Za-z0-9_]{2,32}");

    private final Guild guild;
    private String name;
    private final Object image;
    private final List<Role> roles = new ArrayList<>();

    public EmoteBuilder(String name, Object image, Guild guild) {
        this.name = name;
        this.image = image;
        this.guild = guild;
    }

    public RestAction<CustomEmoji> build() {
        if (name == null || !NAME.matcher(name).matches())
            throw new IllegalStateException("the emote name '" + name + "' is invalid: it must be 2 to 32 "
                + "characters long and only contain letters, digits and underscores");
        for (Role role : roles)
            if (role.getGuild().getIdLong() != guild.getIdLong())
                throw new IllegalStateException("the role '" + role.getName() + "' is not a role of the guild '"
                    + guild.getName() + "'");
        byte[] data = ImageSource.read(image, MAX_IMAGE_BYTES, "emote image");
        ImageSource.Format format = ImageSource.detect(data);
        Icon.IconType type = format == null ? null : switch (format) {
            case PNG -> Icon.IconType.PNG;
            case JPEG -> Icon.IconType.JPEG;
            case GIF -> Icon.IconType.GIF;
            case WEBP -> Icon.IconType.WEBP;
            case LOTTIE -> null;
        };
        if (type == null)
            throw new IllegalStateException("the emote image is not a PNG, JPEG, GIF or WEBP image");
        return guild.createEmoji(name, Icon.from(data, type), roles.toArray(new Role[0]))
            .map(CustomEmoji.class::cast);
    }

    public Guild guild() { return guild; }
    public String name() { return name; }
    public void name(String name) { this.name = name; }
    public List<Role> roles() { return roles; }

    @Override
    public String toString() {
        return "emote builder " + name;
    }
}
