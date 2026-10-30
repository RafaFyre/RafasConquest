package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Point;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattleSiteTest {
    /** Land in 16-block cells, with a river running east-west from z 96 to 127. */
    private static final BattleSite.Water RIVER = new BattleSite.Water() {
        @Override
        public int cell() {
            return 16;
        }

        @Override
        public boolean wet(int cellX, int cellZ) {
            return cellZ == 6 || cellZ == 7;
        }
    };

    /** A part of side {@code side}: blocks every 10 blocks across x -40..40, z from {@code z0} to {@code z1}. */
    private static BattleSite.Part part(int side, double z0, double z1) {
        List<Point> places = new ArrayList<>();
        for (double x = -40; x <= 40; x += 10) {
            for (double z = z0; z <= z1; z += 10) {
                places.add(new Point(x, z));
            }
        }
        return new BattleSite.Part(side, places);
    }

    private static boolean straddles(BattleSite.Part part, Point shift) {
        boolean below = false;
        boolean above = false;
        for (Point p : part.places()) {
            double z = p.z() + shift.z();
            below |= z < 96;
            above |= z >= 128;
            if (z >= 96 && z < 128) {
                return true;
            }
        }
        return below && above;
    }

    @Test
    void aBattleOnDryLandStaysWhereItIs() {
        Point shift = BattleSite.shift(List.of(part(0, 0, 40), part(1, -80, -40)), RIVER);
        assertEquals(0, shift.x());
        assertEquals(0, shift.z());
    }

    @Test
    void anArmyAcrossARiverIsMovedOffItTheShortestWay() {
        BattleSite.Part army = part(0, 40, 140);
        Point shift = BattleSite.shift(List.of(army, part(1, -60, 0)), RIVER);
        assertTrue(!straddles(army, shift), "still across the river, moved " + shift);
        assertTrue(Math.hypot(shift.x(), shift.z()) <= 150, "moved too far: " + shift);
    }

    @Test
    void armiesOnOppositeBanksAreBroughtTogetherIfThatIsCheap() {
        BattleSite.Part a = part(0, 140, 180);
        BattleSite.Part b = part(1, 40, 80);
        Point shift = BattleSite.shift(List.of(a, b), RIVER);
        assertTrue(!straddles(a, shift) && !straddles(b, shift), "a part across the river: " + shift);
        boolean bothBelow = a.places().stream().allMatch(p -> p.z() + shift.z() < 96)
                && b.places().stream().allMatch(p -> p.z() + shift.z() < 96);
        boolean bothAbove = a.places().stream().allMatch(p -> p.z() + shift.z() >= 128)
                && b.places().stream().allMatch(p -> p.z() + shift.z() >= 128);
        assertTrue(bothBelow || bothAbove, "the armies are still on opposite banks: " + shift);
    }
}
