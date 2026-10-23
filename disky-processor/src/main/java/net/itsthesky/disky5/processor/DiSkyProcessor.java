package net.itsthesky.disky5.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import net.itsthesky.disky5.annotations.Build;
import net.itsthesky.disky5.annotations.Comparator;
import net.itsthesky.disky5.annotations.Converter;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.Getter;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.Property;
import net.itsthesky.disky5.annotations.SharedProperty;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.EnumAliases;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.annotations.events.SkriptEvent;
import net.itsthesky.disky5.processor.handlers.BuilderHandler;
import net.itsthesky.disky5.processor.handlers.ConditionHandler;
import net.itsthesky.disky5.processor.handlers.DescriptorHandler;
import net.itsthesky.disky5.processor.handlers.EffectHandler;
import net.itsthesky.disky5.processor.handlers.GetterHandler;
import net.itsthesky.disky5.processor.handlers.PropertyHandler;
import net.itsthesky.disky5.processor.util.Log;
import net.itsthesky.disky5.processor.util.NameUtils;
import net.itsthesky.disky5.processor.util.TypeUtils;
import net.itsthesky.disky5.processor.util.Versions;
import net.itsthesky.disky5.processor.writers.BuilderWriter;
import net.itsthesky.disky5.processor.writers.ConverterComparatorWriter;
import net.itsthesky.disky5.processor.writers.EffectWriter;
import net.itsthesky.disky5.processor.writers.EnumWriter;
import net.itsthesky.disky5.processor.writers.EventWriter;
import net.itsthesky.disky5.processor.writers.GeneratedElementsWriter;
import net.itsthesky.disky5.processor.writers.GeneratedEventsWriter;
import net.itsthesky.disky5.processor.writers.GetterWriter;
import net.itsthesky.disky5.processor.writers.MultiTypePropertyWriter;
import net.itsthesky.disky5.processor.writers.SyntaxManifestWriter;
import net.itsthesky.disky5.processor.writers.TypePropertyWriter;
import net.itsthesky.disky5.processor.writers.TypeRegistryWriter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
    "net.itsthesky.disky5.annotations.SkriptType",
    "net.itsthesky.disky5.annotations.Converter",
    "net.itsthesky.disky5.annotations.Comparator",
    "net.itsthesky.disky5.annotations.SharedProperty",
    "net.itsthesky.disky5.annotations.Condition",
    "net.itsthesky.disky5.annotations.Effect",
    "net.itsthesky.disky5.annotations.New",
    "net.itsthesky.disky5.annotations.Build",
    "net.itsthesky.disky5.annotations.Getter",
    "net.itsthesky.disky5.annotations.Getter.Retrieve",
    "net.itsthesky.disky5.annotations.SkriptEnum",
    "net.itsthesky.disky5.annotations.EnumAliases",
    "net.itsthesky.disky5.annotations.events.SkriptEvent"
})
// Must match the project's Java toolchain.
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedOptions({Log.VERBOSE_OPTION, Versions.VERSION_OPTION})
public final class DiSkyProcessor extends AbstractProcessor {

    private final List<DescriptorHandler> handlers = List.of(
        new PropertyHandler(),
        new ConditionHandler()
    );

    private final Map<String, GetterHandler.GetterSpec> getters = GetterHandler.newSink();

    private final Map<String, EffectHandler.EffectSpec> effects = EffectHandler.newSink();

    private final Map<String, BuilderHandler.NewSpec> factories = BuilderHandler.newFactorySink();
    private final Map<String, BuilderHandler.BuildSpec> builds = BuilderHandler.buildSink();

