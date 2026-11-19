package rafasconquest.battle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnitCardsTest {
    @AfterEach
    void onlyBuiltIns() {
        UnitCards.replace(List.of());
    }

    @Test
    void theBuiltInCardsComeFromTheirFilesInTheJar() {
        assertEquals(26, Arm.values().length);
        assertEquals(12, Machine.count());
        assertTrue(Arm.HEAVY_INFANTRY.shielded());
        assertTrue(Arm.LEVY.expendable());
        assertEquals(Arm.Missile.SHOT, Arm.MUSKETEERS.missile());
        assertEquals(1.3, Arm.HEAVY_CAVALRY.hits(), 1e-9);
        assertEquals(6, Machine.TREBUCHET.perUnit);
        assertEquals(Machine.Role.TOWER, Machine.SIEGE_TOWER.role());
        assertSame(Arm.TREBUCHETS, Arm.of(Machine.TREBUCHET));
        assertSame(Arm.ENGINES, Arm.of(Machine.BATTERING_RAM));
        assertEquals(Arm.CULVERINS.ordinal(), 10, "city armies' old saves name the built-ins by these ordinals");
    }

    @Test
    void aNewCardsFileAddsACardThatDeploysByItsKind() {
        UnitCards.apply(UnitCards.ARMS, "pikemen", """
                {"name": "Pikemen", "kind": "foot", "hits": 1.1, "traits": ["anti_horse"], "front": 0}""");
        UnitCards.apply(UnitCards.ENGINES, "bombard", """
                {"entity": "siegeworks:mons_meg", "per_unit": 4, "reach": 100, "reload": 30, "damage": 70, "field": true}""");
        UnitCards.apply(UnitCards.ARMS, "bombards", """
                {"kind": "engine", "engine": "bombard"}""");
        Arm pikemen = Arm.byId("pikemen");
        Arm bombards = Arm.byId("bombards");
        assertNotNull(pikemen);
        assertEquals(26, pikemen.ordinal(), "after the built-ins");
        assertTrue(pikemen.infantry() && pikemen.has("anti_horse"));
        assertEquals(1, pikemen.pace(), 1e-9, "a field left out keeps the plain rate");
        assertSame(bombards, Arm.of(Machine.byId("bombard")));
        assertEquals(50, Machine.byId("bombard").crew, 1e-9);

        int[] mix = new int[Arm.values().length];
        mix[Arm.HEAVY_INFANTRY.ordinal()] = 10;
        mix[pikemen.ordinal()] = 10;
        mix[Arm.ARCHERS.ordinal()] = 4;
        mix[bombards.ordinal()] = 2;
        mix[Arm.CAVALRY.ordinal()] = 4;
        List<BattlePlan.Slot> slots = BattlePlan.deployment(30, false, new double[]{0.25, 0.5, 0.25}, false, 0, mix);
        assertEquals(30, slots.size());
        assertEquals(10, slots.stream().filter(s -> s.arm() == pikemen).count());
        assertTrue(slots.stream().filter(s -> s.line() == 0).allMatch(s -> s.arm() == pikemen), "front 0: the pikes take the front rank");
        assertTrue(slots.stream().filter(s -> s.arm() == bombards).allMatch(s -> s.band() == BattlePlan.BAND_ARCHERS), "engines stand with the shooters");
        assertTrue(slots.stream().filter(s -> s.arm() == Arm.CAVALRY).allMatch(s -> s.band() == BattlePlan.BAND_CAVALRY));
    }

    @Test
    void shipCardsComeFromFilesAndANewUnitShipSetsTheFleetsRoom() {
        rafasconquest.core.SeaLanes.Size xxl = rafasconquest.core.SeaLanes.Size.XXLARGE;
        assertEquals(41, xxl.length);
        assertEquals("xxlarge.nbt", xxl.hull);
        assertSame(xxl, rafasconquest.core.SeaLanes.Size.unitShip());
        assertEquals(26, FieldBattle.fleetSpacing(), 1e-9, "as it always was");
        assertEquals(31, rafasconquest.core.SeaLanes.Size.longestOnLanes());

        UnitCards.apply(UnitCards.SHIPS, "carrack", """
                {"name": "Carrack", "length": 51, "width": 15, "deck": 6, "speed": 0.8, "battle": "unit", "hull": "carrack.nbt"}""");
        rafasconquest.core.SeaLanes.Size carrack = rafasconquest.core.SeaLanes.Size.byId("carrack");
        assertSame(xxl, rafasconquest.core.SeaLanes.Size.battle("unit", null), "the first card of a role is the built-in");
        assertEquals(7, carrack.ordinal());
        assertEquals(0, carrack.lanes, "a card left off the lanes by default");
        UnitCards.apply(UnitCards.SHIPS, "xxlarge", """
                {"battle": ""}""");
        assertSame(carrack, rafasconquest.core.SeaLanes.Size.unitShip());
        assertEquals(30, FieldBattle.fleetSpacing(), 1e-9);
        assertEquals(53, FieldBattle.shipLength(), 1e-9);
        UnitCards.apply(UnitCards.SHIPS, "xxlarge", """
                {"battle": "unit"}""");
    }

    @Test
    void aCardWhoseModIsMissingFightsAsItsStandIn() {
        boolean was = BattlePlan.zweihandlers;
        BattlePlan.zweihandlers = false;
        try {
            assertSame(Arm.SKIRMISHERS, Arm.ZWEIHANDLERS.fielded());
            assertSame(Arm.HEAVY_INFANTRY, Arm.HALBERDIERS.fielded());
            assertSame(Arm.LEVY, Arm.LEVY.fielded());
        } finally {
            BattlePlan.zweihandlers = was;
        }
    }
}
