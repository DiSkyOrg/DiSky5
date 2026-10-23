package net.itsthesky.disky5.api.syntax;

import java.util.List;

public record EntityInfo(
        String id,
        String name,
        String codeName,
        String jdaType,
        String parentId,
        String description,
        String since,
        List<SyntaxInfo> syntaxes
) {
    public static Builder builder() {
        return new Builder();
    }

    public EntityInfo withSyntaxes(List<SyntaxInfo> syntaxes) {
        return new EntityInfo(id, name, codeName, jdaType, parentId, description, since,
                List.copyOf(syntaxes));
    }

    public static final class Builder {
        private String id;
        private String name;
        private String codeName;
        private String jdaType;
        private String parentId;
        private String description;
        private String since;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder codeName(String codeName) { this.codeName = codeName; return this; }
        public Builder jdaType(String jdaType) { this.jdaType = jdaType; return this; }
        public Builder parentId(String parentId) { this.parentId = parentId; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder since(String since) { this.since = since; return this; }

        public EntityInfo build() {
            if (id == null || id.isBlank())
                throw new IllegalStateException("EntityInfo requires an id");
            if (name == null || name.isBlank())
                throw new IllegalStateException("EntityInfo '" + id + "' requires a name");
            return new EntityInfo(id, name, codeName, jdaType, parentId, description, since, List.of());
        }
    }
}
