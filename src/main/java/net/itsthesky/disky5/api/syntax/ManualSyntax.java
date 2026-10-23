package net.itsthesky.disky5.api.syntax;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Section;
import net.itsthesky.disky5.DiSky5;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.structure.Structure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ManualSyntax {
    private static final Pattern PRE_RELEASE = Pattern.compile("^(\\d+\\.\\d+\\.\\d+)-[ab]\\d+$");

    private ManualSyntax() {}

    public static void effect(Class<? extends Effect> clazz, String... patterns) {
        Skript.registerEffect(clazz, patterns);
        manifest(clazz, SyntaxKind.EFFECT, null, false, null, patterns);
    }

    public static void condition(Class<? extends Condition> clazz, String... patterns) {
        Skript.registerCondition(clazz, patterns);
        manifest(clazz, SyntaxKind.CONDITION, null, false, null, patterns);
    }

    public static void section(Class<? extends Section> clazz, String... patterns) {
        Skript.registerSection(clazz, patterns);
        manifest(clazz, SyntaxKind.SECTION, null, false, null, patterns);
    }

    public static void structure(Class<? extends Structure> clazz, EntryValidator validator, String... patterns) {
        Skript.registerStructure(clazz, validator, patterns);
        manifest(clazz, SyntaxKind.STRUCTURE, null, false, null, patterns);
    }

    public static void expression(Class<? extends Expression<?>> clazz, Class<?> returnType,
                                  boolean returnList, ExpressionType type, String... patterns) {
        expression(clazz, returnType, returnList, type, null, patterns);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void expression(Class<? extends Expression<?>> clazz, Class<?> returnType,
                                  boolean returnList, ExpressionType type,
                                  ChangeMode[] changeModes, String... patterns) {
        Skript.registerExpression((Class) clazz, returnType, type, patterns);
        manifest(clazz, SyntaxKind.EXPRESSION, typeRef(returnType), returnList, changeModes, patterns);
    }

    private static void manifest(Class<?> clazz, SyntaxKind kind, TypeRef returnType,
                                 boolean returnList, ChangeMode[] changeModes, String... patterns) {
        Name name = clazz.getAnnotation(Name.class);
        Description desc = clazz.getAnnotation(Description.class);
        Examples examples = clazz.getAnnotation(Examples.class);
        Since since = clazz.getAnnotation(Since.class);

        String display = name != null ? name.value() : clazz.getSimpleName();
        SyntaxInfo.Builder b = SyntaxInfo.builder()
            .id(kind.name().toLowerCase() + "-" + slug(display))
            .kind(kind)
            .name(display)
            .patterns(patterns);
        if (returnType != null)                                b.returnType(returnType);
        if (returnList)                                        b.returnList(true);
        if (changeModes != null && changeModes.length > 0)     b.changeModes(changeModes);
        if (desc != null && desc.value().length > 0)           b.description(desc.value());
        if (examples != null && examples.value().length > 0)   b.examples(examples.value());
        if (since != null && since.value().length > 0)         b.since(normalizeSince(since.value()[0]));

        DiSkySyntaxRegistry.INSTANCE.addSyntax(null, b.build());
    }

    private static TypeRef typeRef(Class<?> type) {
        if (type == null) return null;
        for (EntityInfo e : DiSkySyntaxRegistry.INSTANCE.entities())
            if (type.getName().equals(e.jdaType()))
                return new TypeRef(e.id(), e.name());
        if (type == String.class || CharSequence.class.isAssignableFrom(type)) return new TypeRef("text", "Text");
        if (Number.class.isAssignableFrom(type) || type == int.class || type == long.class
                || type == double.class || type == float.class) return new TypeRef("number", "Number");
        if (type == Boolean.class || type == boolean.class) return new TypeRef("boolean", "Boolean");
        String s = type.getSimpleName();
        return new TypeRef(s.toLowerCase(), s);
    }

    // Runtime twin of the processor's Versions.normalize: hand-written @Since is read here, not by the APT.
    static String normalizeSince(String since) {
        Matcher m = PRE_RELEASE.matcher(since.trim());
        if (!m.matches()) return since;
        Matcher build = PRE_RELEASE.matcher(DiSky5.getInstance().getDescription().getVersion());
        return build.matches() && build.group(1).equals(m.group(1)) ? since : m.group(1);
    }

    private static String slug(String s) {
        return s.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }
}
