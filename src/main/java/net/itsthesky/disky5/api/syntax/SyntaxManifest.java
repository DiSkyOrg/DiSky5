package net.itsthesky.disky5.api.syntax;

import java.util.List;

public record SyntaxManifest(
        int schemaVersion,
        String diskyVersion,
        String generatedAt,
        List<EntityInfo> entities,
        List<SyntaxInfo> core,
        List<SyntaxInfo> events,
        List<TypeEntry> types
) {
    public static final int SCHEMA_VERSION = 1;
}
