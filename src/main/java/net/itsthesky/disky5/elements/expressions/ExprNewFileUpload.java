package net.itsthesky.disky5.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.FileUpload;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Name("New File Upload")
@Description({
    "A file to send to Discord, from a local file path, a URL, or an attachment of a received "
        + "message (an image too, when SkImage2 is installed).",
    "The name defaults to the file's own; 'with spoiler' hides it behind a spoiler.",
    "Nothing is read or downloaded until the message carrying it is sent."
})
@Examples({
    "set {_f} to a new file upload from file \"plugins/DiSky/logo.png\"",
    "set {_f} to a new file upload from url \"https://example.com/image.png\" named \"custom.png\"",
    "set {_f} to a new file upload from attachment {_attachment} with spoiler true",
    "add {_f} to attachments of {_message}"
})
@Since("5.0.0-a1")
public final class ExprNewFileUpload extends SimpleExpression<FileUpload> {

    private enum Source { LOCAL_FILE, URL, ATTACHMENT, IMAGE }

    private static final String SUFFIX = " [(named|with [the] name) %-string%] [with spoiler %-boolean%]";

    private Source source;
    private Expression<?> exprSource;
    private @Nullable Expression<String> exprName;
    private @Nullable Expression<Boolean> exprSpoiler;

    public static void register() {
        List<String> patterns = new ArrayList<>(List.of(
            "[a] new file upload from [local] file %string%" + SUFFIX,
            "[a] new file upload from ur(l|i) %string%" + SUFFIX,
            "[a] new file upload from attachment %attachment%" + SUFFIX));
        // %image% only exists when SkImage2 registers it; a pattern naming an unknown type fails to register.
        if (Bukkit.getPluginManager().getPlugin("SkImage2") != null)
            patterns.add("[a] new file upload from image %image%" + SUFFIX);
        ManualSyntax.expression(ExprNewFileUpload.class, FileUpload.class, false, ExpressionType.COMBINED,
            patterns.toArray(new String[0]));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern,
                        @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        source = Source.values()[matchedPattern];
        exprSource = expressions[0];
        exprName = (Expression<String>) expressions[1];
        exprSpoiler = (Expression<Boolean>) expressions[2];
        return true;
    }

    @Override
    protected FileUpload @NotNull [] get(@NotNull Event event) {
        Object raw = exprSource.getSingle(event);
        if (raw == null)
            return new FileUpload[0];
        String name = exprName == null ? null : exprName.getSingle(event);
        boolean spoiler = exprSpoiler != null && Boolean.TRUE.equals(exprSpoiler.getSingle(event));

        FileUpload upload;
        try {
            upload = switch (source) {
                case LOCAL_FILE -> fromLocalFile(String.valueOf(raw), name);
                case URL -> fromUrl(String.valueOf(raw), name);
                case ATTACHMENT -> fromAttachment((Message.Attachment) raw, name);
                case IMAGE -> fromImage((BufferedImage) raw, name);
            };
        } catch (Exception ex) {
            DiSkyRuntimeHandler.error(ex, getParser().getNode(), event);
            return new FileUpload[0];
        }
        if (spoiler)
            upload.asSpoiler();
        return new FileUpload[]{upload};
    }

    private static FileUpload fromLocalFile(String path, @Nullable String name) {
        File file = new File(path);
        if (!file.isFile())
            throw new IllegalArgumentException("the file '" + path + "' does not exist");
        return name == null ? FileUpload.fromData(file) : FileUpload.fromData(file, name);
    }

    private static FileUpload fromUrl(String url, @Nullable String name) {
        URI uri = URI.create(url.trim());
        if (uri.getScheme() == null)
            throw new IllegalArgumentException("'" + url + "' is not a URL; use 'from file' for a local path");
        if (name == null) {
            String path = uri.getPath() == null ? "" : uri.getPath();
            name = path.substring(path.lastIndexOf('/') + 1);
            if (name.isBlank())
                name = "file";
        }
        return FileUpload.fromStreamSupplier(name, () -> {
            try {
                return uri.toURL().openStream();
            } catch (IOException ex) {
                throw new UncheckedIOException("could not download '" + url + "'", ex);
            }
        });
    }

    private static FileUpload fromAttachment(Message.Attachment attachment, @Nullable String name) {
        return FileUpload.fromStreamSupplier(name == null ? attachment.getFileName() : name,
            () -> attachment.getProxy().download().join());
    }

    private static FileUpload fromImage(BufferedImage image, @Nullable String name) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bytes);
        InputStream data = new ByteArrayInputStream(bytes.toByteArray());
        return FileUpload.fromData(data, name == null ? "image-" + Integer.toHexString(image.hashCode()) + ".png" : name);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends FileUpload> getReturnType() {
        return FileUpload.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String kind = switch (source) {
            case LOCAL_FILE -> "file";
            case URL -> "url";
            case ATTACHMENT -> "attachment";
            case IMAGE -> "image";
        };
        return "a new file upload from " + kind + " " + exprSource.toString(event, debug)
            + (exprName == null ? "" : " named " + exprName.toString(event, debug))
            + (exprSpoiler == null ? "" : " with spoiler " + exprSpoiler.toString(event, debug));
    }
}
