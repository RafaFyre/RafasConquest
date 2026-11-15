package rafasconquest.client;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.init.ModEntityTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.core.SeatCities;
import rafasconquest.net.ConquestNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * The city manager, opened from the fief menu on a city the player owns. The server holds the player, unseen, above the
 * city ({@link rafasconquest.peerage.CityManager}); here the view leaves them for a camera circling the city's middle:
 * drag to turn it around, scroll to come nearer or draw back. Esc goes back to the game, a click on the city's name to the
 * fief menu.
 * <p>
 * Over it, a HUD laid out as Frostpunk 2's, drawn in pixels: on a 640-wide canvas scaled by whole screen pixels, so every
 * line is crisp at any GUI scale. Its figures are samples (no system behind them yet), the week and day excepted.
 */
public class CityManagerScreen extends Screen {
    private static final double TURN = 0.4;
    private static final int CANVAS = 640;
    private static final int BAR = 0xD0161C22;
    private static final int BAR_DARK = 0xE00E1216;
    private static final int LINE = 0xFFB8C0C8;
    private static final int LINE_DIM = 0x80B8C0C8;
    private static final int TEXT = 0xFFE8ECEF;
    private static final int MUTED = 0xFF8A949C;
    private static final int RED = 0xFFD8402C;
    private static final int GOLD = 0xFFE0B040;
    private static final int TEAL = 0xFF40D8C0;
    private static final String SAMPLE = "Sample figure: not built yet";

    private final SeatCities.City city;
    private Marker camera;
    private boolean hidGui;
    private boolean left;
    private double yaw = 45;
    private double pitch = 40;
    private double radius;
    private double groundY = Double.NaN;
    private final List<AbstractRecruitEntity> council = new ArrayList<>();

    /** Something on the HUD, in canvas units: its box, its hover lines, what a click does (null: nothing yet). */
    private record Spot(int x, int y, int w, int h, List<Component> tip, @Nullable Runnable click) {
        boolean over(double mx, double my) {
            return mx >= x && mx < x + w && my >= y && my < y + h;
        }
    }

    public CityManagerScreen(SeatCities.City city, String name) {
        super(Component.literal(name));
        this.city = city;
        this.radius = city.size() * 0.9;
    }

    @Override
    protected void init() {
        if (camera == null && minecraft != null) {
            ConquestNetwork.cityView(city.province());
            hidGui = minecraft.options.hideGui;
            minecraft.options.hideGui = true;
            aim();
        }
        if (council.isEmpty() && minecraft != null && minecraft.level != null) {
            for (int variant : new int[]{6, 1, 11, 16, 3}) {
                AbstractRecruitEntity e = ModEntityTypes.RECRUIT.get().create(minecraft.level);
                if (e != null) {
                    e.setVariant(variant);
                    council.add(e);
                }
            }
        }
    }

    /** Puts the camera on its circle about the city's middle, looking at it. */
    private void aim() {
        if (minecraft == null || minecraft.level == null) {
            return;
        }
        // A fresh camera if there is none, or the world was swapped under it.
        if (camera == null || camera.level() != minecraft.level) {
            camera = new Marker(EntityType.MARKER, minecraft.level);
            place();
            camera.setOldPosAndRot();
        }
        if (minecraft.getCameraEntity() != camera) {
            minecraft.setCameraEntity(camera);
        }
        camera.setOldPosAndRot();
        place();
    }

    private void place() {
        if (minecraft.level.hasChunkAt(city.x(), city.z())) {
            groundY = minecraft.level.getHeight(Heightmap.Types.MOTION_BLOCKING, city.x(), city.z());
        } else if (Double.isNaN(groundY) && minecraft.player != null) {
            groundY = minecraft.player.getY() - 40; // the server holds the player 40 above the ground
        }
        Vec3 f = forward();
        camera.setPos(city.x() + 0.5 - f.x * radius, groundY - f.y * radius, city.z() + 0.5 - f.z * radius);
        camera.setYRot((float) yaw);
        camera.setXRot((float) pitch);
    }

