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
    public double fromX = Double.NaN;
    public double fromZ = Double.NaN;
    public double toX = Double.NaN;
    public double toZ = Double.NaN;
    public long start;

    static Garrison raise(RandomSource random) {
        Garrison g = new Garrison();
        for (int i = 0; i < START; i++) {
            g.units.add(random.nextInt(BattlePlan.Arm.values().length));
        }
        return g;
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
        t.putDouble("fromX", fromX);
        t.putDouble("fromZ", fromZ);
        t.putDouble("toX", toX);
        t.putDouble("toZ", toZ);
        t.putLong("start", start);
        return t;
    }

    static Garrison load(CompoundTag t) {
        Garrison g = new Garrison();
        for (int u : t.getIntArray("units")) {
            g.units.add(u);
        }
        g.fromX = t.getDouble("fromX");
        g.fromZ = t.getDouble("fromZ");
        g.toX = t.getDouble("toX");
        g.toZ = t.getDouble("toZ");
        g.start = t.getLong("start");
        return g;
    }
}
