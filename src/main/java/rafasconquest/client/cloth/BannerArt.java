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
