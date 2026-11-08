package rafasconquest.bridge;

import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.storage.SubLevelRemovalReason;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import rafasconquest.RafasConquest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A siege's carried ladders and rolling towers as real Sable frames: each a sub-level built of blocks (a ladder on its
 * rail, laid flat on its carriers' shoulders; a wedge-backed tower of planks, hovering), glided each tick to where it should be.
 * Frames no one asks for are taken away. Only {@link SableLink} calls this.
 */
final class SiegeFrames {
    private static final String TAG = RafasConquest.MOD_ID + ":siege_frame";
    /** Each tick a frame closes this share of the way to where it should be. */
    private static final double GLIDE = 0.15;
    /** Frames are put together this high over where they are wanted, clear of the soldiers, then brought down. */
    private static final int BUILT_ABOVE = 24;
    private static final int LADDER = 6;

    private static final Map<String, ServerSubLevel> frames = new HashMap<>();

    private SiegeFrames() {
    }

    static void tick(ServerLevel level, List<SableLink.Frame> wanted) {
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return;
        }
        frames.values().removeIf(ServerSubLevel::isRemoved);
        for (ServerSubLevel sub : container.getAllSubLevels()) {
            CompoundTag tag = sub.getUserDataTag();
            if (tag != null && tag.contains(TAG) && !sub.isRemoved()) {
                ServerSubLevel tracked = frames.putIfAbsent(tag.getString(TAG), sub);
                if (tracked != null && tracked != sub) {
                    container.removeSubLevel(sub, SubLevelRemovalReason.REMOVED); // never two for one strategem
                }
            }
        }
        Set<String> keys = wanted.stream().map(SableLink.Frame::key).collect(Collectors.toSet());
        for (Map.Entry<String, ServerSubLevel> e : new HashMap<>(frames).entrySet()) {
            if (!keys.contains(e.getKey())) {
                frames.remove(e.getKey());
                container.removeSubLevel(e.getValue(), SubLevelRemovalReason.REMOVED);
            }
        }
        for (SableLink.Frame f : wanted) {
            ServerSubLevel sub = frames.get(f.key());
            if (sub == null) {
                if (level.getGameTime() % 20 == Math.floorMod(f.key().hashCode(), 20)) { // a try a second each
                    sub = build(level, f);
                    if (sub != null) {
                        frames.put(f.key(), sub);
                        glide(sub, f, 1);
                    }
                }
            } else {
                glide(sub, f, GLIDE);
            }
        }
    }

    /** Puts the frame's blocks up in the air over its place and makes them a sub-level; null if there is no room there yet. */
    private static ServerSubLevel build(ServerLevel level, SableLink.Frame f) {
        BlockPos base = BlockPos.containing(f.x(), f.y() + BUILT_ABOVE, f.z());
        Map<BlockPos, BlockState> parts = f.tower()
                ? SiegeWorks.towerBlocks(Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH)) : ladder();
        List<BlockPos> blocks = new ArrayList<>();
        for (BlockPos p : parts.keySet()) {
            BlockPos at = base.offset(p);
            if (!level.hasChunkAt(at) || !level.getBlockState(at).isAir() || at.getY() >= level.getMaxBuildHeight()) {
                return null;
            }
            blocks.add(at);
        }
        // Solid parts first, ladders after, without neighbour updates (a ladder pops off without its rail).
        parts.forEach((p, s) -> {
            if (!(s.getBlock() instanceof LadderBlock)) {
                level.setBlock(base.offset(p), s, Block.UPDATE_CLIENTS);
            }
        });
        parts.forEach((p, s) -> {
            if (s.getBlock() instanceof LadderBlock) {
                level.setBlock(base.offset(p), s, Block.UPDATE_CLIENTS);
            }
        });
        ServerSubLevel sub = SubLevelAssemblyHelper.assembleBlocks(level, base, blocks, BoundingBox3i.from(blocks));
        if (sub == null) {
            blocks.forEach(p -> level.removeBlock(p, false));
            return null;
        }
        CompoundTag tag = new CompoundTag();
        tag.putString(TAG, f.key());
        sub.setUserDataTag(tag);
        sub.setName(f.tower() ? "Siege tower" : "Siege ladder");
        return sub;
    }

    /** A ladder on a rail, built standing (its rungs to +z), to be laid flat. */
    private static Map<BlockPos, BlockState> ladder() {
        Map<BlockPos, BlockState> parts = new LinkedHashMap<>();
        BlockState rail = Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState();
        BlockState rungs = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH);
        for (int y = 0; y < LADDER; y++) {
            parts.put(new BlockPos(0, y, 0), rail);
            parts.put(new BlockPos(0, y, 1), rungs);
        }
        return parts;
    }

    /** Moves the frame {@code share} of the way to where it is wanted, turned to its heading, and stills it there. */
    private static void glide(ServerSubLevel sub, SableLink.Frame f, double share) {
        RigidBodyHandle body = RigidBodyHandle.of(sub);
        if (body == null || !body.isValid()) {
            return;
        }
        Pose3dc pose = sub.logicalPose();
        BoundingBox3ic box = sub.getPlot().getBoundingBox();
        // Its middle at the bottom (a tower) or right in the middle (a ladder, laid flat), in the world.
        Vector3d middle = pose.transformPosition(new Vector3d((box.minX() + box.maxX() + 1) / 2.0,
                f.tower() ? box.minY() : (box.minY() + box.maxY() + 1) / 2.0, (box.minZ() + box.maxZ() + 1) / 2.0));
        Quaterniond want = new Quaterniond().rotationY(f.yaw());
        if (!f.tower()) {
            want.rotateX(Math.PI / 2); // laid flat, along its heading
        }
        Vector3d to = new Vector3d(pose.position().x() + (f.x() - middle.x) * share, pose.position().y() + (f.y() - middle.y) * share,
                pose.position().z() + (f.z() - middle.z) * share);
        body.teleport(to, new Quaterniond(pose.orientation()).slerp(want, Math.min(1, share * 2)));
        body.addLinearAndAngularVelocity(body.getLinearVelocity(new Vector3d()).negate(), body.getAngularVelocity(new Vector3d()).negate());
    }
}
