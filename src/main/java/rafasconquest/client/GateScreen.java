package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import rafasconquest.net.ConquestNetwork;

/**
 * A Gate Block's doors: Left Door and Right Door pick which door's box is edited (its two corners, relative to the Gate
 * Block; all six blank for no door), Save sends both to the server, which makes the doors again from them. The boxes
 * are outlined in the world while near the block ({@link GateRenderer}).
 */
public class GateScreen extends Screen {
    private static final int W = 240;
    private static final int LABEL = 0xFFB8A888;
    private static final String[] AXES = {"x", "y", "z"};

    private final BlockPos pos;
    /** Each door's six fields as typed: {left, right}. */
    private final String[][] text = new String[2][6];
    private int editing;
    private final EditBox[] fields = new EditBox[6];
    private Button leftButton;
    private Button rightButton;

    public GateScreen(BlockPos pos, @Nullable int[] left, @Nullable int[] right) {
        super(Component.literal("Gate Block"));
        this.pos = pos;
        int[][] boxes = {left, right};
        for (int d = 0; d < 2; d++) {
            for (int i = 0; i < 6; i++) {
                text[d][i] = boxes[d] == null ? "" : String.valueOf(boxes[d][i]);
            }
        }
    }

    @Override
    protected void init() {
        int x = width / 2 - W / 2;
        int y = 40;
        leftButton = addRenderableWidget(Button.builder(Component.literal("Left Door"), b -> pick(0)).bounds(x, y, W / 2 - 2, 20).build());
        rightButton = addRenderableWidget(Button.builder(Component.literal("Right Door"), b -> pick(1)).bounds(x + W / 2 + 2, y, W / 2 - 2, 20).build());
        y += 40;
        for (int i = 0; i < 6; i++) {
            int col = i % 3;
            int row = i / 3;
            EditBox box = new EditBox(font, x + 40 + col * 68, y + row * 28, 60, 18, Component.literal(AXES[col]));
            box.setFilter(s -> s.matches("-?\\d{0,3}"));
            int k = i;
            box.setResponder(s -> text[editing][k] = s);
            fields[i] = addRenderableWidget(box);
        }
        y += 64;
        addRenderableWidget(Button.builder(Component.literal("Save"), b -> save()).bounds(x, y, W, 20).build());
        pick(editing);
    }

    private void pick(int door) {
        editing = door;
        for (int i = 0; i < 6; i++) {
            fields[i].setValue(text[door][i]);
        }
        leftButton.active = door != 0;
        rightButton.active = door != 1;
    }

    /** A door's box from its fields; null if all are blank or any is not a number. */
    @Nullable
    private int[] box(int door) {
        int[] box = new int[6];
        for (int i = 0; i < 6; i++) {
            try {
                box[i] = Integer.parseInt(text[door][i]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        for (int a = 0; a < 3; a++) {
            if (box[a] > box[a + 3]) {
                int t = box[a];
                box[a] = box[a + 3];
                box[a + 3] = t;
            }
        }
        return box;
    }

    private void save() {
        ConquestNetwork.setGate(pos, box(0), box(1));
        onClose();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        int x = width / 2 - W / 2;
        g.drawCenteredString(font, title, width / 2, 16, 0xFFFFFFFF);
        g.drawString(font, (editing == 0 ? "Left" : "Right") + " door, from the Gate Block (x y z):", x, 68, LABEL);
        g.drawString(font, "From", x, 85, LABEL);
        g.drawString(font, "To", x, 113, LABEL);
        g.drawString(font, "Blank: no door. Outlined in the world: left green, right blue.", x, 172, LABEL);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
