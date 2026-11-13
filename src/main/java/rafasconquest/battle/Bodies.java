package rafasconquest.battle;

import java.util.Arrays;

/**
 * Every soldier of a watched battle as a body, the way Total War fights: each has a size and a weight, walks to its
 * goal (its place in the ranks) and fights the nearest enemy in reach. Enemies are solid and friends give way, so a
 * line forms where two blocks meet, the heavier side pushes it back, only soldiers who touch the enemy fight, and a
 * fast rider knocks a foot soldier down and is slowed by each one it hits.
 * <p>
 * Plain arrays and no Minecraft, so it can run off the server thread and be measured in a test. Coordinates are
 * blocks from the battle's origin. Each step: everyone steers and moves, overlaps are pushed apart (each body moves
 * only itself, from the positions all moved to), then blows land. One thread steps it.
 */
public final class Bodies {
    /** One step, seconds: a game tick. */
    public static final float STEP = 0.05f;
    /** The grid's cells, blocks: wider than the furthest two bodies reach each other, so 3 by 3 cells find them all. */
    static final float CELL = 3;
    /** How much of an overlap with a friend is pushed out each step: friends squeeze past each other. */
    static final float FRIEND_GIVE = 0.3f;
    /** Closing faster than this, blocks a second, a body hitting an enemy is a charge. */
    static final float CHARGE_SPEED = 3;
    /** A charge knocks its target down when its weight times its closing speed is this many times the target's weight. */
    static final float KNOCK = 4; // two men running head-on (closing at 9) stay up; a galloping horse (77) never does
    static final float DOWN_SECONDS = 2;
    /** Within this of an enemy (beyond touching) a soldier goes for it rather than its place. */
    static final float ENGAGE = 1.2f;
    /** Going for a foe, a soldier strays at most this far from its place in the ranks. */
    static final float LEASH = 1;

    /** What a body is: its size and weight from Minecraft's hitboxes (a recruit 0.6 wide, a horse 1.4), pace and blows. */
    public enum Kind {
        FOOT(0.3f, 1, 4.5f, 10, 0.9f, 3, 20, 0.625f),
        HORSE(0.7f, 7, 11, 3, 1.1f, 4, 30, 0.8f);

        final float radius;
        final float mass;
        /** Blocks a second, and how fast it gets there (a share of the difference a second): a horse has inertia. */
        final float speed;
        final float accel;
        /** How far past touching its weapon reaches. */
        final float reach;
        final float damage;
        final float health;
        /** Seconds between blows: a sword's swing. */
        final float interval;

        Kind(float radius, float mass, float speed, float accel, float reach, float damage, float health, float interval) {
            this.radius = radius;
            this.mass = mass;
            this.speed = speed;
            this.accel = accel;
            this.reach = reach;
            this.damage = damage;
            this.health = health;
            this.interval = interval;
        }
    }

    private int n;
    private Kind[] kind = new Kind[0];
    private byte[] side = new byte[0];
    private boolean[] dead = new boolean[0];
    private float[] x = new float[0], z = new float[0], oldX = new float[0], oldZ = new float[0];
    private float[] vx = new float[0], vz = new float[0], fx = new float[0], fz = new float[0];
    private float[] goalX = new float[0], goalZ = new float[0], frontX = new float[0], frontZ = new float[0];
    private float[] health = new float[0], hurt = new float[0], cooldown = new float[0], down = new float[0];
    private float[] pushX = new float[0], pushZ = new float[0], kickX = new float[0], kickZ = new float[0], slow = new float[0];
    private int[] target = new int[0];
    // The grid: soldiers sorted by cell (counting sort, rebuilt each step).
    private int[] cellOf = new int[0], order = new int[0], cellStart = new int[1];
    private int cols;
    private int rows;
    private float minX;
    private float minZ;
    private final int[] fallen = new int[2];
    /** Blows each soldier swung, and took, since {@link #clearCounts}: a battle that keeps health itself reads these. */
    private int[] swings = new int[0], struck = new int[0];
    /** Whether blows take health here; off when the battle deals the damage and only asks who fought whom. */
    private boolean lethal = true;

