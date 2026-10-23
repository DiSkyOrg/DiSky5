package net.itsthesky.disky5.core;

import ch.njol.skript.config.Node;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import org.jetbrains.annotations.Nullable;

import java.util.StringJoiner;

public final class DiscordError {

    private final int code;
    private final String meaning;
    private final String message;
    private final Throwable cause;
    private final @Nullable Node node;

    public DiscordError(int code, String meaning, String message, Throwable cause, @Nullable Node node) {
        this.code = code;
        this.meaning = meaning;
        this.message = message;
        this.cause = cause;
        this.node = node;
    }

    public static DiscordError from(Throwable t, @Nullable Node node) {
        Throwable current = t;
        while (current != null && !(current instanceof ErrorResponseException))
            current = current.getCause();
        if (current instanceof ErrorResponseException ere)
            return new DiscordError(ere.getErrorCode(), ere.getMeaning(), schemaDetails(ere), t, node);
        Throwable reported = t;
        for (Throwable c = t; c != null; c = c.getCause())
            if (c.getMessage() != null && !c.getMessage().startsWith(c.getClass().getName())
                    && (c.getCause() == null || !c.getMessage().equals(c.getCause().toString())))
                reported = c;
        String msg = reported.getMessage() != null ? reported.getMessage() : reported.getClass().getSimpleName();
        return new DiscordError(-1, reported.getClass().getSimpleName(), msg, t, node);
    }

    private static String schemaDetails(ErrorResponseException ere) {
        StringJoiner details = new StringJoiner("; ");
        for (ErrorResponseException.SchemaError schema : ere.getSchemaErrors())
            for (ErrorResponseException.ErrorCode error : schema.getErrors())
                details.add(schema.getLocation() + ": " + error.getMessage());
        return details.length() == 0 ? ere.getMeaning() : details.toString();
    }

    public int getCode() {
        return code;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getCause() {
        return cause;
    }

    public @Nullable Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return (code >= 0 ? "[" + code + "] " : "") + meaning + (message.equals(meaning) ? "" : ": " + message);
    }
}
