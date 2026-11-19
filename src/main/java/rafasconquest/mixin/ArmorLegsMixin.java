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
 * Armour with its own model (Epic Knights) on a wearer whose legs a resource pack re-rigged (Fresh Friends' recruits
 * under Fresh Animations): the pack puts the leg's pivot at the foot and hangs the leg from a child bone at the hip, so
 * the copied pose dropped the armour's legs a block-half below the body. Each armour leg goes where the wearer's leg
 * bone really is instead.
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
        leg.visit(new PoseStack(), (pose, path, index, cube) -> {
            if (bone[0] == null) {
                bone[0] = new Matrix4f(pose.pose());
            }
        });
        if (bone[0] == null) {
            return;
        }
        Vector3f at = bone[0].getTranslation(new Vector3f()).mul(16);
        Vector3f turn = bone[0].getNormalizedRotation(new org.joml.Quaternionf()).getEulerAnglesZYX(new Vector3f());
        armour.x = at.x;
        armour.y = at.y;
        armour.z = at.z;
        armour.xRot = turn.x;
        armour.yRot = turn.y;
        armour.zRot = turn.z;
    }
}
