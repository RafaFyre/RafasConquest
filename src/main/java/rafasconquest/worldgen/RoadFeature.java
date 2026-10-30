package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import rafasconquest.content.ConquestContent;
import rafasconquest.core.Countryside;
import rafasconquest.core.Dojos;
import rafasconquest.core.Roads;
import rafasconquest.core.SeatCities;
import rafasconquest.map.BaseMap;

import java.util.List;

/**
 * Builds the world map's roads into each chunk as it generates ({@link RoadNetwork}), after the trees, so it can clear
 * them. Each column within a road's width gets its surface at the road's height: a full block, and a slab on top where
 * the road stands half a block higher, which eases it up and down hills; the ground is filled below with dirt and cut
 * away above, plants and trees cleared. Where the road is a bridge, the column is the deck instead, railed at its edges
 * and propped on posts down to the ground. Roads stop at city walls and settlements, and short of a dojo.
 * <ul>
 *   <li>High roads: cobblestone, some mossy; stone brick bridges with walls for rails.</li>
 *   <li>City roads: gravel down the middle, dirt path at the edges; spruce bridges on stone brick posts.</li>
 *   <li>Town roads: dirt path; oak bridges on log posts.</li>
 *   <li>Dojo roads: cobblestone; stone brick bridges.</li>
 * </ul>
 */
public class RoadFeature extends Feature<NoneFeatureConfiguration> {
    /** Cleared of everything this high above a road, and of trees this high. */
    private static final int CLEAR = 5;
    private static final int CLEAR_TREES = 14;
    private static final int FILL = 12;
    /** Bridge posts stand every this many blocks along it, no deeper than this. */
    private static final int POST_EVERY = 6;
    private static final int POST_DEPTH = 40;
    /** Roads keep this far from a city's walls, a settlement, and a dojo's middle. */
    private static final int CITY_MARGIN = 3;
    private static final int SETTLEMENT_MARGIN = 2;
    private static final int DOJO_CLEAR = 14;

