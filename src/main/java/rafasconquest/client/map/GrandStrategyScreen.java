package rafasconquest.client.map;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import rafasconquest.client.CityManagerScreen;
import rafasconquest.client.DistantHorizonsLink;
import rafasconquest.client.NationScreen;
import rafasconquest.client.Portraits;
import rafasconquest.client.TechTreeScreen;
import rafasconquest.core.SeatCities;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.GrandStrategy;
import rafasconquest.people.Realm;
import rafasconquest.people.Succession;

import java.util.ArrayList;
import java.util.List;

/**
 * The grand strategy screen, opened from the Grand Strategy table: the world map filling the screen, centred on the
 * player's hold, under a HUD. Top right, the resources (money, prestige, population); down the right, a button for
 * each realm menu (vassals to technology); bottom left, the hold, house and character buttons over the player's realm
 * colour (their nation's page), their figure (its head following the cursor), culture, faith and skills; bottom right,
 * the date. The cities they own are ringed in gold: a click shows a city's summary; a double-click, or Manage city in its
 * right-click menu, opens its {@link CityManagerScreen city manager}.
 */
public class GrandStrategyScreen extends FactionMapScreen {
    private static final int PANEL_BACK = 0xD0101214;
    private static final int HUD = 0xFF4A4A4A;
    private static final int HUD_DARK = 0xFF2E2E2E;
    private static final int HUD_EDGE = 0xFF1A1A1A;
    private static final int HUD_HOVER = 0xFF6A6A6A;
    private static final int TEXT = 0xFFF4EEDC;
    private static final int GOLD = 0xFFE0C060;
    private static final int EDGE = 0xFF8C6A2F;
    private static final int HOVER = 0xFF3A3228;
    private static final int ROW = 12;
    /** How near (in screen pixels) a click must be to an owned city to pick it. */
    private static final double REACH = 8;
    private static final int DOUBLE_CLICK_MS = 350;
    private static final String MANAGE = "Manage city";
    private static final String NOT_BUILT = "Not built yet";
    private static final String[] SEASONS = {"Spring", "Summer", "Autumn", "Winter"};
    /** The HUD's spots in {@link #spots()} order: 3 resources, 9 realm menus, 3 left buttons, then these. */
    private static final int NATION = 15;
    private static final int CULTURE = 16;
    private static final int FAITH = 17;
    private static final int SKILLS = 18;
    private static final int COLOURED = 19;
    /** The realms drawn in their colours (on by default); static, so it stays as set the next time the map opens. */
    private static boolean colouredMap = true;

    /** Something on the HUD: its box, what it is (hover), and what a click does (null: nothing yet). */
    private record Spot(int x, int y, int w, int h, List<Component> tip, @Nullable Runnable click) {
        boolean over(double mx, double my) {
            return mx >= x && mx < x + w && my >= y && my < y + h;
        }
    }

    private final GrandStrategy.View fief;
    @Nullable
    private SeatCities.City summary;
    @Nullable
    private SeatCities.City menu;
    private int menuX;
    private int menuY;
    @Nullable
    private SeatCities.City lastClicked;
    private long lastClick;
    private long lastHoldClick;
    /** Looks down on Distant Horizons' terrain while the map is shown in 3D; null on the flat map. */
    @Nullable
    private Marker camera;
    private boolean hidGui;
    /** The flat map's titles layer, for its double-click on a hold, worked from the 3D map. */
    private final TitleLayer titles = new TitleLayer();
    /** The flat map's nation names, drawn on the 3D map where each realm's middle falls. */
    private final LabelLayer labels = new LabelLayer(this::openFaction);
    private long lastNameClick;
    /** Degrees the 3D camera looks down: Total War's campaign tilt. */
    private static final double PITCH = 55;

    public GrandStrategyScreen(GrandStrategy.View fief) {
        super(null, Component.translatable("gui.rafasconquest.grand_strategy"));
        this.fief = fief;
    }

    @Override
    protected boolean hud() {
        return true;
    }

