package rafasconquest.client;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import rafasconquest.core.TitleMap;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Person;
import rafasconquest.people.Realm;

import java.util.ArrayList;
import java.util.List;

/**
 * Who owns the titles over a province, opened from the world map's right-click menu: the holder of its empire at the
 * top, then of its kingdom, duchy and county, and last whoever keeps the province's own hold. Someone holding several
 * of them in a row is shown once, by the highest, with the lesser titles listed under their rank. Each row has their
 * upper body on the left and their name and rank beside their head; a click on a character opens their page.
 */
public class TitleOwnersScreen extends Screen {
    private final Screen parent;
    private final BaseMap base;
    private final int province;
    private final List<Owner> owners = new ArrayList<>();

    /**
     * One row: a character ({@code person}, with their model), a player ({@code player}, styled, no model), or nobody
     * (both unset), and the titles they hold here, highest first.
     */
    private record Owner(int person, @Nullable String player, List<String> titles, @Nullable AbstractRecruitEntity entity) {
    }

    public TitleOwnersScreen(Screen parent, BaseMap base, int province) {
        super(Component.literal("Title owners: " + base.names().get(province)));
        this.parent = parent;
        this.base = base;
        this.province = province;
    }

    @Override
    protected void init() {
        Portraits.buttons(this, width, this::onClose, this::addRenderableWidget);
        owners.clear();
        int[] seats = base.titles().seatsOver(province);
        for (int i = 0; i < seats.length; i++) {
            add(seats[i], base.titleName(TitleMap.Tier.values()[seats.length - 1 - i], seats[i]));
        }
        if (ClientPeerage.holder(province) != null || base.realm().holderOf(province) >= 0) { // many provinces have no hold of their own
            String hold = base.realm().holdName(province);
            add(province, hold.isEmpty() ? base.names().get(province) : hold);
        }
    }

    /** Adds a title to the list: to the row above if the same owner holds it, else as a new row. */
    private void add(int seat, String title) {
        String player = ClientPeerage.holder(seat);
        int person = player != null ? -1 : base.realm().holderOf(seat);
        Owner last = owners.isEmpty() ? null : owners.get(owners.size() - 1);
        if (last != null && last.person() == person && java.util.Objects.equals(last.player(), player)) {
            if (!last.titles().contains(title)) {
                last.titles().add(title);
            }
            return;
        }
        AbstractRecruitEntity entity = person < 0 ? null : Portraits.model(minecraft, base, person);
        owners.add(new Owner(person, player, new ArrayList<>(List.of(title)), entity));
    }

    private int rowHeight() {
        return Math.min(72, (height - 40) / Math.max(1, owners.size()));
    }

    private int left() {
        return width / 2 - columnWidth() / 2;
    }

    private int columnWidth() {
        return Math.min(width - 40, 340);
    }

    @Override
    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, width, height, Portraits.BACKGROUND);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        g.drawCenteredString(font, title, width / 2, 12, Portraits.GOLD);
        int rowH = rowHeight();
        int side = rowH - 4;
        int x = left();
        int w = columnWidth();
        for (int i = 0; i < owners.size(); i++) {
            Owner o = owners.get(i);
            int y = 34 + i * rowH;
            Portraits.box(g, x, y, w, side);
            Portraits.box(g, x, y, side, side);
            if (o.entity() != null) {
                // Twice the box's height of them, hung from its top: the box cuts them off at the waist.
                Portraits.animate(o.entity());
                int scale = (int) (side / (0.55F * o.entity().getBbHeight()));
                g.enableScissor(x + 1, y + 1, x + side - 1, y + side - 1);
                InventoryScreen.renderEntityInInventoryFollowsMouse(g, x, y + 3, x + side, y + 3 + (int) (scale * o.entity().getBbHeight()),
                        scale, 0.0625F, mouseX, mouseY, o.entity());
                g.disableScissor();
            }
            int textX = x + side + 8;
            int textW = w - side - 14;
            String name = "No one";
            String rank = "Vacant";
            if (o.player() != null) {
                name = o.player();
                rank = "Player";
            } else if (o.person() >= 0) {
                Person p = base.realm().people().get(o.person());
                name = p.name();
                rank = Realm.title(p, base.titles().rank(p.hold().province()));
            }
            boolean hovered = o.person() >= 0 && mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + side;
            g.drawString(font, font.plainSubstrByWidth(name, textW), textX, y + 6, hovered ? 0xFFB8D8F8 : Portraits.TEXT, false);
            g.drawString(font, rank, textX, y + 18, Portraits.GOLD, false);
            int line = y + 30;
            for (String held : o.titles()) {
                if (line + 9 > y + side) {
                    break;
                }
                g.drawString(font, font.plainSubstrByWidth(held, textW), textX, line, Portraits.MUTED, false);
                line += 10;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        int rowH = rowHeight();
        int i = (int) Math.floor((mouseY - 34) / rowH);
        if (button == 0 && i >= 0 && i < owners.size() && owners.get(i).person() >= 0 && mouseY < 34 + i * rowH + rowH - 4
                && mouseX >= left() && mouseX < left() + columnWidth()) {
            minecraft.setScreen(new CharacterScreen(this, base, owners.get(i).person()));
            return true;
        }
        return false;
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
