package rafasconquest.client;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import rafasconquest.peerage.CityStock;
import rafasconquest.peerage.CityStock.Resource;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.awt.font.TransformAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.IntConsumer;

/**
 * Paints the city manager's HUD as Frostpunk 2's, with Java2D, at the window's own pixels: smooth shapes, a thin condensed
 * font, white glyph icons. Laid out in the reference screenshot's units (2000 wide); things at the bottom keep their
 * distance from the bottom edge. Its figures are samples, the date, week and day excepted.
 */
final class CityHud {
    static final float REF_W = 2000;
    static final float REF_H = 1125;
    static final String SAMPLE = "Sample figure: not built yet";
    static final int MUTED_RGB = 0x9AA4AC;

    /** The city's factions at court: each one's x on the rail, its name, what it is, its fervour (0 to 3) and how it feels about the holder. */
    static final float[] FACTION_X = {560, 800, 1250};
    static final String[] FACTIONS = {"The Peasantry", "The Merchant Guilds", "The Great Houses"};
    static final String[] ABOUT = {"The common folk of the city: labourers, craftsmen, the poor",
            "The guilds of traders and makers who run the city's markets", "The families of standing in the city, old blood and new money"};
    static final int[] FERVOUR = {0, 1, 0};
    static final String[] RELATION = {"Content", "Restless", "Loyal"};
    static final float RAIL = 1058;


    /** Something on the HUD, in reference units: its box, its hover lines, what a click does (null: nothing yet). */
    record Spot(float x, float y, float w, float h, List<Component> tip, @Nullable Runnable click) {
        boolean over(double mx, double my) {
            return mx >= x && mx < x + w && my >= y && my < y + h;
        }
    }

    private static final Color TEXT = new Color(0xE8ECEF);
    private static final Color SOFT = new Color(0xC4CCD2);
    private static final Color MUTED = new Color(0x8A949C);
    private static final Color ICON = new Color(0xD0D7DD);
    private static final Color LINE = new Color(210, 218, 224, 160);
    private static final Color LINE_DIM = new Color(210, 218, 224, 70);
    private static final Color RED = new Color(0xD8402C);
    private static final Color GOLD = new Color(0xE0B040);
    private static final Color PALE_GOLD = new Color(0xE6D7A6);
    private static final Color TRACK = new Color(40, 46, 52, 220);
    private static final String FAMILY = family();

    final List<Spot> spots = new ArrayList<>();
    private final Graphics2D g;
    private final float refH;
    /** The city's stockpile by {@link rafasconquest.peerage.CityStock.Resource} ordinal, null until the server sends it. */
    @Nullable
    private int[] res;
    private int busy;
    private Runnable flatten;
    private Runnable districts;
    private Runnable roads;
    private Runnable specials;
    private Runnable map;
    private Runnable court;
    private Runnable ideas;
    private IntConsumer faction;

    private CityHud(Graphics2D g, float refH) {
        this.g = g;
        this.refH = refH;
    }

