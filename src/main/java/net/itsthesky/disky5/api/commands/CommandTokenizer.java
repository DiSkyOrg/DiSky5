package net.itsthesky.disky5.api.commands;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class CommandTokenizer {

    public record Token(String text, int start) {}

    private CommandTokenizer() {}

    public static List<Token> tokenize(String raw) {
        List<Token> tokens = new ArrayList<>();
        int i = 0, n = raw.length();
        while (i < n) {
            while (i < n && Character.isWhitespace(raw.charAt(i))) i++;
            if (i >= n) break;
            int start = i;
            if (raw.charAt(i) == '"') {
                int close = raw.indexOf('"', i + 1);
                if (close > 0) {
                    tokens.add(new Token(raw.substring(i + 1, close), start));
                    i = close + 1;
                    continue;
                }
            }
            int end = i;
            while (end < n && !Character.isWhitespace(raw.charAt(end))) end++;
            tokens.add(new Token(raw.substring(i, end), start));
            i = end;
        }
        return tokens;
    }

    public static List<@Nullable List<String>> assign(String raw, List<CommandArgument> arguments) {
        List<Token> tokens = tokenize(raw);
        List<List<String>> result = new ArrayList<>(arguments.size());
        int cursor = 0;
        for (int a = 0; a < arguments.size(); a++) {
            CommandArgument argument = arguments.get(a);
            boolean last = a == arguments.size() - 1;
            if (cursor >= tokens.size()) {
                result.add(null);
                continue;
            }
            if (last && argument.greedyText()) {
                result.add(List.of(raw.substring(tokens.get(cursor).start()).trim()));
                cursor = tokens.size();
            } else if (!argument.single()) {
                int take = last ? tokens.size() - cursor : 1;
                List<String> values = new ArrayList<>();
                for (int k = 0; k < take; k++) values.add(tokens.get(cursor++).text());
                result.add(values);
            } else {
                result.add(List.of(tokens.get(cursor++).text()));
            }
        }
        return result;
    }
}
