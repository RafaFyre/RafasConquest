package rafasconquest.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.battle.FormationSnapshot.Kind;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FigureMotionTest {
    @AfterEach
    void forget() {
        FigureMotion.clear();
    }

    @Test
    void aFigureWalksToItsPlaceFacingTheWayItGoesThenTurnsToTheFront() {
        FigureMotion.State s = new FigureMotion.State(0, 0, 0, -1); // facing north
        FigureMotion.move(s, 3, 0, 0, -1, false, FigureMotion.FOOT_SPEED, 0.1); // its place is 3 blocks east
        assertEquals(FigureMotion.FOOT_SPEED * 0.1, s.x, 1e-9, "a walking pace, not a jump");
        double turned = Math.toDegrees(Math.atan2(s.fz, s.fx)) + 90;
        assertEquals(30, turned, 1e-6, "turns toward where it walks, a little at a time");
        for (int i = 0; i < 40; i++) {
            FigureMotion.move(s, 3, 0, 0, -1, false, FigureMotion.FOOT_SPEED, 0.1);
        }
        assertEquals(3, s.x, 1e-9);
        assertEquals(0, s.fx, 1e-6, "standing in its place, it faces the front again");
        assertEquals(-1, s.fz, 1e-6);
    }

    @Test
    void aboardAShipFiguresStandAtTheirPostsAsItSails() {
        FormationSnapshot.Ship hull = new FormationSnapshot.Ship(0, 0, rafasconquest.core.SeaLanes.Size.LARGE.ordinal());
        FormationSnapshot at = new FormationSnapshot(1, Kind.LEVY, 0xFF0000, 0, 6, 0, 64, 0, 0, 1, 2, 1, false, false, new int[0], -1, hull);
        FigureMotion.step(List.of(at), id -> null, 1000);
        FormationSnapshot on = at.movedTo(3, 64, 0);
        List<FormationSnapshot.Sprite> posts = on.layout();
        for (FigureMotion.Placed p : FigureMotion.step(List.of(on), id -> null, 1050).get(0)) {
            assertTrue(posts.stream().anyMatch(s -> Math.hypot(s.x() - p.x(), s.z() - p.z()) < 1e-9), "at a post, not walking after it");
        }
    }

    @Test
    void whenABlockTurnsItsSoldiersWalkRoundRatherThanTheBlockSwinging() {
        FormationSnapshot north = block(1, 0xFF0000, 0, 0, 0, -1, false);
        List<FigureMotion.Placed> before = FigureMotion.step(List.of(north), id -> null, 1000).get(0);
        FormationSnapshot east = block(1, 0xFF0000, 0, 0, 1, 0, false);
        List<FigureMotion.Placed> after = FigureMotion.step(List.of(east), id -> null, 1050).get(0);
        List<FormationSnapshot.Sprite> slots = east.layout();
        for (int i = 0; i < after.size(); i++) {
            double moved = Math.hypot(after.get(i).x() - before.get(i).x(), after.get(i).z() - before.get(i).z());
            assertTrue(moved <= FigureMotion.FOOT_SPEED * 0.05 * 10, "figure " + i + " jumped " + moved);
        }
        assertTrue(slots.stream().anyMatch(s -> Math.hypot(s.x() - after.get(slots.indexOf(s)).x(), s.z() - after.get(slots.indexOf(s)).z()) > 1),
                "the figures are still on their way to the turned ranks");
    }

    @Test
    void aFigureGlidesToTheRealSoldierSpawnedAwayFromItsPlaceThenKeepsUp() {
        FigureMotion.State s = new FigureMotion.State(0, 0, 0, -1);
        FigureMotion.move(s, 2, 0, 0, -1, true, FigureMotion.FOOT_SPEED, 0.05); // spawned 2 blocks east
        assertTrue(s.x > 0 && s.x < 2, "a glide, not a jump: " + s.x);
        for (int i = 1; i <= 20; i++) { // the soldier then walks east at a foot soldier's pace
            FigureMotion.move(s, 2 + i * FigureMotion.FOOT_SPEED * 0.05, 0, 0, -1, true, FigureMotion.FOOT_SPEED, 0.05);
        }
        assertEquals(2 + 20 * FigureMotion.FOOT_SPEED * 0.05, s.x, 1e-9, "caught up within a second");
    }

    @Test
    void aWalkingFigureSwingsItsLegsAndStillsWhenItStands() {
        FigureMotion.State s = new FigureMotion.State(0, 0, 0, -1);
        for (int i = 0; i < 20; i++) { // a second's walk after a place that keeps 3 blocks ahead (no hurrying)
            FigureMotion.move(s, s.x + 3, 0, 0, -1, false, FigureMotion.FOOT_SPEED, 0.05);
        }
        assertEquals(Math.min(FigureMotion.FOOT_SPEED / 5, 1), s.amount, 0.02, "a walking pace's stride");
        assertTrue(s.swing > 10, "the cycle runs on: " + s.swing);
        double there = s.x + 3;
        for (int i = 0; i < 400; i++) { // there, and standing
            FigureMotion.move(s, there, 0, 0, -1, false, FigureMotion.FOOT_SPEED, 0.05);
        }
        assertEquals(0, s.amount, 0.01, "legs still");
    }

    @Test
    void aFigureShufflingAShortWaySidestepsFacingTheFront() {
        FigureMotion.State s = new FigureMotion.State(0, 0, 0, -1); // facing north
        FigureMotion.move(s, 2, 0, 0, -1, false, FigureMotion.FOOT_SPEED, 0.1); // its place is 2 blocks east
        assertEquals(FigureMotion.FOOT_SPEED * 0.1, s.x, 1e-9);
        assertEquals(-1, s.fz, 1e-9, "still faces north");
    }

    @Test
    void aFigureKeepingUpWithAMarchingBlockFacesTheMarch() {
        FigureMotion.State s = new FigureMotion.State(0, 0, 0, -1); // facing north, its block's front
        for (int i = 1; i <= 20; i++) { // its place marches east at 2 blocks a second, the figure right on it
            FigureMotion.move(s, i * 0.1, 0, 0, -1, false, FigureMotion.FOOT_SPEED, 0.05);
        }
        assertEquals(1, s.fx, 1e-6, "turned to face the way the block marches");
    }

    @Test
    void aFigureStepsUpOneBlockButNotTwo() {
        FigureMotion.Ground was = FigureMotion.ground;
        try {
            // Flat at 60, a one-block step at x >= 1, a two-block wall at z >= 1.
            FigureMotion.ground = (x, z, near) -> z >= 1 ? 62 : x >= 1 ? 61 : 60;
            FigureMotion.State s = new FigureMotion.State(1.2, 0.5, 0, -1);
            s.y = 60;
            FigureMotion.stayBelowWalls(s, 0.5, 0.5, 64);
            assertEquals(1.2, s.x, 1e-9, "one block: steps up");
            s = new FigureMotion.State(0.8, 1.2, 0, 1);
            s.y = 60;
            FigureMotion.stayBelowWalls(s, 0.5, 0.5, 64);
            assertEquals(0.8, s.x, 1e-9, "slides along the wall");
            assertEquals(0.5, s.z, 1e-9, "two blocks: stopped at the wall");
        } finally {
            FigureMotion.ground = was;
        }
    }

    @Test
    void aFigureAboveTheGroundFallsAndOneBelowStepsUp() {
        FigureMotion.Ground was = FigureMotion.ground;
        try {
            FigureMotion.ground = (x, z, near) -> 60;
            FigureMotion.State s = new FigureMotion.State(0, 0, 0, -1);
            assertEquals(60, FigureMotion.settle(s, 64, 0.05), 1e-9, "first seen: on the ground");
            s.y = 70;
            double y1 = 70 - FigureMotion.settle(s, 64, 0.05);
            double y2 = 70 - FigureMotion.settle(s, 64, 0.05);
            assertTrue(y1 > 0 && y2 > 2 * y1, "falls, gathering speed: " + y1 + ", " + y2);
            for (int i = 0; i < 40; i++) {
                FigureMotion.settle(s, 64, 0.05);
            }
            assertEquals(60, s.y, 1e-9, "lands on the ground");
            FigureMotion.ground = (x, z, near) -> 62;
            assertEquals(62, FigureMotion.settle(s, 64, 0.05), 1e-9, "steps up at once");
            FigureMotion.ground = (x, z, near) -> Double.NaN;
            FigureMotion.State far = new FigureMotion.State(0, 0, 0, -1);
            assertEquals(64, FigureMotion.settle(far, 64, 0.05), 1e-9, "no ground known: the block's height");
        } finally {
            FigureMotion.ground = was;
        }
    }

    @Test
    void aBlockThatAboutFacesTurnsWhereItStands() {
        FormationSnapshot north = block(1, 0xFF0000, 0, 0, 0, -1, false);
        List<FigureMotion.Placed> before = FigureMotion.step(List.of(north), id -> null, 1000).get(0);
        FormationSnapshot south = block(1, 0xFF0000, 0, 0, 0, 1, false);
        List<FigureMotion.Placed> after = null;
        for (long t = 1050; t <= 6000; t += 50) {
            after = FigureMotion.step(List.of(south), id -> null, t).get(0);
        }
        int stayed = 0;
        for (int i = 0; i < after.size(); i++) {
            if (Math.hypot(after.get(i).x() - before.get(i).x(), after.get(i).z() - before.get(i).z()) < 0.5) {
                stayed++;
            }
            assertEquals(1, after.get(i).fz(), 1e-6, "figure " + i + " faces south");
        }
        // 20 in a 6 by 4 grid: the short back rank's few must cross to the new short back rank; the rest stay.
        assertTrue(stayed >= 14, "only " + stayed + " of 20 turned where they stood");
    }

    @Test
    void blocksInMeleeFlowIntoEachOther() {
        // Red faces east, blue faces west, their fronts 2 blocks apart.
        FormationSnapshot red = block(1, 0xFF0000, 0, 0, 1, 0, true);
        FormationSnapshot blue = block(2, 0x0000FF, 2 + 2 * red.halfDepth(), 0, -1, 0, true);
        List<List<FigureMotion.Placed>> placed = null;
        for (long t = 1000; t <= 6000; t += 50) {
            placed = FigureMotion.step(List.of(red, blue), id -> null, t);
        }
        double redFurthest = placed.get(0).stream().mapToDouble(FigureMotion.Placed::x).max().orElseThrow();
        double blueFurthest = placed.get(1).stream().mapToDouble(FigureMotion.Placed::x).min().orElseThrow();
        assertTrue(redFurthest > blueFurthest + 1, "the front ranks mix: red reaches " + redFurthest + ", blue " + blueFurthest);
        for (FigureMotion.Placed p : placed.get(0)) {
            assertTrue(p.fx() > 0.9, "red faces blue");
        }
    }

    private static FormationSnapshot block(int id, int rgb, double x, double z, double fx, double fz, boolean fighting) {
        return new FormationSnapshot(id, Kind.SKIRMISHERS, rgb, 0, 20, x, 64, z, fx, fz, 4, 2, fighting, false, new int[0], -1, null);
    }
}
