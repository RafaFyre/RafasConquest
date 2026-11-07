package rafasconquest.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

/**
 * Recruits' arrow cleaner looks for arrows inside the world border's box, which Sable makes endless and then refuses to
 * search (logging an error each time), so stuck arrows were never cleared. This lists every loaded arrow without a box.
 */
@Pseudo
@Mixin(targets = "com.talhanation.recruits.RecruitEvents", remap = false)
public class RecruitsArrowCleanerMixin {
    @Redirect(method = "onWorldTickArrowCleaner", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    private <T extends Entity> List<T> rafasconquest$allArrows(Level level, Class<T> type, AABB bounds) {
        if (level instanceof ServerLevel server && type == AbstractArrow.class) {
            return new ArrayList<>(server.getEntities(EntityTypeTest.forClass(type), entity -> true));
        }
        return level.getEntitiesOfClass(type, bounds);
    }
}
