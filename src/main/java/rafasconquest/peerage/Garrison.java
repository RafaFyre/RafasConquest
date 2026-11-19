package rafasconquest.peerage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import rafasconquest.battle.BattlePlan;
import rafasconquest.core.DayClock;

import java.util.ArrayList;
import java.util.List;

/**
 * A city's army on the grand strategy map: its unit cards (each a {@link BattlePlan.Arm} of {@link #MEN} soldiers), and
 * its march: from where to where, set off when (game ticks). Home in its city until first marched ({@code toX} NaN).
 */
public final class Garrison {
    /** Every city starts with this many cards, picked at random: 2,000 soldiers. */
    public static final int START = 10;
    public static final int MEN = 200;
    /** Blocks a tick: the wars' armies' pace, {@code Politics.MARCH} blocks a day. */
    public static final double PACE = 20000.0 / DayClock.TICKS_PER_DAY;

    public final List<Integer> units = new ArrayList<>();
    /** Each card's soldiers left, alongside {@link #units}: {@link #MEN} when mustered, fewer after a battle. */
    public final List<Integer> men = new ArrayList<>();
    public double fromX = Double.NaN;
    public double fromZ = Double.NaN;
    public double toX = Double.NaN;
    public double toZ = Double.NaN;
    public long start;
    /**
     * A spawned army's nation (index; -1: the hold's army, of its holder's nation), its general's name ("": the best at
     * war), how many cards it may hold (0: no limit), and the army it marches to attack ({@link GrandStrategy#NO_TARGET}: none).
     */
    public int nation = -1;
    public String general = "";
    public int cap;
    public int target = GrandStrategy.NO_TARGET;
    /** A fleet (ship cards only, on water) rather than an army (land cards only, on land): two kinds of force that never mix. */
    public boolean fleet;

    static Garrison raise(RandomSource random) {
        Garrison g = new Garrison();
        g.fill(START, random);
        return g;
    }

    /** A hold's fleet, raised at random the first time it is asked for, in harbour off its port at (x, z). */
    static Garrison raiseFleet(RandomSource random, double x, double z) {
        Garrison g = new Garrison();
        g.fleet = true;
        g.fill(FLEET_START, random);
        g.fromX = g.toX = x;
        g.fromZ = g.toZ = z;
        return g;
    }

    /** Every port's fleet starts with this many ship cards. */
    public static final int FLEET_START = 3;

    /** Whether a card may serve in this force: ship cards in a fleet, the rest in an army. */
    public boolean takes(BattlePlan.Arm arm) {
        return arm.roster() && arm.naval() == fleet;
    }

    /** Adds random cards of its kind's roster until it holds {@code cards}. */
    void fill(int cards, RandomSource random) {
        BattlePlan.Arm[] roster = java.util.Arrays.stream(BattlePlan.Arm.values()).filter(this::takes).toArray(BattlePlan.Arm[]::new);
        while (roster.length > 0 && units.size() < cards) {
            add(roster[random.nextInt(roster.length)].ordinal());
        }
    }

    /** Musters a full card of {@code arm}. */
    void add(int arm) {
        units.add(arm);
        men.add(full(arm));
    }

    /** A whole card's soldiers: {@link #MEN}, or for a ship's card only what it holds: a boarder's deck, a gunship's gun posts. */
    public static int full(int arm) {
        BattlePlan.Arm a = BattlePlan.Arm.get(arm);
        rafasconquest.core.SeaLanes.Size ship = a == null ? null : a.ship();
        return ship == null ? MEN : ship.gunship() ? ship.crewCap(MEN) : Math.min(MEN, rafasconquest.battle.FieldBattle.deckCrew(ship));
    }

    void remove(int card) {
        units.remove(card);
        men.remove(card);
    }

    /** Its cards as blocks of soldiers per arm (by ordinal), the mix it fights a battle with. */
    public int[] mix() {
        int[] men = new int[BattlePlan.Arm.values().length];
        for (int i = 0; i < units.size(); i++) {
            men[units.get(i)] += this.men.get(i);
        }
        int[] blocks = new int[men.length];
        for (int a = 0; a < men.length; a++) {
            blocks[a] = Math.round(men[a] / (float) BattlePlan.SOLDIERS_PER_FORMATION);
        }
        return java.util.Arrays.stream(blocks).sum() > 0 ? blocks : null; // too few for a block: the usual shares

    }

