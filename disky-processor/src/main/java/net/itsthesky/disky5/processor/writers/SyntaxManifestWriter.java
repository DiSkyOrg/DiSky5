package net.itsthesky.disky5.processor.writers;

import net.itsthesky.disky5.processor.util.Versions;
import com.squareup.javapoet.*;
import net.itsthesky.disky5.processor.DiSkyProcessor.EntityRecord;
import net.itsthesky.disky5.processor.DiSkyProcessor.EventDocValue;
import net.itsthesky.disky5.processor.DiSkyProcessor.EventExprDoc;
import net.itsthesky.disky5.processor.DiSkyProcessor.EventRegistration;
import net.itsthesky.disky5.processor.DiSkyProcessor.SharedContribution;
import net.itsthesky.disky5.processor.DiSkyProcessor.SharedDoc;
import net.itsthesky.disky5.processor.DiSkyProcessor.SyntaxRecord;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SyntaxManifestWriter {
    private SyntaxManifestWriter() {}

    public static final String PACKAGE = "net.itsthesky.disky5.generated";

    private static final ClassName REGISTRY    = ClassName.get("net.itsthesky.disky5.api.syntax", "DiSkySyntaxRegistry");
    private static final ClassName ENTITY_INFO = ClassName.get("net.itsthesky.disky5.api.syntax", "EntityInfo");
    private static final ClassName SYNTAX_INFO = ClassName.get("net.itsthesky.disky5.api.syntax", "SyntaxInfo");
    private static final ClassName SYNTAX_KIND = ClassName.get("net.itsthesky.disky5.api.syntax", "SyntaxKind");
    private static final ClassName CHANGE_MODE = ClassName.get("net.itsthesky.disky5.api.syntax", "ChangeMode");
    private static final ClassName EVENT_DETAILS = ClassName.get("net.itsthesky.disky5.api.syntax", "EventDetails");
    private static final ClassName EVENT_VALUE_DETAIL = ClassName.get("net.itsthesky.disky5.api.syntax", "EventValueDetail");
    private static final ClassName EVENT_EXPRESSION_DETAIL = ClassName.get("net.itsthesky.disky5.api.syntax", "EventExpressionDetail");
    private static final ClassName TYPE_REF = ClassName.get("net.itsthesky.disky5.api.syntax", "TypeRef");
    private static final ClassName LIST = ClassName.get("java.util", "List");

    private static final Map<String, String[]> BUILTIN_TYPES = new LinkedHashMap<>();
    static {
        for (String s : new String[]{"java.lang.String", "java.lang.CharSequence"}) BUILTIN_TYPES.put(s, new String[]{"text", "Text"});
        for (String s : new String[]{"java.lang.Integer", "java.lang.Long", "java.lang.Short",
                "java.lang.Byte", "java.lang.Double", "java.lang.Float", "java.math.BigInteger",
                "java.math.BigDecimal", "java.lang.Number"}) BUILTIN_TYPES.put(s, new String[]{"number", "Number"});
        BUILTIN_TYPES.put("java.lang.Boolean", new String[]{"boolean", "Boolean"});
        for (String s : new String[]{"java.time.OffsetDateTime", "java.time.Instant", "java.util.Date",
                "java.time.LocalDateTime", "java.time.ZonedDateTime"}) BUILTIN_TYPES.put(s, new String[]{"date", "Date"});
        BUILTIN_TYPES.put("java.awt.Color", new String[]{"color", "Color"});
    }

    public static void write(ProcessingEnvironment env, List<EntityRecord> entities,
                             List<SyntaxRecord> syntaxes, List<SharedContribution> shared,
                             Map<String, SharedDoc> sharedDocs, List<EventRegistration> events) throws IOException {
        Map<String, String> idByDescriptorFqn = new LinkedHashMap<>();
        Map<String, EntityRecord> byJdaFqn = new LinkedHashMap<>();
        for (EntityRecord e : entities) {
            idByDescriptorFqn.put(e.descriptorFqn(), e.id());
            byJdaFqn.putIfAbsent(e.jdaTypeFqn(), e);
        }

        ChunkedMethod reg = new ChunkedMethod();

        for (EntityRecord e : entities) {
            CodeBlock.Builder b = CodeBlock.builder().add("r.registerEntity($T.builder()", ENTITY_INFO);
            b.add(".id($S).name($S).codeName($S).jdaType($S)", e.id(), e.name(), e.codeName(), e.jdaTypeFqn());
            String parentId = e.parentDescriptorFqn() == null ? null : idByDescriptorFqn.get(e.parentDescriptorFqn());
            if (parentId != null)             b.add(".parentId($S)", parentId);
            if (notBlank(e.description()))     b.add(".description($S)", e.description());
            if (notBlank(e.since()))           b.add(".since($S)", e.since());
            reg.addStatement("$L", b.add(".build())").build());
        }

        for (SyntaxRecord s : syntaxes) {
            String[] type = resolveType(s.returnTypeFqn(), byJdaFqn);
            List<String> patterns = s.patterns().isEmpty()
                ? propertyPatterns(s.name(), s.entityId()) : s.patterns();
            reg.addStatement("$L", syntaxBlock(
                s.entityId(), slug(s.entityId(), s.name()), s.kind(), s.name(), patterns, type,
                s.returnList(), s.changeModes(), s.awaitable(), s.restBacked(), s.retrieveOnly(),
                false, s.writeOnly(), s.since(), s.description(), s.examples()).build());
        }

        for (SharedContribution c : shared) {
            String[] type = resolveType(c.returnTypeBoxed(), byJdaFqn);
            SharedDoc doc = sharedDocs.get(c.sharedName());
            List<String> description = doc != null ? List.of(doc.description()) : List.of();
            List<String> examples    = doc != null ? List.of(doc.examples())    : List.of();
            String since = Versions.first(doc == null ? null : doc.since());
            reg.addStatement("$L", syntaxBlock(
                c.codeName(), slug(c.codeName(), c.sharedName()), "PROPERTY", c.sharedName(),
                propertyPatterns(c.sharedName(), c.codeName()), type, false,
                c.settable() ? List.of("SET") : List.of(),
                false, false, false, true, c.method() == null, since, description, examples).build());
        }

        for (EventRegistration e : events)
            reg.addStatement("$L", eventBlock(e, byJdaFqn).build());

        TypeSpec.Builder cls = TypeSpec.classBuilder("GeneratedSyntaxManifest")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("Auto-generated by DiSkyProcessor - do not edit.\n"
                + "Fills DiSkySyntaxRegistry so the jar is self-describing (powers the syntax site).\n")
            .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build());
        reg.addTo(cls);

        JavaFile.builder(PACKAGE, cls.build()).build().writeTo(env.getFiler());
    }

    // One statement per syntax with its docs inlined blows past the 64 KB bytecode limit of a single
    // method, so register() is split into fixed-size parts it calls in order.
    private static final class ChunkedMethod {
        private static final int STATEMENTS_PER_PART = 80;
        private final List<CodeBlock> statements = new ArrayList<>();

        void addStatement(String format, Object... args) {
            statements.add(CodeBlock.of(format, args));
        }

        void addTo(TypeSpec.Builder cls) {
            MethodSpec.Builder register = MethodSpec.methodBuilder("register")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            for (int part = 0, from = 0; from < statements.size(); part++, from += STATEMENTS_PER_PART) {
                MethodSpec.Builder m = MethodSpec.methodBuilder("register" + part)
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .addStatement("$T r = $T.INSTANCE", REGISTRY, REGISTRY);
                for (CodeBlock statement : statements.subList(from, Math.min(from + STATEMENTS_PER_PART, statements.size())))
                    m.addStatement("$L", statement);
                cls.addMethod(m.build());
                register.addStatement("register$L()", part);
            }
            cls.addMethod(register.build());
        }
    }

    private static CodeBlock.Builder syntaxBlock(
            String entityId, String id, String kind, String name, List<String> patterns,
            String[] type, boolean returnList, List<String> changeModes,
            boolean awaitable, boolean restBacked, boolean retrieveOnly,
            boolean shared, boolean writeOnly, String since,
            List<String> description, List<String> examples) {
        CodeBlock.Builder b = CodeBlock.builder()
            .add("r.addSyntax($S, $T.builder()", entityId, SYNTAX_INFO)
            .add(".id($S).kind($T.$L).name($S)", id, SYNTAX_KIND, kind, name)
            .add(".patterns($L)", literals(patterns));
        if (type != null)                b.add(".returnType($S, $S)", type[0], type[1]);
        if (returnList)                  b.add(".returnList(true)");
        if (!changeModes.isEmpty())      b.add(".changeModes($L)", enums(changeModes));
        if (awaitable || restBacked || retrieveOnly)
                                         b.add(".async($L, $L, $L)", awaitable, restBacked, retrieveOnly);
        if (shared)                      b.add(".shared(true)");
        if (writeOnly)                   b.add(".writeOnly(true)");
        if (notBlank(since))             b.add(".since($S)", since);
        if (!description.isEmpty())      b.add(".description($L)", literals(description));
        if (!examples.isEmpty())         b.add(".examples($L)", literals(examples));
        return b.add(".build())");
    }

    private static CodeBlock.Builder eventBlock(EventRegistration e, Map<String, EntityRecord> byJdaFqn) {
        CodeBlock.Builder b = CodeBlock.builder()
            .add("r.addEvent($T.builder()", SYNTAX_INFO)
            .add(".id($S).kind($T.EVENT).name($S)", "event-" + slug(null, e.name()), SYNTAX_KIND, e.name())
            .add(".patterns($L)", literals(e.rawPatterns()));
        if (notBlank(e.since()))         b.add(".since($S)", e.since());
        if (notBlank(e.description()))   b.add(".description($L)", literals(List.of(e.description())));
        if (!e.examples().isEmpty())     b.add(".examples($L)", literals(e.examples()));
        b.add(".event($L)", eventDetails(e, byJdaFqn));
        return b.add(".build())");
    }

    private static CodeBlock eventDetails(EventRegistration e, Map<String, EntityRecord> byJdaFqn) {
        CodeBlock intents = e.intents().isEmpty()
            ? CodeBlock.of("$T.of()", LIST) : CodeBlock.of("$T.of($L)", LIST, literals(e.intents()));

        List<CodeBlock> values = new ArrayList<>();
        values.add(CodeBlock.of("new $T($S, new $T($S, $S), false, $S)",
            EVENT_VALUE_DETAIL, "bot", TYPE_REF, "bot", "Bot", "present"));
        for (EventDocValue v : e.docValues()) {
            String[] t = resolveType(v.typeFqn(), byJdaFqn);
            CodeBlock typeRef = t == null ? CodeBlock.of("null") : CodeBlock.of("new $T($S, $S)", TYPE_REF, t[0], t[1]);
            values.add(CodeBlock.of("new $T($S, $L, $L, $S)", EVENT_VALUE_DETAIL,
                v.name(), typeRef, v.list(), timeName(v.time())));
        }
        CodeBlock valuesList = values.isEmpty()
            ? CodeBlock.of("$T.of()", LIST) : CodeBlock.of("$T.of($L)", LIST, CodeBlock.join(values, ", "));

        List<CodeBlock> exprs = new ArrayList<>();
        for (EventExprDoc x : e.exprDocs()) {
            String[] t = resolveType(x.typeFqn(), byJdaFqn);
            CodeBlock typeRef = t == null ? CodeBlock.of("null") : CodeBlock.of("new $T($S, $S)", TYPE_REF, t[0], t[1]);
            exprs.add(CodeBlock.of("new $T($S, $L, $L)", EVENT_EXPRESSION_DETAIL, x.pattern(), typeRef, x.list()));
        }
        CodeBlock exprList = exprs.isEmpty()
            ? CodeBlock.of("$T.of()", LIST) : CodeBlock.of("$T.of($L)", LIST, CodeBlock.join(exprs, ", "));

        return CodeBlock.of("new $T($L, $L, $L, $L)", EVENT_DETAILS, e.cancellable(), intents, valuesList, exprList);
    }

    private static String timeName(int time) {
        return time < 0 ? "past" : time > 0 ? "future" : "present";
    }

    private static List<String> propertyPatterns(String name, String entityId) {
        List<String> p = new ArrayList<>();
        p.add("[the] " + name + " of %" + entityId + "%");
        p.add("%" + entityId + "%'s " + name);
        return p;
    }

    private static String[] resolveType(String fqn, Map<String, EntityRecord> byJdaFqn) {
        if (fqn == null || fqn.isBlank() || fqn.equals("void") || fqn.equals("java.lang.Void")) return null;
        String raw = fqn.contains("<") ? fqn.substring(0, fqn.indexOf('<')) : fqn;
        EntityRecord e = byJdaFqn.get(raw);
        if (e != null) return new String[]{e.id(), e.name()};
        String[] builtin = BUILTIN_TYPES.get(raw);
        if (builtin != null) return builtin;
        String simple = raw.contains(".") ? raw.substring(raw.lastIndexOf('.') + 1) : raw;
        return new String[]{simple.toLowerCase(), simple};
    }

    private static CodeBlock literals(List<String> values) {
        List<CodeBlock> parts = new ArrayList<>();
        for (String v : values) parts.add(CodeBlock.of("$S", v));
        return CodeBlock.join(parts, ", ");
    }

    private static CodeBlock enums(List<String> constants) {
        List<CodeBlock> parts = new ArrayList<>();
        for (String c : constants) parts.add(CodeBlock.of("$T.$L", CHANGE_MODE, c));
        return CodeBlock.join(parts, ", ");
    }

    private static String slug(String entityId, String name) {
        String base = (entityId == null ? "" : entityId + "-") + name;
        return base.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
