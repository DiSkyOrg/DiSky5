package net.itsthesky.disky5.api.syntax;

import net.itsthesky.disky5.generated.GeneratedSyntaxManifest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class GoldenManifestTest {

    private static final Path GOLDEN = Path.of("src/test/resources/golden/syntax-digest.txt");
    private static final Path ACTUAL = Path.of("build/reports/golden/syntax-digest.actual.txt");

    @BeforeEach
    @AfterEach
    void resetRegistry() {
        DiSkySyntaxRegistry.INSTANCE.clear();
    }

    @Test
    void generatedSyntaxMatchesGolden() throws IOException {
        GeneratedSyntaxManifest.register();
        SyntaxManifest manifest = DiSkySyntaxRegistry.INSTANCE.snapshot("test", "0");
        String actual = SyntaxDigest.of(manifest);

        if (Boolean.getBoolean("disky.golden.update")) {
            Files.createDirectories(GOLDEN.getParent());
            Files.writeString(GOLDEN, actual);
            return;
        }

        if (!Files.exists(GOLDEN)) {
            writeActual(actual);
            fail("No golden file at " + GOLDEN + ". Create it with: ./gradlew test -PupdateGolden");
        }

        String expected = Files.readString(GOLDEN).replace("\r\n", "\n");
        if (expected.equals(actual))
            return;

        writeActual(actual);
        fail(diffReport(expected, actual));
    }

    @Test
    void snapshotIsDeterministic() {
        GeneratedSyntaxManifest.register();
        String first = SyntaxDigest.of(DiSkySyntaxRegistry.INSTANCE.snapshot("test", "0"));
        DiSkySyntaxRegistry.INSTANCE.clear();
        GeneratedSyntaxManifest.register();
        String second = SyntaxDigest.of(DiSkySyntaxRegistry.INSTANCE.snapshot("test", "0"));

        assertThat(second).isEqualTo(first);
    }

    @Test
    void manifestIsNotEmpty() {
        GeneratedSyntaxManifest.register();
        SyntaxManifest manifest = DiSkySyntaxRegistry.INSTANCE.snapshot("test", "0");

        assertThat(manifest.entities()).isNotEmpty();
        assertThat(manifest.events()).isNotEmpty();
        assertThat(manifest.entities()).allSatisfy(entity -> {
            assertThat(entity.id()).isNotBlank();
            assertThat(entity.jdaType()).isNotBlank();
        });
    }

    private static void writeActual(String actual) throws IOException {
        Files.createDirectories(ACTUAL.getParent());
        Files.writeString(ACTUAL, actual);
    }

    private static String diffReport(String expected, String actual) {
        List<String> before = expected.lines().toList();
        List<String> after = actual.lines().toList();
        List<String> removed = before.stream().filter(line -> !after.contains(line)).toList();
        List<String> added = after.stream().filter(line -> !before.contains(line)).toList();

        StringBuilder sb = new StringBuilder("The generated syntax surface changed.\n")
            .append("  golden: ").append(before.size()).append(" lines\n")
            .append("  actual: ").append(after.size()).append(" lines (written to ").append(ACTUAL).append(")\n");
        appendSample(sb, "removed", removed);
        appendSample(sb, "added", added);
        return sb.append("\nIf this is intended: ./gradlew test -PupdateGolden").toString();
    }

    private static void appendSample(StringBuilder sb, String label, List<String> lines) {
        sb.append("\n  ").append(lines.size()).append(' ').append(label).append(':').append('\n');
        lines.stream().limit(40).forEach(line -> sb.append("    ").append(line).append('\n'));
        if (lines.size() > 40)
            sb.append("    ... ").append(lines.size() - 40).append(" more\n");
    }
}