    private final Map<String, TypeInfo> typesByDescriptor = new LinkedHashMap<>();
    private final Map<String, EnumInfo> enumsByDescriptor = new LinkedHashMap<>();
    private final List<String> generatedClasses = new ArrayList<>();
    private final List<ConverterInfo> converters = new ArrayList<>();
    private final List<ComparatorInfo> comparators = new ArrayList<>();
    private final List<SharedContribution> sharedContributions = new ArrayList<>();
    private final Map<String, SharedDoc> sharedDocs = new LinkedHashMap<>();
    private final List<MultiTypeContribution> multiTypeContributions = new ArrayList<>();
    private final List<EntityRecord> entityRecords = new ArrayList<>();
    private final List<SyntaxRecord> syntaxRecords = new ArrayList<>();
    private final List<EventRegistration> eventRegistrations = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        Versions.init(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment round) {
        Set<String> multiTypeNames = computeMultiTypeNames(round);
        for (Element el : sortedAnnotated(round, SkriptType.class)) {
            SkriptType annotation = el.getAnnotation(SkriptType.class);
            if (el.getKind() != ElementKind.CLASS) continue;
            TypeElement descriptor = (TypeElement) el;
            try {
                String parentDescriptorFqn = TypeUtils.resolveParentClass(descriptor);
                DescriptorContext ctx = new DescriptorContext(
                    descriptor, TypeUtils.resolveJdaClass(descriptor), annotation.name(),
                    parentDescriptorFqn, processingEnv, generatedClasses, syntaxRecords,
                    multiTypeNames, multiTypeContributions);

                for (DescriptorHandler handler : handlers) {
                    try {
                        handler.handle(ctx);
                    } catch (Exception e) {
                        Log.error(processingEnv, handler.getClass().getSimpleName()
                            + " failed on " + descriptor.getSimpleName(), e, descriptor);
                    }
                }

                ClassName jda = ClassName.bestGuess(ctx.jdaType);
                String code = annotation.name();
                typesByDescriptor.put(ctx.descriptorFqn,
                    new TypeInfo(jda, code, code + "s?", NameUtils.capitalize(code), parentDescriptorFqn));

                recordEntity(descriptor, ctx, code);
                collectSharedContributions(descriptor, code);
            } catch (Exception e) {
                Log.error(processingEnv, "failed on descriptor " + descriptor.getSimpleName(), e, descriptor);
            }
        }

        collectConverters(round);
        collectComparators(round);
        collectSharedDocs(round);
        collectEvents(round);
        collectGetters(round);
        collectEnums(round);
        collectEffects(round);
        collectBuilders(round);

        if (round.processingOver()) {
            writeSharedProperties();
            writeMultiTypeProperties();

            runWriter("getter-by-id expressions", () -> {
                Map<String, String> entityIdByJdaFqn = new LinkedHashMap<>();
                for (EntityRecord e : entityRecords)
                    entityIdByJdaFqn.putIfAbsent(e.jdaTypeFqn(), e.id());
                GetterWriter.write(processingEnv, getters.values(), generatedClasses,
                    syntaxRecords, entityIdByJdaFqn);
            });

            runWriter("effects", () -> EffectWriter.write(processingEnv, effects.values(),
                generatedClasses, syntaxRecords));

            runWriter("builders", () -> {
                Map<String, String> codeNameByJdaFqn = new LinkedHashMap<>();
                for (TypeInfo type : typesByDescriptor.values())
                    codeNameByJdaFqn.putIfAbsent(type.jdaType().toString(), type.codeName());
                BuilderWriter.write(processingEnv, factories.values(), builds.values(),
                    codeNameByJdaFqn, generatedClasses, syntaxRecords);
            });

            if (!typesByDescriptor.isEmpty()) {
                List<TypeInfo> ordered = orderParentsFirst();
                runWriter("GeneratedTypes", () -> TypeRegistryWriter.write(processingEnv, ordered));
                runWriter("GeneratedElements", () -> GeneratedElementsWriter.write(processingEnv, generatedClasses));
            }
            runWriter("GeneratedConverters", () -> ConverterComparatorWriter.writeConverters(processingEnv, converters));
            runWriter("GeneratedComparators", () -> ConverterComparatorWriter.writeComparators(processingEnv, comparators));
            runWriter("GeneratedEnums", () -> EnumWriter.write(processingEnv, enumsByDescriptor.values()));
            runWriter("GeneratedSyntaxManifest", () -> SyntaxManifestWriter.write(processingEnv,
                    entityRecords, syntaxRecords, sharedContributions, sharedDocs, eventRegistrations));
            runWriter("GeneratedEvents", () -> GeneratedEventsWriter.write(processingEnv, eventRegistrations));
        }

        return true;
    }

    private static List<Element> sortedAnnotated(RoundEnvironment round,
                                                 Class<? extends java.lang.annotation.Annotation> annotation) {
        List<Element> elements = new ArrayList<>(round.getElementsAnnotatedWith(annotation));
        elements.sort((a, b) -> sortKey(a).compareTo(sortKey(b)));
        return elements;
    }

