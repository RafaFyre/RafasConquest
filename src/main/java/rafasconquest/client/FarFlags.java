package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
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
import rafasconquest.battle.FormationSnapshot;
import net.minecraft.core.BlockPos;
import rafasconquest.client.cloth.BannerArt;
import rafasconquest.client.cloth.BattleBannerRenderer;
import rafasconquest.content.BattleBannerBlockEntity;
import rafasconquest.content.PlacedBanners;
import rafasconquest.client.cloth.ClothRenderer;
import rafasconquest.content.BattleStandardItem;
import rafasconquest.content.FlagBlock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Flags held by humanoids the game is not drawing this frame (too far, culled, or stood in for by a figure): the flag
 * alone, upright at the hand, out to 512 blocks as placed flags are. A soldier past the server's tracking range is no
 * entity here at all, only a battle figure: the flag it was last seen holding is drawn on its figure. Placed banners and
 * flags past the chunks the game draws are drawn from the server's list of them ({@link PlacedBanners}). Render thread only.
 */
public final class FarFlags {
    private static final double RANGE = 512;
    private static final Set<Integer> DRAWN = new HashSet<>();
    /** Each flag held, by its holder's entity id, kept after the holder leaves the client. */
    private static final Map<Integer, Held> HELD = new HashMap<>();
    private static ClientLevel heldIn;
    private static List<FormationSnapshot> formations = List.of();
    private static List<List<FigureMotion.Placed>> placed = List.of();

    private record Held(ItemStack stack, FlagBlock flag, boolean right) {
    }

    private FarFlags() {
    }

    /** The placed banners and flags the server last told of, about the camera. */
    private static List<BlockPos> bannersAt = List.of();
    private static List<PlacedBanners.Banner> banners = List.of();

    public static void banners(List<BlockPos> where, List<PlacedBanners.Banner> what) {
        bannersAt = where;
        banners = what;
    }

    static void beginFrame() {
        DRAWN.clear();
        BattleBannerRenderer.DRAWN.clear();
        formations = List.of();
        placed = List.of();
    }

    /** The game drew this entity (its flag with it) this frame. */
    static void drawn(Entity entity) {
        DRAWN.add(entity.getId());
    }

    /** This frame's battle figures: each formation's, in its soldiers' order. */
    static void figures(List<FormationSnapshot> formations, List<List<FigureMotion.Placed>> placed) {
        FarFlags.formations = formations;
        FarFlags.placed = placed;
    }

