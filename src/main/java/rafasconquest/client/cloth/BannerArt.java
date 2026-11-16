package rafasconquest.client.cloth;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * How battle banners, standards and capes look: where their cloth hangs (a vanilla banner's place and size, doubled
 * for the big ones), the pole and bar of vanilla's banner model, and the dye layers of a nation's arms.
 */
public final class BannerArt {
    /** Vanilla's banner model is drawn at 2/3 size, upside down. */
    private static final float MODEL = 2F / 3F;
    private static final double E = 0.02;
    /** The vanilla flag (20 × 40 pixels, a pixel deep) as Physics Mod's 9 × 17 grid, in model units (16 px). */
    private static final double SX = 20 / 16.0 / 8;
    private static final double SY = 40 / 16.0 / 16;
    private static final double DEPTH = -1.5 / 16;
    /** Pole and bar boxes in flag units (the flag hangs 2 units below the model's origin). */
    private static final double[] POLE = {-1 / 16.0 - E, 2 - 30 / 16.0, -1 / 16.0 - E, 1 / 16.0 + E, 2 + 12 / 16.0, 1 / 16.0 + E};
    private static final double[] BAR = {-10 / 16.0 - E, -E, -1 / 16.0 - E, 10 / 16.0 + E, 2 / 16.0 + E, 1 / 16.0 + E};
    /**
     * Wool banner cloth: heavier and stiffer than Physics Mod's silk-like flags (which keep 93% of their speed and take
     * all the wind), so it sways and billows in a few broad folds; a pixel thick.
     */
    public static final ClothRenderer.Fabric BANNER_WOOL = new ClothRenderer.Fabric(0.85, 0.5, 0.6, 0.5, 0.6, 1 / 16.0);
    /** Thick wool for capes: hangs heavy, swings slowly and settles soon, curves without creasing; 0.8 px thick. */
    public static final ClothRenderer.Fabric CAPE_WOOL = new ClothRenderer.Fabric(0.78, 0.25, 0.6, 0.8, 0.35, 0.05);
    public static final ClothRenderer.Kind STANDING = new ClothRenderer.Kind(9, 17, SX, SY, DEPTH, BANNER_WOOL, true, false, POLE, BAR);
    public static final ClothRenderer.Kind WALL = new ClothRenderer.Kind(9, 17, SX, SY, DEPTH, BANNER_WOOL, true, false, BAR);
    /**
     * A cape on a body (model units are blocks, y down, +z behind the back): 10 × 16 pixels like vanilla's, hanging
     * behind the back with its inner face on it, kept out of the body and legs.
     */
    public static final ClothRenderer.Kind CAPE = new ClothRenderer.Kind(9, 13, 10 / 16.0 / 8, 1.0 / 12, 0.16, CAPE_WOOL, false, true,
            new double[]{-0.35, -0.2, -0.25, 0.35, 1.6, 0.155});
    /** The cape over a chestplate: armour stands a pixel off the body (3 px behind the spine), so the cape hangs past it. */
    public static final ClothRenderer.Kind CAPE_ON_ARMOUR = new ClothRenderer.Kind(9, 13, 10 / 16.0 / 8, 1.0 / 12, 0.235, CAPE_WOOL, false, true,
            new double[]{-0.4, -0.2, -0.3, 0.4, 1.6, 0.23});

    @Nullable
    private static ModelPart pole;
    @Nullable
    private static ModelPart bar;
    private static final Map<UUID, List<ClothRenderer.Layer>> ARMS = new HashMap<>();
    @Nullable
    private static Level armsLevel;

    private BannerArt() {
    }

    /** A standing banner's model frame in its block; big ones are scaled up from the block's foot. */
    public static void standing(PoseStack pose, int rotation, boolean big) {
        if (big) {
            pose.translate(0.5, 0, 0.5);
            pose.scale(2, 2, 2);
            pose.translate(-0.5, 0, -0.5);
        }
        pose.translate(0.5, 0.5, 0.5);
        pose.mulPose(Axis.YP.rotationDegrees(-RotationSegment.convertToDegrees(rotation)));
        pose.scale(MODEL, -MODEL, -MODEL);
    }

    /** A wall banner's model frame; big ones are scaled up from the top of the bar, on the wall. */
    public static void wall(PoseStack pose, Direction facing, boolean big) {
        float turn = -facing.toYRot();
        if (big) {
            Vector3f pivot = Axis.YP.rotationDegrees(turn).transform(new Vector3f(0, 0, -0.5F)).add(0.5F, 0.854F, 0.5F);
            pose.translate(pivot.x, pivot.y, pivot.z);
            pose.scale(2, 2, 2);
            pose.translate(-pivot.x, -pivot.y, -pivot.z);
        }
        pose.translate(0.5, -1 / 6.0, 0.5);
        pose.mulPose(Axis.YP.rotationDegrees(turn));
        pose.translate(0, -0.3125, -0.4375);
        pose.scale(MODEL, -MODEL, -MODEL);
    }

