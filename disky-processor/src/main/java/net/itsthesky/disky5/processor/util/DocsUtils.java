package net.itsthesky.disky5.processor.util;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import net.itsthesky.disky5.annotations.docs.Documentation;
import org.jetbrains.annotations.Nullable;

public final class DocsUtils {

    private DocsUtils() {
    }

    public static void attachDocumentation(TypeSpec.Builder builder, @Nullable Documentation docs) {
        if (docs == null)
            return;
        attachDocumentation(builder, docs.name(), docs.description(), docs.examples(), docs.since());
    }

    public static void attachDocumentation(TypeSpec.Builder builder, @Nullable String name,
                                           String[] description, String[] examples, String[] since) {
        if (name == null)
            return;

        builder.addAnnotation(AnnotationSpec.builder(SkriptTypes.SKRIPT_DOCS_NAME)
                .addMember("value", "$S", name)
                .build());

        builder.addAnnotation(AnnotationSpec.builder(SkriptTypes.SKRIPT_DOCS_DESCRIPTION)
                .addMember("value", "{\n$L\n}", String.join(",\n", wrapInQuotes(description)))
                .build());

        builder.addAnnotation(AnnotationSpec.builder(SkriptTypes.SKRIPT_DOCS_EXAMPLES)
                .addMember("value", "{\n$L\n}", String.join(",\n", wrapInQuotes(examples)))
                .build());

        builder.addAnnotation(AnnotationSpec.builder(SkriptTypes.SKRIPT_DOCS_SINCE)
                .addMember("value", "{\n$L\n}", String.join(",\n", wrapInQuotes(Versions.normalize(since))))
                .build());
    }

    private static String[] wrapInQuotes(String[] arr) {
        String[] wrapped = new String[arr.length];
        for (int i = 0; i < arr.length; i++)
            wrapped[i] = NameUtils.wrapInQuotes(arr[i]);
        return wrapped;
    }

}
