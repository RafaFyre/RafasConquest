package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A {@link SailBlock}'s block entity. It holds nothing saved. On the client, the sheet's top corner block keeps its
 * size and each square's dye, looked up again every second, and every sheet is drawn at once, instanced
 * ({@code client.SailFigures}).
 */
public class Sail extends BlockEntity {
    /** The client's loaded sail blocks, for drawing. Render thread only. */
    public static final Set<Sail> CLIENT_LOADED = Collections.newSetFromMap(new WeakHashMap<>());
    /** Whether a sail block facing alike is to its left or above it (then it is not the corner). */
    public boolean inner;
    /** The corner's sheet: blocks of sail to its right and below it. */
    public int right;
    public int down;
    /** The corner's sheet: each square's dye colour, row by row (0: no sail there). */
    public int[] colours = new int[0];
    /** The game time the sheet was last measured (long ago until it is). */
    public long measured = -1000;
    /** The way it faced when last measured (null until it is). */
    public net.minecraft.core.Direction facing;

    public Sail(BlockPos pos, BlockState state) {
        super(ConquestContent.SAIL_ENTITY.get(), pos, state);
    }

    @Override
    public void onLoad() {
        if (level != null && level.isClientSide) {
            CLIENT_LOADED.add(this);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        CLIENT_LOADED.remove(this);
    }

    @Override
    public void onChunkUnloaded() {
        CLIENT_LOADED.remove(this);
    }
}
