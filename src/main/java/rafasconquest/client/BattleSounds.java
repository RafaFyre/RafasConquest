package rafasconquest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.battle.FormationSnapshot.Kind;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The sound of a battle as a whole, from the formations the client is told about (so it carries from blocks that are
 * only numbers too): hoofbeats from cavalry on the move, and the din of fighting along the front. Both are made of
 * Minecraft's own sounds played over and over, each at a block's or a stretch of front's middle:
 * <ul>
 *   <li>hoofbeats: the horse gallop step, quick and slightly varied, from each moving cavalry block, louder the more
 *   riders and the faster they go; only the {@link #HOOF_SOURCES} nearest play, and none close enough to hear the real
 *   horses;</li>
 *   <li>the din: sword blows, sweeps, shields and soldiers being hurt, at random, from each stretch of up to
 *   {@link #DIN_GROUP} fighting infantry blocks (about 100 soldiers), louder the more are fighting; only the
 *   {@link #DIN_SOURCES} nearest play.</li>
 * </ul>
 * Client thread only.
 */
public final class BattleSounds {
    private static final int HOOF_SOURCES = 8;
    private static final int DIN_SOURCES = 12;
    /** Fighting infantry blocks heard as one stretch of front, and how far apart they may be. */
    private static final int DIN_GROUP = 5;
    private static final double DIN_GROUP_REACH = 60;
    /** Cavalry is heard galloping above this pace, and not within this distance (the real horses are heard there). */
    private static final double TROTTING = 2;
    private static final double REAL_HORSES_HEARD = 24;
    /** A sound at volume v carries 16 v blocks. */
    private static final float MAX_VOLUME = 8;
    private static final SoundEvent[] DIN = {
            SoundEvents.PLAYER_ATTACK_STRONG, SoundEvents.PLAYER_ATTACK_SWEEP, SoundEvents.PLAYER_ATTACK_KNOCKBACK,
            SoundEvents.PLAYER_ATTACK_CRIT, SoundEvents.SHIELD_BLOCK, SoundEvents.PLAYER_HURT, SoundEvents.VILLAGER_HURT};

    private static final RandomSource RANDOM = RandomSource.create();
    private static List<Source> hooves = List.of();
    private static List<Source> din = List.of();
    private static int ticks;

    /** Where a sound comes from, and how loud. */
    private record Source(double x, double y, double z, double spread, float volume) {
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
            if (RANDOM.nextInt(3) == 0) {
                play(mc, SoundEvents.HORSE_GALLOP, s, 0.8F + RANDOM.nextFloat() * 0.3F);
            }
        }
        for (Source s : din) {
            if (RANDOM.nextInt(4) == 0) {
                play(mc, DIN[RANDOM.nextInt(DIN.length)], s, 0.8F + RANDOM.nextFloat() * 0.4F);
            }
        }
    }

    public static void clear() {
        hooves = List.of();
        din = List.of();
    }

    private static List<Source> hooves(List<FormationSnapshot> formations, Vec3 ear) {
        List<Source> sources = new ArrayList<>();
        for (FormationSnapshot f : formations) {
            double speed = DistantArmies.speed(f);
            if (f.kind() != Kind.CAVALRY || speed < TROTTING || distance(f.x(), f.z(), ear) < REAL_HORSES_HEARD) {
                continue;
            }
            float volume = (float) (2 + (MAX_VOLUME - 2) * Math.min(1, f.alive() / 20.0) * Math.min(1, speed / 7));
            sources.add(new Source(f.x(), f.y() + 1, f.z(), f.halfWidth(), volume));
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
            sources.add(new Source(x / n, y / n + 1, z / n, 8 * n, volume));
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

    /** One sound from somewhere across the source's width, so many of them sound like many soldiers. */
    private static void play(Minecraft mc, SoundEvent sound, Source s, float pitch) {
        double x = s.x() + (RANDOM.nextDouble() * 2 - 1) * s.spread();
        double z = s.z() + (RANDOM.nextDouble() * 2 - 1) * s.spread();
        mc.getSoundManager().play(new SimpleSoundInstance(sound, SoundSource.NEUTRAL, s.volume(), pitch, RANDOM, x, s.y(), z));
    }
}
