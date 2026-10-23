package net.itsthesky.disky5.descriptors.commands;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.commands.application.UserCommandHolder;

@SkriptType(name = "usercommand", jda = UserCommandHolder.class, parent = ApplicationCommandType.class)
@Documentation(
    name = "User Command",
    description = {"A context-menu command shown when right-clicking a user (Apps > name). It has a name and "
        + "nothing else; answer it in 'on user command' with 'target user', or declare it with the 'user command' structure."},
    since = "5.0.0-a2")
public final class UserCommandType {

    private UserCommandType() {}

    @New("[a] new user command (named|with [the] name) %string%")
    @Documentation(
        name = "New User Command",
        description = "Creates a user context-menu command. Its name may contain spaces and capitals (1 to 32 characters).",
        examples = {"set {_cmd} to a new user command named \"Report user\"",
            "register the discord command {_cmd} in event-guild"},
        since = "5.0.0-a2")
    public static UserCommandHolder newUserCommand(String name) {
        return new UserCommandHolder(Commands.user(name));
    }
}
