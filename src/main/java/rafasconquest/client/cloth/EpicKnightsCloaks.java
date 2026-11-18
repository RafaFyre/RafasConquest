package rafasconquest.client.cloth;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.CustomData;
import org.joml.Matrix4d;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Epic Knights: Addon's medieval cloaks (plain and with a cross), decorations on a chestplate, drawn as moving cape
 * cloth in their own texture and dye instead of Epic Knights' stiff box. Read from the chestplate's decoration data, so
 * nothing here needs Epic Knights' classes.
 */
public final class EpicKnightsCloaks {
    private static final ResourceLocation DECORATIONS = ResourceLocation.fromNamespaceAndPath("magistuarmory", "armor_decoration");
    /**
     * The addon's cloak is MedievalCloakModel's body box: 16 × 22 × 4 pixels from texture 9, 16, grown 1.2. Its sides
     * run unbroken along the texture: left side u 9–13 (back to front), front 13–29 (left to right), right side 29–33,
     * back 33–49 (right to left). Its front is clear but for a 3-pixel flap at each edge (u 13–16, 26–29), so the cloth
     * runs from the right flap's edge (u 26) round the back to the left flap's edge (u 16), seamed at the back left
     * corner where the texture jumps from u 49 to 9. Only its top over the shoulders stays stiff; its arm boxes are
     * clear on the texture, so they are left out.
     */
    private static final float GROW = 1.2F;
    /** The cloth stands half a pixel further out than the box, so its thickness clears a chestplate's back. */
    private static final double OUT = GROW + 0.5;
    private static final float V0 = 20, V1 = 42;
    private static final float[] US = new float[(49 - 26 + 1) + (16 - 9 + 1)];
    private static final ClothRenderer.Kind CLOAK;

    static {
        double x0 = -8 - OUT, x1 = 8 + OUT, z0 = -2 - OUT, z1 = 2 + OUT;
        int c = 0;
        for (int t = 26; t <= 49; t++) {
            US[c++] = t;
        }
        for (int t = 9; t <= 16; t++) {
            US[c++] = t;
        }
        double[] xs = new double[US.length];
        double[] zs = new double[US.length];
        for (c = 0; c < US.length; c++) {
            float t = US[c];
            double x, z;
            if (c >= 49 - 26 + 1 && t <= 13) { // left side, back to front (after the seam)
                x = x0;
                z = z1 - (t - 9) / 4 * (z1 - z0);
            } else if (t <= 29) { // front, left to right
                x = x0 + (t - 13) / 16 * (x1 - x0);
                z = z0;
            } else if (t <= 33) { // right side, front to back
                x = x1;
                z = z0 + (t - 29) / 4 * (z1 - z0);
            } else { // back, right to left
                x = x1 - (t - 33) / 16 * (x1 - x0);
                z = z1;
            }
            xs[c] = x / 16;
            zs[c] = z / 16;
        }
        // 12 rows down the box's 22 pixels grown; it stays out of the body and legs (in armour), inside the wrap
        CLOAK = ClothRenderer.Kind.around(12, xs, zs, (22 + 2 * GROW) / 16 / 11, BannerArt.CAPE_WOOL, true,
                new double[]{-0.5, -0.1, -0.19, 0.5, 1.6, 0.19});
    }

    /** The box's top over the shoulders, the one face that stays stiff (DOWN is the top: model y grows downward). */
    private static final ModelPart.Cube TOP = new ModelPart.Cube(9, 16, -8, 0, -2, 16, 22, 4, (float) OUT, GROW, (float) OUT,
            false, 64, 64, EnumSet.of(Direction.DOWN));

    private EpicKnightsCloaks() {
    }

    /** Draws the cloak's stiff top on the decoration model's body ({@code parts}: head, body, arms). */
    public static void drawTop(PoseStack pose, MultiBufferSource buffers, int light, int overlay, int argb, boolean foil,
                                 ModelPart[] parts, ResourceLocation texture) {
        pose.pushPose();
        parts[1].translateAndRotate(pose);
        TOP.compile(pose.last(), ItemRenderer.getArmorFoilBuffer(buffers, RenderType.entityCutoutNoCull(texture), foil), light, overlay, argb);
        pose.popPose();
    }

