package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An oar: four oak fences for its shaft and two open oak trapdoors for its blade, drawn from vanilla's own block models
 * straight out of its {@link OarBlock}'s side. It is part of the block, so a Sable ship carries it with the hull.
 */
public class Oar extends BlockEntity {
    /** Shaft length in blocks, handle to blade tip. */
    public static final int LENGTH = 4;

    public Oar(BlockPos pos, BlockState state) {
        super(ConquestContent.OAR_ENTITY.get(), pos, state);
    }
}
