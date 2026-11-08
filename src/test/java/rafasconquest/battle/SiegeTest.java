package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock.Duty;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SiegeTest {
    /** A castle 41 square about (0, 0), walls 5 thick, one gate on +x, attacked from +x. */
    private static Siege.Plan castle(Set<Siege.Engine> engines) {
        Siege.Gate gate = new Siege.Gate(new Point(21.5, 0), new Point(15.5, 3.5), 1, 0);
        return new Siege.Plan(-20, -20, 21, 21, 5, 27, 1, 0, List.of(gate), new Point(0.5, 0.5), engines);
    }

    private static FieldBattle siege(int aBlocks, int dBlocks, long seed, Set<Siege.Engine> engines) {
        Siege.Plan plan = castle(engines);
        double[] axis = plan.axis();
        FieldBattle battle = new FieldBattle(aBlocks, dBlocks, plan.battleCentre(), axis[0], axis[1], seed);
        battle.setType(BattleType.SIEGE);
        battle.besiege(plan);
        return battle;
    }

    @Test
    void defendersManTheAttackedWallWithReservesInsideAndMostCavalryOnFoot() {
        FieldBattle battle = siege(40, 40, 1, EnumSet.allOf(Siege.Engine.class));
        Siege siege = battle.siege();
        assertNotNull(siege);
        int onWall = 0;
        int inside = 0;
        int superHeavy = 0;
        int riders = 0;
        for (FieldBlock b : battle.army(1).blocks()) {
            assertTrue(siege.region(b.pos()) != Siege.Region.OUT, "a defender deployed outside the walls");
            onWall += siege.region(b.pos()) == Siege.Region.WALL ? 1 : 0;
            inside += siege.region(b.pos()) == Siege.Region.IN ? 1 : 0;
            superHeavy += b.superHeavy() ? 1 : 0;
            riders += b.arm() == Arm.CAVALRY && b.duty() == Duty.SUPPORT ? 1 : 0;
        }
        assertEquals(2, onWall, "one block of archers and one of foot on a castle's wall");
        assertTrue(inside > onWall, "most wait inside");
        assertEquals(Math.round((superHeavy + riders) / 10.0), riders, superHeavy + " dismounted against " + riders + " riding");
        assertTrue(superHeavy > 0);
        long works = siege.works().size();
        assertTrue(works > 0 && works <= Siege.LADDERS + Siege.TOWERS + Siege.RAMS);
    }

    @Test
    void attackersDeployOutsideTheWalls() {
        for (int blocks : new int[]{10, 40, 200}) {
            FieldBattle battle = siege(blocks, 20, 2, EnumSet.allOf(Siege.Engine.class));
            for (FieldBlock b : battle.army(0).blocks()) {
                Point p = b.pos();
                assertTrue(p.x() > 21 + Siege.CLEAR - 0.01 || p.x() < -20 - Siege.CLEAR || p.z() > 21 + Siege.CLEAR
                        || p.z() < -20 - Siege.CLEAR, "an attacker deployed on or by the walls at " + p);
            }
        }
    }

    @Test
    void ramsTakeTheGateAndDefendersNeverFlee() {
        int taken = 0;
        for (long seed = 1; seed <= 3; seed++) {
            FieldBattle battle = siege(60, 15, seed, EnumSet.of(Siege.Engine.RAMS, Siege.Engine.LADDERS));
            while (!battle.over()) {
                battle.second();
                for (FieldBlock b : battle.army(1).blocks()) {
                    assertTrue(b.duty() != Duty.FLEEING && b.duty() != Duty.ROUTED, "a defender ran");
                }
            }
            taken += battle.outcome().winner() == 0 ? 1 : 0;
        }
        assertTrue(taken >= 2, "a four-to-one assault took the castle in only " + taken + " of 3");
    }

    @Test
    void laddersAloneGetOverTheWallAndOpenTheGateFromInside() {
        FieldBattle battle = siege(60, 10, 4, EnumSet.of(Siege.Engine.LADDERS));
        boolean opened = false;
        boolean up = false;
        while (!battle.over()) {
            battle.second();
            opened |= battle.siege().gates().get(0).open();
            up |= battle.siege().works().stream().anyMatch(w -> w.state() == Siege.State.UP);
        }
        assertTrue(up, "no ladder was ever raised");
        assertTrue(opened || battle.outcome().winner() == 0, battle.outcome().result());
    }

    @Test
    void towersRollStraightAtHalfAWalkAndStopAgainstTheWall() {
        FieldBattle battle = siege(40, 10, 3, EnumSet.of(Siege.Engine.TOWERS));
        List<Siege.Work> towers = battle.siege().works().stream().filter(w -> w.kind() == Siege.Engine.TOWERS).toList();
        assertTrue(!towers.isEmpty());
        boolean latched = false;
        for (int s = 0; s < 300 && !battle.over() && !latched; s++) {
            List<Point> was = towers.stream().map(Siege.Work::at).toList();
            battle.second();
            for (int i = 0; i < towers.size(); i++) {
                Point at = towers.get(i).at();
                assertEquals(was.get(i).z(), at.z(), 1e-9, "a tower veered off its line");
                assertTrue(at.x() <= was.get(i).x() && was.get(i).x() - at.x() <= Siege.TOWER_SPEED + 1e-9, "a tower went too fast or back");
                assertTrue(at.x() >= towers.get(i).foot().x(), "a tower rolled into the wall");
                latched |= towers.get(i).state() != Siege.State.CARRIED;
            }
        }
        assertTrue(latched, "no tower reached the wall");
    }

    @Test
    void onlyClimbersReachTheWallFromOutside() {
        FieldBattle battle = siege(20, 20, 2, EnumSet.of(Siege.Engine.LADDERS));
        Siege siege = battle.siege();
        FieldBlock defender = battle.army(1).blocks().stream().filter(b -> siege.region(b.pos()) == Siege.Region.WALL).findFirst().orElseThrow();
        FieldBlock attacker = battle.army(0).blocks().stream().filter(b -> b.arm().infantry()).findFirst().orElseThrow();
        attacker.pos = new Point(23, defender.pos.z()); // at the wall's foot, outside
        assertTrue(!siege.reaches(attacker, defender) && !siege.reaches(defender, attacker));
    }
}
