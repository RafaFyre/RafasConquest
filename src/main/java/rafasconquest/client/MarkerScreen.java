package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
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

    private static Component shown(String s) {
        return Component.literal(s.isEmpty() ? "(any)" : s.toLowerCase(Locale.ROOT).replace('_', ' '));
    }

    @Override
    protected void init() {
        int x = width / 2 - W / 2;
        int y = 30;
        addRenderableWidget(CycleButton.<String>builder(t -> Component.literal(MarkerSpec.title(t))).withValues(MarkerSpec.TYPES).withInitialValue(type)
                .create(x, y, W, 20, Component.literal("Type"), (b, v) -> {
                    keepText();
                    type = v;
                    rebuildWidgets();
                }));
        y += 24;
        MarkerSpec now = spec();
        if (now.soldiers()) {
            addRenderableWidget(CycleButton.<Integer>builder(k -> Component.literal(new String[]{"Levy", "Heavy infantry", "Elite", "Officer"}[k - 1]))
                    .withValues(1, 2, 3, 4).withInitialValue(Math.max(1, Math.min(4, kit))).create(x, y, W, 20, Component.literal("Kit"), (b, v) -> kit = v));
            y += 24;
            addRenderableWidget(CycleButton.booleanBuilder(Component.literal("Patrol"), Component.literal("Stand guard")).withInitialValue(patrol)
                    .create(x, y, W / 2 - 2, 20, Component.literal("Duty"), (b, v) -> patrol = v));
            addRenderableWidget(CycleButton.<Integer>builder(c -> Component.literal(String.valueOf(c))).withValues(1, 2, 3, 4, 5).withInitialValue(count)
                    .create(x + W / 2 + 2, y, W / 2 - 2, 20, Component.literal("Count"), (b, v) -> count = v));
            y += 24;
        }
        if (now.type().equals("NAMED")) {
            addRenderableWidget(CycleButton.<String>builder(MarkerScreen::shown).withValues(MarkerSpec.RANKS).withInitialValue(rank)
                    .create(x, y, W, 20, Component.literal("Rank in peerage"), (b, v) -> rank = v));
            y += 24;
            addRenderableWidget(CycleButton.<String>builder(MarkerScreen::shown).withValues(MarkerSpec.RELATIONS).withInitialValue(relation)
                    .create(x, y, W / 2 - 2, 20, Component.literal("Relation"), (b, v) -> relation = v));
            other = new EditBox(font, x + W / 2 + 2, y, W / 2 - 2, 20, Component.literal("Of whom"));
            other.setValue(otherText);
            other.setTooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal(
                    "place: this place's holder; nation: its ruler; or another marker's label")));
            addRenderableWidget(other);
            y += 24;
            addRenderableWidget(CycleButton.<String>builder(MarkerScreen::shown).withValues(withBlank(Personality.values()))
                    .withInitialValue(trait).create(x, y, W, 20, Component.literal("Trait"), (b, v) -> trait = v));
            y += 24;
            addRenderableWidget(CycleButton.<String>builder(MarkerScreen::shown).withValues(withBlank(Stat.values()))
                    .withInitialValue(stat).create(x, y, W / 2 - 2, 20, Component.literal("Stat"), (b, v) -> stat = v));
            statMin = new EditBox(font, x + W / 2 + 2, y, W / 2 - 2, 20, Component.literal("At least"));
            statMin.setValue(statMinText);
            statMin.setTooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("The least of the stat; -1: the highest")));
            addRenderableWidget(statMin);
            y += 24;
        }
        if (!now.soldiers() && !now.type().equals("RECRUITER")) {
            addRenderableWidget(CycleButton.onOffBuilder(twoPlaces).create(x, y, W / 2 - 2, 20, Component.literal("Two places at once"), (b, v) -> twoPlaces = v));
            addRenderableWidget(CycleButton.<String>builder(s -> Component.literal(s.equals("GUARD") ? "A guard" : "Nothing"))
                    .withValues("EMPTY", "GUARD").withInitialValue(ifNone)
                    .create(x + W / 2 + 2, y, W / 2 - 2, 20, Component.literal("If none"), (b, v) -> ifNone = v));
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
        renderBackground(g);
        g.drawCenteredString(font, title, width / 2, 12, GOLD);
        if (label != null) {
            g.drawString(font, "Label", width / 2 - W / 2, label.getY() + 6, LABEL, false);
            g.drawString(font, "Leaves", width / 2 - W / 2, replace.getY() + 6, LABEL, false);
        }
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
