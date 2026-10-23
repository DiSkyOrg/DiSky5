package net.itsthesky.disky5.processor;

import com.google.testing.compile.Compilation;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Optional;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.compile;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.descriptor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeterminismTest {

    private static final String[] MEMBER = {
        "@SkriptType(name = \"member\", jda = Member.class)",
        "public final class MemberType {",
        "  @Property(\"nickname\")",
        "  public static String nickname(Member m) { return m.getNickname(); }",
        "}"
    };

    private static final String[] ROLE = {
        "@SkriptType(name = \"role\", jda = Role.class)",
        "public final class RoleType {",
        "  @Property(\"hoisted\")",
        "  public static boolean hoisted(Role r) { return r.isHoisted(); }",
        "}"
    };

    @Test
    void aggregatorsDoNotDependOnSourceOrder() throws IOException {
        Compilation first = compile(descriptor("MemberType", MEMBER), descriptor("RoleType", ROLE));
        Compilation second = compile(descriptor("RoleType", ROLE), descriptor("MemberType", MEMBER));

        assertThat(first).succeeded();
        assertThat(second).succeeded();

        for (String aggregator : new String[]{
            "net.itsthesky.disky5.generated.GeneratedElements",
            "net.itsthesky.disky5.generated.GeneratedTypes",
            "net.itsthesky.disky5.generated.GeneratedSyntaxManifest"
        }) {
            assertEquals(contentOf(first, aggregator), contentOf(second, aggregator),
                aggregator + " differs depending on the order the sources were passed in");
        }
    }

    private static String contentOf(Compilation compilation, String fqn) throws IOException {
        String path = "/SOURCE_OUTPUT/" + fqn.replace('.', '/') + ".java";
        Optional<JavaFileObject> file = compilation.generatedSourceFiles().stream()
            .filter(f -> f.getName().equals(path))
            .findFirst();
        assertTrue(file.isPresent(), "not generated: " + fqn);
        return file.get().getCharContent(true).toString();
    }
}
