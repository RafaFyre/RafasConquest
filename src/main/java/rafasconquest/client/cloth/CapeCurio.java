package rafasconquest.client.cloth;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import rafasconquest.content.ConquestContent;

/** The cape in its Curios slot: moving cloth of its dyed colour, hung from the wearer's shoulders. Needs Curios. */
public final class CapeCurio implements ICurioRenderer {
    public static void register() {
        CuriosRendererRegistry.register(ConquestContent.CAPE.get(), CapeCurio::new);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slot, PoseStack pose,
                                                                         RenderLayerParent<T, M> parent, MultiBufferSource buffers, int light,
                                                                         float limbSwing, float limbSwingAmount, float partialTicks,
                                                                         float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity wearer = slot.entity();
        if (wearer.isInvisible() || !(parent.getModel() instanceof HumanoidModel<?> model)) {
            return;
        }
        pose.pushPose();
        model.body.translateAndRotate(pose);
        ClothRenderer.draw(new ClothRenderer.Carried(wearer.getId(), "cape"), BannerArt.CAPE, pose, buffers, light,
                OverlayTexture.NO_OVERLAY, BannerArt.plain(DyedItemColor.getOrDefault(stack, DyeColor.WHITE.getTextureDiffuseColor())),
                wearer.position());
        pose.popPose();
    }
}
