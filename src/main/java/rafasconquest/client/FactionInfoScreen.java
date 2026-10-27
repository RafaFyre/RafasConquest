package rafasconquest.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;
import rafasconquest.faction.FactionType;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.net.FactionInfo;
import rafasconquest.poi.PoiType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Faction Info: one faction at a time (◀ ▶ to switch, nearest first), with its banner, stats and every structure it
 * owns, in a scrolling box. Opened from the Faction Info button on Recruits' faction menu; the X returns there.
 */
public class FactionInfoScreen extends Screen {
    private static final int WIDTH = 260;
    private static final int HEIGHT = 224;
    private static final int HEADER = 52;
    private static final int LINE = 11;
    private static final int PAD = 8;
    private static final int SCROLLBAR = 4;

    private static final int PANEL = 0xFFC6C6C6;
    private static final int PANEL_EDGE = 0xFF373737;
    private static final int BOX = 0xFFE8E4D8;
    private static final int BOX_EDGE = 0xFF6B6B6B;
    private static final int TEXT = 0x404040;
    private static final int HEADING = 0x1F3A5F;
    private static final int MUTED = 0x707070;

    /** Display order and plural labels of the structure types. */
    private static final PoiType[] ORDER = {PoiType.CAPITAL, PoiType.CITY, PoiType.FARM};

    private final Screen parent;
    @Nullable
    private final UUID focus;
    @Nullable
    private List<FactionInfo> factions;
    private int index;
    private ItemStack banner = ItemStack.EMPTY;
    private final List<Line> lines = new ArrayList<>();
    private double scroll;
    private Button previous;
    private Button next;

    private record Line(Component text, int indent, int color) {
    }

    public FactionInfoScreen(Screen parent) {
        this(parent, null);
    }

    /** Opens on {@code focus} (or the nearest faction, if null), as the world map does for a double-clicked name. */
    public FactionInfoScreen(Screen parent, @Nullable UUID focus) {
        super(Component.translatable("gui.rafasconquest.faction_info"));
        this.parent = parent;
        this.focus = focus;
    }

    @Override
    protected void init() {
        int left = left();
        int top = top();
        addRenderableWidget(Button.builder(Component.literal("✕"), b -> onClose())
                .bounds(left + WIDTH - 16, top + 4, 12, 12).build());
        previous = addRenderableWidget(Button.builder(Component.literal("◀"), b -> select(index - 1))
                .bounds(left + 48, top + 10, 16, 16).build());
        next = addRenderableWidget(Button.builder(Component.literal("▶"), b -> select(index + 1))
                .bounds(left + WIDTH - 36, top + 10, 16, 16).build());
        if (factions == null) {
            ConquestNetwork.requestFactionInfo();
        }
        updateButtons();
    }

    /** The server's reply. */
    public void setFactions(List<FactionInfo> factions) {
        this.factions = factions;
        int start = 0;
        for (int i = 0; i < factions.size(); i++) {
            if (factions.get(i).id().equals(focus)) {
                start = i;
            }
        }
        select(start);
    }

    private void select(int i) {
        if (factions == null || factions.isEmpty()) {
            updateButtons();
            return;
        }
        index = Mth.clamp(i, 0, factions.size() - 1);
        FactionInfo faction = factions.get(index);
        banner = minecraft != null && minecraft.level != null
                ? FactionBannerNbt.item(minecraft.level.registryAccess(), FactionBanner.of(faction.id()))
                : ItemStack.EMPTY;
        buildLines(faction);
        scroll = 0;
        updateButtons();
    }

    private void updateButtons() {
        if (previous != null) {
            previous.active = factions != null && index > 0;
            next.active = factions != null && index < factions.size() - 1;
        }
    }

    private void buildLines(FactionInfo f) {
        lines.clear();
        boolean empire = f.type() == FactionType.EMPIRE;
        heading("gui.rafasconquest.faction_info.overview");
        line(0, TEXT, "gui.rafasconquest.faction_info.type", Component.translatable(empire
                ? "gui.rafasconquest.faction_type.empire" : "gui.rafasconquest.faction_type.city_state"));
        line(0, TEXT, "gui.rafasconquest.faction_info.strength", f.radiusSteps());
        if (!empire) {
            line(8, MUTED, "gui.rafasconquest.faction_info.no_claims");
        }
        line(0, TEXT, "gui.rafasconquest.faction_info.military", String.format("%,d", f.strength()));
        line(0, TEXT, empire ? "gui.rafasconquest.faction_info.capital" : "gui.rafasconquest.faction_info.home_city",
                coords(f.origin()));
        line(8, MUTED, "gui.rafasconquest.faction_info.distance", distance(f.origin()));
        line(0, TEXT, "gui.rafasconquest.faction_info.banner", FactionBanner.of(f.id()).describe());
        blank();

        heading("gui.rafasconquest.faction_info.structures", f.structureCount());
        for (PoiType type : ORDER) {
            line(8, TEXT, "gui.rafasconquest.structure." + key(type), f.structures().getOrDefault(type, List.of()).size());
        }
        for (PoiType type : ORDER) {
            List<BlockPos> positions = f.structures().getOrDefault(type, List.of());
            if (positions.isEmpty()) {
                continue;
            }
            blank();
            heading("gui.rafasconquest.structure." + key(type), positions.size());
            for (BlockPos pos : positions) {
                line(8, TEXT, "gui.rafasconquest.faction_info.location", coords(pos), distance(pos));
            }
        }
    }

