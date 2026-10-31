package rafasconquest.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.init.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.Nullable;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;
import rafasconquest.people.Person;
import rafasconquest.people.Race;

import java.util.List;
import java.util.UUID;

/** What the nation and character pages draw alike: a character's model, a nation's banner, the back and close buttons. */
final class Portraits {
    static final int BACKGROUND = 0xFF202226;
    static final int BOX = 0xFF0E0F10;
    static final int BOX_EDGE = 0xFF3C3E42;
    static final int TEXT = 0xFFF0EEE8;
    static final int MUTED = 0xFFA8A49C;
    static final int GOLD = 0xFFE0C060;
    static final int ICON = 0xFF6CE8FF;
    static final int ICON_HOVER = 0xFFB8F4FF;

    private Portraits() {
    }

    /** A character's model: a Recruits recruit in the skin of their race's land. */
    @Nullable
    static AbstractRecruitEntity model(Minecraft mc, Person p) {
        if (mc.level == null) {
            return null;
        }
        AbstractRecruitEntity entity = ModEntityTypes.RECRUIT.get().create(mc.level);
        if (entity != null) {
            Race race = Race.values()[p.race()];
            int[] skins = switch (race.homeland()) {
                case DESERT, JUNGLE, SAVANNA -> new int[]{15, 19};
                case SNOW -> new int[]{5, 10};
                case SWAMP, TAIGA -> new int[]{5, 14};
                case PLAINS -> new int[]{0, 14};
            };
            entity.setBiome((byte) race.homeland().ordinal());
            entity.setVariant(skins[0] + Math.floorMod(p.name().hashCode(), skins[1] - skins[0]));
        }
        return entity;
    }

    /** The back button at the very top left (to the page before) and the close button at the very top right (to the game). */
    static void buttons(Screen screen, int width, Runnable back, java.util.function.Consumer<Button> add) {
        add.accept(Button.builder(Component.literal("<"), b -> back.run()).bounds(6, 6, 20, 20).build());
        add.accept(Button.builder(Component.literal("✕"), b -> Minecraft.getInstance().setScreen(null)).bounds(width - 26, 6, 20, 20).build());
    }

    /** A box: dark inside, with a grey edge. */
    static void box(GuiGraphics g, int x, int y, int w, int h) {
        g.fill(x, y, x + w, y + h, BOX);
        g.renderOutline(x, y, w, h, BOX_EDGE);
    }

    /** A diamond centred on (x, y), {@code r} from its centre to a point: an edge colour round a fill. */
    static void diamond(GuiGraphics g, int x, int y, int r, int fill, int edge) {
        for (int d = 0; d <= r; d++) {
            g.fill(x - (r - d), y - d, x + (r - d) + 1, y - d + 1, edge);
            g.fill(x - (r - d), y + d, x + (r - d) + 1, y + d + 1, edge);
        }
        for (int d = 0; d < r; d++) {
            g.fill(x - (r - 1 - d), y - d, x + (r - 1 - d) + 1, y - d + 1, fill);
            g.fill(x - (r - 1 - d), y + d, x + (r - 1 - d) + 1, y + d + 1, fill);
        }
    }

    /** A filled circle centred on (x, y). */
    static void circle(GuiGraphics g, int x, int y, int r, int color) {
        for (int dy = -r; dy <= r; dy++) {
            int dx = (int) Math.sqrt(r * r - dy * dy);
            g.fill(x - dx, y + dy, x + dx + 1, y + dy + 1, color);
        }
    }

    /** A nation's banner, ready to draw at any size as a loom shows one. */
    static final class Banner {
        @Nullable
        private final ModelPart flag;
        private final List<Pair<Holder<BannerPattern>, DyeColor>> patterns;

        Banner(Minecraft mc, UUID nation) {
            if (mc.level == null) {
                flag = null;
                patterns = List.of();
                return;
            }
            FactionBanner design = FactionBanner.of(nation);
            patterns = BannerBlockEntity.createPatterns(FactionBannerNbt.baseColor(design), FactionBannerNbt.patterns(mc.level.registryAccess(), design));
            flag = mc.getEntityModels().bakeLayer(ModelLayers.BANNER).getChild("flag");
        }

        /** Fills the box at (x, y), w by h. */
        void draw(GuiGraphics g, int x, int y, int w, int h) {
            if (flag == null) {
                g.fill(x, y, x + w, y + h, 0xFFB01010);
                return;
            }
            Lighting.setupForFlatItems();
            PoseStack pose = g.pose();
            pose.pushPose();
            // The flag is 20 by 40 model pixels, its top edge at the origin; stretched to fill the box.
            pose.translate(x + w / 2F, y, 100);
            pose.scale(w / 1.25F, h / 2.5F, -1);
            flag.xRot = 0;
            flag.y = 0;
            BannerRenderer.renderPatterns(pose, g.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, flag,
                    ModelBakery.BANNER_BASE, true, patterns);
            pose.popPose();
            g.flush();
            Lighting.setupFor3DItems();
        }
    }
}