    public int soldiers() {
        return men.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * A battle's losses: of each arm's soldiers, the share {@code kept[arm]} lives on (an arm the battle didn't field,
     * share below 0, keeps {@code otherwise}), filling its cards first to last; a card left with none is gone.
     */
    void survive(double[] kept, double otherwise) {
        int[] left = new int[BattlePlan.Arm.values().length];
        for (int i = 0; i < units.size(); i++) {
            left[units.get(i)] += men.get(i);
        }
        for (int a = 0; a < left.length; a++) {
            double share = a < kept.length && kept[a] >= 0 ? kept[a] : otherwise;
            left[a] = (int) Math.round(left[a] * Math.max(0, Math.min(1, share)));
        }
        for (int i = 0; i < units.size(); i++) {
            int a = units.get(i);
            int n = Math.min(men.get(i), left[a]);
            men.set(i, n);
            left[a] -= n;
        }
        for (int i = units.size() - 1; i >= 0; i--) {
            if (men.get(i) == 0) {
                remove(i);
            }
        }
    }

    public boolean home() {
        return Double.isNaN(toX);
    }

    /** Where it is at game tick {@code now} (x, z), walking straight from its start; null while home. */
    public static double[] at(double fromX, double fromZ, double toX, double toZ, long start, double now) {
        if (Double.isNaN(toX)) {
            return null;
        }
        double d = Math.hypot(toX - fromX, toZ - fromZ);
        double t = d < 1 ? 1 : Math.min(1, Math.max(0, now - start) * PACE / d);
        return new double[]{fromX + (toX - fromX) * t, fromZ + (toZ - fromZ) * t};
    }

    CompoundTag save() {
        CompoundTag t = new CompoundTag();
        // Cards by id, so adding or removing card files never turns one card into another.
        net.minecraft.nbt.ListTag ids = new net.minecraft.nbt.ListTag();
        for (int u : units) {
            BattlePlan.Arm a = BattlePlan.Arm.get(u);
            ids.add(net.minecraft.nbt.StringTag.valueOf(a == null ? "" : a.id));
        }
        t.put("cards", ids);
        t.putIntArray("men", men.stream().mapToInt(Integer::intValue).toArray());
        t.putDouble("fromX", fromX);
        t.putDouble("fromZ", fromZ);
        t.putDouble("toX", toX);
        t.putDouble("toZ", toZ);
        t.putLong("start", start);
        t.putInt("nation", nation);
        t.putString("general", general);
        t.putInt("cap", cap);
        t.putInt("target", target);
        t.putBoolean("fleet", fleet);
        return t;
    }

    /** Takes out the cards not of its kind (older saves mixed ships and land units): each [card, soldiers]. */
    List<int[]> strip() {
        List<int[]> out = new ArrayList<>();
        for (int i = units.size() - 1; i >= 0; i--) {
            BattlePlan.Arm a = BattlePlan.Arm.get(units.get(i));
            if (a != null && a.naval() != fleet) {
                out.add(0, new int[]{units.get(i), men.get(i)});
                remove(i);
            }
        }
        return out;
    }

    static Garrison load(CompoundTag t) {
        Garrison g = new Garrison();
        int[] men = t.getIntArray("men");
        if (t.contains("cards")) {
            net.minecraft.nbt.ListTag ids = t.getList("cards", net.minecraft.nbt.Tag.TAG_STRING);
            for (int i = 0; i < ids.size(); i++) {
                BattlePlan.Arm a = BattlePlan.Arm.byId(ids.getString(i));
                if (a == null) {
                    rafasconquest.RafasConquest.LOGGER.warn("Grand strategy: a city army's card {} has no file any more; it is dropped", ids.getString(i));
                    continue;
                }
                g.units.add(a.ordinal());
                g.men.add(men.length == ids.size() ? men[i] : MEN);
            }
        } else { // older saves: the built-in cards by ordinal, which they keep
            int[] units = t.getIntArray("units");
            for (int i = 0; i < units.length; i++) {
                g.units.add(units[i]);
                g.men.add(men.length == units.length ? men[i] : MEN); // older saves still: every card whole
            }
        }
        g.fromX = t.getDouble("fromX");
        g.fromZ = t.getDouble("fromZ");
        g.toX = t.getDouble("toX");
        g.toZ = t.getDouble("toZ");
        g.start = t.getLong("start");
        g.nation = t.contains("nation") ? t.getInt("nation") : -1; // older saves: hold armies only
        g.general = t.getString("general");
        g.cap = t.getInt("cap");
        g.target = t.contains("target") ? t.getInt("target") : GrandStrategy.NO_TARGET;
        // Older saves: a force of ship cards alone was a fleet in all but name.
        g.fleet = t.contains("fleet") ? t.getBoolean("fleet")
                : !g.units.isEmpty() && g.units.stream().allMatch(u -> BattlePlan.Arm.get(u) != null && BattlePlan.Arm.get(u).naval());
        return g;
    }
}