    private static String key(PoiType type) {
        return type.name().toLowerCase(java.util.Locale.ROOT);
    }

    private void heading(String key, Object... args) {
        lines.add(new Line(Component.translatable(key, args).withStyle(ChatFormatting.BOLD), 0, HEADING));
    }

    private void line(int indent, int color, String key, Object... args) {
        lines.add(new Line(Component.translatable(key, args), indent, color));
    }

    private void blank() {
        lines.add(new Line(Component.empty(), 0, TEXT));
    }

    private static String coords(BlockPos pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    private String distance(BlockPos pos) {
        if (minecraft == null || minecraft.player == null) {
            return "?";
        }
        double dx = pos.getX() - minecraft.player.getX();
        double dz = pos.getZ() - minecraft.player.getZ();
        return String.format("%,d", Math.round(Math.sqrt(dx * dx + dz * dz)));
    }

    private int left() {
        return (width - WIDTH) / 2;
    }

    private int top() {
        return (height - HEIGHT) / 2;
    }

    // The scroll box.
    private int boxLeft() {
        return left() + PAD;
    }

    private int boxTop() {
        return top() + HEADER;
    }

    private int boxRight() {
        return left() + WIDTH - PAD;
    }

    private int boxBottom() {
        return top() + HEIGHT - PAD;
    }

    private double maxScroll() {
        int content = lines.size() * LINE + 2 * 4;
        return Math.max(0, content - (boxBottom() - boxTop()));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scroll = Mth.clamp(scroll - delta * LINE * 3, 0, maxScroll());
        return true;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        int left = left();
        int top = top();
        g.fill(left - 1, top - 1, left + WIDTH + 1, top + HEIGHT + 1, PANEL_EDGE);
        g.fill(left, top, left + WIDTH, top + HEIGHT, PANEL);

        // Header: banner, name, which faction of how many.
        if (!banner.isEmpty()) {
            g.pose().pushPose();
            g.pose().translate(left + 10, top + 8, 0);
            g.pose().scale(2.5F, 2.5F, 1);
            g.renderItem(banner, 0, 0);
            g.pose().popPose();
        }
        int centre = left + WIDTH / 2;
        if (factions == null) {
            g.drawCenteredString(font, Component.translatable("gui.rafasconquest.faction_info.loading"), centre, top + 14, 0xFFFFFF);
        } else if (factions.isEmpty()) {
            g.drawCenteredString(font, Component.translatable("gui.rafasconquest.faction_info.none"), centre, top + 14, 0xFFFFFF);
        } else {
            FactionInfo f = factions.get(index);
            Component name = Component.literal(f.name()).withStyle(ChatFormatting.BOLD);
            g.drawString(font, name, centre - font.width(name) / 2, top + 14, TEXT, false);
            Component count = Component.translatable("gui.rafasconquest.faction_info.page", index + 1, factions.size());
            g.drawString(font, count, centre - font.width(count) / 2, top + 30, MUTED, false);
        }

        // The scrolling box.
        int bl = boxLeft();
        int bt = boxTop();
        int br = boxRight();
        int bb = boxBottom();
        g.fill(bl - 1, bt - 1, br + 1, bb + 1, BOX_EDGE);
        g.fill(bl, bt, br, bb, BOX);
        g.enableScissor(bl, bt, br, bb);
        int y = bt + 4 - (int) Math.round(scroll);
        for (Line line : lines) {
            if (y + LINE > bt && y < bb) {
                g.drawString(font, line.text(), bl + 4 + line.indent(), y, line.color(), false);
            }
            y += LINE;
        }
        g.disableScissor();

        double max = maxScroll();
        if (max > 0) {
            int track = bb - bt;
            int thumb = Math.max(16, (int) (track * (double) track / (track + max)));
            int thumbTop = bt + (int) ((track - thumb) * (scroll / max));
            g.fill(br - SCROLLBAR, bt, br, bb, 0xFFB8B4A8);
            g.fill(br - SCROLLBAR, thumbTop, br, thumbTop + thumb, 0xFF6B6B6B);
        }

        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
