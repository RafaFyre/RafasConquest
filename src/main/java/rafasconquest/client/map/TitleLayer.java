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
 * over a seat names every title it holds ("Empire of Shukai, Kingdom of Shukai, ..."). Each seat city is drawn as its
 * square, outlined in the colour of its highest title.
 */
public class TitleLayer implements MapLayer {
    private static final int[] SEAT_SIZE = {2, 3, 4, 5};
    private static final int[] SEAT_COLOR = {0xFF6B5A44, 0xFF8C6A2F, 0xFFC9A13A, 0xFFB0302A};
    private static final int OUTLINE = 0xFF1E1A16;
    /** The countryside shows from this zoom (screen pixels per block) in. */
    private static final double COUNTRY_MIN_SCALE = 0.05;

    /** Each province's highest seat (a tier's ordinal), or -1; worked out once per map. */
    private BaseMap seatsOf;
    private int[] seatTier;
    private rafasconquest.core.SeatCities.City[] cityAt;

    @Override
    public void render(GuiGraphics g, MapView view, MapContext map, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BaseMapLayer.drawAligned(g, view, map, MapContext.TITLES_TEXTURE);
        RenderSystem.disableBlend();
        // The seat cities' squares, faintly filled, outlined in the colour of their highest title.
        for (rafasconquest.core.SeatCities.City c : map.base.cities()) {
            int half = c.size() / 2;
            int x0 = (int) view.screenX(c.x() - half);
            int z0 = (int) view.screenY(c.z() - half);
            int x1 = (int) view.screenX(c.x() + half);
            int z1 = (int) view.screenY(c.z() + half);
            if (x1 < view.left || x0 > view.right || z1 < view.top || z0 > view.bottom || x1 - x0 < 2) {
                continue;
            }
            int color = SEAT_COLOR[c.tier().ordinal()];
            g.fill(x0, z0, x1, z1, 0x40000000 | color & 0xFFFFFF);
            g.renderOutline(x0, z0, x1 - x0, z1 - z0, color);
        }
        int[] tiers = tiers(map.base);
        String hovered = null;
        int hoveredProvince = -1;
        // The countryside, once zoomed in a little: hamlets, villages, watchtowers and castles.
        if (view.scale >= COUNTRY_MIN_SCALE) {
            for (rafasconquest.core.Countryside.Settlement s : map.base.settlements()) {
                double x = view.screenX(s.x());
                double y = view.screenY(s.z());
                if (!view.near(x, y, 6)) {
                    continue;
                }
                int r = countryMark(g, s.kind(), (int) x, (int) y);
                if (Math.abs(mouseX - x) <= r + 1 && Math.abs(mouseY - y) <= r + 1) {
                    hovered = countryName(s) + " (" + map.base.names().get(s.province()) + ")";
                    hoveredProvince = s.kind() == rafasconquest.core.Countryside.Kind.CASTLE ? s.province() : -1;
                }
            }
        }
        // The known schools' dojos (the hidden ones are not on any map): a dark diamond with a pale heart.
        for (rafasconquest.core.Dojos.Dojo dojo : map.base.dojos()) {
            if (dojo.school().hidden()) {
                continue;
            }
            int x = (int) view.screenX(dojo.x());
            int y = (int) view.screenY(dojo.z());
            if (!view.near(x, y, 8)) {
                continue;
            }
            for (int d = 0; d <= 4; d++) {
                g.fill(x - (4 - d), y - d, x + (4 - d) + 1, y - d + 1, OUTLINE);
                g.fill(x - (4 - d), y + d, x + (4 - d) + 1, y + d + 1, OUTLINE);
            }
            g.fill(x - 1, y - 1, x + 2, y + 2, 0xFFE8D8B0);
            if (Math.abs(mouseX - x) <= 5 && Math.abs(mouseY - y) <= 5) {
                hovered = "Dojo of the " + dojo.school().title() + " school";
                hoveredProvince = -1;
            }
        }
        // The provinces fought over: a red cross on each, naming the war.
        for (int[] war : rafasconquest.client.ClientPolitics.wars()) {
            if (war[2] < 0 || war[2] >= map.base.centres().size()) {
                continue;
            }
            ProvinceMap.Centre c = map.base.centres().get(war[2]);
            int x = (int) view.screenX(c.x());
            int y = (int) view.screenY(c.z());
            if (!view.near(x, y, 10)) {
                continue;
            }
            for (int d = -5; d <= 5; d++) {
                g.fill(x + d - 1, y + d - 1, x + d + 2, y + d + 2, OUTLINE);
                g.fill(x + d - 1, y - d - 1, x + d + 2, y - d + 2, OUTLINE);
            }
            for (int d = -4; d <= 4; d++) {
                g.fill(x + d, y + d, x + d + 1, y + d + 1, 0xFFD02020);
                g.fill(x + d, y - d, x + d + 1, y - d + 1, 0xFFD02020);
            }
            if (Math.abs(mouseX - x) <= 6 && Math.abs(mouseY - y) <= 6) {
                hovered = "War over " + map.base.names().get(war[2]) + ": " + map.base.nations().get(war[0]).name() + " against "
                        + map.base.nations().get(war[1]).name() + " (" + (war[3] > 0 ? "+" : "") + war[3] + ")";
                hoveredProvince = -1;
            }
        }
        for (int p = 0; p < tiers.length; p++) {
            int tier = tiers[p];
            if (tier < 0) {
                continue;
            }
            // On its city, if it has one (a city may stand off its province's centre, by the shore), else the centre.
            ProvinceMap.Centre c = map.base.centres().get(p);
            rafasconquest.core.SeatCities.City city = cityAt[p];
            double x = view.screenX(city != null ? city.x() : c.x());
            double y = view.screenY(city != null ? city.z() : c.z());
            if (!view.near(x, y, 8) || tier == 0 && view.scale < 0.03) {
                continue; // county seats only once zoomed in a little
            }
            int s = SEAT_SIZE[tier];
            g.fill((int) x - s - 1, (int) y - s - 1, (int) x + s + 1, (int) y + s + 1, OUTLINE);
            g.fill((int) x - s, (int) y - s, (int) x + s, (int) y + s, SEAT_COLOR[tier]);
            if (Math.abs(mouseX - x) <= s + 2 && Math.abs(mouseY - y) <= s + 2) {
                hovered = names(map.base, p, tier);
                hoveredProvince = p;
            }
        }
        if (hovered != null) {
            List<Component> lines = new ArrayList<>(List.of(Component.literal(hovered)));
            int holder = hoveredProvince < 0 ? -1 : map.base.realm().holderOf(hoveredProvince);
            String player = hoveredProvince < 0 ? null : rafasconquest.client.ClientPeerage.holder(hoveredProvince);
            if (player != null) {
                lines.add(Component.literal("Held by " + player).withStyle(net.minecraft.ChatFormatting.GOLD));
            } else if (holder >= 0) {
                rafasconquest.people.Person p = map.base.realm().people().get(holder);
                int nation = capitalOf(map.base, p);
                lines.add(Component.literal("Held by " + rafasconquest.client.CharacterScreen.styled(map.base, p)
                        + (nation >= 0 ? "" : " (double-click)")).withStyle(net.minecraft.ChatFormatting.GRAY));
                if (nation >= 0) {
                    lines.add(Component.literal("Capital of " + map.base.nations().get(nation).name() + " (double-click)")
                            .withStyle(net.minecraft.ChatFormatting.GRAY));
                }
            }
            g.renderComponentTooltip(Minecraft.getInstance().font, lines, mouseX, mouseY);
        }
    }

