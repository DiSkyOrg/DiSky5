package net.itsthesky.disky5.elements.structures;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.itsthesky.disky5.api.commands.application.ApplicationCommandRegistry;
import net.itsthesky.disky5.api.commands.application.ContextCommandModel;
import net.itsthesky.disky5.api.syntax.ManualSyntax;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_MessageCommand;
import net.itsthesky.disky5.generated.events.GeneratedBukkitEvt_UserCommand;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.SectionEntryData;

@Name("Context Command Structure")
@Description({
    "Declares a user or message context-menu command (Apps > name when right-clicking a user or a message) "
        + "and the trigger that runs when it is used: 'target user' / 'target message' give what was clicked.",
    "Entries: name (a section of translated names), enabled for, disabled, nsfw, contexts, integration types, "
        + "cooldown + cooldown scope + on cooldown, trigger. The name may contain spaces and capitals.",
    "Nothing is sent to Discord: register it with 'update the discord commands (all structured application "
        + "commands) in ...' from 'on ready' / 'on guild ready'."
})
@Examples({
    "user command Report user:",
    "    trigger:",
    "        reply with hidden \"Reported %target user%\"",
    "",
    "message command Pin this:",
    "    enabled for: manage messages",
    "    trigger:",
    "        pin target message",
    "        reply with hidden \"Pinned!\""
})
@Since("5.0.0-a2")
public final class StructContextCommand extends ApplicationCommandStructure {

    public static void register() {
        ManualSyntax.structure(StructContextCommand.class,
            commonEntries(EntryValidator.builder()
                .addEntryData(new SectionEntryData("trigger", null, false)))
                .unexpectedEntryMessage(key -> "Unexpected entry '" + key + "' in a context command. Known entries: "
                    + "name, enabled for, disabled, nsfw, contexts, integration types, cooldown, cooldown scope, "
                    + "on cooldown, trigger.")
                .build(),
            "user command <(.+)$>",
            "message command <(.+)$>"
        );
    }

    private Command.Type type;
    private String name;
    private @Nullable ContextCommandModel model;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern,
                        SkriptParser.@NotNull ParseResult parseResult, @Nullable EntryContainer entryContainer) {
        type = matchedPattern == 0 ? Command.Type.USER : Command.Type.MESSAGE;
        name = parseResult.regexes.get(0).group(1).trim();
        if (name.isEmpty() || name.length() > 32) {
            Skript.error("A context command name is 1 to 32 characters long");
            return false;
        }
        return true;
    }

    @Override
    public boolean load() {
        EntryContainer container = getEntryContainer();
        String what = type.name().toLowerCase() + " command '" + name + "'";
        ContextCommandModel model = new ContextCommandModel(getParser().getCurrentScript(), type, name);
        if (!loadCommon(container, model, what, null))
            return false;
        SectionNode triggerNode = container.get("trigger", SectionNode.class, false);
        Class<? extends Event> eventClass = type == Command.Type.USER ? GeneratedBukkitEvt_UserCommand.class : GeneratedBukkitEvt_MessageCommand.class;
        model.trigger(loadTrigger(triggerNode, type.name().toLowerCase() + " command", eventClass, what, null));
        this.model = model;
        return true;
    }

    @Override
    public boolean postLoad() {
        if (model != null)
            ApplicationCommandRegistry.INSTANCE.register(model);
        return true;
    }

    @Override
    public void unload() {
        if (model != null)
            ApplicationCommandRegistry.INSTANCE.unregister(model);
        model = null;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return type.name().toLowerCase() + " command " + name;
    }
}
