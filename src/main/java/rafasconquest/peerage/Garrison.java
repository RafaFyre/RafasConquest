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

    static Garrison raise(RandomSource random) {
        Garrison g = new Garrison();
        g.fill(START, random);
        return g;
    }

    /** Adds random cards of the general roster until it holds {@code cards}. */
    void fill(int cards, RandomSource random) {
        BattlePlan.Arm[] roster = java.util.Arrays.stream(BattlePlan.Arm.values()).filter(BattlePlan.Arm::roster).toArray(BattlePlan.Arm[]::new);
        while (units.size() < cards) {
            add(roster[random.nextInt(roster.length)].ordinal());
        }
    }

    /** Musters a full card of {@code arm}. */
    void add(int arm) {
        units.add(arm);
        men.add(MEN);
    }

    void remove(int card) {
        units.remove(card);
        men.remove(card);
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
        t.putIntArray("units", units.stream().mapToInt(Integer::intValue).toArray());
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
        return t;
    }

    static Garrison load(CompoundTag t) {
        Garrison g = new Garrison();
        int[] units = t.getIntArray("units");
        int[] men = t.getIntArray("men");
        for (int i = 0; i < units.length; i++) {
            g.units.add(units[i]);
            g.men.add(men.length == units.length ? men[i] : MEN); // older saves: every card whole
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
        return g;
    }
}
