package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Medium Cannon shot in flight, as plain numbers: no entity, so hundreds cost only a ray each a tick. A ball flies a
 * parabola ({@link CannonAim#GRAVITY}, no drag); Sable's own ray test takes in its ships' blocks, so it strikes a hull
 * as it would the ground. Clients are told of each shot once, as it is fired, and draw its flight themselves
 * (ArrowVolleys.fired). A roundshot smashes through up to {@link #SMASH} blocks, slowing with each, and hurts whoever
 * it passes through; it stops in water, on a block too hard to break, or when spent.
 */
public final class CannonShots {
    /** Blocks one roundshot breaks before it is spent. */
    private static final int SMASH = 3;
    /** Its speed kept through each block it breaks, and through each body. */
    private static final double THROUGH_BLOCK = 0.6;
    private static final double THROUGH_BODY = 0.85;
    /** It breaks nothing harder than this (obsidian is 50). */
    private static final float HARDEST = 10;
    /** Below this speed (blocks a tick) it is spent. */
    private static final double SPENT = 0.5;
    /** It is forgotten after this many ticks in flight. */
    private static final int LIFE = 200;
    /** It strikes nothing for its first tick: its own gun's rail and deck about the muzzle. */
    private static final int CLEAR = 1;
    /** Clients this near (blocks) the muzzle are told of a shot. */
    private static final double TOLD_WITHIN = 640;

    private static final class Ball {
        final Vec3 from;
        Vec3 at;
        Vector3d v;
        final MediumCannonEntity.Shot shot;
        @Nullable
        final Entity by;
        final float damage;
        int smashed;
        int age;
        final Set<Integer> struck = new HashSet<>();

        Ball(Vec3 at, Vector3d v, MediumCannonEntity.Shot shot, @Nullable Entity by, float damage) {
            this.from = at;
            this.at = at;
            this.v = v;
            this.shot = shot;
            this.by = by;
            this.damage = damage;
        }
    }

    private static final java.util.Map<ServerLevel, List<Ball>> FLYING = new java.util.WeakHashMap<>();

    private CannonShots() {
    }

    static void fire(ServerLevel level, Vec3 muzzle, Vector3d v, MediumCannonEntity.Shot shot, @Nullable Entity by, double damage) {
        FLYING.computeIfAbsent(level, k -> new ArrayList<>()).add(new Ball(muzzle, v, shot, by, (float) damage));
        List<ServerPlayer> near = new ArrayList<>();
        for (ServerPlayer p : level.players()) {
            if (p.distanceToSqr(muzzle) <= TOLD_WITHIN * TOLD_WITHIN) {
                near.add(p);
            }
        }
        rafasconquest.net.ConquestNetwork.sendCannonShot(near, muzzle, v.x, v.y, v.z, shot.ordinal());
    }

    /** Each tick of each level. */
    public static void tick(ServerLevel level) {
        List<Ball> balls = FLYING.get(level);
        if (balls != null && !balls.isEmpty()) {
            balls.removeIf(b -> !fly(level, b));
        }
    }

    /** One tick of a ball's flight; false once it is done. */
    private static boolean fly(ServerLevel level, Ball b) {
        if (++b.age > LIFE) {
            return false;
        }
        Vec3 next = b.at.add(b.v.x, b.v.y, b.v.z);
        b.v.y -= CannonAim.GRAVITY;
        if (!level.isLoaded(BlockPos.containing(next))) {
            return false; // never loads ground for a shot
        }
        if (b.age <= CLEAR) {
            b.at = next;
            return true;
        }
        BlockHitResult hit = level.clip(new ClipContext(b.at, next, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, CollisionContext.empty()));
        // On a ship, Sable gives the hit in its plot's frame, millions of blocks off: back into the world, where the ball
        // and whoever it strikes are (else strike's box spans the gap, Sable refuses it, and nobody aboard is hurt).
        Vec3 end = hit.getType() == HitResult.Type.MISS ? next : rafasconquest.bridge.SableLink.outOfPlot(level, hit.getLocation());
        strike(level, b, b.at, end);
        if (b.v.lengthSquared() < SPENT * SPENT) {
            return false;
        }
        if (hit.getType() == HitResult.Type.MISS) {
            b.at = next;
            return true;
        }
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (state.getFluidState().is(FluidTags.WATER)) {
            return false; // shortcut: no splash yet; the client's ball just ends there
        }
        float hardness = state.getDestroySpeed(level, pos);
        if (hardness < 0 || hardness > HARDEST || b.smashed >= SMASH) {
            return false;
        }
        level.destroyBlock(pos, false, b.by);
        b.smashed++;
        b.v.mul(THROUGH_BLOCK);
        b.at = end;
        return b.v.lengthSquared() >= SPENT * SPENT;
    }

    /** Everyone the ball passes through between {@code from} and {@code to} is struck, once each. */
    private static void strike(ServerLevel level, Ball b, Vec3 from, Vec3 to) {
        for (Entity e : level.getEntities(b.by, new AABB(from, to).inflate(0.5), e -> e instanceof LivingEntity && e.isAlive())) {
            if (b.struck.contains(e.getId()) || e.getBoundingBox().inflate(0.3).clip(from, to).isEmpty()) {
                continue;
            }
            b.struck.add(e.getId());
            double speed = b.v.length() / CannonAim.SPEED;
            e.hurt(level.damageSources().explosion(null, b.by), (float) (b.damage * Math.min(1, speed)));
            b.v.mul(THROUGH_BODY);
        }
    }
}