    @Override
    protected void centre(BaseMap base) {
        if (fief.seat() >= 0 && fief.seat() < base.centres().size()) {
            view.centerX = base.centres().get(fief.seat()).x();
            view.centerZ = base.centres().get(fief.seat()).z();
        } else {
            super.centre(base);
        }
    }

    /** The cities the player owns: the one seating the county they hold. */
    private List<SeatCities.City> owned() {
        BaseMap base = ClientMapCache.map();
        return base == null || fief.seat() < 0 ? List.of() : base.cities().stream().filter(c -> c.province() == fief.seat()).toList();
    }

    private static String name(SeatCities.City city) {
        BaseMap base = ClientMapCache.map();
        return base == null ? "City" : base.names().get(city.province());
    }

    @Nullable
    private SeatCities.City ownedAt(double mouseX, double mouseY) {
        for (SeatCities.City c : owned()) {
            double[] s = at(c);
            if (s != null && Math.hypot(s[0] - mouseX, s[1] - mouseY) < REACH) {
                return c;
            }
        }
        return null;
    }

    /** "Spring 2nd, 12 AD": four seasons to a year of {@link Succession#YEAR_DAYS} days, the first year 1 AD. */
    public static String date(long day) {
        int season = Succession.YEAR_DAYS / SEASONS.length;
        int inYear = (int) (day % Succession.YEAR_DAYS);
        int d = inYear % season + 1;
        String th = d % 100 / 10 == 1 ? "th" : switch (d % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
        return SEASONS[inYear / season] + " " + d + th + ", " + (day / Succession.YEAR_DAYS + 1) + " AD";
    }

    private static List<Component> tip(String name, @Nullable String detail) {
        return detail == null ? List.of(Component.literal(name))
                : List.of(Component.literal(name), Component.literal(detail).withColor(0xA8A49C));
    }

    /** The player's nation's people, or -1 if they serve none. */
    private int population() {
        BaseMap base = ClientMapCache.map();
        return base == null || fief.nation() < 0 ? -1 : base.realm().demographics(fief.nation()).population();
    }

    /** The HUD's spots, laid out for the screen as it is now. */
    private List<Spot> spots() {
        BaseMap base = ClientMapCache.map();
        List<Spot> spots = new ArrayList<>();
        // Resources, top right: money, prestige, population.
        String[] resources = {"Money", "Prestige", "Population"};
        for (int i = 0; i < 3; i++) {
            spots.add(new Spot(width - 62 * (3 - i), 0, 62, 18, tip(resources[i], i < 2 ? NOT_BUILT : population() < 0 ? "You serve no nation" : null), null));
        }
        // The realm menus, down the right.
        String[] menus = {"Vassals", "Court", "Council", "Intrigue", "Factions", "Activities", "Army", "Law", "Technology"};
        for (int i = 0; i < menus.length; i++) {
            boolean tech = i == menus.length - 1;
            spots.add(new Spot(width - 21, 24 + i * 22, 20, 20, tip(menus[i], tech ? null : NOT_BUILT),
                    tech ? () -> minecraft.setScreen(new TechTreeScreen(this)) : null));
        }
        // Bottom left: hold, house, character.
        boolean held = !owned().isEmpty();
        spots.add(new Spot(1, height - 92, 16, 16, tip("Your hold", held ? "Click: look at it. Double-click: city view" : "You hold no land"),
                held ? this::holdClicked : null));
        spots.add(new Spot(1, height - 74, 16, 16, tip("House and bloodline", NOT_BUILT), null));
        spots.add(new Spot(1, height - 56, 16, 16, tip("Your character", null), ConquestNetwork::requestStats));
        // The realm colour (the nation's page), culture, faith, skills.
        Realm realm = base == null ? Realm.EMPTY : base.realm();
        int n = fief.nation();
        String nation = base == null || n < 0 ? null : base.nations().get(n).name();
        spots.add(new Spot(3, height - 31, 28, 28, tip(nation == null ? "No nation" : nation, nation == null ? "You serve no nation" : null),
                nation == null ? null : () -> minecraft.setScreen(new NationScreen(this, base, n))));
        String culture = n >= 0 && n < realm.nationCulture().length ? realm.cultures().get(realm.nationCulture()[n]).name() : null;
        String faith = n >= 0 && n < realm.nationFaith().length ? realm.faiths().get(realm.nationFaith()[n]).name() : null;
        spots.add(new Spot(76, height - 28, 25, 25, tip(culture == null ? "Culture" : "Culture: " + culture, NOT_BUILT), null));
        spots.add(new Spot(104, height - 28, 25, 25, tip(faith == null ? "Faith" : "Faith: " + faith, NOT_BUILT), null));
        spots.add(new Spot(92, height - 51, 21, 21, tip("Skills", NOT_BUILT), null));
        spots.add(new Spot(139, height - 25, 20, 20, tip("Coloured map", colouredMap ? "On: the realms in their colours" : "Off: the land as it is"),
                () -> colouredMap = !colouredMap));
        return spots;
    }

    /** Once: the map looks at the hold. Twice: the hold's city view. */
    private void holdClicked() {
        long now = Util.getMillis();
        BaseMap base = ClientMapCache.map();
        if (now - lastHoldClick < DOUBLE_CLICK_MS) {
            manage(owned().get(0));
        } else if (base != null) {
            centre(base);
        }
        lastHoldClick = now;
    }

    @Override
    protected boolean territory() {
        return colouredMap;
    }

    @Override
    protected boolean world3d() {
        return camera != null;
    }

    /**
     * Zoomed in near enough, and inside the ground Distant Horizons draws about the player, the map gives way to the
     * world itself: a camera high over the map's middle, looking north and down, as a Total War campaign map. Zoomed out
     * past that, or with no Distant Horizons, it is the flat map again.
     */
    private void update3d() {
        LocalPlayer p = minecraft == null ? null : minecraft.player;
        int reach = DistantHorizonsLink.reach();
        double span = view.width() / view.scale;
        // 3D up to four times Distant Horizons' reach across, so the camera climbs high; the middle must stay on its terrain.
        boolean want = map != null && p != null && minecraft.level.dimension() == Level.OVERWORLD && span <= 4.0 * reach
                && Math.hypot(view.centerX - p.getX(), view.centerZ - p.getZ()) <= reach;
        if (want && (camera == null || camera.level() != minecraft.level)) {
            if (camera == null) {
                hidGui = minecraft.options.hideGui;
            }
            camera = new Marker(EntityType.MARKER, minecraft.level);
            minecraft.options.hideGui = true;
            DistantHorizonsLink.lodOnly(true);
        } else if (!want && camera != null) {
            leave3d();
        }
        if (camera == null) {
            return;
        }
        // Far enough back that the map's own scale holds at the middle of the screen.
        double d = view.height() / view.scale / 2 / Math.tan(Math.toRadians(fov()) / 2);
        double pitch = Math.toRadians(PITCH);
        camera.setPos(view.centerX, ground() + d * Math.sin(pitch), view.centerZ + d * Math.cos(pitch));
        camera.setYRot(180); // north
        camera.setXRot((float) PITCH);
        camera.setOldPosAndRot(); // moved every frame, so no blur between ticks
        if (minecraft.getCameraEntity() != camera) {
            minecraft.setCameraEntity(camera);
        }
        StrategyRelief.show(view.scale, labels, map, colouredMap);
    }

    private void leave3d() {
        if (camera != null && minecraft != null) {
            camera = null;
            minecraft.setCameraEntity(minecraft.player);
            minecraft.options.hideGui = hidGui;
            DistantHorizonsLink.lodOnly(false);
            StrategyRelief.hide();
        }
    }

    private double fov() {
        return minecraft.options.fov().get();
    }

    /** shortcut: the camera sits over sea level, not the ground under it; fine thousands of blocks up, not when zoomed right in. */
    private double ground() {
        return minecraft.level.getSeaLevel();
    }

    /** A city on screen (GUI pixels): on the flat map, or on the world through the camera; null if behind it. */
    @Nullable
    private double[] at(SeatCities.City c) {
        if (camera == null) {
            return new double[]{view.screenX(c.x()), view.screenY(c.z())};
        }
        return project(c.x() + 0.5, c.z() + 0.5);
    }

    /** The ground at world (x, z) on screen through the 3D camera (GUI pixels), or null if behind it. */
    @Nullable
    private double[] project(double x, double z) {
        double pitch = Math.toRadians(PITCH);
        BaseMap base = ClientMapCache.map();
        double vx = x - camera.getX();
        double vy = (base == null ? ground() : StrategyRelief.groundAt(base, x, z)) - camera.getY();
        double vz = z - camera.getZ();
        // The camera looks north and down: forward (0, -sin, -cos), right east, up (0, cos, -sin).
        double ahead = -vy * Math.sin(pitch) - vz * Math.cos(pitch);
        if (ahead <= 1) {
            return null;
        }
        double up = vy * Math.cos(pitch) - vz * Math.sin(pitch);
        double focal = height / 2.0 / Math.tan(Math.toRadians(fov()) / 2);
        return new double[]{width / 2.0 + vx / ahead * focal, height / 2.0 - up / ahead * focal};
    }

    @Override
    public void removed() {
        leave3d();
        super.removed();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        update3d();
        super.render(g, mouseX, mouseY, partialTick);
        g.enableScissor(view.left, view.top, view.right, view.bottom);
        if (camera != null) {
            renderSeats3d(g);
            if (map != null) {
                labels.render3d(g, map, mouseX, mouseY, (x, z) -> {
                    double[] at = project(x, z);
                    double[] east = project(x + 100, z); // pixels per block there, along the unforeshortened east-west
                    return at == null || east == null ? null : new double[]{at[0], at[1], (east[0] - at[0]) / 100};
                });
            }
        }
        for (SeatCities.City c : owned()) {
            double[] s = at(c);
            if (s != null) {
                g.renderOutline((int) s[0] - 7, (int) s[1] - 7, 14, 14, GOLD);
            }
        }
        g.disableScissor();
        SeatCities.City hovered = menu == null && !overHud(mouseX, mouseY) ? seatAt(mouseX, mouseY) : null;
        List<Spot> spots = spots();
        renderHud(g, spots, mouseX, mouseY);
        g.pose().pushPose();
        g.pose().translate(0, 0, 400); // over every mark and tooltip
        if (summary != null) {
            renderSummary(g, summary);
        }
        if (menu != null) {
            renderMenu(g, mouseX, mouseY);
        } else if (hovered != null) {
            g.renderComponentTooltip(font, seatTip(hovered), mouseX, mouseY);
        } else {
            for (Spot s : spots) {
                if (s.over(mouseX, mouseY)) {
                    g.renderComponentTooltip(font, s.tip(), mouseX, mouseY);
                }
            }
        }
        g.pose().popPose();
    }

    /** Every seat city on the 3D map: a square sized and coloured by its highest title, as on the flat map. */
    private void renderSeats3d(GuiGraphics g) {
        BaseMap base = ClientMapCache.map();
        if (base == null) {
            return;
        }
        for (SeatCities.City c : base.cities()) {
            double[] at = at(c);
            if (at == null || !view.near(at[0], at[1], 8)) {
                continue;
            }
            int s = TitleLayer.SEAT_SIZE[c.tier().ordinal()];
            int x = (int) at[0];
            int y = (int) at[1];
            g.fill(x - s - 1, y - s - 1, x + s + 1, y + s + 1, TitleLayer.OUTLINE);
            g.fill(x - s, y - s, x + s, y + s, TitleLayer.SEAT_COLOR[c.tier().ordinal()]);
        }
    }

    /** The seat city under the cursor on the 3D map (the flat map's layers have their own), or null. */
    @Nullable
    private SeatCities.City seatAt(double mouseX, double mouseY) {
        BaseMap base = ClientMapCache.map();
        if (camera == null || base == null) {
            return null;
        }
        SeatCities.City best = null;
        double near = REACH;
        for (SeatCities.City c : base.cities()) {
            double[] at = at(c);
            double d = at == null ? Double.MAX_VALUE : Math.hypot(at[0] - mouseX, at[1] - mouseY);
            if (d < near) {
                near = d;
                best = c;
            }
        }
        return best;
    }

    /** A seat's tooltip on the 3D map: its titles and holder, as on the flat map, then what a click does on it. */
    private List<Component> seatTip(SeatCities.City c) {
        BaseMap base = ClientMapCache.map();
        List<Component> lines = new ArrayList<>(TitleLayer.tip(base, TitleLayer.names(base, c.province(), c.tier().ordinal()), c.province()));
        if (owned().contains(c)) {
            lines.add(Component.literal("Click: summary. Double-click or right-click: " + MANAGE.toLowerCase()).withColor(0xE0C060));
        }
        return lines;
    }

    private void renderHud(GuiGraphics g, List<Spot> spots, int mouseX, int mouseY) {
        BaseMap base = ClientMapCache.map();
        // Resources.
        int people = population();
        Item[] icons = {Items.GOLD_NUGGET, Items.GOLDEN_HELMET, Items.PLAYER_HEAD};
        String[] values = {"-", "-", people < 0 ? "-" : String.format("%,d", people)}; // money and prestige: no system yet
        for (int i = 0; i < 3; i++) {
            Spot s = spots.get(i);
            g.fill(s.x(), s.y(), s.x() + s.w(), s.y() + s.h(), HUD);
            g.fill(s.x(), s.y(), s.x() + 1, s.y() + s.h(), HUD_EDGE);
            g.renderItem(new ItemStack(icons[i]), s.x() + 3, s.y() + 1);
            g.drawString(font, values[i], s.x() + s.w() - 4 - font.width(values[i]), s.y() + 5, TEXT, false);
        }
        g.fill(spots.get(0).x(), 18, width, 19, HUD_EDGE);
        // Realm menus and the left column: item-icon buttons.
        Item[] buttonIcons = {Items.SHIELD, Items.GOLDEN_HELMET, Items.LECTERN, Items.ENDER_EYE, Items.WHITE_BANNER, Items.BELL,
                Items.IRON_SWORD, Items.WRITABLE_BOOK, Items.ANVIL, Items.STONE_BRICKS, Items.OAK_SAPLING, Items.NAME_TAG};
        for (int i = 0; i < buttonIcons.length; i++) {
            Spot s = spots.get(3 + i);
            g.fill(s.x(), s.y(), s.x() + s.w(), s.y() + s.h(), HUD_EDGE);
            g.fill(s.x() + 1, s.y() + 1, s.x() + s.w() - 1, s.y() + s.h() - 1, s.over(mouseX, mouseY) ? HUD_HOVER : HUD);
            g.pose().pushPose();
            float k = (s.w() - 4) / 16F;
            g.pose().translate(s.x() + 2, s.y() + 2, 0);
            g.pose().scale(k, k, 1);
            g.renderItem(new ItemStack(buttonIcons[i]), 0, 0);
            g.pose().popPose();
        }
        // The bottom-left panel: realm colour, the player, culture, faith, skills.
        g.fill(0, height - 35, 135, height, HUD_EDGE);
        g.fill(0, height - 34, 134, height, HUD_DARK);
        Spot colour = spots.get(NATION);
        int realmColour = map != null && base != null && fief.nation() >= 0 ? 0xFF000000 | map.color(base.nations().get(fief.nation()).id()) : 0xFF6A6A6A;
        g.fill(colour.x() - 1, colour.y() - 1, colour.x() + colour.w() + 1, colour.y() + colour.h() + 1, HUD_EDGE);
        g.fill(colour.x(), colour.y(), colour.x() + colour.w(), colour.y() + colour.h(), realmColour);
        renderPlayer(g, 55, height + 8, 26, mouseX, mouseY);
        Portraits.diamond(g, 88, height - 16, 12, spots.get(CULTURE).over(mouseX, mouseY) ? 0xFF8CB8FF : 0xFF5C9CF0, HUD_EDGE);
        Portraits.diamond(g, 116, height - 16, 12, spots.get(FAITH).over(mouseX, mouseY) ? 0xFFFFE680 : 0xFFF5D040, HUD_EDGE);
        Portraits.circle(g, 102, height - 41, 10, HUD_EDGE);
        Portraits.circle(g, 102, height - 41, 9, spots.get(SKILLS).over(mouseX, mouseY) ? 0xFFE0E0E0 : 0xFFFFFFFF);
        // The coloured map toggle, beside the panel: a menu button, edged in gold while on.
        Spot toggle = spots.get(COLOURED);
        g.fill(toggle.x(), toggle.y(), toggle.x() + toggle.w(), toggle.y() + toggle.h(), colouredMap ? GOLD : HUD_EDGE);
        g.fill(toggle.x() + 1, toggle.y() + 1, toggle.x() + toggle.w() - 1, toggle.y() + toggle.h() - 1,
                toggle.over(mouseX, mouseY) ? HUD_HOVER : HUD);
        g.pose().pushPose();
        g.pose().translate(toggle.x() + 2, toggle.y() + 2, 0);
        g.pose().scale((toggle.w() - 4) / 16F, (toggle.w() - 4) / 16F, 1);
        g.renderItem(new ItemStack(Items.FILLED_MAP), 0, 0);
        g.pose().popPose();
        // The date, bottom right.
        String date = date(fief.day());
        int w = font.width(date) + 12;
        g.fill(width - w - 1, height - 18, width, height, HUD_EDGE);
        g.fill(width - w, height - 17, width, height, HUD);
        g.drawString(font, date, width - w + 6, height - 12, TEXT, false);
    }

    /**
     * The player's own figure, standing square to the screen, only its head turning after the cursor (the eyes follow
     * the head, as a model pack such as Fresh Animations moves them).
     */
    private void renderPlayer(GuiGraphics g, int x, int feet, int scale, int mouseX, int mouseY) {
        LocalPlayer p = minecraft == null ? null : minecraft.player;
        if (p == null) {
            return;
        }
        float centreY = feet - p.getBbHeight() * scale / 2;
        float eyeY = feet - p.getEyeHeight() * scale;
        float yaw = (float) Math.atan((x - mouseX) / 40F);
        float pitch = (float) Math.atan((eyeY - mouseY) / 40F);
        float body = p.yBodyRot;
        float bodyO = p.yBodyRotO;
        float yRot = p.getYRot();
        float xRot = p.getXRot();
        float xRotO = p.xRotO;
        float head = p.yHeadRot;
        float headO = p.yHeadRotO;
        p.yBodyRot = p.yBodyRotO = 180;
        p.setYRot(180 + yaw * 40);
        p.setXRot(-pitch * 20);
        p.xRotO = p.getXRot();
        p.yHeadRot = p.yHeadRotO = p.getYRot();
        InventoryScreen.renderEntityInInventory(g, x, centreY, scale / p.getScale(), new Vector3f(0, p.getBbHeight() / 2, 0),
                new Quaternionf().rotateZ((float) Math.PI), null, p);
        p.yBodyRot = body;
        p.yBodyRotO = bodyO;
        p.setYRot(yRot);
        p.setXRot(xRot);
        p.xRotO = xRotO;
        p.yHeadRot = head;
        p.yHeadRotO = headO;
    }

    /** A city's summary, beside it on the map. */
    private void renderSummary(GuiGraphics g, SeatCities.City city) {
        BaseMap base = ClientMapCache.map();
        List<String> lines = List.of(
                base == null ? "" : "Seat of the " + base.titleName(city.tier(), city.province()),
                city.districts() + " × " + city.districts() + " districts, " + city.size() + " blocks a side",
                String.format("At %,d, %,d", city.x(), city.z()),
                "Double-click or right-click to manage");
        int w = font.width(name(city));
        for (String l : lines) {
            w = Math.max(w, font.width(l));
        }
        w += 12;
        int h = ROW * (lines.size() + 1) + 6;
        double[] s = at(city);
        if (s == null) {
            return;
        }
        int x = (int) s[0] + 10;
        int y = (int) s[1] + 10;
        g.fill(x, y, x + w, y + h, PANEL_BACK);
        g.renderOutline(x, y, w, h, EDGE);
        g.drawString(font, name(city), x + 6, y + 4, GOLD, false);
        int row = y + 4 + ROW;
        for (String l : lines) {
            g.drawString(font, l, x + 6, row, TEXT, false);
            row += ROW;
        }
    }

    private int menuWidth() {
        return Math.max(font.width(name(menu)), font.width(MANAGE)) + 12;
    }

    private boolean inManageRow(double mouseX, double mouseY) {
        int rowY = menuY + 4 + ROW;
        return mouseX >= menuX && mouseX < menuX + menuWidth() && mouseY >= rowY - 2 && mouseY < rowY + ROW - 2;
    }

    private void renderMenu(GuiGraphics g, int mouseX, int mouseY) {
        int w = menuWidth();
        g.fill(menuX, menuY, menuX + w, menuY + ROW * 2 + 6, 0xF0201D18);
        g.renderOutline(menuX, menuY, w, ROW * 2 + 6, EDGE);
        g.drawString(font, name(menu), menuX + 6, menuY + 4, GOLD, false);
        int rowY = menuY + 4 + ROW;
        if (inManageRow(mouseX, mouseY)) {
            g.fill(menuX + 1, rowY - 2, menuX + w - 1, rowY + ROW - 2, HOVER);
        }
        g.drawString(font, MANAGE, menuX + 6, rowY, TEXT, false);
    }

    /** Opens a city's manager; only from the overworld, where the cities are. */
    private void manage(SeatCities.City city) {
        if (minecraft != null && minecraft.level != null && minecraft.level.dimension() == Level.OVERWORLD) {
            minecraft.setScreen(new CityManagerScreen(city, name(city)));
        }
    }

    /** Over the HUD's bars and panels, where the map takes no clicks, drags or scrolls. */
    private boolean overHud(double x, double y) {
        return y < 19 && x >= width - 186
                || x >= width - 21 && y < 24 + 9 * 22
                || x < 135 && y >= height - 52
                || x < 17 && y >= height - 92
                || x >= 139 && x < 159 && y >= height - 25 && y < height - 5
                || y >= height - 18 && x >= width - font.width(date(fief.day())) - 13;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (menu != null) { // any click closes the menu; one on its row also opens the manager
            SeatCities.City city = menu;
            boolean onRow = button == 0 && inManageRow(mouseX, mouseY);
            menu = null;
            if (onRow) {
                manage(city);
            }
            return true;
        }
        if (overHud(mouseX, mouseY)) {
            if (button == 0 && minecraft != null) {
                for (Spot s : spots()) {
                    if (s.over(mouseX, mouseY) && s.click() != null) {
                        s.click().run();
                        break;
                    }
                }
            }
            return true;
        }
        SeatCities.City city = ownedAt(mouseX, mouseY);
        if (city == null) {
            summary = null;
            SeatCities.City seat = seatAt(mouseX, mouseY);
            if (seat != null && button == 0 && map != null) {
                // The flat map's double-click on a hold (its holder's page), worked as if the seat's mark there were clicked.
                long now = Util.getMillis();
                boolean twice = seat == lastClicked && now - lastClick < DOUBLE_CLICK_MS;
                lastClicked = seat;
                lastClick = now;
                if (twice) {
                    titles.mouseClicked(view, map, view.screenX(seat.x()), view.screenY(seat.z()), 0, true);
                }
                return true;
            }
            if (camera != null && map != null) {
                long now = Util.getMillis();
                boolean twice = now - lastNameClick < DOUBLE_CLICK_MS;
                lastNameClick = now;
                if (labels.mouseClicked(view, map, mouseX, mouseY, button, twice)) {
                    return true; // a nation's name: twice opens its Faction Info
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        if (button == 1) {
            menu = city;
            menuX = (int) mouseX + 4;
            menuY = (int) mouseY + 4;
        } else if (button == 0) {
            long now = Util.getMillis();
            if (city == lastClicked && now - lastClick < DOUBLE_CLICK_MS) {
                manage(city);
            } else {
                summary = city;
            }
            lastClicked = city;
            lastClick = now;
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        return overHud(mouseX, mouseY) || super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        return overHud(mouseX, mouseY) || super.mouseScrolled(mouseX, mouseY, scrollX, delta);
    }
}
