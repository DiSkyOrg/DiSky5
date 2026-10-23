package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.entities.automod.AutoModResponse;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptEnum(name = "automodresponsetype", jda = AutoModResponse.Type.class)
@Documentation(
    name = "AutoMod Response Type",
    description = "The kind of action an automod rule takes: block message, send alert message, timeout or block member interaction.",
    examples = "if response type of {_response} is timeout:",
    since = "5.0.0-a2")
public final class AutoModResponseTypeType {
}
