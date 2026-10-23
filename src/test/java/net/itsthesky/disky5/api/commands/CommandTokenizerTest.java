package net.itsthesky.disky5.api.commands;

import ch.njol.skript.classes.ClassInfo;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommandTokenizerTest {

    private static CommandArgument arg(Class<?> type, boolean single, boolean optional, int index) {
        ArgumentType<Object> argumentType = new ArgumentType<>() {
            @Override public String name() { return type.getSimpleName().toLowerCase(); }
            @SuppressWarnings("unchecked") @Override public Class<Object> type() { return (Class<Object>) type; }
            @Override public Object resolve(String raw, CommandInvocation invocation) { return raw; }
        };
        return new CommandArgument(null, argumentType, (ClassInfo<?>) null, single, optional, null, index);
    }

    @Test
    void quotesGroupWords() {
        List<CommandTokenizer.Token> tokens = CommandTokenizer.tokenize("a \"b c\"  d");
        assertThat(tokens).extracting(CommandTokenizer.Token::text).containsExactly("a", "b c", "d");
        assertThat(tokens.get(2).start()).isEqualTo(9);
    }

    @Test
    void trailingTextTakesTheRestOfTheLine() {
        List<CommandArgument> args = List.of(arg(Object.class, true, false, 0), arg(String.class, true, false, 1));
        List<List<String>> assigned = CommandTokenizer.assign("@user hello   there \"friend\"", args);
        assertThat(assigned.get(0)).containsExactly("@user");
        assertThat(assigned.get(1)).containsExactly("hello   there \"friend\"");
    }

    @Test
    void trailingPluralTakesEveryToken() {
        List<CommandArgument> args = List.of(arg(Object.class, false, false, 0));
        assertThat(CommandTokenizer.assign("a b c", args).get(0)).containsExactly("a", "b", "c");
    }

    @Test
    void missingArgumentsAreNull() {
        List<CommandArgument> args = List.of(arg(Object.class, true, false, 0), arg(Object.class, true, true, 1));
        List<List<String>> assigned = CommandTokenizer.assign("only", args);
        assertThat(assigned.get(0)).containsExactly("only");
        assertThat(assigned.get(1)).isNull();
        assertThat(CommandTokenizer.assign("", args)).isEqualTo(Arrays.asList(null, null));
    }

    @Test
    void snowflakesComeOutOfMentions() {
        assertThat(ArgumentTypes.snowflake("<@!123456789012345678>")).isEqualTo(123456789012345678L);
        assertThat(ArgumentTypes.snowflake("<@&123456789012345678>")).isEqualTo(123456789012345678L);
        assertThat(ArgumentTypes.snowflake("<#123456789012345678>")).isEqualTo(123456789012345678L);
        assertThat(ArgumentTypes.snowflake("123456789012345678")).isEqualTo(123456789012345678L);
        assertThat(ArgumentTypes.snowflake("bob")).isNull();
    }
}