    /** Minecraft's look direction for the camera's (yaw, pitch). */
    private Vec3 forward() {
        double yawR = Math.toRadians(yaw);
        double pitchR = Math.toRadians(pitch);
        return new Vec3(-Math.sin(yawR) * Math.cos(pitchR), -Math.sin(pitchR), Math.cos(yawR) * Math.cos(pitchR));
    }

    @Override
    public void tick() {
        aim();
    }

    // --- The canvas: 640 wide, each unit a whole number of screen pixels. ---

    /** Screen pixels per canvas unit. */
    private int pixel() {
        return Math.max(1, minecraft.getWindow().getWidth() / CANVAS);
    }

    /** GUI units per canvas unit. */
    private float k() {
        return (float) (pixel() / minecraft.getWindow().getGuiScale());
    }

    private int canvasW() {
        return minecraft.getWindow().getWidth() / pixel();
    }

    private int canvasH() {
        return minecraft.getWindow().getHeight() / pixel();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // No background: the city itself is the view.
        float k = k();
        double mx = mouseX / k;
        double my = mouseY / k;
        List<Spot> spots = new ArrayList<>();
        g.pose().pushPose();
        g.pose().scale(k, k, 1);
        drawHud(g, spots, mx, my);
        g.pose().popPose();
        drawCouncil(g, spots, k, mouseX, mouseY);
        for (Spot s : spots) {
            if (s.over(mx, my)) {
                g.pose().pushPose();
                g.pose().translate(0, 0, 400);
                g.renderComponentTooltip(font, s.tip(), mouseX, mouseY);
                g.pose().popPose();
                break;
            }
        }
    }

