package rafasconquest.client;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.enums.rendering.EDhApiBlockMaterial;
import com.seibel.distanthorizons.api.enums.worldGeneration.EDhApiLevelType;
import com.seibel.distanthorizons.api.interfaces.config.IDhApiConfig;
import com.seibel.distanthorizons.api.interfaces.render.IDhApiCustomRenderObjectFactory;
import com.seibel.distanthorizons.api.interfaces.render.IDhApiCustomRenderRegister;
import com.seibel.distanthorizons.api.interfaces.render.IDhApiRenderableBoxGroup;
import com.seibel.distanthorizons.api.interfaces.render.IDhApiRenderProxy;
import com.seibel.distanthorizons.api.interfaces.world.IDhApiLevelWrapper;
import com.seibel.distanthorizons.api.interfaces.world.IDhApiWorldProxy;
import com.seibel.distanthorizons.api.objects.math.DhApiVec3d;
import com.seibel.distanthorizons.api.objects.render.DhApiRenderableBox;
import com.seibel.distanthorizons.api.objects.render.DhApiRenderableBoxGroupShading;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.battle.FormationSnapshot.Sprite;


import java.awt.Color;
import java.util.ArrayList;

/**
 * Draws the soldiers beyond the game's own terrain inside Distant Horizons' terrain, as little figures of coloured
 * boxes, so its far hills hide them and they stand on its ground. Nearer soldiers stay with
 * {@link DistantArmyRenderer}. The figures are rebuilt ten times a second from where {@link DistantArmies} says each
 * formation is. Only {@link DistantHorizonsLink} calls this, and only when Distant Horizons is installed.
 */
final class DistantHorizonsArmies {
    private static final long REBUILD_MILLIS = 100;
    private static final Color SKIN = new Color(0xBD8E6E);
    private static final Color GOLD = new Color(0xF9D84A);
    private static final Color POLE = new Color(0x6B4A2B);
    private static final Color HOOVES = new Color(0x3B2A1C);

    /** How often to look again for the level Distant Horizons is drawing, which changes with the dimension. */
    private static final long FIND_MILLIS = 1000;
    /** Distant Horizons' vanilla fade starts at this many times (its near clip plane plus a chunk). */
    private static final double FADE_START_FACTOR = 1.5;
    private static final double FADE_PAST_NEAR_CLIP = 16;

    /** Where our figures are registered, and the figures. Render thread only. */
    @Nullable
    private static IDhApiCustomRenderRegister registeredWith;
    @Nullable
    private static IDhApiRenderableBoxGroup figures;
    private static long builtAt;
    private static long foundAt;
    private static boolean announcedDrawing;

    private DistantHorizonsArmies() {
    }

    /** Fails (with a linkage error) if the installed Distant Horizons has no generic rendering API. */
    static void register() {
        DhApi.Delayed.class.getName();
        IDhApiRenderableBoxGroup.class.getName();
    }

    /** Whether Distant Horizons is drawing its terrain and the objects mods add to it. */
    static boolean drawing() {
        IDhApiConfig configs = DhApi.Delayed.configs;
        return figures != null && configs != null && configs.graphics().renderingEnabled().getValue()
                && configs.graphics().genericRendering().renderingEnabled().getValue();
    }

    /**
     * Where Distant Horizons' fade of the world into its terrain starts, as its fade shader works it out: half again
     * the distance a chunk beyond where its terrain begins. The fade's on/off setting isn't in its API, so this
     * assumes it is on (its default); if it is off, figures just take over a little early.
     */
    static double fadeStart(float partialTick) {
        IDhApiConfig configs = DhApi.Delayed.configs;
        IDhApiRenderProxy render = DhApi.Delayed.renderProxy;
        if (configs == null || render == null || !configs.graphics().renderingEnabled().getValue()) {
            return 0;
        }
        try {
            return (render.getNearClipPlaneDistanceInBlocks(partialTick) + FADE_PAST_NEAR_CLIP) * FADE_START_FACTOR;
        } catch (IllegalStateException e) {
            return 0; // not set up yet
        }
    }

