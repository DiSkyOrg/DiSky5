package net.itsthesky.disky5.processor.util;

import com.squareup.javapoet.ClassName;

public final class SkriptTypes {
    private SkriptTypes() {}

    public static final ClassName SIMPLE_PROPERTY_EXPRESSION    = ClassName.get("ch.njol.skript.expressions.base", "SimplePropertyExpression");
    public static final ClassName CHANGER_MODE                  = ClassName.get("ch.njol.skript.classes", "Changer", "ChangeMode");
    public static final ClassName EVENT                         = ClassName.get("org.bukkit.event", "Event");
    public static final ClassName BUKKIT                        = ClassName.get("org.bukkit", "Bukkit");

    public static final ClassName SKRIPT_DOCS_NAME              = ClassName.get("ch.njol.skript.doc", "Name");
    public static final ClassName SKRIPT_DOCS_EXAMPLES          = ClassName.get("ch.njol.skript.doc", "Examples");
    public static final ClassName SKRIPT_DOCS_SINCE             = ClassName.get("ch.njol.skript.doc", "Since");
    public static final ClassName SKRIPT_DOCS_DESCRIPTION       = ClassName.get("ch.njol.skript.doc", "Description");

    public static final ClassName CLASSES                       = ClassName.get("ch.njol.skript.registrations", "Classes");
    public static final ClassName CLASS_INFO                    = ClassName.get("ch.njol.skript.classes", "ClassInfo");
    public static final ClassName PARSER                        = ClassName.get("ch.njol.skript.classes", "Parser");
    public static final ClassName PARSE_CONTEXT                 = ClassName.get("ch.njol.skript.lang", "ParseContext");

    public static final ClassName DISKY_ASYNC                   = ClassName.get("net.itsthesky.disky5.api.async", "DiSkyAsync");
    public static final ClassName DISKY_CHANGEABLE              = ClassName.get("net.itsthesky.disky5.api.async", "DiSkyChangeable");
    public static final ClassName DISKY_RUNTIME_HANDLER         = ClassName.get("net.itsthesky.disky5.core", "DiSkyRuntimeHandler");
    public static final ClassName CONVERSIONS                   = ClassName.get("net.itsthesky.disky5.core", "Conversions");
    public static final ClassName ASYNC_EFFECT                  = ClassName.get("net.itsthesky.disky5.api.async", "AsyncEffect");
    public static final ClassName NODE                          = ClassName.get("ch.njol.skript.config", "Node");

    public static final ClassName REST_ACTION                   = ClassName.get("net.dv8tion.jda.api.requests", "RestAction");

    public static final ClassName CONVERTERS                    = ClassName.get("org.skriptlang.skript.lang.converter", "Converters");
    public static final ClassName COMPARATORS                   = ClassName.get("org.skriptlang.skript.lang.comparator", "Comparators");
    public static final ClassName RELATION                      = ClassName.get("org.skriptlang.skript.lang.comparator", "Relation");

    public static final ClassName SIMPLE_EXPRESSION             = ClassName.get("ch.njol.skript.lang.util", "SimpleExpression");
    public static final ClassName EXPRESSION                    = ClassName.get("ch.njol.skript.lang", "Expression");
    public static final ClassName EXPRESSION_TYPE               = ClassName.get("ch.njol.skript.lang", "ExpressionType");
    public static final ClassName SKRIPT                        = ClassName.get("ch.njol.skript", "Skript");
    public static final ClassName KLEENEAN                      = ClassName.get("ch.njol.util", "Kleenean");
    public static final ClassName PARSE_RESULT                  = ClassName.get("ch.njol.skript.lang", "SkriptParser", "ParseResult");

    public static final ClassName PROPERTY                      = ClassName.get("org.skriptlang.skript.lang.properties", "Property");
    public static final ClassName PROPERTY_REGISTRY             = ClassName.get("org.skriptlang.skript.lang.properties", "PropertyRegistry");
    public static final ClassName EXPRESSION_PROPERTY_HANDLER   = ClassName.get("org.skriptlang.skript.lang.properties.handlers.base", "ExpressionPropertyHandler");
    public static final ClassName TYPED_VALUE_HANDLER           = ClassName.get("org.skriptlang.skript.lang.properties.handlers", "TypedValueHandler");
    public static final ClassName PROPERTY_BASE_EXPRESSION      = ClassName.get("org.skriptlang.skript.lang.properties", "PropertyBaseExpression");
    public static final ClassName SKRIPT_ADDON                  = ClassName.get("org.skriptlang.skript.addon", "SkriptAddon");

    public static final ClassName DISKY_EVENT                   = ClassName.get("net.itsthesky.disky5.api.events", "DiSkyEvent");
    public static final ClassName DISKY_BUKKIT_EVENT            = ClassName.get("net.itsthesky.disky5.api.events", "DiSkyBukkitEvent");
    public static final ClassName EVENT_VALUES                  = ClassName.get("ch.njol.skript.registrations", "EventValues");
    public static final ClassName GENERIC_EVENT                 = ClassName.get("net.dv8tion.jda.api.events", "GenericEvent");
    public static final ClassName BOT                           = ClassName.get("net.itsthesky.disky5.core", "Bot");
    public static final ClassName CANCELLABLE                   = ClassName.get("org.bukkit.event", "Cancellable");
    public static final ClassName CANCELLATION_AWARE            = ClassName.get("net.itsthesky.disky5.api.events", "CancellationAware");

    public static final ClassName DISKY5                        = ClassName.get("net.itsthesky.disky5", "DiSky5");
    public static final ClassName SHARD_MANAGER                 = ClassName.get("net.dv8tion.jda.api.sharding", "ShardManager");
    public static final ClassName GUILD                         = ClassName.get("net.dv8tion.jda.api.entities", "Guild");
    public static final ClassName MESSAGE_CHANNEL               = ClassName.get("net.dv8tion.jda.api.entities.channel.middleman", "MessageChannel");

    public static final ClassName CONDITION                     = ClassName.get("ch.njol.skript.lang", "Condition");
    public static final ClassName PROPERTY_CONDITION            = ClassName.get("ch.njol.skript.conditions.base", "PropertyCondition");
    public static final ClassName PROPERTY_TYPE                 = ClassName.get("ch.njol.skript.conditions.base", "PropertyCondition", "PropertyType");
}
