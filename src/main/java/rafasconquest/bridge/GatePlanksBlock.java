package rafasconquest.bridge;

import dev.ryanhcode.sable.api.block.BlockWithSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import org.joml.Vector3d;

/**
 * Gate Planks: oak planks for gate doors. On a door ship they touch nothing but other ships: not the wall about the
 * gate, nor the other door, so the doors swing through both. People are still stopped by them (that is Minecraft's own
 * collision, not the physics). Registered only with Sable installed.
 */
public class GatePlanksBlock extends Block implements BlockWithSubLevelCollisionCallback {
    private static final BlockSubLevelCollisionCallback.CollisionResult PASS =
            new BlockSubLevelCollisionCallback.CollisionResult(new Vector3d(), true);
    private static final BlockSubLevelCollisionCallback CALLBACK = (self, other, point, speed) -> {
        SubLevelPhysicsSystem system = SubLevelPhysicsSystem.getCurrentlySteppingSystem();
        ServerLevel level = system == null ? null : system.getLevel();
        if (level == null || other == null) {
            return PASS;
        }
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        // Only another ship's blocks (in Sable's plot grid) that aren't a gate door stop it.
        boolean ship = container != null && container.inBounds(other) && GateDoors.doorAt(level, other) == null;
        return ship ? BlockSubLevelCollisionCallback.CollisionResult.NONE : PASS;
    };

    public GatePlanksBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockSubLevelCollisionCallback sable$getCallback() {
        return CALLBACK;
    }
}