    /** Draws a settlement's mark centred on (x, y); returns its half size. */
    /** A double-click on a hold (a seat city or a castle) opens its holder's page. */
    @Override
    public boolean mouseClicked(MapView view, MapContext map, double mouseX, double mouseY, int button, boolean doubleClick) {
        if (!doubleClick || button != 0) {
            return false;
        }
        java.util.List<rafasconquest.people.Person> people = map.base.realm().people();
        int nearest = -1;
        double best = 7;
        for (int i = 0; i < people.size(); i++) {
            rafasconquest.people.Person.Hold hold = people.get(i).hold();
            double d = Math.hypot(view.screenX(hold.x()) - mouseX, view.screenY(hold.z()) - mouseY);
            if (d < best) {
                best = d;
                nearest = i;
            }
        }
        if (nearest < 0) {
            return false;
        }
        Minecraft mc = Minecraft.getInstance();
        int nation = capitalOf(map.base, people.get(nearest));
        int organization = nation >= 0 ? rafasconquest.client.OrganizationScreen.of(map.base, nation) : -1;
        // A sword sanctum's city opens the sanctum's page; another capital its nation's; any other hold its holder's.
        mc.setScreen(organization >= 0 ? new rafasconquest.client.OrganizationScreen(mc.screen, map.base, organization)
                : nation >= 0 ? new rafasconquest.client.NationScreen(mc.screen, map.base, nation)
                : new rafasconquest.client.CharacterScreen(mc.screen, map.base, nearest));
        return true;
    }

