package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * A Gate Block's two doors: the box of blocks each is made of (relative to the block), the Sable ship each became, its
 * hit points, and whether the gate is closed, swinging open, open or swinging closed.
 */
public class GateBlockEntity extends BlockEntity {
    public static final int HP = 1000;
    /** Ticks a swing takes. */
    public static final int SWING = 60;

    public enum Swing { CLOSED, OPENING, OPEN, CLOSING }

    public static final class Door {
        /** {x0, y0, z0, x1, y1, z1}, inclusive, relative to the Gate Block; null: no door. */
        @Nullable
        public int[] box;
        @Nullable
        public UUID ship;
        public int hp = HP;

        void save(CompoundTag tag) {
            if (box != null) {
                tag.putIntArray("box", box);
            }
            if (ship != null) {
                tag.putUUID("ship", ship);
            }
            tag.putInt("hp", hp);
        }

        void load(CompoundTag tag) {
            box = tag.contains("box") ? tag.getIntArray("box") : null;
            if (box != null && box.length != 6) {
                box = null;
            }
            ship = tag.hasUUID("ship") ? tag.getUUID("ship") : null;
            hp = tag.contains("hp") ? tag.getInt("hp") : HP;
        }
    }

    public final Door left = new Door();
    public final Door right = new Door();
    private Swing swing = Swing.CLOSED;
    private int swingTick;

    public GateBlockEntity(BlockPos pos, BlockState state) {
        super(ConquestContent.GATE_ENTITY.get(), pos, state);
    }

    public Swing swing() {
        return swing;
    }

    /** How far open, 0 (closed) to 1 (open), eased at both ends. */
    public double openness() {
        double t = switch (swing) {
            case CLOSED -> 0;
            case OPEN -> 1;
            case OPENING -> swingTick / (double) SWING;
            case CLOSING -> 1 - swingTick / (double) SWING;
        };
        return t * t * (3 - 2 * t);
    }

    /** A redstone pulse: a closed gate swings open, an open one swings closed; one already swinging carries on. */
    public void pulse() {
        if (swing == Swing.CLOSED || swing == Swing.OPEN) {
            swing = swing == Swing.CLOSED ? Swing.OPENING : Swing.CLOSING;
            swingTick = 0;
            setChanged();
        }
    }

    public Direction facing() {
        return getBlockState().getValue(GateBlock.FACING);
    }

    /** The door's hinge, or null with no box. */
    @Nullable
    public GateGeometry.Hinge hinge(Door door) {
        Door other = door == left ? right : left;
        return door.box == null ? null : GateGeometry.hinge(worldPosition, facing(), door.box, other.box);
    }

    /** New boxes (Save in the gate's screen): the doors are rebuilt from what stands in them, at full strength. */
    public void setBoxes(@Nullable int[] leftBox, @Nullable int[] rightBox) {
        left.box = leftBox;
        right.box = rightBox;
        left.hp = HP;
        right.hp = HP;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    static void serverTick(Level level, BlockPos pos, BlockState state, GateBlockEntity gate) {
        if (gate.swing == Swing.OPENING || gate.swing == Swing.CLOSING) {
            if (++gate.swingTick >= SWING) {
                gate.swing = gate.swing == Swing.OPENING ? Swing.OPEN : Swing.CLOSED;
                gate.swingTick = 0;
            }
            gate.setChanged();
        }
        if (level instanceof ServerLevel server) {
            rafasconquest.bridge.SableLink.gate(server, gate);
        }
    }

    /**
     * Sets up a gate as built: oak doors filling {@code leftDoor} and {@code rightDoor} (world boxes), and a Gate Block at
     * {@code at} facing {@code facing} (the inside), whose boxes are set to them. Only blocks inside {@code within} are
     * placed (a chunk, in world generation).
     */
    public static void install(LevelAccessor level, BoundingBox within, BlockPos at, Direction facing, BoundingBox leftDoor, BoundingBox rightDoor) {
        BlockState planks = planks().defaultBlockState();
        for (BoundingBox door : new BoundingBox[]{leftDoor, rightDoor}) {
            BlockPos.betweenClosedStream(door).filter(within::isInside).forEach(p -> level.setBlock(p, planks, 2));
        }
        if (!within.isInside(at)) {
            return;
        }
        level.setBlock(at, ConquestContent.GATE_BLOCK.get().defaultBlockState().setValue(GateBlock.FACING, facing), 2);
        if (level.getBlockEntity(at) instanceof GateBlockEntity gate) {
            gate.left.box = relative(at, leftDoor);
            gate.right.box = relative(at, rightDoor);
            gate.setChanged();
        }
    }

    /** What doors are made of: Gate Planks (which swing through walls) with Sable, else oak planks. */
    public static net.minecraft.world.level.block.Block planks() {
        return ConquestContent.GATE_PLANKS != null ? ConquestContent.GATE_PLANKS.get() : Blocks.OAK_PLANKS;
    }

    private static int[] relative(BlockPos at, BoundingBox box) {
        return new int[]{box.minX() - at.getX(), box.minY() - at.getY(), box.minZ() - at.getZ(),
                box.maxX() - at.getX(), box.maxY() - at.getY(), box.maxZ() - at.getZ()};
    }

    /** A box relative to the gate, in the world. */
    public BoundingBox world(int[] box) {
        return new BoundingBox(worldPosition.getX() + box[0], worldPosition.getY() + box[1], worldPosition.getZ() + box[2],
                worldPosition.getX() + box[3], worldPosition.getY() + box[4], worldPosition.getZ() + box[5]);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag l = new CompoundTag();
        CompoundTag r = new CompoundTag();
        left.save(l);
        right.save(r);
        tag.put("left", l);
        tag.put("right", r);
        tag.putString("swing", swing.name());
        tag.putInt("swingTick", swingTick);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        left.load(tag.getCompound("left"));
        right.load(tag.getCompound("right"));
        try {
            swing = Swing.valueOf(tag.getString("swing"));
        } catch (IllegalArgumentException e) {
            swing = Swing.CLOSED;
        }
        swingTick = tag.getInt("swingTick");
    }

    @Override
    public CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
