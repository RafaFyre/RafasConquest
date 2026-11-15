package rafasconquest.client.map;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import rafasconquest.faction.FactionType;
import rafasconquest.map.PoliticalStyle;
import rafasconquest.net.FactionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Layer 6, the top: names and labels. An Empire's name is written in capitals across the middle of its realm, turned
 * along the way the realm runs and spaced out to stretch along it, as on a strategy-game map; letters never grow past
 * {@link #MAX_EMPIRE_SCALE}. A City-state's name sits small under its city. Double-clicking a name opens that faction's
 * Faction Info.
 */
public class LabelLayer implements MapLayer {
    private static final int INK = 0xFF231C14;
    private static final int HOVER = 0xFF8E1B12;
    private static final int OUTLINE = 0xFFE9DFC6;
    private static final float MIN_SCALE = 0.6F;
    /** Largest an Empire's letters are drawn, however big its land: names mark territory, they shouldn't cover it. */
    private static final float MAX_EMPIRE_SCALE = 1.2F;
    private static final float CITY_STATE_SCALE = 0.75F;
    /** How much of a realm's length its name spans. */
    private static final double SPAN = 0.6;
    /** Steepest a name is turned; steeper realms get names at this angle. */
    private static final double MAX_ANGLE = Math.toRadians(60);
    /** Most extra space between letters, in letter widths. */
    private static final float MAX_SPACING = 2.5F;

    private final Consumer<UUID> openFaction;
    private final List<Hit> hits = new ArrayList<>();
    /** False while laying names out on the 3D map without drawing them (they are drawn on its ground). */
    private boolean ink = true;
    /** The faction whose name is under the cursor on the 3D map, or null. */
    @org.jetbrains.annotations.Nullable
    private UUID hovered;

    /** Where a drawn name is: centre, turn, and half its size, in screen pixels. */
    private record Hit(UUID faction, double x, double y, double angle, double halfWidth, double halfHeight) {
        boolean contains(double mx, double my) {
            double dx = mx - x;
            double dy = my - y;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            double along = dx * cos + dy * sin;
            double across = -dx * sin + dy * cos;
            return Math.abs(along) <= halfWidth && Math.abs(across) <= halfHeight;
        }
    }

    /** {@code openFaction} opens Faction Info on a faction. */
    public LabelLayer(Consumer<UUID> openFaction) {
        this.openFaction = openFaction;
    }

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        hits.clear();
        for (FactionInfo f : map.factions) {
            PoliticalStyle.LabelShape shape = map.label(f.id());
            if (shape == null) {
                continue;
            }
            if (f.type() == FactionType.EMPIRE) {
                drawEmpire(g, font, view, f, shape, mouseX, mouseY);
            } else {
                drawCityState(g, font, view, f, mouseX, mouseY);
            }
        }
    }

    /**
     * Where the names fall on the 3D map, for hovering and double-clicking them (they are drawn on the ground by
     * {@link #renderGround}): each laid out as on the flat map about where its middle falls on screen, at the map's
     * scale there. {@code project} gives a world (x, z)'s screen point and pixels per block, or null if behind the camera.
     */
    void render3d(GuiGraphics g, MapContext map, int mouseX, int mouseY, java.util.function.BiFunction<Double, Double, double[]> project) {
        Font font = Minecraft.getInstance().font;
        hits.clear();
        ink = false;
        for (FactionInfo f : map.factions) {
            PoliticalStyle.LabelShape shape = map.label(f.id());
            if (shape == null) {
                continue;
            }
            boolean empire = f.type() == FactionType.EMPIRE;
            double wx = empire ? shape.centerX() : f.origin().getX();
            double wz = empire ? shape.centerZ() : f.origin().getZ();
            double[] at = project.apply(wx, wz);
            if (at == null || at[2] <= 0) {
                continue;
            }
            // A flat map that puts this name's middle where the camera sees it.
            MapView local = new MapView();
            local.setViewport(0, 0, g.guiWidth(), g.guiHeight());
            local.scale = at[2];
            local.centerX = wx - (at[0] - local.width() / 2.0) / local.scale;
            local.centerZ = wz - (at[1] - local.height() / 2.0) / local.scale;
            if (empire) {
                drawEmpire(g, font, local, f, shape, mouseX, mouseY);
            } else {
                drawCityState(g, font, local, f, mouseX, mouseY);
            }
        }
        ink = true;
        hovered = null;
        for (int i = hits.size() - 1; i >= 0 && hovered == null; i--) {
            if (hits.get(i).contains(mouseX, mouseY)) {
                hovered = hits.get(i).faction();
            }
        }
    }

    /**
     * The names painted on the 3D map's ground, as Crusader Kings paints its realms': each letter lies flat on the land
     * at the ground under it, laid out as on the flat map at {@code mapScale} (screen pixels per block at the view's
     * middle), so the names keep their flat-map size there and grow and shrink with the land around it.
     */
    void renderGround(com.mojang.blaze3d.vertex.PoseStack pose, net.minecraft.world.phys.Vec3 cam, MapContext map, double mapScale,
                      net.minecraft.client.renderer.MultiBufferSource buffers) {
        Font font = Minecraft.getInstance().font;
        for (FactionInfo f : map.factions) {
            PoliticalStyle.LabelShape shape = map.label(f.id());
            if (shape == null) {
                continue;
            }
            int color = f.id().equals(hovered) ? HOVER : INK;
            if (f.type() != FactionType.EMPIRE) {
                // Small, under the city, as on the flat map.
                ground(pose, cam, map, font, buffers, f.name(), f.origin().getX(),
                        f.origin().getZ() + (10 + font.lineHeight * CITY_STATE_SCALE / 2) / mapScale, 0, CITY_STATE_SCALE / mapScale, color);
                continue;
            }
            String name = f.name().toUpperCase(Locale.ROOT);
            double target = SPAN * shape.length() * mapScale;
            int plain = font.width(name);
            float scale = Mth.clamp((float) (target / plain), MIN_SCALE, MAX_EMPIRE_SCALE);
            float spacing = 0;
            if (name.length() > 1 && plain * scale < target) {
                float average = (float) plain / name.length();
                spacing = Math.min(MAX_SPACING * average, (float) ((target / scale - plain) / (name.length() - 1)));
            }
            double angle = shape.elongated() ? Mth.clamp(shape.angle(), -MAX_ANGLE, MAX_ANGLE) : 0;
            double k = scale / mapScale; // blocks per letter pixel
            float cursor = -(plain + spacing * (name.length() - 1)) / 2;
            for (int i = 0; i < name.length(); i++) {
                String letter = name.substring(i, i + 1);
                float w = font.width(letter);
                double along = (cursor + w / 2) * k;
                ground(pose, cam, map, font, buffers, letter, shape.centerX() + Math.cos(angle) * along,
                        shape.centerZ() + Math.sin(angle) * along, angle, k, color);
                cursor += w + spacing;
            }
        }
    }

    /** One piece of text lying on the 3D map's ground, centred on world (x, z), turned {@code angle} from east toward south. */
    private static void ground(com.mojang.blaze3d.vertex.PoseStack pose, net.minecraft.world.phys.Vec3 cam, MapContext map, Font font,
                               net.minecraft.client.renderer.MultiBufferSource buffers, String text, double x, double z, double angle,
                               double blocksPerPixel, int color) {
        double y = StrategyRelief.groundAt(map.base, x, z) + 3 * StrategyRelief.STRETCH;
        pose.pushPose();
        pose.translate(x - cam.x, y - cam.y, z - cam.z);
        pose.mulPose(Axis.YP.rotation((float) -angle));
        pose.mulPose(Axis.XP.rotationDegrees(90)); // the text's down runs south, so it reads from the camera looking north
        pose.scale((float) blocksPerPixel, (float) blocksPerPixel, (float) blocksPerPixel);
        org.joml.Matrix4f m = pose.last().pose();
        net.minecraft.util.FormattedCharSequence seq = Component.literal(text).getVisualOrderText();
        float x0 = -font.width(text) / 2F;
        float y0 = -font.lineHeight / 2F;
        // Font's outline draws on the depth buffer; see-through draws over the land, which isn't in it.
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    font.drawInBatch(seq, x0 + dx, y0 + dy, OUTLINE, false, m, buffers, Font.DisplayMode.SEE_THROUGH, 0, 0xF000F0);
                }
            }
        }
        font.drawInBatch(seq, x0, y0, color, false, m, buffers, Font.DisplayMode.SEE_THROUGH, 0, 0xF000F0);
        pose.popPose();
    }

    private void drawEmpire(GuiGraphics g, Font font, MapView view, FactionInfo f, PoliticalStyle.LabelShape shape, int mouseX, int mouseY) {
        String name = f.name().toUpperCase(Locale.ROOT);
        double x = view.screenX(shape.centerX());
        double y = view.screenY(shape.centerZ());
        double target = SPAN * shape.length() * view.scale; // screen pixels the name should span
        int plain = font.width(name);
        float scale = Mth.clamp((float) (target / plain), MIN_SCALE, MAX_EMPIRE_SCALE);
        // Letters stop growing at the cap; past it, spacing stretches the name along the realm instead.
        float spacing = 0;
        if (name.length() > 1 && plain * scale < target) {
            float average = (float) plain / name.length();
            spacing = Math.min(MAX_SPACING * average, (float) ((target / scale - plain) / (name.length() - 1)));
        }
        double angle = shape.elongated() ? Mth.clamp(shape.angle(), -MAX_ANGLE, MAX_ANGLE) : 0;
        float width = plain + spacing * (name.length() - 1);
        double halfWidth = width * scale / 2;
        if (!view.near(x, y, halfWidth)) {
            return;
        }
        Hit hit = new Hit(f.id(), x, y, angle, halfWidth, font.lineHeight * scale / 2);
        hits.add(hit);
        int color = hit.contains(mouseX, mouseY) ? HOVER : INK;
        g.pose().pushPose();
        g.pose().translate(x, y, 0);
        g.pose().mulPose(Axis.ZP.rotation((float) angle));
        g.pose().scale(scale, scale, 1);
        float cursor = -width / 2;
        for (int i = 0; i < name.length(); i++) {
            String letter = name.substring(i, i + 1);
            draw(g, font, letter, cursor, -font.lineHeight / 2F, color);
            cursor += font.width(letter) + spacing;
        }
        g.pose().popPose();
    }

    private void drawCityState(GuiGraphics g, Font font, MapView view, FactionInfo f, int mouseX, int mouseY) {
        double x = view.screenX(f.origin().getX());
        double y = view.screenY(f.origin().getZ()) + 10; // under the city's icon
        float width = font.width(f.name());
        if (!view.near(x, y, width)) {
            return;
        }
        Hit hit = new Hit(f.id(), x, y + font.lineHeight * CITY_STATE_SCALE / 2, 0, width * CITY_STATE_SCALE / 2,
                font.lineHeight * CITY_STATE_SCALE / 2);
        hits.add(hit);
        g.pose().pushPose();
        g.pose().translate(x, y, 0);
        g.pose().scale(CITY_STATE_SCALE, CITY_STATE_SCALE, 1);
        draw(g, font, f.name(), -width / 2, 0, hit.contains(mouseX, mouseY) ? HOVER : INK);
        g.pose().popPose();
    }

    /** Dark ink with a pale outline, so names read over any colour. */
    private void draw(GuiGraphics g, Font font, String text, float x, float y, int color) {
        if (!ink) {
            return;
        }
        font.drawInBatch8xOutline(Component.literal(text).getVisualOrderText(), x, y, color, OUTLINE,
                g.pose().last().pose(), g.bufferSource(), 0xF000F0);
        g.flush();
    }

    @Override
    public boolean mouseClicked(MapView view, MapContext map, double mouseX, double mouseY, int button, boolean doubleClick) {
        // Topmost (last drawn) name first.
        for (int i = hits.size() - 1; i >= 0; i--) {
            Hit hit = hits.get(i);
            if (hit.contains(mouseX, mouseY)) {
                if (doubleClick && button == 0) {
                    openFaction.accept(hit.faction());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<LegendEntry> legend() {
        return List.of(new LegendEntry((g, x, y) -> {
            Font font = Minecraft.getInstance().font;
            draw(g, font, "A", x + 2, y + 1, INK);
        }, Component.translatable("gui.rafasconquest.map.legend.name")));
    }
}
