package rafasconquest.core;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TitleMapTest {
    /** A square land of {@code cells} × {@code cells} provinces, each {@code size} pixels wide, sea in the first column of provinces. */
    private record Land(int width, int[] provinces, boolean[] sea, int count) {
    }

    private static Land land(int cells, int size) {
        int width = cells * size;
        int[] provinces = new int[width * width];
        boolean[] sea = new boolean[width * width];
        int count = 0;
        int[][] index = new int[cells][cells];
        for (int[] row : index) {
            Arrays.fill(row, -1);
        }
        for (int cz = 0; cz < cells; cz++) {
            for (int cx = 1; cx < cells; cx++) {
                index[cz][cx] = count++;
            }
        }
        for (int z = 0; z < width; z++) {
            for (int x = 0; x < width; x++) {
                int p = index[z / size][x / size];
                provinces[z * width + x] = p < 0 ? ProvinceMap.WATER : p;
                sea[z * width + x] = p < 0;
            }
        }
        return new Land(width, provinces, sea, count);
    }

    private static TitleMap titles(Land l, long seed) {
        return TitleMap.build(seed, l.width(), l.width(), l.provinces(), l.count(), l.sea());
    }

    @Test
    void everyLevelGroupsNeighboursInItsSizes() {
        Land l = land(30, 4);
        TitleMap t = titles(l, 7);
        int[][] levels = {t.county(), t.duchy(), t.kingdom(), t.empire()};
        int[] counts = {t.counties(), t.duchies(), t.kingdoms(), t.empires()};
        for (int level = 0; level < levels.length; level++) {
            int[] sizes = new int[counts[level]];
            for (int g : levels[level]) {
                assertTrue(g >= 0, "something left out at level " + level);
                sizes[g]++;
            }
            int within = 0;
            for (int size : sizes) {
                assertTrue(size >= TitleMap.MIN[level] || counts[level] == 1, "a group too small at level " + level + ": " + size);
                if (size <= TitleMap.MAX[level]) {
                    within++;
                }
            }
            // Joining too-small groups to a neighbour can push a few over the top.
            assertTrue(within >= sizes.length * 0.8, "too many too big at level " + level + ": " + Arrays.toString(sizes));
        }
    }

    @Test
    void aSeatIsAlsoTheSeatOfEveryTitleBelowIt() {
        TitleMap t = titles(land(30, 4), 11);
        for (int c = 0; c < t.counties(); c++) {
            assertEquals(c, t.county()[t.countySeat()[c]], "a county's seat outside it");
        }
        for (int d = 0; d < t.duchies(); d++) {
            int seat = t.duchySeat()[d];
            assertEquals(seat, t.countySeat()[t.county()[seat]], "a duchy's seat isn't a county seat");
            assertEquals(d, t.duchy()[t.county()[seat]]);
        }
        for (int k = 0; k < t.kingdoms(); k++) {
            int seat = t.kingdomSeat()[k];
            assertEquals(seat, t.duchySeat()[t.duchy()[t.county()[seat]]], "a kingdom's seat isn't a duchy seat");
        }
        for (int e = 0; e < t.empires(); e++) {
            int seat = t.empireSeat()[e];
            assertEquals(seat, t.kingdomSeat()[t.kingdom()[t.duchy()[t.county()[seat]]]], "an empire's seat isn't a kingdom seat");
            assertEquals(TitleMap.Tier.EMPIRE, t.seatOf(seat));
        }
    }

    @Test
    void seatsFavourTheShore() {
        // Among the counties that reach the shore (the first land column, beside the sea): their seats are on the shore
        // more often than their shore provinces are among their provinces.
        Land l = land(30, 4);
        int shoreSeats = 0;
        int seats = 0;
        int shoreMembers = 0;
        int members = 0;
        for (long seed = 0; seed < 20; seed++) {
            TitleMap t = titles(l, seed);
            boolean[] reaches = new boolean[t.counties()];
            for (int p = 0; p < l.count(); p += 29) {
                reaches[t.county()[p]] = true;
            }
            for (int p = 0; p < l.count(); p++) {
                if (reaches[t.county()[p]]) {
                    members++;
                    shoreMembers += p % 29 == 0 ? 1 : 0;
                }
            }
            for (int c = 0; c < t.counties(); c++) {
                if (reaches[c]) {
                    seats++;
                    shoreSeats += t.countySeat()[c] % 29 == 0 ? 1 : 0;
                }
            }
        }
        double shoreShare = (double) shoreMembers / members;
        assertTrue((double) shoreSeats / seats > shoreShare + 0.1, "the shore isn't favoured: " + shoreSeats + " of " + seats
                + " seats against " + shoreMembers + " of " + members + " provinces");
    }

    @Test
    void anIslandStandsAlone() {
        Land l = land(10, 4);
        int[] provinces = l.provinces().clone();
        boolean[] sea = l.sea().clone();
        int island = l.count();
        // A lone province in the sea column, two pixels clear of the land.
        for (int z = 16; z < 18; z++) {
            for (int x = 0; x < 2; x++) {
                provinces[z * l.width() + x] = island;
                sea[z * l.width() + x] = false;
            }
        }
        TitleMap t = TitleMap.build(3, l.width(), l.width(), provinces, island + 1, sea);
        int county = t.county()[island];
        int members = 0;
        for (int c : t.county()) {
            if (c == county) {
                members++;
            }
        }
        assertEquals(1, members, "the island was joined to land it doesn't touch");
    }

    @Test
    void theSameSeedDrawsTheSameTitles() {
        Land l = land(20, 4);
        TitleMap a = titles(l, 42);
        TitleMap b = titles(l, 42);
        assertArrayEquals(a.county(), b.county());
        assertArrayEquals(a.empireSeat(), b.empireSeat());
    }
}
