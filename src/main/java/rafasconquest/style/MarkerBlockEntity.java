package rafasconquest.style;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rafasconquest.content.ConquestContent;

/** A Character Marker's settings ({@link MarkerSpec}), kept with the block (and in a structure saved with it). */
public class MarkerBlockEntity extends BlockEntity {
    /** Ticks an armed marker waits before it fires, and between tries while it waits on something. */
    private static final int SETTLE = 40;
    /** How long it keeps trying before it gives up on another marker of its build it refers to. */
    static final int PATIENCE = 20 * 20;

    private MarkerSpec spec = MarkerSpec.DEFAULT;
    private int age;

    public MarkerBlockEntity(BlockPos pos, BlockState state) {
        super(ConquestContent.MARKER_ENTITY.get(), pos, state);
    }

    public MarkerSpec spec() {
        return spec;
    }

    public void setSpec(MarkerSpec spec) {
        this.spec = spec;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    static void serverTick(Level level, BlockPos pos, BlockState state, MarkerBlockEntity marker) {
        marker.age++;
        if (marker.age < SETTLE || marker.age % SETTLE != 0 || !(level instanceof ServerLevel server)) {
            return;
        }
        CharacterMarkers.fire(server, pos, state, marker.spec, marker.age);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("marker", spec.save());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        spec = MarkerSpec.load(tag.getCompound("marker"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
