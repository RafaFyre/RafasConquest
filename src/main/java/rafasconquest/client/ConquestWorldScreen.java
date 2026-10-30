package rafasconquest.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rafasconquest.worldgen.HorizontalStretch;
import rafasconquest.worldgen.HorizontalStretch.Choice;

import java.util.List;

/**
 * RafasConquest's options for a world being created, opened from the world creation screen: the horizontal stretch,
 * on or off, and how far (1 to 4 times wider each way; off by default if a mod it doesn't suit is installed), and the
 * province size (1: provinces 768 blocks apart, the map out to 16,000 blocks; 2: twice both). The choices are kept for
 * the world when it is made.
 */
public final class ConquestWorldScreen extends Screen {
    private final Screen parent;
    private boolean on;
    private int factor;
    private int provinceSize;
    private AbstractSliderButton slider;

    public ConquestWorldScreen(Screen parent) {
        super(Component.literal("RafasConquest"));
        this.parent = parent;
        Choice choice = HorizontalStretch.pending() != null ? HorizontalStretch.pending() : HorizontalStretch.defaultChoice();
        this.on = choice.on();
        this.factor = choice.factor();
        this.provinceSize = choice.provinceSize();
    }

    @Override
    protected void init() {
        int x = width / 2 - 100;
        int y = height / 2 - 30;
        addRenderableWidget(Button.builder(toggleLabel(), b -> {
            on = !on;
            b.setMessage(toggleLabel());
            slider.active = on;
            keep();
        }).bounds(x, y, 200, 20).build());
        int span = HorizontalStretch.MAX - HorizontalStretch.MIN;
        slider = new AbstractSliderButton(x, y + 26, 200, 20, Component.empty(), (double) (factor - HorizontalStretch.MIN) / span) {
            {
                updateMessage();
            }

            @Override
            protected void updateMessage() {
                setMessage(Component.literal("Horizontal stretch: " + factor + "×"));
            }

            @Override
            protected void applyValue() {
                factor = HorizontalStretch.MIN + (int) Math.round(value * span);
                value = (double) (factor - HorizontalStretch.MIN) / span; // whole steps only
                keep();
            }
        };
        slider.active = on;
        addRenderableWidget(slider);
        int sizes = HorizontalStretch.MAX_PROVINCE_SIZE - HorizontalStretch.MIN_PROVINCE_SIZE;
        addRenderableWidget(new AbstractSliderButton(x, y + 52, 200, 20, Component.empty(),
                (double) (provinceSize - HorizontalStretch.MIN_PROVINCE_SIZE) / sizes) {
            {
                updateMessage();
            }

            @Override
            protected void updateMessage() {
                setMessage(Component.literal("Province size: " + provinceSize));
            }

            @Override
            protected void applyValue() {
                provinceSize = HorizontalStretch.MIN_PROVINCE_SIZE + (int) Math.round(value * sizes);
                value = (double) (provinceSize - HorizontalStretch.MIN_PROVINCE_SIZE) / sizes; // whole steps only
                keep();
            }
        });
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose()).bounds(x, y + 86, 200, 20).build());
    }

    private Component toggleLabel() {
        return Component.literal("Horizontal stretch: " + (on ? "On" : "Off"));
    }

    private void keep() {
        HorizontalStretch.setPending(new Choice(on, factor, provinceSize));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        g.drawCenteredString(font, title, width / 2, height / 2 - 80, 0xFFFFFF);
        g.drawCenteredString(font, "Stretches biomes and the land (continents, mountains, coasts) wider each way.",
                width / 2, height / 2 - 62, 0xA0A0A0);
        List<String> clashing = HorizontalStretch.incompatibleInstalled();
        if (!clashing.isEmpty()) {
            g.drawCenteredString(font, Component.literal("Off by default: " + String.join(", ", clashing) + " installed")
                    .withStyle(ChatFormatting.GOLD), width / 2, height / 2 - 48, 0xFFFFFF);
        }
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        keep();
        minecraft.setScreen(parent);
    }
}
