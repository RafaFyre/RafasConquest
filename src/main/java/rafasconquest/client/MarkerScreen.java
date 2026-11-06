package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.people.Personality;
import rafasconquest.people.Stat;
import rafasconquest.style.MarkerSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

/**
 * A Character Marker's settings: what it becomes (a dropdown of every type), and the settings of that type: soldiers'
 * kit, duty and number; a named character's filters, each of which may be left blank; and for any, its label and the
 * block it leaves behind. Done sends them to the server.
 */
public class MarkerScreen extends Screen {
    private static final int W = 300;
    private static final int GOLD = 0xFFE0C060;
    private static final int LABEL = 0xFFB8A888;

    private final BlockPos pos;
    private String type;
    private int kit;
    private boolean patrol;
    private int count;
    private String rank;
    private String relation;
    private String trait;
    private String stat;
    private boolean twoPlaces;
    private String ifNone;
    private EditBox label;
    private EditBox other;
    private EditBox statMin;
    private EditBox replace;
    private String labelText;
    private String otherText;
    private String statMinText;
    private String replaceText;
    /** The dropdown that is open (null: none), and the first of its rows in view. */
    private Drop open;
    private int scroll;
    private static final int ROWS = 8;
    private static final int ROW = 12;

    public MarkerScreen(BlockPos pos, MarkerSpec spec) {
        super(Component.literal("Character Marker"));
        this.pos = pos;
        type = spec.type();
        kit = spec.kit();
        patrol = spec.patrol();
        count = spec.count();
        rank = spec.rank();
        relation = spec.relation();
        trait = spec.trait();
        stat = spec.stat();
        twoPlaces = spec.twoPlaces();
        ifNone = spec.ifNone();
        labelText = spec.label();
        otherText = spec.other();
        statMinText = String.valueOf(spec.statMin());
        replaceText = spec.replace();
    }

    private static List<String> withBlank(Enum<?>[] values) {
        List<String> list = new ArrayList<>(List.of(""));
        Arrays.stream(values).map(Enum::name).forEach(list::add);
        return list;
    }

    private static String shown(String s) {
        return s.isEmpty() ? "(any)" : s.toLowerCase(Locale.ROOT).replace('_', ' ');
    }

    /** One dropdown: its button, the names it lists, which is chosen, and what picking a row does. */
    private record Drop(Button button, List<String> names, int current, IntConsumer pick) {
    }

    /** Adds a dropdown button; clicking it opens a scrolling list of the values. */
    private <T> void drop(int x, int y, int w, String caption, List<T> values, Function<T, String> name, T current, Consumer<T> set) {
        List<String> names = values.stream().map(name).toList();
        int at = Math.max(0, values.indexOf(current));
        Drop[] self = new Drop[1];
        Button button = addRenderableWidget(Button.builder(Component.literal(caption + ": " + names.get(at) + "  ▼"), b -> {
            open = open == self[0] ? null : self[0];
            scroll = Math.max(0, Math.min(names.size() - Math.min(ROWS, names.size()), at - ROWS / 2));
        }).bounds(x, y, w, 20).build());
        self[0] = new Drop(button, names, at, i -> set.accept(values.get(i)));
    }

