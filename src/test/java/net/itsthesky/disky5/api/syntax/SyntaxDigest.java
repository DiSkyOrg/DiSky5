package net.itsthesky.disky5.api.syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final class SyntaxDigest {

    private SyntaxDigest() {
    }

    static String of(SyntaxManifest manifest) {
        List<String> lines = new ArrayList<>();

        for (EntityInfo entity : manifest.entities()) {
            lines.add("entity " + entity.id()
                + " | code=" + entity.codeName()
                + " | name=" + entity.name()
                + " | parent=" + nullSafe(entity.parentId())
                + " | jda=" + entity.jdaType());
            for (SyntaxInfo syntax : entity.syntaxes())
                lines.add(syntax(entity.id(), syntax));
        }
        for (SyntaxInfo syntax : manifest.core())
            lines.add(syntax("core", syntax));
        for (SyntaxInfo event : manifest.events())
            lines.addAll(event(event));
        for (TypeEntry type : manifest.types())
            lines.add("type " + type.id() + " | name=" + type.name() + " | values=" + String.join(",", type.values()));

        lines.sort(String::compareTo);
        return String.join("\n", lines) + "\n";
    }

    private static String syntax(String owner, SyntaxInfo s) {
        StringBuilder sb = new StringBuilder();
        sb.append("syntax ").append(owner).append('/').append(s.id())
            .append(" | kind=").append(s.kind())
            .append(" | name=").append(nullSafe(s.name()))
            .append(" | return=").append(typeRef(s.returnType())).append(s.returnList() ? "[]" : "")
            .append(" | modes=").append(s.changeModes().stream().map(Enum::name).sorted().collect(Collectors.joining(",")))
            .append(" | shared=").append(s.shared())
            .append(" | writeOnly=").append(s.writeOnly());
        if (s.async() != null)
            sb.append(" | async=").append(s.async());
        if (s.deprecated())
            sb.append(" | deprecated=").append(nullSafe(s.deprecatedReason()));
        if (!s.requiredIntents().isEmpty())
            sb.append(" | intents=").append(String.join(",", s.requiredIntents()));
        sb.append(" | patterns=").append(String.join(" ;; ", s.patterns()));
        return sb.toString();
    }

    private static List<String> event(SyntaxInfo e) {
        List<String> lines = new ArrayList<>();
        lines.add(syntax("event", e));
        EventDetails details = e.event();
        if (details == null)
            return lines;
        for (EventValueDetail value : details.values())
            lines.add("event-value " + e.id() + " | name=" + value.name()
                + " | type=" + typeRef(value.type()) + (value.list() ? "[]" : "")
                + " | time=" + value.time());
        for (EventExpressionDetail expression : details.expressions())
            lines.add("event-expr " + e.id() + " | pattern=" + expression.pattern()
                + " | type=" + typeRef(expression.type()) + (expression.list() ? "[]" : ""));
        return lines;
    }

    private static String typeRef(TypeRef ref) {
        return ref == null ? "-" : ref.id();
    }

    private static String nullSafe(String value) {
        return value == null ? "-" : value;
    }
}
