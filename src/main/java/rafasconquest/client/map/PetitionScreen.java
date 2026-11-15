package rafasconquest.client.map;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import rafasconquest.RafasConquest;
import rafasconquest.client.ClientPeerage;
import rafasconquest.core.TitleMap;
import rafasconquest.map.BaseMap;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.peerage.LandGrants;
import rafasconquest.peerage.NobleTitle;

import java.util.List;

/**
 * The world map, to pick the title a noble is petitioned for ({@link rafasconquest.peerage.Talk}): the titles the noble
 * can give ({@link LandGrants#petition}) are lit, the one under the mouse white with its name in black, and a click
 * asks for it. The bar at the bottom has a notch for each rank of title, Count to Emperor, and lights the titles of
 * the rank it stands on.
 */
public class PetitionScreen extends FactionMapScreen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "map/petition");
    private static final TitleMap.Tier[] TIERS = TitleMap.Tier.values();
    /** ABGR, as NativeImage keeps them: the titles to choose among, and the one under the mouse. */
    private static final int LIT = 0x805ED7FF;
    private static final int WHITE = 0xF0FFFFFF;

    private final int entity;
    private final int noble;
    private TitleMap.Tier tier = TitleMap.Tier.COUNTY;
    private AbstractSliderButton bar;
    private DynamicTexture texture;
    /** For each province, the seat of the title it lies in at this notch if that can be asked for, else -1; null: work it out. */
    private int[] seatOf;
    private int hovered = -1;
    /** The hovered title the texture was painted with. */
    private int painted;
    private boolean pressed;
    private double dragged;

    public PetitionScreen(int entity, int noble, String name) {
        super(null, Component.literal("Petition " + name + ": choose a title to govern"));
        this.entity = entity;
        this.noble = noble;
    }

    @Override
    protected void init() {
        super.init();
        bar = addRenderableWidget(new AbstractSliderButton(width / 2 - 100, height - 34, 200, 20,
                Component.literal(tier.title()), tier.ordinal() / (TIERS.length - 1.0)) {
            @Override
            protected void updateMessage() {
                setMessage(Component.literal(tier.title()));
            }

            @Override
            protected void applyValue() {
                int notch = (int) Math.round(value * (TIERS.length - 1));
                value = notch / (TIERS.length - 1.0); // the knob rests on a notch
                if (tier != TIERS[notch]) {
                    tier = TIERS[notch];
                    seatOf = null;
                }
            }
        });
    }

    @Override
    protected void overlay(GuiGraphics g, MapContext map, int mouseX, int mouseY) {
        BaseMap base = map.base;
        boolean fresh = seatOf == null || texture == null;
        if (seatOf == null) {
            List<Integer> seats = LandGrants.petition(base, noble, tier, s -> ClientPeerage.holder(s) != null);
            seatOf = new int[base.centres().size()];
            for (int p = 0; p < seatOf.length; p++) {
                int[] over = base.titles().seatsOver(p); // empire first
                int seat = over.length == 0 ? -1 : over[over.length - 1 - tier.ordinal()];
                seatOf[p] = seats.contains(seat) ? seat : -1;
            }
        }
        int province = bar.isMouseOver(mouseX, mouseY) || !view.contains(mouseX, mouseY) ? -1
                : base.provinceIndexAt((int) Math.floor(view.worldX(mouseX)), (int) Math.floor(view.worldZ(mouseY)));
        hovered = province < 0 ? -1 : seatOf[province];
        if (fresh || hovered != painted) {
            paint(base);
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BaseMapLayer.drawAligned(g, view, map, TEXTURE);
        RenderSystem.disableBlend();
        if (hovered >= 0) {
            // Its name in black, in the middle of its land.
            double x = 0;
            double z = 0;
            int n = 0;
            for (int p = 0; p < seatOf.length; p++) {
                if (seatOf[p] == hovered) {
                    x += base.centres().get(p).x();
                    z += base.centres().get(p).z();
                    n++;
                }
            }
            String name = base.titleName(tier, hovered);
            g.drawString(font, name, (int) view.screenX(x / n) - font.width(name) / 2, (int) view.screenY(z / n) - 4, 0xFF000000, false);
        }
        // The bar's notches, lowest rank on the left.
        g.fill(bar.getX() - 24, bar.getY() - 19, bar.getX() + bar.getWidth() + 24, bar.getY() + 23, 0xD0101214);
        for (int i = 0; i < TIERS.length; i++) {
            int x = bar.getX() + 4 + i * (bar.getWidth() - 8) / (TIERS.length - 1);
            g.fill(x, bar.getY() - 5, x + 1, bar.getY(), 0xFFF4EEDC);
            g.drawCenteredString(font, NobleTitle.of(TIERS[i]).title(), x, bar.getY() - 15, i == tier.ordinal() ? 0xFFFFD75E : 0xFFA0A0A0);
        }
    }

    private void paint(BaseMap base) {
        if (texture == null) {
            texture = new DynamicTexture(base.width(), base.height(), true);
            minecraft.getTextureManager().register(TEXTURE, texture);
        }
        NativeImage image = texture.getPixels();
        int[] provinces = base.provinces();
        for (int i = 0; i < provinces.length; i++) {
            int seat = provinces[i] < 0 ? -1 : seatOf[provinces[i]];
            image.setPixelRGBA(i % base.width(), i / base.width(), seat < 0 ? 0 : seat == hovered ? WHITE : LIT);
        }
        texture.upload();
        painted = hovered;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        pressed = button == 0;
        dragged = 0;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        dragged += Math.abs(dx) + Math.abs(dy);
        if (isDragging() && getFocused() == bar) {
            return bar.mouseDragged(mouseX, mouseY, button, dx, dy); // the map under the bar stays put
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    /** A click (a press and release without dragging the map) on a lit title asks for it. */
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean click = pressed && button == 0 && dragged < 3 && !bar.isMouseOver(mouseX, mouseY);
        pressed = false;
        if (click && hovered >= 0) {
            ConquestNetwork.choose(entity, "petition:" + hovered);
            minecraft.setScreen(null); // the noble's answer opens the conversation again
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void removed() {
        if (texture != null) {
            minecraft.getTextureManager().release(TEXTURE);
            texture = null;
        }
        super.removed();
    }
}
