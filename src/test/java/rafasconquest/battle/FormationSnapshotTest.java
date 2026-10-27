package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.FormationSnapshot.Kind;
import rafasconquest.battle.FormationSnapshot.Sprite;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormationSnapshotTest {
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
        return FormationSnapshot.of(3, Kind.SWORDSMEN, 0xB02E26, 0xF9FFFE, xs, ys, zs, 1, 0, fighting, true);
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
        FormationSnapshot general = FormationSnapshot.of(6, Kind.GENERAL, 0, 0, new double[]{5}, new double[]{70}, new double[]{-2}, 0, 1, false, false);
        List<Sprite> sprites = general.layout();
        assertEquals(1, sprites.size());
        assertEquals(5, sprites.get(0).x(), 1e-9);
        assertEquals(-2, sprites.get(0).z(), 1e-9);
    }

    @Test
    void glidesBetweenSnapshots() {
        FormationSnapshot a = twoRanksFacingEast(false);
        FormationSnapshot b = new FormationSnapshot(3, Kind.SWORDSMEN, 0, 0, 18, a.x() + 10, a.y(), a.z(), 0, 1, 4, 4, true, false);
        FormationSnapshot half = b.from(a, 0.5);
        assertEquals(a.x() + 5, half.x(), 1e-9);
        assertEquals(1, Math.hypot(half.facingX(), half.facingZ()), 1e-9, "still a direction");
        assertEquals(18, half.alive(), "counts don't glide");
        assertEquals(b.x(), b.from(a, 1).x(), 1e-9);
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
