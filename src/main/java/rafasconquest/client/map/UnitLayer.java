package rafasconquest.client.map;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

/**
 * Layer 5: armies and other things that move. For now that is players: you as a white arrow pointing the way you face,
 * other players near enough to be loaded as blue markers. Recruits' armies go here when the map shows them.
 */
public class UnitLayer implements MapLayer {
    private static final ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/map/map_icons.png");
    /** Vanilla map icons: 8 × 8 each, 16 to a row, in a 128 × 128 texture. */
    private static final int PLAYER_ICON = 0;
    private static final int MARKER_ICON = 3;

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        for (Player other : mc.level.players()) {
            if (other != mc.player) {
                drawIcon(g, view.screenX(other.getX()), view.screenY(other.getZ()), MARKER_ICON, 0, 1.25F);
            }
        }
        drawIcon(g, view.screenX(mc.player.getX()), view.screenY(mc.player.getZ()), PLAYER_ICON, mc.player.getYRot(), 1.5F);
    }

    /** A map icon centred at (x, y), turned to face {@code yaw} (Minecraft yaw: 0 is south). */
    static void drawIcon(GuiGraphics g, double x, double y, int icon, float yaw, float size) {
        g.pose().pushPose();
        g.pose().translate(x, y, 0);
        g.pose().mulPose(Axis.ZP.rotationDegrees(yaw + 180));
        g.pose().scale(size, size, 1);
        g.blit(ICONS, -4, -4, (icon % 16) * 8, (icon / 16) * 8, 8, 8, 128, 128);
        g.pose().popPose();
    }

    @Override
    public List<LegendEntry> legend() {
        return List.of(new LegendEntry((g, x, y) -> drawIcon(g, x + 5, y + 5, PLAYER_ICON, 180, 1.1F),
                Component.translatable("gui.rafasconquest.map.legend.you")));
    }
}
