package net.itsthesky.disky5.api.syntax;

import java.util.ArrayList;
import java.util.List;

public record SyntaxInfo(
        String id,
        String entityId,
        SyntaxKind kind,
        String name,
        List<String> patterns,
        TypeRef returnType,
        boolean returnList,
        List<ChangeMode> changeModes,
        AsyncInfo async,
        boolean shared,
        boolean writeOnly,
        String since,
        List<String> description,
        List<String> examples,
        List<String> requiredIntents,
        boolean deprecated,
        String deprecatedReason,
        EventDetails event
) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String entityId;
        private SyntaxKind kind = SyntaxKind.EXPRESSION;
        private String name;
        private List<String> patterns = new ArrayList<>();
        private TypeRef returnType;
        private boolean returnList;
        private List<ChangeMode> changeModes = new ArrayList<>();
        private AsyncInfo async;
        private boolean shared;
        private boolean writeOnly;
        private String since;
        private List<String> description = new ArrayList<>();
        private List<String> examples = new ArrayList<>();
        private List<String> requiredIntents = new ArrayList<>();
        private boolean deprecated;
        private String deprecatedReason;
        private EventDetails event;

        public Builder id(String id) { this.id = id; return this; }
        public Builder entityId(String entityId) { this.entityId = entityId; return this; }
        public Builder kind(SyntaxKind kind) { this.kind = kind; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder patterns(String... patterns) { this.patterns = List.of(patterns); return this; }
        public Builder returnType(TypeRef returnType) { this.returnType = returnType; return this; }
        public Builder returnType(String id, String name) { this.returnType = new TypeRef(id, name); return this; }
        public Builder returnList(boolean returnList) { this.returnList = returnList; return this; }
        public Builder changeModes(ChangeMode... modes) { this.changeModes = List.of(modes); return this; }
        public Builder async(AsyncInfo async) { this.async = async; return this; }
        public Builder async(boolean awaitable, boolean restBacked, boolean retrieveOnly) {
            this.async = new AsyncInfo(awaitable, restBacked, retrieveOnly); return this;
        }
        public Builder shared(boolean shared) { this.shared = shared; return this; }
        public Builder writeOnly(boolean writeOnly) { this.writeOnly = writeOnly; return this; }
        public Builder since(String since) { this.since = since; return this; }
        public Builder description(String... description) { this.description = List.of(description); return this; }
        public Builder examples(String... examples) { this.examples = List.of(examples); return this; }
        public Builder requiredIntents(String... intents) { this.requiredIntents = List.of(intents); return this; }
        public Builder deprecated(boolean deprecated) { this.deprecated = deprecated; return this; }
        public Builder deprecated(boolean deprecated, String reason) {
            this.deprecated = deprecated; this.deprecatedReason = reason; return this;
        }
        public Builder event(EventDetails event) { this.event = event; return this; }

        public SyntaxInfo build() {
            if (id == null || id.isBlank())
                throw new IllegalStateException("SyntaxInfo requires an id");
            if (name == null || name.isBlank())
                throw new IllegalStateException("SyntaxInfo '" + id + "' requires a name");
            AsyncInfo a = (async == null || async.isNone()) ? null : async;
            return new SyntaxInfo(id, entityId, kind, name,
                    List.copyOf(patterns), returnType, returnList,
                    List.copyOf(changeModes), a, shared, writeOnly, since,
                    List.copyOf(description), List.copyOf(examples),
                    List.copyOf(requiredIntents), deprecated, deprecatedReason, event);
        }
    }
}
