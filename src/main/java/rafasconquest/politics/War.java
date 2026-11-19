package rafasconquest.politics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattleType;

import java.util.ArrayList;
import java.util.List;

/**
 * A war: who attacked whom, for which title ({@link Goal}), since when, which of its counties have been taken, the
 * county fought over next and where (the battle site, on its border), and each side's army. Allies who answered the call
 * lend half their soldiers to their side's army.
 */
public final class War {
    final long id;
    final int attacker;
    final int defender;
    final Goal goal;
    final long started;
    final List<Integer> taken = new ArrayList<>();
    /** The county the next battle is for, and where it is fought. */
    int target = -1;
    double siteX;
    double siteZ;
    /** What the next battle is, and the county whose fleet the attacker has beaten at sea (-1: none), to land at next. */
    BattleType type = BattleType.LAND;
    int seaWon = -1;
    @Nullable
    Army attackerArmy;
    @Nullable
    Army defenderArmy;
    /** Each side's fleet, a force of its own, raised from its ports' ships for a battle at sea; null when none is at sea. */
    @Nullable
    Army attackerFleet;
    @Nullable
    Army defenderFleet;
    int battles;
    /** Each side's general, chosen (a person's index), or -1: the best at war, by Martial, as each battle finds them. */
    int attackerGeneral = -1;
    int defenderGeneral = -1;
    final List<Integer> attackerAllies = new ArrayList<>();
    final List<Integer> defenderAllies = new ArrayList<>();

    War(long id, int attacker, int defender, Goal goal, long started) {
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

    public Goal goal() {
        return goal;
    }

    public long started() {
        return started;
    }

    public List<Integer> taken() {
        return taken;
    }

    public int target() {
        return target;
    }

    public double siteX() {
        return siteX;
    }

    public double siteZ() {
        return siteZ;
    }

    public BattleType type() {
        return type;
    }

    /** A side's force for the next battle: its fleet for a battle at sea, else its army. */
    @Nullable
    public Army army(boolean attacking) {
        return type == BattleType.OCEAN ? fleet(attacking) : land(attacking);
    }

    /** A side's land army, whatever the next battle is. */
    @Nullable
    public Army land(boolean attacking) {
        return attacking ? attackerArmy : defenderArmy;
    }

    @Nullable
    public Army fleet(boolean attacking) {
        return attacking ? attackerFleet : defenderFleet;
    }

    /** Sets a side's force for the next battle (its fleet at sea, else its army). */
    void setArmy(boolean attacking, @Nullable Army army) {
        if (type == BattleType.OCEAN) {
            if (attacking) {
                attackerFleet = army;
            } else {
                defenderFleet = army;
            }
        } else if (attacking) {
            attackerArmy = army;
        } else {
            defenderArmy = army;
        }
    }

    /** A side's chosen general (a person's index), or -1. */
    public int general(boolean attacking) {
        return attacking ? attackerGeneral : defenderGeneral;
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
        tag.putBoolean("duchy", goal.duchy());
        tag.putInt("title", goal.index());
        tag.putLong("started", started);
        tag.put("taken", new IntArrayTag(taken));
        tag.putInt("target", target);
        tag.putDouble("siteX", siteX);
        tag.putDouble("siteZ", siteZ);
        tag.putString("type", type.name());
        tag.putInt("seaWon", seaWon);
        if (attackerArmy != null) {
            tag.put("attackerArmy", attackerArmy.save());
        }
        if (defenderArmy != null) {
            tag.put("defenderArmy", defenderArmy.save());
        }
        if (attackerFleet != null) {
            tag.put("attackerFleet", attackerFleet.save());
        }
        if (defenderFleet != null) {
            tag.put("defenderFleet", defenderFleet.save());
        }
        tag.putInt("battles", battles);
        tag.putInt("attackerGeneral", attackerGeneral);
        tag.putInt("defenderGeneral", defenderGeneral);
        tag.put("attackerAllies", new IntArrayTag(attackerAllies));
        tag.put("defenderAllies", new IntArrayTag(defenderAllies));
        return tag;
    }

    static War load(CompoundTag tag) {
        War w = new War(tag.getLong("id"), tag.getInt("attacker"), tag.getInt("defender"), new Goal(tag.getBoolean("duchy"), tag.getInt("title")),
                tag.getLong("started"));
        for (int c : tag.getIntArray("taken")) {
            w.taken.add(c);
        }
        w.target = tag.getInt("target");
        w.siteX = tag.getDouble("siteX");
        w.siteZ = tag.getDouble("siteZ");
        w.type = tag.contains("type") ? BattleType.valueOf(tag.getString("type")) : BattleType.LAND;
        w.seaWon = tag.contains("seaWon") ? tag.getInt("seaWon") : -1;
        w.attackerArmy = tag.contains("attackerArmy") ? Army.load(tag.getCompound("attackerArmy")) : null;
        w.defenderArmy = tag.contains("defenderArmy") ? Army.load(tag.getCompound("defenderArmy")) : null;
        w.attackerFleet = tag.contains("attackerFleet") ? Army.load(tag.getCompound("attackerFleet")) : null;
        w.defenderFleet = tag.contains("defenderFleet") ? Army.load(tag.getCompound("defenderFleet")) : null;
        w.battles = tag.getInt("battles");
        w.attackerGeneral = tag.contains("attackerGeneral") ? tag.getInt("attackerGeneral") : -1;
        w.defenderGeneral = tag.contains("defenderGeneral") ? tag.getInt("defenderGeneral") : -1;
        for (int n : tag.getIntArray("attackerAllies")) {
            w.attackerAllies.add(n);
        }
        for (int n : tag.getIntArray("defenderAllies")) {
            w.defenderAllies.add(n);
        }
        return w;
    }
}
