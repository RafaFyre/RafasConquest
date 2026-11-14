package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A {@link SailBlock}'s square of its sheet. It holds nothing saved; the client keeps where the square sits in its sheet
 * (blocks of sail to its left, right, above and below), looked up again every second.
 */
public class Sail extends BlockEntity {
    public int left;
    public int right;
    public int up;
    public int down;
    /** The game time the sheet was last measured (long ago until it is). */
    public long measured = -1000;

    public Sail(BlockPos pos, BlockState state) {
        super(ConquestContent.SAIL_ENTITY.get(), pos, state);
    }
}
