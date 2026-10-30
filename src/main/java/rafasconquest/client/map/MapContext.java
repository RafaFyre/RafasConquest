package rafasconquest.client.map;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;
import rafasconquest.map.BaseMap;
import rafasconquest.map.PoliticalMap;
import rafasconquest.map.PoliticalStyle;
import rafasconquest.net.FactionInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Everything the layers draw from, built once when the map opens ({@link PoliticalMap}): the parchment base and the
 * territory textures, the factions, each realm's colour and banner, and where each realm's name goes.
 */
public final class MapContext implements AutoCloseable {
    public static final ResourceLocation BASE_TEXTURE = ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "map/base");
    public static final ResourceLocation TERRITORY_TEXTURE = ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "map/territory");
    public static final ResourceLocation TITLES_TEXTURE = ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "map/titles");

    public final BaseMap base;
    public final List<FactionInfo> factions;
    private final Map<UUID, PoliticalStyle.LabelShape> labels = new HashMap<>();
    private final Map<UUID, ItemStack> banners = new HashMap<>();
    private final Map<UUID, Integer> colors = new HashMap<>();

    public MapContext(BaseMap base, List<FactionInfo> factions) {
        this.base = base;
        this.factions = factions;
        PoliticalMap political = PoliticalMap.build(base, factions);
        Minecraft mc = Minecraft.getInstance();
        for (int i = 0; i < factions.size(); i++) {
            UUID id = factions.get(i).id();
            colors.put(id, political.realmColors()[i]);
            if (political.labels()[i] != null) {
                labels.put(id, political.labels()[i]);
            }
            if (mc.level != null) {
                banners.put(id, FactionBannerNbt.item(mc.level.registryAccess(), FactionBanner.of(id)));
            }
        }
        upload(BASE_TEXTURE, image(political.parchment()));
        upload(TERRITORY_TEXTURE, image(political.territory()));
        upload(TITLES_TEXTURE, image(rafasconquest.map.TitleStyle.borders(base)));
    }

    /** Where a realm's name goes, or null if it holds no land on the map. */
    @Nullable
    public PoliticalStyle.LabelShape label(UUID faction) {
        return labels.get(faction);
    }

    /** A realm's colour on the map, as RGB. */
    public int color(UUID faction) {
        return colors.getOrDefault(faction, 0xFFFFFF);
    }

    public ItemStack banner(UUID faction) {
        return banners.getOrDefault(faction, ItemStack.EMPTY);
    }

    public boolean insideKnownWorld(double x, double z) {
        return x * x + z * z <= (double) base.visibleRadius() * base.visibleRadius();
    }

    private NativeImage image(int[] argb) {
        NativeImage image = new NativeImage(base.width(), base.height(), false);
        for (int z = 0; z < base.height(); z++) {
            for (int x = 0; x < base.width(); x++) {
                image.setPixelRGBA(x, z, toAbgr(argb[z * base.width() + x]));
            }
        }
        return image;
    }

    private static void upload(ResourceLocation location, NativeImage image) {
        DynamicTexture texture = new DynamicTexture(image);
        Minecraft.getInstance().getTextureManager().register(location, texture);
        texture.setFilter(true, false); // smooth when zoomed in
    }

    /** NativeImage stores pixels as ABGR. */
    private static int toAbgr(int argb) {
        return (argb & 0xFF00FF00) | ((argb >> 16) & 0xFF) | ((argb & 0xFF) << 16);
    }

    @Override
    public void close() {
        Minecraft.getInstance().getTextureManager().release(BASE_TEXTURE);
        Minecraft.getInstance().getTextureManager().release(TERRITORY_TEXTURE);
        Minecraft.getInstance().getTextureManager().release(TITLES_TEXTURE);
    }
}
