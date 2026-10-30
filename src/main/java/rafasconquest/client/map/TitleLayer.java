package rafasconquest.client.map;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import rafasconquest.core.ProvinceMap;
import rafasconquest.core.TitleMap;
import rafasconquest.map.BaseMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Layer 3: the land's titles. Borders between counties, duchies, kingdoms and empires, each stronger than the last
 * ({@link rafasconquest.map.TitleStyle}), and a mark on each seat sized by the highest title seated there; hovering
 * over a seat names every title it holds ("Empire of Shukai, Kingdom of Shukai, ...").
 */
public class TitleLayer implements MapLayer {
    private static final int[] SEAT_SIZE = {2, 3, 4, 5};
    private static final int[] SEAT_COLOR = {0xFF6B5A44, 0xFF8C6A2F, 0xFFC9A13A, 0xFFB0302A};
    private static final int OUTLINE = 0xFF1E1A16;

    /** Each province's highest seat (a tier's ordinal), or -1; worked out once per map. */
    private BaseMap seatsOf;
    private int[] seatTier;

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BaseMapLayer.drawAligned(g, view, map, MapContext.TITLES_TEXTURE);
        RenderSystem.disableBlend();
        int[] tiers = tiers(map.base);
        String hovered = null;
        for (int p = 0; p < tiers.length; p++) {
            int tier = tiers[p];
            if (tier < 0) {
                continue;
            }
            ProvinceMap.Centre c = map.base.centres().get(p);
            double x = view.screenX(c.x());
            double y = view.screenY(c.z());
            if (!view.near(x, y, 8) || tier == 0 && view.scale < 0.03) {
                continue; // county seats only once zoomed in a little
            }
            int s = SEAT_SIZE[tier];
            g.fill((int) x - s - 1, (int) y - s - 1, (int) x + s + 1, (int) y + s + 1, OUTLINE);
            g.fill((int) x - s, (int) y - s, (int) x + s, (int) y + s, SEAT_COLOR[tier]);
            if (Math.abs(mouseX - x) <= s + 2 && Math.abs(mouseY - y) <= s + 2) {
                hovered = names(map.base, p, tier);
            }
        }
        if (hovered != null) {
            g.renderTooltip(Minecraft.getInstance().font, Component.literal(hovered), mouseX, mouseY);
        }
    }

    /** Every title seated in a province, highest first. */
    private static String names(BaseMap base, int province, int tier) {
        List<String> names = new ArrayList<>();
        for (int t = tier; t >= 0; t--) {
            names.add(base.titleName(TitleMap.Tier.values()[t], province));
        }
        return String.join(", ", names);
    }

    private int[] tiers(BaseMap base) {
        if (seatsOf != base) {
            seatsOf = base;
            TitleMap t = base.titles();
            seatTier = new int[base.centres().size()];
            java.util.Arrays.fill(seatTier, -1);
            int[][] seats = {t.countySeat(), t.duchySeat(), t.kingdomSeat(), t.empireSeat()};
            for (int tier = 0; tier < seats.length; tier++) {
                for (int s : seats[tier]) {
                    seatTier[s] = Math.max(seatTier[s], tier);
                }
            }
        }
        return seatTier;
    }

    @Override
    public List<LegendEntry> legend() {
        List<LegendEntry> entries = new ArrayList<>();
        String[] keys = {"county", "duchy", "kingdom", "empire"};
        for (int tier = SEAT_SIZE.length - 1; tier >= 0; tier--) {
            int s = SEAT_SIZE[tier];
            int color = SEAT_COLOR[tier];
            entries.add(new LegendEntry((g, x, y) -> {
                g.fill(x + 5 - s - 1, y + 5 - s - 1, x + 5 + s + 1, y + 5 + s + 1, OUTLINE);
                g.fill(x + 5 - s, y + 5 - s, x + 5 + s, y + 5 + s, color);
            }, Component.translatable("gui.rafasconquest.map.legend." + keys[tier])));
        }
        return entries;
    }
}
