package rafasconquest.client.cloth;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import rafasconquest.content.BattleStandardItem;
import rafasconquest.content.ConquestContent;

/**
 * Draws battle banner items, the battle standard and the cape as items: in the hand or on a head the cloth moves
 * (each carrier's own); in menus, frames and on the ground it hangs at rest. Standards and big banners are twice the
 * size everywhere but in menus and their carrier's own first-person view.
 */
public class ClothItemRenderer extends BlockEntityWithoutLevelRenderer {
    public ClothItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        if (stack.getItem() instanceof net.minecraft.world.item.BlockItem item && item.getBlock() instanceof rafasconquest.content.FlagBlock flag) {
            var arms = BannerArt.arms(BattleStandardItem.nation(stack));
            LivingEntity holder = context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                    || context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND ? ClothRenderer.current() : null;
            pose.pushPose();
            if (holder != null) {
                // In a hand: upright and life size about the hand whatever the arm does, the pole's foot half a block
                // below it and its top size + 1.7 above, so the cloth flies clear over the holder's head.
                pose.translate(0.5, 0.5, 0.5);
                org.joml.Matrix4f hand = pose.last().pose();
                org.joml.Vector3f at = hand.getTranslation(new org.joml.Vector3f());
                hand.set(ClothRenderer.upright()).setTranslation(at);
                pose.last().normal().set(hand);
                pose.translate(0, -0.5, 0);
                BannerArt.flagOnPole(pose, buffers, light, overlay, flag.size(), flag.size() + 2.2,
                        new ClothRenderer.Carried(holder.getId(), context), arms, holder.position());
            } else {
                // shortcut: in menus, frames, first person and on the ground every flag is a small one at rest, 3/4 size so its pole fits
                pose.translate(0.5, 0, 0.5);
                pose.scale(0.75F, 0.75F, 0.75F);
                BannerArt.flagOnPole(pose, buffers, light, overlay, 1, 3, null, arms, null);
            }
            pose.popPose();
            return;
        }
        boolean cape = stack.is(ConquestContent.CAPE.get());
        boolean standard = stack.is(ConquestContent.BATTLE_STANDARD.get());
        boolean big = standard || stack.is(ConquestContent.BIG_BATTLE_BANNER_ITEM.get());
        boolean carried = context.firstPerson() || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || context == ItemDisplayContext.HEAD;
        LivingEntity carrier = !carried ? null : context.firstPerson() ? Minecraft.getInstance().player : ClothRenderer.current();
        pose.pushPose();
        // Double size would fill the screen in menus and in the player's own hand.
        BannerArt.standing(pose, 0, big && context != ItemDisplayContext.GUI && !context.firstPerson());
        BannerArt.draw(pose, buffers, light, overlay, !cape, !cape,
                carrier == null ? null : new ClothRenderer.Carried(carrier.getId(), context),
                BannerArt.STANDING,
                cape ? BannerArt.plain(DyedItemColor.getOrDefault(stack, DyeColor.WHITE.getTextureDiffuseColor()))
                        : BannerArt.arms(BattleStandardItem.nation(stack)), // a banner handed a nation's arms flies them too
                carrier == null ? null : carrier.position());
        pose.popPose();
    }
}
