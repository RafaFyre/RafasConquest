package rafasconquest.client;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import rafasconquest.RafasConquest;

import java.util.ArrayDeque;
import java.util.List;

/**
 * What the player overhears ({@link rafasconquest.npc.Barks}): one line at a time just above the hotbar (over the
 * action bar's place), the speaker's name in gold, each held long enough to read and fading in and out.
 */
@EventBusSubscriber(modid = RafasConquest.MOD_ID, value = Dist.CLIENT)
public final class ClientBarks {
    private record Line(String name, String text, long millis) {
    }

    private static final ArrayDeque<Line> QUEUE = new ArrayDeque<>();
    private static long started;

    private ClientBarks() {
    }

    public static void add(List<String> names, List<String> lines) {
        if (QUEUE.size() > 4) {
            return; // still saying the last scene: this one goes unheard
        }
        for (int i = 0; i < lines.size(); i++) {
            // About the pace a reader takes it in, with a moment to notice it first.
            QUEUE.add(new Line(names.get(i), lines.get(i), 1500 + 55L * lines.get(i).length()));
        }
        if (QUEUE.size() == lines.size()) {
            started = Util.getMillis();
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Line line = QUEUE.peek();
        if (line == null) {
            return;
        }
        long age = Util.getMillis() - started;
        if (age > line.millis()) {
            QUEUE.poll();
            started = Util.getMillis();
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) {
            return;
        }
        GuiGraphics g = event.getGuiGraphics();
        Font font = mc.font;
        float alpha = Mth.clamp(Math.min(age, line.millis() - age) / 250f, 0.05f, 1);
        int a = (int) (alpha * 255) << 24;
        String name = line.name().isEmpty() ? "" : line.name() + ": ";
        int w = font.width(name) + font.width(line.text());
        int x = g.guiWidth() / 2 - w / 2;
        int y = g.guiHeight() - 59 - 24;
        g.fill(x - 4, y - 3, x + w + 4, y + 11, (int) (alpha * 0x90) << 24);
        g.drawString(font, name, x, y, a | 0xE0C060);
        g.drawString(font, line.text(), x + font.width(name), y, a | 0xF0EAD8);
    }
}
