package rafasconquest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.talhanation.recruits.init.ModEntityTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import rafasconquest.battle.DistantDepth;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.battle.FormationSnapshot.Sprite;

import java.util.List;

/**
 * Draws a battle's soldiers where the game no longer shows them: each is a flat, upright figure turned to face the
 * camera, in the army's uniform colour. Leaders carry a flag in the banner's colour, the general a gold helmet and a
 * flag on the back, the standard-bearer a flag on a pole; cavalry ride a horse in the army's colour.
 * <p>
 * The real soldiers hand over to figures at {@link #handover}: where the game stops drawing them, or sooner where
 * Distant Horizons starts fading the world into its own terrain (it would fade them too). Past that, the real soldiers
 * and their horses are hidden ({@link #hides}). A soldier the client still has is drawn exactly where it stands, so
 * nothing jumps at the handover. A soldier the client hasn't been sent (the server sends only those near enough), or
 * one of a block that is only numbers, gets a figure in its formation's ranks however near it is, since nothing else
 * would show it. Beyond what the camera can see, figures
 * are pulled in ({@link DistantDepth}) so they look the same on screen. With Distant Horizons drawing, soldiers
 * beyond the game's own terrain are left to {@link DistantHorizonsArmies}.
 */
public final class DistantArmyRenderer {
    private static final int SKIN = 0xBD8E6E;
    private static final int GOLD = 0xF9D84A;
    private static final int POLE = 0x6B4A2B;
    private static final int HOOVES = 0x3B2A1C;
    /**
     * Figures face the camera like the side of a block, so they are shaded like one (as Distant Horizons shades its
     * box figures' sides), on top of the light where they stand.
     */
    private static final float SIDE_SHADE = 0.8F;

    /** No soldier of a formation stands further than this from its middle (a block's half width, and more). */
    private static final double FORMATION_REACH = 32;

    /** This frame's handover distance, worked out before anything is drawn. Render thread only. */
    private static double handover = Double.MAX_VALUE;

    private DistantArmyRenderer() {
    }

    /** Before the frame's world is drawn: where the real soldiers hand over to figures this frame. */
    public static void prepare(float partialTick) {
        double drawn = drawnWithin(ModEntityTypes.RECRUIT.get());
        double fade = DistantHorizonsLink.fadeStart(partialTick);
        handover = fade > 0 ? Math.min(drawn, fade) : drawn;
    }

    /** Whether to hide this real soldier (or its horse) because a figure stands in for it. */
    public static boolean hides(Entity entity, Vec3 eye, float partialTick) {
        if (!DistantArmies.isOurs(entity)) {
            return false;
        }
        Entity rider = entity.getPassengers().isEmpty() ? entity : entity.getPassengers().get(0);
        Vec3 feet = DistantArmies.feet(rider, partialTick);
        return feet != null && feet.distanceTo(eye) > handover;
    }

