package rafasconquest.politics;

import java.util.ArrayList;
import java.util.List;

/** What a war is fought for: a title, for now a county or a duchy (the two smallest), by its index. */
public record Goal(boolean duchy, int index) {
    /** The counties of the title. */
    public List<Integer> counties(Lands lands) {
        if (!duchy) {
            return List.of(index);
        }
        List<Integer> all = new ArrayList<>();
        for (int c = 0; c < lands.counties(); c++) {
            if (lands.duchy(c) == index) {
                all.add(c);
            }
        }
        return all;
    }

    /** The provinces of the title. */
    public List<Integer> provinces(Lands lands) {
        List<Integer> counties = counties(lands);
        List<Integer> all = new ArrayList<>();
        for (int p = 0; p < lands.provinces(); p++) {
            if (counties.contains(lands.county(p))) {
                all.add(p);
            }
        }
        return all;
    }
}
