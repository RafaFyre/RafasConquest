package rafasconquest.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Measures live battles to set {@link FieldRules}: how much health a block loses each second to whoever is fighting it
 * (by the same rules the numeric battle uses to decide who fights whom), how fast blocks walk, and how much health
 * soldiers start with. Plain numbers, filled by the live battle and fitted here.
 * <p>
 * Damage is fitted as one rate per kind of attacker: each second, the health a block lost is set against the soldiers
 * striking it, by kind, each already weighted for where the blow falls (flank, rear, shieldwall) as the numeric
 * battle weights it. A least-squares fit of that gives the damage a second of one soldier of each kind.
 */
public final class Calibration {
    /** The kinds of attacker a rate is fitted for. */
    public enum Striker {
        INFANTRY, CAVALRY, ARCHER_MELEE, ARROWS
    }

    private static final int K = Striker.values().length;

    /** The normal equations, accumulated: X^T X and X^T y. */
    private final double[][] xtx = new double[K][K];
    private final double[] xty = new double[K];
    private final long[] rowsWith = new long[K];
    private long rows;
    private final List<Double> footSpeeds = new ArrayList<>();
    private final List<Double> horseSpeeds = new ArrayList<>();
    private double healthSum;
    private long soldiers;

    /** One block for one second: the health it lost, and the weighted soldiers striking it, by {@link Striker}. */
    public void addDamage(double healthLost, double[] strikers) {
        rows++;
        for (int i = 0; i < K; i++) {
            if (strikers[i] > 0) {
                rowsWith[i]++;
            }
            xty[i] += strikers[i] * healthLost;
            for (int j = 0; j < K; j++) {
                xtx[i][j] += strikers[i] * strikers[j];
            }
        }
    }

    /** How far a block walking to a far spot covered in one second. */
    public void addSpeed(boolean mounted, double blocksPerSecond) {
        (mounted ? horseSpeeds : footSpeeds).add(blocksPerSecond);
    }

    /** A soldier's health when the battle began. */
    public void addSoldier(double health) {
        healthSum += health;
        soldiers++;
    }

    /** Adds everything another calibration measured. */
    public void addAll(Calibration other) {
        rows += other.rows;
        for (int i = 0; i < K; i++) {
            xty[i] += other.xty[i];
            rowsWith[i] += other.rowsWith[i];
            for (int j = 0; j < K; j++) {
                xtx[i][j] += other.xtx[i][j];
            }
        }
        footSpeeds.addAll(other.footSpeeds);
        horseSpeeds.addAll(other.horseSpeeds);
        healthSum += other.healthSum;
        soldiers += other.soldiers;
    }

    /**
     * What was measured: damage a second of one soldier of each kind (NaN where too few seconds were seen), the
     * median walking speeds, and the average starting health.
     */
    public record Fit(double[] dps, long[] seconds, long rows, double footSpeed, double horseSpeed, double health) {
        public String describe() {
            StringBuilder sb = new StringBuilder();
            for (Striker s : Striker.values()) {
                sb.append(String.format(Locale.ROOT, "%s %.3f (%d s), ", s.name().toLowerCase(Locale.ROOT).replace('_', ' '),
                        dps[s.ordinal()], seconds[s.ordinal()]));
            }
            return sb.append(String.format(Locale.ROOT, "damage a second each over %d block-seconds; walking %.1f, riding %.1f blocks a second; health %.1f",
                    rows, footSpeed, horseSpeed, health)).toString();
        }
    }

    /** Fewer seconds than this of a kind of attacker, and its rate isn't fitted. */
    public static final int MIN_SECONDS = 20;

    public Fit fit() {
        // Fit only the kinds seen often enough; the others are left out of the equations.
        List<Integer> used = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            if (rowsWith[i] >= MIN_SECONDS) {
                used.add(i);
            }
        }
        double[] dps = new double[K];
        Arrays.fill(dps, Double.NaN);
        int n = used.size();
        if (n > 0) {
            double[][] a = new double[n][n + 1];
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    a[r][c] = xtx[used.get(r)][used.get(c)];
                }
                a[r][n] = xty[used.get(r)];
            }
            double[] solved = solve(a);
            for (int r = 0; r < n; r++) {
                dps[used.get(r)] = solved[r];
            }
        }
        return new Fit(dps, rowsWith.clone(), rows, median(footSpeeds), median(horseSpeeds),
                soldiers == 0 ? Double.NaN : healthSum / soldiers);
    }

    /** Solves an augmented system by Gaussian elimination with partial pivoting; NaN where it is singular. */
    static double[] solve(double[][] a) {
        int n = a.length;
        for (int col = 0; col < n; col++) {
            int pivot = col;
            for (int r = col + 1; r < n; r++) {
                if (Math.abs(a[r][col]) > Math.abs(a[pivot][col])) {
                    pivot = r;
                }
            }
            double[] t = a[col];
            a[col] = a[pivot];
            a[pivot] = t;
            if (Math.abs(a[col][col]) < 1e-12) {
                double[] nan = new double[n];
                Arrays.fill(nan, Double.NaN);
                return nan;
            }
            for (int r = 0; r < n; r++) {
                if (r != col) {
                    double f = a[r][col] / a[col][col];
                    for (int c = col; c <= n; c++) {
                        a[r][c] -= f * a[col][c];
                    }
                }
            }
        }
        double[] x = new double[n];
        for (int r = 0; r < n; r++) {
            x[r] = a[r][n] / a[r][r];
        }
        return x;
    }

    static double median(List<Double> values) {
        if (values.isEmpty()) {
            return Double.NaN;
        }
        List<Double> sorted = new ArrayList<>(values);
        sorted.sort(null);
        return sorted.get(sorted.size() / 2);
    }
}