    public void lethal(boolean lethal) {
        this.lethal = lethal;
    }

    /** Takes soldier {@code i} out (it fell in the battle's own reckoning, or became a real soldier), not as fallen here. */
    public void remove(int i) {
        dead[i] = true;
    }

    public int swings(int i) {
        return swings[i];
    }

    public int struck(int i) {
        return struck[i];
    }

    /** Whom soldier {@code i} last went for, or -1. */
    public int target(int i) {
        return target[i];
    }

    public void clearCounts() {
        Arrays.fill(swings, 0, n, 0);
        Arrays.fill(struck, 0, n, 0);
    }

    /** Adds a soldier of {@code side} (0 or 1) standing at ({@code x}, {@code z}), facing its block's front; returns its index. */
    public int add(int side, Kind kind, float x, float z, float frontX, float frontZ) {
        if (n == this.x.length) {
            grow(Math.max(64, n * 2));
        }
        int i = n++;
        this.kind[i] = kind;
        this.side[i] = (byte) side;
        this.x[i] = x;
        this.z[i] = z;
        goalX[i] = x;
        goalZ[i] = z;
        this.frontX[i] = fx[i] = frontX;
        this.frontZ[i] = fz[i] = frontZ;
        health[i] = kind.health;
        target[i] = -1;
        return i;
    }

    /** Soldier {@code i} makes for ({@code x}, {@code z}), its place in the ranks, and stands facing ({@code frontX}, {@code frontZ}). */
    public void goal(int i, float x, float z, float frontX, float frontZ) {
        goalX[i] = x;
        goalZ[i] = z;
        this.frontX[i] = frontX;
        this.frontZ[i] = frontZ;
    }

    /** Sets soldier {@code i} moving at ({@code vx}, {@code vz}) blocks a second, as a rider already at the gallop. */
    public void velocity(int i, float vx, float vz) {
        this.vx[i] = vx;
        this.vz[i] = vz;
    }

    public void step() {
        for (int i = 0; i < n; i++) {
            if (!dead[i]) {
                steer(i);
            }
        }
        grid();
        for (int i = 0; i < n; i++) {
            if (!dead[i]) {
                touch(i);
            }
        }
        for (int i = 0; i < n; i++) {
            if (!dead[i]) {
                settle(i);
            }
        }
        for (int i = 0; i < n; i++) {
            if (!dead[i]) {
                strike(i);
            }
        }
        for (int i = 0; i < n; i++) {
            if (!dead[i] && hurt[i] > 0) {
                health[i] -= hurt[i];
                hurt[i] = 0;
                if (health[i] <= 0) {
                    dead[i] = true;
                    fallen[side[i]]++;
                }
            }
        }
    }

    /** Toward its foe if one is close, else its place; a downed soldier lies still. Moves it there for the step. */
    private void steer(int i) {
        Kind k = kind[i];
        float wantX = 0;
        float wantZ = 0;
        if (down[i] > 0) {
            down[i] -= STEP;
        } else {
            int t = target[i];
            float tx = goalX[i];
            float tz = goalZ[i];
            if (t >= 0) { // at its foe, but no further than LEASH from its place: the ranks hold, as Total War's do
                float lx = x[t] - tx;
                float lz = z[t] - tz;
                float l = (float) Math.sqrt(lx * lx + lz * lz);
                float s = l > LEASH ? LEASH / l : 1;
                tx += lx * s;
                tz += lz * s;
            }
            float dx = tx - x[i];
            float dz = tz - z[i];
            float d = (float) Math.sqrt(dx * dx + dz * dz);
            if (d > 1e-4f) {
                float pace = Math.min(k.speed, d / 0.3f); // eases to a stop at its place
                wantX = dx / d * pace;
                wantZ = dz / d * pace;
            }
        }
        float ease = Math.min(1, k.accel * STEP);
        vx[i] += (wantX - vx[i]) * ease;
        vz[i] += (wantZ - vz[i]) * ease;
        oldX[i] = x[i];
        oldZ[i] = z[i];
        x[i] += vx[i] * STEP;
        z[i] += vz[i] * STEP;
        slow[i] = 1;
    }

