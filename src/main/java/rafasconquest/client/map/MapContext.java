package rafasconquest.client.map;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import rafasconquest.RafasConquest;
import rafasconquest.core.TerritoryMap;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;
import rafasconquest.map.BaseMap;
import rafasconquest.net.FactionInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Everything the layers draw from, built once when the map opens: the base map and territory textures, the factions,
 * where each faction's territory is centred, and each faction's colour and banner.
 */
public final class MapContext implements AutoCloseable {
    public static final ResourceLocation BASE_TEXTURE = ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "map/base");
    public static final ResourceLocation TERRITORY_TEXTURE = ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "map/territory");
    /** Territory fill and border opacity (0–255). */
    private static final int FILL_ALPHA = 0x66;
    private static final int BORDER_ALPHA = 0xDD;

    public final BaseMap base;
    public final List<FactionInfo> factions;
    private final Map<UUID, Territory> territories = new HashMap<>();
    private final Map<UUID, ItemStack> banners = new HashMap<>();
    private final Map<UUID, Integer> colors = new HashMap<>();

    /** A faction's land on the map: its centre (world blocks) and size (pixels of the base map). */
    public record Territory(double centerX, double centerZ, int pixels) {
    }

    public MapContext(BaseMap base, List<FactionInfo> factions) {
        this.base = base;
        this.factions = factions;
        Minecraft mc = Minecraft.getInstance();
        for (FactionInfo f : factions) {
            FactionBanner banner = FactionBanner.of(f.id());
            float[] rgb = DyeColor.byId(banner.base()).getTextureDiffuseColors();
            colors.put(f.id(), ((int) (rgb[0] * 255) << 16) | ((int) (rgb[1] * 255) << 8) | (int) (rgb[2] * 255));
            if (mc.level != null) {
                banners.put(f.id(), FactionBannerNbt.item(mc.level.registryAccess(), banner));
            }
        }
        upload(BASE_TEXTURE, baseImage());
        upload(TERRITORY_TEXTURE, territoryImage());
    }

    public Territory territory(UUID faction) {
        return territories.get(faction);
    }

    /** The primary colour of a faction's banner, as RGB. */
    public int color(UUID faction) {
        return colors.getOrDefault(faction, 0xFFFFFF);
    }

    public ItemStack banner(UUID faction) {
        return banners.getOrDefault(faction, ItemStack.EMPTY);
    }

    public boolean insideStormWall(double x, double z) {
        return x * x + z * z <= (double) base.visibleRadius() * base.visibleRadius();
    }

    private NativeImage baseImage() {
        NativeImage image = new NativeImage(base.width(), base.height(), false);
        for (int z = 0; z < base.height(); z++) {
            for (int x = 0; x < base.width(); x++) {
                image.setPixelRGBA(x, z, toAbgr(base.pixels()[z * base.width() + x]));
            }
        }
        return image;
    }

    /**
     * The territory layer: every pixel takes the colour of whoever holds it, see-through, with a stronger edge where
     * the holder changes. Also finds the centre of each faction's land, for its name.
     */
    private NativeImage territoryImage() {
        int w = base.width();
        int h = base.height();
        List<TerritoryMap.Claim> claims = factions.stream().map(FactionInfo::claim).toList();
        int[] owners = TerritoryMap.rasterize(claims, base.minX(), base.minZ(), base.blocksPerPixel(), w, h, base.visibleRadius());
        double[] sumX = new double[factions.size()];
        double[] sumZ = new double[factions.size()];
        int[] count = new int[factions.size()];
        NativeImage image = new NativeImage(w, h, false);
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                int owner = owners[z * w + x];
                if (owner < 0) {
                    image.setPixelRGBA(x, z, 0);
                    continue;
                }
                sumX[owner] += base.minX() + (x + 0.5) * base.blocksPerPixel();
                sumZ[owner] += base.minZ() + (z + 0.5) * base.blocksPerPixel();
                count[owner]++;
                boolean edge = x == 0 || z == 0 || x == w - 1 || z == h - 1
                        || owners[z * w + x - 1] != owner || owners[z * w + x + 1] != owner
                        || owners[(z - 1) * w + x] != owner || owners[(z + 1) * w + x] != owner;
                int rgb = color(factions.get(owner).id());
                image.setPixelRGBA(x, z, toAbgr(((edge ? BORDER_ALPHA : FILL_ALPHA) << 24) | rgb));
            }
        }
        for (int i = 0; i < factions.size(); i++) {
            if (count[i] > 0) {
                territories.put(factions.get(i).id(), new Territory(sumX[i] / count[i], sumZ[i] / count[i], count[i]));
            }
        }
        return image;
    }

    private static void upload(ResourceLocation location, NativeImage image) {
        DynamicTexture texture = new DynamicTexture(image);
        Minecraft.getInstance().getTextureManager().register(location, texture);
        texture.setFilter(true, false); // smooth when zoomed in, so territories read as soft shapes
    }

    /** NativeImage stores pixels as ABGR. */
    private static int toAbgr(int argb) {
        return (argb & 0xFF00FF00) | ((argb >> 16) & 0xFF) | ((argb & 0xFF) << 16);
    }

    @Override
    public void close() {
        Minecraft.getInstance().getTextureManager().release(BASE_TEXTURE);
        Minecraft.getInstance().getTextureManager().release(TERRITORY_TEXTURE);
    }
}