    private void drawHud(GuiGraphics g, List<Spot> spots, double mx, double my) {
        int w = canvasW();
        int h = canvasH();
        int mid = w / 2;
        g.renderOutline(2, 2, w - 4, h - 4, LINE_DIM); // the thin frame round the whole view
        markers(g, spots, w, h);

        // Top bar and the time row under it.
        g.fill(0, 0, w, 20, BAR);
        g.fill(0, 20, w, 21, LINE_DIM);
        g.fill(0, 21, w, 31, BAR_DARK);
        g.fill(0, 31, w, 32, LINE_DIM);

        // City name and date, top left; a click goes back to the fief menu.
        String name = font.plainSubstrByWidth(title.getString().toUpperCase(), 68);
        g.drawString(font, name, 4, 2, TEXT, false);
        String date = date();
        g.drawString(font, date, 4, 11, MUTED, false);
        int nameW = Math.max(font.width(name), font.width(date)) + 4;
        spots.add(new Spot(0, 0, nameW, 20, List.of(Component.literal(title.getString()),
                Component.literal("The city and today's date").withColor(MUTED), Component.literal("Click: back to the fief menu").withColor(GOLD)), this::back));

        // Centre tab: heat. Drawn first so the stat groups know where it starts.
        int tabL = mid - 70;
        int tabR = mid + 70;
        plate(g, tabL, 0, tabR - tabL, 34);
        int x = tabL + 6;
        x = stat(g, spots, x, 4, Items.BLAZE_POWDER, "-935", RED, -0.6F, "Heat", "Heat supply minus demand. Negative: the city is short of heat and districts go cold.", "Hover in Frostpunk 2: where the demand comes from");
        g.fill(x - 2, 4, x - 1, 28, LINE_DIM);
        x = stat(g, spots, x + 2, 4, Items.COAL, "+35", TEXT, 0.3F, "Coal", "Weekly change in the coal stockpile that feeds the generator", null);
        x = stat(g, spots, x, 4, Items.INK_SAC, "-79", TEXT, -0.2F, "Oil", "Weekly change in the oil stockpile", null);
        stat(g, spots, x, 4, Items.FIREWORK_STAR, "0", MUTED, 0, "Steam", "Steam fuel: not unlocked", null);

        // Left groups: people, then currencies; the currencies drop to the time row if the tab is in their way.
        x = 4 + nameW + 4;
        x = stat(g, spots, x, 6, Items.PLAYER_HEAD, "47777", TEXT, Float.NaN, "Population", "Everyone living in the city", null);
        x = stat(g, spots, x, 6, Items.IRON_PICKAXE, "8977", TEXT, Float.NaN, "Workforce", "People free to work: districts, building and frostbreaking take from it", null);
        x = stat(g, spots, x, 6, Items.SPIDER_EYE, "0", TEXT, Float.NaN, "Sick and injured", "People who cannot work until a hospital heals them", null);
        int cx = x + 6;
        int cy = 6;
        if (cx + 120 > tabL) {
            cx = 112;
            cy = 22;
        }
        cx = stat(g, spots, cx, cy, Items.PAPER, "1248", TEXT, Float.NaN, "Heatstamps", "The city's money: pays for building and research; earned each week by population", null);
        cx = stat(g, spots, cx, cy, Items.BRICKS, "2456", TEXT, Float.NaN, "Prefabs", "Needed to build and repair districts and buildings", null);
        stat(g, spots, cx, cy, Items.HEART_OF_THE_SEA, "48", TEXT, Float.NaN, "Cores", "Rare: only found in the Frostland; for advanced buildings", null);

        // Right group: needs (output minus demand, with a bar), then guards and teams.
        x = w - 222;
        x = stat(g, spots, x, 4, Items.RED_BED, "-94", TEXT, -0.3F, "Shelter", "Housing output minus demand. Short: people go cold. Not stored.", null);
        x = stat(g, spots, x, 4, Items.BREAD, "-159", TEXT, -0.5F, "Food", "Output minus demand. Short: hunger, and disease is likelier. Bar: red short, gold stored.", null);
        x = stat(g, spots, x, 4, Items.IRON_INGOT, "-200", TEXT, -0.8F, "Materials", "Output minus demand. Short: squalor, and districts break down.", null);
        x = stat(g, spots, x, 4, Items.CHEST, "-104", TEXT, 0.2F, "Goods", "Output minus demand. Short: crime, and fewer heatstamps each week.", null);
        g.fill(x - 2, 3, x - 1, 17, LINE_DIM);
        x = stat(g, spots, x + 2, 6, Items.SHIELD, "52", TEXT, Float.NaN, "Guard squads", "Cut crime and put down protests; raised at watchtowers", null);
        stat(g, spots, x, 6, Items.COMPASS, "15", TEXT, Float.NaN, "Frostland teams", "Scouts for expeditions and outposts; logistics districts train them", null);

        // Time row: speed buttons, week and day.
        String[] speeds = {"II", ">", ">>", ">>>"};
        String[] speedTips = {"Pause (Space)", "Normal speed (1)", "Fast (2)", "Fastest (3)"};
        int sx = 4;
        for (int i = 0; i < 4; i++) {
            int sw = font.width(speeds[i]) + 6;
            boolean on = i == 1;
            if (on) {
                g.fill(sx, 22, sx + sw, 30, 0x40FFFFFF);
            }
            g.drawString(font, speeds[i], sx + 3, 22, on ? TEXT : MUTED, false);
            spots.add(new Spot(sx, 21, sw, 10, List.of(Component.literal(speedTips[i]), Component.literal("The game runs on the world's own clock").withColor(MUTED)), null));
            sx += sw + 2;
        }
        long day = minecraft.level == null ? 0 : minecraft.level.getDayTime() / 24000L;
        String week = "WEEK: " + (day / 7 + 1) + "  DAY: " + (day % 7 + 1);
        g.drawString(font, week, sx + 4, 22, GOLD, false);
        spots.add(new Spot(sx + 4, 21, font.width(week), 10, List.of(Component.literal("Week and day"), Component.literal("From the world's day count").withColor(MUTED)), null));

        // Weather timeline, right of the time row: the weeks ahead, each a tick.
        int tl = w - 176;
        long firstWeek = day / 7 + 1;
        g.fill(tl, 27, w - 6, 28, LINE_DIM);
        for (int i = 0; i < 9; i++) {
            int tx = tl + 8 + i * 20;
            g.fill(tx, 25, tx + 1, 28, LINE);
            String label = Long.toString(firstWeek + i * 10);
            g.pose().pushPose();
            g.pose().translate(tx + 2, 22.5F, 0);
            g.pose().scale(0.5F, 0.5F, 1);
            g.drawString(font, label, 0, 0, MUTED, false);
            g.pose().popPose();
        }
        g.fill(tl, 22, tl + 3, 30, 0xFF6CA8E0);
        spots.add(new Spot(tl, 21, w - 6 - tl, 10, List.of(Component.literal("Weather forecast"),
                Component.literal("The weeks ahead and their temperature; a storm icon marks a coming whiteout").withColor(MUTED), Component.literal(SAMPLE).withColor(MUTED)), null));

        // Under the tab: the temperature disc between four round buttons.
        disc(g, mid, 46, 13, 0xFF0A0D10, LINE);
        g.drawCenteredString(font, "-40°C", mid, 42, TEXT);
        spots.add(new Spot(mid - 13, 33, 26, 26, List.of(Component.literal("Temperature"), Component.literal("Outside now; heat demand rises as it falls").withColor(MUTED), Component.literal(SAMPLE).withColor(MUTED)), null));
        roundButton(g, spots, mid - 64, 40, 8, Items.BOWL, mx, my, "Overlay: food", "Shows where food is short (number keys pick overlays)");
        roundButton(g, spots, mid - 38, 40, 8, Items.BRUSH, mx, my, "Overlay: squalor", "Shows which districts spoil their neighbours");
        roundButton(g, spots, mid + 38, 40, 8, Items.FURNACE, mx, my, "Generator", "Click in Frostpunk 2: the generator and its overdrive slider (about +35% heat, damages it)");
        roundButton(g, spots, mid + 64, 40, 8, Items.SPYGLASS, mx, my, "Overlay: heat", "Shows how warm each district is");

        // Top right: the Frostland map, and two warnings to its left.
        roundButton(g, spots, w - 26, 52, 14, Items.FILLED_MAP, mx, my, "Frostland", "The map outside the city: expeditions, outposts, colonies, trails");
        roundButton(g, spots, w - 86, 50, 5, Items.SNOWBALL, mx, my, "Warning: cold", "Some housing is colder than Liveable");
        roundButton(g, spots, w - 86, 63, 5, Items.WHEAT_SEEDS, mx, my, "Warning: food", "Food output is below demand");

        // Left: the goals.
        int gy = 44;
        gy = goal(g, spots, gy, "GROW THE CITY", List.of("Have 35,000 people living in the city (20,387/35,000)", "Solve every problem in the city (4/5)"));
        goal(g, spots, gy + 4, "STOP THE COLD", List.of("Heat all housing districts to Liveable or warmer (12/20)"));

        // Bottom: a shade, the faction rail, the tension flask, the trust bar, the menu buttons.
        g.fillGradient(0, h - 60, w, h, 0x00000000, 0xA0000000);
        int rail = h - 22;
        g.fill(mid - 190, rail, mid - 16, rail + 1, LINE_DIM);
        g.fill(mid + 16, rail, mid + 190, rail + 1, LINE_DIM);
        for (int dx : new int[]{-150, -112, -70, 50, 92, 134}) {
            Portraits.diamond(g, mid + dx, rail, 2, 0xFF0E1216, LINE);
        }
        // Tension: a flask filling with black from the bottom.
        orb(g, mid, rail - 2, 15, 0.35F);
        spots.add(new Spot(mid - 15, rail - 17, 30, 30, List.of(Component.literal("Tension"),
                Component.literal("How angry the city is: problems, unpopular laws and fervour fill it.").withColor(MUTED),
                Component.literal("Full: an ultimatum, calm the city in time or be overthrown.").withColor(MUTED), Component.literal(SAMPLE).withColor(MUTED)), null));
        // Trust: a long thin bar under the rail.
        g.fill(mid - 110, h - 7, mid + 110, h - 5, 0xFF2A3036);
        g.fill(mid - 10, h - 7, mid + 22, h - 5, GOLD);
        spots.add(new Spot(mid - 110, h - 9, 220, 6, List.of(Component.literal("Trust: Accepted"),
                Component.literal("Your people's faith in you: each group's approval, weighted by its size.").withColor(MUTED),
                Component.literal("Revered, Respected, Accepted, Tolerated; at nothing, you are overthrown.").withColor(MUTED)), null));
        // Menu buttons: three small gold ones and a ring bottom left, a ring bottom right.
        roundButton(g, spots, 16, h - 14, 8, Items.STONE_BRICKS, mx, my, "Districts (B)", "Lay out a district on cleared ground: housing, food, extraction, industry, logistics");
        roundButton(g, spots, 36, h - 14, 8, Items.IRON_PICKAXE, mx, my, "Frostbreaking", "Clear frozen ground before building on it; takes workforce");
        roundButton(g, spots, 56, h - 14, 8, Items.LECTERN, mx, my, "Hubs and buildings", "Hubs serve the districts beside them; buildings fill a district's slots");
        roundButton(g, spots, 82, h - 16, 11, Items.BELL, mx, my, "Council and laws", "Propose laws and see how each faction will vote");
        roundButton(g, spots, w - 26, h - 16, 11, Items.ENCHANTED_BOOK, mx, my, "Idea Tree (R)", "Research: Survival, Economy and Society; each faction backs its own solution");
    }

