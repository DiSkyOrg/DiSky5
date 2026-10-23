package net.itsthesky.disky5.api.syntax;

import java.util.List;

public final class ManifestJson {
    private ManifestJson() {}

    public static String toJson(SyntaxManifest m) {
        StringBuilder sb = new StringBuilder(1 << 16);
        sb.append('{');
        field(sb, "schemaVersion", m.schemaVersion()).append(',');
        field(sb, "diskyVersion", m.diskyVersion()).append(',');
        field(sb, "generatedAt", m.generatedAt()).append(',');

        key(sb, "entities").append('[');
        for (int i = 0; i < m.entities().size(); i++) {
            if (i > 0) sb.append(',');
            entity(sb, m.entities().get(i));
        }
        sb.append("],");

        key(sb, "core").append('[');
        for (int i = 0; i < m.core().size(); i++) {
            if (i > 0) sb.append(',');
            syntax(sb, m.core().get(i));
        }
        sb.append("],");

        key(sb, "events").append('[');
        for (int i = 0; i < m.events().size(); i++) {
            if (i > 0) sb.append(',');
            syntax(sb, m.events().get(i));
        }
        sb.append("],");

        key(sb, "types").append('[');
        for (int i = 0; i < m.types().size(); i++) {
            if (i > 0) sb.append(',');
            typeEntry(sb, m.types().get(i));
        }
        sb.append(']');

        return sb.append('}').toString();
    }

    private static void entity(StringBuilder sb, EntityInfo e) {
        sb.append('{');
        field(sb, "id", e.id()).append(',');
        field(sb, "name", e.name()).append(',');
        field(sb, "codeName", e.codeName()).append(',');
        field(sb, "jdaType", e.jdaType()).append(',');
        field(sb, "parentId", e.parentId()).append(',');
        field(sb, "description", e.description()).append(',');
        field(sb, "since", e.since()).append(',');
        key(sb, "syntaxes").append('[');
        for (int i = 0; i < e.syntaxes().size(); i++) {
            if (i > 0) sb.append(',');
            syntax(sb, e.syntaxes().get(i));
        }
        sb.append("]}");
    }

    private static void syntax(StringBuilder sb, SyntaxInfo s) {
        sb.append('{');
        field(sb, "id", s.id()).append(',');
        field(sb, "entityId", s.entityId()).append(',');
        field(sb, "kind", s.kind().name()).append(',');
        field(sb, "name", s.name()).append(',');
        arrayField(sb, "patterns", s.patterns()).append(',');
        key(sb, "returnType");
        if (s.returnType() == null) sb.append("null"); else typeRef(sb, s.returnType());
        sb.append(',');
        field(sb, "returnList", s.returnList()).append(',');
        key(sb, "changeModes").append('[');
        for (int i = 0; i < s.changeModes().size(); i++) {
            if (i > 0) sb.append(',');
            str(sb, s.changeModes().get(i).name());
        }
        sb.append("],");
        key(sb, "async");
        if (s.async() == null) sb.append("null");
        else { sb.append('{');
            field(sb, "awaitable", s.async().awaitable()).append(',');
            field(sb, "restBacked", s.async().restBacked()).append(',');
            field(sb, "retrieveOnly", s.async().retrieveOnly());
            sb.append('}'); }
        sb.append(',');
        field(sb, "shared", s.shared()).append(',');
        field(sb, "writeOnly", s.writeOnly()).append(',');
        field(sb, "since", s.since()).append(',');
        arrayField(sb, "description", s.description()).append(',');
        arrayField(sb, "examples", s.examples()).append(',');
        arrayField(sb, "requiredIntents", s.requiredIntents()).append(',');
        field(sb, "deprecated", s.deprecated()).append(',');
        field(sb, "deprecatedReason", s.deprecatedReason()).append(',');
        key(sb, "event");
        if (s.event() == null) sb.append("null"); else event(sb, s.event());
        sb.append('}');
    }

    private static void event(StringBuilder sb, EventDetails e) {
        sb.append('{');
        field(sb, "cancellable", e.cancellable()).append(',');
        arrayField(sb, "intents", e.intents()).append(',');
        key(sb, "values").append('[');
        for (int i = 0; i < e.values().size(); i++) {
            if (i > 0) sb.append(',');
            EventValueDetail v = e.values().get(i);
            sb.append('{');
            field(sb, "name", v.name()).append(',');
            key(sb, "type");
            if (v.type() == null) sb.append("null"); else typeRef(sb, v.type());
            sb.append(',');
            field(sb, "list", v.list()).append(',');
            field(sb, "time", v.time());
            sb.append('}');
        }
        sb.append("],");
        key(sb, "expressions").append('[');
        for (int i = 0; i < e.expressions().size(); i++) {
            if (i > 0) sb.append(',');
            EventExpressionDetail x = e.expressions().get(i);
            sb.append('{');
            field(sb, "pattern", x.pattern()).append(',');
            key(sb, "type");
            if (x.type() == null) sb.append("null"); else typeRef(sb, x.type());
            sb.append(',');
            field(sb, "list", x.list());
            sb.append('}');
        }
        sb.append("]}");
    }

    private static void typeRef(StringBuilder sb, TypeRef t) {
        sb.append('{');
        field(sb, "id", t.id()).append(',');
        field(sb, "name", t.name());
        sb.append('}');
    }

    private static void typeEntry(StringBuilder sb, TypeEntry t) {
        sb.append('{');
        field(sb, "id", t.id()).append(',');
        field(sb, "name", t.name()).append(',');
        arrayField(sb, "values", t.values());
        sb.append('}');
    }

    private static StringBuilder field(StringBuilder sb, String key, String value) {
        key(sb, key);
        if (value == null) sb.append("null"); else str(sb, value);
        return sb;
    }

    private static StringBuilder field(StringBuilder sb, String key, boolean value) {
        return key(sb, key).append(value);
    }

    private static StringBuilder field(StringBuilder sb, String key, int value) {
        return key(sb, key).append(value);
    }

    private static StringBuilder arrayField(StringBuilder sb, String key, List<String> values) {
        key(sb, key).append('[');
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) sb.append(',');
            str(sb, values.get(i));
        }
        return sb.append(']');
    }

    private static StringBuilder key(StringBuilder sb, String key) {
        str(sb, key);
        return sb.append(':');
    }

    private static void str(StringBuilder sb, String s) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                default -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        sb.append('"');
    }
}
