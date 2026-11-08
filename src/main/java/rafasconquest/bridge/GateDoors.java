package rafasconquest.bridge;

import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.physics.PhysicsPipeline;
import dev.ryanhcode.sable.api.physics.constraint.ConstraintJointAxis;
import dev.ryanhcode.sable.api.physics.constraint.RotaryConstraintConfiguration;
import dev.ryanhcode.sable.api.physics.constraint.RotaryConstraintHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import dev.ryanhcode.sable.sublevel.storage.SubLevelRemovalReason;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import rafasconquest.RafasConquest;
import rafasconquest.content.GateBlockEntity;
import rafasconquest.content.GateGeometry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A Gate Block's doors as Sable ships: each door's blocks are made a sub-level, hinged to the world on a vertical line at
 * its outer edge ({@link GateGeometry}) with no contacts against the world (so it swings through the wall about it, yet
 * still stops people and ships), and its hinge motor is driven to the gate's openness each tick. Only {@link SableLink}
 * calls this.
 */
final class GateDoors {
    private static final String TAG = RafasConquest.MOD_ID + ":gate";
    private static final Vector3d UP = new Vector3d(0, 1, 0);
    /** The hinge motor's pull toward the wanted angle, and its damping (per unit mass: Rapier's motors are acceleration based). */
    private static final double STIFFNESS = 60;
    private static final double DAMPING = 16;
    /** Each door's hinge, while its sub-level is loaded; made again after a reload. */
    private static final Map<ServerSubLevel, RotaryConstraintHandle> joints = new WeakHashMap<>();
    // shortcut: Rapier's joint angle may count the other way round from the door's yaw; it is flipped the first time a
    // door is seen swinging the wrong way. Fix the sign for good once seen in game.
    private static int motorSign = 1;

    private GateDoors() {
    }

