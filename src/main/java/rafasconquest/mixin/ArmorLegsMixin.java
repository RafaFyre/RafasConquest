package rafasconquest.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Armour with its own model (Epic Knights) on a wearer whose legs a resource pack re-rigged (Fresh Friends' recruits,
 * FA+Player's players under Fresh Animations): the leg's pivot is no longer at the hip (Fresh Friends hangs the leg
 * from a child bone; FA+Player keeps the cube off-centre in its bone), so the copied pose put the armour's legs in the
 * wrong place. Each armour leg goes on the wearer's real leg cube instead: its top centre, turned as it is.
 */
@Mixin(HumanoidArmorLayer.class)
public class ArmorLegsMixin {
    @Inject(method = "getArmorModelHook", at = @At("RETURN"), remap = false)
    private void rafasconquest$followRiggedLegs(LivingEntity wearer, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original,
                                               CallbackInfoReturnable<Model> cir) {
        if (cir.getReturnValue() instanceof HumanoidModel<?> armour) {
            HumanoidModel<?> body = ((HumanoidArmorLayer<?, ?, ?>) (Object) this).getParentModel();
            follow(body.rightLeg, armour.rightLeg);
            follow(body.leftLeg, armour.leftLeg);
        }
    }

    /** Only a plain armour leg on a re-rigged one (no cubes of its own): anything else the copied pose already fits. */
    private static void follow(ModelPart leg, ModelPart armour) {
        if (!leg.isEmpty() || armour.isEmpty()) {
            return;
        }
        Matrix4f[] bone = {null};
        Vector3f[] hip = {null};
        leg.visit(new PoseStack(), (pose, path, index, cube) -> {
            if (bone[0] == null) {
                bone[0] = new Matrix4f(pose.pose());
                // The hip is the top centre of the leg's cube: packs place it anywhere in its bone's frame.
                hip[0] = new Vector3f((cube.minX + cube.maxX) / 2, cube.minY, (cube.minZ + cube.maxZ) / 2);
            }
        });
        if (bone[0] == null) {
            return;
        }
        Vector3f at = bone[0].transformPosition(hip[0].div(16)).mul(16);
        Vector3f turn = bone[0].getNormalizedRotation(new org.joml.Quaternionf()).getEulerAnglesZYX(new Vector3f());
        armour.x = at.x;
        armour.y = at.y;
        armour.z = at.z;
        armour.xRot = turn.x;
        armour.yRot = turn.y;
        armour.zRot = turn.z;
    }
}
