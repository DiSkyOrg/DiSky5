package net.itsthesky.disky5.processor;

import com.google.testing.compile.Compilation;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.compile;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.descriptor;

class DiagnosticsTest {

    @Test
    void getterWithoutParameterIsAnError() {
        Compilation compilation = compile(descriptor("Getters",
            "public final class Getters {",
            "  @Getter(\"user\")",
            "  public static User user() { return null; }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("@Getter method must take the lookup scope");
    }

    @Test
    void aSharedSetterWithoutAGetterIsAWriteOnlyContribution() {
        Compilation compilation = compile(descriptor("RoleType",
            "@SkriptType(name = \"role\", jda = Role.class)",
            "public final class RoleType {",
            "  @Property.Setter(value = \"name\", shared = true)",
            "  public static AuditableRestAction<Void> setName(Role r, String v) {",
            "    return r.getManager().setName(v);",
            "  }",
            "}"));

        assertThat(compilation).succeeded();
        assertThat(compilation)
            .generatedSourceFile("net.itsthesky.disky5.generated.GeneratedTypeProperties")
            .contentsAsUtf8String()
            .contains("return null");
    }

    @Test
    void aSharedSetterWithoutAValueParameterIsAnError() {
        Compilation compilation = compile(descriptor("RoleType",
            "@SkriptType(name = \"role\", jda = Role.class)",
            "public final class RoleType {",
            "  @Property.Setter(value = \"name\", shared = true)",
            "  public static AuditableRestAction<Void> setName(Role r) {",
            "    return r.getManager().setName(\"x\");",
            "  }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("exactly two parameters");
    }

    @Test
    void sharedGetterMustTakeExactlyTheEntity() {
        Compilation compilation = compile(descriptor("RoleType",
            "@SkriptType(name = \"role\", jda = Role.class)",
            "public final class RoleType {",
            "  @Property(value = \"name\", shared = true)",
            "  public static String name(Role r, String extra) { return r.getName(); }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("parameter");
    }

    @Test
    void converterMustTakeExactlyOneParameter() {
        Compilation compilation = compile(descriptor("Converters",
            "public final class Converters {",
            "  @Converter",
            "  public static String bad(Member m, Role r) { return \"\"; }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("parameter");
    }

    @Test
    void comparatorMustTakeExactlyTwoParameters() {
        Compilation compilation = compile(descriptor("Comparators",
            "public final class Comparators {",
            "  @Comparator",
            "  public static boolean bad(Member m) { return true; }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("parameter");
    }

    @Test
    void eventValueMustTakeExactlyTheJdaEvent() {
        Compilation compilation = compile(descriptor("EvtSomething",
            "@SkriptEvent(name = \"Something\", patterns = \"something\", jda = MessageReceivedEvent.class)",
            "public final class EvtSomething {",
            "  @EventValue",
            "  public static Guild guild(MessageReceivedEvent e, String extra) { return e.getGuild(); }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("exactly one parameter");
    }

    @Test
    void mutatorWithoutAValueParameterIsReportedNotCrashed() {
        Compilation compilation = compile(descriptor("MemberType",
            "@SkriptType(name = \"member\", jda = Member.class)",
            "public final class MemberType {",
            "  @Property(\"nickname\")",
            "  public static String nickname(Member m) { return m.getNickname(); }",
            "  @Property.Setter(\"nickname\")",
            "  public static void setNickname(Member m) { }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("[DiSkyProcessor]");
    }
}
