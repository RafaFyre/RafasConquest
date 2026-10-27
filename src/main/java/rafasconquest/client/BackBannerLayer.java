package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Draws a banner standing on a soldier's back, its pole rising above the shoulders like a sashimono: a battle's
 * general wears one under the gold helmet. The banner is drawn the way a banner worn on the head is, moved down onto
 * the back.
 */
public class BackBannerLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private final ItemInHandRenderer items;

    public BackBannerLayer(RenderLayerParent<T, M> parent, ItemInHandRenderer items) {
        super(parent);
        this.items = items;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, T entity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack banner = BackBanners.of(entity);
        if (banner == null || entity.isInvisible()) {
            return;
        }
        pose.pushPose();
        getParentModel().body.translateAndRotate(pose);
        // In model space +z is behind the body and +y is down; the head-worn banner sits a quarter block up from the neck.
        pose.translate(0, 0.05, 0.3);
        pose.mulPose(Axis.YP.rotationDegrees(180));
        pose.scale(0.625F, -0.625F, -0.625F);
        items.renderItem(entity, banner, ItemDisplayContext.HEAD, false, pose, buffers, light);
        pose.popPose();
    }
}
