package rafasconquest.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import rafasconquest.core.Age;
import rafasconquest.core.WorldFeatures;
import rafasconquest.tech.TechTree;
import rafasconquest.tech.TechTree.Tech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The tech tree, opened from the grand strategy screen: one grey column per age the world has, headed by its name, each
 * tech a dark square where its file places it, and a curved arrow from every tech to each one that needs it. Wider than
 * the screen, it scrolls sideways (wheel or drag). For looking only: a nation's ruler picks what it researches.
 */
public class TechTreeScreen extends Screen {
    private static final int FRAME = 0xFF0E0F11;
    private static final int PANEL = 0xFFD8D8D8;
    private static final int LINE = 0xFF7A7A7A;
    private static final int INK = 0xFF2A2A2A;
    private static final int TECH = 0xFF1C1C1C;
    private static final int ARROW = 0xFF8C8C8C;
    private static final int HOVER = 0xFFE0C060;
    private static final int MARGIN = 6;
    private static final int TOP = 20;
    private static final int HEADER = 26;
    private static final int PAD = 12;
    private static final int SIZE = 10;
    private static final int MIN_COLUMN = 150;

    private final Screen parent;
    private List<Age> ages = List.of();
    private List<Tech> techs = List.of();
    private final Map<String, Tech> byId = new HashMap<>();
    private double scroll;

    public TechTreeScreen(Screen parent) {
        super(Component.literal("Technology"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // shortcut: the world's ages and the tree are read on this machine, right in single player; a client on a
        // dedicated server sees the default ages and its own files until the server sends both (with research, stage 3).
        ages = Arrays.stream(Age.values()).filter(WorldFeatures.ages()::has).toList();
        techs = TechTree.load().stream().filter(t -> ages.contains(t.age())).toList();
        byId.clear();
        techs.forEach(t -> byId.put(t.id(), t));
        scroll = Math.min(scroll, maxScroll());
        addRenderableWidget(Button.builder(Component.literal("✕"), b -> onClose())
                .bounds(width - MARGIN - 14, 4, 14, 14).build());
    }

    private int columnWidth() {
        return Math.max(MIN_COLUMN, (width - 2 * MARGIN) / Math.max(1, ages.size()));
    }

    private double maxScroll() {
        return Math.max(0, columnWidth() * ages.size() - (width - 2 * MARGIN));
    }

    private int columnLeft(Age age) {
        return MARGIN + ages.indexOf(age) * columnWidth() - (int) scroll;
    }

    private int bodyTop() {
        return TOP + HEADER;
    }

    private int bottom() {
        return height - MARGIN;
    }

    private int squareX(Tech t) {
        return columnLeft(t.age()) + PAD + t.x() * (columnWidth() - 2 * PAD - SIZE) / 100;
    }

    private int squareY(Tech t) {
        return bodyTop() + PAD + t.y() * (bottom() - bodyTop() - 2 * PAD - SIZE) / 100;
    }

    @Nullable
    private Tech techAt(double mouseX, double mouseY) {
        if (mouseX < MARGIN || mouseX >= width - MARGIN) {
            return null;
        }
        for (Tech t : techs) {
            int x = squareX(t);
            int y = squareY(t);
            if (mouseX >= x - 1 && mouseX < x + SIZE + 1 && mouseY >= y - 1 && mouseY < y + SIZE + 1) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, width, height, FRAME);
        g.drawString(font, title, MARGIN + 2, 6, 0xF4EEDC, false);
        g.enableScissor(MARGIN, TOP, width - MARGIN, bottom());
        int w = columnWidth();
        for (Age age : ages) {
            int left = columnLeft(age);
            g.fill(left, TOP, left + w, bottom(), PANEL);
            g.renderOutline(left, TOP, w + 1, bottom() - TOP, LINE); // +1: neighbours share their edge line
            g.hLine(left, left + w, bodyTop(), LINE);
            g.drawString(font, age.title, left + (w - font.width(age.title)) / 2, TOP + (HEADER - 8) / 2, INK, false);
        }
        g.drawManaged(() -> { // one batch for every dot of every arrow
            for (Tech t : techs) {
                for (String need : t.needs()) {
                    Tech from = byId.get(need);
                    if (from != null) {
                        arrow(g, from, t);
                    }
                }
            }
        });
        Tech hovered = techAt(mouseX, mouseY);
        for (Tech t : techs) {
            int x = squareX(t);
            int y = squareY(t);
            g.fill(x, y, x + SIZE, y + SIZE, TECH);
            if (t == hovered) {
                g.renderOutline(x - 1, y - 1, SIZE + 2, SIZE + 2, HOVER);
            }
        }
        g.disableScissor();
        for (net.minecraft.client.gui.components.Renderable widget : renderables) { // the widgets only: 1.21 Screen.render would paint the background over what is drawn above
            widget.render(g, mouseX, mouseY, partialTick);
        }
        if (hovered != null) {
            g.renderComponentTooltip(font, tooltip(hovered), mouseX, mouseY);
        }
    }

    private List<Component> tooltip(Tech t) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal(t.name()).withStyle(ChatFormatting.GOLD));
        lines.add(Component.literal(t.age().title).withStyle(ChatFormatting.GRAY));
        List<String> needs = t.needs().stream().map(id -> byId.containsKey(id) ? byId.get(id).name() : id).toList();
        if (!needs.isEmpty()) {
            lines.add(Component.literal("Needs: " + String.join(", ", needs)));
        }
        return lines;
    }

