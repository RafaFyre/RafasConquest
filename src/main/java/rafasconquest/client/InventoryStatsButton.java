package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import rafasconquest.net.ConquestNetwork;

/**
 * The inventory's button for the player's stats: a book, at the right above the crafting grid (over its output slot).
 * It follows the inventory when the recipe book pushes it aside.
 */
final class InventoryStatsButton extends Button {
    private static final int SIZE = 18;
    /** Where it sits within the inventory's panel. */
    private static final int AT_X = 153;
    private static final int AT_Y = 5;
    private static final ItemStack ICON = new ItemStack(Items.WRITABLE_BOOK);

    private final InventoryScreen inventory;

    InventoryStatsButton(InventoryScreen inventory) {
        super(inventory.getGuiLeft() + AT_X, inventory.getGuiTop() + AT_Y, SIZE, SIZE, Component.literal("Stats"),
                b -> ConquestNetwork.requestStats(), DEFAULT_NARRATION);
        this.inventory = inventory;
        setTooltip(Tooltip.create(Component.literal("Your stats")));
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        setX(inventory.getGuiLeft() + AT_X);
        setY(inventory.getGuiTop() + AT_Y);
        // The button's frame without its label, then the book.
        Component label = getMessage();
        setMessage(Component.empty());
        super.renderWidget(g, mouseX, mouseY, partialTick);
        setMessage(label);
        g.renderItem(ICON, getX() + 1, getY() + 1);
    }
}
