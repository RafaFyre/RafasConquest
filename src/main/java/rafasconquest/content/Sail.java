package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A {@link SailBlock}'s block entity. It holds nothing saved. On the client, the sheet's top corner block draws the
 * whole sheet and keeps its size and each square's dye, looked up again every second.
 */
public class Sail extends BlockEntity {
    /** Whether a sail block facing alike is to its left or above it (then it is not the corner). */
    public boolean inner;
    /** The corner's sheet: blocks of sail to its right and below it. */
    public int right;
    public int down;
    /** The corner's sheet: each square's dye colour, row by row (0: no sail there). */
    public int[] colours = new int[0];
    /** The game time the sheet was last measured (long ago until it is). */
    public long measured = -1000;

    public Sail(BlockPos pos, BlockState state) {
        super(ConquestContent.SAIL_ENTITY.get(), pos, state);
    }
}