    public RoadFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BaseMap map = ConquestRuntime.map();
        if (map == null || map.roads().roads().isEmpty()) {
            return false;
        }
        WorldGenLevel level = context.level();
        int x0 = context.origin().getX() & ~15;
        int z0 = context.origin().getZ() & ~15;
        RoadNetwork network = RoadNetwork.of(map);
        List<int[]> stretches = network.inChunk(x0 >> 4, z0 >> 4);
        if (stretches.isEmpty()) {
            return false;
        }
        List<Roads.Road> roads = map.roads().roads();
        List<int[]> keeps = keeps(map, x0, z0);
        boolean built = false;
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int x = x0 + dx;
                int z = z0 + dz;
                double cx = x + 0.5;
                double cz = z + 0.5;
                // The best road over this column: the highest grade, then the nearest.
                int bestRoad = -1;
                int bestRank = -1;
                double bestDistance = Double.MAX_VALUE;
                double bestAlong = 0;
                for (int[] s : stretches) {
                    Roads.Road road = roads.get(s[0]);
                    int k = s[1];
                    double ax = road.xs()[k] + 0.5;
                    double az = road.zs()[k] + 0.5;
                    double bx = road.xs()[k + 1] + 0.5;
                    double bz = road.zs()[k + 1] + 0.5;
                    double vx = bx - ax;
                    double vz = bz - az;
                    double len2 = vx * vx + vz * vz;
                    double t = len2 == 0 ? 0 : Mth.clamp(((cx - ax) * vx + (cz - az) * vz) / len2, 0, 1);
                    double d = Math.hypot(cx - (ax + vx * t), cz - (az + vz * t));
                    if (d > road.grade().width() / 2.0 + 1) {
                        continue;
                    }
                    int rank = rank(road.grade());
                    if (rank > bestRank || rank == bestRank && d < bestDistance) {
                        bestRank = rank;
                        bestDistance = d;
                        bestRoad = s[0];
                        bestAlong = network.along(s[0], k) + t * Math.sqrt(len2);
                    }
                }
                if (bestRoad < 0 || kept(keeps, x, z)) {
                    continue;
                }
                Roads.Road road = roads.get(bestRoad);
                RoadNetwork.Profile profile = network.profile(bestRoad, level.getLevel());
                boolean bridge = profile.bridgeAt(bestAlong);
                double half = road.grade().width() / 2.0;
                if (bestDistance > half + (bridge ? 1 : 0)) {
                    continue;
                }
                float height = profile.heightAt(bestAlong);
                if (bridge) {
                    bridge(level, road.grade(), x, z, height, bestDistance > half, bestAlong);
                } else {
                    road(level, road.grade(), x, z, height, bestDistance);
                }
                built = true;
            }
        }
        signposts(level, map, keeps, x0, z0);
        return built;
    }

    /** How far from a fork's middle its signpost stands, each way: clear of the widest road. */
    private static final int SIGN_OFFSET = 4;

    /**
     * The signposts of the forks in this chunk: an oak fence post beside the road with a sign on top, turned to face the
     * fork, naming the places its roads lead to.
     */
    private static void signposts(WorldGenLevel level, BaseMap map, List<int[]> keeps, int x0, int z0) {
        for (Roads.Junction j : map.roads().junctions()) {
            int x = j.x() + SIGN_OFFSET;
            int z = j.z() + SIGN_OFFSET;
            if (x < x0 || x > x0 + 15 || z < z0 || z > z0 + 15 || kept(keeps, x, z)) {
                continue;
            }
            int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG, x, z);
            BlockPos post = new BlockPos(x, y, z);
            if (!level.getBlockState(post.below()).getFluidState().isEmpty()) {
                continue; // no signs in the water
            }
            level.setBlock(post, Blocks.OAK_FENCE.defaultBlockState(), 2);
            double yaw = Math.toDegrees(Math.atan2(-(j.x() - x), j.z() - z));
            int rotation = Math.floorMod((int) Math.round(yaw / 22.5), 16);
            BlockPos sign = post.above();
            level.setBlock(sign, Blocks.OAK_SIGN.defaultBlockState()
                    .setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.ROTATION_16, rotation), 2);
            if (level.getBlockEntity(sign) instanceof net.minecraft.world.level.block.entity.SignBlockEntity entity) {
                net.minecraft.world.level.block.entity.SignText text = new net.minecraft.world.level.block.entity.SignText();
                for (int i = 0; i < Math.min(4, j.leadsTo().size()); i++) {
                    text = text.setMessage(i, net.minecraft.network.chat.Component.literal(j.leadsTo().get(i)));
                }
                // Written into the sign's data, not set: setting it would tell a world it isn't in yet.
                net.minecraft.nbt.CompoundTag tag = entity.saveWithoutMetadata();
                net.minecraft.world.level.block.entity.SignText.DIRECT_CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, text).result()
                        .ifPresent(encoded -> {
                            tag.put("front_text", encoded);
                            tag.put("back_text", encoded.copy());
                        });
                entity.load(tag);
            }
        }
    }

    /** One column of road: surface at the road's height (a slab on top where half a block higher), filled below, cleared above. */
    private static void road(WorldGenLevel level, Roads.Grade grade, int x, int z, float height, double fromMiddle) {
        int base = Mth.floor(height);
        boolean slab = height - base >= 0.5F;
        boolean mossy = grade == Roads.Grade.HIGH && Math.floorMod(x * 734287 + z * 912931, 7) == 0;
        Block full = switch (grade) {
            case HIGH -> mossy ? Blocks.MOSSY_COBBLESTONE : Blocks.COBBLESTONE;
            case DOJO -> Blocks.COBBLESTONE;
            case CITY -> fromMiddle <= 1.0 ? Blocks.GRAVEL : Blocks.DIRT_PATH;
            case TOWN -> Blocks.DIRT_PATH;
        };
        Block half = switch (grade) {
            case HIGH -> mossy ? Blocks.MOSSY_COBBLESTONE_SLAB : Blocks.COBBLESTONE_SLAB;
            case DOJO -> Blocks.COBBLESTONE_SLAB;
            case CITY -> fromMiddle <= 1.0 ? ConquestContent.GRAVEL_SLAB.get() : ConquestContent.DIRT_PATH_SLAB.get();
            case TOWN -> ConquestContent.DIRT_PATH_SLAB.get();
        };
        fill(level, x, base - 2, z);
        level.setBlock(new BlockPos(x, base - 1, z), full.defaultBlockState(), 2);
        int clearFrom = base;
        if (slab) {
            level.setBlock(new BlockPos(x, base, z), half.defaultBlockState(), 2);
            clearFrom = base + 1;
        }
        clear(level, x, clearFrom, z, base);
    }

    /** One column of bridge: the deck at the road's height, a rail at the edges, a post down to the ground now and then. */
    private static void bridge(WorldGenLevel level, Roads.Grade grade, int x, int z, float height, boolean edge, double along) {
        int base = Mth.floor(height);
        Block deck = switch (grade) {
            case TOWN -> Blocks.OAK_PLANKS;
            case CITY -> Blocks.SPRUCE_PLANKS;
            default -> Blocks.STONE_BRICKS;
        };
        Block rail = switch (grade) {
            case TOWN -> Blocks.OAK_FENCE;
            case CITY -> Blocks.SPRUCE_FENCE;
            default -> Blocks.STONE_BRICK_WALL;
        };
        Block post = grade == Roads.Grade.TOWN ? Blocks.OAK_LOG : Blocks.STONE_BRICKS;
        level.setBlock(new BlockPos(x, base - 1, z), deck.defaultBlockState(), 2);
        clear(level, x, base, z, base);
        if (edge) {
            level.setBlock(new BlockPos(x, base, z), rail.defaultBlockState(), 2);
            if (Math.floorMod((int) Math.floor(along), POST_EVERY) == 0) {
                for (int y = base - 2; y > base - 2 - POST_DEPTH; y--) {
                    BlockPos p = new BlockPos(x, y, z);
                    BlockState here = level.getBlockState(p);
                    if (!here.isAir() && here.getFluidState().isEmpty() && !here.canBeReplaced()) {
                        break;
                    }
                    level.setBlock(p, post.defaultBlockState(), 2);
                }
            }
        }
    }

    /** Dirt under a road, down to firm ground. */
    private static void fill(WorldGenLevel level, int x, int top, int z) {
        for (int y = top; y > top - FILL; y--) {
            BlockPos p = new BlockPos(x, y, z);
            BlockState here = level.getBlockState(p);
            if (!here.isAir() && here.getFluidState().isEmpty() && !here.canBeReplaced()) {
                break;
            }
            level.setBlock(p, Blocks.DIRT.defaultBlockState(), 2);
        }
    }

    /** Everything cleared above a road for a few blocks, and trees above that. */
    private static void clear(WorldGenLevel level, int x, int from, int z, int base) {
        for (int y = from; y <= base + CLEAR_TREES; y++) {
            BlockPos p = new BlockPos(x, y, z);
            BlockState here = level.getBlockState(p);
            if (here.isAir()) {
                continue;
            }
            if (y <= base + CLEAR || here.is(BlockTags.LOGS) || here.is(BlockTags.LEAVES)) {
                level.setBlock(p, Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }

    /** The squares kept from roads near a chunk: city walls, settlements, and the ground about a dojo. */
    private static List<int[]> keeps(BaseMap map, int x0, int z0) {
        List<int[]> keeps = new java.util.ArrayList<>();
        for (SeatCities.City c : ConquestRuntime.cities().inBox(x0, z0, x0 + 15, z0 + 15)) {
            keep(keeps, c.x(), c.z(), c.size() / 2 + CITY_MARGIN, x0, z0);
        }
        for (Countryside.Settlement s : map.settlements()) {
            keep(keeps, s.x(), s.z(), s.reach() + SETTLEMENT_MARGIN, x0, z0);
        }
        for (Dojos.Dojo d : map.dojos()) {
            keep(keeps, d.x(), d.z(), DOJO_CLEAR, x0, z0);
        }
        return keeps;
    }

    private static void keep(List<int[]> keeps, int x, int z, int half, int x0, int z0) {
        if (x + half >= x0 && x - half <= x0 + 15 && z + half >= z0 && z - half <= z0 + 15) {
            keeps.add(new int[]{x - half, z - half, x + half, z + half});
        }
    }

    private static boolean kept(List<int[]> keeps, int x, int z) {
        for (int[] k : keeps) {
            if (x >= k[0] && x <= k[2] && z >= k[1] && z <= k[3]) {
                return true;
            }
        }
        return false;
    }

    private static int rank(Roads.Grade grade) {
        return switch (grade) {
            case HIGH -> 3;
            case CITY -> 2;
            case TOWN -> 1;
            case DOJO -> 0;
        };
    }

}
