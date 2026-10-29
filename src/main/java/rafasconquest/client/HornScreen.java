package rafasconquest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.net.ConquestNetwork.UnitInfo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * The war horn's screen, opened by right-clicking the war horn: a map of the field around the player (north up), with
 * their units (one per block they were given) and every formation of the battle. Click a unit to select it (shift
 * adds to the selection), click the map to send the selected units there, or give them an order with the buttons.
 * Orders wait until the screen is closed; then, if there were any, the horn blows and they are carried out. Client
 * thread only.
 */
public final class HornScreen extends Screen {
    private static final String[] MODES = {"following", "holding", "moving", "charging", "your orders"};
    /** Map pixels per block. */
    private static final double SCALE = 2;
    private static final int UNIT_SIZE = 10;
    private static final int MARGIN = 8;
    private static final int BUTTONS_HEIGHT = 28;

    private static List<UnitInfo> units = List.of();

    private final Set<Integer> selected = new LinkedHashSet<>();
    private final List<int[]> orderUnits = new ArrayList<>();
    private final List<Integer> orders = new ArrayList<>();
    private final List<double[]> places = new ArrayList<>();
    private final List<String> said = new ArrayList<>();
    private int ticks;

    public HornScreen() {
        super(Component.literal("War Horn"));
    }

    /** The server's answer: the player's units now. */
    public static void receive(List<UnitInfo> latest) {
        units = latest;
    }

    public static void open() {
        units = List.of();
        ConquestNetwork.requestUnits();
        Minecraft.getInstance().setScreen(new HornScreen());
    }

    @Override
    protected void init() {
        String[] labels = {"Follow me", "Hold", "Charge", "Square", "Line", "Shields up", "Shields down"};
        int[] codes = {0, 1, 3, 4, 5, 6, 7}; // RecruitsBattle.UnitOrder: FOLLOW, HOLD, MOVE, CHARGE, SQUARE, LINE, SHIELDS_UP, SHIELDS_DOWN
        int w = 74;
        int total = (labels.length + 1) * (w + 4);
        int x = (width - total) / 2;
        int y = height - BUTTONS_HEIGHT + 4;
        addRenderableWidget(Button.builder(Component.literal("All units"), b -> {
            units.forEach(u -> selected.add(u.number()));
        }).bounds(x, y, w, 20).build());
        x += w + 4;
        for (int i = 0; i < labels.length; i++) {
            int code = codes[i];
            String label = labels[i];
            addRenderableWidget(Button.builder(Component.literal(label), b -> give(code, 0, 0, label)).bounds(x, y, w, 20).build());
            x += w + 4;
        }
    }

    private void give(int order, double x, double z, String label) {
        if (selected.isEmpty()) {
            return;
        }
        orderUnits.add(selected.stream().mapToInt(Integer::intValue).toArray());
        orders.add(order);
        places.add(new double[]{x, z});
        said.add(label + ": " + selected.stream().map(String::valueOf).reduce((a, b) -> a + ", " + b).orElse(""));
    }

    @Override
    public void tick() {
        if (++ticks % 20 == 0) {
            ConquestNetwork.requestUnits();
        }
    }

    private int mapLeft() {
        return MARGIN;
    }

    private int mapTop() {
        return MARGIN + 12;
    }

    private int mapRight() {
        return width - MARGIN;
    }

    private int mapBottom() {
        return height - BUTTONS_HEIGHT - MARGIN;
    }

    private double centreX() {
        return (mapLeft() + mapRight()) / 2.0;
    }

