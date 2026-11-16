package rafasconquest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import rafasconquest.battle.BattlePlan;
import rafasconquest.battle.DistantDepth;
import rafasconquest.battle.FormationSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Shots in flight from shooters the client sees only as figures: each second the server says which formation shot at
 * which, how many shots, and by what arm ({@link rafasconquest.net.ConquestNetwork.Volleys}). Each shot leaves a figure
 * of the shooters at a moment within the second, flies over and comes down somewhere in the target formation: arrows
 * and bolts as thin streaks, musket balls as faint fast streaks, rockets with a burning tail, cannonballs and stones as
 * dark round shapes. Guns, cannons and hwachas leave smoke where they fire. All are drawn never smaller on screen than a
 * set share of their distance, so they can be seen from far off; like the figures, those beyond what the camera sees
 * are pulled in ({@link DistantDepth}). Client thread only.
 */
public final class ArrowVolleys {
    /** At most this many shots of one volley are drawn; a big volley is still a cloud of them. */
    private static final int MAX_PER_VOLLEY = 24;
    /** At most this many shots in the air, and smoke puffs hanging, at once. */
    private static final int MAX_IN_FLIGHT = 3000;
    private static final int MAX_PUFFS = 2500;
    /** Of a musket volley, only this many shots make smoke: a line's smoke is one bank anyway. */
    private static final int MAX_PUFFS_PER_VOLLEY = 8;
    private static final double MIN_FLIGHT = 0.6;
    /** The smallest a round shot is drawn (radius, blocks per block away), and a smoke puff. */
    private static final double MIN_BALL_PER_BLOCK = 0.004;
    private static final double MIN_PUFF_PER_BLOCK = 0.008;
    /** How long a shot's muzzle flash shows in its smoke, in seconds. */
    private static final double FLASH = 0.15;
    private static final int SHAFT = 0x8B6B4A;
    private static final int FLETCHING = 0xE8E8E8;
    private static final int HEAD = 0x9A9A9A;
    private static final int LEAD = 0xC8C8C0;
    private static final int IRON = 0x2A2A2A;
    private static final int STONE = 0x7A766E;
    private static final int FIRE = 0xFFA030;
    private static final int SMOKE = 0xD8D8D4;
    private static final int FLASH_COLOUR = 0xFFE090;
    /** A thrown stone's dust trail: how many quads, over how many seconds of its flight, and its colour. */
    private static final int TRAIL_STEPS = 4;
    private static final double TRAIL_SECONDS = 0.6;
    private static final int TRAIL = 0xB8B0A0;

    /**
     * How a kind of shot flies and looks: its speed over the ground (blocks a second), arc (rise over its middle as a
     * share of the distance, and at most), streak length and width share (0: a round shot of {@code ball} radius), how
     * long it is drawn at the least for its distance; how many are drawn for each shot fired, and the smoke where it is
     * fired (puff radius 0: none; how many puffs, how long they hang).
     */
    private enum Kind {
        ARROW(30, 0.18, 25, 0.8, 0.015, 0.08, 0, 1, 0, 0, 0),
        SHOT(160, 0.01, 2, 0.5, 0.01, 0.06, 0, 1, 0.6, 1, 3),
        BOLT(50, 0.08, 10, 2.0, 0.03, 0.06, 0, 1, 0, 0, 0),
        /** A hwacha fires a hundred-odd fire arrows at once. */
        ROCKET(40, 0.15, 20, 1.0, 0.02, 0.1, 0, 16, 2.5, 3, 6),
        BALL(90, 0.04, 8, 0, 0, 0, 0.3, 1, 3, 3, 7),
        BIG_BALL(70, 0.06, 12, 0, 0, 0, 0.7, 1, 5, 4, 9),
        ROCK(25, 0.3, 40, 0, 0, 0, 0.6, 1, 0, 0, 0),
        BIG_ROCK(22, 0.35, 50, 0, 0, 0, 1.0, 1, 0, 0, 0);

        final double speed, arc, maxRise, length, minLength, width, ball;
        final int perShot;
        final double smoke;
        final int puffs;
        final double smokeSeconds;

        Kind(double speed, double arc, double maxRise, double length, double minLength, double width, double ball, int perShot,
             double smoke, int puffs, double smokeSeconds) {
            this.speed = speed;
            this.arc = arc;
            this.maxRise = maxRise;
            this.length = length;
            this.minLength = minLength;
            this.width = width;
            this.ball = ball;
            this.perShot = perShot;
            this.smoke = smoke;
            this.puffs = puffs;
            this.smokeSeconds = smokeSeconds;
        }

