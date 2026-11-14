package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import rafasconquest.battle.Machine;
import rafasconquest.content.ArtilleryBlock;
import rafasconquest.net.ConquestNetwork;

/** An Artillery Block's weapon: a dropdown of every Siegeworks engine that shoots; picking one sends it to the server. */
public class ArtilleryScreen extends Screen {
    private static final int W = 200;

    private final BlockPos pos;
    private final Machine weapon;
    private boolean open;

    public ArtilleryScreen(BlockPos pos, Machine weapon) {
        super(Component.literal("Artillery Block"));
        this.pos = pos;
        this.weapon = weapon;
    }

    private static Component name(Machine m) {
        return Component.translatable("entity." + Machine.MOD + "." + m.id);
    }

    @Override
    protected void init() {
        int x = width / 2 - W / 2;
        int y = 40;
        addRenderableWidget(Button.builder(Component.literal("Weapon: ").append(name(weapon)).append(open ? "  ▲" : "  ▼"), b -> {
            open = !open;
            rebuildWidgets();
        }).bounds(x, y, W, 20).build());
        if (!open) {
            return;
        }
        for (Machine m : ArtilleryBlock.WEAPON.getPossibleValues()) {
            y += 20;
            addRenderableWidget(Button.builder(name(m), b -> {
                ConquestNetwork.setArtillery(pos, m);
                onClose();
            }).bounds(x, y, W, 20).build());
        }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        g.drawCenteredString(font, title, width / 2, 20, 0xFFE0C060);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
