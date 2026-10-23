package net.itsthesky.disky5.processor;

import com.google.testing.compile.Compilation;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.compile;
import static net.itsthesky.disky5.processor.ProcessorTestSupport.descriptor;

class BuilderTest {

    private static final String[] ROLE_BUILDER = {
        "@SkriptType(name = \"roleaction\", jda = RoleAction.class)",
        "public final class RoleActionType {",
        "  @New(\"[a] new role builder named %string% in %guild%\")",
        "  public static RoleAction create(String name, Guild guild) {",
        "    return guild.createRole().setName(name);",
        "  }",
        "  @Build",
        "  public static RestAction<Role> build(RoleAction action) { return action; }",
        "  @Property.Setter(\"hoisted\")",
        "  public static void setHoisted(RoleAction action, Boolean value) { action.setHoisted(value); }",
        "}"
    };

    @Test
    void aBuilderGeneratesItsFactoryAndItsExecutor() {
        Compilation compilation = compile(descriptor("RoleActionType", ROLE_BUILDER));

        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("d.generated.NewExpr_Create");
        assertThat(compilation).generatedSourceFile("d.generated.BuildExpr_Roleaction");
    }

    @Test
    void buildingIsAwaitOnly() {
        Compilation compilation = compile(descriptor("RoleActionType", ROLE_BUILDER));

        assertThat(compilation).succeeded();
        assertThat(compilation)
            .generatedSourceFile("d.generated.BuildExpr_Roleaction")
            .contentsAsUtf8String()
            .contains("isParsingInsideAwait");
    }

    @Test
    void creatingABuilderIsNot() {
        Compilation compilation = compile(descriptor("RoleActionType", ROLE_BUILDER));

        assertThat(compilation).succeeded();
        assertThat(compilation)
            .generatedSourceFile("d.generated.NewExpr_Create")
            .contentsAsUtf8String()
            .doesNotContain("isParsingInsideAwait");
    }

    @Test
    void aWriteOnlyPropertyStillGetsItsExpression() {
        Compilation compilation = compile(descriptor("RoleActionType", ROLE_BUILDER));

        assertThat(compilation).succeeded();
        assertThat(compilation)
            .generatedSourceFile("d.generated.PropRoleActionHoisted")
            .contentsAsUtf8String()
            .contains("acceptChange");
    }

    @Test
    void aBuildMethodOutsideADescriptorIsAnError() {
        Compilation compilation = compile(descriptor("Loose",
            "public final class Loose {",
            "  @Build",
            "  public static RestAction<Role> build(RoleAction action) { return action; }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("must be declared on a @SkriptType descriptor");
    }

    @Test
    void aBuildMethodReturningNothingIsAnError() {
        Compilation compilation = compile(descriptor("RoleActionType",
            "@SkriptType(name = \"roleaction\", jda = RoleAction.class)",
            "public final class RoleActionType {",
            "  @Build",
            "  public static void build(RoleAction action) { action.complete(); }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("is an @Effect, not a builder");
    }

    @Test
    void aFactoryPatternMustBindItsParameters() {
        Compilation compilation = compile(descriptor("RoleActionType",
            "@SkriptType(name = \"roleaction\", jda = RoleAction.class)",
            "public final class RoleActionType {",
            "  @New(\"[a] new role builder named %string% in %guild%\")",
            "  public static RoleAction create(Guild guild, String name) {",
            "    return guild.createRole().setName(name);",
            "  }",
            "}"));

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("slots bind to parameters in order");
    }
}