    /**
     * One stat: an item icon, its figure, and (unless {@code bar} is NaN) a bar under it: red for a shortage of that share,
     * gold for a surplus. Returns the x after it.
     */
    private int stat(GuiGraphics g, List<Spot> spots, int x, int y, Item icon, String value, int colour, float bar,
                     String name, String detail, @Nullable String extra) {
        item(g, icon, x, y, 10);
        g.drawString(font, value, x + 12, y + 1, colour, false);
        int w = 12 + font.width(value) + 6;
        if (!Float.isNaN(bar)) {
            int bw = w - 6;
            g.fill(x, y + 12, x + bw, y + 14, 0xFF2A3036);
            int part = Math.round(Math.abs(bar) * bw);
            if (bar < 0) {
                g.fill(x, y + 12, x + part, y + 14, RED);
            } else if (bar > 0) {
                g.fill(x + bw - part, y + 12, x + bw, y + 14, GOLD);
            }
        }
        List<Component> tip = new ArrayList<>(List.of(Component.literal(name), Component.literal(detail).withColor(MUTED)));
        if (extra != null) {
            tip.add(Component.literal(extra).withColor(MUTED));
        }
        tip.add(Component.literal(SAMPLE).withColor(MUTED));
        spots.add(new Spot(x, y - 1, w - 2, 16, tip, null));
        return x + w;
    }

