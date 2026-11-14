package rafasconquest.battle;

import org.junit.jupiter.api.Test;
import rafasconquest.battle.BattlePlan.Arm;
import rafasconquest.battle.BattlePlan.Point;
import rafasconquest.battle.FieldBlock.Duty;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SiegeTest {
    /** A castle 41 square about (0, 0), walls 5 thick, one gate on +x, attacked from +x. */
    private static Siege.Plan castle(Set<Siege.Engine> engines) {
        Siege.Gate gate = new Siege.Gate(new Point(21.5, 0), new Point(15.5, 3.5), 1, 0);
        return new Siege.Plan(-20, -20, 21, 21, 5, 27, 1, 0, List.of(gate), new Point(0.5, 0.5), engines);
    }

    /** A siege, the attackers' train bringing towers and rams as the strategems say (and nothing else), the defenders' tower crossbows. */
    private static FieldBattle siege(int aBlocks, int dBlocks, long seed, Set<Siege.Engine> engines) {
        List<Machine> train = new java.util.ArrayList<>();
        if (engines.contains(Siege.Engine.TOWERS)) {
            train.add(Machine.SIEGE_TOWER);
        }
        if (engines.contains(Siege.Engine.RAMS)) {
            train.add(Machine.BATTERING_RAM);
        }
        return siege(aBlocks, dBlocks, seed, engines, train);
    }

    private static FieldBattle siege(int aBlocks, int dBlocks, long seed, Set<Siege.Engine> engines, List<Machine> train) {
        Siege.Plan plan = castle(engines);
        double[] axis = plan.axis();
        boolean was = BattlePlan.siegeworks;
        BattlePlan.siegeworks = true; // every army brings its 5% train (besieging lays the attackers out again)
        try {
            FieldBattle battle = new FieldBattle(aBlocks, dBlocks, plan.battleCentre(), axis[0], axis[1], seed);
            battle.setType(BattleType.SIEGE);
            battle.equip(0, train);
            battle.equip(1, List.of(Machine.TOWER_CROSSBOW));
            battle.besiege(plan);
            return battle;
        } finally {
            BattlePlan.siegeworks = was;
        }
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
            riders += b.arm().mounted() && b.duty() == Duty.SUPPORT ? 1 : 0;
        }
        assertEquals(8, onWall, "a block of archers and one of foot on each of a castle's four walls");
        assertTrue(inside > onWall, "most wait inside");
        assertEquals(Math.round((superHeavy + riders) / 10.0), riders, superHeavy + " dismounted against " + riders + " riding");
        assertTrue(superHeavy > 0);
        long works = siege.works().size();
        assertTrue(works > 0 && works <= Siege.LADDERS + 2, "ladders, and a tower or ram for each of the 2 train blocks");
    }

    @Test
    void attackersSurroundTheWallsByTheirShares() {
        // 12,000 soldiers, attacked from +x: 35% there, 22.5% at each wall beside it, 20% at the far wall.
        FieldBattle battle = siege(600, 40, 8, EnumSet.allOf(Siege.Engine.class));
        Siege siege = battle.siege();
        assertFalse(battle.army(0).winged(), "a siege's attackers deployed in wings");
        List<Siege.Front> fronts = siege.fronts();
        assertEquals(4, fronts.size());
        assertTrue(fronts.get(0).main() && fronts.get(0).outX() == 1, "the main attack first");
        int all = fronts.stream().mapToInt(f -> f.blocks().size()).sum();
        for (Siege.Front f : fronts) {
            double expected = f.main() ? Siege.MAIN_SHARE : f.outX() == -1 ? Siege.FAR_SHARE : Siege.SIDE_SHARE;
            assertEquals(expected, f.blocks().size() / (double) all, 0.02, "the wall out " + f.outX() + "," + f.outZ());
            // Each wall's force stands out before its own wall, facing it; its engines too.
            for (FieldBlock b : f.blocks()) {
                double out = (b.pos().x() - 0.5) * f.outX() + (b.pos().z() - 0.5) * f.outZ();
                assertTrue(out > 20.5 + Siege.CLEAR, b.arm() + " of the wall out " + f.outX() + "," + f.outZ() + " stands at " + b.pos());
            }
            long engines = f.blocks().stream().filter(b -> b.arm() == Arm.ENGINES).count();
            assertTrue(engines > 0, "no siege train at the wall out " + f.outX() + "," + f.outZ());
        }
        assertTrue(siege.works().stream().anyMatch(w -> w.outX() == 0), "no strategems brought against the walls beside the main one");
    }

    @Test
    void aWallTheyCantComeAtSharesItsForceOut() {
        // The south wall (+z) has water before it: its 22.5% goes to the other three by their shares.
        Siege.Plan plan = new Siege.Plan(-20, -20, 21, 21, 5, 27, 1, 0, List.of(), new Point(0.5, 0.5),
                EnumSet.allOf(Siege.Engine.class), List.of(), List.of(new Point(0, 1)));
        Siege siege = new Siege(plan);
        assertEquals(3, siege.fronts().size());
        assertTrue(siege.fronts().stream().noneMatch(f -> f.outZ() == 1), "attackers sent to the barred wall");
        double rest = Siege.MAIN_SHARE + Siege.SIDE_SHARE + Siege.FAR_SHARE;
        assertEquals(Siege.MAIN_SHARE / rest, siege.fronts().get(0).share(), 1e-9);
        assertEquals(1, siege.fronts().stream().mapToDouble(Siege.Front::share).sum(), 1e-9);
    }

    @Test
    void defendersFillTheWallsAndWaitInsideNearThem() {
        FieldBattle battle = siege(40, 120, 9, EnumSet.allOf(Siege.Engine.class));
        Siege siege = battle.siege();
        double nearest = Double.MAX_VALUE;
        for (FieldBlock b : battle.army(1).blocks()) {
            assertTrue(siege.region(b.pos()) != Siege.Region.OUT, "a defender deployed outside the walls at " + b.pos());
            if (b.duty() == Duty.LINE) {
                assertEquals(Siege.Region.IN, siege.region(b.pos()), "a reserve stands in the wall at " + b.pos());
                nearest = Math.min(nearest, siege.plan.maxX() - b.pos().x()); // attacked from +x
            }
        }
        assertEquals(siege.plan.thickness() + Siege.RESERVE_IN, nearest, 1e-9, "the first reserves wait right inside the attacked wall");
    }

    @Test
    void siegeTowersComeAtTheWallBetweenItsTowers() {
        // A wall tower across the attacked wall's middle, where the towers would otherwise go.
        Siege.Plan plan = new Siege.Plan(-20, -20, 21, 21, 5, 27, 1, 0, List.of(), new Point(0.5, 0.5),
                EnumSet.of(Siege.Engine.TOWERS), List.of(new Siege.WallTower(new Point(21, -4), new Point(21, 4))));
        boolean was = BattlePlan.siegeworks;
        BattlePlan.siegeworks = true;
        FieldBattle battle;
        try {
            battle = new FieldBattle(60, 10, plan.battleCentre(), plan.axis()[0], plan.axis()[1], 10);
            battle.setType(BattleType.SIEGE);
            battle.equip(0, List.of(Machine.SIEGE_TOWER));
            battle.besiege(plan);
        } finally {
            BattlePlan.siegeworks = was;
        }
        List<Siege.Work> towers = battle.siege().works().stream().filter(w -> w.kind() == Siege.Engine.TOWERS && w.outX() == 1).toList();
        assertTrue(!towers.isEmpty(), "no tower at the main wall");
        for (Siege.Work w : towers) {
            assertTrue(Math.abs(w.foot().z()) >= 4 + Siege.TOWER_HALF + Siege.TOWER_CLEAR - 1e-9, "a siege tower came at the wall tower, at " + w.foot());
            assertTrue(Math.abs(w.foot().z()) <= 27 / 2.0, "a siege tower missed the wall, at " + w.foot());
        }
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
    void towersStandWhereTheirRampComesDownFlattest() {
        // Walkway level with the ramp's hinge: flat anywhere, so the usual 3-block gap.
        assertEquals(Siege.TOWER_OUT, Siege.rampStandOff(70 + Siege.RAMP_HINGE_UP, out -> 70), 1e-9);
        // Walkway 3 below the hinge: as far out as the ramp still reaches onto it, where it lies flattest.
        double out = Siege.rampStandOff(67 + Siege.RAMP_HINGE_UP, d -> 70);
        double run = out - Siege.RAMP_HINGE_OUT;
        assertTrue(out > Siege.TOWER_OUT, "stayed at " + out);
        assertTrue(Math.hypot(run, 3) <= Siege.RAMP_LENGTH - Siege.RAMP_LAND, "out of reach at " + out);
        assertTrue(Math.hypot(run + Siege.RAMP_STEP, 3) > Siege.RAMP_LENGTH - Siege.RAMP_LAND, "could stand further out than " + out);
        // Ground rising toward the wall: the spot where the hinge meets the walkway is level, so it stops there.
        assertEquals(7, Siege.rampStandOff(80 + Siege.RAMP_HINGE_UP, d -> d <= 7 ? 80 : 70), 1e-9);
        // A walkway far over the ramp's reach: as close as it can get.
        assertEquals(Siege.TOWER_HALF_LENGTH, Siege.rampStandOff(200, d -> 70), 1e-9);
    }

    @Test
    void oneRamToAGateAndAtMostFourTowersToAWall() {
        FieldBattle battle = siege(600, 40, 8, EnumSet.of(Siege.Engine.TOWERS, Siege.Engine.RAMS));
        List<Siege.Work> works = battle.siege().works();
        assertEquals(1, works.stream().filter(w -> w.kind() == Siege.Engine.RAMS).count(), "the castle has one gate");
        var perWall = works.stream().filter(w -> w.kind() == Siege.Engine.TOWERS)
                .collect(java.util.stream.Collectors.groupingBy(w -> w.front, java.util.stream.Collectors.counting()));
        assertFalse(perWall.isEmpty());
        perWall.values().forEach(n -> assertTrue(n <= Siege.TOWERS_PER_WALL, n + " towers at one wall"));
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
                // Along its own wall's way in: straight on (none sideways), no faster than its pace, never past its foot.
                Siege.Work w = towers.get(i);
                double inX = -w.outX();
                double inZ = -w.outZ();
                double dx = w.at().x() - was.get(i).x();
                double dz = w.at().z() - was.get(i).z();
                assertEquals(0, dx * inZ - dz * inX, 1e-9, "a tower veered off its line");
                double step = dx * inX + dz * inZ;
                assertTrue(step >= -1e-9 && step <= Siege.TOWER_SPEED + 1e-9, "a tower went too fast or back");
                double left = (w.foot().x() - w.at().x()) * inX + (w.foot().z() - w.at().z()) * inZ;
                assertTrue(left >= -1e-9, "a tower rolled into the wall");
                latched |= w.state() != Siege.State.CARRIED;
            }
        }
        assertTrue(latched, "no tower reached the wall");
    }

    @Test
    void trebuchetsBreakTheWallOpenAndTheAttackersGoInThroughIt() {
        // No ladders and no ram: only the breach lets them in.
        FieldBattle battle = siege(60, 10, 5, EnumSet.of(Siege.Engine.TOWERS), List.of(Machine.TREBUCHET));
        Siege siege = battle.siege();
        assertTrue(siege.works().isEmpty(), "nothing to climb");
        boolean breached = false;
        boolean inside = false;
        for (int s = 0; s < BattlePlan.TIME_LIMIT_SECONDS && !battle.over(); s++) {
            battle.second();
            breached |= siege.breach() != null;
            inside |= battle.army(0).blocks().stream().anyMatch(b -> b.alive() > 0 && siege.region(b.pos()) == Siege.Region.IN);
        }
        assertTrue(breached, "the trebuchets never broke the wall: " + (battle.outcome() == null ? "no end" : battle.outcome().result() + " at " + battle.outcome().seconds()) + ", train " + battle.army(0).blocks().stream().filter(b -> b.arm() == Arm.ENGINES).map(b -> b.machine() + " " + b.arrows + " at " + Math.round(b.pos().distanceTo(siege.battered()))).toList());
        assertTrue(inside, "nobody went in through the breach");
        assertEquals(0, battle.outcome().winner(), battle.outcome().result());
    }

    @Test
    void eachWallsArtilleryBreaksItsOwnWallOnce() {
        FieldBattle battle = siege(200, 10, 11, EnumSet.of(Siege.Engine.TOWERS), List.of(Machine.TREBUCHET));
        Siege siege = battle.siege();
        Siege.Front side = siege.fronts().stream().filter(f -> !f.main() && f.outX() == 0).findFirst().orElseThrow();
        FieldBlock trebuchet = side.blocks().stream().filter(b -> b.machine() == Machine.TREBUCHET).findFirst().orElseThrow();
        siege.batter(trebuchet, Siege.WALL_HP);
        assertTrue(side.breached(), "the wall beside the main one held");
        assertFalse(siege.fronts().get(0).breached(), "the main wall broke for another wall's artillery");
        Siege.Gate breach = siege.breaches().get(0);
        assertEquals(side.outZ(), breach.outZ(), 1e-9, "the breach opened in another wall");
        siege.batter(trebuchet, Siege.WALL_HP);
        assertEquals(1, siege.breaches().size(), "a wall broke open twice");
    }

    @Test
    void mantletsScreenTheShootersFromTheWallsArrows() {
        FieldBattle battle = siege(60, 20, 6, EnumSet.of(Siege.Engine.LADDERS), List.of(Machine.MANTLET));
        Siege siege = battle.siege();
        FieldBlock archers = battle.army(0).blocks().stream().filter(b -> b.arm() == Arm.ARCHERS).findFirst().orElseThrow();
        for (int s = 0; s < 30; s++) {
            battle.second();
        }
        assertEquals(Siege.MANTLET_SHIELDS, siege.arrowFactor(archers), 1e-9, "the archers walk behind a mantlet");
        FieldBlock defender = battle.army(1).blocks().stream().filter(b -> b.arm() == Arm.ARCHERS).findFirst().orElseThrow();
        assertEquals(FieldBattle.WALL_TAKES, siege.arrowFactor(defender), 1e-9, "mantlets screen only their own side");
    }

    @Test
    void nobodyWalksOverTheWallWithoutALadderTowerOrOpening() {
        FieldBattle battle = siege(60, 10, 7, EnumSet.of(Siege.Engine.LADDERS));
        Siege siege = battle.siege();
        FieldBlock defender = battle.army(1).blocks().stream().filter(b -> siege.region(b.pos()) == Siege.Region.IN).findFirst().orElseThrow();
        FieldBlock cavalry = battle.army(0).blocks().stream().filter(b -> b.arm().mounted()).findFirst().orElseThrow();
        Point outside = new Point(24, 0);
        cavalry.pos = outside;
        assertEquals(outside, siege.fence(cavalry, defender.pos()), "over a shut wall");
        FieldBlock climber = siege.works().get(0).crews().get(0);
        climber.pos = siege.works().get(0).foot();
        assertEquals(climber.pos(), siege.fence(climber, siege.works().get(0).top()), "up a ladder not yet raised");
        // Every attacker that is only numbers stays out until a way in opens.
        for (int s = 0; s < 600 && !battle.over(); s++) {
            boolean way = siege.gates().stream().anyMatch(Siege.GateState::breached)
                    || siege.works().stream().anyMatch(w -> w.state() == Siege.State.UP);
            battle.second();
            if (!way) {
                for (FieldBlock b : battle.army(0).blocks()) {
                    assertTrue(b.alive() == 0 || siege.region(b.pos()) == Siege.Region.OUT, "an attacker crossed the wall with no way in");
                }
            }
        }
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

    @Test
    void aHorseOnTheWallIsPushedOffToTheNearerSide() {
        Siege siege = new Siege(castle(EnumSet.noneOf(Siege.Engine.class))); // east wall from x 16 to 21
        assertEquals(Siege.Region.WALL, siege.region(new Point(19, 0)));
        assertEquals(Siege.Region.OUT, siege.region(siege.offWall(new Point(19, 0))));
        assertEquals(Siege.Region.IN, siege.region(siege.offWall(new Point(17, 0))));
    }
}
