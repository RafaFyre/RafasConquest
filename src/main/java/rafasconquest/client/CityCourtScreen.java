package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rafasconquest.client.map.ClientMapCache;
import rafasconquest.map.BaseMap;

/**
 * A city's court, opened from the city manager: the holder over the city's factions (the peasantry, the merchant
 * guilds, the great houses). No votes: the holder decides. One faction's page shows beside them when picked.
 */
public class CityCourtScreen extends Screen {
    private static final int GOLD = 0xFFE0B040;
    private static final int TEXT = 0xFFE4E9EE;
    private static final int MUTED = 0xFF000000 | CityHud.MUTED_RGB;
    private final Screen parent;
    private final int province;
    /** The faction whose page shows, a {@link CityHud#FACTIONS} index, or -1. */
    private int faction;

    public CityCourtScreen(Screen parent, String city, int province, int faction) {
        super(Component.literal("The Court of " + city));
        this.parent = parent;
        this.province = province;
        this.faction = faction;
    }

    @Override
    protected void init() {
        int w = 150;
        int x0 = width / 2 - (CityHud.FACTIONS.length * (w + 6) - 6) / 2;
        for (int i = 0; i < CityHud.FACTIONS.length; i++) {
            int f = i;
            addRenderableWidget(Button.builder(Component.literal(CityHud.FACTIONS[i]), b -> faction = faction == f ? -1 : f)
                    .bounds(x0 + i * (w + 6), 70, w, 20).build());
        }
        addRenderableWidget(Button.builder(Component.literal("Back"), b -> onClose()).bounds(width / 2 - 40, height - 30, 80, 20).build());
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        int cx = width / 2;
        g.drawCenteredString(font, title, cx, 20, GOLD);
        g.drawCenteredString(font, holder(), cx, 36, TEXT);
        g.drawCenteredString(font, "No votes here: the factions bring their requests, and your word is final.", cx, 50, MUTED);
        if (faction < 0) {
            g.drawCenteredString(font, "Pick a faction to see its page.", cx, 110, MUTED);
            return;
        }
        int y = 110;
        g.drawCenteredString(font, CityHud.FACTIONS[faction], cx, y, GOLD);
        g.drawCenteredString(font, CityHud.ABOUT[faction], cx, y + 16, TEXT);
        g.drawCenteredString(font, "Relations: " + CityHud.RELATION[faction] + "   Fervour: " + CityHud.FERVOUR[faction] + " of 3", cx, y + 32, TEXT);
        g.drawCenteredString(font, CityHud.SAMPLE, cx, y + 44, MUTED);
        g.drawCenteredString(font, "Requests: none yet", cx, y + 64, MUTED);
    }

    /** Who holds the city, from the realm the map carries. */
    private String holder() {
        BaseMap base = ClientMapCache.map();
        int h = base == null ? -1 : base.realm().holderOf(province);
        return h < 0 ? "No holder" : "Held by " + base.realm().people().get(h).name();
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false; // the city manager beneath must go on receiving its chunks
    }
}
