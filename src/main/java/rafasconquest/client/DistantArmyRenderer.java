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
import rafasconquest.style.ShipHulls;
import net.minecraft.world.level.EmptyBlockGetter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Draws a battle's soldiers where the game no longer shows them, in the Sprite System's order going away from the
 * camera: the real soldier, then its 3D level of detail ({@link SoldierFigure}), then, past
 * {@link SoldierFigure#SPRITE_PAST}, a sprite: a flat, upright figure turned to face the camera, in the army's uniform
 * colour. Leaders carry a flag in the banner's colour, the general a gold helmet and a flag on the back, the
 * standard-bearer a flag on a pole; cavalry ride a horse in the army's colour.
 * <p>
 * The real soldiers hand over to the 3D figures at {@link #handover}: where the game stops drawing them, or sooner
 * where Distant Horizons starts fading the world into its own terrain (it would fade them too). Past that, the real
 * soldiers and their horses are hidden ({@link #hides}). A soldier the client still has is drawn exactly where it
 * stands, so nothing jumps at the handover. A soldier the client hasn't been sent (the server sends only those near
 * enough), or one of a block that is only numbers, gets a figure in its formation's ranks however near it is, since
 * nothing else would show it. Beyond what the camera can see, figures are pulled in ({@link DistantDepth}) so they look
 * the same on screen. Everything here is drawn after Distant Horizons' terrain, laid over it rather than in it, so each
 * figure moves and turns freely every frame; the cost is that Distant Horizons' far hills don't hide them.
 */
public final class DistantArmyRenderer {
    private static final int SKIN = SoldierFigure.SKIN;
    private static final int GOLD = SoldierFigure.GOLD;
    private static final int POLE = SoldierFigure.POLE;
    private static final int HOOVES = SoldierFigure.HOOVES;
    private static final int HULL = 0x5A3A22;
    private static final int SAIL = 0xEDE6D6;
    private static final int PENNANT = 0xB02E26;
    /**
     * Figures face the camera like the side of a block, so they are shaded like one (as Distant Horizons shades its
     * box figures' sides), on top of the light where they stand.
     */
    private static final float SIDE_SHADE = 0.8F;


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
        double renderBlocks = mc.options.getEffectiveRenderDistance() * 16.0;
        Vec3 eye = camera.getPosition();
        Vector3f left = camera.getLeftVector();
        double length = Math.hypot(left.x(), left.z());
        if (length < 1e-6) {
            return; // looking straight up or down: no sideways to face
        }
        float rightX = (float) (-left.x() / length);
        float rightZ = (float) (-left.z() / length);

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
        Figure figure = new Figure(buffer, pose.last().pose(), rightX, rightZ);
        FormationSnapshot.Positions positions = DistantArmies.positions(partialTick);
        int drawn = 0;
        for (FormationSnapshot formation : formations) {
            for (Sprite sprite : formation.place(positions)) {
                double dx = sprite.x() - eye.x;
                double dy = sprite.y() - eye.y;
                double dz = sprite.z() - eye.z;
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (sprite.real() && distance <= handover) {
                    continue; // the client has this soldier, close enough to draw it itself
                }
                boolean flatSprite = Math.hypot(dx, dz) > SoldierFigure.SPRITE_PAST;
                double scale = DistantOcclusion.scale(distance, renderBlocks);
                figure.at((float) (dx * scale), (float) (dy * scale), (float) (dz * scale), (float) scale,
                        lightAt(mc, sprite));
                if (flatSprite) {
                    draw(figure, formation, sprite.leader());
                } else {
                    SoldierFigure.build(formation, sprite.leader(), figure);
                }
                drawn++;
            }
        }
        finish(mc, buffer, drawn);
    }

    /**
     * Ships beyond the real ones ({@link DistantShips}): out to {@link DistantShips#SPRITE_PAST} a box for each block of
     * the hull, turned to its heading; past that a flat ship turned to face the camera, a hull under a mast and sail.
     */
    public static void renderShips(PoseStack pose, Camera camera, float partialTick) {
        List<DistantShips.Far> ships = DistantShips.all(partialTick);
        Vector3f left = camera.getLeftVector();
        double length = Math.hypot(left.x(), left.z());
        if (ships.isEmpty() || length < 1e-6) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        double renderBlocks = mc.options.getEffectiveRenderDistance() * 16.0;
        Vec3 eye = camera.getPosition();
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
        Figure f = new Figure(buffer, pose.last().pose(), (float) (-left.x() / length), (float) (-left.z() / length));
        double fade = DistantHorizonsLink.fadeStart(partialTick);
        double dy = DistantShips.keel() - eye.y;
        int drawn = 0;
        for (DistantShips.Far ship : ships) {
            double dx = ship.x() - eye.x;
            double dz = ship.z() - eye.z;
            double flat = Math.hypot(dx, dz);
            if (ship.shownReal(flat, fade)) {
                continue; // Sable draws it
            }
            double distance = Math.hypot(flat, dy);
            double scale = DistantOcclusion.scale(distance, renderBlocks);
            f.at((float) (dx * scale), (float) (dy * scale), (float) (dz * scale), (float) scale, LightTexture.FULL_SKY);
            drawn++;
            if (flat <= DistantShips.SPRITE_PAST) {
                shipBoxes(f, ship);
                continue;
            }
            float hull = ship.size().length;
            f.rect(0, 0, hull, 3, HULL);
            f.rect(0, 3, 0.6F, hull / 2 + 1, POLE);
            f.rect(0, 4.5F, hull * 0.55F, hull / 2 - 2, SAIL);
            if (ship.warship()) {
                f.rect(1.2F, hull / 2 + 2.5F, 2.4F, 1, PENNANT);
            }
        }
        finish(mc, buffer, drawn);
    }

    /**
     * A ship's 3D level of detail: a block for each block of its hull in the block's map colour, turned with the ship.
     * Its shape is worked out once per hull ({@link Hull}); each frame only turns, scales and places its faces.
     */
    private static void shipBoxes(Figure f, DistantShips.Far ship) {
        Hull hull = HULLS.computeIfAbsent(ship.parts(), Hull::of);
        float cos = (float) Math.cos(ship.yaw());
        float sin = (float) Math.sin(ship.yaw());
        float[] c = hull.corners;
        float[] v = new float[12];
        for (int face = 0; face < hull.rgb.length; face++) {
            int at = face * 12;
            for (int i = 0; i < 12; i += 3) {
                v[i] = f.x + (c[at + i] * cos + c[at + i + 2] * sin) * f.scale;
                v[i + 1] = f.y + c[at + i + 1] * f.scale;
                v[i + 2] = f.z + (-c[at + i] * sin + c[at + i + 2] * cos) * f.scale;
            }
            f.quad(hull.rgb[face], hull.shade[face], v);
        }
    }

    /** Each hull's shape, by the hull list {@link ShipHulls} hands out (the same list for the same hull). Render thread only. */
    private static final Map<List<ShipHulls.Part>, Hull> HULLS = new IdentityHashMap<>();

    /**
     * A hull's faces open to the air, in its own frame (x across, z to the bow): four corners each, its colour and shade.
     * No bottoms: they're under water. Each face is a hair inside its block, so where the real ship still shows, it wins
     * and nothing flickers.
     */
    private record Hull(float[] corners, int[] rgb, float[] shade) {
        static Hull of(List<ShipHulls.Part> parts) {
            Set<Long> solid = new HashSet<>();
            for (ShipHulls.Part p : parts) {
                if (colour(p) != 0) {
                    solid.add(cell(p.x(), p.y(), p.z()));
                }
            }
            List<float[]> corners = new ArrayList<>();
            List<Integer> rgbs = new ArrayList<>();
            List<Float> shades = new ArrayList<>();
            float in = 0.48F;
            for (ShipHulls.Part p : parts) {
                int rgb = colour(p);
                if (rgb == 0) {
                    continue; // glass and the like
                }
                int x = p.x();
                int y = p.y();
                int z = p.z();
                List<float[]> faces = new ArrayList<>();
                List<Float> faceShades = new ArrayList<>();
                if (!solid.contains(cell(x, y + 1, z))) {
                    float t = y + 2 * in;
                    faces.add(new float[]{x - 0.5F, t, z - 0.5F, x + 0.5F, t, z - 0.5F, x + 0.5F, t, z + 0.5F, x - 0.5F, t, z + 0.5F});
                    faceShades.add(1.0F);
                }
                for (int side = -1; side <= 1; side += 2) {
                    if (!solid.contains(cell(x + side, y, z))) {
                        float s = x + side * in;
                        faces.add(new float[]{s, y, z - 0.5F, s, y, z + 0.5F, s, y + 1, z + 0.5F, s, y + 1, z - 0.5F});
                        faceShades.add(0.6F);
                    }
                    if (!solid.contains(cell(x, y, z + side))) {
                        float s = z + side * in;
                        faces.add(new float[]{x - 0.5F, y, s, x + 0.5F, y, s, x + 0.5F, y + 1, s, x - 0.5F, y + 1, s});
                        faceShades.add(0.8F);
                    }
                }
                corners.addAll(faces);
                shades.addAll(faceShades);
                faces.forEach(unused -> rgbs.add(rgb));
            }
            float[] flat = new float[corners.size() * 12];
            int[] rgb = new int[corners.size()];
            float[] shade = new float[corners.size()];
            for (int i = 0; i < rgb.length; i++) {
                System.arraycopy(corners.get(i), 0, flat, i * 12, 12);
                rgb[i] = rgbs.get(i);
                shade[i] = shades.get(i);
            }
            return new Hull(flat, rgb, shade);
        }
    }

    private static int colour(ShipHulls.Part p) {
        return p.state().getMapColor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).col;
    }

    private static long cell(int x, int y, int z) {
        return ((long) (x + 1024) << 32) | ((long) (y + 1024) << 16) | (z + 1024);
    }

    private static void finish(Minecraft mc, BufferBuilder buffer, int drawn) {
        if (drawn == 0) {
            try (com.mojang.blaze3d.vertex.MeshData unused = buffer.build()) { // nothing to draw: let the buffer go
            }
            return;
        }
        RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        mc.gameRenderer.lightTexture().turnOnLightLayer();
        BufferUploader.drawWithShader(buffer.buildOrThrow());
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
        return (size.width() * 2 + size.height()) / 3 * 64 * Entity.getViewScale();
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
        return SoldierFigure.darker(rgb);
    }

    /**
     * Writes the figure currently being drawn: as a sprite, rectangles upright and square to the camera; as its 3D
     * level of detail, boxes shaded like a block's faces.
     */
    private static final class Figure implements SoldierFigure.Boxes {
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
            buffer.addVertex(matrix, x + rightX * l, lo, z + rightZ * l).setColor(r, g, b, 1F).setLight(light);
            buffer.addVertex(matrix, x + rightX * h, lo, z + rightZ * h).setColor(r, g, b, 1F).setLight(light);
            buffer.addVertex(matrix, x + rightX * h, hi, z + rightZ * h).setColor(r, g, b, 1F).setLight(light);
            buffer.addVertex(matrix, x + rightX * l, hi, z + rightZ * l).setColor(r, g, b, 1F).setLight(light);
        }

        /** A box from the figure's feet, its faces shaded as the game shades a block's; no bottom, it's never seen. */
        @Override
        public void box(double x0, double y0, double z0, double x1, double y1, double z1, int rgb, SoldierFigure.Stuff stuff) {
            float ax = x + (float) x0 * scale;
            float ay = y + (float) y0 * scale;
            float az = z + (float) z0 * scale;
            float bx = x + (float) x1 * scale;
            float by = y + (float) y1 * scale;
            float bz = z + (float) z1 * scale;
            quad(rgb, 1.0F, ax, by, az, bx, by, az, bx, by, bz, ax, by, bz); // top
            quad(rgb, 0.8F, ax, ay, az, bx, ay, az, bx, by, az, ax, by, az); // north
            quad(rgb, 0.8F, ax, ay, bz, bx, ay, bz, bx, by, bz, ax, by, bz); // south
            quad(rgb, 0.6F, ax, ay, az, ax, ay, bz, ax, by, bz, ax, by, az); // west
            quad(rgb, 0.6F, bx, ay, az, bx, ay, bz, bx, by, bz, bx, by, az); // east
        }

        private void quad(int rgb, float shade, float... v) {
            float r = (rgb >> 16 & 255) / 255F * shade;
            float g = (rgb >> 8 & 255) / 255F * shade;
            float b = (rgb & 255) / 255F * shade;
            for (int i = 0; i < 12; i += 3) {
                buffer.addVertex(matrix, v[i], v[i + 1], v[i + 2]).setColor(r, g, b, 1F).setLight(light);
            }
        }
    }
}