    private void item(GuiGraphics g, Item item, float x, float y, float size) {
        g.pose().pushPose();
        g.pose().translate(x, y, 0);
        g.pose().scale(size / 16F, size / 16F, 1);
        g.renderItem(new ItemStack(item), 0, 0);
        g.pose().popPose();
    }

    /** A panel with chamfered bottom corners and a light edge down its sides and along its foot. */
    private void plate(GuiGraphics g, int x, int y, int w, int h) {
        g.fill(x, y, x + w, y + h - 4, BAR);
        g.fill(x + 2, y + h - 4, x + w - 2, y + h - 2, BAR);
        g.fill(x + 4, y + h - 2, x + w - 4, y + h, BAR);
        g.fill(x, y, x + 1, y + h - 4, LINE);
        g.fill(x + w - 1, y, x + w, y + h - 4, LINE);
        g.fill(x + 1, y + h - 4, x + 3, y + h - 3, LINE);
        g.fill(x + w - 3, y + h - 4, x + w - 1, y + h - 3, LINE);
        g.fill(x + 3, y + h - 3, x + 5, y + h - 2, LINE);
        g.fill(x + w - 5, y + h - 3, x + w - 3, y + h - 2, LINE);
        g.fill(x + 5, y + h - 1, x + w - 5, y + h, LINE);
    }

    /** A pixel disc: an edge ring round a fill. */
    private static void disc(GuiGraphics g, int x, int y, int r, int fill, int edge) {
        Portraits.circle(g, x, y, r, edge);
        Portraits.circle(g, x, y, r - 1, fill);
    }

    /** The tension flask: a glass disc filled with black up to {@code level} of its height. */
    private static void orb(GuiGraphics g, int x, int y, int r, float level) {
        Portraits.circle(g, x, y, r + 1, 0xFF0A0D10);
        Portraits.circle(g, x, y, r, LINE);
        Portraits.circle(g, x, y, r - 1, 0xFF5A6670);
        int top = y + r - Math.round(2 * r * level);
        for (int dy = -(r - 1); dy <= r - 1; dy++) {
            if (y + dy >= top) {
                int dx = (int) Math.sqrt((r - 1) * (r - 1) - dy * dy);
                g.fill(x - dx, y + dy, x + dx + 1, y + dy + 1, 0xFF101214);
            }
        }
        g.fill(x - r / 2, y - r / 2, x - r / 2 + 3, y - r / 2 + 1, 0x80FFFFFF); // a glint on the glass
    }