    private void grid() {
        minX = Float.MAX_VALUE;
        minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE;
        float maxZ = -Float.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (!dead[i]) {
                minX = Math.min(minX, x[i]);
                minZ = Math.min(minZ, z[i]);
                maxX = Math.max(maxX, x[i]);
                maxZ = Math.max(maxZ, z[i]);
            }
        }
        if (minX > maxX) {
            cols = rows = 0;
            return;
        }
        cols = (int) ((maxX - minX) / CELL) + 1;
        rows = (int) ((maxZ - minZ) / CELL) + 1;
        int cells = cols * rows;
        if (cellStart.length < cells + 1) {
            cellStart = new int[cells + 1];
        } else {
            Arrays.fill(cellStart, 0, cells + 1, 0);
        }
        for (int i = 0; i < n; i++) {
            if (!dead[i]) {
                int c = (int) ((x[i] - minX) / CELL) + (int) ((z[i] - minZ) / CELL) * cols;
                cellOf[i] = c;
                cellStart[c + 1]++;
            }
        }
        for (int c = 0; c < cells; c++) {
            cellStart[c + 1] += cellStart[c];
        }
        int[] next = Arrays.copyOf(cellStart, cells);
        for (int i = 0; i < n; i++) {
            if (!dead[i]) {
                order[next[cellOf[i]]++] = i;
            }
        }
    }

    /**
     * Soldier {@code i} against those around it: how far it is pushed out of them (it moves itself only, by the
     * lighter one's share, a friend's overlap only partly), the nearest enemy it can go for, and a charge's blow.
     */
    private void touch(int i) {
        Kind k = kind[i];
        float px = 0;
        float pz = 0;
        int best = -1;
        float bestGap = ENGAGE;
        int cx = (int) ((x[i] - minX) / CELL);
        int cz = (int) ((z[i] - minZ) / CELL);
        for (int gz = Math.max(0, cz - 1); gz <= Math.min(rows - 1, cz + 1); gz++) {
            for (int gx = Math.max(0, cx - 1); gx <= Math.min(cols - 1, cx + 1); gx++) {
                int c = gx + gz * cols;
                for (int o = cellStart[c]; o < cellStart[c + 1]; o++) {
                    int j = order[o];
                    if (j == i) {
                        continue;
                    }
                    float dx = x[j] - x[i];
                    float dz = z[j] - z[i];
                    float d2 = dx * dx + dz * dz;
                    float touching = k.radius + kind[j].radius;
                    boolean enemy = side[j] != side[i];
                    if (enemy && d2 < (touching + ENGAGE) * (touching + ENGAGE) && down[j] <= 0) {
                        float gap = (float) Math.sqrt(d2) - touching;
                        if (gap < bestGap) {
                            bestGap = gap;
                            best = j;
                        }
                    }
                    if (d2 >= touching * touching) {
                        continue;
                    }
                    float d = (float) Math.sqrt(d2);
                    float ux = d > 1e-5f ? dx / d : (i < j ? 1 : -1); // two on one spot part along x
                    float uz = d > 1e-5f ? dz / d : 0;
                    float share = kind[j].mass / (k.mass + kind[j].mass) * (enemy ? 1 : FRIEND_GIVE);
                    px -= ux * (touching - d) * share;
                    pz -= uz * (touching - d) * share;
                    if (enemy) {
                        charge(i, j, ux, uz);
                    }
                }
            }
        }
        pushX[i] = px;
        pushZ[i] = pz;
        target[i] = best;
    }

    /** {@code i} running into enemy {@code j} (direction {@code ux}, {@code uz}): fast and heavy enough, it knocks it down, and is slowed. */
    private void charge(int i, int j, float ux, float uz) {
        float closing = (vx[i] - vx[j]) * ux + (vz[i] - vz[j]) * uz;
        if (closing < CHARGE_SPEED || down[j] > 0) {
            return;
        }
        float mi = kind[i].mass;
        float mj = kind[j].mass;
        if (mi * closing >= mj * KNOCK * CHARGE_SPEED) {
            down[j] = DOWN_SECONDS;
            if (lethal) {
                hurt[j] += kind[i].damage * closing / CHARGE_SPEED / 2; // the impact itself
            }
            kickX[j] += ux * closing * 0.1f;
            kickZ[j] += uz * closing * 0.1f;
        }
        slow[i] *= mi / (mi + mj); // each body it hits takes some of its speed
    }

    /** Takes its push and any kick, and its speed from where it truly went; faces its foe, the way it goes, or its front. */
    private void settle(int i) {
        x[i] += pushX[i] + kickX[i];
        z[i] += pushZ[i] + kickZ[i];
        kickX[i] = kickZ[i] = 0;
        vx[i] = (x[i] - oldX[i]) / STEP * slow[i];
        vz[i] = (z[i] - oldZ[i]) / STEP * slow[i];
        int t = target[i];
        float ax;
        float az;
        if (t >= 0) {
            ax = x[t] - x[i];
            az = z[t] - z[i];
        } else if (vx[i] * vx[i] + vz[i] * vz[i] > 0.25f) {
            ax = vx[i];
            az = vz[i];
        } else {
            ax = frontX[i];
            az = frontZ[i];
        }
        float l = (float) Math.sqrt(ax * ax + az * az);
        if (l > 1e-5f) {
            fx[i] = ax / l;
            fz[i] = az / l;
        }
    }

    /** A soldier on its feet with a foe in reach swings when its weapon is ready; every swing lands (as Minecraft's do). */
    private void strike(int i) {
        cooldown[i] = Math.max(0, cooldown[i] - STEP);
        int t = target[i];
        if (t < 0 || down[i] > 0 || cooldown[i] > 0 || dead[t]) {
            return;
        }
        float dx = x[t] - x[i];
        float dz = z[t] - z[i];
        float reach = kind[i].radius + kind[t].radius + kind[i].reach;
        if (dx * dx + dz * dz <= reach * reach) {
            if (lethal) {
                hurt[t] += kind[i].damage;
            }
            swings[i]++;
            struck[t]++;
            cooldown[i] = kind[i].interval;
        }
    }

    private void grow(int size) {
        kind = Arrays.copyOf(kind, size);
        side = Arrays.copyOf(side, size);
        dead = Arrays.copyOf(dead, size);
        x = Arrays.copyOf(x, size);
        z = Arrays.copyOf(z, size);
        oldX = Arrays.copyOf(oldX, size);
        oldZ = Arrays.copyOf(oldZ, size);
        vx = Arrays.copyOf(vx, size);
        vz = Arrays.copyOf(vz, size);
        fx = Arrays.copyOf(fx, size);
        fz = Arrays.copyOf(fz, size);
        goalX = Arrays.copyOf(goalX, size);
        goalZ = Arrays.copyOf(goalZ, size);
        frontX = Arrays.copyOf(frontX, size);
        frontZ = Arrays.copyOf(frontZ, size);
        health = Arrays.copyOf(health, size);
        hurt = Arrays.copyOf(hurt, size);
        cooldown = Arrays.copyOf(cooldown, size);
        down = Arrays.copyOf(down, size);
        pushX = Arrays.copyOf(pushX, size);
        pushZ = Arrays.copyOf(pushZ, size);
        kickX = Arrays.copyOf(kickX, size);
        kickZ = Arrays.copyOf(kickZ, size);
        slow = Arrays.copyOf(slow, size);
        target = Arrays.copyOf(target, size);
        swings = Arrays.copyOf(swings, size);
        struck = Arrays.copyOf(struck, size);
        cellOf = Arrays.copyOf(cellOf, size);
        order = Arrays.copyOf(order, size);
    }

    public int size() {
        return n;
    }

    public boolean alive(int i) {
        return !dead[i];
    }

    public boolean down(int i) {
        return down[i] > 0;
    }

    public float x(int i) {
        return x[i];
    }

    public float z(int i) {
        return z[i];
    }

    public float facingX(int i) {
        return fx[i];
    }

    public float facingZ(int i) {
        return fz[i];
    }

    public int side(int i) {
        return side[i];
    }

    /** How many of {@code side} have fallen. */
    public int fallen(int side) {
        return fallen[side];
    }
}
