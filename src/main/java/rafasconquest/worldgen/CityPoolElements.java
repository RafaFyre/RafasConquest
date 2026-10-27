package rafasconquest.worldgen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import rafasconquest.RafasConquest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pool elements that place vanilla village templates unchanged, but send their jigsaw connections to the city's own
 * street and house pools instead of the village ones. That is what lets cities reuse vanilla buildings with
 * different pool weights, without copying any templates.
 */
public final class CityPoolElements {
    /** minecraft:village/&lt;v&gt;/{streets,houses} → rafasconquest:city/&lt;v&gt;/{streets,houses}. */
    private static final Map<String, String> REMAP = new HashMap<>();

    static {
        for (CityVariant v : CityVariant.values()) {
            for (String pool : new String[]{"streets", "houses"}) {
                REMAP.put("minecraft:village/" + v.id() + "/" + pool, RafasConquest.MOD_ID + ":city/" + v.id() + "/" + pool);
            }
        }
    }

    private CityPoolElements() {
    }

    static List<StructureTemplate.StructureBlockInfo> remap(List<StructureTemplate.StructureBlockInfo> jigsaws) {
        return jigsaws.stream().map(info -> {
            if (info.nbt() == null) {
                return info;
            }
            String target = REMAP.get(info.nbt().getString("pool"));
            if (target == null) {
                return info;
            }
            CompoundTag nbt = info.nbt().copy();
            nbt.putString("pool", target);
            return new StructureTemplate.StructureBlockInfo(info.pos(), info.state(), nbt);
        }).toList();
    }

    public static class Single extends SinglePoolElement {
        public static final Codec<Single> CODEC = RecordCodecBuilder.create(i ->
                i.group(templateCodec(), processorsCodec(), projectionCodec()).apply(i, Single::new));

        protected Single(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection) {
            super(template, processors, projection);
        }

        @Override
        public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager templates, BlockPos pos, Rotation rotation, RandomSource random) {
            return remap(super.getShuffledJigsawBlocks(templates, pos, rotation, random));
        }

        @Override
        public StructurePoolElementType<?> getType() {
            return ConquestWorldgen.CITY_SINGLE_ELEMENT.get();
        }

        @Override
        public String toString() {
            return "CitySingle[" + template + "]";
        }
    }

    public static class Legacy extends LegacySinglePoolElement {
        public static final Codec<Legacy> CODEC = RecordCodecBuilder.create(i ->
                i.group(templateCodec(), processorsCodec(), projectionCodec()).apply(i, Legacy::new));

        protected Legacy(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection) {
            super(template, processors, projection);
        }

        @Override
        public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager templates, BlockPos pos, Rotation rotation, RandomSource random) {
            return remap(super.getShuffledJigsawBlocks(templates, pos, rotation, random));
        }

        @Override
        public StructurePoolElementType<?> getType() {
            return ConquestWorldgen.CITY_LEGACY_ELEMENT.get();
        }

        @Override
        public String toString() {
            return "CityLegacySingle[" + template + "]";
        }
    }
}
