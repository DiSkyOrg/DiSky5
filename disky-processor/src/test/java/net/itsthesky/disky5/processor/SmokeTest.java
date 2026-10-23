package net.itsthesky.disky5.processor;

import com.google.testing.compile.Compilation;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.compile;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.descriptor;

class SmokeTest {

    @Test
    void aWellFormedDescriptorGeneratesItsPropertyClassAndAggregators() {
        Compilation compilation = compile(descriptor("MemberType",
            "@SkriptType(name = \"member\", jda = Member.class)",
            "public final class MemberType {",
            "  @Property(\"nickname\")",
            "  public static String nickname(Member m) { return m.getNickname(); }",
            "  @Property.Setter(\"nickname\")",
            "  public static AuditableRestAction<Void> setNickname(Member m, String v) {",
            "    return m.modifyNickname(v);",
            "  }",
            "}"));

        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("d.generated.PropMemberNickname");
        assertThat(compilation).generatedSourceFile("net.itsthesky.disky5.generated.GeneratedTypes");
        assertThat(compilation).generatedSourceFile("net.itsthesky.disky5.generated.GeneratedElements");
        assertThat(compilation).generatedSourceFile("net.itsthesky.disky5.generated.GeneratedSyntaxManifest");
    }
}
