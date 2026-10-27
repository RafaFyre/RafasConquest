package rafasconquest.map;

import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;

import java.util.List;

/**
 * Base map colours: a flat, readable colour per kind of biome, in the style of a strategy-game map. Checked in order,
 * so the more specific kinds come first. Water is shaded by depth separately.
 */
public final class BiomePalette {
    public static final int SHALLOW_WATER = 0x4F86C6;
    public static final int DEEP_WATER = 0x1F3F7A;
    public static final int FROZEN_WATER = 0xA9C8E8;
    public static final int RIVER = 0x4A80C8;

    private record Entry(TagKey<Biome> tag, int color) {
    }

    private static final List<Entry> LAND = List.of(
            new Entry(BiomeTags.IS_BEACH, 0xE6D9A2),
            new Entry(BiomeTags.IS_BADLANDS, 0xC2703A),
            new Entry(Tags.Biomes.IS_DESERT, 0xE2D193),
            new Entry(Tags.Biomes.IS_MUSHROOM, 0x9C7BA6),
            new Entry(Tags.Biomes.IS_SNOWY, 0xEEF2F5),
            new Entry(BiomeTags.IS_JUNGLE, 0x3C8A24),
            new Entry(Tags.Biomes.IS_SWAMP, 0x4F6B3C),
            new Entry(BiomeTags.IS_SAVANNA, 0xB4AE5A),
            new Entry(Tags.Biomes.IS_PEAK, 0x9A9A9A),
            new Entry(BiomeTags.IS_TAIGA, 0x4D7A58),
            new Entry(BiomeTags.IS_FOREST, 0x4E8A36),
            new Entry(Tags.Biomes.IS_MOUNTAIN, 0x8A9A7A),
            new Entry(Tags.Biomes.IS_PLAINS, 0x88B85C));
    private static final int DEFAULT_LAND = 0x7DAA50;

    private BiomePalette() {
    }

    /**
     * The sea: no province covers it, and provinces don't like to cross it. Rivers are not: they run through
     * provinces, and only lean borders onto them (crossing one is a biome edge each way).
     */
    public static boolean isSea(Holder<Biome> biome) {
        return biome.is(BiomeTags.IS_OCEAN);
    }

    /** Hard country, where provinces are larger: deserts, badlands, snow and peaks. */
    public static boolean isRough(Holder<Biome> biome) {
        return biome.is(Tags.Biomes.IS_DESERT) || biome.is(BiomeTags.IS_BADLANDS) || biome.is(Tags.Biomes.IS_SNOWY)
                || biome.is(Tags.Biomes.IS_PEAK);
    }

    /** Colour of a column whose surface is {@code height}, on a world whose sea is at {@code seaLevel}. */
    public static int color(Holder<Biome> biome, int height, int seaLevel) {
        // Frozen ocean and frozen river: their water freezes over.
        boolean frozen = biome.value().getBaseTemperature() <= 0.0F;
        if (biome.is(BiomeTags.IS_OCEAN)) {
            if (frozen) {
                return FROZEN_WATER;
            }
            float depth = Math.min(1, Math.max(0, (seaLevel - height) / 40F));
            return MapColors.lerp(SHALLOW_WATER, DEEP_WATER, depth);
        }
        if (biome.is(BiomeTags.IS_RIVER)) {
            return frozen ? FROZEN_WATER : RIVER;
        }
        for (Entry e : LAND) {
            if (biome.is(e.tag())) {
                return e.color();
            }
        }
        return DEFAULT_LAND;
    }
}
