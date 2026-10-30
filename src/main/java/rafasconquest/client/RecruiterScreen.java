package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.Talk;

/**
 * A conversation with a recruiter or a noble ({@link Talk}): what they say, and a button for each of the player's
 * choices. Choosing sends it to the server, which answers with what follows (or nothing, and the screen closes).
 */
public class RecruiterScreen extends Screen {
    private static final int WIDTH = 320;
    private static final int TEXT = 0xFFE8E0D0;
    private static final int GOLD = 0xFFE0C060;

    private final Talk.View view;

    public RecruiterScreen(Talk.View view) {
        super(Component.literal(view.speaker()));
        this.view = view;
    }

    @Override
    protected void init() {
        int x = width / 2 - WIDTH / 2;
        int y = height / 2 + 10;
        for (Talk.Option option : view.options()) {
            addRenderableWidget(Button.builder(Component.literal(option.label()), b -> {
                ConquestNetwork.choose(view.entity(), option.key());
                if (option.key().equals("bye")) {
                    onClose();
                }
            }).bounds(x, y, WIDTH, 20).build());
            y += 22;
        }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        int x = width / 2 - WIDTH / 2;
        int lines = 0;
        for (String line : view.lines()) {
            lines += font.split(Component.literal(line), WIDTH).size();
        }
        int y = height / 2 - 6 - lines * 11 - view.lines().size() * 4 - 18;
        g.drawCenteredString(font, view.speaker(), width / 2, y, GOLD);
        y += 18;
        for (String line : view.lines()) {
            for (FormattedCharSequence wrapped : font.split(Component.literal(line), WIDTH)) {
                g.drawString(font, wrapped, x, y, TEXT, false);
                y += 11;
            }
            y += 4;
        }
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
