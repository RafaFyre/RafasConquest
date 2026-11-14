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

    /** Which provinces touch which, on the land's grid. */
    private static java.util.List<java.util.TreeSet<Integer>> adjacency(Land l) {
        java.util.List<java.util.TreeSet<Integer>> adj = new java.util.ArrayList<>();
        for (int i = 0; i < l.count(); i++) {
            adj.add(new java.util.TreeSet<>());
        }
        int w = l.width();
        for (int z = 0; z < w; z++) {
            for (int x = 0; x < w; x++) {
                int p = l.provinces()[z * w + x];
                for (int q : new int[]{x + 1 < w ? l.provinces()[z * w + x + 1] : -1, z + 1 < w ? l.provinces()[(z + 1) * w + x] : -1}) {
                    if (p >= 0 && q >= 0 && p != q) {
                        adj.get(p).add(q);
                        adj.get(q).add(p);
                    }
                }
            }
        }
        return adj;
    }

    @Test
    void aProvinceTakenJoinsTheTakersTitleNextToItAndGoesBackWhenReturned() {
        Land l = land(30, 4);
        TitleMap t = titles(l, 13);
        var adj = adjacency(l);
        int[] holders = new int[l.count()];
        for (int p = 0; p < holders.length; p++) {
            holders[p] = t.county()[p]; // each county its own lord
        }
        // A province that isn't a seat, touching another county.
        int moved = -1;
        int into = -1;
        for (int p = 0; p < l.count() && moved < 0; p++) {
            if (t.countySeat()[t.county()[p]] == p) {
                continue;
            }
            for (int q : adj.get(p)) {
                if (t.county()[q] != t.county()[p]) {
                    moved = p;
                    into = t.county()[q];
                    break;
                }
            }
        }
        int from = t.county()[moved];
        int[] seatsBefore = t.countySeat().clone();
        holders[moved] = into;
        t.regroup(holders, adj);
        assertEquals(into, t.county()[moved], "the province didn't join its taker's county");
        assertArrayEquals(seatsBefore, t.countySeat());
        for (int c = 0; c < t.counties(); c++) {
            assertEquals(c, t.county()[t.countySeat()[c]], "a seat left its title");
        }
        holders[moved] = from;
        t.regroup(holders, adj);
        assertEquals(from, t.county()[moved], "given back, it didn't go back");
    }

    @Test
    void rankFollowsTheFiefAsDrawnAndAsItShrinks() {
        Land l = land(30, 4);
        TitleMap t = titles(l, 17);
        int seats = 0;
        int matching = 0;
        for (int c = 0; c < t.counties(); c++) {
            int seat = t.countySeat()[c];
            seats++;
            matching += t.rank(seat) == t.seatOf(seat) ? 1 : 0;
        }
        assertTrue(matching >= seats * 0.85, "ranks as drawn too far from the titles: " + matching + " of " + seats);
        // A duchy's holder losing all but their own county is a count's rank.
        int seat = t.duchySeat()[0];
        int[] holders = new int[l.count()];
        java.util.Arrays.fill(holders, 1);
        for (int p = 0; p < holders.length; p++) {
            if (t.county()[p] == t.county()[seat]) {
                holders[p] = 0;
            }
        }
        holders[seat] = 0;
        t.regroup(holders, adjacency(l));
        if (t.seatOf(seat) == TitleMap.Tier.DUCHY) {
            assertEquals(TitleMap.Tier.COUNTY, t.rank(seat));
        }
    }

    @Test
    void seatsOverAProvinceRunEmpireDownToCounty() {
        // Provinces 0 and 1 are counties 0 and 1 of one duchy, kingdom and empire; province 2 lies in no county.
        TitleMap t = new TitleMap(new int[]{0, 1, -1}, new int[]{0, 0}, new int[]{0}, new int[]{0},
                new int[]{0, 1}, new int[]{0}, new int[]{0}, new int[]{1});
        assertArrayEquals(new int[]{1, 0, 0, 1}, t.seatsOver(1));
        assertEquals(0, t.seatsOver(2).length);
    }
}
