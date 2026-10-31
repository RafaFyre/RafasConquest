package rafasconquest.politics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;

import java.util.ArrayList;
import java.util.List;

/**
 * A war: who attacked whom, over which of the defender's provinces, since when, and how it goes. Its score runs from
 * -100 (the defender has won) to 100 (the attacker has); each battle moves it. Allies who answered the call fight beside
 * each side.
 */
public final class War {
    final long id;
    final int attacker;
    final int defender;
    /** The province fought over: the defender's, beside the attacker's land. */
    final int goal;
    final long started;
    int score;
    int battles;
    final List<Integer> attackerAllies = new ArrayList<>();
    final List<Integer> defenderAllies = new ArrayList<>();

    War(long id, int attacker, int defender, int goal, long started) {
        this.id = id;
        this.attacker = attacker;
        this.defender = defender;
        this.goal = goal;
        this.started = started;
    }

    public long id() {
        return id;
    }

    public int attacker() {
        return attacker;
    }

    public int defender() {
        return defender;
    }

    public int goal() {
        return goal;
    }

    public long started() {
        return started;
    }

    public int score() {
        return score;
    }

    public int battles() {
        return battles;
    }

    public List<Integer> attackerAllies() {
        return attackerAllies;
    }

    public List<Integer> defenderAllies() {
        return defenderAllies;
    }

    /** Every nation on a side: its leader first. */
    public List<Integer> side(boolean attacking) {
        List<Integer> all = new ArrayList<>();
        all.add(attacking ? attacker : defender);
        all.addAll(attacking ? attackerAllies : defenderAllies);
        return all;
    }

    /** Whether a nation fights in this war, on either side. */
    public boolean involves(int nation) {
        return side(true).contains(nation) || side(false).contains(nation);
    }

    CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("id", id);
        tag.putInt("attacker", attacker);
        tag.putInt("defender", defender);
        tag.putInt("goal", goal);
        tag.putLong("started", started);
        tag.putInt("score", score);
        tag.putInt("battles", battles);
        tag.put("attackerAllies", new IntArrayTag(attackerAllies));
        tag.put("defenderAllies", new IntArrayTag(defenderAllies));
        return tag;
    }

    static War load(CompoundTag tag) {
        War w = new War(tag.getLong("id"), tag.getInt("attacker"), tag.getInt("defender"), tag.getInt("goal"), tag.getLong("started"));
        w.score = tag.getInt("score");
        w.battles = tag.getInt("battles");
        for (int n : tag.getIntArray("attackerAllies")) {
            w.attackerAllies.add(n);
        }
        for (int n : tag.getIntArray("defenderAllies")) {
            w.defenderAllies.add(n);
        }
        return w;
    }
}