        static Kind of(int arm) {
            BattlePlan.Arm[] arms = BattlePlan.Arm.values();
            return arm < 0 || arm >= arms.length ? ARROW : switch (arms[arm]) {
                case MUSKETEERS -> SHOT;
                case CULVERINS, SERPENTINES -> BALL;
                case MONS_MEGS -> BIG_BALL;
                case HWACHAS -> ROCKET;
                case ARCBALLISTAS, TOWER_CROSSBOWS -> BOLT;
                case ONAGERS -> ROCK;
                case TREBUCHETS -> BIG_ROCK;
                default -> ARROW;
            };
        }
    }

    /** A shot: of what kind, from where, to where, how high its arc rises, when it is loosed and how long it flies. */
    private record Arrow(Kind kind, Vec3 from, Vec3 to, double rise, long start, double seconds) {
    }

    /** Smoke where a shot was fired: where, from when, for how long, how big at the start. */
    private record Puff(double x, double y, double z, long start, double seconds, double size) {
    }

    private static final List<Arrow> ARROWS = new ArrayList<>();
    private static final List<Puff> PUFFS = new ArrayList<>();
    private static final Random RANDOM = new Random();

    private ArrowVolleys() {
    }

    public static void receive(int[] from, int[] to, int[] shots, int[] arms) {
        long now = Util.getMillis();
        for (int v = 0; v < from.length; v++) {
            FormationSnapshot archers = DistantArmies.at(from[v]);
            FormationSnapshot target = DistantArmies.at(to[v]);
            if (archers == null || target == null || archers.alive() <= 0) {
                continue;
            }
            Kind kind = Kind.of(v < arms.length ? arms[v] : -1);
            List<FormationSnapshot.Sprite> figures = archers.layout();
            int fired = Math.min(shots[v], MAX_PER_VOLLEY);
            for (int i = 0; i < fired; i++) {
                FormationSnapshot.Sprite s = figures.get(RANDOM.nextInt(figures.size()));
                Vec3 start = new Vec3(s.x(), s.y() + 1.4, s.z());
                long when = now + RANDOM.nextInt(1000);
                Vec3 aim = null;
                for (int k = 0; k < kind.perShot && ARROWS.size() < MAX_IN_FLIGHT; k++) {
                    double right = (RANDOM.nextDouble() * 2 - 1) * Math.max(1, target.halfWidth());
                    double back = (RANDOM.nextDouble() * 2 - 1) * Math.max(1, target.halfDepth());
                    Vec3 end = new Vec3(target.x() - target.facingZ() * right - target.facingX() * back, target.y() + 0.5,
                            target.z() + target.facingX() * right - target.facingZ() * back);
                    double distance = Math.hypot(end.x - start.x, end.z - start.z);
                    ARROWS.add(new Arrow(kind, start, end, Math.min(kind.maxRise, distance * kind.arc),
                            when + (kind.perShot > 1 ? RANDOM.nextInt(400) : 0), MIN_FLIGHT + distance / kind.speed));
                    aim = end;
                }
                if (kind.smoke > 0 && aim != null && i < MAX_PUFFS_PER_VOLLEY) {
                    smoke(kind, start, aim, when);
                }
            }
        }
    }

    /** The smoke of one shot: puffs from the muzzle a little way toward the target. */
    private static void smoke(Kind kind, Vec3 start, Vec3 aim, long when) {
        double dx = aim.x - start.x;
        double dz = aim.z - start.z;
        double d = Math.max(1e-3, Math.hypot(dx, dz));
        for (int p = 0; p < kind.puffs && PUFFS.size() < MAX_PUFFS; p++) {
            double out = p * kind.smoke * 0.8;
            PUFFS.add(new Puff(start.x + dx / d * out + (RANDOM.nextDouble() - 0.5) * kind.smoke,
                    start.y - 0.4 + RANDOM.nextDouble() * kind.smoke * 0.5,
                    start.z + dz / d * out + (RANDOM.nextDouble() - 0.5) * kind.smoke,
                    when, kind.smokeSeconds * (0.8 + RANDOM.nextDouble() * 0.4), kind.smoke * (1 - 0.15 * p)));
        }
    }

    public static void clear() {
        ARROWS.clear();
        PUFFS.clear();
    }

