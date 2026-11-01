package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import rafasconquest.peerage.PlayerStats;

/**
 * The player's stats (opened from the inventory's button): their name and title, the nation they serve, rank and
 * renown, land, and how they fight; and a settings wheel in its corner ({@link SettingsScreen}).
 */
public class StatsScreen extends Screen {
    private static final int WIDTH = 260;
    private static final int ROW = 14;
    private static final int PANEL = 0xE0201810;
    private static final int BORDER = 0xFF8A6A30;
    private static final int GOLD = 0xFFE0C060;
    private static final int LABEL = 0xFFB8A888;
    private static final int TEXT = 0xFFE8E0D0;

    @Nullable
    private final Screen parent;
    private final PlayerStats.View view;
    /** The settings as last chosen here (the server keeps them). */
    boolean teleportAtMuster;

    public StatsScreen(@Nullable Screen parent, PlayerStats.View view) {
        super(Component.literal("Your Stats"));
        this.parent = parent;
        this.view = view;
        this.teleportAtMuster = view.teleportAtMuster();
    }

    private int height() {
        return 30 + view.rows().size() * ROW + 10;
    }

    private int left() {
        return width / 2 - WIDTH / 2;
    }

    private int top() {
        return height / 2 - height() / 2;
    }

    @Override
    protected void init() {
        Button settings = Button.builder(Component.literal("⚙"), b -> minecraft.setScreen(new SettingsScreen(this)))
                .bounds(left() + WIDTH - 24, top() + 4, 20, 20).build();
        settings.setTooltip(Tooltip.create(Component.literal("Settings")));
        addRenderableWidget(settings);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        int x = left();
        int y = top();
        g.fill(x - 1, y - 1, x + WIDTH + 1, y + height() + 1, BORDER);
        g.fill(x, y, x + WIDTH, y + height(), PANEL);
        g.drawCenteredString(font, title, x + WIDTH / 2, y + 10, GOLD);
        int row = y + 30;
        for (String[] r : view.rows()) {
            g.drawString(font, r[0], x + 10, row, LABEL, false);
            g.drawString(font, font.plainSubstrByWidth(r[1], WIDTH - 120), x + 110, row, TEXT, false);
            row += ROW;
        }
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
