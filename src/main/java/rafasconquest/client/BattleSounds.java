package rafasconquest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import rafasconquest.RafasConquest;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.battle.FormationSnapshot.Kind;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The sound of a battle as a whole, from the formations the client is told about (so it carries from blocks that are
 * only numbers too): the thunder of charging cavalry, and the din of fighting along the front. Both are recordings,
 * cut into 10 s clips that fade in and out; each source starts a new clip, picked at random, before the last ends, so
 * it never falls silent and no two sound alike:
 * <ul>
 *   <li>the charge: a cavalry charge, from each cavalry block riding at a gallop, louder the more riders and the
 *   faster they go; only the {@link #HOOF_SOURCES} nearest play. A clip follows its block as it rides, and fades out
 *   when the block slows or falls;</li>
 *   <li>the din: a medieval battle (crowd and swords), from the middle of each stretch of up to {@link #DIN_GROUP}
 *   fighting infantry blocks (about 100 soldiers), louder the more are fighting; only the {@link #DIN_SOURCES} nearest
 *   play;</li>
 *   <li>the oars: a galley rowing, from each ship under way or turning; only the {@link #OAR_SOURCES} nearest play, and
 *   a clip follows its ship and fades out when it lies still;</li>
 *   <li>the shouting: troops calling out, from each stretch of troops of any kind, fighting or not; only the
 *   {@link #SHOUT_SOURCES} nearest play, and it carries half as far as the din.</li>
 * </ul>
 * Client thread only.
 */
public final class BattleSounds {
    private static final int HOOF_SOURCES = 8;
    private static final int DIN_SOURCES = 12;
    private static final int OAR_SOURCES = 6;
    private static final int SHOUT_SOURCES = 8;
    /** A ship is heard rowing above this pace, in blocks a second, or while it turns. */
    private static final double ROW_PACE = 0.5;
    /** Fighting infantry blocks heard as one stretch of front, and how far apart they may be. */
    private static final int DIN_GROUP = 5;
    private static final double DIN_GROUP_REACH = 60;
    /** Cavalry is heard charging above this pace, in blocks a second (a charge rides at 7). */
    private static final double GALLOP = 4;
    /** A sound at volume v carries 16 v blocks. */
    private static final float MAX_VOLUME = 8;
    /** The shouting of troops, and a ship's oars, carry half as far as the din of fighting. */
    private static final float SHOUT_VOLUME = MAX_VOLUME / 2;
    /** In the strategy view sounds carry this many times as far, up to the camera and across the field. */
    private static final float STRATEGY_REACH = 2;
    /** The clips (assets/rafasconquest/sounds.json); played on the client only, so never registered. */
    private static final SoundEvent CHARGE = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "battle.charge"));
    private static final SoundEvent DIN = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "battle.din"));
    private static final SoundEvent OARS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "battle.oars"));
    private static final SoundEvent SHOUT = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "battle.shout"));
    /** A clip lasts 10 s, fading for the last second: the next starts 8 to 9 s after the last. */
    private static final int CLIP_EVERY = 160;
    private static final int CLIP_JITTER = 20;
    /** The size of the cells stretches of front are known by. */
    private static final int DIN_CELL = 32;
    /** The sounds of a fight die away over this many ticks once it stops. */
    private static final int FADE_TICKS = 60;

    private static final RandomSource RANDOM = RandomSource.create();
    private static List<Source> hooves = List.of();
    private static List<Source> din = List.of();
    private static List<Source> oars = List.of();
    private static List<Source> shouts = List.of();
    private static int ticks;
    /** When each cavalry block, by its id, and each stretch of front, by its cell, starts its next clip. */
    private static final Map<Long, Integer> nextCharge = new HashMap<>();
    private static final Map<Long, Integer> nextDin = new HashMap<>();
    private static final Map<Long, Integer> nextOars = new HashMap<>();
    private static final Map<Long, Integer> nextShout = new HashMap<>();

    /** Where a sound comes from, and how loud; the key tells one source's clips from another's. */
    private record Source(long key, double x, double y, double z, double spread, float volume) {
    }

    /**
     * A din clip, from its stretch of front; it fades out over {@link #FADE_TICKS} once nobody is fighting there any
     * more (the lines broke apart, or pulled back for the night).
     */
    private static final class DinSound extends AbstractTickableSoundInstance {
        private final float loudness;
        private final double reach;
        /** The shouting, heard while any troops stand there; else the din, heard while infantry fights there. */
        private final boolean shout;

        DinSound(Source s, double x, double z, float pitch, boolean shout) {
            super(shout ? SHOUT : DIN, SoundSource.NEUTRAL, RANDOM);
            this.shout = shout;
            this.loudness = loudness(s);
            this.volume = loudness;
            this.pitch = pitch;
            this.x = x;
            this.y = s.y();
            this.z = z;
            this.reach = s.spread() + DIN_GROUP_REACH;
        }

        @Override
        public void tick() {
            if (ticks % 10 == 0) {
                fighting = heardNear(x, z, reach, shout);
            }
            if (!fighting) {
                volume -= loudness / FADE_TICKS;
                if (volume <= 0) {
                    stop();
                }
            }
        }

        private boolean fighting = true;
    }

    /** Whether a formation is heard: shouting, any troops; in the din, infantry fighting. */
    private static boolean heard(FormationSnapshot f, boolean shout) {
        return shout ? f.alive() > 1 : f.fighting() && f.kind().infantry();
    }

    /** Whether a ship is rowing: under way or turning (the one formation aboard that draws it). */
    private static boolean rowing(FormationSnapshot f) {
        return f.ship() != null && f.ship().hull() >= 0 && f.alive() > 0
                && (DistantArmies.speed(f) >= ROW_PACE || DistantArmies.turning(f));
    }

    /** Whether any formation is {@link #heard} within {@code reach} of a point. */
    private static boolean heardNear(double x, double z, double reach, boolean shout) {
        for (FormationSnapshot f : DistantArmies.now()) {
            if (heard(f, shout) && Math.hypot(f.x() - x, f.z() - z) <= reach) {
                return true;
            }
        }
        return false;
    }

    /**
     * A charge clip, riding along with its cavalry block; it fades out over three seconds once the block stops charging.
     * A ship's oars likewise, until it lies still.
     */
    private static final class ChargeSound extends AbstractTickableSoundInstance {
        private final int formation;
        private final float loudness;
        private final double offsetX;
        private final double offsetZ;
        private final boolean oars;

        ChargeSound(Source s, float pitch, boolean oars) {
            super(oars ? OARS : CHARGE, SoundSource.NEUTRAL, RANDOM);
            this.oars = oars;
            this.formation = (int) s.key();
            this.loudness = loudness(s);
            this.volume = loudness;
            this.pitch = pitch;
            this.offsetX = (RANDOM.nextDouble() * 2 - 1) * s.spread();
            this.offsetZ = (RANDOM.nextDouble() * 2 - 1) * s.spread();
            this.x = s.x() + offsetX;
            this.y = s.y();
            this.z = s.z() + offsetZ;
        }

        @Override
        public void tick() {
            FormationSnapshot f = DistantArmies.at(formation);
            if (f == null || f.alive() == 0 || (oars ? !rowing(f) : DistantArmies.speed(f) < GALLOP)) {
                volume -= loudness / FADE_TICKS;
                if (volume <= 0) {
                    stop();
                }
                return;
            }
            x = f.x() + offsetX;
            y = f.y() + 1;
            z = f.z() + offsetZ;
        }
    }

    private BattleSounds() {
    }

    /** A clip's volume; every source is at least 2, so the game plays it at full gain and the volume only sets its reach. */
    private static float loudness(Source s) {
        return Minecraft.getInstance().screen instanceof BattleScreen ? s.volume() * STRATEGY_REACH : s.volume();
    }

    /** Each client tick: once a second works out where the sounds come from, every tick plays some of them. */
    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.isPaused()) {
            return;
        }
        Vec3 ear = mc.gameRenderer.getMainCamera().getPosition();
        if (ticks++ % 20 == 0) {
            List<FormationSnapshot> formations = DistantArmies.now();
            hooves = hooves(formations, ear);
            din = din(formations, ear, false);
            shouts = din(formations, ear, true);
            oars = oars(formations, ear);
        }
        for (Source s : hooves) {
            if (due(nextCharge, s.key())) {
                mc.getSoundManager().play(new ChargeSound(s, 0.95F + RANDOM.nextFloat() * 0.1F, false));
            }
        }
        for (Source s : oars) {
            if (due(nextOars, s.key())) {
                mc.getSoundManager().play(new ChargeSound(s, 0.95F + RANDOM.nextFloat() * 0.1F, true));
            }
        }
        play(mc, shouts, nextShout, true);
        play(mc, din, nextDin, false);
        if (ticks % 200 == 0) {
            nextCharge.values().removeIf(next -> next < ticks - 200);
            nextDin.values().removeIf(next -> next < ticks - 200);
            nextOars.values().removeIf(next -> next < ticks - 200);
            nextShout.values().removeIf(next -> next < ticks - 200);
        }
    }

    private static void play(Minecraft mc, List<Source> sources, Map<Long, Integer> next, boolean shout) {
        for (Source s : sources) {
            if (due(next, s.key())) {
                double x = s.x() + (RANDOM.nextDouble() * 2 - 1) * s.spread();
                double z = s.z() + (RANDOM.nextDouble() * 2 - 1) * s.spread();
                mc.getSoundManager().play(new DinSound(s, x, z, 0.95F + RANDOM.nextFloat() * 0.1F, shout));
            }
        }
    }

    /**
     * Whether a source starts its next clip now (and when the one after starts, if so). A source newly heard starts
     * within 2 s, so sources heard at once don't all start together.
     */
    private static boolean due(Map<Long, Integer> next, long key) {
        if (ticks < next.computeIfAbsent(key, k -> ticks + RANDOM.nextInt(40))) {
            return false;
        }
        next.put(key, ticks + CLIP_EVERY + RANDOM.nextInt(CLIP_JITTER));
        return true;
    }

    /** A cavalry block's next charge clip starts no sooner than {@code delay} ticks from now (after its war cry). */
    public static void holdCharge(int formation, int delay) {
        nextCharge.put((long) formation, ticks + delay);
    }

    public static void clear() {
        hooves = List.of();
        din = List.of();
        oars = List.of();
        shouts = List.of();
        nextCharge.clear();
        nextDin.clear();
        nextOars.clear();
        nextShout.clear();
    }

    private static List<Source> hooves(List<FormationSnapshot> formations, Vec3 ear) {
        List<Source> sources = new ArrayList<>();
        for (FormationSnapshot f : formations) {
            double speed = DistantArmies.speed(f);
            if (!f.kind().mounted() || speed < GALLOP) {
                continue;
            }
            float volume = (float) (2 + (MAX_VOLUME - 2) * Math.min(1, f.alive() / 20.0) * Math.min(1, speed / 7));
            sources.add(new Source(f.id(), f.x(), f.y() + 1, f.z(), f.halfWidth(), volume));
        }
        return nearest(sources, ear, HOOF_SOURCES);
    }

    /** Each rowing ship, louder the bigger its crew. */
    private static List<Source> oars(List<FormationSnapshot> formations, Vec3 ear) {
        List<Source> sources = new ArrayList<>();
        for (FormationSnapshot f : formations) {
            if (rowing(f)) {
                float volume = (float) (2 + (SHOUT_VOLUME - 2) * Math.min(1, f.alive() / 40.0));
                sources.add(new Source(f.id(), f.x(), f.y() + 1, f.z(), f.halfWidth(), volume));
            }
        }
        return nearest(sources, ear, OAR_SOURCES);
    }

    /**
     * Groups the fighting infantry into stretches of front, nearest neighbours together; for the shouting, all troops
     * into stretches likewise.
     */
    private static List<Source> din(List<FormationSnapshot> formations, Vec3 ear, boolean shout) {
        List<FormationSnapshot> fighting = new ArrayList<>();
        for (FormationSnapshot f : formations) {
            if (heard(f, shout)) {
                fighting.add(f);
            }
        }
        List<Source> sources = new ArrayList<>();
        while (!fighting.isEmpty()) {
            FormationSnapshot seed = fighting.remove(0);
            List<FormationSnapshot> group = new ArrayList<>(List.of(seed));
            fighting.sort(Comparator.comparingDouble(f -> Math.hypot(f.x() - seed.x(), f.z() - seed.z())));
            while (group.size() < DIN_GROUP && !fighting.isEmpty()
                    && Math.hypot(fighting.get(0).x() - seed.x(), fighting.get(0).z() - seed.z()) <= DIN_GROUP_REACH) {
                group.add(fighting.remove(0));
            }
            double x = 0;
            double y = 0;
            double z = 0;
            int soldiers = 0;
            for (FormationSnapshot f : group) {
                x += f.x();
                y += f.y();
                z += f.z();
                soldiers += f.alive();
            }
            int n = group.size();
            float volume = (float) (2 + ((shout ? SHOUT_VOLUME : MAX_VOLUME) - 2) * Math.min(1, soldiers / 100.0));
            // A stretch is known by the cell its middle is in, to keep its clips in turn as the front moves.
            long cell = ((long) Math.floorDiv((int) (x / n), DIN_CELL) << 32) | (Math.floorDiv((int) (z / n), DIN_CELL) & 0xFFFFFFFFL);
            sources.add(new Source(cell, x / n, y / n + 1, z / n, 8 * n, volume));
        }
        return nearest(sources, ear, shout ? SHOUT_SOURCES : DIN_SOURCES);
    }

    private static List<Source> nearest(List<Source> sources, Vec3 ear, int most) {
        sources.sort(Comparator.comparingDouble(s -> distance(s.x(), s.z(), ear)));
        return List.copyOf(sources.subList(0, Math.min(most, sources.size())));
    }

    private static double distance(double x, double z, Vec3 ear) {
        return Math.hypot(x - ear.x, z - ear.z);
    }
}