    /** Bahnschrift (Windows' DIN), else a narrow sans, else Java's own. */
    private static String family() {
        List<String> have = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String f : new String[]{"Bahnschrift", "DIN Alternate", "Roboto Condensed", "Arial Narrow", "Segoe UI"}) {
            if (have.contains(f)) {
                return f;
            }
        }
        return Font.SANS_SERIF;
    }

    private static Graphics2D smooth(BufferedImage img, float s) {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.scale(s, s);
        return g;
    }

    /** The whole HUD on a window-sized image; returns its hover spots in reference units. */
    static List<Spot> paint(BufferedImage img, String name, String date, long day, Runnable back, @Nullable int[] res, int busy, Runnable flatten, Runnable districts, Runnable roads,
                            Runnable specials, Runnable map, Runnable court, Runnable ideas, IntConsumer faction) {
        float s = img.getWidth() / REF_W;
        Graphics2D g = smooth(img, s);
        CityHud hud = new CityHud(g, img.getHeight() / s);
        hud.res = res;
        hud.busy = busy;
        hud.flatten = flatten;
        hud.districts = districts;
        hud.roads = roads;
        hud.specials = specials;
        hud.map = map;
        hud.court = court;
        hud.ideas = ideas;
        hud.faction = faction;
        hud.draw(name, date, day, back);
        g.dispose();
        return hud.spots;
    }

    /** A y near the bottom, kept at its distance from the bottom edge. */
    private float b(float y) {
        return refH - (REF_H - y);
    }

    private void draw(String name, String date, long day, Runnable back) {
        // The frame round the view, then the shade along the bottom.
        g.setStroke(new BasicStroke(1.2F));
        g.setColor(new Color(210, 218, 224, 55));
        g.draw(new RoundRectangle2D.Float(10, 74, REF_W - 20, refH - 84, 34, 34));
        g.setPaint(new GradientPaint(0, refH - 230, new Color(0, 0, 0, 0), 0, refH, new Color(0, 0, 0, 165)));
        g.fill(new Rectangle2D.Float(0, refH - 230, REF_W, 230));

        topBar(name, date, day, back);
        underBar();
        goals();
        bottomFrame();
        council();
        buttons();
    }

    // --- The top bar ---

    private void topBar(String name, String date, long day, Runnable back) {
        g.setPaint(new GradientPaint(0, 0, new Color(84, 94, 104, 200), 0, 46, new Color(44, 50, 58, 160)));
        g.fill(new Rectangle2D.Float(0, 0, REF_W, 46));
        g.setColor(new Color(14, 18, 22, 170));
        g.fill(new Rectangle2D.Float(0, 46, REF_W, 26));
        g.setStroke(new BasicStroke(1));
        g.setColor(LINE_DIM);
        g.draw(new Line2D.Float(0, 46, REF_W, 46));
        g.draw(new Line2D.Float(0, 72, REF_W, 72));

        // The centre panel hangs from the bar, its ears curving into it.
        Path2D p = new Path2D.Float();
        p.moveTo(788, 0);
        p.quadTo(815, 0, 815, 22);
        p.lineTo(815, 36);
        p.quadTo(815, 52, 831, 52);
        p.lineTo(1169, 52);
        p.quadTo(1185, 52, 1185, 36);
        p.lineTo(1185, 22);
        p.quadTo(1185, 0, 1212, 0);
        p.closePath();
        g.setPaint(new GradientPaint(0, 0, new Color(30, 35, 40, 240), 0, 52, new Color(12, 15, 18, 240)));
        g.fill(p);
        g.setStroke(new BasicStroke(1.4F));
        g.setColor(LINE);
        g.draw(p);

        // City and date, top left; a click goes back to the fief menu.
        g.setFont(font(17, false));
        String shown = fit(name.toUpperCase(), 236);
        text(shown, 12, 20, 17, false, SOFT);
        text(date, 12, 41, 15, false, MUTED);
        spots.add(new Spot(0, 0, 250, 46, List.of(Component.literal(name), mutedLine("The city and today's date"),
                Component.literal("Click: back to the fief menu").withColor(0xE0B040)), back));

        // People, then currencies.
        stat(262, 23, "people", amount(Resource.POPULATION), 17, TEXT, "Population", "Everyone living in the city", null);
        dots(266, 31, 70, 6);
        stat(344, 23, "workers", res == null ? "-" : (res[Resource.WORKERS.ordinal()] - busy) + "/" + res[Resource.WORKERS.ordinal()], 17, TEXT,
                "Workers", "Free of all the city's workers; flattening holds " + CityStock.FLATTEN_WORKERS + " a plate until it is flat", null);
        stat(554, 23, "stamps", amount(Resource.GOLD), 17, TEXT, "Gold", "The city's money: pays for flattening, districts and research", null);
        stat(628, 23, "prefabs", amount(Resource.RAW), 17, TEXT, "Raw materials", "Needed to build districts", null);
        stat(712, 23, "cores", amount(Resource.HIGH_GRADE), 17, TEXT, "High grade materials", "For advanced buildings", null);

        // The centre panel: heat, then the fuels.
        stat(840, 30, "heat", "-935", 21, TEXT, "Heat", "Heat supply minus demand. Negative: the city is short of heat and districts go cold.",
                "Hover in Frostpunk 2: where the demand comes from");
        bar(843, 41, 64, -0.92F);
        g.setColor(MUTED);
        g.fill(poly(926, 25, 919, 29, 926, 33));
        stat(940, 30, "coal", "+35", 19, TEXT, "Coal", "Weekly change in the coal stockpile that feeds the generator", null);
        dots(942, 41, 58, 0);
        stat(1016, 30, "oil", "-79", 19, TEXT, "Oil", "Weekly change in the oil stockpile", null);
        dots(1018, 41, 58, 0);
        stat(1100, 30, "steam", "0", 19, new Color(0x5C656C), "Steam", "Steam fuel: not unlocked", null);

        // Needs (output minus demand, with a bar), then guards and teams.
        stat(1250, 23, "house", amount(Resource.HOUSING), 17, TEXT, "Housing", "Homes for the city's people", null);
        stat(1318, 23, "food", amount(Resource.FOOD), 17, TEXT, "Food", "The city's food store", null);
        stat(1392, 23, "goods", amount(Resource.LUXURY), 17, TEXT, "Luxury goods", "The city's store of luxury goods", null);
        stat(1736, 23, "guard", "52", 17, TEXT, "Guard squads", "Cut crime and put down protests; raised at watchtowers", null);
        stat(1802, 23, "teams", "15", 17, TEXT, "Frostland teams", "Scouts for expeditions and outposts; logistics districts train them", null);

        // The time row: speeds, week and day.
        String[] speeds = {"pause", "play", "fast", "fastest"};
        String[] speedTips = {"Pause (Space)", "Normal speed (1)", "Fast (2)", "Fastest (3)"};
        for (int i = 0; i < 4; i++) {
            float x = 25 + i * 50;
            icon(speeds[i], x, 59, 15, i == 1 ? Color.WHITE : MUTED);
            spots.add(new Spot(x - 20, 47, 40, 25, List.of(Component.literal(speedTips[i]), mutedLine("The game runs on the world's own clock")), null));
        }
        float wx = text("WEEK: " + (day / 7 + 1), 203, 66, 17, false, PALE_GOLD);
        text("DAY: " + (day % 7 + 1), wx + 22, 66, 17, false, PALE_GOLD);
        spots.add(new Spot(200, 47, 160, 25, List.of(Component.literal("Week and day"), mutedLine("From the world's day count")), null));

        // The weather forecast, right of the time row: the weeks ahead.
        g.setColor(new Color(0x6CA8E0));
        g.fill(new Rectangle2D.Float(1656, 50, 3, 19));
        icon("thermo", 1680, 60, 15, new Color(0x6CA8E0));
        icon("thermo", 1745, 60, 15, new Color(0xE09A50));
        long firstWeek = day / 7 + 1;
        g.setStroke(new BasicStroke(1));
        for (int i = 0; i < 9; i++) {
            float tx = 1712 + i * 34;
            g.setColor(LINE);
            g.draw(new Line2D.Float(tx, 58, tx, 70));
            text(Long.toString(firstWeek + i * 10), tx + 3, 63, 10, false, MUTED);
        }
        spots.add(new Spot(1650, 47, 345, 25, List.of(Component.literal("Weather forecast"),
                mutedLine("The weeks ahead and their temperature; a storm icon marks a coming whiteout"), mutedLine(SAMPLE)), null));
    }

    // --- Under the bar: the temperature, the overlays, the warnings, the Frostland ---

    private void underBar() {
        smallCircle(800, 95, "bowl", "Overlay: food", "Shows where food is short (number keys pick overlays)", false);
        smallCircle(905, 95, "brush", "Overlay: squalor", "Shows which districts spoil their neighbours", false);
        smallCircle(1093, 95, "gear", "Generator", "Click in Frostpunk 2: the generator and its overdrive slider (about +35% heat, damages it)", true);
        smallCircle(1198, 95, "thermo", "Overlay: heat", "Shows how warm each district is", false);

        // The temperature: a dark disc trailing a plume of smoke.
        Random r = new Random(7);
        for (int i = 0; i < 90; i++) {
            float t = r.nextFloat();
            float y = 118 + t * 95;
            float x = 1000 + (float) Math.sin(t * 7) * (4 + t * 14) + (r.nextFloat() - 0.5F) * 14;
            float rad = 16 - t * 9 + r.nextFloat() * 5;
            g.setColor(new Color(10, 11, 13, (int) (55 * (1 - t) + 8)));
            g.fill(new Ellipse2D.Float(x - rad, y - rad * 0.7F, rad * 2, rad * 1.4F));
        }
        g.setPaint(new RadialGradientPaint(990, 84, 44, new float[]{0, 0.55F, 1}, new Color[]{new Color(0x464C52), new Color(0x181B1E), new Color(0x08090A)}));
        g.fill(circle(1000, 95, 34));
        for (int i = 0; i < 160; i++) {
            double a = r.nextDouble() * Math.PI * 2;
            double d = Math.sqrt(r.nextDouble()) * 32;
            g.setColor(new Color(255, 255, 255, 10 + r.nextInt(25)));
            g.fill(circle((float) (1000 + Math.cos(a) * d), (float) (95 + Math.sin(a) * d), 0.8F));
        }
        g.setStroke(new BasicStroke(1.6F));
        g.setColor(new Color(170, 180, 188, 200));
        g.draw(circle(1000, 95, 34));
        g.setFont(font(21, true));
        String t = "-40°C";
        text(t, 1000 - width(t) / 2, 103, 21, true, Color.WHITE);
        spots.add(new Spot(966, 61, 68, 68, List.of(Component.literal("Temperature"), mutedLine("Outside now; heat demand rises as it falls"), mutedLine(SAMPLE)), null));

        // Warnings and the Frostland, top right.
        for (int i = 0; i < 2; i++) {
            float y = 155 + i * 33;
            g.setColor(new Color(74, 82, 90, 210));
            g.fill(circle(1730, y, 12));
            g.setStroke(new BasicStroke(1));
            g.setColor(new Color(190, 200, 208, 130));
            g.draw(circle(1730, y, 12));
            icon(i == 0 ? "snow" : "sprout", 1730, y, 15, Color.WHITE);
            spots.add(new Spot(1718, y - 12, 24, 24, List.of(Component.literal(i == 0 ? "Warning: cold" : "Warning: food"),
                    mutedLine(i == 0 ? "Some housing is colder than Liveable" : "Food output is below demand"), mutedLine("Not built yet")), null));
        }
        ringButton(1948, 140, 40, "mountain", "World map", "The grand strategy map", map);
    }

    // --- The goals, left ---

    private void goals() {
        float y = goal(153, "GROW THE CITY", List.of("Have 35,000 people living in the city (20,387/35,000)", "Solve every problem in the city (4/5)"), false);
        goal(y + 8, "STOP THE COLD", List.of("Heat all housing districts to Liveable or warmer (12/20)"), true);
    }

    /** A goal: its heading (lit and ringed if it is the one in hand), then its tasks, each a hollow bullet. Returns the y after it. */
    private float goal(float base, String heading, List<String> tasks, boolean active) {
        float top = base - 20;
        float x = 28;
        if (active) {
            g.setColor(new Color(255, 255, 255, 34));
            g.fill(new Rectangle2D.Float(15, base - 18, 250, 26));
            g.setStroke(new BasicStroke(2));
            g.setColor(new Color(255, 255, 255, 90));
            g.draw(circle(33, base - 5, 8));
            g.setColor(Color.WHITE);
            g.draw(new Arc2D.Float(25, base - 13, 16, 16, 90, -220, Arc2D.OPEN));
            x = 50;
        }
        text(heading, x, base, 18, false, TEXT);
        g.setStroke(new BasicStroke(1));
        g.setColor(LINE_DIM);
        g.draw(new Line2D.Float(15, base + 9, 265, base + 9));
        float y = base + 33;
        g.setFont(font(15, false));
        for (String task : tasks) {
            g.setStroke(new BasicStroke(1.2F));
            g.setColor(SOFT);
            g.draw(circle(33, y - 5, 4));
            for (String line : wrap(task, 330)) {
                text(line, 45, y, 15, false, TEXT);
                y += 20;
            }
            y += 10;
        }
        spots.add(new Spot(15, top, 370, y - top, List.of(Component.literal("Goals"), mutedLine("What the city is working towards, with its progress"), mutedLine(SAMPLE)), null));
        return y;
    }

    // --- The council along the bottom ---

    private void council() {
        float rail = b(RAIL);
        g.setStroke(new BasicStroke(1));
        for (int i = 0; i < FACTION_X.length; i++) {
            if (FERVOUR[i] > 0) {
                g.setColor(new Color(210, 218, 224, 45));
                g.draw(circle(FACTION_X[i], rail, 72));
            }
        }
        g.setStroke(new BasicStroke(1.3F));
        g.setColor(new Color(210, 218, 224, 150));
        g.draw(new Line2D.Float(425, rail, 958, rail));
        g.draw(new Line2D.Float(1042, rail, 1460, rail));
        for (float x : new float[]{425, 655, 665, 800, 935, 1060, 1070, 1225, 1235, 1460}) {
            diamond(x, rail, 5, new Color(14, 18, 22), new Color(210, 218, 224, 220), 1.2F);
        }
        diamond(383, b(1042), 5, new Color(14, 18, 22), new Color(210, 218, 224, 220), 1.2F);

        // Fervour: three diamonds over the faction's head, red as it rises.
        for (int i = 0; i < FACTION_X.length; i++) {
            float cx = FACTION_X[i];
            int f = i;
            if (FERVOUR[i] > 0) {
                for (int d = 0; d < 3; d++) {
                    float dx = cx - 32 + d * 32;
                    float dy = b(d == 1 ? 992 : 1004);
                    if (d < FERVOUR[i]) {
                        diamond(dx, dy, 12, RED, new Color(0x6A160C), 1.4F);
                        diamond(dx, dy, 3.5F, Color.WHITE, Color.WHITE, 0.5F);
                    } else {
                        diamond(dx, dy, 12, new Color(14, 18, 22, 200), Color.WHITE, 1.6F);
                    }
                }
            }
            spots.add(new Spot(cx - 40, b(985), 80, 120, List.of(Component.literal(FACTIONS[i]),
                    mutedLine(ABOUT[i]), mutedLine("Relations: " + RELATION[i]),
                    mutedLine("Fervour: " + FERVOUR[i] + " of 3 diamonds; radicals protest, then riot"), mutedLine(SAMPLE),
                    Component.literal("Click: their faction at court").withColor(0xE0B040)), () -> faction.accept(f)));
        }

        // Tension: a glass sphere in a silver ring, filling with black from the bottom.
        float cy = rail;
        float r = 38;
        g.setStroke(new BasicStroke(3.5F));
        g.setPaint(new GradientPaint(0, cy - r, new Color(0xF0F4F6), 0, cy + r, new Color(0x5C666E)));
        g.draw(circle(1000, cy, r + 1.5F));
        Shape glass = circle(1000, cy, r);
        g.setPaint(new RadialGradientPaint(986, cy - 16, r * 1.5F, new float[]{0, 0.45F, 1},
                new Color[]{new Color(0xC8D0D8), new Color(0x5A646E), new Color(0x14181C)}));
        g.fill(glass);
        Shape clip = g.getClip();
        g.clip(glass);
        float level = 0.35F;
        g.setColor(new Color(10, 12, 14, 225));
        g.fill(new Rectangle2D.Float(1000 - r, cy + r - 2 * r * level, 2 * r, 2 * r * level));
        g.setClip(clip);
        g.setColor(new Color(255, 255, 255, 110));
        g.fill(new Ellipse2D.Float(972, cy - 30, 26, 11));
        spots.add(new Spot(1000 - r, cy - r, 2 * r, 2 * r, List.of(Component.literal("Tension"),
                mutedLine("How angry the city is: problems, unpopular laws and fervour fill it."),
                mutedLine("Full: an ultimatum, calm the city in time or be overthrown."), mutedLine(SAMPLE)), null));

        // Trust: a long thin bar along the bottom.
        float ty = b(1110);
        g.setColor(TRACK);
        g.fill(new Rectangle2D.Float(650, ty, 700, 3));
        g.setColor(GOLD);
        g.fill(new Rectangle2D.Float(972, ty, 58, 3));
        spots.add(new Spot(650, ty - 5, 700, 13, List.of(Component.literal("Trust: Accepted"),
                mutedLine("Your people's faith in you: each group's approval, weighted by its size."),
                mutedLine("Revered, Respected, Accepted, Tolerated; at nothing, you are overthrown.")), null));
    }

    // --- The frame along the bottom: a frosted strip on the edge, rising into a bracket round each corner's buttons ---

    private void bottomFrame() {
        float top = b(1046);
        float edge = b(1110);
        Path2D left = new Path2D.Float();
        left.moveTo(0, top);
        left.lineTo(330, top);
        left.curveTo(372, top, 372, edge, 430, edge);
        left.lineTo(0, edge);
        left.closePath();
        Path2D right = new Path2D.Float();
        right.moveTo(REF_W, top);
        right.lineTo(1670, top);
        right.curveTo(1628, top, 1628, edge, 1570, edge);
        right.lineTo(REF_W, edge);
        right.closePath();
        Rectangle2D strip = new Rectangle2D.Float(0, edge, REF_W, refH - edge);
        g.setPaint(new GradientPaint(0, top, new Color(70, 80, 90, 150), 0, refH, new Color(28, 33, 38, 200)));
        g.fill(left);
        g.fill(right);
        g.fill(strip);
        // Thin traces, spaced apart: out of the frame's side, along the panel, past the medallions, into the corner ring.
        // Drawn for the left corner, then mirrored for the right.
        Path2D traces = new Path2D.Float();
        traces.moveTo(10, b(985));
        traces.lineTo(10, b(1030));
        traces.quadTo(10, b(1050), 30, b(1050));
        traces.lineTo(375, b(1050));
        traces.moveTo(18, b(1040));
        traces.quadTo(18, b(1057), 36, b(1057));
        traces.lineTo(362, b(1057));
        traces.moveTo(70, b(1057));
        traces.quadTo(90, b(1057), 95, b(1076));
        traces.lineTo(352, b(1076));
        traces.moveTo(30, b(1112));
        traces.lineTo(345, b(1112));
        traces.quadTo(358, b(1112), 362, b(1103));
        g.setStroke(new BasicStroke(1.1F));
        g.setColor(new Color(228, 233, 238, 150));
        g.draw(traces);
        g.draw(new AffineTransform(-1, 0, 0, 1, REF_W, 0).createTransformedShape(traces));
        // The rail runs on into the corner rings.
        g.setColor(new Color(210, 218, 224, 150));
        g.draw(new Line2D.Float(414, b(RAIL), 425, b(RAIL)));
        g.draw(new Line2D.Float(1460, b(RAIL), 1586, b(RAIL)));
    }

    // --- The menu buttons ---

    private void buttons() {
        medallion(81, b(1090), "road", "Main roads", "Drag a main road out from the city's edge or another road, at any angle, over any ground: 5 wide, cobblestone; free for now", roads);
        medallion(148, b(1090), "district", "Districts", "Build a district on flat ground touching the city or another district: housing, food, extraction, industrial, logistics; "
                + CityStock.DISTRICT_GOLD + " gold and " + CityStock.DISTRICT_RAW + " raw materials each", districts);
        medallion(215, b(1090), "pick", "Flatten ground", "Level 32-block plates round the city before building districts on them: "
                + CityStock.FLATTEN_GOLD + " gold and " + CityStock.FLATTEN_WORKERS + " workers a plate", flatten);
        medallion(283, b(1090), "hub", "Special buildings", "Put down a monument of the city's style on flat ground touching the city, a road, a district or another building; free for now", specials);
        ringButton(383, b(1080), 31, "spiral", "Court", "The city's factions bring their requests; your word is final", court);
        ringButton(1617, b(1080), 31, "tree", "Idea Tree (R)", "The ages and what each one brings", ideas);
    }

    private String amount(Resource r) {
        return res == null ? "-" : Integer.toString(res[r.ordinal()]);
    }

    private void medallion(float x, float y, String glyph, String name, String detail) {
        medallion(x, y, glyph, name, detail, null);
    }

    private void medallion(float x, float y, String glyph, String name, String detail, @Nullable Runnable click) {
        float r = 24;
        g.setPaint(new RadialGradientPaint(x - 8, y - 9, r * 1.5F, new float[]{0, 0.5F, 1},
                new Color[]{new Color(0xF8E092), new Color(0xC48C32), new Color(0x6A4810)}));
        g.fill(circle(x, y, r));
        g.setStroke(new BasicStroke(1.6F));
        g.setColor(new Color(0x3A2808));
        g.draw(circle(x, y, r));
        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(248, 228, 160, 140));
        g.draw(circle(x, y, r - 4));
        g.setColor(new Color(228, 233, 238, 150));
        g.draw(circle(x, y, r + 5)); // the trace ring round each medallion
        icon(glyph, x, y, 25, new Color(0x3E2A0C));
        spots.add(new Spot(x - r, y - r, 2 * r, 2 * r, List.of(Component.literal(name), mutedLine(detail),
                click == null ? mutedLine("Not built yet") : Component.literal("Click: start or stop").withColor(0xE0B040)), click));
    }

    private void ringButton(float x, float y, float r, String glyph, String name, String detail, Runnable click) {
        g.setColor(new Color(16, 20, 24, 190));
        g.fill(circle(x, y, r));
        g.setStroke(new BasicStroke(2));
        g.setColor(new Color(220, 226, 230, 230));
        g.draw(circle(x, y, r));
        g.setStroke(new BasicStroke(1));
        g.setColor(LINE);
        g.draw(circle(x, y, r - 6));
        icon(glyph, x, y, r * 1.0F, Color.WHITE);
        spots.add(new Spot(x - r, y - r, 2 * r, 2 * r, List.of(Component.literal(name), mutedLine(detail), Component.literal("Click: open").withColor(0xE0B040)), click));
    }

    private void smallCircle(float x, float y, String glyph, String name, String detail, boolean dark) {
        g.setColor(dark ? new Color(6, 7, 8, 235) : new Color(26, 30, 34, 215));
        g.fill(circle(x, y, 20));
        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(90, 98, 106, 200));
        g.draw(circle(x, y, 20));
        icon(glyph, x, y, 20, dark ? Color.WHITE : new Color(0xA8B0B8));
        spots.add(new Spot(x - 20, y - 20, 40, 40, List.of(Component.literal(name), mutedLine(detail), mutedLine("Not built yet")), null));
    }

    // --- Pieces ---

    /** One figure: its glyph and its value. Returns the x after it. */
    private float stat(float x, float base, String glyph, String value, float size, Color colour, String name, String detail, @Nullable String extra) {
        icon(glyph, x + 8, base - size * 0.34F, size * 0.95F, colour.getRed() < 120 ? colour : ICON);
        float end = text(value, x + 21, base, size, false, colour);
        List<Component> tip = new ArrayList<>(List.of(Component.literal(name), mutedLine(detail)));
        if (extra != null) {
            tip.add(mutedLine(extra));
        }
        tip.add(mutedLine(SAMPLE));
        spots.add(new Spot(x - 2, base - size - 2, end - x + 6, size + 12, tip, null));
        return end;
    }

    /** A thin bar: red from the left for a shortage of that share, gold from the right for a surplus. */
    private void bar(float x, float y, float w, float share) {
        g.setColor(TRACK);
        g.fill(new Rectangle2D.Float(x, y, w, 3));
        float part = Math.abs(share) * w;
        g.setColor(share < 0 ? RED : GOLD);
        g.fill(new Rectangle2D.Float(share < 0 ? x : x + w - part, y, part, 3));
    }

    /** A dotted bar; the last {@code dim} dots darker. */
    private void dots(float x, float y, float w, int dim) {
        int n = (int) (w / 7);
        for (int i = 0; i < n; i++) {
            g.setColor(i >= n - dim ? new Color(120, 128, 136, 140) : new Color(200, 208, 214, 200));
            g.fill(new Rectangle2D.Float(x + i * 7, y, 4.5F, 2));
        }
    }

    private void diamond(float x, float y, float r, Color fill, Color edge, float stroke) {
        Shape d = poly(x, y - r, x + r, y, x, y + r, x - r, y);
        g.setColor(fill);
        g.fill(d);
        g.setStroke(new BasicStroke(stroke));
        g.setColor(edge);
        g.draw(d);
    }

    private static Font font(float size, boolean bold) {
        Map<TextAttribute, Object> a = new HashMap<>();
        a.put(TextAttribute.FAMILY, FAMILY);
        a.put(TextAttribute.SIZE, size);
        a.put(TextAttribute.WEIGHT, bold ? TextAttribute.WEIGHT_SEMIBOLD : TextAttribute.WEIGHT_REGULAR);
        a.put(TextAttribute.TRACKING, 0.07F);
        a.put(TextAttribute.TRANSFORM, new TransformAttribute(AffineTransform.getScaleInstance(0.9, 1)));
        return new Font(a);
    }

    /** Text at a baseline, with a soft shadow; returns the x after it. */
    private float text(String s, float x, float base, float size, boolean bold, Color c) {
        g.setFont(font(size, bold));
        g.setColor(new Color(0, 0, 0, 120));
        g.drawString(s, x + 1, base + 1);
        g.setColor(c);
        g.drawString(s, x, base);
        return x + width(s);
    }

    private float width(String s) {
        return (float) g.getFont().getStringBounds(s, g.getFontRenderContext()).getWidth();
    }

    private String fit(String s, float w) {
        while (s.length() > 1 && width(s) > w) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private List<String> wrap(String s, float w) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : s.split(" ")) {
            String next = line.isEmpty() ? word : line + " " + word;
            if (width(next) > w && !line.isEmpty()) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(next);
            }
        }
        lines.add(line.toString());
        return lines;
    }

    private static Component mutedLine(String s) {
        return Component.literal(s).withColor(MUTED_RGB);
    }

    private static Ellipse2D circle(float x, float y, float r) {
        return new Ellipse2D.Float(x - r, y - r, 2 * r, 2 * r);
    }

    private static Path2D poly(float... xy) {
        Path2D p = new Path2D.Float();
        p.moveTo(xy[0], xy[1]);
        for (int i = 2; i < xy.length; i += 2) {
            p.lineTo(xy[i], xy[i + 1]);
        }
        p.closePath();
        return p;
    }

    /** A white glyph in a 20-unit box about (x, y), drawn {@code size} across. */
    private void icon(String kind, float x, float y, float size, Color c) {
        Graphics2D q = (Graphics2D) g.create();
        q.translate(x, y);
        q.scale(size / 20, size / 20);
        q.setColor(c);
        q.setStroke(new BasicStroke(1.8F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        switch (kind) {
            case "people" -> {
                q.fill(circle(0, -5, 4));
                q.fill(new Arc2D.Float(-8, 0, 16, 18, 0, 180, Arc2D.CHORD));
            }
            case "workers", "gear" -> q.fill(gear());
            case "sick" -> {
                q.fill(new RoundRectangle2D.Float(-2.6F, -8, 5.2F, 16, 2, 2));
                q.fill(new RoundRectangle2D.Float(-8, -2.6F, 16, 5.2F, 2, 2));
            }
            case "stamps" -> {
                for (int i = 0; i < 3; i++) {
                    q.fill(new RoundRectangle2D.Float(-7, 4 - i * 5.5F, 14, 4.2F, 3, 3));
                }
            }
            case "prefabs" -> {
                q.fill(poly(-8, 3, 0, -1, 8, 3, 0, 7));
                q.setComposite(AlphaComposite.SrcOver.derive(0.7F));
                q.fill(poly(-8, -3, 0, -7, 8, -3, 0, 1));
            }
            case "cores" -> {
                q.draw(new RoundRectangle2D.Float(-5, -6, 10, 15, 3, 3));
                q.fill(new Rectangle2D.Float(-2, -9, 4, 2.5F));
                q.fill(new Rectangle2D.Float(-2.5F, 1, 5, 5));
            }
            case "heat" -> {
                Path2D f = new Path2D.Float();
                f.moveTo(0, -9.5);
                f.curveTo(7, -3, 8, 3, 6, 6);
                f.curveTo(4, 9.5, -4, 9.5, -6, 6);
                f.curveTo(-8, 2, -5, -1, -3, -3);
                f.curveTo(-3, 0, -1, 1, 0, 1);
                f.curveTo(2, -2, 2, -6, 0, -9.5);
                f.closePath();
                q.fill(f);
            }
            case "coal" -> q.fill(poly(-8, 7, -7, 0, -2, -5, 4, -4, 8, 1, 7, 7));
            case "oil", "drop" -> q.fill(drop());
            case "steam" -> {
                q.fill(circle(-4, 2, 4.5F));
                q.fill(circle(2, -1, 5.5F));
                q.fill(circle(5, 4, 3.5F));
            }
            case "house" -> q.fill(poly(-9, 0, 0, -9, 9, 0, 6.5F, 0, 6.5F, 8, -6.5F, 8, -6.5F, 0));
            case "food" -> {
                q.draw(new Line2D.Float(0, 9, 0, -8));
                for (int j = 0; j < 3; j++) {
                    for (int side = -1; side <= 1; side += 2) {
                        Graphics2D l = (Graphics2D) q.create();
                        l.translate(side * 2.6, -5 + j * 4.5);
                        l.rotate(side * 0.6);
                        l.fill(new Ellipse2D.Float(-1.6F, -3, 3.2F, 6));
                        l.dispose();
                    }
                }
            }
            case "materials" -> {
                q.fill(poly(0, -9, 8.5F, -4.5F, 0, 0, -8.5F, -4.5F));
                q.setComposite(AlphaComposite.SrcOver.derive(0.55F));
                q.fill(poly(-8.5F, -3, -0.8F, 1.4F, -0.8F, 9.5F, -8.5F, 5));
                q.setComposite(AlphaComposite.SrcOver.derive(0.8F));
                q.fill(poly(0.8F, 1.4F, 8.5F, -3, 8.5F, 5, 0.8F, 9.5F));
            }
            case "goods" -> {
                Path2D j = new Path2D.Float();
                j.moveTo(-3, -9);
                j.lineTo(3, -9);
                j.lineTo(3, -5);
                j.curveTo(9, -3, 9, 7, 4, 9);
                j.lineTo(-4, 9);
                j.curveTo(-9, 7, -9, -3, -3, -5);
                j.closePath();
                q.fill(j);
            }
            case "guard" -> {
                Path2D sh = new Path2D.Float();
                sh.moveTo(-7.5, -8.5);
                sh.lineTo(7.5, -8.5);
                sh.lineTo(7.5, 0);
                sh.curveTo(7.5, 5, 3, 8, 0, 9.5);
                sh.curveTo(-3, 8, -7.5, 5, -7.5, 0);
                sh.closePath();
                q.fill(sh);
            }
            case "teams" -> {
                q.draw(circle(0, 0, 8));
                q.fill(poly(5, -5, 1.6F, 1.6F, -5, 5, -1.6F, -1.6F));
            }
            case "pause" -> {
                q.fill(new Rectangle2D.Float(-5.5F, -7, 3.6F, 14));
                q.fill(new Rectangle2D.Float(1.9F, -7, 3.6F, 14));
            }
            case "play" -> q.fill(poly(-4.5F, -7, 6.5F, 0, -4.5F, 7));
            case "fast" -> {
                q.fill(poly(-9, -6, 0, 0, -9, 6));
                q.fill(poly(0, -6, 9, 0, 0, 6));
            }
            case "fastest" -> {
                for (int i = 0; i < 3; i++) {
                    q.fill(poly(-10 + i * 7, -6, -3 + i * 7, 0, -10 + i * 7, 6));
                }
            }
            case "thermo" -> {
                q.draw(new RoundRectangle2D.Float(-2.2F, -9, 4.4F, 12, 4.4F, 4.4F));
                q.fill(circle(0, 5.5F, 3.8F));
                q.fill(new Rectangle2D.Float(-0.9F, -3, 1.8F, 7));
            }
            case "snow" -> {
                for (int i = 0; i < 3; i++) {
                    Graphics2D l = (Graphics2D) q.create();
                    l.rotate(i * Math.PI / 3);
                    l.draw(new Line2D.Float(0, -8.5F, 0, 8.5F));
                    l.draw(new Line2D.Float(-2.5F, -7.5F, 0, -5));
                    l.draw(new Line2D.Float(2.5F, -7.5F, 0, -5));
                    l.draw(new Line2D.Float(-2.5F, 7.5F, 0, 5));
                    l.draw(new Line2D.Float(2.5F, 7.5F, 0, 5));
                    l.dispose();
                }
            }
            case "sprout" -> {
                q.draw(new Line2D.Float(0, 9, 0, -1));
                Graphics2D l = (Graphics2D) q.create();
                l.translate(-4, -3);
                l.rotate(-0.7);
                l.fill(new Ellipse2D.Float(-2.5F, -5, 5, 10));
                l.dispose();
                l = (Graphics2D) q.create();
                l.translate(4, -4);
                l.rotate(0.7);
                l.fill(new Ellipse2D.Float(-2.5F, -5, 5, 10));
                l.dispose();
            }
            case "mountain" -> {
                q.fill(poly(-10, 8, -3, -6, 4, 8));
                q.setComposite(AlphaComposite.SrcOver.derive(0.75F));
                q.fill(poly(0, 8, 5, -1, 10, 8));
            }
            case "bowl" -> {
                q.fill(new Arc2D.Float(-9, -6, 18, 16, 180, 180, Arc2D.CHORD));
                q.draw(new Line2D.Float(-3, -8, -3, -3));
                q.draw(new Line2D.Float(2, -9, 2, -3));
            }
            case "brush" -> {
                q.rotate(Math.PI / 4);
                q.fill(new RoundRectangle2D.Float(-1.5F, -10, 3, 11, 2, 2));
                q.fill(poly(-4, 1, 4, 1, 3, 10, -3, 10));
            }
            case "spiral" -> {
                Path2D sp = new Path2D.Float();
                sp.moveTo(0, 0);
                for (double t = 0; t < 4 * Math.PI; t += 0.15) {
                    sp.lineTo(Math.cos(t) * t * 0.72, Math.sin(t) * t * 0.72);
                }
                q.draw(sp);
            }
            case "tree" -> {
                q.draw(circle(0, 0, 8.5F));
                q.draw(circle(0, 0, 5));
                q.fill(circle(0, 0, 1.8F));
                q.draw(new Line2D.Float(0, -8.5F, 0, -5));
                q.draw(new Line2D.Float(6, 6, 3.5F, 3.5F));
            }
            case "district" -> {
                Path2D hex = new Path2D.Float();
                for (int i = 0; i < 6; i++) {
                    double a = Math.PI / 6 + i * Math.PI / 3;
                    if (i == 0) {
                        hex.moveTo(Math.cos(a) * 8.5, Math.sin(a) * 8.5);
                    } else {
                        hex.lineTo(Math.cos(a) * 8.5, Math.sin(a) * 8.5);
                    }
                }
                hex.closePath();
                q.draw(hex);
                q.fill(circle(0, 0, 3.5F));
            }
            case "road" -> { // two edges closing toward the horizon, a dashed middle
                q.setStroke(new BasicStroke(2.2F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                q.draw(new Line2D.Float(-9, 9, -3, -9));
                q.draw(new Line2D.Float(9, 9, 3, -9));
                q.draw(new Line2D.Float(0, 8, 0, 4));
                q.draw(new Line2D.Float(0, 0, 0, -3));
                q.draw(new Line2D.Float(0, -6, 0, -8));
            }
            case "pick" -> {
                q.setStroke(new BasicStroke(2.4F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                q.draw(new Arc2D.Float(-9, -8, 18, 12, 20, 140, Arc2D.OPEN));
                q.draw(new Line2D.Float(0, -2, 0, 9));
            }
            case "hub" -> {
                q.draw(circle(0, 0, 8));
                q.fill(circle(0, 0, 3.5F));
            }
            case "ban" -> {
                q.setStroke(new BasicStroke(2.2F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                q.draw(circle(0, 0, 6.5F));
                q.draw(new Line2D.Float(-6, 6, 6, -6));
            }
            default -> q.fill(circle(0, 0, 4));
        }
        q.dispose();
    }

    private static Area gear() {
        Area a = new Area(circle(0, 0, 6.5F));
        for (int i = 0; i < 8; i++) {
            a.add(new Area(AffineTransform.getRotateInstance(i * Math.PI / 4).createTransformedShape(new Rectangle2D.Float(-1.8F, -9.2F, 3.6F, 4))));
        }
        a.subtract(new Area(circle(0, 0, 2.6F)));
        return a;
    }

    private static Path2D drop() {
        Path2D d = new Path2D.Float();
        d.moveTo(0, -9);
        d.curveTo(3, -4, 7, 0, 7, 3);
        d.curveTo(7, 7, 3.5, 9, 0, 9);
        d.curveTo(-3.5, 9, -7, 7, -7, 3);
        d.curveTo(-7, 0, -3, -4, 0, -9);
        d.closePath();
        return d;
    }
}
