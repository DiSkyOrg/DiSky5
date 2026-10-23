package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public final class SubcommandHolder {

    private final SubcommandData data;

    public SubcommandHolder(SubcommandData data) {
        this.data = data;
    }

    public static SubcommandHolder from(SubcommandData data) {
        return new SubcommandHolder(data);
    }

    public SubcommandData data() { return data; }

    public SubcommandData build() { return data; }

    @Override
    public String toString() {
        return "subcommand '" + data.getName() + "'";
    }
}
