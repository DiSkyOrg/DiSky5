package net.itsthesky.disky5.processor;

import com.google.testing.compile.Compilation;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.compile;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.descriptor;

class EventFilterTest {

    @Test
    void aFilterBecomesTheAcceptOverride() {
        Compilation compilation = compile(descriptor("EvtBoost",
            "@SkriptEvent(name = \"Boost\", patterns = \"boost\", jda = MessageReceivedEvent.class)",
            "public final class EvtBoost {",
            "  @EventFilter",
            "  public static boolean accept(MessageReceivedEvent e) { return e.isFromGuild(); }",
            "  @EventValue",
            "  public static Guild guild(MessageReceivedEvent e) { return e.getGuild(); }",
            "}"));

        assertThat(compilation).succeeded();
        assertThat(compilation)
            .generatedSourceFile("net.itsthesky.disky5.generated.events.GeneratedEvt_Boost")
            .contentsAsUtf8String()
            .contains("protected boolean accept(");
    }

    @Test
    void anEventWithoutAFilterDoesNotOverrideAccept() {
        Compilation compilation = compile(descriptor("EvtPlain",
            "@SkriptEvent(name = \"Plain\", patterns = \"plain\", jda = MessageReceivedEvent.class)",
            "public final class EvtPlain {",
            "  @EventValue",
            "  public static Guild guild(MessageReceivedEvent e) { return e.getGuild(); }",
            "}"));

        assertThat(compilation).succeeded();
        assertThat(compilation)
            .generatedSourceFile("net.itsthesky.disky5.generated.events.GeneratedEvt_Plain")
            .contentsAsUtf8String()
            .doesNotContain("protected boolean accept(");
    }

    @Test
    void aNonBooleanFilterIsAnError() {
        Compilation compilation = compile(descriptor("EvtBad",
            "@SkriptEvent(name = \"Bad\", patterns = \"bad\", jda = MessageReceivedEvent.class)",
            "public final class EvtBad {",
            "  @EventFilter",
            "  public static String accept(MessageReceivedEvent e) { return \"\"; }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("must return boolean");
    }

    @Test
    void twoFiltersOnOneEventAreAnError() {
        Compilation compilation = compile(descriptor("EvtTwo",
            "@SkriptEvent(name = \"Two\", patterns = \"two\", jda = MessageReceivedEvent.class)",
            "public final class EvtTwo {",
            "  @EventFilter",
            "  public static boolean first(MessageReceivedEvent e) { return true; }",
            "  @EventFilter",
            "  public static boolean second(MessageReceivedEvent e) { return true; }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("only one @EventFilter");
    }
}
