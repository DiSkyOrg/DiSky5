package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public final class SubcommandGroupHolder {

    private final SubcommandGroupData data;

    public SubcommandGroupHolder(SubcommandGroupData data) {
        this.data = data;
    }

    public static SubcommandGroupHolder from(SubcommandGroupData data) {
        return new SubcommandGroupHolder(data);
    }

    public SubcommandGroupData data() { return data; }

    public SubcommandGroupData build() { return data; }

    @Override
    public String toString() {
        return "subcommand group '" + data.getName() + "'";
    }
}
