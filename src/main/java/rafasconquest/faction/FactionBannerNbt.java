package rafasconquest.faction;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.Optional;

/** Turns a {@link FactionBanner} into vanilla banner data: block colour, pattern layers, and a banner item. */
public final class FactionBannerNbt {
    private FactionBannerNbt() {
    }

    public static DyeColor baseColor(FactionBanner banner) {
        return DyeColor.byId(banner.base());
    }

    /** The pattern layers a banner block entity or item carries. */
    public static BannerPatternLayers patterns(RegistryAccess registries, FactionBanner banner) {
        Registry<BannerPattern> patterns = registries.registryOrThrow(Registries.BANNER_PATTERN);
        BannerPatternLayers.Builder layers = new BannerPatternLayers.Builder();
        for (FactionBanner.Layer layer : banner.layers()) {
            Optional<Holder.Reference<BannerPattern>> pattern = patterns.getHolder(ResourceLocation.fromNamespaceAndPath("minecraft", layer.pattern()));
            pattern.ifPresent(p -> layers.add(p, DyeColor.byId(layer.color())));
        }
        return layers.build();
    }

    /** A banner item showing this design, as a player would craft it. */
    public static ItemStack item(RegistryAccess registries, FactionBanner banner) {
        ItemStack stack = new ItemStack(BannerBlock.byColor(baseColor(banner)).asItem());
        stack.set(DataComponents.BANNER_PATTERNS, patterns(registries, banner));
        return stack;
    }
}