    private double centreY() {
        return (mapTop() + mapBottom()) / 2.0;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        double px = mc.player.getX();
        double pz = mc.player.getZ();
        g.fill(mapLeft(), mapTop(), mapRight(), mapBottom(), 0xC0101418);
        g.enableScissor(mapLeft(), mapTop(), mapRight(), mapBottom());
        // Every formation of the battle, in its uniform.
        for (FormationSnapshot f : DistantArmies.now()) {
            int sx = (int) (centreX() + (f.x() - px) * SCALE);
            int sy = (int) (centreY() + (f.z() - pz) * SCALE);
            int r = (int) Math.max(1, Math.max(f.halfWidth(), f.halfDepth()) * SCALE);
            g.fill(sx - r, sy - r, sx + r, sy + r, 0x90000000 | f.rgb() & 0xFFFFFF);
        }
        // The player, and which way they look.
        int cx = (int) centreX();
        int cy = (int) centreY();
        g.fill(cx - 2, cy - 2, cx + 3, cy + 3, 0xFFFFFFFF);
        double yaw = Math.toRadians(mc.player.getYRot());
        g.fill(cx + (int) (-Math.sin(yaw) * 6) - 1, cy + (int) (Math.cos(yaw) * 6) - 1, cx + (int) (-Math.sin(yaw) * 6) + 1,
                cy + (int) (Math.cos(yaw) * 6) + 1, 0xFFFFFFFF);
        // The player's units.
        for (UnitInfo u : units) {
            int sx = (int) (centreX() + (u.x() - px) * SCALE);
            int sy = (int) (centreY() + (u.z() - pz) * SCALE);
            int half = UNIT_SIZE / 2;
            boolean on = selected.contains(u.number());
            g.fill(sx - half - 1, sy - half - 1, sx + half + 1, sy + half + 1, on ? 0xFFFFD700 : 0xFF000000);
            g.fill(sx - half, sy - half, sx + half, sy + half, 0xFF000000 | u.rgb() & 0xFFFFFF);
            g.drawString(font, String.valueOf(u.number()), sx - 3, sy - 4, 0xFFFFFFFF);
        }
        // Moves given but not yet carried out.
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i) == 2) {
                int sx = (int) (centreX() + (places.get(i)[0] - px) * SCALE);
                int sy = (int) (centreY() + (places.get(i)[1] - pz) * SCALE);
                g.fill(sx - 2, sy - 2, sx + 2, sy + 2, 0xFFFFD700);
            }
        }
        g.disableScissor();
        g.drawString(font, "War Horn: click a unit to select it (shift adds), click the map to send them there", MARGIN, MARGIN, 0xFFFFFFFF);
        int y = mapTop() + 4;
        for (UnitInfo u : units) {
            String line = String.format(Locale.ROOT, "%s%d: %d soldiers, %s, %s%s", selected.contains(u.number()) ? "> " : "  ",
                    u.number(), u.soldiers(), MODES[Math.min(u.mode(), MODES.length - 1)], u.shape() == 0 ? "square" : "line",
                    u.shields() ? ", shields up" : "");
            g.drawString(font, line, mapLeft() + 4, y, 0xFFE0E0E0);
            y += 10;
        }
        y += 6;
        for (String s : said) {
            g.drawString(font, "Order: " + s, mapLeft() + 4, y, 0xFFFFD700);
            y += 10;
        }
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (mouseX < mapLeft() || mouseX > mapRight() || mouseY < mapTop() || mouseY > mapBottom()) {
            return false;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return false;
        }
        double px = mc.player.getX();
        double pz = mc.player.getZ();
        for (UnitInfo u : units) {
            double sx = centreX() + (u.x() - px) * SCALE;
            double sy = centreY() + (u.z() - pz) * SCALE;
            if (Math.abs(mouseX - sx) <= UNIT_SIZE && Math.abs(mouseY - sy) <= UNIT_SIZE) {
                if (!hasShiftDown()) {
                    selected.clear();
                }
                selected.add(u.number());
                return true;
            }
        }
        double x = px + (mouseX - centreX()) / SCALE;
        double z = pz + (mouseY - centreY()) / SCALE;
        give(2, x, z, String.format(Locale.ROOT, "Move to %d, %d", Math.round(x), Math.round(z)));
        return true;
    }

    @Override
    public void removed() {
        if (!orders.isEmpty()) {
            int[] codes = orders.stream().mapToInt(Integer::intValue).toArray();
            double[] xs = places.stream().mapToDouble(p -> p[0]).toArray();
            double[] zs = places.stream().mapToDouble(p -> p[1]).toArray();
            ConquestNetwork.sendHornOrders(new ConquestNetwork.HornOrders(new ArrayList<>(orderUnits), codes, xs, zs));
        }
        super.removed();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
