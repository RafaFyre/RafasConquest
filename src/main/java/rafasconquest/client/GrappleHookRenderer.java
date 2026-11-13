package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import rafasconquest.RafasConquest;
import rafasconquest.content.GrappleHook;
import rafasconquest.content.GrappleHooks;

/** A grapple hook: the item where it caught, and its line back to its thrower's hand, drawn as a fishing rod's line is. */
public final class GrappleHookRenderer extends EntityRenderer<GrappleHook> {
    /** Points along a line: as many as a fishing line's. */
    private static final int SEGMENTS = 16;

    private ItemStack hook;

    GrappleHookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(GrappleHook entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light) {
        if (hook == null) {
            hook = new ItemStack(GrappleHooks.ITEM.get());
        }
        poseStack.pushPose();
        Minecraft.getInstance().getItemRenderer().renderStatic(hook, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY,
                poseStack, buffers, entity.level(), entity.getId());
        poseStack.popPose();
        Entity thrower = entity.thrower();
        if (thrower != null) {
            Vec3 hand = GrappleHook.hand(thrower, partialTick);
            Vec3 at = entity.getPosition(partialTick);
            line(poseStack, buffers, (float) (hand.x - at.x), (float) (hand.y - at.y), (float) (hand.z - at.z));
        }
        super.render(entity, yaw, partialTick, poseStack, buffers, light);
    }

    /** A black line from the hook to (dx, dy, dz), sagging toward its low end as a fishing line does. */
    private static void line(PoseStack poseStack, MultiBufferSource buffers, float dx, float dy, float dz) {
        VertexConsumer lines = buffers.getBuffer(RenderType.lineStrip());
        PoseStack.Pose pose = poseStack.last();
        for (int i = 0; i <= SEGMENTS; i++) {
            float t = i / (float) SEGMENTS;
            float next = (i + 1) / (float) SEGMENTS;
            float x = dx * t;
            float y = dy * (t * t + t) * 0.5F;
            float z = dz * t;
            float nx = dx * next - x;
            float ny = dy * (next * next + next) * 0.5F - y;
            float nz = dz * next - z;
            float length = (float) Math.max(1e-6, Math.sqrt(nx * nx + ny * ny + nz * nz));
            lines.addVertex(pose, x, y, z).setColor(0, 0, 0, 255).setNormal(pose, nx / length, ny / length, nz / length);
        }
    }

    @Override
    public boolean shouldRender(GrappleHook entity, Frustum frustum, double camX, double camY, double camZ) {
        return true; // its line reaches past its own small box; there are only a few of them, close to a battle
    }

    @Override
    public ResourceLocation getTextureLocation(GrappleHook entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    @EventBusSubscriber(modid = RafasConquest.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class Registration {
        @SubscribeEvent
        public static void renderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(GrappleHooks.HOOK.get(), GrappleHookRenderer::new);
        }
    }
}
