package rafasconquest.sim;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import rafasconquest.core.CityLayout;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionBanner;
import rafasconquest.poi.Poi;
import rafasconquest.poi.PoiType;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Paints the banners on city walls in the colours of the city's current owner. Runs when a wall chunk loads (the
 * first load after generation included), so banners follow the owner if a city ever changes hands. Server thread only.
 */
final class CityBanners {
    private CityBanners() {
    }

    /** Returns how many banners were repainted. */
    static int paintChunk(ServerLevel level, LevelChunk chunk, ConquestSavedData data, PoiPrecomputer precomputer) {
        int painted = 0;
        Registry<BannerPattern> patterns = level.registryAccess().registryOrThrow(Registries.BANNER_PATTERN);
        for (BlockEntity be : new ArrayList<>(chunk.getBlockEntities().values())) {
            if (!(be instanceof BannerBlockEntity)) {
                continue;
            }
            BlockPos pos = be.getBlockPos();
            Optional<CityLayout.Site> site = CityLayout.wallAt(level.getSeed(), data.settings(), pos.getX(), pos.getZ());
            if (site.isEmpty()) {
                continue;
            }
            ChunkPos cityChunk = new ChunkPos(site.get().x() >> 4, site.get().z() >> 4);
            precomputer.precomputeRegion(PoiPrecomputer.regionOf(site.get().x()), PoiPrecomputer.regionOf(site.get().z()));
            Poi city = data.poiInChunk(cityChunk, PoiType.CITY);
            Faction owner = city == null ? null : data.faction(city.owner());
            if (owner != null && paint(level, pos, FactionBanner.of(owner.id()), patterns)) {
                painted++;
            }
        }
        return painted;
    }

    private static boolean paint(ServerLevel level, BlockPos pos, FactionBanner design, Registry<BannerPattern> patterns) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof BannerBlock)) {
            return false;
        }
        ListTag layers = new ListTag();
        for (FactionBanner.Layer layer : design.layers()) {
            BannerPattern pattern = patterns.get(ResourceLocation.fromNamespaceAndPath("minecraft", layer.pattern()));
            if (pattern == null) {
                continue;
            }
            CompoundTag tag = new CompoundTag();
            tag.putString("Pattern", pattern.getHashname());
            tag.putInt("Color", layer.color());
            layers.add(tag);
        }
        Block colored = BannerBlock.byColor(DyeColor.byId(design.base()));
        BlockEntity current = level.getBlockEntity(pos);
        if (state.getBlock() == colored && current != null && layers.equals(current.saveWithoutMetadata().getList("Patterns", 10))) {
            return false;
        }
        if (state.getBlock() != colored) {
            state = colored.defaultBlockState().setValue(BannerBlock.ROTATION, state.getValue(BannerBlock.ROTATION));
            level.setBlock(pos, state, 3);
        }
        if (level.getBlockEntity(pos) instanceof BannerBlockEntity banner) {
            CompoundTag tag = banner.saveWithoutMetadata();
            tag.put("Patterns", layers);
            banner.load(tag);
            banner.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
        return true;
    }
}
