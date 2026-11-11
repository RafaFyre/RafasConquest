package rafasconquest.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rafasconquest.core.Age;
import rafasconquest.worldgen.HorizontalStretch;
import rafasconquest.worldgen.HorizontalStretch.Choice;

import java.util.ArrayList;
import java.util.List;

/**
 * RafasConquest's options for a world being created, opened from the world creation screen: the horizontal stretch,
 * on or off, and how far (1 to 4 times wider each way; off by default if a mod it doesn't suit is installed), the
 * province size (1: provinces 768 blocks apart, the map out to 16,000 blocks; 2: twice both), and the ages, ticked on or
 * off but always adjacent. The choices are kept for the world when it is made.
 */
public final class ConquestWorldScreen extends Screen {
    private final Screen parent;
    private boolean on;
    private int factor;
    private int provinceSize;
    private boolean martialTitles;
    private boolean mantraSchools;
    private Age.Range ages;
    private final List<Checkbox> ageBoxes = new ArrayList<>();
    private AbstractSliderButton slider;

    public ConquestWorldScreen(Screen parent) {
        super(Component.literal("RafasConquest"));
        this.parent = parent;
        Choice choice = HorizontalStretch.pending() != null ? HorizontalStretch.pending() : HorizontalStretch.defaultChoice();
        this.on = choice.on();
        this.factor = choice.factor();
        this.provinceSize = choice.provinceSize();
        this.martialTitles = choice.martialTitles();
        this.mantraSchools = choice.mantraSchools();
        this.ages = choice.ages();
    }

    @Override
    protected void init() {
        int x = width / 2 - 100;
        int y = height / 2 - 36;
        addRenderableWidget(Button.builder(toggleLabel(), b -> {
            on = !on;
            b.setMessage(toggleLabel());
            slider.active = on;
            keep();
        }).bounds(x, y, 200, 20).build());
        int span = HorizontalStretch.MAX - HorizontalStretch.MIN;
        slider = new AbstractSliderButton(x, y + 24, 200, 20, Component.empty(), (double) (factor - HorizontalStretch.MIN) / span) {
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
        addRenderableWidget(new AbstractSliderButton(x, y + 48, 200, 20, Component.empty(),
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
        addRenderableWidget(Button.builder(yesNo("Enable Martial Titles", martialTitles), b -> {
            martialTitles = !martialTitles;
            b.setMessage(yesNo("Enable Martial Titles", martialTitles));
            keep();
        }).bounds(x, y + 72, 200, 20).tooltip(Tooltip.create(Component.literal("Sword Gods, Sword Kings, Sword Saints, and the sword sanctums")))
                .build());
        addRenderableWidget(Button.builder(yesNo("Enable Mantra Schools", mantraSchools), b -> {
            mantraSchools = !mantraSchools;
            b.setMessage(yesNo("Enable Mantra Schools", mantraSchools));
            keep();
        }).bounds(x, y + 96, 200, 20).tooltip(Tooltip.create(Component.literal("Offensive magic system; 10 schools, each with a dojo")))
                .build());
        // The ages, one tick box each, in a centred row.
        ageBoxes.clear();
        int gap = 8;
        int rowWidth = -gap;
        for (Age age : Age.values()) {
            Checkbox box = Checkbox.builder(Component.literal(age.shortName), font).selected(ages.has(age))
                    .onValueChange((b, ticked) -> {
                        ages = ages.toggle(age);
                        keep();
                        refreshAges();
                    }).build();
            ageBoxes.add(box);
            rowWidth += box.getWidth() + gap;
        }
        int boxX = width / 2 - rowWidth / 2;
        for (Checkbox box : ageBoxes) {
            box.setPosition(boxX, y + 122);
            boxX += box.getWidth() + gap;
            addRenderableWidget(box);
        }
        refreshAges();
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose()).bounds(x, y + 146, 200, 20).build());
    }

    /** Only the ages that can change are clickable (so a box's tick always matches the range); the rest are greyed. */
    private void refreshAges() {
        Age[] all = Age.values();
        for (int i = 0; i < all.length; i++) {
            Checkbox box = ageBoxes.get(i);
            boolean can = ages.canToggle(all[i]);
            box.active = can;
            box.setAlpha(can ? 1f : 0.5f);
            box.setTooltip(Tooltip.create(Component.literal(can ? all[i].title
                    : all[i].title + "\nAges must be adjacent: only the first or last can be turned off, only a neighbour turned on")));
        }
    }

    private Component toggleLabel() {
        return Component.literal("Horizontal stretch: " + (on ? "On" : "Off"));
    }

    private static Component yesNo(String label, boolean on) {
        return Component.literal(label + ": " + (on ? "Y" : "N"));
    }

    private void keep() {
        HorizontalStretch.setPending(new Choice(on, factor, provinceSize, martialTitles, mantraSchools, ages));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g, mouseX, mouseY, partialTick);
        g.drawCenteredString(font, title, width / 2, height / 2 - 80, 0xFFFFFF);
        g.drawCenteredString(font, "Stretches biomes and the land (continents, mountains, coasts) wider each way.",
                width / 2, height / 2 - 62, 0xA0A0A0);
        List<String> clashing = HorizontalStretch.incompatibleInstalled();
        if (!clashing.isEmpty()) {
            g.drawCenteredString(font, Component.literal("Off by default: " + String.join(", ", clashing) + " installed")
                    .withStyle(ChatFormatting.GOLD), width / 2, height / 2 - 48, 0xFFFFFF);
        }
        for (net.minecraft.client.gui.components.Renderable widget : renderables) { // the widgets only: 1.21 Screen.render would paint the background over what is drawn above
            widget.render(g, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public void onClose() {
        keep();
        minecraft.setScreen(parent);
    }
}
