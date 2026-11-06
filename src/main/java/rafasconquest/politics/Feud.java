package rafasconquest.politics;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * A feud between two rival lords in peacetime: raids, skirmishes and champions' duels, scored until one side yields (or
 * their liege makes them stop, or one of them makes peace). {@code a} began it. A skirmish or duel is first set for a
 * time and place (a {@link Clash}), shown on the map, so players may go and see it fought for real.
 */
public final class Feud {
    /** A lord: one of the realm's characters (by index), or a landed player. */
    public record Lord(int person, @Nullable UUID player) {
        public static Lord of(int person) {
            return new Lord(person, null);
        }

        public static Lord of(UUID player) {
            return new Lord(-1, player);
        }

        public boolean isPlayer() {
            return player != null;
        }

        /** A key for maps kept with the world: "p12" or "u&lt;uuid&gt;". */
        String key() {
            return player != null ? "u" + player : "p" + person;
        }

        static Lord ofKey(String key) {
            return key.startsWith("u") ? of(UUID.fromString(key.substring(1))) : of(Integer.parseInt(key.substring(1)));
        }
    }

    public enum Kind {
        SKIRMISH, DUEL
    }

    /**
     * A fight set for a time (in days) and place. For a duel, each side's champion (a character, -1: a player's sworn
     * sword) and whether they fight to the death rather than yield.
     */
    public static final class Clash {
        final Kind kind;
        final double at;
        final double x;
        final double z;
        /** Who called it: true if {@code a}. */
        final boolean byA;
        final int championA;
        final int championB;
        final boolean deathA;
        final boolean deathB;

        Clash(Kind kind, double at, double x, double z, boolean byA, int championA, int championB, boolean deathA, boolean deathB) {
            this.kind = kind;
            this.at = at;
            this.x = x;
            this.z = z;
            this.byA = byA;
            this.championA = championA;
            this.championB = championB;
            this.deathA = deathA;
            this.deathB = deathB;
        }

        public Kind kind() {
            return kind;
        }

        public double at() {
            return at;
        }

        public double x() {
            return x;
        }

        public double z() {
            return z;
        }

        public int champion(boolean a) {
            return a ? championA : championB;
        }

        public boolean toTheDeath(boolean a) {
            return a ? deathA : deathB;
        }

        CompoundTag save() {
            CompoundTag t = new CompoundTag();
            t.putInt("kind", kind.ordinal());
            t.putDouble("at", at);
            t.putDouble("x", x);
            t.putDouble("z", z);
            t.putBoolean("byA", byA);
            t.putInt("championA", championA);
            t.putInt("championB", championB);
            t.putBoolean("deathA", deathA);
            t.putBoolean("deathB", deathB);
            return t;
        }

        static Clash load(CompoundTag t) {
            return new Clash(Kind.values()[t.getInt("kind")], t.getDouble("at"), t.getDouble("x"), t.getDouble("z"), t.getBoolean("byA"),
                    t.getInt("championA"), t.getInt("championB"), t.getBoolean("deathA"), t.getBoolean("deathB"));
        }
    }

    final long id;
    final Lord a;
    final Lord b;
    final int nationA;
    final int nationB;
    final long started;
    int scoreA;
    int scoreB;
    /** Whether each side holds a captive from a won fight, to ransom. */
    boolean captiveA;
    boolean captiveB;
    @Nullable
    Clash clash;
    /** Its clash is being fought for real; its result comes later. */
    boolean awaiting;

    Feud(long id, Lord a, Lord b, int nationA, int nationB, long started) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.nationA = nationA;
        this.nationB = nationB;
        this.started = started;
    }

    public long id() {
        return id;
    }

    public Lord lord(boolean a) {
        return a ? this.a : b;
    }

    public int nation(boolean a) {
        return a ? nationA : nationB;
    }

    @Nullable
    public Clash clash() {
        return clash;
    }

    public boolean involves(Lord lord) {
        return a.equals(lord) || b.equals(lord);
    }

    CompoundTag save() {
        CompoundTag t = new CompoundTag();
        t.putLong("id", id);
        t.putString("a", a.key());
        t.putString("b", b.key());
        t.putInt("nationA", nationA);
        t.putInt("nationB", nationB);
        t.putLong("started", started);
        t.putInt("scoreA", scoreA);
        t.putInt("scoreB", scoreB);
        t.putBoolean("captiveA", captiveA);
        t.putBoolean("captiveB", captiveB);
        if (clash != null) {
            t.put("clash", clash.save());
        }
        return t;
    }

    static Feud load(CompoundTag t) {
        Feud f = new Feud(t.getLong("id"), Lord.ofKey(t.getString("a")), Lord.ofKey(t.getString("b")), t.getInt("nationA"),
                t.getInt("nationB"), t.getLong("started"));
        f.scoreA = t.getInt("scoreA");
        f.scoreB = t.getInt("scoreB");
        f.captiveA = t.getBoolean("captiveA");
        f.captiveB = t.getBoolean("captiveB");
        f.clash = t.contains("clash") ? Clash.load(t.getCompound("clash")) : null; // a real fight cut short is fought again
        return f;
    }
}
