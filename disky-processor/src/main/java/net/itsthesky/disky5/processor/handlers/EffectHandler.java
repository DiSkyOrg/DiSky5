package net.itsthesky.disky5.processor.handlers;

import com.squareup.javapoet.ClassName;
import net.itsthesky.disky5.annotations.Effect;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.processor.util.Log;
import net.itsthesky.disky5.processor.util.NameUtils;
import net.itsthesky.disky5.processor.util.TypeUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class EffectHandler {

    private EffectHandler() {}

    private static final Map<String, String> BUILTIN_TYPES = Map.of(
        "string",   "java.lang.String",
        "text",     "java.lang.String",
        "number",   "java.lang.Number",
        "integer",  "java.lang.Number",
        "boolean",  "java.lang.Boolean",
        "timespan", "ch.njol.skript.util.Timespan",
        "date",     "ch.njol.skript.util.Date",
        "object",   "java.lang.Object"
    );

    public static Map<String, EffectSpec> newSink() {
        return new LinkedHashMap<>();
    }

    public static void collect(ProcessingEnvironment env, ExecutableElement method, Effect effect,
                               Map<String, String> jdaFqnByCodeName, Map<String, EffectSpec> sink) {
        List<String> patterns = List.of(effect.value());
        if (patterns.isEmpty()) {
            Log.error(env, "@Effect needs at least one pattern", method);
            return;
        }

        List<Param> params = params(method);
        List<Slot> slots = validateBinding(env, method, patterns, params, jdaFqnByCodeName, "@Effect");
        if (slots == null) return;

        boolean rest = !TypeUtils.isVoid(method.getReturnType())
            && TypeUtils.isRestAction(env, method.getReturnType());
        if (effect.reason()) {
            if (!isAuditable(env, method.getReturnType())) {
                Log.error(env, "@Effect(reason = true) needs the method to return an "
                    + "AuditableRestAction (the reason is applied to the request)", method);
                return;
            }
        }

        String className = "Eff" + NameUtils.toClassName(humanName(method));
        EffectSpec existing = sink.get(className);
        if (existing != null) {
            Log.error(env, "two @Effect methods would generate " + className + " ("
                + existing.owner().simpleName() + "#" + existing.method() + " and "
                + method.getEnclosingElement().getSimpleName() + "#" + method.getSimpleName()
                + "); rename one so each effect keeps a stable class name", method);
            return;
        }

        sink.put(className, new EffectSpec(
            className,
            ClassName.get((TypeElement) method.getEnclosingElement()),
            method.getSimpleName().toString(),
            humanName(method),
            patterns,
            slots,
            params,
            rest,
            effect.reason(),
            subjectEntity(slots, jdaFqnByCodeName),
            method.getAnnotation(Documentation.class)));
    }

    public static List<Param> params(ExecutableElement method) {
        List<Param> params = new ArrayList<>();
        for (VariableElement p : method.getParameters()) {
            String raw = TypeUtils.paramType(p.asType().toString());
            boolean array = TypeUtils.isArray(raw);
            String element = TypeUtils.boxed(array ? TypeUtils.arrayComponent(raw) : raw);
            params.add(new Param(p.getSimpleName().toString(), ClassName.bestGuess(element), array,
                p.asType().getKind().isPrimitive()));
        }
        return params;
    }

    public static List<Slot> validateBinding(ProcessingEnvironment env, ExecutableElement method,
                                             List<String> patterns, List<Param> params,
                                             Map<String, String> jdaFqnByCodeName, String what) {
        List<Slot> slots = slots(patterns.get(0));
        if (slots.size() != params.size()) {
            Log.error(env, what + " pattern has " + slots.size() + " slot(s) but the method takes "
                + params.size() + " parameter(s); each %...% binds to one parameter, in order", method);
            return null;
        }
        for (int i = 1; i < patterns.size(); i++) {
            if (slots(patterns.get(i)).size() != slots.size()) {
                Log.error(env, "all patterns of one " + what + " must bind the same parameters; pattern "
                    + i + " does not. Declare it as its own method instead", method);
                return null;
            }
        }
        for (int i = 0; i < slots.size(); i++)
            checkSlot(env, method, slots.get(i), params.get(i), jdaFqnByCodeName);
        return slots;
    }

    private static void checkSlot(ProcessingEnvironment env, ExecutableElement method,
                                  Slot slot, Param param, Map<String, String> jdaFqnByCodeName) {
        if (slot.optional() && param.primitive()) {
            Log.error(env, "the optional slot %" + slot.raw() + "% binds to primitive parameter '"
                + param.name() + "'; an unset optional is null, so the parameter must be boxed",
                method);
            return;
        }

        TypeMirror target = env.getElementUtils().getTypeElement(param.element().toString()) == null
            ? null : env.getElementUtils().getTypeElement(param.element().toString()).asType();
        if (target == null) return;

        for (String alternative : slot.alternatives()) {
            String fqn = resolveSlotType(alternative, jdaFqnByCodeName);
            if (fqn == null) return;
            TypeElement produced = env.getElementUtils().getTypeElement(fqn);
            if (produced == null) return;
            if (!env.getTypeUtils().isAssignable(produced.asType(), target)) {
                Log.error(env, "slot %" + slot.raw() + "% yields " + fqn + ", which parameter '"
                    + param.name() + "' (" + param.element() + ") cannot accept; slots bind to "
                    + "parameters in order", method);
                return;
            }
        }
    }

    private static String resolveSlotType(String name, Map<String, String> jdaFqnByCodeName) {
        String lower = name.toLowerCase(Locale.ROOT);
        String direct = jdaFqnByCodeName.get(lower);
        if (direct != null) return direct;
        String builtin = BUILTIN_TYPES.get(lower);
        if (builtin != null) return builtin;
        if (lower.endsWith("s")) {
            String singular = lower.substring(0, lower.length() - 1);
            String plural = jdaFqnByCodeName.get(singular);
            if (plural != null) return plural;
            return BUILTIN_TYPES.get(singular);
        }
        return null;
    }

    private static String subjectEntity(List<Slot> slots, Map<String, String> jdaFqnByCodeName) {
        if (slots.isEmpty()) return null;
        List<String> alternatives = slots.get(0).alternatives();
        if (alternatives.size() != 1) return null;
        String name = alternatives.get(0).toLowerCase(Locale.ROOT);
        if (jdaFqnByCodeName.containsKey(name)) return name;
        String singular = name.endsWith("s") ? name.substring(0, name.length() - 1) : null;
        return singular != null && jdaFqnByCodeName.containsKey(singular) ? singular : null;
    }

    private static boolean isAuditable(ProcessingEnvironment env, TypeMirror type) {
        TypeMirror erased = env.getTypeUtils().erasure(type);
        return erased.toString().equals("net.dv8tion.jda.api.requests.restaction.AuditableRestAction");
    }

    public static String humanName(ExecutableElement method) {
        return method.getSimpleName().toString()
            .replaceAll("([a-z0-9])([A-Z])", "$1 $2")
            .toLowerCase(Locale.ROOT);
    }

    public static List<Slot> slots(String pattern) {
        List<Slot> slots = new ArrayList<>();
        int i = 0;
        while (i < pattern.length()) {
            int open = pattern.indexOf('%', i);
            if (open < 0) break;
            int close = pattern.indexOf('%', open + 1);
            if (close < 0) break;
            String raw = pattern.substring(open + 1, close);
            if (!raw.isEmpty())
                slots.add(slot(raw));
            i = close + 1;
        }
        return slots;
    }

    private static Slot slot(String raw) {
        String body = raw;
        boolean optional = false;
        while (!body.isEmpty() && "-*~^".indexOf(body.charAt(0)) >= 0) {
            optional |= body.charAt(0) == '-';
            body = body.substring(1);
        }
        return new Slot(raw, optional, List.of(body.split("/")));
    }

    public record Slot(String raw, boolean optional, List<String> alternatives) {}

    public record Param(String name, ClassName element, boolean array, boolean primitive) {}

    public record EffectSpec(String className, ClassName owner, String method, String humanName,
                             List<String> patterns, List<Slot> slots, List<Param> params,
                             boolean rest, boolean reason, String subjectEntity, Documentation docs) {}
}