    /**
     * Draws the pole (if {@code withPole}) and bar in the model frame, then the cloth below the bar. {@code key}
     * makes the cloth move (null: at rest).
     */
    public static void draw(PoseStack pose, MultiBufferSource buffers, int light, int overlay, boolean withPole, boolean withBar,
                            @Nullable Object key, ClothRenderer.Kind kind, List<ClothRenderer.Layer> layers,
                            @Nullable Vec3 near) {
        if (pole == null) {
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BANNER);
            pole = root.getChild("pole");
            bar = root.getChild("bar");
        }
        if (withPole || withBar) {
            var wood = ModelBakery.BANNER_BASE.buffer(buffers, RenderType::entitySolid);
            if (withPole) {
                pole.render(pose, wood, light, overlay);
            }
            if (withBar) {
                bar.render(pose, wood, light, overlay);
            }
        }
        pose.pushPose();
        pose.translate(0, -2, 0);
        ClothRenderer.draw(key, kind, pose, buffers, light, overlay, layers, near);
        pose.popPose();
    }

    /** A flag's pole half-width in blocks: thicker for the bigger flags. */
    private static double poleRadius(int size) {
        return (1 + size) / 32.0;
    }

    /**
     * Flags, small to huge: the banner's grid in blocks (size across, 2·size along), pinned down the pole and kept
     * out of it, so it wraps round the pole rather than through it. Hung flags (held by a block) have no pole.
     */
    private static final ClothRenderer.Kind[] FLAGS = new ClothRenderer.Kind[4];
    private static final ClothRenderer.Kind[] HUNG_FLAGS = new ClothRenderer.Kind[4];

    static {
        for (int s = 1; s <= 4; s++) {
            double r = poleRadius(s);
            FLAGS[s - 1] = new ClothRenderer.Kind(9, 17, s / 8.0, s / 8.0, 0, BANNER_WOOL, true, false,
                    new double[]{-s * 0.5 - 0.1 - E, -2 * r - E, -r - E, 100, 0, r + E});
            HUNG_FLAGS[s - 1] = new ClothRenderer.Kind(9, 17, s / 8.0, s / 8.0, 0, BANNER_WOOL, true, false);
        }
    }

    /**
     * A flag of {@code size} (1–4) on its own pole {@code poleHeight} blocks tall, the pose at the pole's foot: the
     * cloth flown sideways toward +x from its top (the banner's top edge along the pole).
     */
    public static void flagOnPole(PoseStack pose, MultiBufferSource buffers, int light, int overlay, int size, double poleHeight,
                                  @Nullable Object key, List<ClothRenderer.Layer> layers, @Nullable Vec3 near) {
        if (pole == null) {
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BANNER);
            pole = root.getChild("pole");
            bar = root.getChild("bar");
        }
        double r = poleRadius(size);
        pose.pushPose();
        // Vanilla's pole spans y −30..12 px and 2 px across: stretched from the foot to the top.
        pose.scale((float) (16 * r), (float) (poleHeight / (42 / 16.0)), (float) (16 * r));
        pose.translate(0, 30 / 16.0, 0);
        pole.render(pose, ModelBakery.BANNER_BASE.buffer(buffers, RenderType::entitySolid), light, overlay);
        pose.popPose();
        pose.pushPose();
        pose.translate(r, poleHeight - 0.1, 0);
        flagCloth(pose, buffers, light, overlay, size, FLAGS[size - 1], key, layers, near);
        pose.popPose();
    }

    /** A flag held by a block, no pole: the pose at the top of its pinned edge, the cloth toward +x. */
    public static void flagHung(PoseStack pose, MultiBufferSource buffers, int light, int overlay, int size,
                                @Nullable Object key, List<ClothRenderer.Layer> layers) {
        flagCloth(pose, buffers, light, overlay, size, HUNG_FLAGS[size - 1], key, layers, null);
    }

    /** The cloth from the top of its pinned edge down: the pinned row runs down (local x), the cloth out (local y). */
    private static void flagCloth(PoseStack pose, MultiBufferSource buffers, int light, int overlay, int size, ClothRenderer.Kind kind,
                                  @Nullable Object key, List<ClothRenderer.Layer> layers, @Nullable Vec3 near) {
        pose.pushPose();
        // A hair off the pole's (or holding block's) face: the cloth's pinned rim lay in it and z-fought.
        pose.translate(0.01, -size * 0.5, 0);
        pose.mulPose(Axis.ZP.rotationDegrees(-90));
        ClothRenderer.draw(key, kind, pose, buffers, light, overlay, layers, near);
        pose.popPose();
    }

    /** A nation's arms as dye layers over the cloth (plain white without a nation). */
    public static List<ClothRenderer.Layer> arms(@Nullable UUID nation) {
        Level level = Minecraft.getInstance().level;
        if (level != armsLevel) {
            ARMS.clear();
            armsLevel = level;
        }
        if (nation == null || level == null) {
            return plain(DyeColor.WHITE.getTextureDiffuseColor());
        }
        return ARMS.computeIfAbsent(nation, id -> {
            FactionBanner design = FactionBanner.of(id);
            List<ClothRenderer.Layer> layers = new ArrayList<>(plain(FactionBannerNbt.baseColor(design).getTextureDiffuseColor()));
            BannerPatternLayers patterns = FactionBannerNbt.patterns(level.registryAccess(), design);
            for (BannerPatternLayers.Layer layer : patterns.layers()) {
                layers.add(new ClothRenderer.Layer(Sheets.getBannerMaterial(layer.pattern()), layer.color().getTextureDiffuseColor(), false));
            }
            return List.copyOf(layers);
        });
    }

    /** Bare cloth dyed one colour (RGB). */
    public static List<ClothRenderer.Layer> plain(int rgb) {
        return List.of(new ClothRenderer.Layer(ModelBakery.BANNER_BASE, 0xFFFFFFFF, true),
                new ClothRenderer.Layer(Sheets.BANNER_BASE, 0xFF000000 | rgb, false));
    }
}
