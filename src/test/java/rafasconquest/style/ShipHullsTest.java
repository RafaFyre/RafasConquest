package rafasconquest.style;

import org.junit.jupiter.api.Test;
import rafasconquest.core.SeaLanes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ShipHullsTest {
    @Test
    void gunshipGunsStandTwoRailPlanksApart() {
        List<Integer> zs = ShipHulls.hull(SeaLanes.Size.GUNSHIP, false).stream()
                .filter(p -> p.state().getBlock() instanceof rafasconquest.content.MediumCannonBlock && p.x() < 0)
                .map(ShipHulls.Part::z).sorted().toList();
        assertFalse(zs.isEmpty());
        for (int i = 1; i < zs.size(); i++) {
            assertEquals(3, zs.get(i) - zs.get(i - 1), "guns at " + zs);
        }
    }

    @Test
    void theManOWarHasThreeGunDecksAndThreeMastsTheMainTallest() {
        SeaLanes.Size mow = SeaLanes.Size.MAN_O_WAR;
        assertEquals(mow, rafasconquest.battle.BattlePlan.Arm.MAN_O_WAR.ship());
        List<ShipHulls.Part> parts = ShipHulls.hull(mow, false);
        List<ShipHulls.Part> hull = parts.stream().filter(p -> p.y() <= mow.deck + 1).toList();
        assertEquals(17, hull.stream().mapToInt(ShipHulls.Part::x).max().getAsInt() - hull.stream().mapToInt(ShipHulls.Part::x).min().getAsInt() + 1);
        assertEquals(67, hull.stream().mapToInt(ShipHulls.Part::z).max().getAsInt() - hull.stream().mapToInt(ShipHulls.Part::z).min().getAsInt() + 1);
        // three rows of guns down each side, the same spacing on each
        List<ShipHulls.Part> right = parts.stream().filter(p -> p.state().getBlock() instanceof rafasconquest.content.MediumCannonBlock && p.x() < 0).toList();
        assertEquals(3, right.stream().map(ShipHulls.Part::y).distinct().count());
        for (int y : right.stream().map(ShipHulls.Part::y).distinct().toList()) {
            List<Integer> zs = right.stream().filter(p -> p.y() == y).map(ShipHulls.Part::z).sorted().toList();
            for (int i = 1; i < zs.size(); i++) {
                assertEquals(3, zs.get(i) - zs.get(i - 1), "guns at " + zs);
            }
        }
        assertEquals(2 * right.size(), mow.posts.stream().filter(p -> !p.gun().isEmpty()).count(), "a gunner for every gun");
        // masts: the fore and mizzen alike, the main taller
        java.util.Map<Integer, Integer> heads = new java.util.TreeMap<>();
        parts.stream().filter(p -> p.x() == 0 && p.state().is(net.minecraft.world.level.block.Blocks.SPRUCE_LOG)
                        && p.state().getValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS) == net.minecraft.core.Direction.Axis.Y)
                .forEach(p -> heads.merge(p.z(), p.y(), Math::max));
        List<Integer> h = List.copyOf(heads.values()); // mizzen, main, fore
        assertEquals(3, h.size());
        assertEquals(h.get(0), h.get(2));
        assertEquals(4, h.get(1) - h.get(0));
    }

    @Test
    void theGunshipCardSailsTheGunshipAndFiresMediumCannons() {
        assertEquals(SeaLanes.Size.GUNSHIP, rafasconquest.battle.BattlePlan.Arm.GUNSHIP.ship());
        String cannon = rafasconquest.battle.Machine.MEDIUM_CANNON.id;
        assertFalse(SeaLanes.Size.GUNSHIP.gunsRight.isEmpty());
        assertEquals(List.of(cannon), SeaLanes.Size.GUNSHIP.gunsRight.stream().distinct().toList());
        assertEquals(List.of(cannon), SeaLanes.Size.GUNSHIP.gunsLeft.stream().distinct().toList());
        assertFalse(SeaLanes.Size.GUNSHIP.posts.stream().noneMatch(p -> cannon.equals(p.gun())), "no post works a cannon");
    }
}
