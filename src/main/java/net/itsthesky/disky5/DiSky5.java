package net.itsthesky.disky5;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptConfig;
import dev.faststats.ErrorTracker;
import dev.faststats.Metrics;
import dev.faststats.bukkit.BukkitContext;
import net.dv8tion.jda.api.requests.RestAction;
import net.itsthesky.disky5.api.syntax.DiSkySyntaxRegistry;
import net.itsthesky.disky5.api.syntax.ManifestJson;
import net.itsthesky.disky5.api.syntax.SyntaxManifest;
import net.itsthesky.disky5.api.async.EffAwait;
import net.itsthesky.disky5.core.Bot;
import net.itsthesky.disky5.core.BotManager;
import net.itsthesky.disky5.api.syntax.SyntaxCollisionCheck;
import net.itsthesky.disky5.core.DiSkyRuntimeHandler;
import net.itsthesky.disky5.elements.effects.EffLogin;
import net.itsthesky.disky5.elements.effects.EffDeferInteraction;
import net.itsthesky.disky5.elements.effects.EffEditInteractionComponents;
import net.itsthesky.disky5.elements.effects.EffReplaceComponent;
import net.itsthesky.disky5.elements.effects.EffReplyWith;
import net.itsthesky.disky5.elements.effects.EffShowModal;
import net.itsthesky.disky5.elements.effects.EffShutdownBot;
import net.itsthesky.disky5.elements.expressions.ExprBots;
import net.itsthesky.disky5.elements.expressions.ExprLastError;
import net.itsthesky.disky5.elements.expressions.ExprModalValue;
import net.itsthesky.disky5.elements.expressions.ExprNewBot;
import net.itsthesky.disky5.elements.expressions.ExprNewEmbed;
import net.itsthesky.disky5.elements.expressions.ExprNewEmbedField;
import net.itsthesky.disky5.elements.expressions.ExprNewFileUpload;
import net.itsthesky.disky5.elements.expressions.ExprRecommendedShardCount;
import net.itsthesky.disky5.elements.sections.SecTry;
import net.itsthesky.disky5.elements.structures.StructBot;
import net.itsthesky.disky5.elements.structures.StructDiscordCommand;
import net.itsthesky.disky5.elements.expressions.ExprCommandArgument;
import net.itsthesky.disky5.elements.expressions.ExprDiscordCommands;
import net.itsthesky.disky5.api.commands.CommandDispatcher;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandDispatcher;
import net.itsthesky.disky5.api.commands.application.SlashArgumentTypes;
import net.itsthesky.disky5.elements.effects.commands.EffLocalizeCommand;
import net.itsthesky.disky5.elements.effects.commands.EffRegisterCommands;
import net.itsthesky.disky5.elements.effects.commands.EffReturnChoices;
import net.itsthesky.disky5.elements.effects.commands.EffUnregisterCommands;
import net.itsthesky.disky5.elements.effects.commands.EffUpdateCommands;
import net.itsthesky.disky5.elements.expressions.commands.ExprNewTypedSlashOption;
import net.itsthesky.disky5.elements.expressions.commands.ExprRegisteredCommands;
import net.itsthesky.disky5.elements.expressions.commands.ExprSlashOptionValue;
import net.itsthesky.disky5.elements.expressions.commands.ExprStructuredCommands;
import net.itsthesky.disky5.elements.expressions.getters.ExprEmoji;
import net.itsthesky.disky5.elements.expressions.getters.ExprMemberOfUser;
import net.itsthesky.disky5.elements.expressions.getters.ExprStickerNamed;
import net.itsthesky.disky5.elements.expressions.messages.ExprMessageHistory;
import net.itsthesky.disky5.elements.expressions.permissions.ExprChannelPermissions;
import net.itsthesky.disky5.elements.expressions.permissions.ExprPermissionOverride;
import net.itsthesky.disky5.elements.expressions.polls.ExprPollVoters;
import net.itsthesky.disky5.elements.structures.StructContextCommand;
import net.itsthesky.disky5.elements.structures.StructSlashCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.skriptlang.skript.addon.SkriptAddon;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public final class DiSky5 extends JavaPlugin {

    private static DiSky5 instance;
    private static SkriptAddon addon;
    private static final BotManager bots = new BotManager();

    public static DiSky5 getInstance() {
        return instance;
    }

    public static SkriptAddon getAddon() {
        return addon;
    }

    public static BotManager getBots() {
        return bots;
    }

    public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextUnaware();
    private final BukkitContext context = new BukkitContext.Factory(this, "5a8d4fa4d9ef2d6222b0e52a4a3ef98a")
            .metrics(Metrics.Factory::create)
            .errorTrackerService(ERROR_TRACKER)
            .create();

    @Override
    public void onEnable() {
        instance = this;
        context.ready();

        if (!getServer().getPluginManager().isPluginEnabled("Skript")) {
            getLogger().severe("Skript is not installed or failed to enable; cannot start DiSky.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        addon = Skript.instance().registerAddon(DiSky5.class, "DiSky");

        try {
            createRegisteringSpace(() -> {
                invokeStatic("net.itsthesky.disky5.generated.GeneratedTypes", "register");
                invokeStatic("net.itsthesky.disky5.generated.GeneratedEnums", "register");
                invokeRegisterTypeProperties(addon);
                invokeStatic("net.itsthesky.disky5.generated.GeneratedConverters", "register");
                invokeStatic("net.itsthesky.disky5.generated.GeneratedComparators", "register");
                invokeStatic("net.itsthesky.disky5.generated.GeneratedElements", "registerAll");
                invokeStatic("net.itsthesky.disky5.generated.GeneratedSyntaxManifest", "register");
                EffAwait.register();

                ExprNewBot.register();
                EffLogin.register();
                EffShutdownBot.register();
                ExprRecommendedShardCount.register();
                StructBot.register();

                invokeStatic("net.itsthesky.disky5.generated.GeneratedEvents", "registerAll");

                SecTry.register();
                ExprLastError.register();

                ExprBots.register();

                ExprNewEmbed.register();
                ExprNewEmbedField.register();

                EffReplyWith.register();
                ExprNewFileUpload.register();

                EffShowModal.register();
                EffDeferInteraction.register();
                EffEditInteractionComponents.register();
                EffReplaceComponent.register();
                ExprModalValue.register();

                StructDiscordCommand.register();
                ExprCommandArgument.register();
                ExprDiscordCommands.register();
                CommandDispatcher.install();

                StructSlashCommand.register();
                StructContextCommand.register();
                ExprStructuredCommands.register();
                ExprRegisteredCommands.register();
                ExprPollVoters.register();
                ExprMessageHistory.register();
                ExprChannelPermissions.register();
                ExprPermissionOverride.register();
                ExprEmoji.register();
                ExprStickerNamed.register();
                ExprMemberOfUser.register();
                EffUpdateCommands.register();
                EffRegisterCommands.register();
                EffUnregisterCommands.register();
                EffLocalizeCommand.register();
                ExprSlashOptionValue.register();
                EffReturnChoices.register();
                ExprNewTypedSlashOption.register();
                ApplicationCommandDispatcher.install();
            });
        } catch (Throwable t) {
            getLogger().severe("Failed to register DiSky syntax: " + t.getMessage());
            t.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        RestAction.setDefaultFailure(t -> DiSkyRuntimeHandler.error(t));

        // Skript stops accepting registrations in a task of its own; this one runs after it, so every
        // addon's enums are known when they become slash argument types.
        getServer().getScheduler().runTask(this, SlashArgumentTypes::registerSkriptEnums);

        warnIfTypePropertiesDisabled();

        SyntaxCollisionCheck.run();

        exportSyntaxManifest();

        getLogger().info("DiSky5 enabled: types, properties, bot lifecycle and error layer registered.");
    }

    private void exportSyntaxManifest() {
        try {
            SyntaxManifest manifest = DiSkySyntaxRegistry.INSTANCE.snapshot(
                getDescription().getVersion(), java.time.Instant.now().toString());
            java.nio.file.Path out = getDataFolder().toPath().resolve("disky-syntax.json");
            java.nio.file.Files.createDirectories(out.getParent());
            java.nio.file.Files.writeString(out, ManifestJson.toJson(manifest));
            int entitySyntaxes = manifest.entities().stream().mapToInt(e -> e.syntaxes().size()).sum();
            getLogger().info("Exported syntax manifest: " + manifest.entities().size() + " entities, "
                + entitySyntaxes + " entity syntaxes + " + manifest.core().size() + " core → " + out + ".");
        } catch (Exception ex) {
            getLogger().warning("Failed to export syntax manifest: " + ex.getMessage());
        }
    }

    private static void invokeStatic(String fqn, String method) {
        try {
            Class<?> clazz = Class.forName(fqn, true, DiSky5.class.getClassLoader());
            Method m = clazz.getDeclaredMethod(method);
            m.invoke(null);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("[DiSky] generated aggregator unavailable: " + fqn + "#" + method, ex);
        }
    }

    private static void invokeRegisterTypeProperties(SkriptAddon addon) {
        try {
            Class<?> clazz = Class.forName(
                "net.itsthesky.disky5.generated.GeneratedTypeProperties", true, DiSky5.class.getClassLoader());
            Method m = clazz.getDeclaredMethod("register", SkriptAddon.class);
            m.invoke(null, addon);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("[DiSky] GeneratedTypeProperties#register unavailable", ex);
        }
    }

    private void warnIfTypePropertiesDisabled() {
        if (!Boolean.TRUE.equals(SkriptConfig.useTypeProperties.value())) {
            getLogger().warning("Skript's 'use type properties' option is DISABLED in your config.sk. "
                + "DiSky's shared syntaxes (e.g. \"name of %member%\") rely on it and will NOT parse. "
                + "It is ON by default in Skript 2.15+ — remove the override or set 'use type properties: true'.");
        }
    }

    @Override
    public void onDisable() {
        context.shutdown();

        for (Bot bot : new ArrayList<>(bots.all())) {
            try {
                bot.shutdown();
            } catch (Throwable t) {
                getLogger().warning("Error shutting down bot '" + bot.getName() + "': " + t.getMessage());
            }
        }
    }

    private static void createRegisteringSpace(Runnable code) throws Exception {
        if (Skript.isAcceptRegistrations()) {
            code.run();
            return;
        }

        Field field = Skript.class.getDeclaredField("acceptRegistrations");
        field.setAccessible(true);
        boolean previous = field.getBoolean(null);
        try {
            field.setBoolean(null, true);
            code.run();
        } finally {
            field.setBoolean(null, previous);
        }
    }
}
