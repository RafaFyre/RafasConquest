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
    void theGunshipCardSailsTheGunshipAndFiresMediumCannons() {
        assertEquals(SeaLanes.Size.GUNSHIP, rafasconquest.battle.BattlePlan.Arm.GUNSHIP.ship());
        String cannon = rafasconquest.battle.Machine.MEDIUM_CANNON.id;
        assertFalse(SeaLanes.Size.GUNSHIP.gunsRight.isEmpty());
        assertEquals(List.of(cannon), SeaLanes.Size.GUNSHIP.gunsRight.stream().distinct().toList());
        assertEquals(List.of(cannon), SeaLanes.Size.GUNSHIP.gunsLeft.stream().distinct().toList());
        assertFalse(SeaLanes.Size.GUNSHIP.posts.stream().noneMatch(p -> cannon.equals(p.gun())), "no post works a cannon");
    }
}
