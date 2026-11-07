package rafasconquest.bridge;

import org.junit.jupiter.api.Test;
import rafasconquest.peerage.Talk;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WarCouncilPagesTest {
    private static WarCouncil.Said said(int entity, String name, String text) {
        return new WarCouncil.Said(entity, name, "General of Shukai", 0, 0, text);
    }

    @Test
    void eachSpeakersRunIsAPageWithTheChoicesOnTheLast() {
        List<Talk.Option> options = List.of(new Talk.Option("council:leave", "Leave"));
        List<Talk.View> pages = WarCouncil.pages(List.of(said(1, "Aldric", "a"), said(1, "Aldric", "b"), said(2, "Bors", "c"),
                said(1, "Aldric", "d")), options);
        assertEquals(3, pages.size());
        assertEquals(List.of("a", "b"), pages.get(0).lines());
        assertEquals(List.of(), pages.get(0).options());
        assertEquals(2, pages.get(1).entity());
        assertEquals(options, pages.get(2).options());
    }
}
