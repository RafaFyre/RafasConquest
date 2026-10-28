package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.FormationSnapshot.Kind;
import rafasconquest.battle.FormationSnapshot.Sprite;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormationSnapshotTest {
    /** Entity ids 100 to 119, one for each soldier of a formation of 20. */
    private static final int[] IDS = java.util.stream.IntStream.range(100, 120).toArray();

    /** A formation of 20 in two ranks of 10 facing east (+x), its front rank at x = 0, centred on z = 0. */
    private static FormationSnapshot twoRanksFacingEast(boolean fighting) {
        double[] xs = new double[20];
        double[] ys = new double[20];
        double[] zs = new double[20];
        for (int i = 0; i < 20; i++) {
            xs[i] = -(i / 10) * BattlePlan.SPACING;
            ys[i] = 63;
            zs[i] = (i % 10 - 4.5) * BattlePlan.SPACING;
        }
        return FormationSnapshot.of(3, Kind.SWORDSMEN, 0xB02E26, 0xF9FFFE, xs, ys, zs, 1, 0, fighting, true, IDS, IDS[BattlePlan.LEADER_INDEX]);
    }

    @Test
    void summarisesWhereTheSoldiersStandAndHowSpreadOutTheyAre() {
        FormationSnapshot s = twoRanksFacingEast(false);
        assertEquals(20, s.alive());
        assertEquals(-BattlePlan.SPACING / 2, s.x(), 1e-9);
        assertEquals(0, s.z(), 1e-9);
        assertEquals(63, s.y(), 1e-9);
        // A rank of 10 at 1.75 apart covers about 17.5 blocks; two ranks about 3.5.
        assertEquals(8.75, s.halfWidth(), 0.5);
        assertEquals(1.5, s.halfDepth(), 0.3);
    }

    @Test
    void laysOutEverySoldierInRanksAcrossTheFormation() {
        List<Sprite> sprites = twoRanksFacingEast(false).layout();
        assertEquals(20, sprites.size());
        double minZ = sprites.stream().mapToDouble(Sprite::z).min().orElseThrow();
        double maxZ = sprites.stream().mapToDouble(Sprite::z).max().orElseThrow();
        assertTrue(maxZ - minZ > 14 && maxZ - minZ < 19, "about as wide as the line: " + (maxZ - minZ));
        assertEquals(2, sprites.stream().mapToDouble(Sprite::x).map(x -> Math.round(x * 100)).distinct().count(), "two ranks");
    }

    @Test
    void theLeaderStandsInTheMiddleOfTheFrontRank() {
        List<Sprite> sprites = twoRanksFacingEast(false).layout();
        List<Sprite> leaders = sprites.stream().filter(Sprite::leader).toList();
        assertEquals(1, leaders.size());
        double front = sprites.stream().mapToDouble(Sprite::x).max().orElseThrow();
        assertEquals(front, leaders.get(0).x(), 1e-9, "front rank faces east, so it has the largest x");
        assertTrue(Math.abs(leaders.get(0).z()) < 3);
    }

    @Test
    void aFightingFormationIsScatteredTheSameWayEveryTime() {
        List<Sprite> calm = twoRanksFacingEast(false).layout();
        List<Sprite> melee = twoRanksFacingEast(true).layout();
        assertEquals(melee, twoRanksFacingEast(true).layout());
        assertTrue(calm.get(0).x() != melee.get(0).x() || calm.get(0).z() != melee.get(0).z());
    }

    @Test
    void aGeneralIsOneSpriteWhereTheyStand() {
        FormationSnapshot general = FormationSnapshot.of(6, Kind.GENERAL, 0, 0, new double[]{5}, new double[]{70}, new double[]{-2}, 0, 1, false, false,
                new int[]{900}, -1);
        List<Sprite> sprites = general.layout();
        assertEquals(1, sprites.size());
        assertEquals(5, sprites.get(0).x(), 1e-9);
        assertEquals(-2, sprites.get(0).z(), 1e-9);
    }

    @Test
    void glidesBetweenSnapshots() {
        FormationSnapshot a = twoRanksFacingEast(false);
        FormationSnapshot b = new FormationSnapshot(3, Kind.SWORDSMEN, 0, 0, 18, a.x() + 10, a.y(), a.z(), 0, 1, 4, 4, true, false, new int[18], -1);
        FormationSnapshot half = b.from(a, 0.5);
        assertEquals(a.x() + 5, half.x(), 1e-9);
        assertEquals(1, Math.hypot(half.facingX(), half.facingZ()), 1e-9, "still a direction");
        assertEquals(18, half.alive(), "counts don't glide");
        assertEquals(b.x(), b.from(a, 1).x(), 1e-9);
    }

    @Test
    void soldiersTheClientHasStandWhereTheyReallyAre() {
        FormationSnapshot s = twoRanksFacingEast(false);
        List<Sprite> sprites = s.place(id -> new double[]{id, 70, -id});
        assertEquals(20, sprites.size());
        assertEquals(new Sprite(100, 70, -100, false, true), sprites.get(0));
        assertEquals(1, sprites.stream().filter(Sprite::leader).count());
        assertTrue(sprites.get(BattlePlan.LEADER_INDEX).leader(), "the leader is known by its entity id");
    }

    @Test
    void soldiersTheClientLacksTakeTheirPlaceInTheRanks() {
        FormationSnapshot s = twoRanksFacingEast(false);
        List<Sprite> ranks = s.layout();
        List<Sprite> sprites = s.place(id -> id < 110 ? new double[]{0, 0, 0} : null);
        assertEquals(20, sprites.size());
        assertEquals(ranks.get(15), sprites.get(15));
    }

    @Test
    void aBlockThatIsOnlyNumbersStandsInItsRanks() {
        FormationSnapshot s = twoRanksFacingEast(false);
        FormationSnapshot numbers = new FormationSnapshot(s.id(), s.kind(), s.rgb(), s.mark(), s.alive(), s.x(), s.y(), s.z(),
                s.facingX(), s.facingZ(), s.halfWidth(), s.halfDepth(), false, true, new int[0], -1);
        assertEquals(numbers.layout(), numbers.place(id -> null));
    }

    @Test
    void onlySoldiersTheClientHasAreReal() {
        List<Sprite> sprites = twoRanksFacingEast(false).place(id -> id % 2 == 0 ? new double[]{0, 0, 0} : null);
        assertEquals(20, sprites.size(), "a soldier the client lacks still gets a figure");
        assertEquals(10, sprites.stream().filter(Sprite::real).count());
    }

    @Test
    void distantSoldiersArePulledInsideTheFarPlaneInOrder() {
        double render = 192;
        assertEquals(300, DistantDepth.drawDistance(300, render), 1e-9, "close enough to draw as it is");
        double previous = 0;
        for (double d = 100; d < 100_000; d *= 1.5) {
            double drawn = DistantDepth.drawDistance(d, render);
            assertTrue(drawn < DistantDepth.FAR * render, d + " drawn beyond the far plane");
            assertTrue(drawn > previous, "order kept at " + d);
            previous = drawn;
        }
    }
}
