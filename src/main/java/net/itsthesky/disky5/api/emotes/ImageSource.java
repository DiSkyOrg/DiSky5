package net.itsthesky.disky5.api.emotes;

import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;

public final class ImageSource {

    private ImageSource() {}

    public enum Format { PNG, JPEG, GIF, WEBP, LOTTIE }

    public static byte[] read(Object source, int maxBytes, String what) {
        String label = label(source);
        byte[] data;
        try (InputStream stream = open(source)) {
            data = stream.readNBytes(maxBytes + 1);
        } catch (IOException | UncheckedIOException ex) {
            String reason = ex instanceof FileNotFoundException ? "not found" : ex.getMessage();
            // No cause attached: DiscordError reports the deepest cause's message, which would hide this one.
            throw new IllegalArgumentException("could not read the " + what + " from " + label + ": " + reason);
        }
        if (data.length == 0)
            throw new IllegalArgumentException("the " + what + " read from " + label + " is empty");
        if (data.length > maxBytes)
            throw new IllegalArgumentException("the " + what + " read from " + label + " is larger than "
                + maxBytes / 1024 + " KB, the maximum Discord accepts");
        return data;
    }

    public static @Nullable Format detect(byte[] data) {
        if (startsWith(data, 0, 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A))
            return Format.PNG;
        if (startsWith(data, 0, 0xFF, 0xD8, 0xFF))
            return Format.JPEG;
        if (startsWith(data, 0, 'G', 'I', 'F', '8'))
            return Format.GIF;
        if (startsWith(data, 0, 'R', 'I', 'F', 'F') && startsWith(data, 8, 'W', 'E', 'B', 'P'))
            return Format.WEBP;
        int i = startsWith(data, 0, 0xEF, 0xBB, 0xBF) ? 3 : 0;
        while (i < data.length && Character.isWhitespace(data[i]))
            i++;
        return i < data.length && data[i] == '{' ? Format.LOTTIE : null;
    }

    private static InputStream open(Object source) throws IOException {
        if (source instanceof FileUpload upload)
            return upload.getData();
        String value = String.valueOf(source).trim();
        if (value.startsWith("http://") || value.startsWith("https://"))
            return URI.create(value).toURL().openStream();
        File file = new File(value);
        if (!file.isFile())
            throw new IllegalArgumentException("the file '" + value + "' does not exist "
                + "(give a local path, an http(s) URL or a file upload)");
        return new FileInputStream(file);
    }

    private static String label(Object source) {
        return source instanceof FileUpload upload
            ? "the file upload '" + upload.getName() + "'"
            : "'" + source + "'";
    }

    private static boolean startsWith(byte[] data, int offset, int... prefix) {
        if (data.length < offset + prefix.length)
            return false;
        for (int i = 0; i < prefix.length; i++)
            if ((data[offset + i] & 0xFF) != prefix[i])
                return false;
        return true;
    }
}
