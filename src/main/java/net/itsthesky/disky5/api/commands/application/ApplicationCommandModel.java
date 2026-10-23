package net.itsthesky.disky5.api.commands.application;

import ch.njol.skript.lang.Trigger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.itsthesky.disky5.api.commands.CooldownTracker;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.script.Script;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class ApplicationCommandModel {

    private final Script script;
    private final String name;
    private final Map<DiscordLocale, String> nameLocalizations = new LinkedHashMap<>();
    private final Set<Permission> enabledFor = EnumSet.noneOf(Permission.class);
    private boolean disabled;
    private boolean nsfw;
    private @Nullable Set<InteractionContextType> contexts;
    private @Nullable Set<IntegrationType> integrationTypes;
    private CooldownTracker cooldowns = CooldownTracker.NONE;
    private @Nullable Trigger onCooldown;
    private @Nullable Trigger trigger;

    protected ApplicationCommandModel(Script script, String name) {
        this.script = script;
        this.name = name;
    }

    public Script script() { return script; }
    public String name() { return name; }
    public Map<DiscordLocale, String> nameLocalizations() { return nameLocalizations; }
    public Set<Permission> enabledFor() { return enabledFor; }
    public boolean disabled() { return disabled; }
    public boolean nsfw() { return nsfw; }
    public @Nullable Set<InteractionContextType> contexts() { return contexts; }
    public @Nullable Set<IntegrationType> integrationTypes() { return integrationTypes; }
    public CooldownTracker cooldowns() { return cooldowns; }
    public @Nullable Trigger onCooldown() { return onCooldown; }
    public @Nullable Trigger trigger() { return trigger; }

    public void disabled(boolean disabled) { this.disabled = disabled; }
    public void nsfw(boolean nsfw) { this.nsfw = nsfw; }
    public void contexts(@Nullable Set<InteractionContextType> contexts) { this.contexts = contexts; }
    public void integrationTypes(@Nullable Set<IntegrationType> types) { this.integrationTypes = types; }
    public void cooldowns(CooldownTracker cooldowns) { this.cooldowns = cooldowns; }
    public void onCooldown(@Nullable Trigger onCooldown) { this.onCooldown = onCooldown; }
    public void trigger(@Nullable Trigger trigger) { this.trigger = trigger; }

    public DefaultMemberPermissions defaultPermissions() {
        if (!enabledFor.isEmpty()) return DefaultMemberPermissions.enabledFor(enabledFor);
        return disabled ? DefaultMemberPermissions.DISABLED : DefaultMemberPermissions.ENABLED;
    }

    public void applyTo(CommandData data) {
        if (!nameLocalizations.isEmpty()) data.setNameLocalizations(nameLocalizations);
        data.setDefaultPermissions(defaultPermissions());
        data.setNSFW(nsfw);
        if (contexts != null && !contexts.isEmpty()) data.setContexts(contexts);
        if (integrationTypes != null && !integrationTypes.isEmpty()) data.setIntegrationTypes(integrationTypes);
    }
}
