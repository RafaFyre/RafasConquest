package rafasconquest.client.map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import rafasconquest.core.Countryside;
import rafasconquest.core.Dojos;
import rafasconquest.core.SeatCities;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.net.FactionInfo;
import rafasconquest.poi.PoiType;

import java.util.ArrayList;
import java.util.List;

/**
 * Layer 8, on top of the rest: in creative mode, a right-click on any place drawn on the map (a seat city, a castle,
 * village, hamlet, watchtower or ruin, a dojo, a war's battle site, a capital, City-state or farm) opens a small menu
 * there, headed by the place's name, offering to teleport to it. The server checks the player is in creative and puts
 * them on the ground there. A click anywhere else closes it.
 */
public class PlaceMenuLayer implements MapLayer {
    private static final int BG = 0xF0201D18;
    private static final int EDGE = 0xFF8C6A2F;
    private static final int TEXT = 0xFFF4EEDC;
    private static final int HOVER = 0xFF3A3228;
    private static final int ROW = 12;
    /** How near (in screen pixels) a right-click must be to a place to pick it. */
    private static final double REACH = 8;

    /** A place the menu can be opened on: its name and where it is. */
    private record Place(String name, int x, int z) {
    }

    @Nullable
    private Place open;
    private int menuX;
    private int menuY;

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        if (open == null) {
            return;
        }
        Font font = Minecraft.getInstance().font;
        String teleport = "Teleport here";
        int w = Math.max(font.width(open.name()), font.width(teleport)) + 12;
        int h = ROW * 2 + 6;
        g.pose().pushPose();
        g.pose().translate(0, 0, 400); // over every mark and tooltip
        g.fill(menuX, menuY, menuX + w, menuY + h, BG);
        g.renderOutline(menuX, menuY, w, h, EDGE);
        g.drawString(font, open.name(), menuX + 6, menuY + 4, 0xFFE0C060, false);
        int rowY = menuY + 4 + ROW;
        if (inRow(mouseX, mouseY, w, rowY)) {
            g.fill(menuX + 1, rowY - 2, menuX + w - 1, rowY + ROW - 2, HOVER);
        }
        g.drawString(font, teleport, menuX + 6, rowY, TEXT, false);
        g.pose().popPose();
    }

    private boolean inRow(double mouseX, double mouseY, int w, int rowY) {
        return mouseX >= menuX && mouseX < menuX + w && mouseY >= rowY - 2 && mouseY < rowY + ROW - 2;
    }

    @Override
    public boolean mouseClicked(MapView view, MapContext map, double mouseX, double mouseY, int button, boolean doubleClick) {
        Minecraft mc = Minecraft.getInstance();
        if (open != null) {
            Font font = mc.font;
            int w = Math.max(font.width(open.name()), font.width("Teleport here")) + 12;
            if (button == 0 && inRow(mouseX, mouseY, w, menuY + 4 + ROW)) {
                ConquestNetwork.teleport(open.x(), open.z());
                mc.setScreen(null);
            }
            open = null;
            return true;
        }
        if (button != 1 || mc.player == null || !mc.player.isCreative()) {
            return false;
        }
        Place place = nearest(view, map, mouseX, mouseY);
        if (place == null) {
            return false;
        }
        open = place;
        menuX = (int) mouseX + 4;
        menuY = (int) mouseY + 4;
        return true;
    }

    /** The place drawn nearest the mouse, within {@link #REACH}, or null. */
    @Nullable
    private static Place nearest(MapView view, MapContext map, double mouseX, double mouseY) {
        List<Place> places = new ArrayList<>();
        for (SeatCities.City c : map.base.cities()) {
            places.add(new Place(map.base.names().get(c.province()), c.x(), c.z()));
        }
        for (Countryside.Settlement s : map.base.settlements()) {
            String kind = switch (s.kind()) {
                case HAMLET -> "Hamlet";
                case VILLAGE -> "Village";
                case WATCHTOWER -> "Watchtower";
                case CASTLE -> "Castle";
                case RUIN -> "Ruin";
            };
            places.add(new Place(kind + " (" + map.base.names().get(s.province()) + ")", s.x(), s.z()));
        }
        for (Dojos.Dojo d : map.base.dojos()) {
            if (!d.school().hidden()) {
                places.add(new Place("Dojo of the " + d.school().title() + " school", d.x(), d.z()));
            }
        }
        for (int[] war : rafasconquest.client.ClientPolitics.wars()) {
            places.add(new Place("Battle site: " + map.base.nations().get(war[0]).name() + " against "
                    + map.base.nations().get(war[1]).name(), war[7], war[8]));
        }
        for (FactionInfo f : map.factions) {
            for (PoiType type : new PoiType[]{PoiType.CAPITAL, PoiType.CITY, PoiType.FARM}) {
                for (BlockPos pos : f.structures().getOrDefault(type, List.of())) {
                    String kind = switch (type) {
                        case CAPITAL -> "Capital of ";
                        case CITY -> "City of ";
                        default -> "Farm of ";
                    };
                    places.add(new Place(kind + f.name(), pos.getX(), pos.getZ()));
                }
            }
        }
        Place best = null;
        double bestDistance = REACH;
        for (Place p : places) {
            double d = Math.hypot(view.screenX(p.x()) - mouseX, view.screenY(p.z()) - mouseY);
            if (d < bestDistance) {
                bestDistance = d;
                best = p;
            }
        }
        return best;
    }
}
