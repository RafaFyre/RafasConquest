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
 *   play.</li>
 * </ul>
 * Client thread only.
 */
public final class BattleSounds {
    private static final int HOOF_SOURCES = 8;
    private static final int DIN_SOURCES = 12;
    /** Fighting infantry blocks heard as one stretch of front, and how far apart they may be. */
    private static final int DIN_GROUP = 5;
    private static final double DIN_GROUP_REACH = 60;
    /** Cavalry is heard charging above this pace, in blocks a second (a charge rides at 7). */
    private static final double GALLOP = 4;
    /** A sound at volume v carries 16 v blocks. */
    private static final float MAX_VOLUME = 8;
    /** The clips (assets/rafasconquest/sounds.json); played on the client only, so never registered. */
    private static final SoundEvent CHARGE = SoundEvent.createVariableRangeEvent(new ResourceLocation(RafasConquest.MOD_ID, "battle.charge"));
    private static final SoundEvent DIN = SoundEvent.createVariableRangeEvent(new ResourceLocation(RafasConquest.MOD_ID, "battle.din"));
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
    private static int ticks;
    /** When each cavalry block, by its id, and each stretch of front, by its cell, starts its next clip. */
    private static final Map<Long, Integer> nextCharge = new HashMap<>();
    private static final Map<Long, Integer> nextDin = new HashMap<>();

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

        DinSound(Source s, double x, double z, float pitch) {
            super(DIN, SoundSource.NEUTRAL, RANDOM);
            this.loudness = s.volume();
            this.volume = s.volume();
            this.pitch = pitch;
            this.x = x;
            this.y = s.y();
            this.z = z;
            this.reach = s.spread() + DIN_GROUP_REACH;
        }

        @Override
        public void tick() {
            if (ticks % 10 == 0) {
                fighting = fightingNear(x, z, reach);
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

    /** Whether any infantry is fighting within {@code reach} of a point. */
    private static boolean fightingNear(double x, double z, double reach) {
        for (FormationSnapshot f : DistantArmies.now()) {
            if (f.fighting() && (f.kind() == Kind.SWORDSMEN || f.kind() == Kind.SHIELDWALL) && Math.hypot(f.x() - x, f.z() - z) <= reach) {
                return true;
            }
        }
        return false;
    }

    /** A charge clip, riding along with its cavalry block; it fades out over three seconds once the block stops charging. */
    private static final class ChargeSound extends AbstractTickableSoundInstance {
        private final int formation;
        private final float loudness;
        private final double offsetX;
        private final double offsetZ;

        ChargeSound(Source s, float pitch) {
            super(CHARGE, SoundSource.NEUTRAL, RANDOM);
            this.formation = (int) s.key();
            this.loudness = s.volume();
            this.volume = s.volume();
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
            if (f == null || f.alive() == 0 || DistantArmies.speed(f) < GALLOP) {
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
            din = din(formations, ear);
        }
        for (Source s : hooves) {
            if (due(nextCharge, s.key())) {
                mc.getSoundManager().play(new ChargeSound(s, 0.95F + RANDOM.nextFloat() * 0.1F));
            }
        }
        for (Source s : din) {
            if (due(nextDin, s.key())) {
                double x = s.x() + (RANDOM.nextDouble() * 2 - 1) * s.spread();
                double z = s.z() + (RANDOM.nextDouble() * 2 - 1) * s.spread();
                mc.getSoundManager().play(new DinSound(s, x, z, 0.95F + RANDOM.nextFloat() * 0.1F));
            }
        }
        if (ticks % 200 == 0) {
            nextCharge.values().removeIf(next -> next < ticks - 200);
            nextDin.values().removeIf(next -> next < ticks - 200);
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

    public static void clear() {
        hooves = List.of();
        din = List.of();
        nextCharge.clear();
        nextDin.clear();
    }

    private static List<Source> hooves(List<FormationSnapshot> formations, Vec3 ear) {
        List<Source> sources = new ArrayList<>();
        for (FormationSnapshot f : formations) {
            double speed = DistantArmies.speed(f);
            if (f.kind() != Kind.CAVALRY || speed < GALLOP) {
                continue;
            }
            float volume = (float) (2 + (MAX_VOLUME - 2) * Math.min(1, f.alive() / 20.0) * Math.min(1, speed / 7));
            sources.add(new Source(f.id(), f.x(), f.y() + 1, f.z(), f.halfWidth(), volume));
        }
        return nearest(sources, ear, HOOF_SOURCES);
    }

    /** Groups the fighting infantry into stretches of front, nearest neighbours together. */
    private static List<Source> din(List<FormationSnapshot> formations, Vec3 ear) {
        List<FormationSnapshot> fighting = new ArrayList<>();
        for (FormationSnapshot f : formations) {
            if (f.fighting() && (f.kind() == Kind.SWORDSMEN || f.kind() == Kind.SHIELDWALL)) {
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
            float volume = (float) (2 + (MAX_VOLUME - 2) * Math.min(1, soldiers / 100.0));
            // A stretch is known by the cell its middle is in, to keep its clips in turn as the front moves.
            long cell = ((long) Math.floorDiv((int) (x / n), DIN_CELL) << 32) | (Math.floorDiv((int) (z / n), DIN_CELL) & 0xFFFFFFFFL);
            sources.add(new Source(cell, x / n, y / n + 1, z / n, 8 * n, volume));
        }
        return nearest(sources, ear, DIN_SOURCES);
    }

    private static List<Source> nearest(List<Source> sources, Vec3 ear, int most) {
        sources.sort(Comparator.comparingDouble(s -> distance(s.x(), s.z(), ear)));
        return List.copyOf(sources.subList(0, Math.min(most, sources.size())));
    }

    private static double distance(double x, double z, Vec3 ear) {
        return Math.hypot(x - ear.x, z - ear.z);
    }
}
