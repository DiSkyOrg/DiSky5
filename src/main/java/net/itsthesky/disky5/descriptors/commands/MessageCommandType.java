package net.itsthesky.disky5.descriptors.commands;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.itsthesky.disky5.annotations.New;
import net.itsthesky.disky5.annotations.SkriptType;
import net.itsthesky.disky5.annotations.docs.Documentation;
import net.itsthesky.disky5.api.commands.application.MessageCommandHolder;

@SkriptType(name = "messagecommand", jda = MessageCommandHolder.class, parent = ApplicationCommandType.class)
@Documentation(
    name = "Message Command",
    description = {"A context-menu command shown when right-clicking a message (Apps > name). It has a name and "
        + "nothing else; answer it in 'on message command' with 'target message', or declare it with the 'message command' structure."},
    since = "5.0.0-a2")
public final class MessageCommandType {

    private MessageCommandType() {}

    @New("[a] new message command (named|with [the] name) %string%")
    @Documentation(
        name = "New Message Command",
        description = "Creates a message context-menu command. Its name may contain spaces and capitals (1 to 32 characters).",
        examples = {"set {_cmd} to a new message command named \"Pin this\"",
            "register the discord command {_cmd} in event-guild"},
        since = "5.0.0-a2")
    public static MessageCommandHolder newMessageCommand(String name) {
        return new MessageCommandHolder(Commands.message(name));
    }
}
