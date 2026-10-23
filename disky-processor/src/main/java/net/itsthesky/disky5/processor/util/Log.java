package net.itsthesky.disky5.processor.util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public final class Log {

    public static final String PREFIX = "[DiSkyProcessor] ";

    public static final String VERBOSE_OPTION = "disky.verbose";

    private Log() {
    }

    public static boolean verbose(ProcessingEnvironment env) {
        return Boolean.parseBoolean(env.getOptions().get(VERBOSE_OPTION));
    }

    public static void note(ProcessingEnvironment env, String message) {
        if (verbose(env))
            env.getMessager().printMessage(Diagnostic.Kind.NOTE, PREFIX + message);
    }

    public static void warning(ProcessingEnvironment env, String message, Element element) {
        env.getMessager().printMessage(Diagnostic.Kind.WARNING, PREFIX + message, element);
    }

    public static void error(ProcessingEnvironment env, String message, Element element) {
        if (element == null)
            env.getMessager().printMessage(Diagnostic.Kind.ERROR, PREFIX + message);
        else
            env.getMessager().printMessage(Diagnostic.Kind.ERROR, PREFIX + message, element);
    }

    public static void error(ProcessingEnvironment env, String context, Throwable thrown, Element element) {
        Throwable root = thrown;
        while (root.getCause() != null && root.getCause() != root)
            root = root.getCause();
        String detail = root.getMessage() == null ? root.getClass().getSimpleName() : root.getMessage();
        error(env, context + ": " + detail, element);
    }
}
