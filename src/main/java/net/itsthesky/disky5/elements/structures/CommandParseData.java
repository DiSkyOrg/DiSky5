package net.itsthesky.disky5.elements.structures;

import ch.njol.skript.lang.parser.ParserInstance;
import net.itsthesky.disky5.api.commands.InvocationArgument;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class CommandParseData extends ParserInstance.Data {

    private @Nullable List<? extends InvocationArgument> arguments;

    public CommandParseData(ParserInstance parser) {
        super(parser);
    }

    public @Nullable List<? extends InvocationArgument> arguments() {
        return arguments;
    }

    public void arguments(@Nullable List<? extends InvocationArgument> arguments) {
        this.arguments = arguments;
    }
}