    /** Whether Epic Knights' decoration texture is a cloak this draws as cloth (its base or its overlay). */
    public static boolean isCloth(ResourceLocation texture) {
        return texture.getNamespace().equals("magistuarmoryaddon") && texture.getPath().contains("medieval_cloak");
    }

    /** Draws the cloak decorating {@code wearer}'s chestplate, if any, from the body of {@code model}. */
    public static void draw(HumanoidModel<?> model, PoseStack pose, MultiBufferSource buffers, int light, LivingEntity wearer) {
        if (wearer.isInvisible()) {
            return;
        }
        List<ClothRenderer.Layer> layers = layers(wearer);
        if (layers.isEmpty()) {
            return;
        }
        pose.pushPose();
        model.body.translateAndRotate(pose);
        pose.translate(0, -GROW / 16, 0); // hung from the box's top, under the stiff shoulders
        ClothRenderer.Carried key = new ClothRenderer.Carried(wearer.getId(), "ek_cloak");
        ClothRenderer.draw(key, CLOAK, pose, buffers, light, OverlayTexture.NO_OVERLAY, layers, wearer.position());
        pose.popPose();
        ClothRenderer.limbs(key, ARMS, toArms(model));
    }

    /**
     * Each arm in armour (4 × 12 pixels from 2 above its shoulder pivot, grown a pixel) and half a pixel more for the
     * cloth's thickness, in the arm's own units: the side cloth stays out of an arm as it swings.
     */
    private static final double[][] ARMS = {
            {-4.5 / 16, -3.5 / 16, -3.5 / 16, 2.5 / 16, 11.5 / 16, 3.5 / 16},
            {-2.5 / 16, -3.5 / 16, -3.5 / 16, 4.5 / 16, 11.5 / 16, 3.5 / 16}};

    /** From the cloth's units (the body's, hung from the box's top) to each arm's (right, left), as the model stands now. */
    private static Matrix4d[] toArms(HumanoidModel<?> model) {
        PoseStack at = new PoseStack();
        model.body.translateAndRotate(at);
        at.translate(0, -GROW / 16, 0);
        Matrix4d cloth = new Matrix4d(at.last().pose());
        ModelPart[] arms = {model.rightArm, model.leftArm};
        Matrix4d[] to = new Matrix4d[arms.length];
        for (int k = 0; k < arms.length; k++) {
            PoseStack arm = new PoseStack();
            arms[k].translateAndRotate(arm);
            to[k] = new Matrix4d(arm.last().pose()).invert().mul(cloth);
        }
        return to;
    }

    /** The first cloak on the chestplate: its texture in its dye, then its undyed overlay (the cross). */
    private static List<ClothRenderer.Layer> layers(LivingEntity wearer) {
        DataComponentType<?> type = BuiltInRegistries.DATA_COMPONENT_TYPE.get(DECORATIONS);
        if (type == null || !(wearer.getItemBySlot(EquipmentSlot.CHEST).get(type) instanceof CustomData data)) {
            return List.of();
        }
        ListTag items = data.copyTag().getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < items.size(); i++) {
            CompoundTag item = items.getCompound(i);
            ResourceLocation name = ResourceLocation.tryParse(item.getString("name"));
            if (name == null || !isCloth(name)) {
                continue;
            }
            boolean dyed = item.getBoolean("dyeable");
            List<ClothRenderer.Layer> layers = new ArrayList<>(2);
            layers.add(layer(name, "", dyed ? 0xFF000000 | item.getInt("color") : -1, true));
            if (dyed) {
                layers.add(layer(name, "_overlay", -1, false));
            }
            return layers;
        }
        return List.of();
    }

    private static ClothRenderer.Layer layer(ResourceLocation name, String suffix, int argb, boolean solid) {
        ResourceLocation texture = name.withPath(p -> "textures/models/armor/" + p + suffix + ".png");
        return new ClothRenderer.Layer(new Material(texture, texture), argb, solid, US, V0, V1);
    }
}