    static void update() {
        long now = Util.getMillis();
        if (now - foundAt >= FIND_MILLIS) {
            foundAt = now;
            follow(renderRegister());
        }
        IDhApiRenderableBoxGroup group = figures;
        if (group == null || now - builtAt < REBUILD_MILLIS) {
            return;
        }
        builtAt = now;
        boolean hadAny = !group.isEmpty();
        group.clear();
        if (drawing()) {
            build(group);
        }
        if (hadAny || !group.isEmpty()) {
            group.triggerBoxChange();
        }
        if (!announcedDrawing && !group.isEmpty()) {
            announcedDrawing = true;
            RafasConquest.LOGGER.info("Drawing {} battle boxes in Distant Horizons' terrain", group.size());
        }
    }

    /**
     * The render register of the level Distant Horizons draws for the client's dimension. In singleplayer that is
     * the integrated server's level (Distant Horizons draws one level for both sides), and the client's own level
     * wrapper has none; in multiplayer it is the client's. So take whichever of the dimension's levels has one,
     * preferring the client's.
     */
    @Nullable
    private static IDhApiCustomRenderRegister renderRegister() {
        Minecraft mc = Minecraft.getInstance();
        IDhApiWorldProxy world = DhApi.Delayed.worldProxy;
        if (mc.level == null || world == null || !world.worldLoaded()) {
            return null;
        }
        String dimension = mc.level.dimension().location().toString();
        IDhApiCustomRenderRegister best = null;
        try {
            for (IDhApiLevelWrapper level : world.getAllLoadedLevelWrappers()) {
                String name = level.getDimensionName();
                if (!dimension.equals(name) && !mc.level.dimension().location().getPath().equals(name)) {
                    continue;
                }
                IDhApiCustomRenderRegister register = level.getRenderRegister();
                if (register != null && (best == null || level.getLevelType() == EDhApiLevelType.CLIENT_LEVEL)) {
                    best = register;
                }
            }
        } catch (IllegalStateException e) {
            return null; // no world loaded after all
        }
        return best;
    }

    /** Moves our figures to the register Distant Horizons now draws from (or drops them when there is none). */
    private static void follow(@Nullable IDhApiCustomRenderRegister register) {
        if (register == registeredWith) {
            return;
        }
        if (registeredWith != null && figures != null) {
            try {
                registeredWith.remove(figures.getId());
            } catch (RuntimeException e) {
                // The old level is already gone, and our figures with it.
            }
        }
        registeredWith = null;
        figures = null;
        IDhApiCustomRenderObjectFactory factory = DhApi.Delayed.customRenderObjectFactory;
        if (register == null || factory == null) {
            return;
        }
        try {
            IDhApiRenderableBoxGroup group = factory.createAbsolutePositionedGroup(RafasConquest.MOD_ID + ":distant_armies", new ArrayList<>());
            group.setSkyLight(15);
            group.setBlockLight(0);
            group.setShading(DhApiRenderableBoxGroupShading.getDefaultShaded());
            register.add(group);
            figures = group;
            RafasConquest.LOGGER.info("Battle figures added to Distant Horizons' terrain");
        } catch (RuntimeException e) {
            RafasConquest.LOGGER.warn("Couldn't add battle figures to Distant Horizons", e);
        }
        registeredWith = register; // on failure too, so it isn't retried every second
    }

