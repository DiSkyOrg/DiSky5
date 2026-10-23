package net.itsthesky.disky5.processor.util;

import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Versions {

    public static final String VERSION_OPTION = "disky.version";

    private static final Pattern PRE_RELEASE = Pattern.compile("^(\\d+\\.\\d+\\.\\d+)-[ab]\\d+$");

    private static @Nullable String buildVersion;

    private Versions() {
    }

    public static void init(ProcessingEnvironment env) {
        String v = env.getOptions().get(VERSION_OPTION);
        buildVersion = (v == null || v.isBlank()) ? null : v.trim();
    }

    public static @Nullable String buildVersion() {
        return buildVersion;
    }

    public static String defaultSince() {
        return buildVersion == null ? "5.0.0" : normalize(buildVersion);
    }

    public static @Nullable String normalize(@Nullable String since) {
        if (since == null) return null;
        Matcher m = PRE_RELEASE.matcher(since.trim());
        if (!m.matches()) return since;
        if (buildVersion == null) return since;
        String base = m.group(1);
        Matcher build = PRE_RELEASE.matcher(buildVersion);
        boolean stillPreRelease = build.matches() && build.group(1).equals(base);
        return stillPreRelease ? since : base;
    }

    public static String[] normalize(String[] since) {
        String[] out = new String[since.length];
        for (int i = 0; i < since.length; i++)
            out[i] = normalize(since[i]);
        return out;
    }

    public static @Nullable String first(String[] since) {
        return (since == null || since.length == 0) ? null : normalize(since[0]);
    }
}
