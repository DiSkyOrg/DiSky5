package net.itsthesky.disky5.api.syntax;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DiSkySyntaxRegistry {

    public static final DiSkySyntaxRegistry INSTANCE = new DiSkySyntaxRegistry();

    private final Map<String, EntityInfo> entityMeta = new LinkedHashMap<>();
    private final Map<String, List<SyntaxInfo>> entitySyntaxes = new LinkedHashMap<>();
    private final List<SyntaxInfo> core = new ArrayList<>();
    private final List<SyntaxInfo> events = new ArrayList<>();
    private final Map<String, TypeEntry> enumTypes = new LinkedHashMap<>();

    private DiSkySyntaxRegistry() {}

    public EntityInfo registerEntity(EntityInfo entity) {
        entityMeta.putIfAbsent(entity.id(), entity);
        entitySyntaxes.computeIfAbsent(entity.id(), k -> new ArrayList<>());
        return entity;
    }

    public SyntaxInfo addSyntax(String entityId, SyntaxInfo syntax) {
        if (entityId == null)
            core.add(syntax);
        else
            entitySyntaxes.computeIfAbsent(entityId, k -> new ArrayList<>()).add(syntax);
        return syntax;
    }

    public SyntaxInfo addEvent(SyntaxInfo event) {
        events.add(event);
        return event;
    }

    public void addEnumType(String id, String name, List<String> values) {
        enumTypes.putIfAbsent(id, new TypeEntry(id, name, List.copyOf(values)));
    }

    public List<EntityInfo> entities() {
        return List.copyOf(entityMeta.values());
    }

    public void clear() {
        entityMeta.clear();
        entitySyntaxes.clear();
        core.clear();
        events.clear();
        enumTypes.clear();
    }

    public SyntaxManifest snapshot(String diskyVersion, String generatedAt) {
        List<EntityInfo> entities = new ArrayList<>();
        Map<String, TypeRef> types = new LinkedHashMap<>();

        for (EntityInfo meta : entityMeta.values()) {
            List<SyntaxInfo> syntaxes = entitySyntaxes.getOrDefault(meta.id(), List.of());
            collectTypes(syntaxes, types);
            entities.add(meta.withSyntaxes(syntaxes));
        }
        collectTypes(core, types);
        collectTypes(events, types);

        Map<String, TypeEntry> typeEntries = new LinkedHashMap<>();
        for (TypeRef t : types.values()) {
            TypeEntry enumEntry = enumTypes.get(t.id());
            typeEntries.put(t.id(), enumEntry != null ? enumEntry : new TypeEntry(t.id(), t.name(), List.of()));
        }
        for (TypeEntry e : enumTypes.values())
            typeEntries.putIfAbsent(e.id(), e);

        return new SyntaxManifest(
                SyntaxManifest.SCHEMA_VERSION, diskyVersion, generatedAt,
                List.copyOf(entities), List.copyOf(core), List.copyOf(events), List.copyOf(typeEntries.values()));
    }

    private static void collectTypes(List<SyntaxInfo> syntaxes, Map<String, TypeRef> into) {
        for (SyntaxInfo s : syntaxes) {
            TypeRef t = s.returnType();
            if (t != null)
                into.putIfAbsent(t.id(), t);
        }
    }
}
