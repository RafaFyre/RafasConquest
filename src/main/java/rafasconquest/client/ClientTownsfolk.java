package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rafasconquest.core.StreetWalks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The townsfolk of the districts near this player, which the server names ({@link rafasconquest.npc.Townsfolk}): the
 * client alone walks them, each back and forth along their stretch of road by the game time, and draws them instanced
 * as {@link RecruitFigures}. They are not entities: nothing ticks them on the server, and nothing can touch them.
 */
public final class ClientTownsfolk {
    private static final int SKINS = 20;
    /** Walking speed, blocks a tick (a stroll); each townsperson a little faster or slower. */
    private static final double SPEED = 0.045;
    /** Drawn within this many blocks. */
    private static final double DRAWN_WITHIN = 96;
    /** One walk cycle of the game's limb swing ({@code cos(swing * 0.6662)}), kept small for the shader's floats. */
    private static final double CYCLE = 2 * Math.PI / 0.6662;

    private record Folk(StreetWalks.Walk walk, int skin, double start, double speed) {
    }

    private static List<Folk> folk = List.of();

    private ClientTownsfolk() {
    }

    /** The stretches the server sent: each gets the same skin, pace and starting place every time it is sent. */
    public static void set(List<StreetWalks.Walk> walks) {
        List<Folk> next = new ArrayList<>(walks.size());
        for (StreetWalks.Walk w : walks) {
            Random random = new Random(Double.hashCode(w.x0()) * 31L + Double.hashCode(w.z0()) * 961L + Double.hashCode(w.x1()));
            next.add(new Folk(w, random.nextInt(SKINS), random.nextDouble() * 2 * w.length(), SPEED * (0.75 + random.nextDouble() * 0.5)));
        }
        next.sort(java.util.Comparator.comparingInt(Folk::skin)); // one run per skin
        folk = next;
    }

    public static void clear() {
        folk = List.of();
    }

    /** How far along a stretch of {@code length} someone is after walking {@code walked} blocks, turning at each end; negative: walking back. */
    static double along(double walked, double length) {
        if (length <= 0) {
            return 0;
        }
        double p = walked % (2 * length);
        return p < length ? p : -(2 * length - p);
    }

    static void render(PoseStack pose, Camera camera, Frustum frustum, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (folk.isEmpty() || mc.level == null || mc.level.dimension() != Level.OVERWORLD) {
            return;
        }
        Vec3 eye = camera.getPosition();
        double ticks = mc.level.getGameTime() + partialTick;
        RecruitFigures.begin();
        int skin = -1;
        for (Folk f : folk) {
            StreetWalks.Walk w = f.walk();
            double length = w.length();
            double a = along(f.start() + f.speed() * ticks, length);
            double t = length == 0 ? 0 : Math.abs(a) / length;
            double x = w.x0() + (w.x1() - w.x0()) * t;
            double z = w.z0() + (w.z1() - w.z0()) * t;
            double dx = x - eye.x;
            double dz = z - eye.z;
            if (dx * dx + dz * dz > DRAWN_WITHIN * DRAWN_WITHIN) {
                continue;
            }
            int bx = net.minecraft.util.Mth.floor(x);
            int bz = net.minecraft.util.Mth.floor(z);
            if (!mc.level.hasChunk(bx >> 4, bz >> 4)) {
                continue;
            }
            // MOTION_BLOCKING: the no-leaves map is server-only, empty on the client.
            int y = mc.level.getHeight(Heightmap.Types.MOTION_BLOCKING, bx, bz);
            if (!frustum.isVisible(new AABB(x - 0.5, y, z - 0.5, x + 0.5, y + 2, z + 0.5))) {
                continue;
            }
            if (f.skin() != skin) {
                if (!RecruitFigures.townsfolk(f.skin())) {
                    return;
                }
                skin = f.skin();
            }
            double heading = a < 0 ? -1 : 1;
            float fx = (float) (heading * (w.x1() - w.x0()) / Math.max(length, 1e-9));
            float fz = (float) (heading * (w.z1() - w.z0()) / Math.max(length, 1e-9));
            // As the game animates a walk: 4 times the blocks a tick walked, at most 1.
            float amount = (float) Math.min(4 * f.speed(), 1);
            float swing = (float) ((amount * ticks) % CYCLE);
            RecruitFigures.add((float) dx, (float) (y - eye.y), (float) dz, 1, fx, fz, swing, amount,
                    FigureBatches.lightColour(DistantArmyRenderer.lightAt(mc, x, y, z)), 0, 0);
        }
        RecruitFigures.draw(pose.last().pose(), false);
    }
}
