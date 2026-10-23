package net.itsthesky.disky5.api.commands.application;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.SimpleNode;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.entry.EntryData;

import java.util.Locale;

public final class NodeEntryData extends EntryData<Node> {

    public NodeEntryData(String key, boolean optional) {
        super(key, null, optional);
    }

    @Override
    public Node getValue(@NotNull Node node) {
        return node;
    }

    @Override
    public boolean canCreateWith(@NotNull Node node) {
        String key = node.getKey();
        if (key == null) return false;
        key = ScriptLoader.replaceOptions(key).trim().toLowerCase(Locale.ROOT);
        if (node instanceof SectionNode) return key.equals(getKey());
        return node instanceof SimpleNode && key.startsWith(getKey() + ":");
    }

    public static String valueOf(SimpleNode node, String key) {
        String text = ScriptLoader.replaceOptions(node.getKey()).trim();
        text = text.substring(key.length()).trim();
        if (text.startsWith(":")) text = text.substring(1).trim();
        if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\""))
            text = text.substring(1, text.length() - 1);
        return text;
    }
}
