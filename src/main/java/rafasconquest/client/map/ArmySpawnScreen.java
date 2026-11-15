package rafasconquest.client.map;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.GrandStrategy;
import rafasconquest.people.Person;
import rafasconquest.people.Stat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The Army Spawn Menu, from the grand strategy map's creative right-click: the army's nation and general (dropdowns),
 * its size in soldiers, whether to fill it with random cards, and Spawn, which makes it where the map was clicked.
 */
final class ArmySpawnScreen extends Screen implements GrandStrategyScreen.OverMap {
    private static final int W = 220;
    private static final int H = 150;
    private static final int ROWS = 8;
    private static final int ROW = 12;
    private static final int GOLD = 0xFFE0C060;
    private static final String BEST = "(best at war)";

    private final GrandStrategyScreen parent;
    private final double x;
    private final double z;
    private int nation;
    /** The chosen general's name; "" the nation's best at war. */
    private String general = "";
    private String soldiers = "2000";
    private boolean autofill = true;
    private EditBox size;
    /** The open dropdown's button and names (null: none), what picking a row does, and its first row in view. */
    private Button openButton;
    private List<String> openNames;
    private java.util.function.IntConsumer openPick;
    private int scroll;

    ArmySpawnScreen(GrandStrategyScreen parent, double x, double z, int nation) {
        super(Component.literal("Spawn Army"));
        this.parent = parent;
        this.x = x;
        this.z = z;
        this.nation = Math.max(0, nation);
    }

    @Override
    public GrandStrategyScreen map() {
        return parent;
    }

    /** The nation's grown characters, best at war first, after the "best at war" choice. */
    private List<String> generals(BaseMap base) {
        List<String> names = new ArrayList<>(List.of(BEST));
        base.realm().people().stream().filter(p -> p.nation() == nation && p.age() >= 16)
                .sorted(Comparator.comparingInt((Person p) -> -p.stat(Stat.MARTIAL))).map(Person::name).forEach(names::add);
        return names;
    }

    private void drop(int bx, int by, String caption, List<String> names, String current, java.util.function.IntConsumer pick) {
        Button[] self = new Button[1];
        self[0] = addRenderableWidget(Button.builder(Component.literal(caption + ": " + current + "  ▼"), b -> {
            boolean wasOpen = openButton == self[0];
            openButton = wasOpen ? null : self[0];
            openNames = names;
            openPick = pick;
            scroll = Math.max(0, Math.min(names.size() - Math.min(ROWS, names.size()), names.indexOf(current) - ROWS / 2));
        }).bounds(bx, by, W - 20, 20).build());
    }

    @Override
    protected void init() {
        BaseMap base = ClientMapCache.map();
        openButton = null;
        if (base == null || base.nations().isEmpty()) {
            return;
        }
        nation = Math.min(nation, base.nations().size() - 1);
        int left = (width - W) / 2 + 10;
        int top = (height - H) / 2 + 20;
        List<String> nations = base.nations().stream().map(n -> n.name()).toList();
        drop(left, top, "Nation", nations, nations.get(nation), i -> {
            nation = i;
            general = "";
        });
        drop(left, top + 24, "General", generals(base), general.isEmpty() ? BEST : general, i -> general = i == 0 ? "" : generals(base).get(i));
        size = new EditBox(font, left + 50, top + 48, 70, 20, Component.literal("Soldiers"));
        size.setFilter(s -> s.matches("\\d{0,6}"));
        size.setValue(soldiers);
        size.setResponder(s -> soldiers = s);
        addRenderableWidget(size);
        addRenderableWidget(Checkbox.builder(Component.literal("Autofill"), font).pos(left + 128, top + 48).selected(autofill)
                .onValueChange((c, on) -> autofill = on).build());
        addRenderableWidget(Button.builder(Component.literal("Spawn"), b -> spawn()).bounds(width / 2 - 40, top + 80, 80, 20).build());
    }

    private void spawn() {
        int men;
        try {
            men = Math.min(GrandStrategy.MAX_SOLDIERS, Integer.parseInt(soldiers));
        } catch (NumberFormatException e) {
            return; // nothing typed: no army
        }
        ConquestNetwork.spawnArmy(nation, general, men, autofill, x, z);
        parent.pickNextSpawned();
        minecraft.setScreen(parent);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        parent.renderUnder(g, width, height, partialTick);
        int left = (width - W) / 2;
        int top = (height - H) / 2;
        g.fill(left, top, left + W, top + H, 0xF0201D18);
        g.renderOutline(left, top, W, H, 0xFF8C6A2F);
        g.drawCenteredString(font, title, width / 2, top + 6, GOLD);
        g.drawString(font, "Soldiers", left + 10, top + 74, 0xFFF4EEDC, false);
        for (var widget : renderables) { // the widgets only: Screen.render would paint the background over the panel
            widget.render(g, mouseX, mouseY, partialTick);
        }
        if (openButton == null) {
            return;
        }
        int lx = openButton.getX(), w = openButton.getWidth(), shown = shownRows(), ly = openButton.getBottom();
        g.pose().pushPose();
        g.pose().translate(0, 0, 400);
        g.fill(lx, ly, lx + w, ly + shown * ROW + 2, 0xF0101010);
        g.renderOutline(lx, ly, w, shown * ROW + 2, GOLD);
        int hovered = row(mouseX, mouseY);
        for (int i = 0; i < shown; i++) {
            int index = scroll + i;
            int rowY = ly + 1 + i * ROW;
            if (index == hovered) {
                g.fill(lx + 1, rowY, lx + w - 1, rowY + ROW, 0xFF404040);
            }
            g.drawString(font, openNames.get(index), lx + 5, rowY + 2, 0xFFFFFFFF, false);
        }
        g.pose().popPose();
    }

    private int shownRows() {
        return Math.min(ROWS, openNames.size());
    }

    /** The row under the mouse in the open dropdown, or -1. */
    private int row(double mouseX, double mouseY) {
        int lx = openButton.getX(), ly = openButton.getBottom() + 1;
        int row = (int) Math.floor((mouseY - ly) / ROW);
        if (mouseX < lx || mouseX >= lx + openButton.getWidth() || row < 0 || row >= shownRows()) {
            return -1;
        }
        return scroll + row;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (openButton == null || openButton.isMouseOver(mouseX, mouseY)) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        int index = row(mouseX, mouseY);
        java.util.function.IntConsumer pick = openPick;
        openButton = null;
        if (index >= 0) {
            pick.accept(index);
            rebuildWidgets();
        }
        return true; // a click off the open list only shuts it
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        if (openButton != null) {
            scroll = Math.max(0, Math.min(openNames.size() - shownRows(), scroll - (int) Math.signum(delta)));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, delta);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
