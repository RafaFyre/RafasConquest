package rafasconquest.faction;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Turns a {@link FactionBanner} into vanilla banner data: block colour, pattern NBT, and a banner item. */
public final class FactionBannerNbt {
    private FactionBannerNbt() {
    }

    public static DyeColor baseColor(FactionBanner banner) {
        return DyeColor.byId(banner.base());
    }

    /** The "Patterns" list a banner block entity or item stores. */
    public static ListTag patterns(RegistryAccess registries, FactionBanner banner) {
        Registry<BannerPattern> patterns = registries.registryOrThrow(Registries.BANNER_PATTERN);
        ListTag list = new ListTag();
        for (FactionBanner.Layer layer : banner.layers()) {
            BannerPattern pattern = patterns.get(ResourceLocation.fromNamespaceAndPath("minecraft", layer.pattern()));
            if (pattern == null) {
                continue;
            }
            CompoundTag tag = new CompoundTag();
            tag.putString("Pattern", pattern.getHashname());
            tag.putInt("Color", layer.color());
            list.add(tag);
        }
        return list;
    }

    /** A banner item showing this design, as a player would craft it. */
    public static ItemStack item(RegistryAccess registries, FactionBanner banner) {
        ItemStack stack = new ItemStack(BannerBlock.byColor(baseColor(banner)).asItem());
        CompoundTag blockEntity = new CompoundTag();
        blockEntity.put("Patterns", patterns(registries, banner));
        BlockItem.setBlockEntityData(stack, BlockEntityType.BANNER, blockEntity);
        return stack;
    }
}
