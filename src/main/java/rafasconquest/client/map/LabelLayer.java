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
    private static void draw(GuiGraphics g, Font font, String text, float x, float y, int color) {
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