    @Override
    protected void init() {
        open = null;
        int x = width / 2 - W / 2;
        int y = 30;
        drop(x, y, W, "Type", MarkerSpec.TYPES, MarkerSpec::title, type, v -> type = v);
        y += 24;
        MarkerSpec now = spec();
        if (now.soldiers()) {
            String[] kits = {"Levy", "Heavy infantry", "Elite", "Officer"};
            drop(x, y, W, "Kit", List.of(1, 2, 3, 4), k -> kits[k - 1], Math.max(1, Math.min(4, kit)), v -> kit = v);
            y += 24;
            drop(x, y, W / 2 - 2, "Duty", List.of(false, true), p -> p ? "Patrol" : "Stand guard", patrol, v -> patrol = v);
            drop(x + W / 2 + 2, y, W / 2 - 2, "Count", List.of(1, 2, 3, 4, 5), String::valueOf, count, v -> count = v);
            y += 24;
        }
        if (now.type().equals("NAMED")) {
            drop(x, y, W, "Rank in peerage", MarkerSpec.RANKS, MarkerScreen::shown, rank, v -> rank = v);
            y += 24;
            drop(x, y, W / 2 - 2, "Relation", MarkerSpec.RELATIONS, MarkerScreen::shown, relation, v -> relation = v);
            other = new EditBox(font, x + W / 2 + 2, y, W / 2 - 2, 20, Component.literal("Of whom"));
            other.setValue(otherText);
            other.setTooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal(
                    "place: this place's holder; nation: its ruler; or another marker's label")));
            addRenderableWidget(other);
            y += 24;
            drop(x, y, W, "Trait", withBlank(Personality.values()), MarkerScreen::shown, trait, v -> trait = v);
            y += 24;
            drop(x, y, W / 2 - 2, "Stat", withBlank(Stat.values()), MarkerScreen::shown, stat, v -> stat = v);
            statMin = new EditBox(font, x + W / 2 + 2, y, W / 2 - 2, 20, Component.literal("At least"));
            statMin.setValue(statMinText);
            statMin.setTooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("The least of the stat; -1: the highest")));
            addRenderableWidget(statMin);
            y += 24;
        }
        if (!now.soldiers() && !now.type().equals("RECRUITER")) {
            drop(x, y, W / 2 - 2, "Two places", List.of(false, true), t -> t ? "ON" : "OFF", twoPlaces, v -> twoPlaces = v);
            drop(x + W / 2 + 2, y, W / 2 - 2, "If none", List.of("EMPTY", "GUARD"), s -> s.equals("GUARD") ? "A guard" : "Nothing", ifNone, v -> ifNone = v);
            y += 24;
        }
        label = new EditBox(font, x + 70, y, W - 70, 20, Component.literal("Label"));
        label.setValue(labelText);
        addRenderableWidget(label);
        y += 24;
        replace = new EditBox(font, x + 70, y, W - 70, 20, Component.literal("Leaves"));
        replace.setValue(replaceText);
        replace.setTooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal(
                "The block it becomes once it fires, e.g. minecraft:red_carpet (empty: air)")));
        addRenderableWidget(replace);
        y += 30;
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> {
            keepText();
            ConquestNetwork.setMarker(pos, spec());
            onClose();
        }).bounds(x, y, W / 2 - 2, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Cancel"), b -> onClose()).bounds(x + W / 2 + 2, y, W / 2 - 2, 20).build());
    }

    /** What is typed, kept while the fields are rebuilt for another type. */
    private void keepText() {
        labelText = label == null ? labelText : label.getValue();
        otherText = other == null || !other.isActive() ? otherText : other.getValue();
        statMinText = statMin == null || !statMin.isActive() ? statMinText : statMin.getValue();
        replaceText = replace == null ? replaceText : replace.getValue();
    }

    private MarkerSpec spec() {
        int min;
        try {
            min = Integer.parseInt(statMinText.trim());
        } catch (NumberFormatException e) {
            min = -1;
        }
        return new MarkerSpec(type, kit, patrol, count, labelText.trim(), rank, relation, otherText.trim().isEmpty() ? "place" : otherText.trim(),
                trait, stat, min, twoPlaces, ifNone, replaceText.trim());
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g, mouseX, mouseY, partialTick);
        g.drawCenteredString(font, title, width / 2, 12, GOLD);
        if (label != null) {
            g.drawString(font, "Label", width / 2 - W / 2, label.getY() + 6, LABEL, false);
            g.drawString(font, "Leaves", width / 2 - W / 2, replace.getY() + 6, LABEL, false);
        }
        for (net.minecraft.client.gui.components.Renderable widget : renderables) { // the widgets only: 1.21 Screen.render would paint the background over what is drawn above
            widget.render(g, mouseX, mouseY, partialTick);
        }
        if (open != null) {
            Button b = open.button();
            int lx = b.getX(), w = b.getWidth(), shown = shownRows(), ly = listTop();
            g.pose().pushPose();
            g.pose().translate(0, 0, 400); // over the widgets and their text
            g.fill(lx, ly, lx + w, ly + shown * ROW + 2, 0xF0101010);
            g.renderOutline(lx, ly, w, shown * ROW + 2, GOLD);
            int hovered = row(mouseX, mouseY);
            for (int i = 0; i < shown; i++) {
                int index = scroll + i;
                int rowY = ly + 1 + i * ROW;
                if (index == hovered) {
                    g.fill(lx + 1, rowY, lx + w - 1, rowY + ROW, 0xFF404040);
                }
                g.drawString(font, open.names().get(index), lx + 5, rowY + 2, index == open.current() ? GOLD : 0xFFFFFFFF, false);
            }
            int size = open.names().size();
            if (size > ROWS) { // scrollbar
                int track = shown * ROW;
                int thumb = Math.max(8, track * ROWS / size);
                int thumbY = ly + 1 + (track - thumb) * scroll / (size - ROWS);
                g.fill(lx + w - 4, thumbY, lx + w - 1, thumbY + thumb, 0xFFA0A0A0);
            }
            g.pose().popPose();
        }
    }

    private int shownRows() {
        return Math.min(ROWS, open.names().size());
    }

    /** The open list hangs below its button, or above it when there is no room below. */
    private int listTop() {
        int h = shownRows() * ROW + 2;
        return open.button().getBottom() + h <= height ? open.button().getBottom() : open.button().getY() - h;
    }

    /** The row under the mouse in the open dropdown, or -1. */
    private int row(double mouseX, double mouseY) {
        int lx = open.button().getX(), ly = listTop() + 1;
        int row = (int) Math.floor((mouseY - ly) / ROW);
        if (mouseX < lx || mouseX >= lx + open.button().getWidth() || row < 0 || row >= shownRows()) {
            return -1;
        }
        return scroll + row;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (open == null || open.button().isMouseOver(mouseX, mouseY)) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        int index = row(mouseX, mouseY);
        Drop picked = open;
        open = null;
        if (index >= 0) {
            keepText();
            picked.pick().accept(index);
            rebuildWidgets();
        }
        return true; // a click off the open list only shuts it
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        if (open != null) {
            scroll = Math.max(0, Math.min(open.names().size() - shownRows(), scroll - (int) Math.signum(delta)));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
