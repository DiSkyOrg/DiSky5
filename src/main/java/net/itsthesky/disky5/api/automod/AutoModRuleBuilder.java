package net.itsthesky.disky5.api.automod;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.automod.AutoModEventType;
import net.dv8tion.jda.api.entities.automod.AutoModResponse;
import net.dv8tion.jda.api.entities.automod.build.AutoModRuleData;
import net.dv8tion.jda.api.entities.automod.build.TriggerConfig;

public final class AutoModRuleBuilder extends AutoModRuleData {

    private final Guild guild;

    public AutoModRuleBuilder(Guild guild, AutoModEventType eventType, String name, TriggerConfig config) {
        super(eventType, name, config);
        this.guild = guild;
        if (eventType == AutoModEventType.MEMBER_UPDATE)
            putResponses(AutoModResponse.blockMemberInteraction());
    }

    public Guild guild() {
        return guild;
    }

    @Override
    public String toString() {
        return "automod rule builder";
    }
}