    private void roundButton(GuiGraphics g, List<Spot> spots, int x, int y, int r, Item icon, double mx, double my, String name, String detail) {
        boolean over = (mx - x) * (mx - x) + (my - y) * (my - y) <= r * r;
        disc(g, x, y, r, over ? 0xFF2A3440 : 0xFF141A20, r <= 8 ? GOLD : LINE);
        float size = Math.min(16, r * 1.4F);
        item(g, icon, x - size / 2, y - size / 2, size);
        spots.add(new Spot(x - r, y - r, 2 * r + 1, 2 * r + 1, List.of(Component.literal(name), Component.literal(detail).withColor(MUTED),
                Component.literal("Not built yet").withColor(MUTED)), null));
    }

    /** A goal: its heading with a ring, then its tasks, each a hollow bullet. Returns the y after it. */
    private int goal(GuiGraphics g, List<Spot> spots, int y, String heading, List<String> tasks) {
        int top = y;
        disc(g, 8, y + 4, 4, 0xFF0E1216, LINE);
        g.drawString(font, heading, 16, y, TEXT, true);
        g.fill(4, y + 10, 150, y + 11, LINE_DIM);
        y += 14;
        for (String task : tasks) {
            g.renderOutline(6, y + 2, 4, 4, LINE);
            for (var line : font.split(Component.literal(task), 138)) {
                g.drawString(font, line, 14, y, TEXT, true);
                y += 10;
            }
            y += 2;
        }
        spots.add(new Spot(4, top, 146, y - top, List.of(Component.literal("Goals"), Component.literal("What the city is working towards, with its progress").withColor(MUTED),
                Component.literal(SAMPLE).withColor(MUTED)), null));
        return y;
    }

    /** Problem markers: red discs pinned to spots in the city, drawn where the camera sees them. */
    private void markers(GuiGraphics g, List<Spot> spots, int w, int h) {
        if (minecraft.level == null || Double.isNaN(groundY)) {
            return;
        }
        Item[] icons = {Items.ROTTEN_FLESH, Items.SPIDER_EYE, Items.SNOWBALL};
        String[] names = {"Hunger", "Disease", "Cold"};
        double[][] at = {{-0.25, -0.2}, {0.3, 0.1}, {-0.05, 0.3}};
        Vec3 eye = minecraft.gameRenderer.getMainCamera().getPosition();
        Vec3 f = forward();
        double yawR = Math.toRadians(yaw);
        Vec3 right = new Vec3(-Math.cos(yawR), 0, -Math.sin(yawR));
        Vec3 up = right.cross(f);
        double focal = (h / 2.0) / Math.tan(Math.toRadians(minecraft.options.fov().get()) / 2);
        for (int i = 0; i < icons.length; i++) {
            Vec3 d = new Vec3(city.x() + at[i][0] * city.size(), groundY + 6, city.z() + at[i][1] * city.size()).subtract(eye);
            double z = d.dot(f);
            if (z < 1) {
                continue;
            }
            int sx = (int) (w / 2.0 + d.dot(right) / z * focal);
            int sy = (int) (h / 2.0 - d.dot(up) / z * focal);
            if (sx < 10 || sx > w - 10 || sy < 40 || sy > h - 40) {
                continue;
            }
            disc(g, sx, sy, 7, RED, 0xFF3A0A06);
            item(g, icons[i], sx - 5, sy - 5, 10);
            spots.add(new Spot(sx - 7, sy - 7, 15, 15, List.of(Component.literal("Problem: " + names[i]),
                    Component.literal("A district here suffers it; hover shows its causes, click opens the district").withColor(MUTED),
                    Component.literal(SAMPLE).withColor(MUTED)), null));
        }
    }

