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
import rafasconquest.style.ShipHulls;
import net.minecraft.world.level.EmptyBlockGetter;

import java.util.ArrayList;
import java.util.HashSet;
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

    public static void render(PoseStack pose, Camera camera, net.minecraft.client.renderer.culling.Frustum frustum, float partialTick) {
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
        player(mc, pose, eye, rightX, rightZ, renderBlocks, partialTick);
        List<FormationSnapshot> formations = DistantArmies.now();
        if (formations.isEmpty()) {
            return;
        }

        List<List<FigureMotion.Placed>> figures = FigureMotion.step(formations, DistantArmies::target, DistantArmies.positions(partialTick), net.minecraft.Util.getMillis());
        boolean[] seen = new boolean[formations.size()];
        int[] light = new int[formations.size()];
        boolean[] flat = new boolean[formations.size()];
        for (int f = 0; f < seen.length; f++) {
            FormationSnapshot formation = formations.get(f);
            seen[f] = inView(frustum, eye, renderBlocks, figures.get(f), formation.engine() == null ? FIGURE_REACH : ENGINE_REACH);
            // The light where the block stands, for all its figures: each figure's own costs a lookup apiece.
            light[f] = seen[f] ? lightAt(mc, formation.x(), formation.y(), formation.z()) : 0;
            flat[f] = flat(formation, eye);
        }
        if (FigureBatches.dump) {
            FigureBatches.dump = false;
            dump(formations, seen, light, flat);
        }
        pass(mc, pose, eye, rightX, rightZ, renderBlocks, formations, figures, seen, light, flat, null);
        Set<Integer> selected = BattleScreen.selectedFormations();
        if (!selected.isEmpty()) {
            pass(mc, pose, eye, rightX, rightZ, renderBlocks, formations, figures, seen, light, flat, selected);
        }
    }

    /** For {@code /pvefigures}: what each block on screen is coloured from this frame, to the log. */
    private static void dump(List<FormationSnapshot> formations, boolean[] seen, int[] light, boolean[] flat) {
        float[] c = RenderSystem.getShaderColor();
        rafasconquest.RafasConquest.LOGGER.info("[figures] test {}: shader colour {} {} {} {}", FigureBatches.debug, c[0], c[1], c[2], c[3]);
        for (int f = 0; f < formations.size(); f++) {
            FormationSnapshot s = formations.get(f);
            if (seen[f]) {
                rafasconquest.RafasConquest.LOGGER.info("[figures] block {} {} colour {} light block {} sky {} (lightmap ABGR {}), {}{}", s.id(), s.kind(),
                        String.format("%06X", s.rgb()), light[f] >> 4 & 15, light[f] >> 20 & 15,
                        String.format("%08X", FigureBatches.lightColour(light[f])), flat[f] ? "flat" : "3D",
                        RecruitFigures.draws(s, flat[f]) ? ", " + RecruitFigures.describe(s) : "");
            }
        }
    }

    /** Blocks drawn as flat sprites, by formation id. Render thread only. */
    private static final Set<Integer> FLAT = new HashSet<>();
    /** How far either side of {@link SoldierFigure#SPRITE_PAST} a block keeps how it was drawn, so it doesn't flicker. */
    private static final double FLAT_BAND = 15;

    /**
     * Whether a block is drawn as flat sprites rather than 3D figures: all of it at once, by where it stands, so no
     * block is half one and half the other.
     */
    private static boolean flat(FormationSnapshot formation, Vec3 eye) {
        double distance = Math.hypot(formation.x() - eye.x, formation.z() - eye.z);
        if (distance > SoldierFigure.SPRITE_PAST + FLAT_BAND) {
            FLAT.add(formation.id());
        } else if (distance < SoldierFigure.SPRITE_PAST - FLAT_BAND) {
            FLAT.remove(formation.id());
        }
        return FLAT.contains(formation.id());
    }

    /** Room about a block's figures' feet for their height, flags and glow, in blocks. */
    private static final double FIGURE_REACH = 4;
    /** The same about a block with a siege engine: a tower stands well ahead of its crew, and tall. */
    private static final double ENGINE_REACH = 18;

    /**
     * Whether any of a block's figures can be on screen: the box about them, pulled in as they are drawn
     * ({@link DistantDepth}), touches the camera's view.
     */
    private static boolean inView(net.minecraft.client.renderer.culling.Frustum frustum, Vec3 eye, double renderBlocks,
                                  List<FigureMotion.Placed> figures, double reach) {
        if (figures.isEmpty()) {
            return false;
        }
        double x0 = Double.MAX_VALUE, y0 = Double.MAX_VALUE, z0 = Double.MAX_VALUE;
        double x1 = -Double.MAX_VALUE, y1 = -Double.MAX_VALUE, z1 = -Double.MAX_VALUE;
        for (FigureMotion.Placed p : figures) {
            x0 = Math.min(x0, p.x());
            y0 = Math.min(y0, p.y());
            z0 = Math.min(z0, p.z());
            x1 = Math.max(x1, p.x());
            y1 = Math.max(y1, p.y());
            z1 = Math.max(z1, p.z());
        }
        double cx = (x0 + x1) / 2 - eye.x;
        double cy = (y0 + y1) / 2 - eye.y;
        double cz = (z0 + z1) / 2 - eye.z;
        double distance = Math.max(1, Math.sqrt(cx * cx + cy * cy + cz * cz));
        // Pulled in along the line of sight, shrunk by the same factor: the same on screen, and always short of the
        // view's far end (figures drawn at their real distance, with the merged depth, may lie past it).
        double s = DistantDepth.drawDistance(distance, renderBlocks) / distance;
        double hx = ((x1 - x0) / 2 + reach) * s;
        double hy = ((y1 - y0) / 2 + reach) * s;
        double hz = ((z1 - z0) / 2 + reach) * s;
        double px = eye.x + cx * s;
        double py = eye.y + cy * s;
        double pz = eye.z + cz * s;
        return frustum.isVisible(new net.minecraft.world.phys.AABB(px - hx, py - hy, pz - hz, px + hx, py + hy, pz + hz));
    }

    /**
     * The player seen from a camera away from their body (the battle screen's): a sprite where the game stops drawing
     * them, in their army's colour with a gold cap, on a horse when riding; glowing when selected.
     */
    private static void player(Minecraft mc, PoseStack pose, Vec3 eye, float rightX, float rightZ, double renderBlocks, float partialTick) {
        net.minecraft.world.entity.player.Player p = mc.player;
        if (p == null || mc.getCameraEntity() == p) {
            return;
        }
        Vec3 feet = p.getPosition(partialTick);
        double dx = feet.x - eye.x;
        double dy = feet.y - eye.y;
        double dz = feet.z - eye.z;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (distance <= Math.min(handover, drawnWithin(EntityType.PLAYER))) {
            return; // the game draws the player itself
        }
        double scale = DistantOcclusion.scale(distance, renderBlocks);
        int colour = BattleScreen.playerColour();
        int uniform = colour < 0 ? 0x3C44AA : colour; // no units known yet: a plain blue
        for (boolean glow : BattleScreen.playerSelected() ? new boolean[]{false, true} : new boolean[]{false}) {
            BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
            Figure f = new Figure(buffer, pose.last().pose(), rightX, rightZ);
            f.tint = glow ? 0xFFFFFF : -1;
            if (glow) {
                double back = 1 + HALO_BEHIND / Math.max(distance, 1);
                f.at((float) (dx * scale * back), (float) ((dy - 0.1) * scale * back), (float) (dz * scale * back),
                        (float) scale * HALO_GROWTH, LightTexture.FULL_BRIGHT);
            } else {
                f.at((float) (dx * scale), (float) (dy * scale), (float) (dz * scale), (float) scale, lightAt(mc, feet.x, feet.y, feet.z));
            }
            if (p.isPassenger()) { // as a cavalry sprite
                f.rect(0, 0, 1.2F, 0.7F, HOOVES);
                f.rect(0, 0.7F, 1.5F, 0.6F, uniform);
                f.rect(0, 1.3F, 0.5F, 0.4F, darker(uniform));
                f.rect(0, 1.7F, 0.6F, 0.6F, uniform);
                head(f, 2.3F, GOLD, 0, false);
            } else {
                body(f, uniform, darker(uniform));
                head(f, 1.45F, GOLD, 0, false);
            }
            finish(mc, buffer, 1);
        }
    }

    /** How much bigger a selected figure's glow is than the figure, and how far behind it (in blocks, at full size). */
    private static final float HALO_GROWTH = 1.18F;
    private static final double HALO_BEHIND = 0.35;

    /**
     * Draws the figures, or ({@code halo} not null) the glow of those in the formations named there: each figure again,
     * a little bigger and a little further from the camera, in full-bright white, so it shows as an outline about the
     * figure as the game's glowing outline does about a real soldier.
     */
    private static void pass(Minecraft mc, PoseStack pose, Vec3 eye, float rightX, float rightZ, double renderBlocks,
                             List<FormationSnapshot> formations, List<List<FigureMotion.Placed>> figures, boolean[] seen, int[] light,
                             boolean[] flat, @javax.annotation.Nullable Set<Integer> halo) {
        FigureBatches.begin();
        RecruitFigures.begin();
        boolean glow = halo != null;
        for (int f = 0; f < formations.size(); f++) {
            FormationSnapshot formation = formations.get(f);
            if (!seen[f] || glow && !halo.contains(formation.id())) {
                continue;
            }
            FigureGear gear = DistantArmies.gear(formation.id());
            int[] palette = SoldierFigure.Palette.of(formation, gear).slots();
            SoldierFigure.Shape rank = SoldierFigure.Shape.of(formation, gear, false, flat[f]);
            SoldierFigure.Shape leader = SoldierFigure.Shape.of(formation, gear, true, flat[f]);
            // Foot soldiers near enough for 3D look as the real recruits do; their leader keeps its banner figure.
            boolean recruits = RecruitFigures.draws(formation, flat[f]) && RecruitFigures.block(formation, gear);
            int flags = recruits ? RecruitFigures.flags(formation) | (glow ? RecruitFigures.GLOW : 0) : 0;
            int lit = FigureBatches.lightColour(glow ? LightTexture.FULL_BRIGHT : light[f]);
            List<FigureMotion.Placed> placed = figures.get(f);
            for (int i = 0; i < placed.size(); i++) {
                FigureMotion.Placed sprite = placed.get(i);
                double dx = sprite.x() - eye.x;
                double dy = sprite.y() - eye.y;
                double dz = sprite.z() - eye.z;
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (sprite.real() && distance <= handover) {
                    continue; // the client has this soldier, close enough to draw it itself (and glow it)
                }
                double scale = DistantOcclusion.scale(distance, renderBlocks);
                double back = glow ? 1 + HALO_BEHIND / Math.max(distance, 1) : 1;
                // A flat figure faces the camera: its right is the camera's right.
                float fx = flat[f] ? rightZ : (float) sprite.fx();
                float fz = flat[f] ? -rightX : (float) sprite.fz();
                if (recruits && !sprite.leader()) {
                    RecruitFigures.add((float) (dx * scale * back), (float) ((glow ? dy - 0.1 : dy) * scale * back),
                            (float) (dz * scale * back), (float) scale * (glow ? HALO_GROWTH : 1), fx, fz, sprite.swing(),
                            sprite.amount(), lit, flags,
                            Math.floorMod(formation.id() * 31 + i * 17, 100) / 100F);
                    continue;
                }
                FigureBatches.add(sprite.leader() ? leader : rank, palette, (float) (dx * scale * back),
                        (float) ((glow ? dy - 0.1 : dy) * scale * back), (float) (dz * scale * back),
                        (float) scale * (glow ? HALO_GROWTH : 1), fx, fz, lit, glow);
            }
            if (formation.engine() != null) {
                engines(mc, formation, formation.engine(), placed, palette, eye, renderBlocks, lit, glow);
            }
        }
        FigureBatches.draw(pose.last().pose());
        RecruitFigures.draw(pose.last().pose(), glow);
    }

    /**
     * A formation's siege engine, drawn in the same instanced batches as the figures: a tower, ram or ladder where it
     * stands; a train's engines under every crew-th of its soldiers, as the real ones are set up. Not where the game
     * draws the real one.
     */
    private static void engines(Minecraft mc, FormationSnapshot formation, FormationSnapshot.Engine engine, List<FigureMotion.Placed> placed,
                                int[] palette, Vec3 eye, double renderBlocks, int lit, boolean glow) {
        SoldierFigure.Shape shape = SoldierFigure.Shape.engine(engine.kind(), engine.up());
        float size = SoldierFigure.engineSize(engine.kind());
        if (!engine.withCrew()) {
            engine(mc, shape, palette, engine.x(), engine.y(), engine.z(), engine.facingX(), engine.facingZ(), size, eye, renderBlocks, lit, glow);
            return;
        }
        int crew = engine.kind() == FormationSnapshot.LADDER ? placed.size() : rafasconquest.battle.Machine.values()[engine.kind()].crew;
        for (int i = 0; i < placed.size(); i += Math.max(1, crew)) {
            FigureMotion.Placed p = placed.get(i);
            engine(mc, shape, palette, p.x(), p.y(), p.z(), formation.facingX(), formation.facingZ(), size, eye, renderBlocks, lit, glow);
        }
    }

    private static void engine(Minecraft mc, SoldierFigure.Shape shape, int[] palette, double x, double y, double z, double fx, double fz,
                               float size, Vec3 eye, double renderBlocks, int lit, boolean glow) {
        double dx = x - eye.x;
        double dy = y - eye.y;
        double dz = z - eye.z;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (distance <= handover && realEngineNear(mc, x, y, z)) {
            return; // the game draws the real one
        }
        double scale = DistantOcclusion.scale(distance, renderBlocks);
        double back = glow ? 1 + HALO_BEHIND / Math.max(distance, 1) : 1;
        FigureBatches.add(shape, palette, (float) (dx * scale * back), (float) ((glow ? dy - 0.1 : dy) * scale * back), (float) (dz * scale * back),
                (float) scale * size * (glow ? HALO_GROWTH : 1), (float) fx, (float) fz, lit, glow);
    }

    /** Whether the client has a real Siegeworks engine about this spot (a tower stands up to its height over it). */
    private static boolean realEngineNear(Minecraft mc, double x, double y, double z) {
        return mc.level != null && !mc.level.getEntities((Entity) null, new net.minecraft.world.phys.AABB(x - 4, y - 4, z - 4, x + 4, y + 16, z + 4),
                e -> rafasconquest.battle.Machine.MOD.equals(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).getNamespace())).isEmpty();
    }

    /**
     * Ships beyond the real ones ({@link DistantShips}): out to {@link DistantShips#SPRITE_PAST} a box for each block of
     * the hull, turned to its heading; past that a flat ship turned to face the camera, a hull under a mast and sail.
     * Both are models drawn instanced by {@link FigureBatches}, as the soldiers are.
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
        float rightX = (float) (-left.x() / length);
        float rightZ = (float) (-left.z() / length);
        double dy = DistantShips.keel() - eye.y;
        int lit = FigureBatches.lightColour(LightTexture.FULL_SKY);
        FigureBatches.begin();
        for (DistantShips.Far ship : ships) {
            double dx = ship.x() - eye.x;
            double dz = ship.z() - eye.z;
            double flat = Math.hypot(dx, dz);
            if (ship.shownReal(flat)) {
                continue; // Sable draws it
            }
            double distance = Math.hypot(flat, dy);
            float scale = (float) DistantOcclusion.scale(distance, renderBlocks);
            if (ship.real()) {
                // The real one is still drawn here, but Distant Horizons fades it to holes, and its depth hid this copy:
                // drawn 4 blocks nearer and as much smaller, it looks the same and covers it.
                scale *= (float) Math.max(0.5, (distance - REAL_SHIP_PULL) / distance);
            }
            float x = (float) dx * scale;
            float y = (float) dy * scale;
            float z = (float) dz * scale;
            if (flat <= DistantShips.SPRITE_PAST) {
                List<ShipHulls.Part> parts = ship.parts(); // keyed by its parts: a lane's list is rebuilt every few seconds
                FigureBatches.add(parts, () -> hullModel(parts), FigureBatches.shipPalette(ship.fore(), ship.port(), ship.lift()),
                        x, y, z, scale, (float) Math.sin(ship.yaw()), (float) Math.cos(ship.yaw()), lit, FigureBatches.SHIP);
            } else { // too far for its tilt or bob to show
                ShipSprite sprite = new ShipSprite(ship.size().length, ship.warship());
                FigureBatches.add(sprite, sprite::model, NO_PALETTE, x, y, z, scale, rightZ, -rightX, lit, 0);
            }
        }
        FigureBatches.draw(pose.last().pose());
    }

    /** How many blocks nearer the camera a real ship's copy is drawn, so it covers the real one's depth. */
    private static final double REAL_SHIP_PULL = 4;

    /** Ships take their colours from their blocks, not a palette. */
    private static final int[] NO_PALETTE = new int[8];

    /** A far ship's flat model, one per hull length and kind. */
    private record ShipSprite(int hull, boolean warship) {
        int[] model() {
            FigureBatches.Corners f = new FigureBatches.Corners();
            f.rect(0, 0, hull, 3, HULL);
            f.rect(0, 3, 0.6F, hull / 2F + 1, POLE);
            f.rect(0, 4.5F, hull * 0.55F, hull / 2F - 2, SAIL);
            if (warship) {
                f.rect(1.2F, hull / 2F + 2.5F, 2.4F, 1, PENNANT);
            }
            return f.model();
        }
    }

    /**
     * A ship's 3D level of detail: its hull's faces open to the air, in the block's map colour, in its own frame (z to
     * the bow). No bottoms: they're under water. Each face is a hair inside its block, so where the real ship still
     * shows, it wins and nothing flickers.
     */
    private static int[] hullModel(List<ShipHulls.Part> parts) {
        Set<Long> solid = new HashSet<>();
        for (ShipHulls.Part p : parts) {
            if (colour(p) != 0) {
                solid.add(cell(p.x(), p.y(), p.z()));
            }
        }
        FigureBatches.Corners out = new FigureBatches.Corners();
        float in = 0.48F;
        for (ShipHulls.Part p : parts) {
            int rgb = colour(p);
            if (rgb == 0) {
                continue; // glass and the like
            }
            // The figure shader's right is the ship's -x (port and starboard would swap otherwise).
            float x = -p.x();
            int y = p.y();
            int z = p.z();
            if (!solid.contains(cell(p.x(), y + 1, z))) {
                float t = y + 2 * in;
                out.quad(rgb, 0, 0, 1.0F, x - 0.5F, t, z - 0.5F, x + 0.5F, t, z - 0.5F, x + 0.5F, t, z + 0.5F, x - 0.5F, t, z + 0.5F);
            }
            for (int side = -1; side <= 1; side += 2) {
                if (!solid.contains(cell(p.x() + side, y, z))) {
                    float s = x - side * in;
                    out.quad(rgb, 0, 0, 0.6F, s, y, z - 0.5F, s, y, z + 0.5F, s, y + 1, z + 0.5F, s, y + 1, z - 0.5F);
                }
                if (!solid.contains(cell(p.x(), y, z + side))) {
                    float s = z + side * in;
                    out.quad(rgb, 0, 0, 0.8F, x - 0.5F, y, s, x + 0.5F, y, s, x + 0.5F, y + 1, s, x - 0.5F, y + 1, s);
                }
            }
        }
        return out.model();
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

    static {
        FigureMotion.ground = DistantArmyRenderer::ground;
    }

    /**
     * The ground a figure stands on at ({@code x}, {@code z}): the top that blocks motion, under any leaves; NaN where
     * the client lacks the chunk (out on Distant Horizons' terrain).
     * shortcut: no ground beyond the game's own chunks, figures there keep their block's height; read DH heights if they float.
     */
    private static double ground(double x, double z, double near) {
        Minecraft mc = Minecraft.getInstance();
        int bx = net.minecraft.util.Mth.floor(x);
        int bz = net.minecraft.util.Mth.floor(z);
        if (mc.level == null || !mc.level.hasChunk(bx >> 4, bz >> 4)) {
            return Double.NaN;
        }
        // MOTION_BLOCKING: the no-leaves map is server-only, so step down through any canopy ourselves.
        int y = mc.level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, bx, bz);
        BlockPos.MutableBlockPos at = new BlockPos.MutableBlockPos(bx, y - 1, bz);
        for (int i = 0; i < 32 && y > mc.level.getMinBuildHeight(); i++, y--, at.setY(y - 1)) {
            net.minecraft.world.level.block.state.BlockState below = mc.level.getBlockState(at);
            if (below.blocksMotion() && !below.is(net.minecraft.tags.BlockTags.LEAVES)) {
                break;
            }
        }
        return y;
    }

    /**
     * The light a figure stands in, as the game lights a mob there: the sky and block light at its head height. Out
     * past the chunks the client has, it is open sky.
     */
    private static int lightAt(Minecraft mc, double x, double y, double z) {
        BlockPos head = BlockPos.containing(x, y + 1, z);
        if (mc.level == null || !mc.level.hasChunkAt(head)) {
            return LightTexture.FULL_SKY;
        }
        // Not under the ground: a block's middle on a slope can sit below it, and solid ground has no light (black figures).
        // MOTION_BLOCKING: the no-leaves map is server-only, empty on the client, so it never lifted anything.
        int surface = mc.level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, head.getX(), head.getZ());
        int packed = LevelRenderer.getLightColor(mc.level, head.getY() < surface ? head.atY(surface) : head);
        // At or above the surface is open sky: chunks at the edge of those loaded can have no sky light yet (sky 0).
        return mc.level.dimensionType().hasSkyLight() ? packed | LightTexture.FULL_SKY : packed;
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

    /** Takes a flat figure's rectangles: centred {@code right} blocks to its right, from {@code bottom} up {@code height}. */
    interface Rects {
        void rect(float right, float bottom, float width, float height, int rgb);
    }

    /**
     * A flat figure of {@code shape}, built of rectangles side by side so none overlaps another (a shield and a weapon in
     * front). Sizes in blocks, from the feet up. Coloured from {@code p}: its soldiers' gear ({@link FigureGear}), else
     * the army's colour.
     */
    static void flatFigure(Rects f, SoldierFigure.Shape shape, SoldierFigure.Palette p) {
        switch (shape.body()) {
            case RIDER -> {
                f.rect(0, 0, 1.2F, 0.7F, HOOVES);
                f.rect(0, 0.7F, 1.5F, 0.6F, p.horse()); // the horse, in its barding
                f.rect(0, 1.3F, 0.5F, 0.4F, p.legs());
                f.rect(0, 1.7F, 0.6F, 0.6F, p.chest());
                head(f, 2.3F, p.cap(), p.mark(), shape.leader());
                arms(f, shape, p, 0.9F);
            }
            case GENERAL -> {
                body(f, p.chest(), p.legs());
                f.rect(0, 1.45F, 0.45F, 0.25F, SKIN);
                f.rect(0, 1.7F, 0.55F, 0.3F, p.helm());
                f.rect(-0.05F, 2.0F, 0.08F, 1.3F, POLE); // the banner on the back, its pole above the helmet
                f.rect(0.33F, 2.4F, 0.5F, 0.85F, p.mark());
            }
            case BEARER -> {
                body(f, p.chest(), p.legs());
                head(f, 1.45F, p.cap(), p.mark(), false);
                f.rect(0.45F, 0.3F, 0.08F, 3.0F, POLE);
                f.rect(0.78F, 2.3F, 0.55F, 0.95F, p.mark());
            }
            case FOOT -> {
                body(f, p.chest(), p.legs());
                head(f, 1.45F, p.cap(), p.mark(), shape.leader());
                arms(f, shape, p, 0);
            }
        }
    }

    /**
     * A flat figure's shield, left, and weapon, right, {@code lift} up for a rider: beside the body, above the horse,
     * not over them, as a figure's rectangles never overlap.
     */
    private static void arms(Rects f, SoldierFigure.Shape shape, SoldierFigure.Palette p, float lift) {
        if (!shape.armed()) {
            return;
        }
        if (shape.shield()) {
            f.rect(-0.55F, lift + 0.55F, 0.45F, 0.8F, p.off());
        }
        switch (shape.weapon()) {
            case SWORD -> f.rect(0.42F, lift + 0.85F, 0.08F, 1.0F, p.main());
            case GREAT -> f.rect(0.42F, lift + 0.7F, 0.1F, 1.9F, p.main());
            case LONG -> f.rect(0.42F, lift + 0.45F, 0.06F, 2.8F, p.main());
            case BOW -> f.rect(0.42F, lift + 0.55F, 0.08F, 1.3F, p.main());
            case NONE -> {
            }
        }
    }

    private static void body(Rects f, int chest, int legs) {
        f.rect(0, 0, 0.5F, 0.75F, legs);
        f.rect(0, 0.75F, 0.6F, 0.7F, chest);
    }

    /** A face and a leather cap; a leader wears the faction banner instead, standing tall on the head. */
    private static void head(Rects f, float bottom, int cap, int mark, boolean leader) {
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
     * Writes a figure drawn the old way, a vertex at a time: the player's sprite and the ships. Soldiers go through
     * {@link FigureBatches}. A sprite is rectangles upright and square to the camera.
     */
    private static final class Figure implements Rects {
        private final BufferBuilder buffer;
        private final Matrix4f matrix;
        private final float rightX;
        private final float rightZ;
        private float x;
        private float y;
        private float z;
        private float scale;
        private int light;
        /** One colour for everything drawn, unshaded (a glow), or -1 for the figure's own colours. */
        int tint = -1;

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
        @Override
        public void rect(float right, float bottom, float width, float height, int rgb) {
            float shade = tint < 0 ? SIDE_SHADE : 1F;
            rgb = tint < 0 ? rgb : tint;
            float r = (rgb >> 16 & 255) / 255F * shade;
            float g = (rgb >> 8 & 255) / 255F * shade;
            float b = (rgb & 255) / 255F * shade;
            float l = (right - width / 2) * scale;
            float h = (right + width / 2) * scale;
            float lo = y + bottom * scale;
            float hi = y + (bottom + height) * scale;
            buffer.addVertex(matrix, x + rightX * l, lo, z + rightZ * l).setColor(r, g, b, 1F).setLight(light);
            buffer.addVertex(matrix, x + rightX * h, lo, z + rightZ * h).setColor(r, g, b, 1F).setLight(light);
            buffer.addVertex(matrix, x + rightX * h, hi, z + rightZ * h).setColor(r, g, b, 1F).setLight(light);
            buffer.addVertex(matrix, x + rightX * l, hi, z + rightZ * l).setColor(r, g, b, 1F).setLight(light);
        }
    }
}
