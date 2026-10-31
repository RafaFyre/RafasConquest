package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NationsTest {
    private static final int BPP = 16;
    private static final int CELL = 30;
    private static final int CELLS = 30;
    private static final int EYE = 1000;
    private static final int WAR_ZONE = 4000;

    private record World(TitleMap titles, List<ProvinceMap.Centre> centres, Nations.Plan plan) {
        int[] at(int province) {
            return new int[]{centres.get(province).x(), centres.get(province).z()};
        }
    }

    /** A land of square provinces 480 blocks a side, spawn at its north-west corner. */
    private static World world(long seed) {
        int width = CELLS * CELL;
        int[] provinces = new int[width * width];
        boolean[] sea = new boolean[width * width];
        List<ProvinceMap.Centre> centres = new ArrayList<>();
        for (int cz = 0; cz < CELLS; cz++) {
            for (int cx = 0; cx < CELLS; cx++) {
                centres.add(new ProvinceMap.Centre(cx * CELL * BPP + CELL * BPP / 2, cz * CELL * BPP + CELL * BPP / 2, false));
            }
        }
        for (int z = 0; z < width; z++) {
            for (int x = 0; x < width; x++) {
                provinces[z * width + x] = (z / CELL) * CELLS + x / CELL;
            }
        }
        TitleMap titles = TitleMap.build(seed, width, width, provinces, centres.size(), sea);
        Nations.Plan plan = Nations.plan(seed, titles, p -> new int[]{centres.get(p).x(), centres.get(p).z()}, EYE, WAR_ZONE);
        return new World(titles, centres, plan);
    }

    @Test
    void nobodyHoldsTheEyeOrAMiniEye() {
        for (long seed = 0; seed < 5; seed++) {
            World w = world(seed);
            for (int p = 0; p < w.plan().holder().length; p++) {
                if (w.plan().holder()[p] < 0 || w.plan().nations().get(w.plan().holder()[p]).sanctum()) {
                    continue; // a sword sanctum holds its county in a mini-eye's heart
                }
                int[] at = w.at(p);
                assertTrue(Math.hypot(at[0], at[1]) > EYE, "a held province in the eye");
                for (int[] e : w.plan().miniEyes()) {
                    assertTrue(Math.hypot(e[0] - at[0], e[1] - at[1]) > EYE, "a held province in a mini-eye");
                }
            }
        }
    }

    @Test
    void miniEyesLieInTheOuterWorldApart() {
        int eyes = 0;
        for (long seed = 0; seed < 5; seed++) {
            List<int[]> miniEyes = world(seed).plan().miniEyes();
            eyes += miniEyes.size();
            for (int i = 0; i < miniEyes.size(); i++) {
                int[] a = miniEyes.get(i);
                assertTrue(Math.hypot(a[0], a[1]) > WAR_ZONE + EYE, "a mini-eye reaching into the war zone");
                for (int j = i + 1; j < miniEyes.size(); j++) {
                    int[] b = miniEyes.get(j);
                    assertTrue(Math.hypot(a[0] - b[0], a[1] - b[1]) > 4.0 * EYE, "mini-eyes too close");
                }
            }
        }
        assertTrue(eyes > 0, "no mini-eyes at all");
    }

    @Test
    void nationsTakeTheirTitlesByZone() {
        boolean empire = false;
        boolean cityState = false;
        for (long seed = 0; seed < 5; seed++) {
            World w = world(seed);
            List<Nations.Nation> nations = w.plan().nations();
            for (int i = 0; i < nations.size(); i++) {
                Nations.Nation n = nations.get(i);
                if (n.sanctum()) {
                    continue; // a sword sanctum stands where it likes; tested on its own
                }
                // A City-state lies in the zone of its kingdom's seat.
                int[] zoneAt = n.tier() == TitleMap.Tier.COUNTY
                        ? w.at(w.titles().kingdomSeat()[w.titles().kingdom()[w.titles().duchy()[n.title()]]]) : new int[]{n.x(), n.z()};
                double d = Math.hypot(zoneAt[0], zoneAt[1]);
                if (d <= WAR_ZONE) {
                    assertTrue(n.tier() != TitleMap.Tier.EMPIRE, "an empire in the war zone");
                } else {
                    assertTrue(n.tier() != TitleMap.Tier.COUNTY, "a City-state in the outer world");
                }
                empire |= n.tier() == TitleMap.Tier.EMPIRE;
                cityState |= n.tier() == TitleMap.Tier.COUNTY;
                boolean holdsAny = false;
                for (int p = 0; p < w.plan().holder().length; p++) {
                    if (w.plan().holder()[p] == i) {
                        holdsAny = true;
                        assertTrue(Nations.under(w.titles(), p, n.tier(), n.title()), "a nation holding land outside its title");
                    }
                }
                assertTrue(holdsAny, "a nation with no land");
                assertEquals(i, w.plan().holder()[n.seat()], "a nation not holding its own seat");
            }
        }
        assertTrue(empire, "no empires");
        assertTrue(cityState, "no City-states");
    }

    @Test
    void theKnownWorldHasTwelveNationsAndAtMostFourCityStates() {
        for (long seed = 0; seed < 20; seed++) {
            World w = world(seed);
            List<Nations.Nation> nations = w.plan().nations();
            assertTrue(Nations.known(nations, Integer.MAX_VALUE) >= Nations.MIN_NATIONS, nations.size() + " nations, seed " + seed);
            assertTrue(Nations.cityStates(nations) <= Nations.MAX_CITY_STATES, Nations.cityStates(nations) + " City-states, seed " + seed);
        }
    }

    @Test
    void theTopUpStaysInTheKnownWorld() {
        int known = 11_000;
        for (long seed = 0; seed < 10; seed++) {
            World w = world(seed);
            List<ProvinceMap.Centre> centres = w.centres();
            Nations.Plan plan = Nations.plan(seed, w.titles(), p -> new int[]{centres.get(p).x(), centres.get(p).z()}, EYE, WAR_ZONE, known,
                    Nations.SANCTUMS);
            assertTrue(Nations.known(plan.nations(), known) >= Nations.MIN_NATIONS, "seed " + seed);
            for (Nations.Nation n : plan.nations()) {
                if (n.tier() == TitleMap.Tier.DUCHY) {
                    assertTrue(Math.hypot(n.x(), n.z()) <= known, "a duchy topped up beyond the known world");
                }
            }
        }
    }

    @Test
    void someOfTheLandIsNobodys() {
        World w = world(1);
        long unheld = java.util.Arrays.stream(w.plan().holder()).filter(h -> h < 0).count();
        assertFalse(unheld == 0 || unheld == w.plan().holder().length, unheld + " unheld provinces");
    }
}
