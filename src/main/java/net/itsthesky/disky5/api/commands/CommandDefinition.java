package net.itsthesky.disky5.api.commands;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.util.Timespan;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.script.Script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class CommandDefinition {

    private final Script script;
    private final String name;
    private final List<String> aliases;
    private final List<Expression<? extends String>> prefixes;
    private final List<CommandArgument> arguments;
    private final @Nullable String description;
    private final @Nullable String usage;
    private final @Nullable String category;
    private final Set<String> roles;
    private final Set<Permission> permissions;
    private final Set<ChannelType> executableIn;
    private final Set<String> bots;
    private final CooldownTracker cooldowns;
    private final @Nullable VariableString permissionMessage;
    private final @Nullable VariableString cooldownMessage;
    private final @Nullable VariableString usageMessage;
    private final Trigger trigger;

    private CommandDefinition(Builder b) {
        this.script = b.script;
        this.name = b.name;
        this.aliases = List.copyOf(b.aliases);
        this.prefixes = List.copyOf(b.prefixes);
        this.arguments = List.copyOf(b.arguments);
        this.description = b.description;
        this.usage = b.usage;
        this.category = b.category;
        this.roles = Collections.unmodifiableSet(new LinkedHashSet<>(b.roles));
        this.permissions = b.permissions.isEmpty() ? Set.of() : Collections.unmodifiableSet(EnumSet.copyOf(b.permissions));
        this.executableIn = Collections.unmodifiableSet(EnumSet.copyOf(b.executableIn));
        this.bots = Collections.unmodifiableSet(new LinkedHashSet<>(b.bots));
        this.cooldowns = new CooldownTracker(b.cooldown, b.cooldownScope);
        this.permissionMessage = b.permissionMessage;
        this.cooldownMessage = b.cooldownMessage;
        this.usageMessage = b.usageMessage;
        this.trigger = b.trigger;
    }

    public Script script() { return script; }
    public String name() { return name; }
    public List<String> aliases() { return aliases; }
    public List<CommandArgument> arguments() { return arguments; }
    public @Nullable String description() { return description; }
    public @Nullable String usage() { return usage; }
    public @Nullable String category() { return category; }
    public Set<String> roles() { return roles; }
    public Set<Permission> permissions() { return permissions; }
    public Set<ChannelType> executableIn() { return executableIn; }
    public Set<String> bots() { return bots; }
    public @Nullable Timespan cooldown() { return cooldowns.cooldown(); }
    public CooldownTracker cooldowns() { return cooldowns; }
    public @Nullable VariableString permissionMessage() { return permissionMessage; }
    public @Nullable VariableString cooldownMessage() { return cooldownMessage; }
    public @Nullable VariableString usageMessage() { return usageMessage; }
    public Trigger trigger() { return trigger; }

    public List<String> names() {
        List<String> names = new ArrayList<>(aliases.size() + 1);
        names.add(name);
        names.addAll(aliases);
        return names;
    }

    public List<String> prefixes(@Nullable Event event) {
        if (prefixes.isEmpty())
            return List.of("");
        List<String> result = new ArrayList<>();
        for (Expression<? extends String> expression : prefixes)
            for (String prefix : expression.getArray(event))
                if (prefix != null)
                    result.add(prefix);
        return result;
    }

    public String defaultUsage() {
        StringBuilder sb = new StringBuilder(name);
        for (CommandArgument argument : arguments)
            sb.append(' ').append(argument);
        return sb.toString();
    }

    public static Builder builder(Script script, String name) {
        return new Builder(script, name);
    }

    public static final class Builder {
        private final Script script;
        private final String name;
        private final List<String> aliases = new ArrayList<>();
        private final List<Expression<? extends String>> prefixes = new ArrayList<>();
        private final List<CommandArgument> arguments = new ArrayList<>();
        private @Nullable String description;
        private @Nullable String usage;
        private @Nullable String category;
        private final Set<String> roles = new LinkedHashSet<>();
        private final Set<Permission> permissions = EnumSet.noneOf(Permission.class);
        private final Set<ChannelType> executableIn = EnumSet.noneOf(ChannelType.class);
        private final Set<String> bots = new LinkedHashSet<>();
        private @Nullable Timespan cooldown;
        private CooldownTracker.Scope cooldownScope = CooldownTracker.Scope.USER;
        private @Nullable VariableString permissionMessage;
        private @Nullable VariableString cooldownMessage;
        private @Nullable VariableString usageMessage;
        private Trigger trigger;

        private Builder(Script script, String name) {
            this.script = script;
            this.name = name;
        }

        public Builder aliases(List<String> aliases) {
            for (String alias : aliases)
                if (!alias.equalsIgnoreCase(name))
                    this.aliases.add(alias);
            return this;
        }
        public Builder prefixes(List<Expression<? extends String>> prefixes) { this.prefixes.addAll(prefixes); return this; }
        public Builder arguments(List<CommandArgument> arguments) { this.arguments.addAll(arguments); return this; }
        public Builder description(@Nullable String description) { this.description = description; return this; }
        public Builder usage(@Nullable String usage) { this.usage = usage; return this; }
        public Builder category(@Nullable String category) { this.category = category; return this; }
        public Builder roles(List<String> roles) { this.roles.addAll(roles); return this; }
        public Builder permissions(Set<Permission> permissions) { this.permissions.addAll(permissions); return this; }
        public Builder executableIn(Set<ChannelType> types) { this.executableIn.addAll(types); return this; }
        public Builder bots(List<String> bots) { this.bots.addAll(bots); return this; }
        public Builder cooldown(@Nullable Timespan cooldown, CooldownTracker.Scope scope) { this.cooldown = cooldown; this.cooldownScope = scope; return this; }
        public Builder permissionMessage(@Nullable VariableString m) { this.permissionMessage = m; return this; }
        public Builder cooldownMessage(@Nullable VariableString m) { this.cooldownMessage = m; return this; }
        public Builder usageMessage(@Nullable VariableString m) { this.usageMessage = m; return this; }
        public Builder trigger(Trigger trigger) { this.trigger = trigger; return this; }

        public CommandDefinition build() {
            if (trigger == null)
                throw new IllegalStateException("a discord command needs a trigger");
            return new CommandDefinition(this);
        }
    }
}
