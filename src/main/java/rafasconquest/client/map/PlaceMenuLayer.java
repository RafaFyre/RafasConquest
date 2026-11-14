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
 * Layer 8, on top of the rest: a right-click on the map opens a small drop-down menu there. Over any province it
 * offers "See title owners" ({@link rafasconquest.client.TitleOwnersScreen}); in creative mode, on a place drawn on the
 * map (a seat city, a castle, village, hamlet, watchtower or ruin, a dojo, a war's battle site, a capital, City-state
 * or farm), it is headed by the place's name and also offers to teleport there (the server checks the player is in
 * creative and puts them on the ground there). A click anywhere else closes it.
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

    /** One option of the menu, and what a click on it does. */
    private record Row(String label, Runnable run) {
    }

    private String heading = "";
    /** The open menu's options; empty while it is closed. */
    private List<Row> rows = List.of();
    private int menuX;
    private int menuY;

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        if (rows.isEmpty()) {
            return;
        }
        Font font = Minecraft.getInstance().font;
        int w = width(font);
        int h = ROW * (rows.size() + 1) + 6;
        g.pose().pushPose();
        g.pose().translate(0, 0, 400); // over every mark and tooltip
        g.fill(menuX, menuY, menuX + w, menuY + h, BG);
        g.renderOutline(menuX, menuY, w, h, EDGE);
        g.drawString(font, heading, menuX + 6, menuY + 4, 0xFFE0C060, false);
        for (int i = 0; i < rows.size(); i++) {
            int rowY = rowY(i);
            if (inRow(mouseX, mouseY, w, rowY)) {
                g.fill(menuX + 1, rowY - 2, menuX + w - 1, rowY + ROW - 2, HOVER);
            }
            g.drawString(font, rows.get(i).label(), menuX + 6, rowY, TEXT, false);
        }
        g.pose().popPose();
    }

    private int width(Font font) {
        int w = font.width(heading);
        for (Row row : rows) {
            w = Math.max(w, font.width(row.label()));
        }
        return w + 12;
    }

    private int rowY(int i) {
        return menuY + 4 + ROW * (i + 1);
    }

    private boolean inRow(double mouseX, double mouseY, int w, int rowY) {
        return mouseX >= menuX && mouseX < menuX + w && mouseY >= rowY - 2 && mouseY < rowY + ROW - 2;
    }

    @Override
    public boolean mouseClicked(MapView view, MapContext map, double mouseX, double mouseY, int button, boolean doubleClick) {
        Minecraft mc = Minecraft.getInstance();
        if (!rows.isEmpty()) {
            List<Row> open = rows;
            int w = width(mc.font);
            rows = List.of();
            for (int i = 0; button == 0 && i < open.size(); i++) {
                if (inRow(mouseX, mouseY, w, rowY(i))) {
                    open.get(i).run().run();
                }
            }
            return true;
        }
        if (button != 1 || mc.player == null) {
            return false;
        }
        int province = map.base.provinceIndexAt((int) Math.floor(view.worldX(mouseX)), (int) Math.floor(view.worldZ(mouseY)));
        Place place = mc.player.isCreative() ? nearest(view, map, mouseX, mouseY) : null;
        List<Row> found = new ArrayList<>();
        if (province >= 0) {
            found.add(new Row("See title owners",
                    () -> mc.setScreen(new rafasconquest.client.TitleOwnersScreen(mc.screen, map.base, province))));
        }
        if (place != null) {
            found.add(new Row("Teleport here", () -> {
                ConquestNetwork.teleport(place.x(), place.z());
                mc.setScreen(null);
            }));
        }
        if (found.isEmpty()) {
            return false;
        }
        heading = place != null ? place.name() : map.base.names().get(province);
        rows = found;
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
        List<int[]> clashes = rafasconquest.client.ClientPolitics.clashes();
        for (int i = 0; i < clashes.size(); i++) {
            places.add(new Place(rafasconquest.client.ClientPolitics.clashNames().get(i), clashes.get(i)[1], clashes.get(i)[2]));
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