    static void tick(ServerLevel level, GateBlockEntity gate) {
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return;
        }
        for (GateBlockEntity.Door door : new GateBlockEntity.Door[]{gate.left, gate.right}) {
            GateGeometry.Hinge hinge = gate.hinge(door);
            if (hinge == null || door.hp <= 0) {
                continue;
            }
            if (door.ship == null) {
                if (gate.swing() == GateBlockEntity.Swing.CLOSED && level.getGameTime() % 20 == 0) {
                    build(level, container, gate, door, hinge);
                }
                continue;
            }
            SubLevel found = container.getSubLevel(door.ship);
            if (found instanceof ServerSubLevel sub && !sub.isRemoved()) {
                drive(container, sub, hinge, gate.openness());
            }
        }
    }

    /** Makes the door's blocks a ship, hinged; nothing if its box is empty or not all loaded. */
    private static void build(ServerLevel level, ServerSubLevelContainer container, GateBlockEntity gate, GateBlockEntity.Door door,
                              GateGeometry.Hinge hinge) {
        net.minecraft.world.level.levelgen.structure.BoundingBox box = gate.world(door.box);
        if (!level.hasChunksAt(box.minX(), box.minZ(), box.maxX(), box.maxZ())) {
            return;
        }
        List<BlockPos> blocks = new ArrayList<>();
        BlockState planks = GateBlockEntity.planks().defaultBlockState();
        for (BlockPos p : BlockPos.betweenClosed(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ())) {
            BlockState state = level.getBlockState(p);
            if (state.is(net.minecraft.world.level.block.Blocks.OAK_PLANKS)) {
                level.setBlock(p, planks, Block.UPDATE_CLIENTS); // oak planks built by hand become Gate Planks, to swing free
            }
            if (!state.isAir()) {
                blocks.add(p.immutable());
            }
        }
        if (blocks.isEmpty()) {
            return;
        }
        ServerSubLevel sub = SubLevelAssemblyHelper.assembleBlocks(level, blocks.get(0), blocks, BoundingBox3i.from(blocks));
        if (sub == null) {
            return;
        }
        Vector3d plotHinge = sub.logicalPose().transformPositionInverse(new Vector3d(hinge.x(), hinge.y(), hinge.z()), new Vector3d());
        CompoundTag tag = new CompoundTag();
        tag.putLong(TAG, gate.getBlockPos().asLong());
        tag.putBoolean("left", door == gate.left);
        tag.putDouble("hx", plotHinge.x);
        tag.putDouble("hy", plotHinge.y);
        tag.putDouble("hz", plotHinge.z);
        sub.setUserDataTag(tag);
        sub.setName("Gate door");
        door.ship = sub.getUniqueId();
        gate.setChanged();
        drive(container, sub, hinge, gate.openness());
    }

    /** Hinges the door if it isn't yet (it was just built, or loaded again), and sets its motor to the angle it should be at. */
    private static void drive(ServerSubLevelContainer container, ServerSubLevel sub, GateGeometry.Hinge hinge, double openness) {
        PhysicsPipeline pipeline = container.physicsSystem().getPipeline();
        RotaryConstraintHandle joint = joints.get(sub);
        if (joint == null || !joint.isValid()) {
            CompoundTag tag = sub.getUserDataTag();
            Vector3d plotHinge = new Vector3d(tag.getDouble("hx"), tag.getDouble("hy"), tag.getDouble("hz"));
            joint = pipeline.addConstraint(sub, null, new RotaryConstraintConfiguration(plotHinge,
                    new Vector3d(hinge.x(), hinge.y(), hinge.z()), UP, UP));
            joint.setContactsEnabled(false);
            joints.put(sub, joint);
        }
        double want = hinge.openYaw() * openness;
        joint.setMotor(ConstraintJointAxis.ANGULAR_X, motorSign * want, STIFFNESS, DAMPING, false, 0);
        if (openness > 0 && openness < 1) {
            pipeline.wakeUp(sub);
            double yaw = yaw(sub.logicalPose().orientation());
            if (Math.abs(want) > 0.6 && yaw * want < -0.2) {
                motorSign = -motorSign;
                RafasConquest.LOGGER.info("Gate doors swung the wrong way round; the hinge motor's sign is flipped");
            }
        }
    }

    private static double yaw(Quaterniondc q) {
        return 2 * Math.atan2(q.y(), q.w());
    }

    /** The door ship a block (in Sable's plot grid) belongs to, or null. */
    @Nullable
    static ServerSubLevel doorAt(ServerLevel level, BlockPos pos) {
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null || !container.inBounds(pos)) {
            return null;
        }
        LevelPlot plot = container.getPlot(new ChunkPos(pos));
        if (plot == null || !(plot.getSubLevel() instanceof ServerSubLevel sub) || sub.isRemoved()) {
            return null;
        }
        CompoundTag tag = sub.getUserDataTag();
        return tag != null && tag.contains(TAG) ? sub : null;
    }

    /** Takes {@code amount} from the door this block belongs to; at 0 the door and all its blocks are gone. True if it is a door. */
    static boolean hit(ServerLevel level, BlockPos pos, double amount) {
        ServerSubLevel sub = doorAt(level, pos);
        if (sub == null) {
            return false;
        }
        CompoundTag tag = sub.getUserDataTag();
        BlockPos gatePos = BlockPos.of(tag.getLong(TAG));
        Vec3 where = sub.logicalPose().transformPosition(Vec3.atCenterOf(pos));
        if (!(level.getBlockEntity(gatePos) instanceof GateBlockEntity gate)) {
            return true; // its Gate Block is gone or not loaded: the door can't be hurt, nor mined
        }
        GateBlockEntity.Door door = tag.getBoolean("left") ? gate.left : gate.right;
        door.hp -= (int) Math.ceil(amount);
        gate.setChanged();
        if (door.hp > 0) {
            level.playSound(null, where.x, where.y, where.z, SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, SoundSource.BLOCKS, 1, 1);
            return true;
        }
        door.hp = 0;
        door.ship = null;
        level.playSound(null, where.x, where.y, where.z, SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.BLOCKS, 1.5f, 1);
        remove(level, sub);
        return true;
    }

    /**
     * New door boxes from the gate's screen: a closed gate's door ships are put back as blocks where they stand, then the
     * doors are made again from the new boxes. Says what was done.
     */
    static String save(GateBlockEntity gate, @Nullable int[] left, @Nullable int[] right) {
        if (gate.swing() != GateBlockEntity.Swing.CLOSED) {
            return "Close the gate before changing its doors";
        }
        if (gate.getLevel() instanceof ServerLevel level) {
            ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
            for (GateBlockEntity.Door door : new GateBlockEntity.Door[]{gate.left, gate.right}) {
                if (container != null && door.ship != null && container.getSubLevel(door.ship) instanceof ServerSubLevel sub && !sub.isRemoved()) {
                    dissolve(level, sub);
                }
                door.ship = null;
            }
        }
        gate.setBoxes(left, right);
        return "Gate doors saved";
    }

    /** Puts a ship's blocks back in the world where it stands, then takes the ship away. */
    private static void dissolve(ServerLevel level, ServerSubLevel sub) {
        BoundingBox3ic b = sub.getPlot().getBoundingBox();
        for (BlockPos p : BlockPos.betweenClosed(b.minX(), b.minY(), b.minZ(), b.maxX(), b.maxY(), b.maxZ())) {
            BlockState state = level.getBlockState(p);
            if (!state.isAir()) {
                BlockPos to = BlockPos.containing(sub.logicalPose().transformPosition(Vec3.atCenterOf(p)));
                if (level.getBlockState(to).canBeReplaced()) {
                    level.setBlock(to, state, Block.UPDATE_ALL);
                }
            }
        }
        remove(level, sub);
    }

    private static void remove(ServerLevel level, ServerSubLevel sub) {
        RotaryConstraintHandle joint = joints.remove(sub);
        if (joint != null && joint.isValid()) {
            joint.remove();
        }
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container != null && !sub.isRemoved()) {
            container.removeSubLevel(sub, SubLevelRemovalReason.REMOVED);
        }
    }
}
