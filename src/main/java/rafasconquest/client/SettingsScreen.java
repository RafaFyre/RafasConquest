package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.PlayerStats;

/** The player's settings, from the stats window's wheel: each change goes to the server, which keeps it. */
public class SettingsScreen extends Screen {
    private static final int WIDTH = 260;
    private static final int GOLD = 0xFFE0C060;

    private final StatsScreen parent;

    public SettingsScreen(StatsScreen parent) {
        super(Component.literal("Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        CycleButton<Boolean> teleport = CycleButton.booleanBuilder(Component.literal("Y"), Component.literal("N"))
                .withInitialValue(parent.teleportAtMuster)
                .create(width / 2 - WIDTH / 2, height / 2 - 10, WIDTH, 20, Component.literal("Teleport to position at battle muster"),
                        (button, value) -> {
                            parent.teleportAtMuster = value;
                            ConquestNetwork.setSetting(PlayerStats.TELEPORT_AT_MUSTER, value);
                        });
        teleport.setTooltip(Tooltip.create(Component.literal("At the start of each day of a battle, be brought to your place in the line.")));
        addRenderableWidget(teleport);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g, mouseX, mouseY, partialTick);
        g.drawCenteredString(font, title, width / 2, height / 2 - 40, GOLD);
        for (net.minecraft.client.gui.components.Renderable widget : renderables) { // the widgets only: 1.21 Screen.render would paint the background over what is drawn above
            widget.render(g, mouseX, mouseY, partialTick);
        }
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