    /** The nation whose capital a character holds, or -1. */
    private static int capitalOf(BaseMap base, rafasconquest.people.Person p) {
        return p.nation() >= 0 && p.hold().kind() == rafasconquest.people.Person.Kind.CAPITAL
                && base.nations().get(p.nation()).seat() == p.hold().province() ? p.nation() : -1;
    }

    private static int countryMark(GuiGraphics g, rafasconquest.core.Countryside.Kind kind, int x, int y) {
        switch (kind) {
            case HAMLET -> {
                g.fill(x - 1, y - 1, x + 2, y + 2, 0xFF7A5A3A);
                return 1;
            }
            case VILLAGE -> {
                g.fill(x - 3, y - 1, x - 1, y + 1, 0xFF7A5A3A);
                g.fill(x + 1, y - 1, x + 3, y + 1, 0xFF7A5A3A);
                g.fill(x - 1, y - 3, x + 1, y - 1, 0xFF7A5A3A);
                return 3;
            }
            case RUIN -> {
                g.fill(x - 1, y - 1, x + 2, y + 2, 0xFF8A8A80);
                return 1;
            }
            case WATCHTOWER -> {
                g.fill(x - 1, y - 3, x + 1, y + 2, 0xFF5C5C5C);
                g.fill(x - 2, y - 4, x + 2, y - 3, 0xFF3A3A3A);
                return 3;
            }
            default -> {
                g.fill(x - 3, y - 3, x + 3, y + 3, 0xFF6E6E6E);
                g.renderOutline(x - 3, y - 3, 6, 6, OUTLINE);
                g.fill(x - 1, y - 1, x + 1, y + 1, 0xFF3A3A3A);
                return 3;
            }
        }
    }

    private static String countryName(rafasconquest.core.Countryside.Settlement s) {
        return switch (s.kind()) {
            case HAMLET -> "Hamlet";
            case VILLAGE -> "Village of " + s.hamlets() + " hamlets";
            case WATCHTOWER -> "Watchtower";
            case CASTLE -> "Castle";
            case RUIN -> "Ruin";
        };
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
            cityAt = new rafasconquest.core.SeatCities.City[base.centres().size()];
            for (rafasconquest.core.SeatCities.City city : base.cities()) {
                cityAt[city.province()] = city;
            }
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
        String[] country = {"castle", "watchtower", "village", "hamlet", "ruin"};
        rafasconquest.core.Countryside.Kind[] kinds = {rafasconquest.core.Countryside.Kind.CASTLE,
                rafasconquest.core.Countryside.Kind.WATCHTOWER, rafasconquest.core.Countryside.Kind.VILLAGE,
                rafasconquest.core.Countryside.Kind.HAMLET, rafasconquest.core.Countryside.Kind.RUIN};
        for (int i = 0; i < kinds.length; i++) {
            rafasconquest.core.Countryside.Kind kind = kinds[i];
            entries.add(new LegendEntry((g, x, y) -> countryMark(g, kind, x + 5, y + 5),
                    Component.translatable("gui.rafasconquest.map.legend." + country[i])));
        }
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
