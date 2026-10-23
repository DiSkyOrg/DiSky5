package net.itsthesky.disky5.api.syntax;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SyntaxCollisionCheckTest {

    private static final String SLOT = "\0";

    @Test
    void skriptsKickAndDiSkysKickShareAShape() {
        Set<String> skript = SyntaxCollisionCheck.shapes(
            "kick %players% [(by reason of|because [of]|on account of|due to) %-string%]");
        Set<String> disky = SyntaxCollisionCheck.shapes(
            "[discord] kick [the] [member] %member% [(due to|because of|with [the] reason) %-string%]");

        assertThat(skript).contains("kick " + SLOT);
        assertThat(disky).contains("kick " + SLOT);
    }

    @Test
    void theDiscordPrefixIsWhatSeparatesThem() {
        Set<String> skript = SyntaxCollisionCheck.shapes("[the] colo[u]r[s] of %blocks/entities%");
        Set<String> disky = SyntaxCollisionCheck.shapes("[the] discord color of %role/roleaction%");

        assertThat(skript).contains("color of " + SLOT, "the colours of " + SLOT);
        assertThat(disky).contains("discord color of " + SLOT);
        assertThat(skript).doesNotContainAnyElementsOf(disky);
    }

    @Test
    void parseMarksAreNotPartOfWhatIsTyped() {
        Set<String> shapes = SyntaxCollisionCheck.shapes(
            "catch [the] [error[s]] %~objects% [(stop:[and] stop)]");

        assertThat(shapes).contains("catch " + SLOT);
        assertThat(shapes).allSatisfy(shape -> assertThat(shape).doesNotContain("stop:"));
    }

    @Test
    void noShapeKeepsAnUnresolvedGroup() {
        Set<String> shapes = SyntaxCollisionCheck.shapes(
            "[a] new role builder [(named|with [the] name)] %string% in [the] [guild] %guild%");

        assertThat(shapes).isNotEmpty();
        assertThat(shapes).allSatisfy(shape ->
            assertThat(shape).doesNotContain("[").doesNotContain("(").doesNotContain("]"));
        assertThat(shapes).contains("a new role builder named " + SLOT + " in the guild " + SLOT);
    }

    @Test
    void aRegexSlotCountsAsAValueSlot() {
        assertThat(SyntaxCollisionCheck.shapes("await <.+>")).contains("await " + SLOT);
    }
}
