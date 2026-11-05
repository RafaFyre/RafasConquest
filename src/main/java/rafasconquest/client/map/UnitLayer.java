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
    /** Vanilla map icons, one 8 × 8 texture each since 1.20.5. */
    private static final ResourceLocation PLAYER_ICON = ResourceLocation.withDefaultNamespace("textures/map/decorations/player.png");
    private static final ResourceLocation MARKER_ICON = ResourceLocation.withDefaultNamespace("textures/map/decorations/blue_marker.png");

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
    static void drawIcon(GuiGraphics g, double x, double y, ResourceLocation icon, float yaw, float size) {
        g.pose().pushPose();
        g.pose().translate(x, y, 0);
        g.pose().mulPose(Axis.ZP.rotationDegrees(yaw + 180));
        g.pose().scale(size, size, 1);
        g.blit(icon, -4, -4, 0, 0, 8, 8, 8, 8);
        g.pose().popPose();
    }

    @Override
    public List<LegendEntry> legend() {
        return List.of(new LegendEntry((g, x, y) -> drawIcon(g, x + 5, y + 5, PLAYER_ICON, 180, 1.1F),
                Component.translatable("gui.rafasconquest.map.legend.you")));
    }
}
