package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.Permission;
import net.itsthesky.disky5.annotations.EnumAliases;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.Map;

@SkriptEnum(name = "permission", jda = Permission.class)
@Documentation(
    name = "Permission",
    description = "A Discord permission. Accepts both the canonical name (e.g. \"message manage\") and "
        + "friendly aliases (e.g. \"manage messages\").",
    examples = "if event-member has discord permission manage messages:",
    since = "5.0.0-a1")
public final class PermissionType {

    @EnumAliases
    public static Map<Permission, String[]> aliases() {
        return Map.ofEntries(
            Map.entry(Permission.MESSAGE_MANAGE, new String[]{"manage messages"}),
            Map.entry(Permission.MESSAGE_SEND, new String[]{"send messages"}),
            Map.entry(Permission.MESSAGE_HISTORY, new String[]{"read message history", "message history"}),
            Map.entry(Permission.MESSAGE_EMBED_LINKS, new String[]{"embed links"}),
            Map.entry(Permission.MESSAGE_ATTACH_FILES, new String[]{"attach files"}),
            Map.entry(Permission.MESSAGE_ADD_REACTION, new String[]{"add reactions"}),
            Map.entry(Permission.VOICE_CONNECT, new String[]{"connect"}),
            Map.entry(Permission.VOICE_SPEAK, new String[]{"speak"}),
            Map.entry(Permission.MANAGE_PERMISSIONS, new String[]{"manage permissions"})
        );
    }
}
