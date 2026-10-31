package rafasconquest.politics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * A side's army in a war: how many soldiers, where it is (x, z), where it mustered, and when it may march (until then it
 * musters there). It marches at {@link Politics#MARCH} blocks a day through its stops, if any (a ceremonial muster's
 * city by the field), then to the war's battle site.
 */
public final class Army {
    int soldiers;
    double x;
    double z;
    /** The time (in days) it is ready to march. */
    double readyAt;
    /** Where it mustered: a capital, city or castle, by name. */
    final String place;
    /** The stops still ahead of it before the battle site, in order: each (x, z). */
    final List<double[]> stops = new ArrayList<>();

    Army(int soldiers, double x, double z, double readyAt, String place) {
        this.soldiers = soldiers;
        this.x = x;
        this.z = z;
        this.readyAt = readyAt;
        this.place = place;
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

    public String place() {
        return place;
    }

    public boolean mustering(double now) {
        return now < readyAt;
    }

    /** Where it is heading next: its next stop, or the battle site. */
    public double[] heading(War war) {
        return stops.isEmpty() ? new double[]{war.siteX, war.siteZ} : stops.get(0);
    }

    CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("soldiers", soldiers);
        tag.putDouble("x", x);
        tag.putDouble("z", z);
        tag.putDouble("readyAt", readyAt);
        tag.putString("place", place);
        ListTag list = new ListTag();
        for (double[] s : stops) {
            CompoundTag t = new CompoundTag();
            t.putDouble("x", s[0]);
            t.putDouble("z", s[1]);
            list.add(t);
        }
        tag.put("stops", list);
        return tag;
    }

    static Army load(CompoundTag tag) {
        Army army = new Army(tag.getInt("soldiers"), tag.getDouble("x"), tag.getDouble("z"), tag.getDouble("readyAt"), tag.getString("place"));
        for (Tag t : tag.getList("stops", Tag.TAG_COMPOUND)) {
            CompoundTag s = (CompoundTag) t;
            army.stops.add(new double[]{s.getDouble("x"), s.getDouble("z")});
        }
        return army;
    }
}
