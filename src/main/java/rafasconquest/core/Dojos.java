package rafasconquest.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The ten dojos of the mantra schools, one for each school, planned with the world map: on land, clear of the seat
 * cities and the eye of spawn, and at least {@link #APART} blocks from each other and from the sword sanctums. Seven
 * schools are known, their dojos standing open on the surface; three are hidden, their dojos underground.
 */
public final class Dojos {
    /** The mantra schools. Each has one dojo; the hidden ones' dojos are underground and not on the map. */
    public enum School {
        FIRE("Fire", false), WATER("Water", false), WIND("Wind", false), LIGHTNING("Lightning", false), ICE("Ice", false),
        STONE("Stone", false), IRON("Iron", false), BLOOD("Blood", true), SHADOW("Shadow", true), LIGHT("Light", true);

        private final String title;
        private final boolean hidden;

        School(String title, boolean hidden) {
            this.title = title;
            this.hidden = hidden;
        }

        public String title() {
            return title;
        }

        public boolean hidden() {
            return hidden;
        }
    }

    /** A school's dojo, and where it stands. */
    public record Dojo(School school, int x, int z) {
    }

    /** Whether a spot is fit for a dojo: on land, and clear of every city. */
    @FunctionalInterface
    public interface Ground {
        boolean fits(int x, int z);
    }

    /** How far apart the dojos stand, from each other and from the sword sanctums. */
    public static final int APART = 7000;
    /** Spots looked at, at random over the known world. */
    static final int CANDIDATES = 4000;
    private static final long SALT = 0x444F4A4F53L;

    private Dojos() {
    }

    /**
     * The dojos, each school's at a spot of the known world (within {@code radius} of spawn, beyond {@code eyeRadius})
     * that {@code ground} allows, {@link #APART} from the others and from {@code sanctums}. Spots are tried at random; if
     * the world is too small for all ten, as many as fit.
     */
    public static List<Dojo> plan(long seed, int radius, int eyeRadius, List<int[]> sanctums, Ground ground) {
        Random random = new Random(seed ^ SALT);
        List<int[]> taken = new ArrayList<>(sanctums);
        List<int[]> spots = new ArrayList<>();
        for (int i = 0; i < CANDIDATES && spots.size() < School.values().length; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double distance = eyeRadius + Math.sqrt(random.nextDouble()) * (radius - eyeRadius);
            int x = (int) Math.round(Math.cos(angle) * distance);
            int z = (int) Math.round(Math.sin(angle) * distance);
            if (!ground.fits(x, z) || taken.stream().anyMatch(t -> Math.hypot(t[0] - x, t[1] - z) < APART)) {
                continue;
            }
            taken.add(new int[]{x, z});
            spots.add(new int[]{x, z});
        }
        List<School> schools = new ArrayList<>(List.of(School.values()));
        Collections.shuffle(schools, random);
        // The known schools first, so a world too small for all ten loses hidden ones before known ones.
        schools.sort((a, b) -> Boolean.compare(a.hidden(), b.hidden()));
        List<Dojo> dojos = new ArrayList<>();
        for (int i = 0; i < spots.size(); i++) {
            dojos.add(new Dojo(schools.get(i), spots.get(i)[0], spots.get(i)[1]));
        }
        return dojos;
    }
}
