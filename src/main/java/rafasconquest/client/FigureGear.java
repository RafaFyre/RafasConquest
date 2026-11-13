package rafasconquest.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * What a block's soldiers wear, as its figures show it: a colour for each piece (-1 for nothing), what they carry in
 * hand and whether they bear a shield. The colours are those of the items' own textures, so a figure matches the real
 * soldier whatever the kit (vanilla or Epic Knights), dyed leather in its dye and a banner shield in its banner's colour.
 */
record FigureGear(int head, int chest, int legs, int feet, int main, int off, int horse, Weapon weapon, boolean shield, int[] raw) {
    enum Weapon { NONE, SWORD, GREAT, LONG, BOW }

    /** Each item's texture, averaged. Client thread only; let go on leaving the world. */
    private static final Map<Item, Integer> AVERAGE = new HashMap<>();

    /** A tint flagged so stands in for the item's texture (a shield's banner) rather than dyeing it. */
    private static final int REPLACES = 0x1000000;

    /**
     * From what the server sends ({@code rafasconquest.net.ConquestNetwork.BattleSnapshot#gear}): for each of head,
     * chest, legs, feet, main hand, off hand and horse, an item's registry id (-1 for nothing) and its tint.
     */
    @Nullable
    static FigureGear of(@Nullable int[] gear) {
        if (gear == null || gear.length < 14) {
            return null;
        }
        Item main = item(gear[8]);
        Item off = item(gear[10]);
        return new FigureGear(colour(gear, 0), colour(gear, 1), colour(gear, 2), colour(gear, 3), colour(gear, 4),
                colour(gear, 5), colour(gear, 6), weapon(main), off != null && shield(off), gear);
    }

    /** What is worn or held in a slot (0 head … 6 horse, as sent), or null. */
    @Nullable
    Item worn(int slot) {
        return item(raw[slot * 2]);
    }

    /** A slot's dye as the game dyes leather, or -1 for none (a banner shield's colour isn't one). */
    int dye(int slot) {
        int tint = raw[slot * 2 + 1];
        return tint < 0 || (tint & REPLACES) != 0 ? -1 : tint & 0xFFFFFF;
    }

    static void clear() {
        AVERAGE.clear();
    }

    @Nullable
    private static Item item(int id) {
        return id < 0 ? null : BuiltInRegistries.ITEM.byId(id);
    }

    private static int colour(int[] gear, int slot) {
        Item item = item(gear[slot * 2]);
        int tint = gear[slot * 2 + 1];
        if (item == null) {
            return -1;
        }
        if (tint >= 0 && (tint & REPLACES) != 0) {
            return tint & 0xFFFFFF;
        }
        int rgb = AVERAGE.computeIfAbsent(item, FigureGear::average);
        return tint < 0 ? rgb : multiply(rgb, tint);
    }

    /** Dyes a colour as the game dyes leather: each channel times the dye's. */
    static int multiply(int rgb, int dye) {
        int r = (rgb >> 16 & 255) * (dye >> 16 & 255) / 255;
        int g = (rgb >> 8 & 255) * (dye >> 8 & 255) / 255;
        int b = (rgb & 255) * (dye & 255) / 255;
        return r << 16 | g << 8 | b;
    }

    /** The average colour of an item's icon, its see-through pixels left out; mid grey if it can't be read. */
    private static int average(Item item) {
        try {
            SpriteContents sprite = Minecraft.getInstance().getItemRenderer()
                    .getModel(new ItemStack(item), null, null, 0).getParticleIcon().contents();
            NativeImage image = sprite.getOriginalImage();
            long r = 0, g = 0, b = 0, n = 0;
            for (int y = 0; y < sprite.height(); y++) { // the first frame of an animated one
                for (int x = 0; x < sprite.width(); x++) {
                    int p = image.getPixelRGBA(x, y); // ABGR
                    if ((p >>> 24) < 128) {
                        continue;
                    }
                    r += p & 255;
                    g += p >> 8 & 255;
                    b += p >> 16 & 255;
                    n++;
                }
            }
            return n == 0 ? 0x808080 : (int) (r / n) << 16 | (int) (g / n) << 8 | (int) (b / n);
        } catch (RuntimeException e) {
            return 0x808080;
        }
    }

    private static Weapon weapon(@Nullable Item item) {
        if (item == null || item instanceof net.minecraft.world.item.BannerItem) {
            return Weapon.NONE;
        }
        if (item instanceof BowItem || item instanceof CrossbowItem) {
            return Weapon.BOW;
        }
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        if (name.contains("pike") || name.contains("lance") || name.contains("halberd") || name.contains("spear")
                || name.contains("glaive") || name.contains("partisan") || name.contains("musket")) {
            return Weapon.LONG;
        }
        if (name.contains("zweihander") || name.contains("claymore") || name.contains("great")) {
            return Weapon.GREAT;
        }
        return Weapon.SWORD;
    }

    private static boolean shield(Item item) {
        return item instanceof ShieldItem || BuiltInRegistries.ITEM.getKey(item).getPath().contains("shield");
    }
}
