package rafasconquest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.npc.Recruitment;

import java.util.ArrayList;
import java.util.List;

/**
 * Talking to a recruiter: what they say, and the choice to join their nation and enlist in its army ("Join and enlist")
 * or not ("Not now"). One who already serves the nation is only greeted.
 */
public class RecruiterScreen extends Screen {
    private static final int WIDTH = 300;
    private static final int TEXT = 0xFFE8E0D0;
    private static final int GOLD = 0xFFE0C060;

    private final Recruitment.Offer offer;

    public RecruiterScreen(Recruitment.Offer offer) {
        super(Component.literal(offer.recruiterName()));
        this.offer = offer;
    }

    private List<String> speech() {
        List<String> lines = new ArrayList<>();
        if (offer.member()) {
            lines.add("Well met, soldier of " + offer.nation() + ". Your place is with the army; we will call when we march.");
            return lines;
        }
        lines.add("Well met, traveller. " + offer.nation() + " has need of those who can hold a line.");
        if (offer.noble()) {
            lines.add("One of your standing would not serve in the ranks: you would join as a " + offer.startRank()
                    + ", and lead soldiers of your own.");
        } else {
            lines.add("Swear to " + offer.nation() + " and you join its army as a " + offer.startRank()
                    + ". Serve well and you will rise.");
        }
        lines.add("Will you join us?");
        return lines;
    }

    @Override
    protected void init() {
        int x = width / 2 - WIDTH / 2;
        int y = height / 2 + 30;
        if (offer.member()) {
            addRenderableWidget(Button.builder(Component.literal("Farewell"), b -> onClose()).bounds(x + WIDTH / 2 - 50, y, 100, 20).build());
            return;
        }
        addRenderableWidget(Button.builder(Component.literal("Join and enlist"), b -> {
            ConquestNetwork.acceptRecruiter(offer.recruiter());
            onClose();
        }).bounds(x + 20, y, 120, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Not now"), b -> onClose()).bounds(x + WIDTH - 140, y, 120, 20).build());
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        int x = width / 2 - WIDTH / 2;
        int y = height / 2 - 60;
        g.drawCenteredString(font, offer.recruiterName(), width / 2, y, GOLD);
        y += 18;
        for (String line : speech()) {
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
