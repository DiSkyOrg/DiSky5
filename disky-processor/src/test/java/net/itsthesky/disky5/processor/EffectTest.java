package net.itsthesky.disky5.processor;

import com.google.testing.compile.Compilation;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.compile;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.descriptor;

class EffectTest {

    private static final String MEMBER_TYPE = "@SkriptType(name = \"member\", jda = Member.class)";

    @Test
    void anEffectGeneratesAnAsyncEffectClass() {
        Compilation compilation = compile(
            descriptor("MemberType", MEMBER_TYPE, "public final class MemberType {}"),
            descriptor("Moderation",
                "public final class Moderation {",
                "  @Effect(\"kick [the] [member] %member%\")",
                "  public static AuditableRestAction<Void> kick(Member m) { return m.kick(); }",
                "}"));

        assertThat(compilation).succeeded();
        assertThat(compilation)
            .generatedSourceFile("net.itsthesky.disky5.generated.EffKick")
            .contentsAsUtf8String()
            .contains("extends AsyncEffect");
    }

    @Test
    void aRestActionIsCompletedNotQueued() {
        Compilation compilation = compile(
            descriptor("MemberType", MEMBER_TYPE, "public final class MemberType {}"),
            descriptor("Moderation",
                "public final class Moderation {",
                "  @Effect(\"kick [the] [member] %member%\")",
                "  public static AuditableRestAction<Void> kick(Member m) { return m.kick(); }",
                "}"));

        assertThat(compilation).succeeded();
        assertThat(compilation)
            .generatedSourceFile("net.itsthesky.disky5.generated.EffKick")
            .contentsAsUtf8String()
            .contains(".complete()");
    }

    @Test
    void moreSlotsThanParametersIsAnError() {
        Compilation compilation = compile(
            descriptor("MemberType", MEMBER_TYPE, "public final class MemberType {}"),
            descriptor("Moderation",
                "public final class Moderation {",
                "  @Effect(\"kick [the] [member] %member% for %string%\")",
                "  public static AuditableRestAction<Void> kick(Member m) { return m.kick(); }",
                "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("binds to one parameter");
    }

    @Test
    void aSlotBoundToTheWrongParameterIsAnError() {
        Compilation compilation = compile(
            descriptor("MemberType", MEMBER_TYPE, "public final class MemberType {}"),
            descriptor("Moderation",
                "public final class Moderation {",
                "  @Effect(\"kick [the] [member] %member% because of %string%\")",
                "  public static AuditableRestAction<Void> kick(String reason, Member m) { return m.kick(); }",
                "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("slots bind to parameters in order");
    }

    @Test
    void anAuditReasonNeedsAnAuditableAction() {
        Compilation compilation = compile(
            descriptor("MemberType", MEMBER_TYPE, "public final class MemberType {}"),
            descriptor("Moderation",
                "public final class Moderation {",
                "  @Effect(value = \"disconnect [the] [member] %member%\", reason = true)",
                "  public static RestAction<Void> disconnect(Member m) {",
                "    return m.getGuild().kickVoiceMember(m);",
                "  }",
                "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("AuditableRestAction");
    }

    @Test
    void anOptionalSlotCannotBindToAPrimitive() {
        Compilation compilation = compile(
            descriptor("MemberType", MEMBER_TYPE, "public final class MemberType {}"),
            descriptor("Moderation",
                "public final class Moderation {",
                "  @Effect(\"mute [the] [member] %member% [silently %-boolean%]\")",
                "  public static AuditableRestAction<Void> mute(Member m, boolean silent) {",
                "    return m.mute(true);",
                "  }",
                "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("must be boxed");
    }

    @Test
    void twoEffectsWithTheSameGeneratedNameAreAnError() {
        Compilation compilation = compile(
            descriptor("MemberType", MEMBER_TYPE, "public final class MemberType {}"),
            descriptor("Moderation",
                "public final class Moderation {",
                "  @Effect(\"kick [the] [member] %member%\")",
                "  public static AuditableRestAction<Void> kick(Member m) { return m.kick(); }",
                "}"),
            descriptor("Voice",
                "public final class Voice {",
                "  @Effect(\"kick [the] [member] %member% from voice\")",
                "  public static AuditableRestAction<Void> kick(Member m) { return m.kick(); }",
                "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("stable class name");
    }
}
