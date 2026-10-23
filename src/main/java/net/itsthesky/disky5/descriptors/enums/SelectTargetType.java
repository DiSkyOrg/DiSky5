package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.components.selections.EntitySelectMenu.SelectTarget;
import net.itsthesky.disky5.annotations.EnumAliases;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

import java.util.Map;

@SkriptEnum(name = "selecttarget", jda = SelectTarget.class)
@Documentation(
    name = "Select Target",
    description = "What an entity dropdown lets the user pick: users, roles or channels.",
    examples = "set {_d} to a new entity dropdown with id \"pick\" targeting users and roles",
    since = "5.0.0-a1")
public final class SelectTargetType {

    @EnumAliases
    public static Map<SelectTarget, String[]> aliases() {
        return Map.of(
            SelectTarget.USER, new String[]{"users", "members", "member"},
            SelectTarget.ROLE, new String[]{"roles"},
            SelectTarget.CHANNEL, new String[]{"channels"});
    }
}