    /**
     * A curved arrow from one tech to another: side to side when the second stands clearly to the right, else from the
     * bottom of one to the top of the other (or top to bottom when it is above).
     */
    private void arrow(GuiGraphics g, Tech from, Tech to) {
        int fx = squareX(from);
        int fy = squareY(from);
        int tx = squareX(to);
        int ty = squareY(to);
        double sx, sy, ex, ey, c1x, c1y, c2x, c2y;
        int head; // 0 right, 1 down, 2 up
        if (tx >= fx + SIZE + 4) {
            sx = fx + SIZE;
            sy = fy + SIZE / 2.0;
            ex = tx - 1;
            ey = ty + SIZE / 2.0;
            c1x = c2x = (sx + ex) / 2;
            c1y = sy;
            c2y = ey;
            head = 0;
        } else {
            boolean down = ty >= fy;
            sx = fx + SIZE / 2.0;
            sy = down ? fy + SIZE : fy - 1;
            ex = tx + SIZE / 2.0;
            ey = down ? ty - 1 : ty + SIZE;
            c1x = sx;
            c2x = ex;
            c1y = c2y = (sy + ey) / 2;
            head = down ? 1 : 2;
        }
        int steps = Math.max(8, (int) (Math.hypot(ex - sx, ey - sy) * 1.5));
        for (int i = 0; i <= steps; i++) {
            double u = (double) i / steps;
            double a = (1 - u) * (1 - u) * (1 - u);
            double b = 3 * (1 - u) * (1 - u) * u;
            double c = 3 * (1 - u) * u * u;
            double d = u * u * u;
            int x = (int) Math.floor(a * sx + b * c1x + c * c2x + d * ex);
            int y = (int) Math.floor(a * sy + b * c1y + c * c2y + d * ey);
            g.fill(x, y, x + 1, y + 1, ARROW);
        }
        int hx = (int) Math.floor(ex);
        int hy = (int) Math.floor(ey);
        for (int k = 0; k < 3; k++) { // a small triangle, its point on the target's edge
            switch (head) {
                case 0 -> g.fill(hx - k, hy - k, hx - k + 1, hy + k + 1, ARROW);
                case 1 -> g.fill(hx - k, hy - k, hx + k + 1, hy - k + 1, ARROW);
                default -> g.fill(hx - k, hy + k, hx + k + 1, hy + k + 1, ARROW);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        scroll = Math.max(0, Math.min(maxScroll(), scroll - (scrollX + scrollY) * 24));
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (button == 0) {
            scroll = Math.max(0, Math.min(maxScroll(), scroll - dx));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