    /**
     * The council along the rail: each faction's figure from the chest up, its relations bar under it and its fervour as
     * diamonds above. Drawn in GUI units, as the figure's scissor box is.
     */
    private void drawCouncil(GuiGraphics g, List<Spot> spots, float k, int mouseX, int mouseY) {
        int mid = canvasW() / 2;
        int rail = canvasH() - 22;
        int[] at = {-130, -90, -48, 70, 112};
        String[] names = {"The Labourers", "The Stalwarts", "The Faithkeepers", "The Evolvers", "The Merchants"};
        int[] fervour = {0, 0, 3, 2, 0};
        float[] relation = {0.7F, 0.4F, -0.5F, 0.2F, 0.9F};
        int side = 30;
        for (int i = 0; i < at.length && i < council.size(); i++) {
            int cx = mid + at[i];
            int top = rail - side;
            g.pose().pushPose();
            g.pose().scale(k, k, 1);
            if (fervour[i] > 0) {
                Portraits.circle(g, cx, top + side / 2, side / 2 + 2, 0x60000000);
                for (int d = 0; d < 3; d++) {
                    Portraits.diamond(g, cx - 8 + d * 8, top - 4 - (d == 1 ? 3 : 0), 3, d < fervour[i] ? RED : 0xFF0E1216, d < fervour[i] ? 0xFF3A0A06 : LINE);
                }
            }
            int bar = 22;
            g.fill(cx - bar / 2, rail + 3, cx + bar / 2, rail + 5, 0xFF2A3036);
            int part = Math.round(Math.abs(relation[i]) * bar);
            g.fill(cx - bar / 2, rail + 3, cx - bar / 2 + part, rail + 5, relation[i] < 0 ? RED : GOLD);
            g.pose().popPose();

            AbstractRecruitEntity e = council.get(i);
            Portraits.animate(e);
            int gx1 = Math.round((cx - side / 2F) * k);
            int gy1 = Math.round(top * k);
            int gSide = Math.round(side * k);
            int scale = (int) (gSide / (0.55F * e.getBbHeight()));
            g.enableScissor(gx1, gy1, gx1 + gSide, gy1 + gSide);
            InventoryScreen.renderEntityInInventoryFollowsMouse(g, gx1, gy1 + 3, gx1 + gSide, gy1 + 3 + (int) (scale * e.getBbHeight()),
                    scale, 0.0625F, mouseX, mouseY, e);
            g.disableScissor();

            spots.add(new Spot(cx - side / 2, top - 8, side, side + 14, List.of(Component.literal(names[i]),
                    Component.literal("Relations: the bar under them, red unhappy, gold content").withColor(MUTED),
                    Component.literal("Fervour: " + fervour[i] + " of 3 diamonds; radicals protest, then riot").withColor(MUTED),
                    Component.literal("Click in Frostpunk 2: gift or collect heatstamps, promise, calm, council seats, special").withColor(MUTED),
                    Component.literal(SAMPLE).withColor(MUTED)), null));
        }
    }

    private String date() {
        long day = minecraft.level == null ? 0 : minecraft.level.getDayTime() / 24000L;
        return rafasconquest.client.map.GrandStrategyScreen.date(day).toUpperCase();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            float k = k();
            // Only the name is live; rebuilt from the layout, which is cheap.
            int nameW = Math.max(font.width(font.plainSubstrByWidth(title.getString().toUpperCase(), 68)), font.width(date())) + 4;
            if (mouseX / k < nameW && mouseY / k < 20) {
                back();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (button == 0) {
            yaw += dx * TURN;
            pitch = Mth.clamp(pitch + dy * TURN, 10, 85);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        radius = Mth.clamp(radius * Math.pow(0.85, delta), 15, 250);
        return true;
    }

    /** Back to the fief menu: the server puts the player back and sends the menu, which replaces this screen. */
    private void back() {
        if (!left) {
            left = true;
            ConquestNetwork.cityView(ConquestNetwork.CITY_BACK);
        }
    }

    @Override
    public void removed() {
        if (!left) {
            left = true;
            ConquestNetwork.cityView(ConquestNetwork.CITY_LEAVE);
        }
        if (minecraft != null) {
            minecraft.setCameraEntity(minecraft.player);
            minecraft.options.hideGui = hidGui;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false; // the server must go on, to send the city's chunks
    }
}
