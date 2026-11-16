package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import rafasconquest.client.cloth.BannerArt;
import rafasconquest.client.cloth.ClothRenderer;
import rafasconquest.content.BattleStandardItem;
import rafasconquest.content.FlagBlock;

import java.util.HashSet;
import java.util.Set;

/**
 * Flags held by humanoids the game is not drawing this frame (too far, culled, or stood in for by a figure): the flag
 * alone, upright at the hand, out to 512 blocks as placed flags are. Only entities the client has; a soldier the server
 * never sent is a figure, and figures carry no flag. Render thread only.
 */
final class FarFlags {
    private static final double RANGE = 512;
    private static final Set<Integer> DRAWN = new HashSet<>();

    private FarFlags() {
    }

    static void beginFrame() {
        DRAWN.clear();
    }

    /** The game drew this entity (its flag with it) this frame. */
    static void drawn(Entity entity) {
        DRAWN.add(entity.getId());
    }

    static void render(PoseStack pose, Camera camera, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Vec3 cam = camera.getPosition();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        for (Entity e : mc.level.entitiesForRendering()) {
            if (!(e instanceof LivingEntity holder) || DRAWN.contains(e.getId()) || e.isInvisible()
                    || e == camera.getEntity() && !camera.isDetached()) {
                continue;
            }
            Vec3 feet = holder.getPosition(partialTick);
            if (feet.distanceToSqr(cam) > RANGE * RANGE
                    || !(mc.getEntityRenderDispatcher().getRenderer(e) instanceof LivingEntityRenderer<?, ?> r && r.getModel() instanceof HumanoidModel)) {
                continue;
            }
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = holder.getItemInHand(hand);
                if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof FlagBlock flag) {
                    boolean right = (hand == InteractionHand.MAIN_HAND) == (holder.getMainArm() == HumanoidArm.RIGHT);
                    draw(mc, pose, buffers, cam, holder, feet, flag, stack, right, partialTick);
                }
            }
        }
        buffers.endBatch();
    }

    private static void draw(Minecraft mc, PoseStack pose, MultiBufferSource buffers, Vec3 cam, LivingEntity holder, Vec3 feet,
                             FlagBlock flag, ItemStack stack, boolean right, float partialTick) {
        float yaw = Mth.rotLerp(partialTick, holder.yBodyRotO, holder.yBodyRot);
        double turn = Math.toRadians(yaw);
        // The hand: 3/8 block to the holder's right (−cos, −sin of its turn) or left, two fifths of its height up.
        double side = (right ? 0.375 : -0.375) * holder.getScale();
        double x = feet.x - side * Math.cos(turn);
        double y = feet.y + holder.getBbHeight() * 0.4;
        double z = feet.z - side * Math.sin(turn);
        pose.pushPose();
        pose.translate(x - cam.x, y - cam.y, z - cam.z);
        pose.mulPose(Axis.YP.rotationDegrees(180 - yaw)); // as the hand's upright frame is turned
        pose.translate(0, -0.5, 0);
        // The same cloth as in the hand, so it carries on unbroken when the holder stops being drawn.
        var key = new ClothRenderer.Carried(holder.getId(),
                right ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
        BannerArt.flagOnPole(pose, buffers, DistantArmyRenderer.lightAt(mc, x, y, z), OverlayTexture.NO_OVERLAY, flag.size(),
                flag.size() + 2.2, key, BannerArt.arms(BattleStandardItem.nation(stack)), feet);
        pose.popPose();
    }
}
