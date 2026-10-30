package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.Boards;

/**
 * A quest board's notices: each quest, who offers it and where they are, its reward, and the player's part in it.
 * "Read" remembers it (its giver then offers it in conversation); a general quest is taken and reported here.
 */
public class BoardScreen extends Screen {
    private static final int WIDTH = 380;
    private static final int ROW = 34;
    private static final int TEXT = 0xFFE8E0D0;
    private static final int MUTED = 0xFFA89F90;
    private static final int GOLD = 0xFFE0C060;

    private final Boards.View view;

    public BoardScreen(Boards.View view) {
        super(Component.literal("Quest Board"));
        this.view = view;
    }

    @Override
    protected void init() {
        int x = width / 2 + WIDTH / 2 - 70;
        int y = top() + 22;
        for (Boards.Entry e : view.entries()) {
            String action = null;
            String label = null;
            if (e.general()) {
                if (e.state().equals("ready")) {
                    action = "report";
                    label = "Report";
                } else if (!e.state().equals("taken")) {
                    action = "take";
                    label = "Take";
                }
            } else if (e.state().equals("new")) {
                action = "read";
                label = "Read";
            }
            if (action != null) {
                String chosen = action;
                addRenderableWidget(Button.builder(Component.literal(label), b -> ConquestNetwork.boardAction(view.board(), e.id(), chosen))
                        .bounds(x, y + 4, 70, 20).build());
            }
            y += ROW;
        }
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose()).bounds(width / 2 - 50, y + 6, 100, 20).build());
    }

    private int top() {
        return Math.max(10, height / 2 - (view.entries().size() * ROW + 50) / 2);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        int x = width / 2 - WIDTH / 2;
        int y = top();
        g.drawCenteredString(font, "Quests of " + view.place(), width / 2, y, GOLD);
        y += 22;
        if (view.entries().isEmpty()) {
            g.drawCenteredString(font, "Nothing is posted today.", width / 2, y, MUTED);
        }
        for (Boards.Entry e : view.entries()) {
            g.drawString(font, e.title(), x, y, TEXT, false);
            String where = e.general() ? "Posted here" : e.giver() + " (" + e.giverX() + ", " + e.giverZ() + ")";
            String state = switch (e.state()) {
                case "read" -> "  · read: speak to them";
                case "taken" -> "  · taken";
                case "ready" -> e.general() ? "  · done: report here" : "  · done: report to them";
                default -> "";
            };
            g.drawString(font, where + " — " + e.reward() + state, x, y + 11, MUTED, false);
            y += ROW;
        }
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