    /**
     * Draws every shot in flight, and the smoke, that the camera can see. Worked in plain numbers, not vectors:
     * thousands of shots a frame would otherwise make tens of thousands of objects to throw away.
     */
    public static void render(PoseStack pose, Camera camera, net.minecraft.client.renderer.culling.Frustum frustum) {
        long now = Util.getMillis();
        ARROWS.removeIf(a -> (now - a.start()) / 1000.0 / a.seconds() > 1); // landed
        PUFFS.removeIf(p -> (now - p.start()) / 1000.0 > p.seconds()); // gone
        if (ARROWS.isEmpty() && PUFFS.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        double renderBlocks = mc.options.getEffectiveRenderDistance() * 16.0;
        Vec3 eye = camera.getPosition();
        Vector3f left = camera.getLeftVector();
        Vector3f up = camera.getUpVector();
        Matrix4f matrix = pose.last().pose();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
        int drawn = 0;
        for (Arrow a : ARROWS) {
            double u = (now - a.start()) / 1000.0 / a.seconds();
            if (u < 0) {
                continue; // not loosed yet
            }
            Kind kind = a.kind();
            Vec3 from = a.from();
            Vec3 to = a.to();
            // Where it is along its flight, from the camera, and which way it flies there.
            double x = from.x + (to.x - from.x) * u;
            double y = from.y + (to.y - from.y) * u + 4 * a.rise() * u * (1 - u);
            double z = from.z + (to.z - from.z) * u;
            double hx = x - eye.x;
            double hy = y - eye.y;
            double hz = z - eye.z;
            double distance = Math.sqrt(hx * hx + hy * hy + hz * hz);
            if (distance < 1e-3) {
                continue;
            }
            double scale = DistantDepth.drawDistance(distance, renderBlocks) / distance;
            double px = hx * scale;
            double py = hy * scale;
            double pz = hz * scale;
            if (kind.ball > 0) {
                double r = Math.max(kind.ball, distance * MIN_BALL_PER_BLOCK) * scale;
                if (visible(frustum, eye, px, py, pz, r)) {
                    disc(buffer, matrix, left, up, px, py, pz, r, kind == Kind.ROCK || kind == Kind.BIG_ROCK ? STONE : IRON, 1, 1,
                            lightAt(mc, pos.set(x, y, z)));
                    drawn++;
                }
                continue;
            }
            double ax = to.x - from.x;
            double ay = to.y - from.y + 4 * a.rise() * (1 - 2 * u);
            double az = to.z - from.z;
            double alongLength = Math.sqrt(ax * ax + ay * ay + az * az);
            if (alongLength < 1e-9) {
                continue;
            }
            ax /= alongLength;
            ay /= alongLength;
            az /= alongLength;
            double sx = ay * hz - az * hy; // square to its flight and to the line of sight
            double sy = az * hx - ax * hz;
            double sz = ax * hy - ay * hx;
            double sideLength = Math.sqrt(sx * sx + sy * sy + sz * sz);
            if (sideLength < 1e-3) {
                continue; // flying straight at the camera
            }
            double length = Math.max(kind.length, distance * kind.minLength) * scale;
            if (!visible(frustum, eye, px, py, pz, length)) {
                continue;
            }
            double w = length * kind.width / 2 / sideLength;
            int light = lightAt(mc, pos.set(x, y, z));
            float[] head = {(float) px, (float) py, (float) pz};
            float[] back = {(float) (-ax * length), (float) (-ay * length), (float) (-az * length)};
            float[] side = {(float) (sx * w), (float) (sy * w), (float) (sz * w)};
            switch (kind) {
                case SHOT -> streak(buffer, matrix, head, back, 0, 1, side, 1, LEAD, light);
                case ROCKET -> {
                    streak(buffer, matrix, head, back, 0, 0.6F, side, 1, SHAFT, light);
                    streak(buffer, matrix, head, back, 0.6F, 1, side, 2.2F, FIRE, LightTexture.FULL_BRIGHT);
                }
                default -> {
                    streak(buffer, matrix, head, back, 0, 0.1F, side, 1, HEAD, light);
                    streak(buffer, matrix, head, back, 0.1F, 0.75F, side, 1, SHAFT, light);
                    streak(buffer, matrix, head, back, 0.75F, 1, side, 1.6F, FLETCHING, light);
                }
            }
            drawn++;
        }
        RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        mc.gameRenderer.lightTexture().turnOnLightLayer();
        draw(buffer, drawn);

        // The smoke, see-through, after the solid shots: soft discs that swell, rise and fade; a flash as it is fired.
        BufferBuilder smoke = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
        int puffs = 0;
        for (Arrow a : ARROWS) { // a thrown stone's dust trail: a few fading quads along its own arc, nothing stored
            double u = (now - a.start()) / 1000.0 / a.seconds();
            if (u > 0 && (a.kind() == Kind.ROCK || a.kind() == Kind.BIG_ROCK)) {
                puffs += trail(smoke, matrix, a, u, eye, renderBlocks);
            }
        }
        for (Puff p : PUFFS) {
            double age = (now - p.start()) / 1000.0;
            if (age < 0) {
                continue;
            }
            double u = age / p.seconds();
            double y = p.y() + age * 0.4;
            double hx = p.x() - eye.x;
            double hy = y - eye.y;
            double hz = p.z() - eye.z;
            double distance = Math.sqrt(hx * hx + hy * hy + hz * hz);
            if (distance < 1e-3) {
                continue;
            }
            double scale = DistantDepth.drawDistance(distance, renderBlocks) / distance;
            double r = Math.max(p.size() * (0.5 + 1.1 * Math.sqrt(u)), distance * MIN_PUFF_PER_BLOCK) * scale;
            double px = hx * scale;
            double py = hy * scale;
            double pz = hz * scale;
            if (!visible(frustum, eye, px, py, pz, r)) {
                continue;
            }
            boolean flash = age < FLASH;
            float alpha = flash ? 0.9F : (float) (0.55 * Math.pow(1 - u, 1.5));
            disc(smoke, matrix, left, up, px, py, pz, flash ? r * 0.6 : r, flash ? FLASH_COLOUR : SMOKE, alpha, 0,
                    flash ? LightTexture.FULL_BRIGHT : lightAt(mc, pos.set(p.x(), y, p.z())));
            puffs++;
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        draw(smoke, puffs);
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        mc.gameRenderer.lightTexture().turnOffLightLayer();
        RenderSystem.enableCull();
    }

    /**
     * The trail behind stone {@code a}, {@code u} of the way along its flight: {@link #TRAIL_STEPS} quads back along
     * its arc over the last {@link #TRAIL_SECONDS}, narrowing and fading from the stone's own width. Returns quads drawn.
     */
    private static int trail(BufferBuilder buffer, Matrix4f matrix, Arrow a, double u, Vec3 eye, double renderBlocks) {
        double du = TRAIL_SECONDS / a.seconds();
        double lx = 0, ly = 0, lz = 0, lw = 0;
        float la = 0;
        int drawn = 0;
        for (int k = 0; k <= TRAIL_STEPS; k++) {
            double uk = Math.max(0, u - du * k / TRAIL_STEPS);
            double hx = a.from().x + (a.to().x - a.from().x) * uk - eye.x;
            double hy = a.from().y + (a.to().y - a.from().y) * uk + 4 * a.rise() * uk * (1 - uk) - eye.y;
            double hz = a.from().z + (a.to().z - a.from().z) * uk - eye.z;
            double distance = Math.sqrt(hx * hx + hy * hy + hz * hz);
            if (distance < 1e-3) {
                return drawn;
            }
            double scale = DistantDepth.drawDistance(distance, renderBlocks) / distance;
            double fade = 1 - k / (double) TRAIL_STEPS;
            double px = hx * scale, py = hy * scale, pz = hz * scale;
            double w = Math.max(a.kind().ball, distance * MIN_BALL_PER_BLOCK) * scale * fade;
            float alpha = (float) (0.45 * fade);
            if (k > 0) {
                double dx = px - lx, dy = py - ly, dz = pz - lz;
                double sx = dy * pz - dz * py, sy = dz * px - dx * pz, sz = dx * py - dy * px; // square to it and the sight line
                double s = Math.sqrt(sx * sx + sy * sy + sz * sz);
                if (s > 1e-9) {
                    sx /= s;
                    sy /= s;
                    sz /= s;
                    float r = (TRAIL >> 16 & 255) / 255F, g = (TRAIL >> 8 & 255) / 255F, b = (TRAIL & 255) / 255F;
                    int light = LightTexture.FULL_SKY;
                    buffer.addVertex(matrix, (float) (lx + sx * lw), (float) (ly + sy * lw), (float) (lz + sz * lw)).setColor(r, g, b, la).setLight(light);
                    buffer.addVertex(matrix, (float) (px + sx * w), (float) (py + sy * w), (float) (pz + sz * w)).setColor(r, g, b, alpha).setLight(light);
                    buffer.addVertex(matrix, (float) (px - sx * w), (float) (py - sy * w), (float) (pz - sz * w)).setColor(r, g, b, alpha).setLight(light);
                    buffer.addVertex(matrix, (float) (lx + sx * lw), (float) (ly + sy * lw), (float) (lz + sz * lw)).setColor(r, g, b, la).setLight(light);
                    buffer.addVertex(matrix, (float) (px - sx * w), (float) (py - sy * w), (float) (pz - sz * w)).setColor(r, g, b, alpha).setLight(light);
                    buffer.addVertex(matrix, (float) (lx - sx * lw), (float) (ly - sy * lw), (float) (lz - sz * lw)).setColor(r, g, b, la).setLight(light);
                    drawn++;
                }
            }
            lx = px;
            ly = py;
            lz = pz;
            lw = w;
            la = alpha;
        }
        return drawn;
    }

    private static void draw(BufferBuilder buffer, int drawn) {
        if (drawn == 0) {
            try (com.mojang.blaze3d.vertex.MeshData unused = buffer.build()) { // nothing to draw: let the buffer go
            }
            return;
        }
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private static boolean visible(net.minecraft.client.renderer.culling.Frustum frustum, Vec3 eye, double px, double py, double pz, double r) {
        return frustum.isVisible(new net.minecraft.world.phys.AABB(eye.x + px - r, eye.y + py - r, eye.z + pz - r,
                eye.x + px + r, eye.y + py + r, eye.z + pz + r));
    }

    /**
     * A disc facing the camera at (px, py, pz) from it, of radius {@code r}: {@code alpha} at its middle, {@code rim}
     * times that at its edge (1: a solid round shot; 0: a soft puff).
     */
    private static void disc(BufferBuilder buffer, Matrix4f matrix, Vector3f left, Vector3f up, double px, double py, double pz,
                             double r, int rgb, float alpha, float rim, int light) {
        float red = (rgb >> 16 & 255) / 255F;
        float green = (rgb >> 8 & 255) / 255F;
        float blue = (rgb & 255) / 255F;
        int sides = 8;
        for (int i = 0; i < sides; i++) {
            double a0 = Math.PI * 2 * i / sides;
            double a1 = Math.PI * 2 * (i + 1) / sides;
            buffer.addVertex(matrix, (float) px, (float) py, (float) pz).setColor(red, green, blue, alpha).setLight(light);
            rimVertex(buffer, matrix, left, up, px, py, pz, r, a0).setColor(red, green, blue, alpha * rim).setLight(light);
            rimVertex(buffer, matrix, left, up, px, py, pz, r, a1).setColor(red, green, blue, alpha * rim).setLight(light);
        }
    }

    private static com.mojang.blaze3d.vertex.VertexConsumer rimVertex(BufferBuilder buffer, Matrix4f matrix, Vector3f left, Vector3f up,
                                                                       double px, double py, double pz, double r, double angle) {
        double c = Math.cos(angle) * r;
        double s = Math.sin(angle) * r;
        return buffer.addVertex(matrix, (float) (px + left.x() * c + up.x() * s), (float) (py + left.y() * c + up.y() * s),
                (float) (pz + left.z() * c + up.z() * s));
    }

    /**
     * The part of a streak from {@code a} to {@code b} of the way along {@code back} from its head, {@code side}
     * (times {@code widen}) to each side: two triangles.
     */
    private static void streak(BufferBuilder buffer, Matrix4f matrix, float[] head, float[] back, float a, float b, float[] side,
                               float widen, int rgb, int light) {
        float r = (rgb >> 16 & 255) / 255F;
        float g = (rgb >> 8 & 255) / 255F;
        float bl = (rgb & 255) / 255F;
        float px = head[0] + back[0] * a, py = head[1] + back[1] * a, pz = head[2] + back[2] * a;
        float qx = head[0] + back[0] * b, qy = head[1] + back[1] * b, qz = head[2] + back[2] * b;
        float wx = side[0] * widen, wy = side[1] * widen, wz = side[2] * widen;
        buffer.addVertex(matrix, px + wx, py + wy, pz + wz).setColor(r, g, bl, 1F).setLight(light);
        buffer.addVertex(matrix, qx + wx, qy + wy, qz + wz).setColor(r, g, bl, 1F).setLight(light);
        buffer.addVertex(matrix, qx - wx, qy - wy, qz - wz).setColor(r, g, bl, 1F).setLight(light);
        buffer.addVertex(matrix, px + wx, py + wy, pz + wz).setColor(r, g, bl, 1F).setLight(light);
        buffer.addVertex(matrix, qx - wx, qy - wy, qz - wz).setColor(r, g, bl, 1F).setLight(light);
        buffer.addVertex(matrix, px - wx, py - wy, pz - wz).setColor(r, g, bl, 1F).setLight(light);
    }

    /** The light where the shot flies, as for the figures; out past the chunks the client has, open sky. */
    private static int lightAt(Minecraft mc, BlockPos pos) {
        if (mc.level == null || !mc.level.hasChunkAt(pos)) {
            return LightTexture.FULL_SKY;
        }
        return LevelRenderer.getLightColor(mc.level, pos);
    }
}