    private static void build(IDhApiRenderableBoxGroup group) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 eye = mc.gameRenderer.getMainCamera().getPosition();
        double renderBlocks = mc.options.getEffectiveRenderDistance() * 16.0;
        FormationSnapshot.Positions positions = DistantArmies.positions(mc.getFrameTime());
        for (FormationSnapshot formation : DistantArmies.now()) {
            for (Sprite sprite : formation.place(positions, false)) {
                if (DistantArmyRenderer.onGameTerrain(sprite.x() - eye.x, sprite.z() - eye.z, renderBlocks)) {
                    continue;
                }
                new Figure(group, formation, sprite).build();
            }
        }
    }

    /**
     * One soldier as boxes, in the soldier's own frame: {@code right} along its rank, {@code back} away from the enemy,
     * {@code up} from the feet. Boxes can't turn, so each is the upright box that holds the turned one.
     */
    private record Figure(IDhApiRenderableBoxGroup group, FormationSnapshot f, Sprite at) {
        void build() {
            Color uniform = new Color(f.rgb());
            Color shade = new Color(darker(f.rgb()));
            Color mark = new Color(f.mark());
            switch (f.kind()) {
                case CAVALRY -> {
                    box(0, 0, 0.2, 0.25, 0, 0.7, HOOVES, EDhApiBlockMaterial.UNKNOWN);
                    box(0, 0, 0.3, 0.6, 0.7, 1.3, uniform, EDhApiBlockMaterial.UNKNOWN); // the horse, in leather armour
                    box(0, -0.75, 0.15, 0.2, 1.1, 1.7, uniform, EDhApiBlockMaterial.UNKNOWN); // its head, forward
                    box(0, 0, 0.25, 0.15, 1.3, 1.65, shade, EDhApiBlockMaterial.UNKNOWN);
                    box(0, 0, 0.3, 0.2, 1.65, 2.3, uniform, EDhApiBlockMaterial.UNKNOWN);
                    head(2.3, uniform, mark);
                }
                case GENERAL -> {
                    body(uniform, shade);
                    box(0, 0, 0.22, 0.22, 1.45, 1.7, SKIN, EDhApiBlockMaterial.UNKNOWN);
                    box(0, 0, 0.27, 0.27, 1.7, 2.0, GOLD, EDhApiBlockMaterial.METAL);
                    box(0, 0.3, 0.04, 0.04, 1.0, 3.3, POLE, EDhApiBlockMaterial.WOOD); // the banner on the back
                    box(0.3, 0.3, 0.27, 0.04, 2.4, 3.25, mark, EDhApiBlockMaterial.UNKNOWN);
                }
                case BEARER -> {
                    body(uniform, shade);
                    head(1.45, uniform, mark);
                    box(0.45, 0, 0.04, 0.04, 0.3, 3.3, POLE, EDhApiBlockMaterial.WOOD);
                    box(0.77, 0, 0.28, 0.04, 2.3, 3.25, mark, EDhApiBlockMaterial.UNKNOWN);
                }
                default -> {
                    body(uniform, shade);
                    head(1.45, uniform, mark);
                }
            }
        }

        private void body(Color uniform, Color shade) {
            box(0, 0, 0.25, 0.13, 0, 0.75, shade, EDhApiBlockMaterial.UNKNOWN);
            box(0, 0, 0.3, 0.15, 0.75, 1.45, uniform, EDhApiBlockMaterial.UNKNOWN);
        }

        /** A face and a leather cap; a leader wears the faction banner instead, standing tall on the head. */
        private void head(double bottom, Color cap, Color mark) {
            box(0, 0, 0.22, 0.22, bottom, bottom + 0.3, SKIN, EDhApiBlockMaterial.UNKNOWN);
            if (at.leader()) {
                box(0, 0, 0.04, 0.04, bottom + 0.3, bottom + 0.55, POLE, EDhApiBlockMaterial.WOOD);
                box(0, 0, 0.28, 0.04, bottom + 0.55, bottom + 1.45, mark, EDhApiBlockMaterial.UNKNOWN);
            } else {
                box(0, 0, 0.25, 0.25, bottom + 0.3, bottom + 0.48, cap, EDhApiBlockMaterial.UNKNOWN);
            }
        }

        /**
         * A box centred {@code right} to the soldier's right and {@code back} behind it, {@code halfWidth} to each side
         * along the rank and {@code halfDepth} front and back, from {@code bottom} to {@code top}.
         */
        private void box(double right, double back, double halfWidth, double halfDepth, double bottom, double top,
                         Color color, EDhApiBlockMaterial material) {
            double rightX = -f.facingZ();
            double rightZ = f.facingX();
            double cx = at.x() + rightX * right - f.facingX() * back;
            double cz = at.z() + rightZ * right - f.facingZ() * back;
            double hx = Math.abs(rightX) * halfWidth + Math.abs(f.facingX()) * halfDepth;
            double hz = Math.abs(rightZ) * halfWidth + Math.abs(f.facingZ()) * halfDepth;
            group.add(new DhApiRenderableBox(new DhApiVec3d(cx - hx, at.y() + bottom, cz - hz),
                    new DhApiVec3d(cx + hx, at.y() + top, cz + hz), color, material));
        }

        private static int darker(int rgb) {
            return (int) ((rgb >> 16 & 255) * 0.7) << 16 | (int) ((rgb >> 8 & 255) * 0.7) << 8 | (int) ((rgb & 255) * 0.7);
        }
    }
}
