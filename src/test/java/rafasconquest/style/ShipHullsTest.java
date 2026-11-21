package rafasconquest.style;

import org.junit.jupiter.api.Test;
import rafasconquest.core.SeaLanes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void oaredHullsAreGalleysTheRatesAndTheGunshipSail() {
        assertEquals(SeaLanes.Propulsion.OARS, SeaLanes.Size.XXLARGE.propulsion);
        assertEquals(SeaLanes.Propulsion.SAIL, SeaLanes.Size.MAN_O_WAR.propulsion);
        assertEquals(SeaLanes.Propulsion.SAIL, SeaLanes.Size.FIRST_RATE.propulsion);
        assertEquals(SeaLanes.Propulsion.SAIL, SeaLanes.Size.GUNSHIP.propulsion, "the card's propulsion wins over its oars");
    }

    /** The z of a hull's mast: its upright logs on the centreline, above the deck. */
    private static java.util.Set<Integer> mastRows(List<ShipHulls.Part> parts) {
        return parts.stream().filter(p -> p.x() == 0 && p.y() > ShipHulls.DECK + 2 && p.state().is(net.minecraft.world.level.block.Blocks.SPRUCE_LOG))
                .map(ShipHulls.Part::z).collect(java.util.stream.Collectors.toSet());
    }

    @Test
    void aGalleysMastStandsAmidshipsAMerchantsAstern() {
        assertEquals(java.util.Set.of(0), mastRows(ShipHulls.hull(SeaLanes.Size.LARGE, false)));
        int stern = -(SeaLanes.Size.LARGE.length / 2) + 2;
        assertEquals(java.util.Set.of(stern), mastRows(ShipHulls.hull(SeaLanes.Size.LARGE, true)), "the large merchant's");
    }

    @Test
    void aCannonGalleyCarriesTwoGunsOverItsBowEachWithAGunner() {
        SeaLanes.Size g = SeaLanes.Size.CANNON_GALLEY;
        assertEquals(SeaLanes.Propulsion.OARS, g.propulsion);
        assertTrue(g.length > SeaLanes.Size.XXLARGE.length, "no longer than a plain galley");
        assertEquals(2, g.gunsBow.size());
        assertTrue(g.gunsRight.isEmpty() && g.gunsLeft.isEmpty(), "bow guns counted in a broadside");
        assertEquals(2, g.posts.stream().filter(p -> !p.gun().isEmpty()).count(), "a gunner for each gun");
        assertTrue(g.posts.stream().allMatch(p -> p.z() > g.length / 4), "the gunners stand forward");
        assertEquals(rafasconquest.battle.BattlePlan.Arm.CANNON_GALLEY.ship(), g);
    }

    @Test
    void engineGalleysCarryTheirEnginesOverTheBowEachWithItsCrew() {
        Object[][] galleys = {
                {SeaLanes.Size.ONAGER_GALLEY, rafasconquest.battle.Machine.ONAGER, 1},
                {SeaLanes.Size.TREBUCHET_GALLEY, rafasconquest.battle.Machine.TREBUCHET, 1},
                {SeaLanes.Size.BALLISTA_GALLEY, rafasconquest.battle.Machine.ARCBALLISTA, 2},
                {SeaLanes.Size.CATAPULT_GALLEY, rafasconquest.battle.Machine.CATAPULT, 2}};
        for (Object[] row : galleys) {
            SeaLanes.Size g = (SeaLanes.Size) row[0];
            rafasconquest.battle.Machine engine = (rafasconquest.battle.Machine) row[1];
            int count = (int) row[2];
            assertEquals(SeaLanes.Propulsion.OARS, g.propulsion, g.id);
            assertEquals(java.util.Collections.nCopies(count, engine.id), g.gunsBow, g.id);
            assertTrue(g.gunsRight.isEmpty() && g.gunsLeft.isEmpty(), g.id + ": an engine counted in a broadside");
            assertEquals(count, g.posts.stream().filter(p -> engine.id.equals(p.gun())).count(), g.id + ": a crew for each engine");
            assertTrue(g.length > SeaLanes.Size.XXLARGE.length, g.id + " is no longer than a plain galley");
            assertEquals(java.util.Set.of(0), mastRows(ShipHulls.hull(g, false)), g.id + "'s mast");
        }
        assertEquals("siegeworks:onager", rafasconquest.battle.Machine.CATAPULT.entity(), "the catapult takes the onager's model");
    }

    @Test
    void aFightingTowerGalleysArchersStandOnItsRaisedPlatform() {
        SeaLanes.Size t = SeaLanes.Size.TOWER_GALLEY;
        assertEquals(SeaLanes.Propulsion.OARS, t.propulsion);
        assertEquals(25, t.posts.size());
        assertTrue(t.posts.stream().allMatch(p -> p.y() == ShipHulls.DECK + ShipHulls.TOWER_HEIGHT + 1 && p.gun().isEmpty()));
        assertTrue(t.archerReach > 1, "no further from the tower");
        assertTrue(rafasconquest.battle.BattlePlan.Arm.TOWER_GALLEY.shoots(), "the tower galley's crew aren't archers");
        // every post is on the hull, inside its rails
        List<ShipHulls.Part> parts = ShipHulls.hull(t, false);
        int maxZ = parts.stream().mapToInt(ShipHulls.Part::z).max().getAsInt();
        assertTrue(t.posts.stream().allMatch(p -> p.z() < maxZ && Math.abs(p.x()) <= 2));
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
    void eachRateIsBiggerThanTheLastWithTenMoreGunsASideAndTallerMastsAndSails() {
        SeaLanes.Size[] rates = {SeaLanes.Size.MAN_O_WAR, SeaLanes.Size.THIRD_RATE, SeaLanes.Size.SECOND_RATE,
                SeaLanes.Size.FIRST_RATE, SeaLanes.Size.GRAND_FLAGSHIP};
        rafasconquest.battle.BattlePlan.Arm[] arms = {rafasconquest.battle.BattlePlan.Arm.MAN_O_WAR, rafasconquest.battle.BattlePlan.Arm.THIRD_RATE,
                rafasconquest.battle.BattlePlan.Arm.SECOND_RATE, rafasconquest.battle.BattlePlan.Arm.FIRST_RATE, rafasconquest.battle.BattlePlan.Arm.GRAND_FLAGSHIP};
        int[] sideGuns = {45, 50, 60, 70, 80};
        long lastMast = 0;
        long lastSail = 0;
        for (int i = 0; i < rates.length; i++) {
            SeaLanes.Size s = rates[i];
            assertEquals(s, arms[i].ship());
            List<ShipHulls.Part> parts = ShipHulls.hull(s, false);
            List<ShipHulls.Part> guns = parts.stream().filter(p -> p.state().getBlock() instanceof rafasconquest.content.MediumCannonBlock).toList();
            assertEquals(sideGuns[i], guns.stream().filter(p -> p.x() < 0).count(), s.id);
            assertEquals(sideGuns[i], guns.stream().filter(p -> p.x() > 0).count(), s.id);
            assertEquals(sideGuns[i], s.gunsRight.size(), s.id);
            assertEquals(2L * sideGuns[i], s.posts.stream().filter(p -> !p.gun().isEmpty()).count(), "a gunner for every gun: " + s.id);
            int half = s.length / 2;
            assertTrue(guns.stream().allMatch(p -> half - Math.abs(p.z()) >= 12), "guns clear of forecastle and quarterdeck: " + s.id);
            long mast = parts.stream().filter(p -> p.state().is(net.minecraft.world.level.block.Blocks.SPRUCE_LOG) && p.x() == 0).mapToInt(ShipHulls.Part::y).max().getAsInt();
            long sail = parts.stream().filter(p -> p.state().getBlock() instanceof rafasconquest.content.SailBlock).count();
            if (i > 0) {
                SeaLanes.Size last = rates[i - 1];
                assertTrue(s.length > last.length && s.width > last.width && s.deck > last.deck, s.id + " bigger than " + last.id);
                assertTrue(mast > lastMast && sail > lastSail, s.id + "'s masts and sails bigger than " + last.id + "'s");
            }
            lastMast = mast;
            lastSail = sail;
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
