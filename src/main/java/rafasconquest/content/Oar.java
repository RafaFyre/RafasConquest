package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * An oar sticking straight out of its {@link OarBlock}'s side. It is part of the block, so a Sable ship carries it with
 * the hull. The client draws every loaded one at once, instanced like the soldiers ({@code client.OarFigures}).
 */
public class Oar extends BlockEntity {
    /** Shaft length in blocks, handle to blade tip. */
    public static final int LENGTH = 4;
    /** The client's loaded oars, for drawing. Render thread only. */
    public static final Set<Oar> CLIENT_LOADED = Collections.newSetFromMap(new WeakHashMap<>());

    public Oar(BlockPos pos, BlockState state) {
        super(ConquestContent.OAR_ENTITY.get(), pos, state);
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
