package rafasconquest.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rafasconquest.client.cloth.EpicKnightsCloaks;

/** Epic Knights: Addon's cloaks hang as our moving cloth: drawn here, and of their stiff box only the top over the shoulders. */
@Pseudo
@Mixin(targets = "com.magistuarmory.client.render.entity.layer.ArmorDecorationLayer", remap = false)
public class EpicKnightsCloakMixin {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At("HEAD"))
    private void rafasconquest$cloakCloth(PoseStack pose, MultiBufferSource buffers, int light, LivingEntity wearer,
                                          float a, float b, float c, float d, float e, float f, CallbackInfo ci) {
        if (((RenderLayer<?, ?>) (Object) this).getParentModel() instanceof HumanoidModel<?> model) {
            EpicKnightsCloaks.draw(model, pose, buffers, light, wearer);
        }
    }

    @Inject(method = "renderDecoration(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IIIZ[Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/resources/ResourceLocation;)V",
            at = @At("HEAD"), cancellable = true)
    private void rafasconquest$noStiffCloak(PoseStack pose, MultiBufferSource buffers, int light, int overlay, int colour, boolean foil,
                                           ModelPart[] parts, ResourceLocation texture, CallbackInfo ci) {
        if (EpicKnightsCloaks.isCloth(texture)) {
            EpicKnightsCloaks.drawTop(pose, buffers, light, overlay, colour, foil, parts, texture);
            ci.cancel();
        }
    }
}
