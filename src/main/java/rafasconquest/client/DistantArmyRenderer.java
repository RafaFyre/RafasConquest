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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import rafasconquest.battle.DistantDepth;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.battle.FormationSnapshot.Kind;
import rafasconquest.battle.FormationSnapshot.Sprite;

import java.util.List;

/**
 * Draws a battle's soldiers where the game no longer draws them: each is a flat, upright figure turned to face the
 * camera, in the army's uniform colour, standing where its formation's snapshot lays it out. Leaders carry a flag in
 * the banner's colour, the general a gold helmet and a flag on the back, the standard-bearer a flag on a pole;
 * cavalry ride a horse in the army's colour. A figure appears just beyond the distance at which the game stops drawing
 * that kind of mob, so the two hand over to each other. Beyond what the camera can see, figures are pulled in
 * ({@link DistantDepth}) so they look the same on screen.
 */
public final class DistantArmyRenderer {
    private static final int SKIN = 0xBD8E6E;
    private static final int GOLD = 0xF9D84A;
    private static final int POLE = 0x6B4A2B;
    private static final int HOOVES = 0x3B2A1C;

    private DistantArmyRenderer() {
    }

    public static void render(PoseStack pose, Camera camera) {
        List<FormationSnapshot> formations = DistantArmies.now();
        if (formations.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        double renderBlocks = mc.options.getEffectiveRenderDistance() * 16.0;
        double soldierRange = drawnWithin(ModEntityTypes.RECRUIT.get());
        double horseRange = drawnWithin(EntityType.HORSE);
        Vec3 eye = camera.getPosition();
        Vector3f left = camera.getLeftVector();
        double length = Math.hypot(left.x(), left.z());
        if (length < 1e-6) {
            return; // looking straight up or down: no sideways to face
        }
        float rightX = (float) (-left.x() / length);
        float rightZ = (float) (-left.z() / length);

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Figure figure = new Figure(buffer, pose.last().pose(), rightX, rightZ);
        int drawn = 0;
        for (FormationSnapshot formation : formations) {
            double range = formation.kind() == Kind.CAVALRY ? horseRange : soldierRange;
            for (Sprite sprite : formation.layout()) {
                double dx = sprite.x() - eye.x;
                double dy = formation.y() - eye.y;
                double dz = sprite.z() - eye.z;
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (distance <= range) {
                    continue; // close enough that the game draws the soldier itself
                }
                double scale = DistantDepth.drawDistance(distance, renderBlocks) / distance;
                figure.at((float) (dx * scale), (float) (dy * scale), (float) (dz * scale), (float) scale);
                draw(figure, formation, sprite.leader());
                drawn++;
            }
        }
        if (drawn == 0) {
            buffer.end().release();
            return;
        }
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        BufferUploader.drawWithShader(buffer.end());
        RenderSystem.enableCull();
    }

    /** How far away the game still draws a mob of this type (vanilla's {@code Entity.shouldRenderAtSqrDistance}). */
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

        Figure(BufferBuilder buffer, Matrix4f matrix, float rightX, float rightZ) {
            this.buffer = buffer;
            this.matrix = matrix;
            this.rightX = rightX;
            this.rightZ = rightZ;
        }

        /** The next figure stands at ({@code x}, {@code y}, {@code z}) from the camera, drawn {@code scale} times its size. */
        void at(float x, float y, float z, float scale) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.scale = scale;
        }

        /** A rectangle centred {@code right} blocks to the figure's right, from {@code bottom} up {@code height}. */
        void rect(float right, float bottom, float width, float height, int rgb) {
            float r = (rgb >> 16 & 255) / 255F;
            float g = (rgb >> 8 & 255) / 255F;
            float b = (rgb & 255) / 255F;
            float l = (right - width / 2) * scale;
            float h = (right + width / 2) * scale;
            float lo = y + bottom * scale;
            float hi = y + (bottom + height) * scale;
            buffer.vertex(matrix, x + rightX * l, lo, z + rightZ * l).color(r, g, b, 1F).endVertex();
            buffer.vertex(matrix, x + rightX * h, lo, z + rightZ * h).color(r, g, b, 1F).endVertex();
            buffer.vertex(matrix, x + rightX * h, hi, z + rightZ * h).color(r, g, b, 1F).endVertex();
            buffer.vertex(matrix, x + rightX * l, hi, z + rightZ * l).color(r, g, b, 1F).endVertex();
        }
    }
}