    static void render(PoseStack pose, Camera camera, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        if (heldIn != mc.level) {
            heldIn = mc.level;
            HELD.clear();
            banners(List.of(), List.of()); // another world or dimension: the server tells anew within a second
        }
        Vec3 cam = camera.getPosition();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        for (Entity e : mc.level.entitiesForRendering()) {
            if (!(e instanceof LivingEntity holder) || e.isInvisible() || e == camera.getEntity() && !camera.isDetached()) {
                continue;
            }
            Vec3 feet = holder.getPosition(partialTick);
            if (feet.distanceToSqr(cam) > RANGE * RANGE
                    || !(mc.getEntityRenderDispatcher().getRenderer(e) instanceof LivingEntityRenderer<?, ?> r && r.getModel() instanceof HumanoidModel)) {
                continue;
            }
            HELD.remove(e.getId());
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = holder.getItemInHand(hand);
                if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof FlagBlock flag) {
                    Held held = new Held(stack, flag, (hand == InteractionHand.MAIN_HAND) == (holder.getMainArm() == HumanoidArm.RIGHT));
                    HELD.put(e.getId(), held);
                    if (!DRAWN.contains(e.getId())) {
                        draw(mc, pose, buffers, cam, e.getId(), feet, Mth.rotLerp(partialTick, holder.yBodyRotO, holder.yBodyRot),
                                holder.getBbHeight(), holder.getScale(), 1, held);
                    }
                    break; // shortcut: one flag a holder (the main hand's first), upgrade if anyone carries two
                }
            }
        }
        // Holders the client no longer has: their flag on their figure.
        double renderBlocks = mc.options.getEffectiveRenderDistance() * 16.0;
        for (int f = 0; f < formations.size() && f < placed.size(); f++) {
            FormationSnapshot formation = formations.get(f);
            List<FigureMotion.Placed> figures = placed.get(f);
            if (figures.size() != formation.alive()) {
                continue; // a figure fell out of the ranks: its order no longer follows its soldiers'
            }
            int[] ids = formation.soldierIds();
            for (int i = 0; i < ids.length && i < figures.size(); i++) {
                Held held = HELD.get(ids[i]);
                if (held == null || mc.level.getEntity(ids[i]) != null) {
                    continue;
                }
                FigureMotion.Placed p = figures.get(i);
                double distance = Math.sqrt(Mth.lengthSquared(p.x() - cam.x, p.y() - cam.y, p.z() - cam.z));
                if (distance > RANGE) {
                    continue;
                }
                // Pulled in toward the eye and shrunk as its figure is (DistantDepth), unless the distant pass is on.
                double s = DistantOcclusion.scale(distance, renderBlocks);
                Vec3 feet = new Vec3(cam.x + (p.x() - cam.x) * s, cam.y + (p.y() - cam.y) * s, cam.z + (p.z() - cam.z) * s);
                float yaw = (float) Math.toDegrees(Math.atan2(-p.fx(), p.fz()));
                draw(mc, pose, buffers, cam, ids[i], feet, yaw, 1.8F, 1, (float) s, held);
            }
        }
        // Placed ones past the chunks the game draws (or has): as their block entity would draw them.
        for (int i = 0; i < bannersAt.size(); i++) {
            BlockPos at = bannersAt.get(i);
            PlacedBanners.Banner banner = banners.get(i);
            if (BattleBannerRenderer.DRAWN.contains(at.asLong())
                    || mc.level.hasChunkAt(at) && mc.level.getBlockState(at).getBlock() != banner.state().getBlock()) {
                continue; // drawn by the game, or gone since the server last told
            }
            double distance = Math.sqrt(at.getCenter().distanceToSqr(cam));
            if (distance > PlacedBanners.RANGE) {
                continue;
            }
            double s = DistantOcclusion.scale(distance, renderBlocks);
            pose.pushPose();
            pose.translate((at.getX() - cam.x) * s, (at.getY() - cam.y) * s, (at.getZ() - cam.z) * s);
            pose.scale((float) s, (float) s, (float) s);
            Object key = mc.level.getBlockEntity(at) instanceof BattleBannerBlockEntity loaded ? loaded : at;
            BattleBannerRenderer.draw(banner.state(), key, banner.nation(), pose, buffers,
                    DistantArmyRenderer.lightAt(mc, at.getX() + 0.5, at.getY(), at.getZ() + 0.5), OverlayTexture.NO_OVERLAY);
            pose.popPose();
        }
        buffers.endBatch();
    }

    private static void draw(Minecraft mc, PoseStack pose, MultiBufferSource buffers, Vec3 cam, int holder, Vec3 feet, float yaw,
                             float height, float size, float shrink, Held held) {
        double turn = Math.toRadians(yaw);
        // The hand: 3/8 block to the holder's right (−cos, −sin of its turn) or left, two fifths of its height up.
        double side = (held.right() ? 0.375 : -0.375) * size * shrink;
        double x = feet.x - side * Math.cos(turn);
        double y = feet.y + height * 0.4 * shrink;
        double z = feet.z - side * Math.sin(turn);
        pose.pushPose();
        pose.translate(x - cam.x, y - cam.y, z - cam.z);
        pose.scale(shrink, shrink, shrink);
        pose.mulPose(Axis.YP.rotationDegrees(180 - yaw)); // as the hand's upright frame is turned
        pose.translate(0, -0.5, 0);
        // The same cloth as in the hand, so it carries on unbroken when the holder stops being drawn.
        var key = new ClothRenderer.Carried(holder,
                held.right() ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
        BannerArt.flagOnPole(pose, buffers, DistantArmyRenderer.lightAt(mc, x, y, z), OverlayTexture.NO_OVERLAY, held.flag().size(),
                held.flag().size() + 2.2, key, BannerArt.arms(BattleStandardItem.nation(held.stack())), feet);
        pose.popPose();
    }
}