    private static String sortKey(Element el) {
        Element owner = el.getEnclosingElement();
        String ownerName = owner instanceof TypeElement type
            ? type.getQualifiedName().toString()
            : String.valueOf(owner);
        return ownerName + '#' + el.getSimpleName();
    }

    @FunctionalInterface
    private interface WriterTask {
        void run() throws Exception;
    }

    // Only IOException used to be caught here; an unchecked failure in one writer aborted javac and
    // skipped every writer queued behind it, burying the real error in unrelated "cannot find symbol".
    private void runWriter(String what, WriterTask task) {
        try {
            task.run();
        } catch (Exception ex) {
            Log.error(processingEnv, "writing " + what, ex, null);
        }
    }

    private void collectConverters(RoundEnvironment round) {
        for (Element el : sortedAnnotated(round, Converter.class)) {
            if (el.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) el;
            if (m.getParameters().size() != 1) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "[DiSkyProcessor] @Converter must take exactly one parameter", m);
                continue;
            }
            TypeName from = TypeName.get(processingEnv.getTypeUtils().erasure(m.getParameters().get(0).asType()));
            TypeName to   = TypeName.get(processingEnv.getTypeUtils().erasure(m.getReturnType()));
            ClassName owner = ClassName.get((TypeElement) m.getEnclosingElement());
            converters.add(new ConverterInfo(from, to, owner, m.getSimpleName().toString()));
        }
    }

    private void collectComparators(RoundEnvironment round) {
        for (Element el : sortedAnnotated(round, Comparator.class)) {
            if (el.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) el;
            if (m.getParameters().size() != 2) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "[DiSkyProcessor] @Comparator must take exactly two parameters", m);
                continue;
            }
            TypeName t1 = TypeName.get(processingEnv.getTypeUtils().erasure(m.getParameters().get(0).asType()));
            TypeName t2 = TypeName.get(processingEnv.getTypeUtils().erasure(m.getParameters().get(1).asType()));
            boolean returnsBoolean = m.getReturnType().getKind() == TypeKind.BOOLEAN;
            ClassName owner = ClassName.get((TypeElement) m.getEnclosingElement());
            comparators.add(new ComparatorInfo(t1, t2, owner, m.getSimpleName().toString(), returnsBoolean));
        }
    }

    private void collectEvents(RoundEnvironment round) {
        for (Element el : sortedAnnotated(round, SkriptEvent.class)) {
            if (el.getKind() != ElementKind.CLASS) continue;
            try {
                eventRegistrations.add(EventWriter.write(processingEnv, (TypeElement) el));
            } catch (Exception e) {
                Log.error(processingEnv, "EventWriter failed on " + el.getSimpleName(), e, el);
            }
        }
    }

    private void collectGetters(RoundEnvironment round) {
        Set<Element> seen = new LinkedHashSet<>(sortedAnnotated(round, Getter.class));
        seen.addAll(sortedAnnotated(round, Getter.Retrieve.class));
        for (Element el : seen) {
            if (el.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) el;
            if (m.getParameters().isEmpty()) {
                Log.error(processingEnv, "@Getter method must take the lookup scope (bot, guild or "
                    + "message channel) as its first parameter", m);
                continue;
            }
            try {
                GetterHandler.collect(m, getters);
            } catch (Exception e) {
                Log.error(processingEnv, "collecting @Getter " + m.getSimpleName(), e, m);
            }
        }
    }

    private void collectEffects(RoundEnvironment round) {
        Map<String, String> jdaFqnByCodeName = new LinkedHashMap<>();
        for (TypeInfo type : typesByDescriptor.values())
            jdaFqnByCodeName.putIfAbsent(type.codeName(), type.jdaType().toString());
        for (EnumInfo enumType : enumsByDescriptor.values())
            jdaFqnByCodeName.putIfAbsent(enumType.codeName(), enumType.jdaType().toString());

        for (Element el : sortedAnnotated(round, Effect.class)) {
            if (el.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) el;
            if (!m.getModifiers().contains(Modifier.STATIC)) {
                Log.error(processingEnv, "an @Effect method must be static", m);
                continue;
            }
            try {
                EffectHandler.collect(processingEnv, m, m.getAnnotation(Effect.class),
                    jdaFqnByCodeName, effects);
            } catch (Exception e) {
                Log.error(processingEnv, "collecting @Effect " + m.getSimpleName(), e, m);
            }
        }
    }

    private void collectBuilders(RoundEnvironment round) {
        Map<String, String> jdaFqnByCodeName = new LinkedHashMap<>();
        for (TypeInfo type : typesByDescriptor.values())
            jdaFqnByCodeName.putIfAbsent(type.codeName(), type.jdaType().toString());
        for (EnumInfo enumType : enumsByDescriptor.values())
            jdaFqnByCodeName.putIfAbsent(enumType.codeName(), enumType.jdaType().toString());

        for (Element el : sortedAnnotated(round, New.class)) {
            if (el.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) el;
            try {
                BuilderHandler.collectFactory(processingEnv, m, m.getAnnotation(New.class),
                    jdaFqnByCodeName, factories);
            } catch (Exception e) {
                Log.error(processingEnv, "collecting @New " + m.getSimpleName(), e, m);
            }
        }
        for (Element el : sortedAnnotated(round, Build.class)) {
            if (el.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) el;
            try {
                BuilderHandler.collectBuild(processingEnv, m, builds);
            } catch (Exception e) {
                Log.error(processingEnv, "collecting @Build " + m.getSimpleName(), e, m);
            }
        }
    }

    private void collectEnums(RoundEnvironment round) {
        for (Element el : sortedAnnotated(round, SkriptEnum.class)) {
            if (el.getKind() != ElementKind.CLASS) continue;
            TypeElement holder = (TypeElement) el;
            try {
                SkriptEnum ann = holder.getAnnotation(SkriptEnum.class);
                ClassName jda = ClassName.bestGuess(TypeUtils.resolveEnumClass(holder));
                String code = ann.name();
                Documentation d = holder.getAnnotation(Documentation.class);
                String display = (d != null && !d.name().isBlank()) ? d.name() : NameUtils.capitalize(code);
                enumsByDescriptor.put(holder.getQualifiedName().toString(),
                    new EnumInfo(jda, code, code + (code.endsWith("s") ? "" : "s?"), display,
                        ClassName.get(holder), findAliasMethod(holder)));
            } catch (Exception e) {
                Log.error(processingEnv, "collecting @SkriptEnum " + holder.getSimpleName(), e, holder);
            }
        }
    }

    private static String findAliasMethod(TypeElement holder) {
        for (Element e : holder.getEnclosedElements()) {
            if (e.getKind() == ElementKind.METHOD && e.getAnnotation(EnumAliases.class) != null)
                return e.getSimpleName().toString();
        }
        return null;
    }

    private void recordEntity(TypeElement descriptor, DescriptorContext ctx, String code) {
        Documentation d = descriptor.getAnnotation(Documentation.class);
        String name = (d != null && !d.name().isBlank()) ? d.name() : NameUtils.capitalize(code);
        String description = (d != null && d.description().length > 0) ? String.join(" ", d.description()) : null;
        String since = Versions.first(d == null ? null : d.since());
        entityRecords.add(new EntityRecord(
            ctx.descriptorFqn, code, name, code, ctx.jdaType, ctx.parentDescriptorFqn, description, since));
    }

    private void collectSharedContributions(TypeElement descriptor, String codeName) {
        for (Element e : descriptor.getEnclosedElements()) {
            if (e.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) e;
            Property p = m.getAnnotation(Property.class);
            if (p == null || !p.shared()) continue;
            if (m.getParameters().size() != 1) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "[DiSkyProcessor] a shared @Property must take exactly one parameter", m);
                continue;
            }
            ClassName paramType = ClassName.bestGuess(
                TypeUtils.stripAnnotations(m.getParameters().get(0).asType().toString()));
            String returnBoxed = TypeUtils.boxed(TypeUtils.stripAnnotations(m.getReturnType().toString()));

            ExecutableElement setter = findSharedSetter(descriptor, p.value());
            String setterMethod = null;
            ClassName setterValueType = null;
            boolean setterRest = false;
            if (setter != null) {
                if (setter.getParameters().size() != 2) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "[DiSkyProcessor] a shared @Property.Setter must take exactly two parameters "
                            + "(owner, value)", setter);
                } else {
                    setterMethod = setter.getSimpleName().toString();
                    setterValueType = ClassName.bestGuess(TypeUtils.boxed(
                        TypeUtils.paramType(setter.getParameters().get(1).asType().toString())));
                    setterRest = !TypeUtils.isVoid(setter.getReturnType())
                        && TypeUtils.isRestAction(processingEnv, setter.getReturnType());
                }
            }

            sharedContributions.add(new SharedContribution(
                p.value(), codeName, paramType, ClassName.get(descriptor),
                m.getSimpleName().toString(), returnBoxed, setterMethod, setterValueType, setterRest));
        }

        for (Element e : descriptor.getEnclosedElements()) {
            if (e.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) e;
            Property.Setter s = m.getAnnotation(Property.Setter.class);
            if (s == null || !s.shared()) continue;
            if (findSharedGetter(descriptor, s.value()) != null) continue;
            if (m.getParameters().size() != 2) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "[DiSkyProcessor] a shared @Property.Setter must take exactly two parameters "
                        + "(owner, value)", m);
                continue;
            }
            ClassName paramType = ClassName.bestGuess(
                TypeUtils.stripAnnotations(m.getParameters().get(0).asType().toString()));
            ClassName valueType = ClassName.bestGuess(TypeUtils.boxed(
                TypeUtils.paramType(m.getParameters().get(1).asType().toString())));
            boolean rest = !TypeUtils.isVoid(m.getReturnType())
                && TypeUtils.isRestAction(processingEnv, m.getReturnType());
            sharedContributions.add(new SharedContribution(
                s.value(), codeName, paramType, ClassName.get(descriptor),
                null, valueType.toString(), m.getSimpleName().toString(), valueType, rest));
        }
    }

    private static ExecutableElement findSharedSetter(TypeElement descriptor, String name) {
        for (Element e : descriptor.getEnclosedElements()) {
            if (e.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) e;
            Property.Setter s = m.getAnnotation(Property.Setter.class);
            if (s != null && s.shared() && s.value().equals(name)) return m;
        }
        return null;
    }

    private static ExecutableElement findSharedGetter(TypeElement descriptor, String name) {
        for (Element e : descriptor.getEnclosedElements()) {
            if (e.getKind() != ElementKind.METHOD) continue;
            ExecutableElement m = (ExecutableElement) e;
            Property p = m.getAnnotation(Property.class);
            if (p != null && p.shared() && p.value().equals(name)) return m;
        }
        return null;
    }

    private void collectSharedDocs(RoundEnvironment round) {
        for (Element e : sortedAnnotated(round, SharedProperty.class)) {
            if (e.getKind() != ElementKind.METHOD) continue;
            SharedProperty sp = e.getAnnotation(SharedProperty.class);
            Documentation d = e.getAnnotation(Documentation.class);
            if (d != null)
                sharedDocs.put(sp.value(), new SharedDoc(d.name(), d.description(), d.examples(), d.since()));
        }
    }

    private void writeSharedProperties() {
        Map<String, List<SharedContribution>> byName = new LinkedHashMap<>();
        for (SharedContribution c : sharedContributions)
            byName.computeIfAbsent(c.sharedName(), k -> new ArrayList<>()).add(c);
        runWriter("GeneratedTypeProperties", () -> TypePropertyWriter.write(processingEnv, byName, sharedDocs));
    }

    private Set<String> computeMultiTypeNames(RoundEnvironment round) {
        Map<String, Set<String>> nameToDescriptors = new LinkedHashMap<>();
        for (Element el : sortedAnnotated(round, SkriptType.class)) {
            if (el.getKind() != ElementKind.CLASS) continue;
            String dfqn = ((TypeElement) el).getQualifiedName().toString();
            for (Element e : el.getEnclosedElements()) {
                if (e.getKind() != ElementKind.METHOD) continue;
                Property p = e.getAnnotation(Property.class);
                if (p != null && !p.shared())
                    nameToDescriptors.computeIfAbsent(p.value(), k -> new LinkedHashSet<>()).add(dfqn);
                Property.Retrieve r = e.getAnnotation(Property.Retrieve.class);
                if (r != null)
                    nameToDescriptors.computeIfAbsent(r.value(), k -> new LinkedHashSet<>()).add(dfqn);
                Property.Setter st = e.getAnnotation(Property.Setter.class);
                if (st != null && !st.shared())
                    nameToDescriptors.computeIfAbsent(st.value(), k -> new LinkedHashSet<>()).add(dfqn);
            }
        }
        Set<String> multi = new LinkedHashSet<>();
        for (Map.Entry<String, Set<String>> en : nameToDescriptors.entrySet())
            if (en.getValue().size() >= 2) multi.add(en.getKey());
        return multi;
    }

    private void writeMultiTypeProperties() {
        if (multiTypeContributions.isEmpty()) return;
        Map<String, List<MultiTypeContribution>> byName = new LinkedHashMap<>();
        for (MultiTypeContribution c : multiTypeContributions)
            byName.computeIfAbsent(c.name(), k -> new ArrayList<>()).add(c);
        runWriter("union (multi-type) properties", () -> MultiTypePropertyWriter.write(processingEnv, byName, generatedClasses));
    }

    private List<TypeInfo> orderParentsFirst() {
        List<TypeInfo> result = new ArrayList<>();
        Set<String> emitted = new java.util.HashSet<>();
        for (String fqn : typesByDescriptor.keySet())
            emitInOrder(fqn, result, emitted, new java.util.HashSet<>());
        return result;
    }

    private void emitInOrder(String fqn, List<TypeInfo> result, Set<String> emitted, Set<String> path) {
        if (fqn == null || emitted.contains(fqn)) return;
        TypeInfo info = typesByDescriptor.get(fqn);
        if (info == null) return;
        if (!path.add(fqn)) return;
        emitInOrder(info.parentDescriptorFqn(), result, emitted, path);
        if (emitted.add(fqn))
            result.add(info);
        path.remove(fqn);
    }

    public record TypeInfo(ClassName jdaType, String codeName, String userPattern,
                           String displayName, String parentDescriptorFqn) {}

    public record EnumInfo(ClassName jdaType, String codeName, String userPattern,
                           String displayName, ClassName holder, String aliasMethod) {}

    public record ConverterInfo(TypeName from, TypeName to, ClassName owner, String method) {}

    public record ComparatorInfo(TypeName t1, TypeName t2, ClassName owner, String method, boolean returnsBoolean) {}

    public record SharedContribution(String sharedName, String codeName, ClassName paramType,
                                     ClassName owner, String method, String returnTypeBoxed,
                                     String setterMethod, ClassName setterValueType, boolean setterRest) {
        public boolean settable() { return setterMethod != null; }
    }

    public record SharedDoc(String name, String[] description, String[] examples, String[] since) {}

    public record MultiTypeContribution(String name, String codeName, ClassName jdaType, ClassName descriptor,
                                        String getter, String elementTypeFqn, boolean multi, boolean array,
                                        String setter, ClassName setterValueType, boolean setterArray, boolean setterRest,
                                        String adder, ClassName adderValueType, boolean adderArray, boolean adderRest,
                                        String remover, ClassName removerValueType, boolean removerArray, boolean removerRest,
                                        String retrieve, boolean retrieveRest) {}

    public record EntityRecord(String descriptorFqn, String id, String name, String codeName,
                               String jdaTypeFqn, String parentDescriptorFqn,
                               String description, String since) {}

    public record SyntaxRecord(String entityId, String name, String kind, String returnTypeFqn,
                               boolean returnList, List<String> changeModes,
                               boolean awaitable, boolean restBacked, boolean retrieveOnly,
                               boolean shared, boolean writeOnly,
                               String docName, List<String> description,
                               List<String> examples, String since, List<String> patterns) {}

    public record EventRegistration(String name, List<String> patterns, ClassName skriptEvent,
                                    ClassName bukkitEvent, ClassName descriptor, ClassName jdaType,
                                    List<EventValueReg> values,
                                    List<String> rawPatterns, String description, List<String> examples,
                                    String since, boolean cancellable, List<String> intents,
                                    List<EventDocValue> docValues,
                                    List<ClassName> expressionClasses, List<EventExprDoc> exprDocs) {}

    public record EventDocValue(String name, String typeFqn, boolean list, int time) {}

    public record EventExprDoc(String pattern, String typeFqn, boolean list) {}

    public record EventValueReg(ClassName valueType, ClassName paramType, String method, int time,
                                boolean channelFilter, String restGetter) {}
}
