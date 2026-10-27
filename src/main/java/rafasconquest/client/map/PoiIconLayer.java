package rafasconquest.client.map;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import rafasconquest.net.FactionInfo;
import rafasconquest.poi.PoiType;

import java.util.List;

/**
 * Layer 3: cities and points of interest. A capital is its Empire's banner in a gold frame, a City-state's city its
 * banner in a silver frame, and a Farm a sheaf of wheat (shown once zoomed in far enough to tell them apart).
 */
public class PoiIconLayer implements MapLayer {
    private static final int GOLD = 0xFFD9B13B;
    private static final int SILVER = 0xFFC8CDD2;
    private static final int FRAME_BACK = 0xCC1E2126;
    /** Farms appear from this zoom (screen pixels per block) in. */
    private static final double FARM_MIN_SCALE = 0.12;
    private static final ItemStack WHEAT = new ItemStack(Items.WHEAT);
    private static final ItemStack WHITE_BANNER = new ItemStack(Items.WHITE_BANNER);

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        boolean farms = view.scale >= FARM_MIN_SCALE;
        for (FactionInfo f : map.factions) {
            if (farms) {
                for (BlockPos pos : f.structures().getOrDefault(PoiType.FARM, List.of())) {
                    icon(g, view, map, pos, WHEAT, 0.5F, 0);
                }
            }
            ItemStack banner = map.banner(f.id());
            for (BlockPos pos : f.structures().getOrDefault(PoiType.CITY, List.of())) {
                icon(g, view, map, pos, banner, 0.75F, SILVER);
            }
            for (BlockPos pos : f.structures().getOrDefault(PoiType.CAPITAL, List.of())) {
                icon(g, view, map, pos, banner, 1.0F, GOLD);
            }
        }
    }

    /** An item icon centred on a world position, optionally in a coloured frame. */
    private static void icon(GuiGraphics g, MapView view, MapContext map, BlockPos pos, ItemStack stack, float size, int frame) {
        if (!map.insideKnownWorld(pos.getX(), pos.getZ())) {
            return;
        }
        double x = view.screenX(pos.getX());
        double y = view.screenY(pos.getZ());
        if (!view.near(x, y, 16)) {
            return;
        }
        drawIcon(g, (float) x, (float) y, stack, size, frame);
    }

    static void drawIcon(GuiGraphics g, float x, float y, ItemStack stack, float size, int frame) {
        int half = Math.round(8 * size) + 1;
        if (frame != 0) {
            g.fill((int) x - half, (int) y - half, (int) x + half, (int) y + half, FRAME_BACK);
            g.renderOutline((int) x - half, (int) y - half, 2 * half, 2 * half, frame);
        }
        g.pose().pushPose();
        g.pose().translate(x, y, 0);
        g.pose().scale(size, size, 1);
        g.renderItem(stack, -8, -8);
        g.pose().popPose();
    }

    @Override
    public List<LegendEntry> legend() {
        return List.of(
                new LegendEntry((g, x, y) -> drawIcon(g, x + 5, y + 5, WHITE_BANNER, 0.5F, GOLD),
                        Component.translatable("gui.rafasconquest.map.legend.capital")),
                new LegendEntry((g, x, y) -> drawIcon(g, x + 5, y + 5, WHITE_BANNER, 0.5F, SILVER),
                        Component.translatable("gui.rafasconquest.map.legend.city_state")),
                new LegendEntry((g, x, y) -> drawIcon(g, x + 5, y + 5, WHEAT, 0.6F, 0),
                        Component.translatable("gui.rafasconquest.map.legend.farm")));
    }
}