    public static void render(PoseStack pose, Camera camera, float partialTick) {
        List<FormationSnapshot> formations = DistantArmies.now();
        if (formations.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        int renderChunks = mc.options.getEffectiveRenderDistance();
        double renderBlocks = renderChunks * 16.0;
        Vec3 eye = camera.getPosition();
        Vector3f left = camera.getLeftVector();
        double length = Math.hypot(left.x(), left.z());
        if (length < 1e-6) {
            return; // looking straight up or down: no sideways to face
        }
        float rightX = (float) (-left.x() / length);
        float rightZ = (float) (-left.z() / length);

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
        Figure figure = new Figure(buffer, pose.last().pose(), rightX, rightZ);
        boolean farInDistantHorizons = DistantHorizonsLink.drawsFarSoldiers();
        FormationSnapshot.Positions positions = DistantArmies.positions(partialTick);
        int drawn = 0;
        for (FormationSnapshot formation : formations) {
            if (farInDistantHorizons && !onGameTerrain(formation.x() - eye.x, formation.z() - eye.z, renderBlocks + FORMATION_REACH)) {
                continue; // all of it is drawn in Distant Horizons' terrain
            }
            for (Sprite sprite : formation.place(positions)) {
                double dx = sprite.x() - eye.x;
                double dy = sprite.y() - eye.y;
                double dz = sprite.z() - eye.z;
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (sprite.real() && distance <= handover) {
                    continue; // the client has this soldier, close enough to draw it itself
                }
                if (farInDistantHorizons && !onGameTerrain(dx, dz, renderBlocks)) {
                    continue; // drawn in Distant Horizons' terrain instead
                }
                double scale = DistantDepth.drawDistance(distance, renderBlocks) / distance;
                figure.at((float) (dx * scale), (float) (dy * scale), (float) (dz * scale), (float) scale,
                        lightAt(mc, sprite));
                draw(figure, formation, sprite.leader());
                drawn++;
            }
        }
        if (drawn == 0) {
            buffer.end().release();
            return;
        }
        RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        mc.gameRenderer.lightTexture().turnOnLightLayer();
        BufferUploader.drawWithShader(buffer.end());
        mc.gameRenderer.lightTexture().turnOffLightLayer();
        RenderSystem.enableCull();
    }

    /**
     * The light a figure stands in, as the game lights a mob there: the sky and block light at its head height. Out
     * past the chunks the client has, it is open sky.
     */
    private static int lightAt(Minecraft mc, Sprite sprite) {
        BlockPos head = BlockPos.containing(sprite.x(), sprite.y() + 1, sprite.z());
        if (mc.level == null || !mc.level.hasChunkAt(head)) {
            return LightTexture.FULL_SKY;
        }
        return LevelRenderer.getLightColor(mc.level, head);
    }

    /**
     * Whether a soldier ({@code dx}, {@code dz}) from the camera stands within the game's own terrain (the render
     * distance) rather than Distant Horizons'.
     */
    static boolean onGameTerrain(double dx, double dz, double renderBlocks) {
        return dx * dx + dz * dz <= renderBlocks * renderBlocks;
    }

    /**
     * How far away the game still draws a soldier (vanilla's {@code Entity.shouldRenderAtSqrDistance}); a horse is
     * drawn further, but hands over with its rider.
     */
    private static double drawnWithin(EntityType<?> type) {
        EntityDimensions size = type.getDimensions();
        return (size.width * 2 + size.height) / 3 * 64 * Entity.getViewScale();
    }

    /** One figure, built of rectangles side by side so none overlaps another. Sizes in blocks, from the feet up. */
    private static void draw(Figure f, FormationSnapshot formation, boolean leader) {
        int uniform = formation.rgb();
        int shade = darker(uniform);
        int mark = formation.mark();
        switch (formation.kind()) {
            case CAVALRY -> {
                f.rect(0, 0, 1.2F, 0.7F, HOOVES);
                f.rect(0, 0.7F, 1.5F, 0.6F, uniform); // the horse, in dyed leather armour
                f.rect(0, 1.3F, 0.5F, 0.4F, shade);
                f.rect(0, 1.7F, 0.6F, 0.6F, uniform);
                head(f, 2.3F, uniform, mark, leader);
            }
            case GENERAL -> {
                body(f, uniform, shade);
                f.rect(0, 1.45F, 0.45F, 0.25F, SKIN);
                f.rect(0, 1.7F, 0.55F, 0.3F, GOLD);
                f.rect(-0.05F, 2.0F, 0.08F, 1.3F, POLE); // the banner on the back, its pole above the helmet
                f.rect(0.33F, 2.4F, 0.5F, 0.85F, mark);
            }
            case BEARER -> {
                body(f, uniform, shade);
                head(f, 1.45F, uniform, mark, false);
                f.rect(0.45F, 0.3F, 0.08F, 3.0F, POLE);
                f.rect(0.78F, 2.3F, 0.55F, 0.95F, mark);
            }
            default -> {
                body(f, uniform, shade);
                head(f, 1.45F, uniform, mark, leader);
            }
        }
    }

    private static void body(Figure f, int uniform, int shade) {
        f.rect(0, 0, 0.5F, 0.75F, shade);
        f.rect(0, 0.75F, 0.6F, 0.7F, uniform);
    }

    /** A face and a leather cap; a leader wears the faction banner instead, standing tall on the head. */
    private static void head(Figure f, float bottom, int cap, int mark, boolean leader) {
        f.rect(0, bottom, 0.45F, 0.3F, SKIN);
        if (leader) {
            f.rect(0, bottom + 0.3F, 0.08F, 0.25F, POLE);
            f.rect(0, bottom + 0.55F, 0.55F, 0.9F, mark);
        } else {
            f.rect(0, bottom + 0.3F, 0.5F, 0.18F, cap);
        }
    }

    private static int darker(int rgb) {
        return (int) ((rgb >> 16 & 255) * 0.7) << 16 | (int) ((rgb >> 8 & 255) * 0.7) << 8 | (int) ((rgb & 255) * 0.7);
    }

    /** Writes the rectangles of the figure currently being drawn, upright and square to the camera. */
    private static final class Figure {
        private final BufferBuilder buffer;
        private final Matrix4f matrix;
        private final float rightX;
        private final float rightZ;
        private float x;
        private float y;
        private float z;
        private float scale;
        private int light;

        Figure(BufferBuilder buffer, Matrix4f matrix, float rightX, float rightZ) {
            this.buffer = buffer;
            this.matrix = matrix;
            this.rightX = rightX;
            this.rightZ = rightZ;
        }

        /** The next figure stands at ({@code x}, {@code y}, {@code z}) from the camera, drawn {@code scale} times its size. */
        void at(float x, float y, float z, float scale, int light) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.scale = scale;
            this.light = light;
        }

        /** A rectangle centred {@code right} blocks to the figure's right, from {@code bottom} up {@code height}. */
        void rect(float right, float bottom, float width, float height, int rgb) {
            float r = (rgb >> 16 & 255) / 255F * SIDE_SHADE;
            float g = (rgb >> 8 & 255) / 255F * SIDE_SHADE;
            float b = (rgb & 255) / 255F * SIDE_SHADE;
            float l = (right - width / 2) * scale;
            float h = (right + width / 2) * scale;
            float lo = y + bottom * scale;
            float hi = y + (bottom + height) * scale;
            buffer.vertex(matrix, x + rightX * l, lo, z + rightZ * l).color(r, g, b, 1F).uv2(light).endVertex();
            buffer.vertex(matrix, x + rightX * h, lo, z + rightZ * h).color(r, g, b, 1F).uv2(light).endVertex();
            buffer.vertex(matrix, x + rightX * h, hi, z + rightZ * h).color(r, g, b, 1F).uv2(light).endVertex();
            buffer.vertex(matrix, x + rightX * l, hi, z + rightZ * l).color(r, g, b, 1F).uv2(light).endVertex();
        }
    }
}
