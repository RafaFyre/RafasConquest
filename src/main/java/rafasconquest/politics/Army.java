package rafasconquest.politics;

import net.minecraft.nbt.CompoundTag;

/**
 * A side's army in a war: how many soldiers, where it is (x, z), and when it may march (until then it musters, at its
 * capital). It marches to the war's battle site at {@link Politics#MARCH} blocks a day.
 */
public final class Army {
    int soldiers;
    double x;
    double z;
    /** The time (in days) it is ready to march. */
    double readyAt;

    Army(int soldiers, double x, double z, double readyAt) {
        this.soldiers = soldiers;
        this.x = x;
        this.z = z;
        this.readyAt = readyAt;
    }

    public int soldiers() {
        return soldiers;
    }

    public double x() {
        return x;
    }

    public double z() {
        return z;
    }

    public boolean mustering(double now) {
        return now < readyAt;
    }

    CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("soldiers", soldiers);
        tag.putDouble("x", x);
        tag.putDouble("z", z);
        tag.putDouble("readyAt", readyAt);
        return tag;
    }

    static Army load(CompoundTag tag) {
        return new Army(tag.getInt("soldiers"), tag.getDouble("x"), tag.getDouble("z"), tag.getDouble("readyAt"));
    }
}
